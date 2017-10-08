package us.blav.dina.is1;

import us.blav.dina.MemoryHeap;

import static us.blav.dina.is1.Processor.Flags.auto_increment_ip;

public class Read implements InstructionFactory {

  @Override
  public void register (Processor processor) {
    for (int address = 0; address < 4; address++) {
      for (int result = 0; result < 4; result++) {
        final int faddress = address;
        final int fresult = result;
        processor.register (
          10 << 4 | address << 2 | result << 0,
          String.format ("read_at_r%d_into_r%d", faddress, fresult),
          (machine, state) -> {
            MemoryHeap heap = machine.getHeap ();
            state.set (fresult, heap.get (faddress));
          }, auto_increment_ip);
      }
    }
  }
}
