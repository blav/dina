package us.actar.dina.is.is1;

import us.actar.dina.InstructionSet;
import us.actar.dina.randomizers.RegisterRandomizer;

public class Nop extends Base {

  public Nop (RegisterRandomizer.Name randomizer) {
    super (randomizer);
  }

  @Override
  public void register (InstructionSet registry) {
    registry.register (
      "nop",
      (machine, state) -> {
      }
    );
  }
}
