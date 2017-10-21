package us.blav.dina.dql.schema;

import us.blav.dina.dql.Evaluable;

public class Column {

  private final String name;

  private final Evaluable evaluable;

  public Column (String name, Evaluable evaluable) {
    this.name = name;
    this.evaluable = evaluable;
  }

  public String getName () {
    return name;
  }

  public Evaluable getEvaluable () {
    return evaluable;
  }
}
