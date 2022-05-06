package us.actar.dina.is.is1;

import us.actar.dina.*;

public class FindBackward extends Base {

  private final int label;

  private final int register;

  public FindBackward (InstructionGroup randomizer, int label, int register) {
    super (randomizer);
    this.label = label;
    this.register = register;
  }

  @Override
  public void register (InstructionSet registry) {
    int opcode = Label.getOpcode (registry, label);
    registry.register (
      new Instruction (String.format ("find_backward_label%d_into_r%d", label, register), group) {
        @Override
        public void process (Machine machine, Program state) throws Fault {
          Heap heap = machine.getHeap ();
          for (int i = state.getInstructionPointer (); i >= 0; i--) {
            if (opcode == heap.get (i)) {
              FindBackward.this.getRegisters (state).set (register, i, machine.getRandomizer (group));
              return;
            }
          }

          throw new Fault ();
        }
      });
  }
}
