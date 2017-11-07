package us.actar.dina.is.is1;

import us.actar.dina.InstructionSet;
import us.actar.dina.randomizers.RegisterRandomizer;

import static us.actar.dina.randomizers.RegisterRandomizer.NOP;

public class Increment extends Base {

  private final int register;

  public Increment (RegisterRandomizer.Name randomizer, int register) {
    super (randomizer);
    this.register = register;
  }

  @Override
  public void register (InstructionSet registry) {
    registry.register (
      String.format ("increment_r%d", register),
      (machine, state) -> {
        getRegisters (state).set (this.register, getRegisters (state).get (this.register, NOP) + 1, machine.getRandomizer (randomizer));
      }
    );
  }
}
