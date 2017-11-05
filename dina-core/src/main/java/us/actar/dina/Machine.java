package us.actar.dina;

import us.actar.commons.Chain;
import us.actar.commons.Chain.Handle;
import us.actar.commons.Chain.State;
import us.actar.dina.Config.Bootstrap;
import us.actar.dina.Extension.Execute;
import us.actar.dina.Extension.Kill;
import us.actar.dina.Extension.Launch;
import us.actar.dina.randomizers.RegisterRandomizer;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Optional.ofNullable;
import static us.actar.commons.Injector.getInstance;
import static us.actar.dina.Heap.Direction.left;
import static us.actar.dina.Heap.Direction.right;
import static us.actar.dina.randomizers.RegisterRandomizerConfig.createRandomizer;

public final class Machine {

  private final Heap heap;

  private final Randomizer randomizer;

  private final InstructionRegistry registry;

  private final Map<Integer, ProgramState> programStates;

  private final Config config;

  private final Chain<Execute> execution;

  private final Chain<Extension.Reclaim> reclaim;

  private final Chain<Kill> kill;

  private final Chain<Launch> launch;

  private int programIdGenerator;

  private RegisterRandomizer<?> ipRandomizer;

  public Machine (Config config) {
    this.config = config;
    this.heap = getInstance (Heap.FACTORY_TYPE).create (this);
    this.randomizer = getInstance (config.getRandomizer ().getName (), Randomizer.FACTORY_TYPE).create (this);
    this.programStates = new HashMap<> ();
    this.ipRandomizer = createRandomizer (this, this.config.getInstructionSet ().getIpRandomizer ());
    this.registry = new InstructionRegistry ();
    this.config.getInstructionSet ().registerInstructions (this.registry);

    this.execution = new Chain<> ();
    this.execution.install (this::internalExecute);

    this.kill = new Chain<> ();
    this.kill.install (this::internalKill);

    this.launch = new Chain<> ();
    this.launch.install (this::internalLaunch);

    this.reclaim = new Chain<> ();
    install (getInstance (config.getReclaimer ().getName (),
      HeapReclaimer.FACTORY_TYPE).create (this));

    this.boostrap ();
  }

  public Handle install (Extension extension) {
    List<Handle> handles = new ArrayList<> ();
    ofNullable (extension.getReclaimFilter ()).map (this.reclaim::install).ifPresent (handles::add);
    ofNullable (extension.getExecuteFilter ()).map (this.execution::install).ifPresent (handles::add);
    ofNullable (extension.getKillFilter ()).map (this.kill::install).ifPresent (handles::add);
    ofNullable (extension.getLaunchFilter ()).map (this.launch::install).ifPresent (handles::add);
    return () -> handles.forEach (Handle::uninstall);
  }

  private void boostrap () {
    try {
      int size = config.getBootstraps ().stream ()
        .map (Bootstrap::getCode).mapToInt (List::size).sum ();

      Heap.Cell first = this.heap.getFirst ();
      Heap.Cell c = first.split (right, (this.heap.size () + size) / 2);
      Heap.Cell d = first.split (right, 1);
      c.free ();

      for (Bootstrap b : config.getBootstraps ()) {
        List<String> boostrap = b.getCode ();
        System.out.printf ("bootstrap is %d bytes\n", boostrap.size ());
        Heap.Cell cell = c.split (left, boostrap.size ());
        if (cell.getSize () != boostrap.size ())
          throw new IllegalArgumentException ();

        Heap heap = getHeap ();
        for (int i = 0; i < boostrap.size (); i++) {
          int opcode = registry.getOpcode (boostrap.get (i));
          heap.set (cell.getOffset () + i, opcode);
        }

        ProgramState state = new ProgramState (cell, config.getInstructionSet ().getRegisters ());
        state.setInstructionPointer (cell.getOffset (), RegisterRandomizer.NOP);
        launch (state);
      }

      d.free ();
    } catch (Fault fault) {
      throw new RuntimeException ("", fault);
    }
  }

  private void execute (ProgramState state) {
    Opcode opcode = this.registry.getInstruction (this.heap.get (state.getInstructionPointer ()));
    state.incrementCycles ();
    execution.next (new Execute () {
      @Override
      public Opcode getOpcode () {
        return opcode;
      }

      @Override
      public ProgramState getState () {
        return state;
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

  public int launch (ProgramState state) {
    AtomicInteger result = new AtomicInteger ();
    launch.next (new Launch () {
      @Override
      public ProgramState getProgram () {
        return state;
      }

      @Override
      public void setLaunchedId (int id) {
        result.set (id);
      }

      @Override
      public int getLaunchedId () {
        return result.get ();
      }

      @Override
      public Machine getMachine () {
        return Machine.this;
      }
    });

    return result.get ();
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
    return new ArrayList<> (programStates.values ());
  }

  public ProgramState getProgram (int pid) {
    return programStates.get (pid);
  }

  public RegisterRandomizer<?> getRandomizer (RegisterRandomizer.Name name) {
    return randomizer.getRegisterRandomizer (name);
  }

  public InstructionRegistry getRegistry () {
    return registry;
  }

  public void update () {
    // copy state list since it can be altered while in the loop
    new ArrayList<> (programStates.values ()).forEach (this::execute);

    ArrayList<Program> programs = new ArrayList<> ();
    reclaim.next (new Extension.Reclaim () {
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
    ProgramState state = execute.getState ();
    try {
      state.setInstructionPointer (state.getInstructionPointer (), this.ipRandomizer);
      execute.getOpcode ().getInstruction ().process (execute.getMachine (), state);
      chain.next (execute);
    } catch (Fault fault) {
      state.incrementFaults ();
    }
  }

  private void internalLaunch (State<Launch> chain, Launch launch) {
    ProgramState state = launch.getProgram ();
    if (state == null)
      throw new NullPointerException ();

    int pid = this.programIdGenerator++;
    this.programStates.put (pid, state);
    state.setId (pid);
    launch.setLaunchedId (pid);
    chain.next (launch);
  }

  private void internalKill (State<Kill> chain, Kill kill) {
    int pid = kill.getProgram ();
    ProgramState p = ofNullable (this.programStates.remove (pid))
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
