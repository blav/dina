package us.blav.dina;

import org.junit.Test;

import java.util.List;
import java.util.stream.StreamSupport;

import static java.util.Arrays.asList;
import static java.util.Collections.reverse;
import static java.util.stream.Collectors.joining;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static us.blav.dina.MemoryHeap.Direction.left;
import static us.blav.dina.MemoryHeap.Direction.right;

public class MemoryHeapTest {

  private String toStringFromFirst (MemoryHeap memory) {
    return StreamSupport.stream (memory.fromFirst ().spliterator (), false)
      .map (Object::toString)
      .collect (joining (", "));
  }

  private String toStringFromLast (MemoryHeap memory) {
    return StreamSupport.stream (memory.fromLast ().spliterator (), false)
      .map (Object::toString)
      .collect (joining (", "));
  }

  @Test
  public void initial_cell_should_be_wide_and_free () {
    assertCells ("[0-F-100[", new MemoryHeap (100));
  }

  @Test(expected = Fault.class)
  public void split_right_should_not_exceed_cell_size () throws Fault {
    new MemoryHeap (100).getFirst ().split (right, 101, newTracker ());
  }

  private EnergyTracker newTracker () {
    return amount -> {
    };
  }

  @Test(expected = Fault.class)
  public void split_left_should_not_exceed_cell_size () throws Fault {
    new MemoryHeap (100).getFirst ().split (left, 101, newTracker ());
  }

  @Test
  public void split_right_all_size_should_take_all_size_and_set_state_inuse () throws Fault {
    MemoryHeap memory = new MemoryHeap (100);
    memory.getFirst ().split (right, 100, newTracker ());
    assertCells ("[0-I-100[", memory);
  }

  @Test
  public void split_left_all_size_should_take_all_size_and_set_state_inuse () throws Fault {
    MemoryHeap memory = new MemoryHeap (100);
    memory.getFirst ().split (left, 100, newTracker ());
    assertCells ("[0-I-100[", memory);
  }

  @Test
  public void split_right_smaller_size_should_split_and_set_state_inuse () throws Fault {
    MemoryHeap memory = new MemoryHeap (100);
    memory.getFirst ().split (right, 90, newTracker ());
    assertCells ("[0-F-10[, [10-I-100[", memory);
  }

  @Test
  public void split_left_smaller_size_should_split_and_set_state_inuse () throws Fault {
    MemoryHeap memory = new MemoryHeap (100);
    memory.getFirst ().split (left, 90, newTracker ());
    assertCells ("[0-I-90[, [90-F-100[", memory);
  }

  @Test
  public void right_splitted_cell_should_not_be_splittable () throws Fault {
    MemoryHeap memory = new MemoryHeap (100);
    memory.getFirst ().split (right, 50, newTracker ()).split (right, 20, newTracker ());
    assertCells ("[0-F-50[, [50-I-80[, [80-I-100[", memory);
  }

  @Test
  public void left_splitted_cell_should_not_be_splittable () throws Fault {
    MemoryHeap memory = new MemoryHeap (100);
    memory.getFirst ().split (left, 50, newTracker ()).split (left, 20, newTracker ());
    assertCells ("[0-I-20[, [20-I-50[, [50-F-100[", memory);
  }

  @Test
  public void right_split_between_to_cells_should_preserve_chaining () throws Fault {
    MemoryHeap memory = new MemoryHeap (100);
    memory.getFirst ().split (right, 30, newTracker ());
    memory.getFirst ().split (right, 20, newTracker ());
    assertCells ("[0-F-50[, [50-I-70[, [70-I-100[", memory);
  }

  @Test
  public void left_split_between_to_cells_should_preserve_chaining () throws Fault {
    MemoryHeap memory = new MemoryHeap (100);
    memory.getLast ().split (left, 30, newTracker ());
    memory.getLast ().split (left, 20, newTracker ());
    assertCells ("[0-I-30[, [30-I-50[, [50-F-100[", memory);
  }

  @Test
  public void free_should_merge_cell_with_left_if_free () throws Fault {
    MemoryHeap memory = new MemoryHeap (100);
    memory.getFirst ().split (right, 30, newTracker ()).free (newTracker ());
    assertCells ("[0-F-100[", memory);
  }

  @Test
  public void free_should_merge_cell_with_right_if_free () throws Fault {
    MemoryHeap memory = new MemoryHeap (100);
    memory.getFirst ().split (left, 30, newTracker ()).free (newTracker ());
    assertCells ("[0-F-100[", memory);
  }

  @Test
  public void free_should_just_mark_as_free_if_right_is_in_use_or_null () throws Fault {
    MemoryHeap memory = new MemoryHeap (100);
    MemoryHeap.Cell l = memory.getFirst ();
    MemoryHeap.Cell c = l.split (right, 70, newTracker ());
    MemoryHeap.Cell r = c.split (right, 20, newTracker ());
    r.free (newTracker ());
    assertCells ("[0-F-30[, [30-I-80[, [80-F-100[", memory);
  }

  @Test
  public void free_should_just_mark_as_free_if_left_is_in_use_or_null () throws Fault {
    MemoryHeap memory = new MemoryHeap (100);
    MemoryHeap.Cell r = memory.getFirst ();
    MemoryHeap.Cell c = r.split (left, 70, newTracker ());
    MemoryHeap.Cell l = c.split (left, 20, newTracker ());
    l.free (newTracker ());
    assertCells ("[0-F-20[, [20-I-70[, [70-F-100[", memory);
  }

  @Test
  public void free_should_just_mark_as_free_if_left_and_right_are_in_use () throws Fault {
    MemoryHeap memory = new MemoryHeap (100);
    MemoryHeap.Cell l = memory.getFirst ();
    MemoryHeap.Cell c = l.split (right, 70, newTracker ());
    MemoryHeap.Cell r = c.split (right, 20, newTracker ());
    r.free (newTracker ());
    assertCells ("[0-F-30[, [30-I-80[, [80-F-100[", memory);

    c.free (newTracker ());
    assertCells ("[0-F-100[", memory);
  }

  @Test
  public void free_should_should_merge_left_if_left_is_free () throws Fault {
    MemoryHeap memory = new MemoryHeap (100);
    MemoryHeap.Cell l = memory.getFirst ();
    MemoryHeap.Cell c = l.split (right, 70, newTracker ());
    MemoryHeap.Cell r = c.split (right, 20, newTracker ());
    assertCells ("[0-F-30[, [30-I-80[, [80-I-100[", memory);

    c.free (newTracker ());
    assertCells ("[0-F-80[, [80-I-100[", memory);

    memory.getLast ().free (newTracker ());
    assertCells ("[0-F-100[", memory);
  }

  @Test
  public void free_should_should_merge_right_if_right_is_free () throws Fault {
    MemoryHeap memory = new MemoryHeap (100);
    MemoryHeap.Cell r = memory.getFirst ();
    MemoryHeap.Cell c = r.split (left, 70, newTracker ());
    MemoryHeap.Cell l = c.split (left, 20, newTracker ());
    assertCells ("[0-I-20[, [20-I-70[, [70-F-100[", memory);

    c.free (newTracker ());
    assertCells ("[0-I-20[, [20-F-100[", memory);

    memory.getFirst ().free (newTracker ());
    assertCells ("[0-F-100[", memory);
  }

  @Test
  public void single_fat_cell_should_be_properly_freed () throws Fault {
    MemoryHeap memory = new MemoryHeap (100);
    memory.getLast ().split (right, 100, newTracker ());
    assertCells ("[0-I-100[", memory);

    memory.getLast ().free (newTracker ());
    assertCells ("[0-F-100[", memory);
  }

  @Test
  public void freed_middle_cell_in_ififi_sequence_should_give_ifi_sequence () throws Fault {
    MemoryHeap memory = new MemoryHeap (100);
    MemoryHeap.Cell i1 = memory.getFirst ().split (left, 10, newTracker ());
    MemoryHeap.Cell f1 = memory.getLast ().split (left, 20, newTracker ());
    MemoryHeap.Cell i2 = memory.getLast ().split (left, 40, newTracker ());
    MemoryHeap.Cell f2 = memory.getLast ().split (left, 20, newTracker ());
    MemoryHeap.Cell i3 = memory.getLast ().split (left, 10, newTracker ());
    f1.free (newTracker ());
    f2.free (newTracker ());
    assertCells ("[0-I-10[, [10-F-30[, [30-I-70[, [70-F-90[, [90-I-100[", memory);

    i2.free (newTracker ());
    assertCells ("[0-I-10[, [10-F-90[, [90-I-100[", memory);
  }

  @Test(expected = Fault.class)
  public void freeing_free_cell_should_throw () throws Fault {
    new MemoryHeap (100).getFirst ().free (newTracker ());
  }

  public void assertCells (String expected, MemoryHeap memory) {
    assertEquals (expected, toStringFromFirst (memory));
    List<String> cells = asList (toStringFromLast (memory).split (", "));
    reverse (cells);
    assertEquals (expected, cells.stream ().collect (joining (", ")));
    if (cells.size () == 1)
      assertSame (memory.getFirst (), memory.getLast ());
  }
}
