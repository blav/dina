package us.blav.dina.is.is1;

import us.blav.dina.Fault;
import us.blav.dina.InstructionFactory;
import us.blav.dina.InstructionRegistry;
import us.blav.dina.MemoryHeap;

import static us.blav.dina.InstructionProcessor.Decorator.auto_increment_ip;
import static us.blav.dina.RegisterRandomizer.NOP;
import static us.blav.dina.is.is1.IS1Randomizers.READ;

public class Read implements InstructionFactory {

  @Override
  public void register (InstructionRegistry registry) {
    for (int address = 0; address < 4; address++) {
      for (int result = 0; result < 4; result++) {
        final int faddress = address;
        final int fresult = result;
        registry.register (
          10 << 4 | address << 2 | result << 0,
          String.format ("read_at_r%d_into_r%d", faddress, fresult),
          (machine, state) -> {
            int vaddress = state.get (faddress, NOP);
            MemoryHeap heap = machine.getHeap ();
            try {
              state.set (fresult, heap.get (vaddress), machine.getRandomizer (READ));
            } catch (IllegalStateException x) {
              throw new Fault (x);
            }
          }, auto_increment_ip);
      }
    }
  }
}
