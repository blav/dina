package us.blav.dina.filters;

import us.blav.dina.*;

public class TraceForksFilter implements ExecutionFilter {
  @Override
  public void next (ExecutionChain chain, VirtualMachine machine, ProgramState state, Opcode opcode) {
    chain.next (new VirtualMachineDecorator (machine) {
      @Override
      public long launch (ProgramState state) {
        long pid = super.launch (state);
        System.out.printf ("launched program %d - size=%d\n", pid, state.getCell ().getSize ());
        return pid;
      }
    }, state, opcode);
  }
}
