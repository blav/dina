package us.actar.dina;

public interface Instruction {

  void process (Machine machine, Program state) throws Fault;

}
