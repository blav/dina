package us.actar.dina.is.is2;

import us.actar.dina.InstructionSet;
import us.actar.dina.randomizers.RegisterRandomizer;

import static us.actar.dina.is.is2.IS2Randomizers.DECREMENT;
import static us.actar.dina.randomizers.RegisterRandomizer.NOP;

public class Decrement extends Base {

  public Decrement () {
    super (DECREMENT);
  }

  @Override
  public void register (InstructionSet registry) {
    registry.register (
      "decrement",
      (machine, state) -> {
        IS2Registers registers = getRegisters (state);
        registers.push (registers.pop (NOP) - 1, machine.getRandomizer (randomizer));
      }
    );
  }
}
