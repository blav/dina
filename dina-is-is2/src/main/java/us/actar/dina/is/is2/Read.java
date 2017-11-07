package us.actar.dina.is.is2;

import us.actar.dina.Fault;
import us.actar.dina.Heap;
import us.actar.dina.InstructionSet;
import us.actar.dina.randomizers.RegisterRandomizer;

import static us.actar.dina.is.is2.IS2Randomizers.READ;
import static us.actar.dina.randomizers.RegisterRandomizer.NOP;

public class Read extends Base {


  public Read () {
    super (READ);
  }

  @Override
  public void register (InstructionSet registry) {
    registry.register (
      "read",
      (machine, state) -> {
        IS2Registers registers = getRegisters (state);
        int vaddress = registers.pop (NOP);
        Heap heap = machine.getHeap ();
        try {
          registers.push (heap.get (vaddress), machine.getRandomizer (randomizer));
        } catch (IllegalStateException x) {
          throw new Fault (x);
        }
      });
  }
}
