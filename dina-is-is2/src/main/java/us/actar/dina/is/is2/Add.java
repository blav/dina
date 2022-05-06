package us.actar.dina.is.is2;

import us.actar.dina.Fault;
import us.actar.dina.Instruction;
import us.actar.dina.InstructionSet;
import us.actar.dina.Machine;
import us.actar.dina.Program;

import static us.actar.dina.is.is2.IS2InstructionGroup.ADD;
import static us.actar.dina.randomizers.RegisterRandomizer.NOP;

public class Add extends Base {

  public Add () {
    super (ADD);
  }

  public void register (InstructionSet registry) {
    registry.register (
      new Instruction ("add", group) {
        @Override
        public void process (Machine machine, Program state) throws Fault {
          IS2Registers registers = Add.this.getRegisters (state);
          registers.push (registers.pop (NOP) + registers.pop (NOP), machine.getRandomizer (group));
        }
      });
  }
}
