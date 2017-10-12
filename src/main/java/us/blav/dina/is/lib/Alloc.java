package us.blav.dina.is.lib;

import us.blav.dina.*;

import static us.blav.dina.MemoryHeap.Direction.left;
import static us.blav.dina.MemoryHeap.Direction.right;
import static us.blav.dina.MemoryHeap.State.free;
import static us.blav.dina.RegisterRandomizer.NOP;
import static us.blav.dina.is.is1.IS1Randomizers.ALLOC;

public class Alloc extends Base {

  private final int address;

  private final int size;

  public Alloc (RegisterRandomizer.Name randomizer, int address, int size) {
    super (randomizer);
    this.address = address;
    this.size = size;
  }

  @Override
  public void register (InstructionRegistry registry) {
    registry.register (
      String.format ("alloc_r%d_bytes_into_r%d", size, address),
      (machine, state) -> {
        if (state.getChild () != null)
          throw new Fault ();

        int vsize = state.get (size, machine.getRandomizer (randomizer));
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

        state.set (address, child.getOffset (), NOP);
        state.setChild (child);
      });
  }
}
