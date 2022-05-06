package us.actar.dina.is.is2;

import us.actar.dina.Fault;
import us.actar.dina.*;

import static us.actar.dina.is.is2.IS2InstructionGroup.GOTO;

public class GotoForward extends Base {

  private final int label;

  public GotoForward (int label) {
    super (GOTO);
    this.label = label;
  }

  @Override
  public void register (InstructionSet registry) {
    registry.register (
      new Instruction (String.format ("go_forward_to_label%d", label), group) {
        @Override
        public void process (Machine machine, Program state) throws Fault {
          Heap heap = machine.getHeap ();
          int opcode = Label.getOpcode (registry, label);
          for (int offset = state.getInstructionPointer (); offset < heap.size (); offset++) {
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
