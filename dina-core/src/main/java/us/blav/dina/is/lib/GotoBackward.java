package us.blav.dina.is.lib;

import us.blav.dina.Fault;
import us.blav.dina.InstructionRegistry;
import us.blav.dina.MemoryHeap;
import us.blav.dina.randomizers.RegisterRandomizer;

public class GotoBackward extends Base {

  private final int label;

  public GotoBackward (RegisterRandomizer.Name randomizer, int label) {
    super (randomizer);
    this.label = label;
  }


  @Override
  public void register (InstructionRegistry registry) {
    registry.register (
      String.format ("go_backward_to_label%d", label),
      (machine, state) -> {
        MemoryHeap heap = machine.getHeap ();
        int opcode = Label.getOpcode (registry, label);
        for (int offset = state.getInstructionPointer (); offset >= 0; offset--) {
          if (heap.get (offset) == opcode) {
            state.setInstructionPointer (offset, machine.getRandomizer (randomizer));
            return;
          }
        }

        throw new Fault ();
      });
  }
}
