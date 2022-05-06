package us.actar.dina.is.is2;

import us.actar.dina.Fault;
import us.actar.dina.Instruction;
import us.actar.dina.InstructionSet;
import us.actar.dina.Machine;
import us.actar.dina.Program;
import us.actar.dina.is.is2.IS2Registers.Register;

import static us.actar.dina.is.is2.IS2InstructionGroup.INCREMENT;
import static us.actar.dina.randomizers.RegisterRandomizer.NOP;

public class IncrementR extends Base {

  private final Register register;

  public IncrementR (Register register) {
    super (INCREMENT);
    this.register = register;
  }

  @Override
  public void register (InstructionSet registry) {
    registry.register (
      new Instruction (String.format ("increment_%s", register.name ()), group) {
        @Override
        public void process (Machine machine, Program state) throws Fault {
          IS2Registers registers = IncrementR.this.getRegisters (state);
          registers.set (register, registers.get (register, NOP) + 1, machine.getRandomizer (group));
        }
      }
    );
  }
}
