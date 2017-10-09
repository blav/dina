package us.blav.dina.is1;

import us.blav.dina.*;

import static us.blav.dina.InstructionProcessor.Decorator.auto_increment_ip;
import static us.blav.dina.MemoryHeap.Direction.left;
import static us.blav.dina.MemoryHeap.Direction.right;
import static us.blav.dina.MemoryHeap.State.free;

public class Alloc implements InstructionFactory {

  @Override
  public void register (InstructionRegistry registry) {
    for (int size = 0; size < 4; size++) {
      for (int address = 0; address < 4; address++) {
        final int fsize = size;
        final int faddress = address;
        registry.register (
          12 << 4 | size << 2 | address << 0,
          String.format ("alloc_r%d_bytes_into_r%d", fsize, faddress),
          (machine, state) -> {
            if (state.getChild () != null)
              throw new Fault ();

            int vsize = state.get (fsize);
            final MemoryHeap.Cell cell = state.getCell ();
            MemoryHeap.Cell cright = cell;
            MemoryHeap.Cell cleft = cell;
            MemoryHeap.Cell child;
            while (cright != null && (cright.getState () != free || cright.getSize () < vsize))
              cright = cright.getRight ();

            while (cleft != null && (cleft.getState () != free || cleft.getSize () < vsize))
              cleft = cleft.getLeft ();

            if (cleft == null && cright == null) {
              throw new Fault ();
            } else if (cleft == null && cright != null) {
              child = cright.split (left, vsize, state);
            } else if (cleft != null && cright == null) {
              child = cleft.split (right, vsize, state);
            } else {
              MemoryHeap.Cell c;
              MemoryHeap.Direction d;
              int dl = cell.getOffset () - cleft.getOffset () - cleft.getSize ();
              int dr = cright.getOffset () - cell.getOffset () - cell.getSize ();
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
