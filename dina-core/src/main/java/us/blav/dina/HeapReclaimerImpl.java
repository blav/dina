package us.blav.dina;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.ToIntFunction;

import static java.util.Comparator.comparingInt;
import static java.util.Comparator.comparingLong;
import static java.util.Optional.ofNullable;

public class HeapReclaimerImpl implements HeapReclaimer {

  private static final Comparator<Program> COMPARATOR =
    comparingInt (Program::getFaults).reversed ()
      .thenComparing (comparingInt (Program::getCycles).reversed ());

  private static final Comparator<Program> COMPARATOR2 =
    comparingInt ((ToIntFunction<Program>) p -> p.getFaults () / (1 + p.getCycles ())).reversed ();

  public HeapReclaimerImpl (VirtualMachine machine) {
    this.machine = machine;
  }

  @Override
  public List<Program> reclaim (VirtualMachine machine) {
    MemoryHeap heap = machine.getHeap ();
    Config.Reclaimer reclaimer = this.machine.getConfig ().getReclaimer ();
    if (heap.getUsed () / (double) heap.getTotal () < reclaimer.getThresholdHigh ())
      return Collections.emptyList ();

    double reclaim = heap.getUsed () - reclaimer.getThresholdLow () * heap.getTotal ();
    ArrayList<Program> programs = new ArrayList<> (machine.getPrograms ());
    Collections.sort (programs, COMPARATOR2);

    ArrayList<Program> ret = new ArrayList<> ();
    int size = 0;
    for (Program program : programs) {
      if (size >= reclaim) {
        return ret;
      } else {
        size += program.getCell ().getSize ();
        size += ofNullable (program.getCell ()).map (MemoryHeap.Cell::getSize).orElse (0);
        ret.add (program);
      }
    }

    return ret;
  }

  private final VirtualMachine machine;

}
