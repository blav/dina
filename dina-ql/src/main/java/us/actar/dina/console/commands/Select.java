package us.actar.dina.console.commands;

import de.vandermeer.asciitable.AT_Context;
import de.vandermeer.asciitable.AsciiTable;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.select.Limit;
import net.sf.jsqlparser.statement.select.PlainSelect;
import org.jline.builtins.Completers.TreeCompleter;
import us.actar.commons.Injector;
import us.actar.dina.Machine;
import us.actar.dina.ProgramState;
import us.actar.dina.console.Command;
import us.actar.dina.console.Context;
import us.actar.dina.dql.*;
import us.actar.dina.dql.functions.Operation;
import us.actar.dina.dql.schema.Column;
import us.actar.dina.dql.schema.Schema;
import us.actar.dina.dql.schema.Table;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static java.lang.Math.signum;
import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;
import static java.util.stream.Stream.of;
import static org.jline.builtins.Completers.TreeCompleter.node;
import static us.actar.dina.dql.schema.Schema.PROGRAM;

public class Select implements Command {

  @Override
  public boolean run (Context context, Scanner arguments) {
    return context.executePaused ((Context c) -> {
      try {
        PlainSelect select = ofNullable (new PlainSelectMatcher ().match (
          CCJSqlParserUtil.parse ("select " + arguments.nextLine ())))
          .orElseThrow (() -> new IllegalArgumentException ("not a select statement"));

        Table defaultTable = Schema.SCHEMA.getTable (
          ofNullable (select.getFromItem ())
            .map (f -> new FromTableMatcher ().match (f))
            .map (t -> t.getName ())
            .orElse (Schema.TABLE_PROGRAMS));

        List<Column> columns = new SelectColumns ().getColumns (defaultTable, select);

        Machine machine = context.getLoop ().getMachine ();
        long limit = ofNullable (select.getLimit ())
          .map (Limit::getRowCount)
          .map (x -> EvaluableFactory.evaluate (machine, x))
          .map (Value::getLong)
          .orElse (10L);

        long offset = ofNullable (select.getLimit ())
          .map (Limit::getOffset)
          .map (x -> EvaluableFactory.evaluate (machine, x))
          .map (Value::getLong)
          .orElse (0L);

        EvaluableFactory factory = new EvaluableFactory ();
        Evaluable where = ofNullable (factory.create (select.getWhere ()))
          .orElse (new EvaluableConstant (true));

        where.getType ().ensureBoolean ();

        List<Comparator<EvaluationContext>> comparators =
          ofNullable (select.getOrderByElements ()).orElse (emptyList ()).stream ()
            .map (o -> {
              Evaluable e = new EvaluableCache (factory.create (o.getExpression ())).ensureNumber ();
              Comparator<EvaluationContext> comparator =
                (o1, o2) -> (int) signum (e.evaluate (o1).asDouble () - e.evaluate (o2).asDouble ());

              return o.isAsc () ? comparator : comparator.reversed ();
            })
            .collect (toList ());

        comparators.add (Comparator.comparingInt (
          e -> ofNullable (e.getCurrentProgram ()).map (ProgramState::getId).orElse (0)));

        Comparator<EvaluationContext> order = comparators.stream ()
          .reduce (comparators.remove (0), (a, b) -> a.thenComparing (b));

        AT_Context asciiContext = new AT_Context ();
        asciiContext.setWidth (160);
        asciiContext.setLineSeparator ("\n");

        AsciiTable asciiTable = new AsciiTable (asciiContext);
        asciiTable.addRule ();
        asciiTable.addRow (columns.stream ()
          .map (Column::getName)
          .map (String::toUpperCase)
          .collect (toList ()));

        asciiTable.addRule ();

        Map<String, Operation> operations = Injector.getMap (String.class, Operation.class);
        List<EvaluationContext> contexts = machine.getPrograms ().stream ()
          .map (p -> new EvaluationContext (machine, p))
          .filter (e -> where.evaluate (e).getBoolean ())
          .collect (toList ());

        contexts.stream ()
          .sorted (order)
          .skip (offset)
          .limit (limit)
          .forEach (ec -> {
            asciiTable.addRow (columns.stream ()
              .map (col -> col.getEvaluable ().evaluate (ec).toString ())
              .collect (toList ()));

            asciiTable.addRule ();
          });

        System.out.println (asciiTable.render (context.getReader ().getTerminal ().getWidth ()));
        return true;
      } catch (Exception e) {
        System.err.println (e.getMessage ());
        return true;
      }
    });
  }

  @Override
  public TreeCompleter.Node getCompletions (String commandName) {
    return node (
      commandName,
      node (
        concat (
          PROGRAM.getColumns ().stream ().map (Column::getName),
          of ("*")
        ).toArray ()
      )
    );
  }
}
