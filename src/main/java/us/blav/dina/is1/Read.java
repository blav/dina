package us.blav.dina.is1;

import us.blav.dina.InstructionFactory;
import us.blav.dina.InstructionRegistry;
import us.blav.dina.MemoryHeap;
import us.blav.dina.VirtualMachine;

import static us.blav.dina.InstructionProcessor.Decorator.auto_increment_ip;

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
            int vaddress = state.get (faddress);
            MemoryHeap heap = machine.getHeap ();
            state.set (fresult, heap.get (vaddress));
          }, auto_increment_ip);
      }
    }
  }
}
