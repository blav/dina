package us.actar.dina.is.is2;

import us.actar.dina.Fault;
import us.actar.dina.Instruction;
import us.actar.dina.InstructionSet;
import us.actar.dina.Machine;
import us.actar.dina.Program;

import static us.actar.dina.is.is2.IS2InstructionGroup.IF;

public class IfNull extends Base {

  public IfNull () {
    super (IF);
  }

  @Override
  public void register (InstructionSet registry) {
    registry.register (
      new Instruction ("if_null", group) {
        @Override
        public void process (Machine machine, Program state) throws Fault {
          if (IfNull.this.getRegisters (state).pop (machine.getRandomizer (group)) != 0)
            state.incrementIP ();
        }
      });
  }
}
