package us.blav.dina.is.lib;

import us.blav.dina.InstructionFactory;
import us.blav.dina.randomizers.RegisterRandomizer;

public abstract class Base implements InstructionFactory {

  protected final RegisterRandomizer.Name randomizer;

  public Base (RegisterRandomizer.Name randomizer) {
    this.randomizer = randomizer;
  }
}
