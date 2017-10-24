package us.actar.dina.is.lib;

import us.actar.dina.InstructionFactory;
import us.actar.dina.randomizers.RegisterRandomizer;

public abstract class Base implements InstructionFactory {

  protected final RegisterRandomizer.Name randomizer;

  public Base (RegisterRandomizer.Name randomizer) {
    this.randomizer = randomizer;
  }
}
