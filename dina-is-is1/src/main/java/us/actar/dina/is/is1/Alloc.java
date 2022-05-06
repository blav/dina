package us.actar.dina.is.is1;

import us.actar.dina.*;
import us.actar.dina.randomizers.RegisterRandomizer;

public class Alloc extends Base {

  private final int address;

  private final int size;

  public Alloc (InstructionGroup randomizer, int address, int size) {
    super (randomizer);
    this.address = address;
    this.size = size;
  }

  @Override
  public void register (InstructionSet registry) {
    registry.register (
      new Instruction (String.format ("alloc_r%d_bytes_into_r%d", size, address), group) {
        @Override
        public void process (Machine machine, Program state) throws Fault {
          if (state.getChild () != null)
            throw new Fault ();

          int vsize = Alloc.this.getRegisters (state).get (size, machine.getRandomizer (group));
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

          Alloc.this.getRegisters (state).set (address, child.getOffset (), RegisterRandomizer.NOP);
          state.setChild (child);
        }
      });
  }
}
