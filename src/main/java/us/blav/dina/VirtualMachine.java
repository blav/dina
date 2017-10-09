package us.blav.dina;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static us.blav.dina.Injection.getInstance;
import static us.blav.dina.MemoryHeap.Direction.right;

public class VirtualMachine {

  private final MemoryHeap heap;

  private final InstructionProcessor processor;

  private final Randomizer randomizer;

  private final Map<Long, ProgramState> programStates;

  private long programIdGenerator;

  public VirtualMachine (Config config) {
    this.heap = getInstance (MemoryHeap.FACTORY_TYPE).create (config);
    this.processor = getInstance (config.getInstructionSet (), InstructionProcessor.class);
    this.randomizer = getInstance (config.getRandomizer ().getName (), Randomizer.FACTORY_TYPE).create (config);
    this.programStates = new HashMap<> ();
    try {
      List<String> bootstrap = config.getBootstrapCode ();
      int size = bootstrap.size ();
      EnergyTracker tracker = amount -> {
      };

      MemoryHeap.Cell c = this.heap.getFirst ().split (right, (this.heap.size () - size) / 2, tracker);
      MemoryHeap.Cell initial = this.heap.getFirst ().split (right, size, tracker);
      c.free (tracker);
      this.processor.launch (this, initial, bootstrap);
    } catch (Fault fault) {
      throw new RuntimeException ("", fault);
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

  public long launch (ProgramState state) {
    if (state == null)
      throw new NullPointerException ();

    long pid = this.programIdGenerator ++;
    this.programStates.put (pid, state);
    return pid;
  }

  public void kill (long pid) {
    if (this.programStates.remove (pid) == null)
      throw new IllegalStateException ("no such pid " + pid);
  }

  public void start () {
    while (true) {
      // copy state list since it can be altered while in the loop
      for (ProgramState state : new ArrayList<> (programStates.values ())) {
        processor.process (this, state);
      }
    }
  }
}
