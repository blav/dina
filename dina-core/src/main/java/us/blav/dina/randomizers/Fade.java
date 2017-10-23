package us.blav.dina.randomizers;

import us.blav.dina.Fault;
import us.blav.dina.MemoryHeap;
import us.blav.dina.ProgramState;
import us.blav.dina.VirtualMachine;

public class Fade extends AbstractRegisterRandomizer<FadeConfig> {

  public static final Factory<FadeConfig> FACTORY = Fade::new;

  public Fade (VirtualMachine machine, FadeConfig config) {
    super (machine, config);
  }

  @Override
  public int randomizeValue (ProgramState state, int value) throws Fault {
    MemoryHeap.Cell cell = state.getCell ();
    int radius = cell.getSize () / 2;
    int center = cell.getOffset () + radius;
    int d = Math.abs (value - center) - radius;

    if (d <= 0)
      return value;

    int distance = getConfig ().getDistance ();
    if (d >= distance)
      throw new Fault ();

    int i = getMachine ().getRandomizer ().nextInt ();
    if (i % this.getConfig ().getProbability () != 0)
      return value;

    if (i % distance <= d)
      throw new Fault ();

    return value;
  }
}
