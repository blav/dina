package us.blav.dina.is.lib;

import us.blav.dina.*;

import static us.blav.dina.RegisterRandomizer.NOP;
import static us.blav.dina.is.is1.IS1Randomizers.READ;

public class Read extends Base {

  private final int address;

  private final int result;

  public Read (RegisterRandomizer.Name randomizer, int address, int result) {
    super (randomizer);
    this.address = address;
    this.result = result;
  }

  @Override
  public void register (InstructionRegistry registry) {
    registry.register (
      String.format ("read_at_r%d_into_r%d", address, result),
      (machine, state) -> {
        int vaddress = state.get (address, NOP);
        MemoryHeap heap = machine.getHeap ();
        try {
          state.set (result, heap.get (vaddress), machine.getRandomizer (randomizer));
        } catch (IllegalStateException x) {
          throw new Fault (x);
        }
      });
  }
}
