package us.blav.dina.randomizers;

import us.blav.dina.ProgramState;
import us.blav.dina.Randomizer;
import us.blav.dina.VirtualMachine;

public class Shift extends AbstractRegisterRandomizer<ShiftConfig> {

  public static final Factory<ShiftConfig> FACTORY = Shift::new;

  public Shift (VirtualMachine machine, ShiftConfig config) {
    super (machine, config);
  }

  @Override
  public int randomizeValue (ProgramState state, int value) {
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
