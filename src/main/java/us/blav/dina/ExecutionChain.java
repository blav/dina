package us.blav.dina;

public interface ExecutionChain {

  void next (VirtualMachine machine, ProgramState state, Opcode instruction);

}
