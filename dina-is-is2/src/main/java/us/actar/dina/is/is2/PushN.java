package us.actar.dina.is.is2;

import us.actar.dina.Fault;
import us.actar.dina.Instruction;
import us.actar.dina.InstructionSet;
import us.actar.dina.Machine;
import us.actar.dina.Program;

import static us.actar.dina.is.is2.IS2InstructionGroup.PUSH;

public class PushN extends Base {

  private final int value;

  public PushN (int value) {
    super (PUSH);
    this.value = value;
  }

  public void register (InstructionSet registry) {
    registry.register (
      new Instruction (String.format ("push_%d", value), group) {
        @Override
        public void process (Machine machine, Program state) throws Fault {
          PushN.this.getRegisters (state).push (value, machine.getRandomizer (group));
        }
      });
  }
}
