package us.actar.dina.randomizers;

import us.actar.dina.Machine;
import us.actar.dina.Program;
import us.actar.dina.Randomizer;

public class Shift extends AbstractRegisterRandomizer<ShiftConfig> {

  public static final Factory<ShiftConfig> FACTORY = Shift::new;

  public Shift (Machine machine, ShiftConfig config) {
    super (machine, config);
  }

  @Override
  public int randomizeValue (Program state, int value) {
    Randomizer randomizer = getMachine ().getRandomizer ();
    int i = randomizer.nextInt () % getConfig ().getProbability ();
    int v = getConfig ().getValue ();
    if (i != 0) {
      return value;
    } else {
      return value + (randomizer.nextInt () % ((v + 1) * 2)) - v;
    }
  }
}
