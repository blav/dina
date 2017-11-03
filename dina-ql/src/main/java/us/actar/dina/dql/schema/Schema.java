package us.actar.dina.dql.schema;

import us.actar.dina.dql.EvaluableFunction;
import us.actar.dina.dql.functions.*;

import java.util.HashMap;
import java.util.Map;

import static java.util.Arrays.stream;
import static us.actar.dina.dql.schema.PrimitiveType.LONG;
import static us.actar.dina.dql.schema.PrimitiveType.STRING;

public class Schema {

  public static final String TABLE_PROGRAMS = "programs";

  public static final Column PROGRAM_ID = new Column ("id",
    new EvaluableFunction (LONG, new Id ()::compute));

  public static final Column PROGRAM_FAULTS = new Column ("faults",
    new EvaluableFunction (LONG, new Faults ()::compute));

  public static final Column PROGRAM_CYCLES = new Column ("cycles",
    new EvaluableFunction (LONG, new Cycles ()::compute));

  public static final Column PROGRAM_FORKS = new Column ("forks",
    new EvaluableFunction (LONG, new Forks ()::compute));

  public static final Column PROGRAM_SIZE = new Column ("size",
    new EvaluableFunction (LONG, new Size ()::compute));

  public static final Column PROGRAM_POSITION = new Column ("position",
    new EvaluableFunction (LONG, new Position ()::compute));

  public static final Column PROGRAM_IP = new Column ("ip",
    new EvaluableFunction (LONG, new Ip ()::compute));

  public static final Column PROGRAM_ENTROPY = new Column ("entropy",
    new EvaluableFunction (LONG, new Entropy ()::compute));

  public static final Column PROGRAM_CODE = new Column ("code",
    new EvaluableFunction (STRING, new Code ()::compute));

  public static final Table PROGRAM = new Table (TABLE_PROGRAMS,
    PROGRAM_ID,
    PROGRAM_SIZE,
    PROGRAM_CYCLES,
    PROGRAM_FORKS,
    PROGRAM_FAULTS,
    PROGRAM_POSITION,
    PROGRAM_IP,
    PROGRAM_ENTROPY,
    PROGRAM_CODE
  );

  public static final Schema SCHEMA = new Schema (PROGRAM);

  private final Map<String, Table> tables;

  private Schema (Table... tables) {
    this.tables = new HashMap<> ();
    stream (tables).forEach (t -> this.tables.put (t.getName ().toLowerCase (), t));
  }

  public Table getTable (String name) {
    return tables.get (name.toLowerCase ());
  }
}
