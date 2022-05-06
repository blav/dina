package us.actar.dina.is.is2;

import us.actar.dina.Fault;
import us.actar.dina.Instruction;
import us.actar.dina.InstructionSet;
import us.actar.dina.Machine;
import us.actar.dina.Program;

import static us.actar.dina.is.is2.IS2InstructionGroup.RESET;

public class Reset extends Base {

  public Reset () {
    super (RESET);
  }


  @Override
  public void register (InstructionSet registry) {
    registry.register (
      new Instruction ("reset", group) {
        @Override
        public void process (Machine machine, Program state) throws Fault {
          state.setInstructionPointer (state.getCell ().getOffset (), machine.getRandomizer (group));
        }
      });
  }
}
