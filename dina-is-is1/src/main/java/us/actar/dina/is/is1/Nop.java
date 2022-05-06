package us.actar.dina.is.is1;

import us.actar.dina.*;

public class Nop extends Base {

  public Nop (InstructionGroup randomizer) {
    super (randomizer);
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
