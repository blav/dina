package us.actar.dina.is.is2;

import us.actar.dina.*;
import us.actar.dina.Fault;

import static us.actar.dina.is.is2.IS2InstructionGroup.FIND;

public class FindBackward extends Base {

  private final int label;

  public FindBackward (int label) {
    super (FIND);
    this.label = label;
  }

  @Override
  public void register (InstructionSet registry) {
    int opcode = Label.getOpcode (registry, label);
    registry.register (
      new Instruction (String.format ("find_backward_label%d", label), group) {
        @Override
        public void process (Machine machine, Program state) throws Fault {
          Heap heap = machine.getHeap ();
          for (int i = state.getInstructionPointer (); i >= 0; i--) {
            if (opcode == heap.get (i)) {
              FindBackward.this.getRegisters (state).push (i, machine.getRandomizer (group));
              return;
            }
          }

          throw new Fault ();
        }
      });
  }
}
