package us.actar.dina.is.is2;

import us.actar.dina.InstructionSet;
import us.actar.dina.randomizers.RegisterRandomizer;

import static us.actar.dina.is.is2.IS2Randomizers.POP;
import static us.actar.dina.randomizers.RegisterRandomizer.NOP;

public class Pop extends Base {

  public Pop () {
    super (POP);
  }


  @Override
  public void register (InstructionSet registry) {
    registry.register (
      "swap",
      (machine, state) -> getRegisters (state).pop (NOP));
  }
}
