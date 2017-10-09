package us.blav.dina;

import us.blav.dina.InstructionRegistry.RegisteredInstruction;

public interface ExecutionChain {

  void next (VirtualMachine machine, ProgramState state, RegisteredInstruction instruction);

}
