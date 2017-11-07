package us.actar.dina.is.is2;

import us.actar.dina.Fault;
import us.actar.dina.Heap;
import us.actar.dina.InstructionSet;
import us.actar.dina.randomizers.RegisterRandomizer;

import static us.actar.dina.is.is2.IS2Randomizers.*;

public class Write extends Base {

  public Write () {
    super (WRITE);
  }

  @Override
  public void register (InstructionSet registry) {
    registry.register (
      "write",
      (machine, state) -> {
        Heap heap = machine.getHeap ();
        IS2Registers registers = getRegisters (state);
        int v = registers.pop (RegisterRandomizer.NOP);
        if (v >> 8 > 0)
          throw new Fault ();

        int vaddress = registers.pop (machine.getRandomizer (randomizer));
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
