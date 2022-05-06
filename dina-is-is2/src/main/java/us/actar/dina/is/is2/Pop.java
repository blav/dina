package us.actar.dina.is.is2;

import us.actar.dina.Fault;
import us.actar.dina.Instruction;
import us.actar.dina.InstructionSet;
import us.actar.dina.Machine;
import us.actar.dina.Program;
import us.actar.dina.is.is2.IS2Registers.Register;

import static us.actar.dina.is.is2.IS2InstructionGroup.POP;
import static us.actar.dina.randomizers.RegisterRandomizer.NOP;

public class Pop extends Base {

  private final Register register;

  public Pop (Register register) {
    super (POP);
    this.register = register;
  }

  public void register (InstructionSet registry) {
    registry.register (
      new Instruction (String.format ("pop_%s", register), group) {
        @Override
        public void process (Machine machine, Program state) throws Fault {
          IS2Registers registers = Pop.this.getRegisters (state);
          registers.set (register, registers.pop (NOP), machine.getRandomizer (group));
        }
      });
  }
}
