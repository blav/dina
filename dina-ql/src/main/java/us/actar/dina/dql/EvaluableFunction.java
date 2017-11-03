package us.actar.dina.dql;

import us.actar.dina.dql.schema.PrimitiveType;
import us.actar.dina.dql.functions.Operation;

public class EvaluableFunction extends Evaluable {

  private final Operation operation;

  public EvaluableFunction (PrimitiveType type, Operation operation) {
    super (type);
    this.operation = operation;
  }

  public Value evaluate (EvaluationContext context) {
    return operation.compute (context);
  }
}
