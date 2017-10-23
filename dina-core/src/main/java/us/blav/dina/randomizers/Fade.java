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
    int v = Math.abs (value - cell.getOffset () - cell.getSize () / 2);

    int from = getConfig ().getFrom ();
    if (v <= from)
      return value;

    int to = getConfig ().getTo ();
    if (v >= to)
      throw new Fault ();

    int i = getMachine ().getRandomizer ().nextInt ();
    if (i % this.getConfig ().getProbability () != 0)
      return value;

    if (i % (to - from) <= v - from)
      throw new Fault ();

    return value;
  }
}
