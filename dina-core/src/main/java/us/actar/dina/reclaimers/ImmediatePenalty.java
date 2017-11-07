package us.actar.dina.reclaimers;

import us.actar.commons.Chain.Filter;
import us.actar.dina.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;

public class ImmediatePenalty implements HeapReclaimer {

  private final List<Program> programs;

  public ImmediatePenalty (Machine machine) {
    this.programs = new ArrayList<> (machine.getPrograms ());
  }

  @Override
  public Filter<Reclaim> getReclaimFilter () {
    return (chain, payloadReclaim) -> {
      Machine machine = payloadReclaim.getMachine ();
      List<Program> result = payloadReclaim.getReclaimList ();
      Config.Reclaimer config = machine.getConfig ().getReclaimer ();
      Heap heap = machine.getHeap ();
      double actual = heap.getUsed () / (double) heap.getTotal ();
      if (actual < config.getThresholdHigh ())
        return;

      double goal = (actual - config.getThresholdLow ()) * heap.getTotal ();
      double reclaimed = 0;

      Iterator<Program> i = programs.iterator ();
      while (i.hasNext () && reclaimed < goal) {
        Program p = i.next ();
        i.remove ();
        result.add (p);
        reclaimed += p.getCell ().getSize ();
        if (p.getChild () != null)
          reclaimed += p.getChild ().getSize ();
      }
    };
  }

  @Override
  public Filter<Launch> getLaunchFilter () {
    return (chain, payloadLaunch) -> {
      chain.next (payloadLaunch);
      programs.add (payloadLaunch.getProgram ());
    };
  }

  @Override
  public Filter<Kill> getKillFilter () {
    return (chain, kill) -> {
      programs.removeIf (p -> p.getId () == kill.getProgram ());
      chain.next (kill);
    };
  }

  @Override
  public Filter<Execute> getExecuteFilter () {
    return (chain, execute) -> {
      Program program = execute.getState ();
      int faultsBefore = program.getFaults ();
      chain.next (execute);

      int faultsAfter = program.getFaults ();
      if (faultsAfter <= faultsBefore)
        return;

      int index = IntStream.range (0, programs.size ())
        .filter (i -> programs.get (i) == program)
        .findFirst ()
        .orElseThrow (IllegalStateException::new);

      if (index == 0)
        return;

      Program p = programs.get (index - 1);
      programs.set (index - 1, program);
      programs.set (index, p);
    };
  }
}
