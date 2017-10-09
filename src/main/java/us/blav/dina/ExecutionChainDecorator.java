package us.blav.dina;

import us.blav.dina.InstructionRegistry.RegisteredInstruction;

public class ExecutionChainDecorator implements ExecutionChain {

  private final ExecutionChain chain;

  public ExecutionChainDecorator (ExecutionChain chain) {
    this.chain = chain;
  }

  @Override
  public void next (VirtualMachine machine, ProgramState state, RegisteredInstruction instruction) {
    chain.next (machine, state, instruction);
  }
}
