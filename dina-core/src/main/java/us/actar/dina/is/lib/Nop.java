package us.actar.dina.is.lib;

import us.actar.dina.InstructionRegistry;
import us.actar.dina.randomizers.RegisterRandomizer;

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
