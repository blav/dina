package us.actar.dina.is.lib;

import us.actar.dina.InstructionRegistry;
import us.actar.dina.randomizers.RegisterRandomizer;

public class IfNotNull extends Base {

  private final int value;

  public IfNotNull (RegisterRandomizer.Name randomizer, int value) {
    super (randomizer);
    this.value = value;
  }

  @Override
  public void register (InstructionRegistry registry) {
    registry.register (
      String.format ("if_r%d_is_not_null", value),
      (machine, state) -> {
        if (state.get (value, machine.getRandomizer (randomizer)) == 0)
          state.incrementIP ();
      });
  }
}