package us.actar.dina.dql.functions;

import us.actar.dina.dql.EvaluationContext;
import us.actar.dina.dql.Value;

public class Id implements Operation {
  @Override
  public Value compute (EvaluationContext context) {
    return new Value ((long) context.getCurrentProgram ().getId ());
  }
}
