package us.blav.dina.is.lib;

import us.blav.dina.InstructionRegistry;
import us.blav.dina.randomizers.RegisterRandomizer;

import static us.blav.dina.randomizers.RegisterRandomizer.NOP;

public class Decrement extends Base {

  private final int register;

  public Decrement (RegisterRandomizer.Name randomizer, int register) {
    super (randomizer);
    this.register = register;
  }

  @Override
  public void register (InstructionRegistry registry) {
    registry.register (
      String.format ("decrement_r%d", register),
      (machine, state) -> {
        state.set (register, state.get (register, NOP) - 1, machine.getRandomizer (randomizer));
      }
    );
  }
}
