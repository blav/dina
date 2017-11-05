package us.actar.dina.randomizers;

import us.actar.dina.Machine;
import us.actar.dina.ProgramState;
import us.actar.dina.Randomizer;

public class Shuffle extends AbstractRegisterRandomizer<ShuffleConfig> {

  public static final Factory<ShuffleConfig> FACTORY = Shuffle::new;

  public Shuffle (Machine machine, ShuffleConfig config) {
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
