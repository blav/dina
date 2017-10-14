package us.blav.dina.is.lib;

import us.blav.dina.*;
import us.blav.dina.randomizers.RegisterRandomizer;

public class Write extends Base {

  private final int address;

  private final int value;

  public Write (RegisterRandomizer.Name randomizer, int address, int value) {
    super (randomizer);
    this.address = address;
    this.value = value;
  }

  @Override
  public void register (InstructionRegistry registry) {
    registry.register (
      String.format ("write_r%d_at_r%d", value, address),
      (machine, state) -> {
        MemoryHeap heap = machine.getHeap ();
        int v = state.get (value, RegisterRandomizer.NOP);
        if (v >> 8 > 0)
          throw new Fault ();

        int vaddress = state.get (address, machine.getRandomizer (randomizer));
        boolean ok = state.getCell ().contains (vaddress);
        if (ok == false && state.getChild () != null)
          ok = state.getChild ().contains (vaddress);

        if (ok) {
          heap.set (vaddress, (byte) v);
          return;
        }

        throw new Fault ();
      });
  }
}
