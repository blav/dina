package us.actar.dina.reclaimers;

import us.actar.dina.*;

import java.util.Comparator;
import java.util.function.ToDoubleFunction;

import static java.util.Comparator.comparingDouble;

public class FaultAgingPenalty extends ComparatorBasedPenalty {

  public FaultAgingPenalty (Machine machine) {
    super (machine, comparingDouble (
      (ToDoubleFunction<Program>) p -> p.getFaults () + p.getCycles ()).reversed ());
  }
}
