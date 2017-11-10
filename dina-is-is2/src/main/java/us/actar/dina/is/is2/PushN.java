package us.actar.dina.is.is2;

import us.actar.dina.InstructionSet;

import static us.actar.dina.is.is2.IS2Randomizers.PUSH;
import static us.actar.dina.randomizers.RegisterRandomizer.NOP;

public class PushN extends Base {

  private final int value;

  public PushN (int value) {
    super (PUSH);
    this.value = value;
  }

  public void register (InstructionSet registry) {
    registry.register (
      String.format ("push_%d", value),
      (machine, state) -> {
        getRegisters (state).push (value, machine.getRandomizer (randomizer));
      });
  }
}
