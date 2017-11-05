package us.actar.dina.reclaimers;

import us.actar.commons.Chain;
import us.actar.dina.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.ToDoubleFunction;

import static java.util.Comparator.comparingDouble;
import static java.util.Comparator.comparingInt;
import static java.util.Optional.ofNullable;

public class CumulativePenalty implements HeapReclaimer {

  private static final Comparator<Program> COMPARATOR =
    comparingInt (Program::getFaults).reversed ()
      .thenComparing (comparingInt (Program::getCycles).reversed ());

  private static final Comparator<Program> COMPARATOR2 =
    comparingDouble ((ToDoubleFunction<Program>) p -> p.getFaults () / (1.0 + p.getCycles ())).reversed ();

  public CumulativePenalty (Machine machine) {
    this.machine = machine;
  }

  @Override
  public Chain.Filter<Reclaim> getReclaimFilter () {
    return (chain, payloadReclaim) -> {
      List<Program> result = payloadReclaim.getReclaimList ();
      result.clear ();
      Heap heap = machine.getHeap ();
      Config.Reclaimer reclaimer = payloadReclaim.getMachine ().getConfig ().getReclaimer ();
      if (heap.getUsed () / (double) heap.getTotal () < reclaimer.getThresholdHigh ())
        return;

      double reclaim = heap.getUsed () - reclaimer.getThresholdLow () * heap.getTotal ();
      ArrayList<Program> programs = new ArrayList<> (machine.getPrograms ());
      Collections.sort (programs, COMPARATOR2);

      int size = 0;
      for (Program program : programs) {
        if (size >= reclaim) {
          return;
        } else {
          size += program.getCell ().getSize ();
          size += ofNullable (program.getCell ()).map (Heap.Cell::getSize).orElse (0);
          result.add (program);
        }
      }
    };
  }

  private final Machine machine;

}
