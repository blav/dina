package us.actar.dina.is.is2;

import us.actar.dina.InstructionSet;
import us.actar.dina.randomizers.RegisterRandomizer;

import static us.actar.dina.is.is2.IS2Randomizers.INCREMENT;
import static us.actar.dina.randomizers.RegisterRandomizer.NOP;

public class Increment extends Base {

  public Increment () {
    super (INCREMENT);
  }

  @Override
  public void register (InstructionSet registry) {
    registry.register (
      "increment",
      (machine, state) -> {
        IS2Registers registers = getRegisters (state);
        registers.push (registers.pop (NOP) + 1, machine.getRandomizer (randomizer));
      }
    );
  }
}
