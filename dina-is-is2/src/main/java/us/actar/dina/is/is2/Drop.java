package us.actar.dina.is.is2;

import us.actar.dina.InstructionSet;

import static us.actar.dina.is.is2.IS2Randomizers.DROP;
import static us.actar.dina.randomizers.RegisterRandomizer.NOP;

public class Drop extends Base {

  public Drop () {
    super (DROP);
  }

  @Override
  public void register (InstructionSet registry) {
    registry.register (
      "drop",
      (machine, state) -> getRegisters (state).pop (NOP));
  }
}
