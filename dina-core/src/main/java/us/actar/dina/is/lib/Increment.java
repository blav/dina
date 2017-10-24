package us.actar.dina.is.lib;

import us.actar.dina.InstructionRegistry;
import us.actar.dina.randomizers.RegisterRandomizer;

import static us.actar.dina.randomizers.RegisterRandomizer.NOP;

public class Increment extends Base {

  private final int register;

  public Increment (RegisterRandomizer.Name randomizer, int register) {
    super (randomizer);
    this.register = register;
  }

  @Override
  public void register (InstructionRegistry registry) {
    registry.register (
      String.format ("increment_r%d", register),
      (machine, state) -> {
        state.set (this.register, state.get (this.register, NOP) + 1, machine.getRandomizer (randomizer));
      }
    );
  }
}