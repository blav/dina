package us.blav.dina.dql;

import us.blav.dina.Program;
import us.blav.dina.ProgramState;
import us.blav.dina.VirtualMachine;
import us.blav.dina.dql.schema.Table;

public class EvaluationContext {

  private final VirtualMachine machine;

  private final Table defaultTable;

  private final ProgramState program;

  public EvaluationContext (VirtualMachine machine, Table defaultTable, Program program) {
    this.machine = machine;
    this.defaultTable = defaultTable;
    this.program = (ProgramState) program;
  }

  public Table getDefaultTable () {
    return defaultTable;
  }

  public ProgramState getCurrentProgram () {
    return program;
  }

  public VirtualMachine getMachine () {
    return machine;
  }

}
