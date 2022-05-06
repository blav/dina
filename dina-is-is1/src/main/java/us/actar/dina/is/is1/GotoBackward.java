package us.actar.dina.is.is1;

import us.actar.dina.*;

public class GotoBackward extends Base {

  private final int label;

  public GotoBackward (InstructionGroup randomizer, int label) {
    super (randomizer);
    this.label = label;
  }


  @Override
  public void register (InstructionSet registry) {
    registry.register (
      new Instruction (String.format ("go_backward_to_label%d", label), group) {
        @Override
        public void process (Machine machine, Program state) throws Fault {
          Heap heap = machine.getHeap ();
          int opcode = Label.getOpcode (registry, label);
          for (int offset = state.getInstructionPointer (); offset >= 0; offset--) {
            if (heap.get (offset) == opcode) {
              state.setInstructionPointer (offset, machine.getRandomizer (group));
              return;
            }
          }

          throw new Fault ();
        }
      });
  }
}
