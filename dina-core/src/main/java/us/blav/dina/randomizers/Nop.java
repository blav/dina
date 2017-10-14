package us.blav.dina.randomizers;

import us.blav.dina.ProgramState;

public class Nop implements RegisterRandomizer<NopConfig> {

  public static final Factory<NopConfig> FACTORY = (machine, config) -> new Nop ();

  @Override
  public int randomizeValue (ProgramState state, int value) {
    return value;
  }
}
