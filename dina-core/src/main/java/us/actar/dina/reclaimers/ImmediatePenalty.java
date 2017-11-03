package us.actar.dina.reclaimers;

import us.actar.commons.Chain;
import us.actar.commons.Chain.Filter;
import us.actar.dina.*;

import java.util.*;
import java.util.stream.IntStream;

import static java.util.Optional.of;

public class ImmediatePenalty implements HeapReclaimer, MachineFilters {

  private final List<Program> programs;

  public ImmediatePenalty (Machine machine) {
    this.programs = new ArrayList<> (machine.getPrograms ());
  }

  @Override
  public List<Program> reclaim (Machine machine) {
    Config.Reclaimer config = machine.getConfig ().getReclaimer ();
    Heap heap = machine.getHeap ();
    double actual = heap.getUsed () / (double) heap.getTotal ();
    if (actual < config.getThresholdHigh ())
      return Collections.emptyList ();

    double goal = (actual - config.getThresholdLow ()) * heap.getTotal ();
    ArrayList<Program> ret = new ArrayList<> ();
    double reclaimed = 0;

    Iterator<Program> i = programs.iterator ();
    while (i.hasNext () && reclaimed < goal) {
      Program p = i.next ();
      i.remove ();
      ret.add (p);
      reclaimed += p.getCell ().getSize ();
      if (p.getChild () != null)
        reclaimed += p.getChild ().getSize ();
    }

    return ret;
  }

  @Override
  public Filter<ExecutionStep> getExecutionFilter () {
    return new Filter<ExecutionStep> () {
      @Override
      public void next (Chain<ExecutionStep> chain, ExecutionStep executionStep) {
        ProgramState program = executionStep.getState ();
        int faultsBefore = program.getFaults ();
        chain.next (new ExecutionStepDecorator (executionStep) {
          @Override
          public Machine getMachine () {
            return new MachineDecorator (executionStep.getMachine ()) {
              @Override
              public long launch (ProgramState state) {
                programs.add (state);
                return super.launch (state);
              }

              @Override
              public void kill (int pid) {
                programs.removeIf (p -> p.getId () == pid);
                super.kill (pid);
              }
            };
          }
        });

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
      }
    };
  }
}
