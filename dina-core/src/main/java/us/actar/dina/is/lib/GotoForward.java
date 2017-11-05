package us.actar.dina.is.lib;

import us.actar.dina.Fault;
import us.actar.dina.Heap;
import us.actar.dina.InstructionRegistry;
import us.actar.dina.randomizers.RegisterRandomizer;

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
        Heap heap = machine.getHeap ();
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
