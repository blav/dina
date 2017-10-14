package us.blav.dina;

import us.blav.dina.randomizers.RegisterRandomizer;

import java.util.Map;
import java.util.Random;

public class RandomizerDefault implements Randomizer {

  private final Random random;

  private final Map<? extends RegisterRandomizer.Name, ? extends RegisterRandomizer> randomizers;

  private final Config config;

  public RandomizerDefault (VirtualMachine machine) {
    this.config = machine.getConfig ();
    this.random = new Random (this.config.getRandomizer ().getSeed ());
    this.randomizers = this.config.getInstructionSet ().createRandomizers (machine);
  }

  @Override
  public boolean nextBoolean () {
    return this.random.nextBoolean ();
  }

  @Override
  public int nextInt () {
    return this.random.nextInt ();
  }

  public Config.Randomizer getConfig () {
    return config.getRandomizer ();
  }

  @Override
  public RegisterRandomizer<?> getRegisterRandomizer (RegisterRandomizer.Name name) {
    return this.randomizers.get (name);
  }
}
