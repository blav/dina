package us.actar.dina.dql;

import us.actar.dina.dql.schema.PrimitiveType;

import java.util.function.Function;

public class EvaluableFunction extends Evaluable {

  private final Function<EvaluationContext, Value> function;

  public EvaluableFunction (PrimitiveType type, Function<EvaluationContext, Value> function) {
    super (type);
    this.function = function;
  }

  public Value evaluate (EvaluationContext context) {
    return function.apply (context);
  }
}
