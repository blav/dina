package us.actar.dina.dql.functions;

import us.actar.dina.Metrics;
import us.actar.dina.ProgramState;
import us.actar.dina.Machine;
import us.actar.dina.dql.EvaluationContext;
import us.actar.dina.dql.Value;

import static java.util.Arrays.stream;

public class Entropy implements Operation {
  @Override
  public Value compute (EvaluationContext context) {
    Machine machine = context.getMachine ();
    ProgramState program = context.getCurrentProgram ();
    return new Value (Metrics.entropy (machine, program));
  }

}
