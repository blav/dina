package us.blav.dina.is.lib;

import us.blav.dina.*;
import us.blav.dina.randomizers.RegisterRandomizer;

public class GotoForward extends Base {

  private final int label;

  public GotoForward (RegisterRandomizer.Name randomizer, int label) {
    super (randomizer);
    this.label = label;
  }

  @Override
  public void register (InstructionRegistry registry) {
    registry.register (
      String.format ("go_forward_to_label%d", label),
      (machine, state) -> {
        MemoryHeap heap = machine.getHeap ();
        int opcode = Label.getOpcode (registry, label);
        for (int offset = state.getInstructionPointer (); offset < heap.size (); offset++) {
          if (heap.get (offset) == opcode) {
            state.setInstructionPointer (offset, machine.getRandomizer (randomizer));
            return;
          }
        }

        throw new Fault ();
      });
  }
}
