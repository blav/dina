package us.actar.dina.is.is2;

import us.actar.dina.Fault;
import us.actar.dina.Instruction;
import us.actar.dina.InstructionSet;
import us.actar.dina.Machine;
import us.actar.dina.Program;

import static us.actar.dina.is.is2.IS2InstructionGroup.NOP;

public class Nop extends Base {

  public Nop () {
    super (NOP);
  }

  @Override
  public void register (InstructionSet registry) {
    registry.register (
      new Instruction ("nop", group) {
        @Override
        public void process (Machine machine, Program state) throws Fault {
        }
      }
    );
  }
}
