package us.blav.dina.is.lib;

import us.blav.dina.InstructionRegistry;
import us.blav.dina.randomizers.RegisterRandomizer;

public class Nop extends Base {

  public Nop (RegisterRandomizer.Name randomizer) {
    super (randomizer);
  }

  @Override
  public void register (InstructionRegistry registry) {
    registry.register (
      "nop",
      (machine, state) -> {}
    );
  }
}
