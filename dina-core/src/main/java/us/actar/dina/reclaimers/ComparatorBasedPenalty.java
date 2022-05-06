package us.actar.dina.reclaimers;

import us.actar.commons.Chain;
import us.actar.dina.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static java.util.Optional.ofNullable;

public abstract class ComparatorBasedPenalty implements HeapReclaimer {

  protected final Machine machine;

  protected final Comparator<Program> comparator;

  public ComparatorBasedPenalty (Machine machine, Comparator<Program> comparator) {
    this.machine = machine;
    this.comparator = comparator;
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
      Collections.sort (programs, comparator);

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
}
