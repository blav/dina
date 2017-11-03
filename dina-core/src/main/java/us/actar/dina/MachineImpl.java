package us.actar.dina;

import com.google.inject.TypeLiteral;
import us.actar.commons.Chain.Handle;
import us.actar.dina.Config.Bootstrap;
import us.actar.dina.randomizers.RegisterRandomizer;
import us.actar.commons.Chain;
import us.actar.commons.Chain.Filter;

import java.util.*;

import static java.util.Optional.ofNullable;
import static us.actar.commons.Injector.getInstance;
import static us.actar.dina.Heap.Direction.left;
import static us.actar.dina.Heap.Direction.right;
import static us.actar.dina.randomizers.RegisterRandomizerConfig.createRandomizer;

public class MachineImpl implements Machine {

  private final Heap heap;

  private final Randomizer randomizer;

  private final InstructionRegistry registry;

  private final Map<Integer, ProgramState> programStates;

  private final Config config;

  private final Chain<ExecutionStep> execution;

  private final Chain<Reclaim> reclaim;

  private int programIdGenerator;

  private RegisterRandomizer<?> ipRandomizer;

  public MachineImpl (Config config) {
    this.config = config;
    this.heap = getInstance (Heap.FACTORY_TYPE).create (this);
    this.randomizer = getInstance (config.getRandomizer ().getName (), Randomizer.FACTORY_TYPE).create (this);
    this.programStates = new HashMap<> ();
    this.execution = new Chain<> (ExecutionStep.FILTER_TYPE);
    this.ipRandomizer = createRandomizer (this, this.config.getInstructionSet ().getIpRandomizer ());
    this.execution.install ((chain, step) -> {
      ProgramState state = step.getState ();
      try {
        state.setInstructionPointer (state.getInstructionPointer (), this.ipRandomizer);
        step.getOpcode ().getInstruction ().process (step.getMachine (), state);
      } catch (Fault fault) {
        state.incrementFaults ();
      }
    });

    this.execution.installAll (config.getExecutionFilters ());

    this.registry = new InstructionRegistry ();
    this.config.getInstructionSet ().registerInstructions (this.registry);
    this.boostrap ();

    HeapReclaimer reclaimer = getInstance (config.getReclaimer ().getName (),
      HeapReclaimer.FACTORY_TYPE).create (this);

    this.reclaim = new Chain<> (Reclaim.FILTER_TYPE);
    this.reclaim.install (((chain, reclaim) -> {
      reclaim.getReclaimList ().addAll (reclaimer.reclaim (reclaim.getMachine ()));
    }));

    this.reclaim.installAll (config.getReclaimerFilters ());

    if (reclaimer instanceof MachineFilters)
      install ((MachineFilters) reclaimer);
  }

  @Override
  public Handle install (MachineFilters mf) {
    List<Handle> handles = new ArrayList<> ();
    ofNullable (mf.getReclaimFilter ()).map (this.reclaim::install).ifPresent (handles::add);
    ofNullable (mf.getExecutionFilter ()).map (this.execution::install).ifPresent (handles::add);
    return () -> handles.forEach (Handle::uninstall);
  }

  private void boostrap () {
    try {
      int size = config.getBootstraps ().stream ()
        .map (Bootstrap::getCode).map (List::size).reduce (0, (s, i) -> s + i);

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

  private void process (ProgramState state) {
    Opcode opcode = this.registry.getInstruction (this.heap.get (state.getInstructionPointer ()));
    state.incrementCycles ();
    execution.next (new ExecutionStep () {
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
        return MachineImpl.this;
      }
    });
  }

  @Override
  public Heap getHeap () {
    return heap;
  }

  @Override
  public Randomizer getRandomizer () {
    return randomizer;
  }

  @Override
  public long launch (ProgramState state) {
    if (state == null)
      throw new NullPointerException ();

    int pid = this.programIdGenerator++;
    this.programStates.put (pid, state);
    state.setId (pid);
    return pid;
  }

  @Override
  public Config getConfig () {
    return config;
  }

  @Override
  public void kill (int pid) {
    ProgramState p = ofNullable (this.programStates.remove (pid))
      .orElseThrow (() -> new IllegalStateException ("no such pid " + pid));

    try {
      p.getCell ().free ();
      if (p.getChild () != null)
        p.getChild ().free ();
    } catch (Fault fault) {
      throw new RuntimeException (fault);
    }
  }

  @Override
  public Collection<Program> getPrograms () {
    return new ArrayList<> (programStates.values ());
  }

  @Override
  public ProgramState getProgram (int pid) {
    return programStates.get (pid);
  }

  @Override
  public RegisterRandomizer<?> getRandomizer (RegisterRandomizer.Name name) {
    return randomizer.getRegisterRandomizer (name);
  }

  @Override
  public InstructionRegistry getRegistry () {
    return registry;
  }

  @Override
  public <TYPE> Handle install (TypeLiteral<Filter<TYPE>> type, Filter<TYPE> filter) {
    if (type.equals (ExecutionStep.FILTER_TYPE)) {
      return execution.install ((Filter<ExecutionStep>) filter);
    } else if (type.equals (Reclaim.FILTER_TYPE)) {
      return reclaim.install ((Filter<Reclaim>) filter);
    } else {
      throw new IllegalArgumentException ("" + type);
    }
  }

  @Override
  public void update () {
    // copy state list since it can be altered while in the loop
    new ArrayList<> (programStates.values ()).forEach (this::process);

    ArrayList<Program> programs = new ArrayList<> ();
    reclaim.next (new Reclaim () {
      @Override
      public Machine getMachine () {
        return MachineImpl.this;
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
}
