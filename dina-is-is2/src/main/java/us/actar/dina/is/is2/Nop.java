package us.actar.dina.is.is2;

import us.actar.dina.InstructionSet;
import us.actar.dina.randomizers.RegisterRandomizer;

import static us.actar.dina.is.is2.IS2Randomizers.NOP;

public class Nop extends Base {

  public Nop () {
    super (NOP);
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
