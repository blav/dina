package us.blav.dina.is1;

import us.blav.dina.Fault;
import us.blav.dina.InstructionFactory;
import us.blav.dina.InstructionRegistry;
import us.blav.dina.MemoryHeap;

import static us.blav.dina.InstructionProcessor.Decorator.auto_increment_ip;

public class Write implements InstructionFactory {

  @Override
  public void register (InstructionRegistry registry) {
    for (int address = 0; address < 4; address++) {
      for (int value = 0; value < 4; value++) {
        final int faddress = address;
        final int fvalue = value;
        registry.register (
          11 << 4 | address << 2 | value << 0,
          String.format ("write_r%d_at_r%d", fvalue, faddress),
          (machine, state) -> {
            MemoryHeap heap = machine.getHeap ();
            int v = state.get (fvalue);
            if (v >> 8 > 0)
              throw new Fault ();

            int vaddress = state.get (faddress);
            boolean ok = state.getCell ().contains (vaddress);
            if (ok == false && state.getChild () != null)
              ok = state.getChild ().contains (vaddress);

            if (ok) {
              heap.set (vaddress, (byte) v);
              return;
            }

            throw new Fault ();
          }, auto_increment_ip);
      }
    }
  }
}
