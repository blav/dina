package us.blav.dina;

import java.util.Iterator;

import static us.blav.dina.MemoryHeap.State.*;

public class MemoryHeap {

  public int size () {
    return heap.length;
  }

  public enum State {
    free("F"),
    inuse("I"),
    disposed("D"),;

    private final String shortName;

    State (String shortName) {
      this.shortName = shortName;
    }

    public String getShortName () {
      return shortName;
    }
  }

  public enum Direction {
    left,
    right,
  }

  public Iterable<Cell> fromFirst () {
    return () -> new Iterator<Cell> () {
      private Cell current = first;
      public boolean hasNext () {
        return current != null;
      }

      public Cell next () {
        Cell ret = current;
        current = current.right;
        return ret;
      }
    };
  }


  public Iterable<Cell> fromLast () {
    return () -> new Iterator<Cell> () {
      private Cell current = last;
      public boolean hasNext () {
        return current != null;
      }

      public Cell next () {
        Cell ret = current;
        current = current.left;
        return ret;
      }
    };
  }

  public MemoryHeap (int size) {
    this.heap = new byte [size];
    this.first = this.last = new Cell ();
  }

  public Cell getFirst () {
    return first;
  }

  public Cell getLast () {
    return last;
  }

  public class Cell {

    private Cell () {
      this.left = null;
      this.right = null;
      this.offset = 0;
      this.state = free;
      this.size = heap.length;
    }

    private Cell (int size, State state) {
      this.size = size;
      this.state = state;
    }

    public MemoryHeap getMemoryHeap () {
      return MemoryHeap.this;
    }

    public Cell split (Direction direction, int size, EnergyTracker tracker) throws Fault {
      if (size > this.size)
        throw new Fault ();

      if (size == this.size) {
        this.state = inuse;
        return this;
      }

      Cell cell = new Cell (size, inuse);
      this.size -= size;
      if (direction == Direction.right) {
        if (this.right == null) {
          last = cell;
        } else {
          this.right.left = cell;
        }

        cell.left = this;
        cell.right = this.right;
        this.right = cell;
        cell.offset = this.offset + this.size;
      } else {
        if (this.left == null) {
          first = cell;
        } else {
          this.left.right = cell;
        }

        cell.right = this;
        cell.left = this.left;
        this.left = cell;
        cell.offset = this.offset;
        this.offset += size;
      }

      return cell;
    }

    public void free (EnergyTracker tracker) throws Fault {
      if (state != inuse)
        throw new Fault ();

      if (right != null && right.state == free && left != null && left.state == free) {
        this.state = free;
        this.left.state = disposed;
        this.right.state = disposed;
        this.size += this.left.size;
        this.size += this.right.size;
        this.offset = this.left.offset;
        if (this.left.left != null) {
          this.left.left.right = this;
        } else {
          first = this;
        }

        this.left = this.left.left;

        if (this.right.right != null) {
          this.right.right.left = this;
        } else {
          last = this;
        }

        this.right = this.right.right;
      } else if (right != null && right.state == free) {
        this.state = disposed;
        this.right.offset = this.offset;
        this.right.size += this.size;
        this.right.left = this.left;
        if (this.left != null)
          this.left.right = this.right;

        if (this == first)
          first = this.right;
      } else if (left != null && left.state == free) {
        this.state = disposed;
        this.left.size += this.size;
        this.left.right = this.right;
        if (this.right != null)
          this.right.left = this.left;

        if (this == last)
          last = this.left;
      } else {
        this.state = free;
      }
    }

    @Override
    public String toString () {
      return "[" + offset + "-" + state.getShortName () + "-" + (offset + size) + "[";
    }

    private long offset;

    private int size;

    private Cell left;

    private Cell right;

    private State state;

  }

  private byte [] heap;

  private Cell first;

  private Cell last;

}
