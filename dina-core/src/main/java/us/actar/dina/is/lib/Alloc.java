package us.actar.dina.is.lib;

import us.actar.dina.Fault;
import us.actar.dina.Heap;
import us.actar.dina.InstructionRegistry;
import us.actar.dina.randomizers.RegisterRandomizer;

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
        final Heap.Cell cell = state.getCell ();
        Heap.Cell cright = cell;
        Heap.Cell cleft = cell;
        Heap.Cell child;
        while (cright != null && (cright.getState () != Heap.State.free || cright.getSize () < vsize))
          cright = cright.getRight ();

        while (cleft != null && (cleft.getState () != Heap.State.free || cleft.getSize () < vsize))
          cleft = cleft.getLeft ();

        if (cleft == null && cright == null) {
          throw new Fault ();
        } else if (cleft == null && cright != null) {
          child = cright.split (Heap.Direction.left, vsize);
        } else if (cleft != null && cright == null) {
          child = cleft.split (Heap.Direction.right, vsize);
        } else {
          Heap.Cell c;
          Heap.Direction d;
          int dl = cell.getOffset () - cleft.getOffset () - cleft.getSize ();
          int dr = cright.getOffset () - cell.getOffset () - cell.getSize ();
          if (dr == dl) {
            boolean l = machine.getRandomizer ().nextBoolean ();
            d = l ? Heap.Direction.right : Heap.Direction.left;
            c = l ? cleft : cright;
          } else if (dr < dl) {
            d = Heap.Direction.left;
            c = cright;
          } else {
            d = Heap.Direction.right;
            c = cleft;
          }

          child = c.split (d, vsize);
        }

        state.set (address, child.getOffset (), RegisterRandomizer.NOP);
        state.setChild (child);
      });
  }
}
