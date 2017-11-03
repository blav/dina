package us.actar.dina.dql;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Row {

  private final Map<String, Value> values;

  public Row () {
    this.values = new HashMap<> ();
  }

  public void set (String column, Value value) {
    this.values.put (column, value);
  }

  public Value get (String column) {
    return this.values.get (column);
  }

}
