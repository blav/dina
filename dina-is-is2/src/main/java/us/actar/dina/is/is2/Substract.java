package us.actar.dina.is.is2;

import us.actar.dina.InstructionSet;
import us.actar.dina.randomizers.RegisterRandomizer;

import static us.actar.dina.is.is2.IS2Randomizers.SUBSTRACT;
import static us.actar.dina.randomizers.RegisterRandomizer.NOP;

public class Substract extends Base {

  public Substract () {
    super (SUBSTRACT);
  }

  public void register (InstructionSet registry) {
    registry.register (
      "substract",
      (machine, state) -> {
        IS2Registers registers = getRegisters (state);
        registers.push (- registers.pop (NOP) + registers.pop (NOP), machine.getRandomizer (randomizer));
      });
  }
}
