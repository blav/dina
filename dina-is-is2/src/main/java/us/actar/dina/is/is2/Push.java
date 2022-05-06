package us.actar.dina.is.is2;

import us.actar.dina.Fault;
import us.actar.dina.Instruction;
import us.actar.dina.InstructionSet;
import us.actar.dina.Machine;
import us.actar.dina.Program;
import us.actar.dina.is.is2.IS2Registers.Register;

import static us.actar.dina.is.is2.IS2InstructionGroup.PUSH;
import static us.actar.dina.randomizers.RegisterRandomizer.NOP;

public class Push extends Base {

  private final Register register;

  public Push (Register register) {
    super (PUSH);
    this.register = register;
  }

  public void register (InstructionSet registry) {
    registry.register (
      new Instruction (String.format ("push_%s", register), group) {
        @Override
        public void process (Machine machine, Program state) throws Fault {
          IS2Registers registers = Push.this.getRegisters (state);
          registers.push (registers.get (register, NOP), machine.getRandomizer (group));
        }
      });
  }
}
