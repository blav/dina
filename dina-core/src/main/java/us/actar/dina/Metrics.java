package us.actar.dina;

import java.util.stream.IntStream;

import static java.lang.Math.log;
import static java.util.Arrays.stream;

public class Metrics {

  public static double entropy (Machine machine, Program program) {
    int base = machine.getConfig ().getInstructionSet ().getInstructionsCount ();
    if (base <= 1)
      return 0.0;

    double[] counts = new double[base];
    program.getCell ().bytes ()
      .map (machine.getHeap ()::get)
      .filter (opcode -> opcode >= 0 && opcode < base)
      .forEach (opcode -> counts[opcode]++);

    return stream (counts)
      .filter (d -> d != 0)
      .map (d -> d / (double) program.getCell ().getSize ())
      .map (p -> - p * log (p))
      .sum () / log ((double) base);
  }

  public static long hash (Machine machine, Program p) {
    Heap heap = machine.getHeap ();
    return p.getCell ().bytes ()
      .asLongStream ()
      .map (i -> heap.get ((int) i) & 255)
      .reduce (1, (h, i) -> h * 31 + i);
  }

}
