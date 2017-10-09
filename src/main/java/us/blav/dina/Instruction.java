package us.blav.dina;

public interface Instruction {

  void process (VirtualMachine machine, ProgramState state) throws Fault;

}
