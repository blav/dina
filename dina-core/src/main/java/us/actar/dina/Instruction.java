package us.actar.dina;

public interface Instruction {

  void process (Machine machine, ProgramState state) throws Fault;

}
