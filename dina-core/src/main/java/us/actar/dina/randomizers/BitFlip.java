package us.actar.dina.randomizers;

import us.actar.dina.Machine;
import us.actar.dina.Program;
import us.actar.dina.Randomizer;

public class BitFlip extends AbstractRegisterRandomizer<BitFlipConfig> {

  public static final Factory<BitFlipConfig> FACTORY = BitFlip::new;

  public BitFlip (Machine machine, BitFlipConfig config) {
    super (machine, config);
  }

  @Override
  public int randomizeValue (Program state, int value) {
    Randomizer randomizer = getMachine ().getRandomizer ();
    int i = randomizer.nextInt () % getConfig ().getProbability ();
    if (i != 0) {
      return value;
    } else {
      return value ^ (1 << (randomizer.nextInt () % 32));
    }
  }
}
