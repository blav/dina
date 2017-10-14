package us.blav.dina.randomizers;

import us.blav.dina.ProgramState;
import us.blav.dina.Randomizer;
import us.blav.dina.VirtualMachine;

public class Shuffle extends AbstractRegisterRandomizer<ShuffleConfig> {

  public static final Factory<ShuffleConfig> FACTORY = Shuffle::new;

  public Shuffle (VirtualMachine machine, ShuffleConfig config) {
    super (machine, config);
  }

  @Override
  public int randomizeValue (ProgramState state, int value) {
    Randomizer randomizer = getMachine ().getRandomizer ();
    int i = randomizer.nextInt () % getConfig ().getProbability ();
    if (i != 0) {
      return value;
    } else {
      return randomizer.nextInt () % getConfig ().getRange ().range (getMachine ());
    }
  }
}
