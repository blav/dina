package us.actar.dina.is.is1;

import us.actar.dina.InstructionSet;
import us.actar.dina.randomizers.RegisterRandomizer;

import static us.actar.dina.randomizers.RegisterRandomizer.NOP;

public class Decrement extends Base {

  private final int register;

  public Decrement (RegisterRandomizer.Name randomizer, int register) {
    super (randomizer);
    this.register = register;
  }

  @Override
  public void register (InstructionSet registry) {
    registry.register (
      String.format ("decrement_r%d", register),
      (machine, state) -> {
        getRegisters (state).set (register, getRegisters (state).get (register, NOP) - 1, machine.getRandomizer (randomizer));
      }
    );
  }
}
