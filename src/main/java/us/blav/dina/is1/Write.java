package us.blav.dina.is1;

import us.blav.dina.Fault;
import us.blav.dina.MemoryHeap;

import static us.blav.dina.is1.Processor.Flags.auto_increment_ip;

public class Write implements InstructionFactory {

  @Override
  public void register (Processor processor) {
    for (int address = 0; address < 4; address++) {
      for (int value = 0; value < 4; value++) {
        final int faddress = address;
        final int fvalue = value;
        processor.register (
          11 << 4 | address << 2 | value << 0,
          String.format ("write_r%d_at_r%d", fvalue, faddress),
          (machine, state) -> {
            MemoryHeap heap = machine.getHeap ();
            int v = state.get (fvalue);
            if (v >> 8 > 0)
              throw new Fault ();

            boolean ok = state.getCell ().contains (faddress);
            if (ok == false && state.getChild () != null)
              ok = state.getCell ().contains (faddress);

            if (ok)
              heap.set (state.get (faddress), (byte) v);

            throw new Fault ();
          }, auto_increment_ip);
      }
    }
  }
}
