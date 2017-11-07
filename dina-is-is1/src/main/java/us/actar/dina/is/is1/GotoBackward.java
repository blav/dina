package us.actar.dina.is.is1;

import us.actar.dina.Fault;
import us.actar.dina.Heap;
import us.actar.dina.InstructionSet;
import us.actar.dina.randomizers.RegisterRandomizer;

public class GotoBackward extends Base {

  private final int label;

  public GotoBackward (RegisterRandomizer.Name randomizer, int label) {
    super (randomizer);
    this.label = label;
  }


  @Override
  public void register (InstructionSet registry) {
    registry.register (
      String.format ("go_backward_to_label%d", label),
      (machine, state) -> {
        Heap heap = machine.getHeap ();
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
