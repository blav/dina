package us.actar.dina.is.is2;

import us.actar.dina.Fault;
import us.actar.dina.Instruction;
import us.actar.dina.InstructionSet;
import us.actar.dina.Machine;
import us.actar.dina.Program;

import static us.actar.dina.is.is2.IS2InstructionGroup.INCREMENT;
import static us.actar.dina.randomizers.RegisterRandomizer.NOP;

public class Increment extends Base {

  public Increment () {
    super (INCREMENT);
  }

  @Override
  public void register (InstructionSet registry) {
    registry.register (
      new Instruction ("increment", group) {
        @Override
        public void process (Machine machine, Program state) throws Fault {
          IS2Registers registers = Increment.this.getRegisters (state);
          registers.push (registers.pop (NOP) + 1, machine.getRandomizer (group));
        }
      }
    );
  }
}
