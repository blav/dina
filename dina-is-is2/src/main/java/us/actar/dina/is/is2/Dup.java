package us.actar.dina.is.is2;

import us.actar.dina.InstructionSet;
import us.actar.dina.randomizers.RegisterRandomizer;

import static us.actar.dina.is.is2.IS2Randomizers.DUP;
import static us.actar.dina.randomizers.RegisterRandomizer.NOP;

public class Dup extends Base {

  public Dup () {
    super (DUP);
  }


  @Override
  public void register (InstructionSet registry) {
    registry.register (
      "dup",
      (machine, state) -> {
        IS2Registers registers = getRegisters (state);
        registers.push (registers.peek (NOP), machine.getRandomizer (randomizer));
      });
  }
}
