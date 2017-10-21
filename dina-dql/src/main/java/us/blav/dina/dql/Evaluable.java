package us.blav.dina.dql;


import us.blav.dina.dql.schema.PrimitiveType;

import static us.blav.dina.dql.schema.PrimitiveType.*;

public abstract class Evaluable {

  protected final PrimitiveType type;

  protected Evaluable (PrimitiveType type) {
    this.type = type;
  }

  public Evaluable ensureString () {
    return ensureType (STRING);
  }

  public Evaluable ensureBoolean () {
    return ensureType (BOOLEAN);
  }

  public Evaluable ensureLong () {
    return ensureType (LONG);
  }

  public Evaluable ensureType (PrimitiveType expected) {
    getType ().ensureType (expected);
    return this;
  }

  public PrimitiveType getType () {
    return type;
  }

  public abstract Value evaluate (EvaluationContext context);

}
