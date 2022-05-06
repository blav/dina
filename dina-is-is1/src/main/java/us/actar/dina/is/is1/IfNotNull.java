package us.actar.dina.is.is1;

import us.actar.dina.*;

public class IfNotNull extends Base {

  private final int value;

  public IfNotNull (InstructionGroup randomizer, int value) {
    super (randomizer);
    this.value = value;
  }

  @Override
  public void register (InstructionSet registry) {
    registry.register (
      new Instruction (String.format ("if_r%d_is_not_null", value), group) {
        @Override
        public void process (Machine machine, Program state) throws Fault {
          if (IfNotNull.this.getRegisters (state).get (value, machine.getRandomizer (group)) == 0)
            state.incrementIP ();
        }
      });
  }
}
