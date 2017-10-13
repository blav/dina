package us.blav.dina.filters;

import us.blav.dina.*;
import us.blav.dina.Chain.Filter;

public class TraceForksFilter implements Filter<ExecutionStep> {
  @Override
  public void next (Chain<ExecutionStep> chain, VirtualMachine machine, ExecutionStep step) {
    chain.next (new VirtualMachineDecorator (machine) {
      @Override
      public long launch (ProgramState state) {
        long pid = super.launch (state);
        System.out.printf ("launched program %d - size=%d\n", pid, state.getCell ().getSize ());
        return pid;
      }
    }, step);
  }
}
