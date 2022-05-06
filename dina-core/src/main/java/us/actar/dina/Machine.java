package us.actar.dina;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import us.actar.commons.Chain;
import us.actar.commons.Chain.State;
import us.actar.commons.Disposable;
import us.actar.dina.Extension.Execute;
import us.actar.dina.Extension.Kill;
import us.actar.dina.Extension.Launch;
import us.actar.dina.Extension.Reclaim;
import us.actar.dina.InstructionSetConfig.Bootstrap;
import us.actar.dina.randomizers.RegisterRandomizer;

import static java.util.Optional.ofNullable;
import static us.actar.commons.Injector.getInstance;
import static us.actar.dina.Heap.Direction.left;
import static us.actar.dina.Heap.Direction.right;
import static us.actar.dina.randomizers.RegisterRandomizer.NOP;
import static us.actar.dina.randomizers.RegisterRandomizerConfig.createRandomizer;

public final class Machine {

  private final Heap heap;

  private final Randomizer randomizer;

  private final InstructionSet instructionSet;

  private final Map<Integer, Program> programs;

  private final Config config;

  private final Chain<Execute> execution;

  private final Chain<Reclaim> reclaim;

  private final Chain<Kill> kill;

  private final Chain<Launch> launch;

  private int programIdGenerator;

  private int cycles;

  private final RegisterRandomizer<?> ipRandomizer;

  public Machine (Config config) {
    this.config = config;
    this.heap = getInstance (Heap.FACTORY_TYPE).create (this);
    this.randomizer = getInstance (config.getRandomizer ().getName (), Randomizer.FACTORY_TYPE).create (this);
    this.programs = new HashMap<> ();
    this.ipRandomizer = createRandomizer (this, this.config.getInstructionSet ().getIpRandomizer ());
    this.instructionSet = new InstructionSet ();
    this.config.getInstructionSet ().registerInstructions (this.instructionSet);

    this.execution = new Chain<> (this::internalExecute);
    this.kill = new Chain<> (this::internalKill);
    this.launch = new Chain<> (this::internalLaunch);
    this.reclaim = new Chain<> ();

    install (getInstance (config.getReclaimer ().getName (),
      HeapReclaimer.FACTORY_TYPE).create (this));

    this.boostrap ();
  }

  public Disposable install (Extension extension) {
    List<Disposable> disposables = new ArrayList<> ();
    ofNullable (extension.getReclaimFilter ()).map (this.reclaim::install).ifPresent (disposables::add);
    ofNullable (extension.getExecuteFilter ()).map (this.execution::install).ifPresent (disposables::add);
    ofNullable (extension.getKillFilter ()).map (this.kill::install).ifPresent (disposables::add);
    ofNullable (extension.getLaunchFilter ()).map (this.launch::install).ifPresent (disposables::add);
    return () -> disposables.forEach (Disposable::dispose);
  }

  private void boostrap () {
    try {
      int size = config.getInstructionSet ().getBootstraps ().stream ()
        .map (Bootstrap::getCode).mapToInt (List::size).sum ();

      Heap.Cell first = this.heap.getFirst ();
      Heap.Cell c = first.split (right, (this.heap.size () + size) / 2);
      Heap.Cell d = first.split (right, 1);
      c.free ();

      for (Bootstrap b : config.getInstructionSet ().getBootstraps ()) {
        List<String> boostrap = b.getCode ();
        System.out.printf ("bootstrap is %d bytes\n", boostrap.size ());
        Heap.Cell cell = c.split (left, boostrap.size ());
        if (cell.getSize () != boostrap.size ())
          throw new IllegalArgumentException ();

        Heap heap = getHeap ();
        for (int i = 0; i < boostrap.size (); i++) {
          int opcode = this.instructionSet.getOpcode (boostrap.get (i));
          heap.set (cell.getOffset () + i, opcode);
        }

        Program state = new Program (null, cell, this.config.getInstructionSet ());
        state.setInstructionPointer (cell.getOffset (), NOP);
        launch (state);
      }

      d.free ();
    } catch (Fault fault) {
      throw new RuntimeException ("", fault);
    }
  }

  private void execute (Program program) {
    InstructionSetConfig<?, ?> config = this.config.getInstructionSet ();
    program.updateEnergy (config.getGainPerCycle ());
    Opcode opcode = this.instructionSet.getInstruction (this.heap.get (program.getInstructionPointer ()));
    program.incrementCycles ();
    int cost = config.getCost (opcode.getGroup ().name ());
    if (cost > program.getEnergy ()) {
      program.incrementSkipped ();
      return;
    }

    program.updateEnergy (- cost);
    this.execution.next (new Execute () {
      @Override
      public Opcode getOpcode () {
        return opcode;
      }

      @Override
      public Program getState () {
        return program;
      }

      @Override
      public Machine getMachine () {
        return Machine.this;
      }
    });
  }

  public Heap getHeap () {
    return heap;
  }

  public Randomizer getRandomizer () {
    return randomizer;
  }

  public void launch (Program state) {
    AtomicInteger result = new AtomicInteger ();
    this.launch.next (new Launch () {
      @Override
      public Program getProgram () {
        return state;
      }

      @Override
      public int getLaunchedId () {
        return result.get ();
      }

      @Override
      public void setLaunchedId (int id) {
        result.set (id);
      }

      @Override
      public Machine getMachine () {
        return Machine.this;
      }
    });

    result.get ();
  }

  public Config getConfig () {
    return config;
  }

  public void kill (int pid) {
    this.kill.next (new Kill () {
      @Override
      public int getProgram () {
        return pid;
      }

      @Override
      public Machine getMachine () {
        return Machine.this;
      }
    });
  }

  public Collection<Program> getPrograms () {
    return new ArrayList<> (this.programs.values ());
  }

  public Program getProgram (int pid) {
    return this.programs.get (pid);
  }

  public RegisterRandomizer<?> getRandomizer (InstructionGroup instructionGroup) {
    return this.randomizer.getRegisterRandomizer (instructionGroup);
  }

  public int getCycles () {
    return cycles;
  }

  public InstructionSet getInstructionSet () {
    return instructionSet;
  }

  public void update () {
    this.cycles ++;
    // copy state list since it can be altered while in the loop
    new ArrayList<> (this.programs.values ()).forEach (this::execute);

    ArrayList<Program> programs = new ArrayList<> ();
    reclaim.next (new Reclaim () {
      @Override
      public Machine getMachine () {
        return Machine.this;
      }

      @Override
      public List<Program> getReclaimList () {
        return programs;
      }
    });

    for (Program program : programs) {
      program.getCell ().bytes ().forEach (i -> getHeap ().set (i, 0));
      if (program.getChild () != null)
        program.getChild ().bytes ().forEach (i -> getHeap ().set (i, 0));

      kill (program.getId ());
    }
  }

  private void internalExecute (State<Execute> chain, Execute execute) {
    Program state = execute.getState ();
    try {
      state.setInstructionPointer (state.getInstructionPointer (), this.ipRandomizer);
      execute.getOpcode ().getInstruction ().process (execute.getMachine (), state);
      chain.next (execute);
    } catch (Fault fault) {
      state.incrementFaults ();
    }
  }

  private void internalLaunch (State<Launch> chain, Launch launch) {
    Program state = launch.getProgram ();
    if (state == null)
      throw new NullPointerException ();

    int pid = this.programIdGenerator++;
    this.programs.put (pid, state);
    state.setId (pid);
    launch.setLaunchedId (pid);
    chain.next (launch);
  }

  private void internalKill (State<Kill> chain, Kill kill) {
    int pid = kill.getProgram ();
    Program p = ofNullable (this.programs.remove (pid))
      .orElseThrow (() -> new IllegalStateException ("no such pid " + pid));

    try {
      p.getCell ().free ();
      if (p.getChild () != null)
        p.getChild ().free ();

      chain.next (kill);
    } catch (Fault fault) {
      throw new RuntimeException (fault);
    }
  }
}
