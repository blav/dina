package us.blav.dina;

import us.blav.commons.Chain;
import us.blav.dina.randomizers.RegisterRandomizer;

import java.io.IOException;
import java.util.*;

import static us.blav.commons.Injector.getInstance;
import static us.blav.dina.MemoryHeap.Direction.right;
import static us.blav.dina.randomizers.RegisterRandomizer.NOP;

public class VirtualMachineImpl implements VirtualMachine {

  private final MemoryHeap heap;

  private final Randomizer randomizer;

  private final InstructionRegistry registry;

  private final Map<Long, ProgramState> programStates;

  private final Config config;

  private final Chain<ExecutionStep> execution;

  private final Chain<Reclaim> reclaim;

  private long programIdGenerator;

  private EnergyTracker tracker;

  public VirtualMachineImpl (Config config) {
    this.config = config;
    this.heap = getInstance (MemoryHeap.FACTORY_TYPE).create (this);
    this.randomizer = getInstance (config.getRandomizer ().getName (), Randomizer.FACTORY_TYPE).create (this);
    this.programStates = new HashMap<> ();
    this.execution = new Chain<> (ExecutionStep.FILTER_TYPE);
    this.execution.install ((chain, step) -> {
      try {
        step.getOpcode ().getInstruction ().process (step.getMachine (), step.getState ());
      } catch (Fault fault) {
        step.getState ().incrementFaults ();
      }
    });

    this.execution.installAll (config.getExecutionFilters ());

    HeapReclaimer reclaimer = getInstance (config.getReclaimer ().getName (), HeapReclaimer.FACTORY_TYPE).create (this);
    this.reclaim = new Chain<> (HeapReclaimer.FILTER_TYPE);
    this.reclaim.install (((chain, reclaim) -> {
      reclaim.getReclaimList ().addAll (reclaimer.reclaim (reclaim.getMachine ()));
    }));

    this.reclaim.installAll (config.getReclaimerFilters ());

    this.tracker = amount -> {
    };

    this.registry = new InstructionRegistry ();
    this.config.getInstructionSet ().registerInstructions (this.registry);

    this.boostrap ();
  }

  private void boostrap () {
    List<String> boostrap = config.getBootstrapCode ();
    System.out.printf ("bootstrap is %d bytes\n", new HashSet<> (boostrap).size ());
    try {
      int size = boostrap.size ();
      MemoryHeap.Cell first = this.heap.getFirst ();
      MemoryHeap.Cell c = first.split (right, (this.heap.size () - size) / 2, this.tracker);
      MemoryHeap.Cell cell = first.split (right, size, this.tracker);

      c.free (this.tracker);
      if (cell.getSize () != boostrap.size ())
        throw new IllegalArgumentException ();

      MemoryHeap heap = getHeap ();
      for (int i = 0; i < boostrap.size (); i++) {
        int opcode = registry.getOpcode (boostrap.get (i));
        heap.set (cell.getOffset () + i, opcode);
      }

      ProgramState state = new ProgramState (cell, config.getInstructionSet ().getRegisters ());
      ;
      state.setInstructionPointer (cell.getOffset (), NOP);
      launch (state);
    } catch (Fault fault) {
      throw new RuntimeException ("", fault);
    }
  }

  private void process (ProgramState state) {
    Opcode opcode = this.registry.getInstruction (this.heap.get (state.getInstructionPointer ()));
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
      public VirtualMachine getMachine () {
        return VirtualMachineImpl.this;
      }
    });
  }

  @Override
  public MemoryHeap getHeap () {
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

    long pid = this.programIdGenerator++;
    this.programStates.put (pid, state);
    state.setId (pid);
    return pid;
  }

  @Override
  public Config getConfig () {
    return config;
  }

  @Override
  public void kill (long pid) {
    ProgramState p = this.programStates.remove (pid);
    if (p == null)
      throw new IllegalStateException ("no such pid " + pid);

    try {
      p.getCell ().free (this.tracker);
      if (p.getChild () != null)
        p.getChild ().free (this.tracker);
    } catch (Fault fault) {
      throw new RuntimeException (fault);
    }
  }

  @Override
  public Collection<Program> getPrograms () {
    return new ArrayList<> (programStates.values ());
  }

  @Override
  public RegisterRandomizer<?> getRandomizer (RegisterRandomizer.Name name) {
    return randomizer.getRegisterRandomizer (name);
  }

  public <A extends Appendable> A dump (long pid, A out) throws IOException {
    ProgramState state = programStates.get (pid);
    if (state == null) {
      out.append (String.format ("no such program %d\n", pid));
      return out;
    }

    MemoryHeap.Cell cell = state.getCell ();
    for (int i = 0; i < cell.getSize (); i++) {
      int offset = cell.getOffset () + i;
      out.append (String.format ("%08d %-30s\n", offset, registry.getInstruction (heap.get (offset)).getSymbol ()));
    }

    return out;
  }

  public void start () {
    while (true) {
      // copy state list since it can be altered while in the loop
      for (Map.Entry<Long, ProgramState> state : new ArrayList<> (programStates.entrySet ()))
        process (state.getValue ());

      ArrayList<Program> programs = new ArrayList<> ();
      reclaim.next (new Reclaim () {
        @Override
        public VirtualMachine getMachine () {
          return VirtualMachineImpl.this;
        }

        @Override
        public List<Program> getReclaimList () {
          return programs;
        }
      });

      for (Program program : programs)
        kill (program.getId ());
    }
  }
}
