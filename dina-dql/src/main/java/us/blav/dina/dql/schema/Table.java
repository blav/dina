package us.blav.dina.dql.schema;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.util.Arrays.stream;

public class Table {

  private final Map<String, Column> columns;

  private final String name;

  public Table (String name, Column... columns) {
    this.name = name;
    this.columns = new LinkedHashMap<> ();
    stream (columns).forEach (c -> this.columns.put (c.getName ().toLowerCase (), c));
  }

  public String getName () {
    return name;
  }

  public Column getColumn (String name) {
    return columns.get (name.toLowerCase ());
  }

  public Collection<Column> getColumns () {
    return columns.values ();
  }
}
