package us.blav.dina.randomizers;

import us.blav.dina.ProgramState;
import us.blav.dina.Randomizer;
import us.blav.dina.VirtualMachine;

public class BitFlip extends AbstractRegisterRandomizer<BitFlipConfig> {

  public static final Factory<BitFlipConfig> FACTORY = BitFlip::new;

  public BitFlip (VirtualMachine machine, BitFlipConfig config) {
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
