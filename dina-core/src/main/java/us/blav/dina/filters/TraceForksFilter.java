package us.blav.dina.filters;

import us.blav.commons.Chain;
import us.blav.commons.Chain.Filter;
import us.blav.dina.*;

public class TraceForksFilter implements Filter<ExecutionStep> {
  @Override
  public void next (Chain<ExecutionStep> chain, ExecutionStep step) {
    chain.next (new ExecutionStepDecorator (step) {
      @Override
      public VirtualMachine getMachine () {
        return new VirtualMachineDecorator (step.getMachine ()) {
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
