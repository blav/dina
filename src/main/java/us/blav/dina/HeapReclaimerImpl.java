package us.blav.dina;

import java.util.*;

import static java.util.Comparator.comparingLong;
import static java.util.Optional.ofNullable;

public class HeapReclaimerImpl implements HeapReclaimer {

  public HeapReclaimerImpl (Config config) {
    this.config = config;
  }

  @Override
  public List<Program> reclaim (VirtualMachine machine) {
    MemoryHeap heap = machine.getHeap ();
    Config.Reclaimer reclaimer = config.getReclaimer ();
    if (heap.getUsed () / (double) heap.getTotal () < reclaimer.getThresholdHigh ())
      return Collections.emptyList ();

    double reclaim = heap.getUsed () - reclaimer.getThresholdLow () * heap.getTotal ();
    ArrayList<Program> programs = new ArrayList<> (machine.getPrograms ());
    Collections.sort (programs, comparingLong (Program::getId).reversed ());
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

  private final Config config;

}
