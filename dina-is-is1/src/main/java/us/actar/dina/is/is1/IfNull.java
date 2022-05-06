package us.actar.dina.is.is1;

import us.actar.dina.*;

public class IfNull extends Base {

  private final int value;

  public IfNull (InstructionGroup randomizer, int value) {
    super (randomizer);
    this.value = value;
  }

  @Override
  public void register (InstructionSet registry) {
    registry.register (
      new Instruction (String.format ("if_r%d_is_null", value), group) {
        @Override
        public void process (Machine machine, Program state) throws Fault {
          if (IfNull.this.getRegisters (state).get (value, machine.getRandomizer (group)) != 0)
            state.incrementIP ();
        }
      });
  }
}
