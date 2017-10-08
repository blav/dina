package us.blav.dina.is1;

import us.blav.dina.Fault;
import us.blav.dina.MemoryHeap;

import static us.blav.dina.MemoryHeap.Direction.left;
import static us.blav.dina.MemoryHeap.Direction.right;
import static us.blav.dina.MemoryHeap.State.free;
import static us.blav.dina.is1.Processor.Flags.auto_increment_ip;

public class Alloc implements InstructionFactory {

  @Override
  public void register (Processor processor) {
    for (int size = 0; size < 4; size++) {
      for (int address = 0; address < 4; address++) {
        final int fsize = size;
        final int faddress = address;
        processor.register (
          12 << 4 | size << 2 | address << 0,
          String.format ("alloc_r%d_bytes_into_r%d", fsize, faddress),
          (machine, state) -> {
            if (state.getChild () != null)
              throw new Fault ();

            int vsize = state.get (fsize);
            MemoryHeap heap = machine.getHeap ();
            MemoryHeap.Cell cright = state.getCell ();
            MemoryHeap.Cell cleft = state.getCell ();
            MemoryHeap.Cell child;
            while (cright != null && cright.getState () != free && cright.getSize () < vsize)
              cright = cright.getRight ();

            while (cleft != null && cleft.getState () != free && cleft.getSize () < vsize)
              cleft = cright.getLeft ();

            if (cleft == null && cright == null) {
              throw new Fault ();
            } else if (cleft == null && cright != null) {
              child = cright.split (left, vsize, state);
            } else if (cleft != null && cright == null) {
              child = cleft.split (right, vsize, state);
            } else {
              MemoryHeap.Cell c;
              MemoryHeap.Direction d;
              int dl = state.getCell ().getOffset () - cleft.getOffset ();
              int dr = cright.getOffset () - state.getCell ().getOffset ();
              if (dr == dl) {
                boolean l = machine.getRandomizer ().nextBoolean ();
                d = l ? right : left;
                c = l ? cleft : cright;
              } else if (dr < dl) {
                d = left;
                c = cright;
              } else {
                d = right;
                c = cleft;
              }

              child = c.split (d, vsize, state);
            }

            state.set (faddress, child.getOffset ());
            state.setChild (child);
          }, auto_increment_ip);
      }
    }
  }
}
