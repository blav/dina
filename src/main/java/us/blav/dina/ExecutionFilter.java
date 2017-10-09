package us.blav.dina;

public interface ExecutionFilter {

  void next (ExecutionChain chain, VirtualMachine machine, ProgramState state, Opcode opcode);

}
