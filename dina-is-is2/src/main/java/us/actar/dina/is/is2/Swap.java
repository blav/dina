package us.actar.dina.is.is2;

import us.actar.dina.InstructionSet;
import us.actar.dina.randomizers.RegisterRandomizer;

import static us.actar.dina.is.is2.IS2Randomizers.SWAP;
import static us.actar.dina.randomizers.RegisterRandomizer.NOP;

public class Swap extends Base {

  public Swap () {
    super (SWAP);
  }


  @Override
  public void register (InstructionSet registry) {
    registry.register (
      "swap",
      (machine, state) -> {
        IS2Registers registers = getRegisters (state);
        int a = registers.pop (NOP);
        int b = registers.pop (NOP);
        registers.push (a, NOP);
        registers.push (b, NOP);
        machine.getRandomizer (randomizer).randomizeValue (state, 0);
      });
  }
}
