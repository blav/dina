package us.blav.dina.is.lib;

import us.blav.dina.InstructionFactory;
import us.blav.dina.InstructionRegistry;
import us.blav.dina.RegisterRandomizer;

import static us.blav.dina.RegisterRandomizer.NOP;
import static us.blav.dina.is.is1.IS1Randomizers.INCREMENT;

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
