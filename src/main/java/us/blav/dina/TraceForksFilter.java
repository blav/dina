package us.blav.dina;

import us.blav.dina.InstructionRegistry.RegisteredInstruction;

public class TraceForksFilter implements ExecutionFilter {
  @Override
  public void next (ExecutionChain chain, VirtualMachine machine, ProgramState state, RegisteredInstruction instruction) {
    chain.next (new VirtualMachineDecorator (machine) {
      @Override
      public long launch (ProgramState state) {
        long pid = super.launch (state);
        System.out.printf ("lanuched program %d\n", pid);
        return pid;
      }
    }, state, instruction);
  }
}
