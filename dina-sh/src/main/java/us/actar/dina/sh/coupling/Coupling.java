package us.actar.dina.sh.coupling;

import us.actar.dina.Heap;
import us.actar.dina.Machine;

import java.util.stream.IntStream;

public class Coupling {

  public static int[][] coupling (Machine machine) {
    int count = machine.getConfig ().getInstructionSet ().getInstructionsCount ();

    int[][] coupling = new int[count + 2][];
    IntStream.range (0, count + 2).forEach (i -> coupling[i] = new int[count + 2]);

    int[] weights = new int[count + 2];
    final int first = 0;
    final int last = count + 2;

    Heap heap = machine.getHeap ();
    machine.getPrograms ().forEach (p -> {
      int offset = p.getCell ().getOffset ();
      int size = p.getCell ().getSize ();
      if (size == 0)
        return;

      int prev = first;
      for (int i = 0; i < size + 1; i++) {
        int is;
        if (i == size) {
          is = last - 1;
        } else {
          is = heap.get (offset + i) + 1;
        }

        heap.get (offset + i);
        if (is < 0 || is >= count + 2)
          continue;

        weights[is]++;
        coupling[is][prev]++;
        prev = is;
      }
    });

    return coupling;
  }
}
