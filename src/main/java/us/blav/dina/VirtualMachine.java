package us.blav.dina;

import us.blav.dina.InstructionRegistry.RegisteredInstruction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;
import static us.blav.dina.Injection.getInstance;
import static us.blav.dina.Injection.getInstanceSet;
import static us.blav.dina.MemoryHeap.Direction.right;

public class VirtualMachine {

  private final MemoryHeap heap;

  private final InstructionProcessor processor;

  private final Randomizer randomizer;

  private final InstructionRegistry registry;

  private final Map<Long, ProgramState> programStates;

  private final FaultHandler faultHandler;

  private final Config config;

  private long programIdGenerator;

  public VirtualMachine (Config config) {
    this.config = config;
    this.heap = getInstance (MemoryHeap.FACTORY_TYPE).create (config);
    this.randomizer = getInstance (config.getRandomizer ().getName (), Randomizer.FACTORY_TYPE).create (config);
    this.programStates = new HashMap<> ();

    String instructionSet = config.getInstructionSet ();
    this.processor = getInstance (instructionSet, InstructionProcessor.class);
    this.registry = new InstructionRegistry (getInstanceSet (instructionSet, InstructionFactory.class));
    this.faultHandler = getInstance (instructionSet, FaultHandler.class);

    this.boostrap ();
  }

  public void boostrap () {
    List<String> boostrap = config.getBootstrapCode ();
    try {
      int size = boostrap.size ();
      EnergyTracker tracker = amount -> {
      };

      MemoryHeap.Cell c = this.heap.getFirst ().split (right, (this.heap.size () - size) / 2, tracker);
      MemoryHeap.Cell cell = this.heap.getFirst ().split (right, size, tracker);
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

  public void process (ProgramState state) {
    int ip = state.getInstructionPointer ();
    int opcode = this.heap.get (ip);
    RegisteredInstruction i = this.registry.getInstruction (this, opcode);
    try {
      try {
        i.getInstruction ().process (this, state);
      } catch (Fault fault) {
        faultHandler.handleFault (fault, state);
      }
    } finally {
      System.out.printf ("%08d %-30s ip=%08d  regs=%s\n",
        state.getId (),
        i.getSymbol (),
        state.getInstructionPointer (),
        IntStream.of (state.getRegisters ()).boxed ().map (r -> format ("%012d", r)).collect (joining (" "))
      );
    }
  }

  public InstructionProcessor getProcessor () {
    return processor;
  }

  public MemoryHeap getHeap () {
    return heap;
  }

  public Randomizer getRandomizer () {
    return randomizer;
  }

  public InstructionRegistry getRegistry () {
    return registry;
  }

  public long launch (ProgramState state) {
    if (state == null)
      throw new NullPointerException ();

    long pid = this.programIdGenerator ++;
    this.programStates.put (pid, state);
    state.setId (pid);
    return pid;
  }

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
    for (int i = 0; i < cell.getSize (); i ++) {
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
