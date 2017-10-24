package us.actar.dina;

import com.google.inject.TypeLiteral;

import java.util.Iterator;
import java.util.stream.IntStream;

import static us.actar.dina.Heap.State.*;

public class Heap {

  public static final TypeLiteral<Factory<Heap>> FACTORY_TYPE =
    new TypeLiteral<Factory<Heap>> () {};

  public int size () {
    return heap.length;
  }

  public int get (int offset) {
    ensureValidOffset (offset);
    return Byte.toUnsignedInt (heap [offset]);
  }

  public void set (int offset, int value) {
    ensureValidOffset (offset);
    ensureValidValue (value);
    heap[offset] = (byte) (value & 0xff);
  }

  public int getAvailable () {
    return available;
  }

  public int getTotal () {
    return heap.length;
  }

  public int getUsed () {
    return heap.length - available;
  }

  public void ensureValidValue (int value) {
    if (value >> 8 > 0)
      throw new IllegalStateException ();
  }

  public void ensureValidOffset (int offset)  {
    if (offset < 0 || offset >= heap.length)
      throw new IllegalStateException ();
  }

  public enum State {
    free ("F"),
    inuse ("I"),
    disposed ("D"),;

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

  public Heap (Machine machine) {
    this (machine.getConfig ().getMemory ());
  }

  public Heap (int memory) {
    this.heap = new byte[memory];
    this.first = this.last = new Cell ();
    this.available = this.heap.length;
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

    public IntStream bytes () {
      return IntStream.range (offset, offset + size);
    }

    public Heap getMemoryHeap () {
      return Heap.this;
    }

    public Cell split (Direction direction, int size) throws Fault {
      if (size <= 0 || size > this.size)
        throw new Fault ();

      available -= size;
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

    public void free () throws Fault {
      if (state != inuse)
        throw new Fault ();

      available += size;
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

    public int getOffset () {
      return offset;
    }

    public int getSize () {
      return size;
    }

    public Cell getLeft () {
      return left;
    }

    public Cell getRight () {
      return right;
    }

    public State getState () {
      return state;
    }

    public boolean contains (int offset) {
      return offset >= this.offset && offset < this.offset + size;
    }

    @Override
    public String toString () {
      return "[" + offset + "-" + state.getShortName () + "-" + (offset + size) + "[";
    }

    private int offset;

    private int size;

    private Cell left;

    private Cell right;

    private State state;

  }

  private int available;

  private byte[] heap;

  private Cell first;

  private Cell last;

}
