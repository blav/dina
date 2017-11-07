package us.actar.dina;

import org.junit.Test;

import java.util.List;
import java.util.stream.StreamSupport;

import static java.util.Arrays.asList;
import static java.util.Collections.reverse;
import static java.util.stream.Collectors.joining;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class MemoryHeapTest {

  private String toStringFromFirst (Heap memory) {
    return StreamSupport.stream (memory.fromFirst ().spliterator (), false)
      .map (Object::toString)
      .collect (joining (", "));
  }

  private String toStringFromLast (Heap memory) {
    return StreamSupport.stream (memory.fromLast ().spliterator (), false)
      .map (Object::toString)
      .collect (joining (", "));
  }

  @Test
  public void initial_cell_should_be_wide_and_free () {
    assertCells ("[0-F-100[", new Heap (100));
  }

  @Test (expected = Fault.class)
  public void split_right_should_not_exceed_cell_size () throws Fault {
    new Heap (100).getFirst ().split (Heap.Direction.right, 101);
  }

  @Test (expected = Fault.class)
  public void split_left_should_not_exceed_cell_size () throws Fault {
    new Heap (100).getFirst ().split (Heap.Direction.left, 101);
  }

  @Test
  public void split_right_all_size_should_take_all_size_and_set_state_inuse () throws Fault {
    Heap memory = new Heap (100);
    memory.getFirst ().split (Heap.Direction.right, 100);
    assertCells ("[0-I-100[", memory);
  }

  @Test
  public void split_left_all_size_should_take_all_size_and_set_state_inuse () throws Fault {
    Heap memory = new Heap (100);
    memory.getFirst ().split (Heap.Direction.left, 100);
    assertCells ("[0-I-100[", memory);
  }

  @Test
  public void split_right_smaller_size_should_split_and_set_state_inuse () throws Fault {
    Heap memory = new Heap (100);
    memory.getFirst ().split (Heap.Direction.right, 90);
    assertCells ("[0-F-10[, [10-I-100[", memory);
  }

  @Test
  public void split_left_smaller_size_should_split_and_set_state_inuse () throws Fault {
    Heap memory = new Heap (100);
    memory.getFirst ().split (Heap.Direction.left, 90);
    assertCells ("[0-I-90[, [90-F-100[", memory);
  }

  @Test
  public void right_splitted_cell_should_not_be_splittable () throws Fault {
    Heap memory = new Heap (100);
    memory.getFirst ().split (Heap.Direction.right, 50).split (Heap.Direction.right, 20);
    assertCells ("[0-F-50[, [50-I-80[, [80-I-100[", memory);
  }

  @Test
  public void left_splitted_cell_should_not_be_splittable () throws Fault {
    Heap memory = new Heap (100);
    memory.getFirst ().split (Heap.Direction.left, 50).split (Heap.Direction.left, 20);
    assertCells ("[0-I-20[, [20-I-50[, [50-F-100[", memory);
  }

  @Test
  public void right_split_between_to_cells_should_preserve_chaining () throws Fault {
    Heap memory = new Heap (100);
    memory.getFirst ().split (Heap.Direction.right, 30);
    memory.getFirst ().split (Heap.Direction.right, 20);
    assertCells ("[0-F-50[, [50-I-70[, [70-I-100[", memory);
  }

  @Test
  public void left_split_between_to_cells_should_preserve_chaining () throws Fault {
    Heap memory = new Heap (100);
    memory.getLast ().split (Heap.Direction.left, 30);
    memory.getLast ().split (Heap.Direction.left, 20);
    assertCells ("[0-I-30[, [30-I-50[, [50-F-100[", memory);
  }

  @Test
  public void free_should_merge_cell_with_left_if_free () throws Fault {
    Heap memory = new Heap (100);
    memory.getFirst ().split (Heap.Direction.right, 30).free ();
    assertCells ("[0-F-100[", memory);
  }

  @Test
  public void free_should_merge_cell_with_right_if_free () throws Fault {
    Heap memory = new Heap (100);
    memory.getFirst ().split (Heap.Direction.left, 30).free ();
    assertCells ("[0-F-100[", memory);
  }

  @Test
  public void free_should_just_mark_as_free_if_right_is_in_use_or_null () throws Fault {
    Heap memory = new Heap (100);
    Heap.Cell l = memory.getFirst ();
    Heap.Cell c = l.split (Heap.Direction.right, 70);
    Heap.Cell r = c.split (Heap.Direction.right, 20);
    r.free ();
    assertCells ("[0-F-30[, [30-I-80[, [80-F-100[", memory);
  }

  @Test
  public void free_should_just_mark_as_free_if_left_is_in_use_or_null () throws Fault {
    Heap memory = new Heap (100);
    Heap.Cell r = memory.getFirst ();
    Heap.Cell c = r.split (Heap.Direction.left, 70);
    Heap.Cell l = c.split (Heap.Direction.left, 20);
    l.free ();
    assertCells ("[0-F-20[, [20-I-70[, [70-F-100[", memory);
  }

  @Test
  public void free_should_just_mark_as_free_if_left_and_right_are_in_use () throws Fault {
    Heap memory = new Heap (100);
    Heap.Cell l = memory.getFirst ();
    Heap.Cell c = l.split (Heap.Direction.right, 70);
    Heap.Cell r = c.split (Heap.Direction.right, 20);
    r.free ();
    assertCells ("[0-F-30[, [30-I-80[, [80-F-100[", memory);

    c.free ();
    assertCells ("[0-F-100[", memory);
  }

  @Test
  public void free_should_should_merge_left_if_left_is_free () throws Fault {
    Heap memory = new Heap (100);
    Heap.Cell l = memory.getFirst ();
    Heap.Cell c = l.split (Heap.Direction.right, 70);
    Heap.Cell r = c.split (Heap.Direction.right, 20);
    assertCells ("[0-F-30[, [30-I-80[, [80-I-100[", memory);

    c.free ();
    assertCells ("[0-F-80[, [80-I-100[", memory);

    memory.getLast ().free ();
    assertCells ("[0-F-100[", memory);
  }

  @Test
  public void free_should_should_merge_right_if_right_is_free () throws Fault {
    Heap memory = new Heap (100);
    Heap.Cell r = memory.getFirst ();
    Heap.Cell c = r.split (Heap.Direction.left, 70);
    Heap.Cell l = c.split (Heap.Direction.left, 20);
    assertCells ("[0-I-20[, [20-I-70[, [70-F-100[", memory);

    c.free ();
    assertCells ("[0-I-20[, [20-F-100[", memory);

    memory.getFirst ().free ();
    assertCells ("[0-F-100[", memory);
  }

  @Test
  public void single_fat_cell_should_be_properly_freed () throws Fault {
    Heap memory = new Heap (100);
    memory.getLast ().split (Heap.Direction.right, 100);
    assertCells ("[0-I-100[", memory);

    memory.getLast ().free ();
    assertCells ("[0-F-100[", memory);
  }

  @Test
  public void freed_middle_cell_in_ififi_sequence_should_give_ifi_sequence () throws Fault {
    Heap memory = new Heap (100);
    Heap.Cell i1 = memory.getFirst ().split (Heap.Direction.left, 10);
    Heap.Cell f1 = memory.getLast ().split (Heap.Direction.left, 20);
    Heap.Cell i2 = memory.getLast ().split (Heap.Direction.left, 40);
    Heap.Cell f2 = memory.getLast ().split (Heap.Direction.left, 20);
    Heap.Cell i3 = memory.getLast ().split (Heap.Direction.left, 10);
    f1.free ();
    f2.free ();
    assertCells ("[0-I-10[, [10-F-30[, [30-I-70[, [70-F-90[, [90-I-100[", memory);

    i2.free ();
    assertCells ("[0-I-10[, [10-F-90[, [90-I-100[", memory);
  }

  @Test (expected = Fault.class)
  public void freeing_free_cell_should_throw () throws Fault {
    new Heap (100).getFirst ().free ();
  }

  public void assertCells (String expected, Heap memory) {
    assertEquals (expected, toStringFromFirst (memory));
    List<String> cells = asList (toStringFromLast (memory).split (", "));
    reverse (cells);
    assertEquals (expected, cells.stream ().collect (joining (", ")));
    if (cells.size () == 1)
      assertSame (memory.getFirst (), memory.getLast ());
  }
}
