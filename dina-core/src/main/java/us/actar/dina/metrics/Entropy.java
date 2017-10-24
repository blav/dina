package us.actar.dina.metrics;

import us.actar.dina.ProgramState;
import us.actar.dina.Machine;

import static java.lang.Math.log;
import static java.util.Arrays.stream;

public class Entropy {
  public double compute (Machine machine, ProgramState program) {
    int base = machine.getConfig ().getInstructionSet ().getInstructionsCount ();
    if (base <= 1)
      return 0;

    double [] counts = new double [base];
    program.getCell ().bytes ()
      .map (machine.getHeap ()::get)
      .filter (opcode -> opcode >= 00 && opcode < base)
      .forEach (opcode -> counts[opcode] ++);

    return stream (counts)
      .filter (d -> d != 0)
      .map (d -> d / (double) program.getCell ().getSize ())
      .map (p -> -p * log (p))
      .sum () / log ((double) base);
  }
}
