package us.actar.dina.is.lib;

import us.actar.dina.Heap;
import us.actar.dina.Fault;
import us.actar.dina.InstructionRegistry;
import us.actar.dina.randomizers.RegisterRandomizer;

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
        Heap heap = machine.getHeap ();
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
