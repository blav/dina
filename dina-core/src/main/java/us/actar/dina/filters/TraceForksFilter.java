package us.actar.dina.filters;

import us.actar.commons.Chain;
import us.actar.commons.Chain.Filter;
import us.actar.dina.*;

public class TraceForksFilter implements Filter<ExecutionStep> {
  @Override
  public void next (Chain<ExecutionStep> chain, ExecutionStep step) {
    chain.next (new ExecutionStepDecorator (step) {
      @Override
      public Machine getMachine () {
        return new MachineDecorator (step.getMachine ()) {
          @Override
          public long launch (ProgramState state) {
            long pid = super.launch (state);
            System.out.printf ("launched program %d - size=%d\n", pid, state.getCell ().getSize ());
            return pid;
          }
        };
      }
    });
  }
}
