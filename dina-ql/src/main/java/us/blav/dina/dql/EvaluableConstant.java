package us.blav.dina.dql;

import static us.blav.dina.dql.schema.PrimitiveType.*;

public class EvaluableConstant extends Evaluable {

  private final Value value;

  public EvaluableConstant (String value) {
    super (STRING);
    this.value = new Value (value);
  }

  public EvaluableConstant (Boolean value) {
    super (BOOLEAN);
    this.value = new Value (value);
  }

  public EvaluableConstant (Long value) {
    super (LONG);
    this.value = new Value (value);
  }

  public EvaluableConstant (Double value) {
    super (DOUBLE);
    this.value = new Value (value);
  }


  @Override
  public Value evaluate (EvaluationContext context) {
    return value;
  }
}
