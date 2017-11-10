package us.actar.dina.reclaimers;

import us.actar.dina.Machine;
import us.actar.dina.Program;

import java.util.Comparator;
import java.util.function.ToDoubleFunction;

import static java.util.Comparator.comparingDouble;
import static java.util.Comparator.comparingInt;

public class CumulativePenalty extends ComparatorBasedPenalty {

  public CumulativePenalty (Machine machine) {
    super (machine, comparingDouble ((ToDoubleFunction<Program>)
      p -> p.getFaults () / (1.0 + p.getCycles ())).reversed ());
  }
}
