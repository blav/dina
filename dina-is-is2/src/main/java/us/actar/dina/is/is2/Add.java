package us.actar.dina.is.is2;

import us.actar.dina.InstructionSet;
import us.actar.dina.randomizers.RegisterRandomizer;

import static us.actar.dina.is.is2.IS2Randomizers.ADD;
import static us.actar.dina.randomizers.RegisterRandomizer.NOP;

public class Add extends Base {

  public Add () {
    super (ADD);
  }

  public void register (InstructionSet registry) {
    registry.register (
      "add",
      (machine, state) -> {
        IS2Registers registers = getRegisters (state);
        registers.push (registers.pop (NOP) + registers.pop (NOP), machine.getRandomizer (randomizer));
      });
  }
}
