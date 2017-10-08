package us.blav.dina;

import java.util.Random;

public class DefaultRandomizer implements Randomizer {

  private final Random random;

  public DefaultRandomizer (long seed) {
    this.random = new Random (seed);
  }

  @Override
  public boolean nextBoolean () {
    return this.random.nextBoolean ();
  }

}
