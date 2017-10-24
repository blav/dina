package us.actar.dina.randomizers;

import us.actar.dina.ProgramState;
import us.actar.dina.Randomizer;
import us.actar.dina.Machine;

public class BitFlip extends AbstractRegisterRandomizer<BitFlipConfig> {

  public static final Factory<BitFlipConfig> FACTORY = BitFlip::new;

  public BitFlip (Machine machine, BitFlipConfig config) {
    super (machine, config);
  }

  @Override
  public int randomizeValue (ProgramState state, int value) {
    Randomizer randomizer = getMachine ().getRandomizer ();
    int i = randomizer.nextInt () % getConfig ().getProbability ();
    if (i != 0) {
      return value;
    } else {
      return value ^ (1 << (randomizer.nextInt () % 32));
    }
  }
}
