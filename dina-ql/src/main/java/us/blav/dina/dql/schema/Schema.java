package us.blav.dina.dql.schema;

import us.blav.dina.console.commands.Dump;
import us.blav.dina.dql.EvaluableFunction;
import us.blav.dina.dql.Value;
import us.blav.dina.metrics.Entropy;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.Map;

import static java.util.Arrays.stream;
import static us.blav.dina.dql.schema.PrimitiveType.LONG;
import static us.blav.dina.dql.schema.PrimitiveType.STRING;

public class Schema {

  public static final String TABLE_PROGRAMS = "programs";

  private static final Column PROGRAM_ID = new Column ("id",
    new EvaluableFunction (LONG, c -> new Value ((long) c.getCurrentProgram ().getId ())));

  private static final Column PROGRAM_FAULTS = new Column ("faults",
    new EvaluableFunction (LONG, c -> new Value ((long) c.getCurrentProgram ().getFaults ())));

  private static final Column PROGRAM_CYCLES = new Column ("cycles",
    new EvaluableFunction (LONG, c -> new Value ((long) c.getCurrentProgram ().getCycles ())));

  private static final Column PROGRAM_FORKS = new Column ("forks",
    new EvaluableFunction (LONG, c -> new Value ((long) c.getCurrentProgram ().getForks ())));

  private static final Column PROGRAM_SIZE = new Column ("size",
    new EvaluableFunction (LONG, c -> new Value ((long) c.getCurrentProgram ().getCell ().getSize ())));

  private static final Column PROGRAM_POSITION = new Column ("position",
    new EvaluableFunction (LONG, c -> new Value ((long) c.getCurrentProgram ().getCell ().getOffset ())));

  private static final Column PROGRAM_IP = new Column ("ip",
    new EvaluableFunction (LONG, c -> new Value ((long) c.getCurrentProgram ().getInstructionPointer ())));

  private static final Column PROGRAM_ENTROPY = new Column ("entropy",
    new EvaluableFunction (LONG, c -> new Value (new Entropy ().compute (c.getMachine (), c.getCurrentProgram ()))));

  private static final Column PROGRAM_CODE = new Column ("code",
    new EvaluableFunction (STRING, c -> {
      try {
        return new Value (Dump.dump (c.getMachine (), c.getCurrentProgram (), new StringWriter ()).toString ());
      } catch (IOException e) {
        throw new UncheckedIOException (e);
      }
    }));

  private static final Table PROGRAM = new Table (TABLE_PROGRAMS,
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
