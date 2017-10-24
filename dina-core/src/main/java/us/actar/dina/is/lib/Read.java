package us.actar.dina.is.lib;

import us.actar.dina.InstructionRegistry;
import us.actar.dina.Heap;
import us.actar.dina.Fault;
import us.actar.dina.randomizers.RegisterRandomizer;

import static us.actar.dina.randomizers.RegisterRandomizer.NOP;

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
        Heap heap = machine.getHeap ();
        try {
          state.set (result, heap.get (vaddress), machine.getRandomizer (randomizer));
        } catch (IllegalStateException x) {
          throw new Fault (x);
        }
      });
  }
}
