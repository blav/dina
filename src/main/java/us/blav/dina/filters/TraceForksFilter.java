package us.blav.dina.filters;

import us.blav.dina.*;
import us.blav.dina.Opcode;

public class TraceForksFilter implements ExecutionFilter {
  @Override
  public void next (ExecutionChain chain, VirtualMachine machine, ProgramState state, Opcode opcode) {
    chain.next (new VirtualMachineDecorator (machine) {
      @Override
      public long launch (ProgramState state) {
        long pid = super.launch (state);
        System.out.printf ("lanuched program %d\n", pid);
        return pid;
      }
    }, state, opcode);
  }
}
