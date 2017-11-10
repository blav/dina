package us.actar.dina.randomizers;

import us.actar.dina.Machine;
import us.actar.dina.Program;
import us.actar.dina.Randomizer;

public class Shuffle extends AbstractRegisterRandomizer<ShuffleConfig> {

  public static final Factory<ShuffleConfig> FACTORY = Shuffle::new;

  public Shuffle (Machine machine, ShuffleConfig config) {
    super (machine, config);
  }

  @Override
  public int randomizeValue (Program state, int value) {
    Randomizer randomizer = getMachine ().getRandomizer ();
    int v = randomizer.nextInt ();
    int i = v % getConfig ().getProbability ();
    if (i != 0) {
      return value;
    } else {
      return v % getConfig ().getRange ().range (getMachine ());
    }
  }
}
