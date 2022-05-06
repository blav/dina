package us.actar.dina.is.is2;

import us.actar.dina.Instruction;
import us.actar.dina.InstructionSet;
import us.actar.dina.Machine;
import us.actar.dina.Program;

import static us.actar.dina.is.is2.IS2InstructionGroup.FAULT;
import static us.actar.dina.is.is2.IS2InstructionGroup.NOP;

public class Fault extends Base {

  public Fault () {
    super (FAULT);
  }

  @Override
  public void register (InstructionSet registry) {
    registry.register (
      new Instruction ("fault", group) {
        @Override
        public void process (Machine machine, Program state) throws us.actar.dina.Fault {
          throw new us.actar.dina.Fault ();
        }
      }
    );
  }
}
