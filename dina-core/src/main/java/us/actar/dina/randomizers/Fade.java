package us.actar.dina.randomizers;

import us.actar.dina.Fault;
import us.actar.dina.Heap;
import us.actar.dina.Machine;
import us.actar.dina.ProgramState;

public class Fade extends AbstractRegisterRandomizer<FadeConfig> {

  public static final Factory<FadeConfig> FACTORY = Fade::new;

  public Fade (Machine machine, FadeConfig config) {
    super (machine, config);
  }

  @Override
  public int randomizeValue (ProgramState state, int value) throws Fault {
    Heap.Cell cell = state.getCell ();
    int radius = cell.getSize () / 2;
    int center = cell.getOffset () + radius;
    int d = Math.abs (value - center) - radius;
    if (d <= 0)
      return value;

    int distance = getConfig ().getDistance () * radius;
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
