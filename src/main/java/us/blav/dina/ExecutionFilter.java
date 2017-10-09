package us.blav.dina;

import us.blav.dina.InstructionRegistry.RegisteredInstruction;

public interface ExecutionFilter {

  void next (ExecutionChain chain, VirtualMachine machine, ProgramState state, RegisteredInstruction instruction);

}
