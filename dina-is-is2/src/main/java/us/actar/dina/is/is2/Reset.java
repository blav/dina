package us.actar.dina.is.is2;

import us.actar.dina.InstructionSet;
import us.actar.dina.randomizers.RegisterRandomizer;

import static us.actar.dina.is.is2.IS2Randomizers.RESET;
import static us.actar.dina.randomizers.RegisterRandomizer.NOP;

public class Reset extends Base {

  public Reset () {
    super (RESET);
  }


  @Override
  public void register (InstructionSet registry) {
    registry.register (
      "reset",
      (machine, state) ->
        state.setInstructionPointer (state.getCell ().getOffset (), machine.getRandomizer (randomizer)));
  }
}
