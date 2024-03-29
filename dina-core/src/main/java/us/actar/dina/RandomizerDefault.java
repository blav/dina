package us.actar.dina;

import us.actar.dina.randomizers.RegisterRandomizer;

import java.util.Map;
import java.util.Random;

public class RandomizerDefault implements Randomizer {

  private final Random random;

  private final Map<? extends InstructionGroup, ? extends RegisterRandomizer> randomizers;

  private final Config config;

  public RandomizerDefault (Machine machine) {
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
  public RegisterRandomizer<?> getRegisterRandomizer (InstructionGroup instructionGroup) {
    return this.randomizers.get (instructionGroup);
  }
}
