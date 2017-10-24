package us.actar.dina.dql;

import us.actar.dina.Program;
import us.actar.dina.ProgramState;
import us.actar.dina.Machine;

public class EvaluationContext {

  private final Machine machine;

  private final ProgramState program;

  public EvaluationContext (Machine machine, Program program) {
    this.machine = machine;
    this.program = (ProgramState) program;
  }

  public ProgramState getCurrentProgram () {
    return program;
  }

  public Machine getMachine () {
    return machine;
  }

}
