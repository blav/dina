package us.actar.dina.is.is2;

import us.actar.dina.*;
import us.actar.dina.Fault;

import static us.actar.dina.is.is2.IS2InstructionGroup.FIND;

public class FindForward extends Base {

  private final int label;

  public FindForward (int label) {
    super (FIND);
    this.label = label;
  }

  @Override
  public void register (InstructionSet registry) {
    int opcode = Label.getOpcode (registry, label);
    registry.register (
      new Instruction (String.format ("find_forward_label%d", label), group) {
        @Override
        public void process (Machine machine, Program state) throws Fault {
          Heap heap = machine.getHeap ();
          for (int i = state.getInstructionPointer (); i < heap.size (); i++) {
            if (opcode == heap.get (i)) {
              FindForward.this.getRegisters (state).push (i, machine.getRandomizer (group));
              return;
            }
          }

          throw new Fault ();
        }
      });
  }
}
