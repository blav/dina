package us.blav.dina;

public interface InstructionProcessor {

  void process (VirtualMachine machine, ProgramState state);

}
