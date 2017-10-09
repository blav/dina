package us.blav.dina;

import java.io.IOException;
import java.util.*;

import static us.blav.dina.Injection.getInstance;
import static us.blav.dina.Injection.getInstanceMap;
import static us.blav.dina.Injection.getInstanceSet;
import static us.blav.dina.MemoryHeap.Direction.right;

public class VirtualMachineImpl implements VirtualMachine {

  private final MemoryHeap heap;

  private final InstructionProcessor processor;

  private final Randomizer randomizer;

  private final InstructionRegistry registry;

  private final Map<Long, ProgramState> programStates;

  private final FaultHandler faultHandler;

  private final Config config;

  private final ExecutionChainImpl execution;

  private long programIdGenerator;

  public VirtualMachineImpl (Config config) {
    this.config = config;
    this.heap = getInstance (MemoryHeap.FACTORY_TYPE).create (config);
    this.randomizer = getInstance (config.getRandomizer ().getName (), Randomizer.FACTORY_TYPE).create (config);
    this.programStates = new HashMap<> ();
    this.execution = new ExecutionChainImpl (this);

    Map<String, ExecutionFilter> filterMap = getInstanceMap (String.class, ExecutionFilter.class);
    for (String filter : config.getExecutionFilters ()) {
      ExecutionFilter f = filterMap.get (filter);
      if (f == null) {
        throw new NoSuchElementException (filter);
      } else {
        this.execution.install (f);
      }
    }

    String instructionSet = config.getInstructionSet ();
    this.processor = getInstance (instructionSet, InstructionProcessor.class);
    this.registry = new InstructionRegistry (getInstanceSet (instructionSet, InstructionFactory.class));
    this.faultHandler = getInstance (instructionSet, FaultHandler.class);

    this.boostrap ();
  }

  private void boostrap () {
    List<String> boostrap = config.getBootstrapCode ();
    try {
      int size = boostrap.size ();
      EnergyTracker tracker = amount -> {
      };
      MemoryHeap.Cell first = this.heap.getFirst ();
      MemoryHeap.Cell c = first.split (right, (this.heap.size () - size) / 2, tracker);
      MemoryHeap.Cell cell = first.split (right, size, tracker);

      c.free (tracker);
      if (cell.getSize () != boostrap.size ())
        throw new IllegalArgumentException ();

      MemoryHeap heap = getHeap ();
      for (int i = 0; i < boostrap.size (); i++) {
        int opcode = registry.getOpcode (boostrap.get (i));
        heap.set (cell.getOffset () + i, opcode);
      }

      ProgramState state = processor.newProgram (cell);
      state.setInstructionPointer (cell.getOffset ());
      launch (state);
    } catch (Fault fault) {
      throw new RuntimeException ("", fault);
    }
  }

  private void process (ProgramState state) {
    execution.next (this, state,
      this.registry.getInstruction (this, this.heap.get (state.getInstructionPointer ())));
  }

  @Override
  public InstructionProcessor getProcessor () {
    return processor;
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
  public FaultHandler getFaultHandler () {
    return faultHandler;
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
  public void kill (long pid) {
    if (this.programStates.remove (pid) == null)
      throw new IllegalStateException ("no such pid " + pid);
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
      out.append (String.format ("%08d %-30s\n", offset, registry.getInstruction (this, heap.get (offset)).getSymbol ()));
    }

    return out;
  }

  public void start () {
    while (true) {
      // copy state list since it can be altered while in the loop
      for (Map.Entry<Long, ProgramState> state : new ArrayList<> (programStates.entrySet ()))
        process (state.getValue ());
    }
  }
}
