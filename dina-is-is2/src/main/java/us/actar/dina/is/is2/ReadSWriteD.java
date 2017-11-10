package us.actar.dina.is.is2;

import us.actar.dina.Fault;
import us.actar.dina.Heap;
import us.actar.dina.InstructionSet;

import static us.actar.dina.is.is2.IS2Randomizers.READ;
import static us.actar.dina.is.is2.IS2Randomizers.WRITE;
import static us.actar.dina.is.is2.IS2Registers.Register.D;
import static us.actar.dina.is.is2.IS2Registers.Register.S;
import static us.actar.dina.randomizers.RegisterRandomizer.NOP;

public class ReadSWriteD extends Base {


  public ReadSWriteD () {
    super (WRITE);
  }

  @Override
  public void register (InstructionSet registry) {
    registry.register (
      "read_at_S_write_at_D",
      (machine, state) -> {
        IS2Registers registers = getRegisters (state);
        Heap heap = machine.getHeap ();
        try {
          heap.set (registers.get (D, NOP),
            machine.getRandomizer (randomizer).randomizeValue (state, heap.get (registers.get (S, NOP))));
        } catch (IllegalStateException x) {
          throw new Fault (x);
        }
      });
  }
}
