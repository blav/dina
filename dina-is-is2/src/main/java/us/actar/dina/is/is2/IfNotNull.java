package us.actar.dina.is.is2;

import us.actar.dina.InstructionSet;
import us.actar.dina.randomizers.RegisterRandomizer;

import static us.actar.dina.is.is2.IS2Randomizers.IF;

public class IfNotNull extends Base {

  public IfNotNull () {
    super (IF);
  }

  @Override
  public void register (InstructionSet registry) {
    registry.register (
      "if_not_null",
      (machine, state) -> {
        if (getRegisters (state).pop (machine.getRandomizer (randomizer)) == 0)
          state.incrementIP ();
      });
  }
}
