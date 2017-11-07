package us.actar.dina.randomizers;

import us.actar.dina.Program;

public class Nop implements RegisterRandomizer<NopConfig> {

  public static final Factory<NopConfig> FACTORY = (machine, config) -> new Nop ();

  @Override
  public int randomizeValue (Program state, int value) {
    return value;
  }
}
