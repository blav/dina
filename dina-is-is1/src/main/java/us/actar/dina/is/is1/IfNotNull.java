package us.actar.dina.is.is1;

import us.actar.dina.InstructionSet;
import us.actar.dina.randomizers.RegisterRandomizer;

public class IfNotNull extends Base {

  private final int value;

  public IfNotNull (RegisterRandomizer.Name randomizer, int value) {
    super (randomizer);
    this.value = value;
  }

  @Override
  public void register (InstructionSet registry) {
    registry.register (
      String.format ("if_r%d_is_not_null", value),
      (machine, state) -> {
        if (getRegisters (state).get (value, machine.getRandomizer (randomizer)) == 0)
          state.incrementIP ();
      });
  }
}
