package us.blav.dina.console.commands;

import de.vandermeer.asciitable.AT_Context;
import de.vandermeer.asciitable.AsciiTable;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.select.Limit;
import net.sf.jsqlparser.statement.select.PlainSelect;
import us.blav.dina.ProgramState;
import us.blav.dina.VirtualMachine;
import us.blav.dina.console.Command;
import us.blav.dina.console.Context;
import us.blav.dina.dql.*;
import us.blav.dina.dql.schema.Column;
import us.blav.dina.dql.schema.Schema;
import us.blav.dina.dql.schema.Table;

import java.util.*;

import static java.lang.Math.signum;
import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toConcurrentMap;
import static java.util.stream.Collectors.toList;

public class Select implements Command {

  @Override
  public boolean run (Context context, Scanner arguments) {
    return context.executePaused (c -> {
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

        VirtualMachine machine = context.getLoop ().getMachine ();
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

        machine.getPrograms ().stream ()
          .map (p -> new EvaluationContext (machine, defaultTable, p))
          .filter (e -> where.evaluate (e).getBoolean ())
          .sorted (order)
          .skip (offset)
          .limit (limit)
          .forEach (ec -> {
            asciiTable.addRow (columns.stream ()
              .map (col -> col.getEvaluable ().evaluate (ec).toString ())
              .collect (toList ()));

            asciiTable.addRule ();
          });

        System.out.println (asciiTable.render ());
        return true;
      } catch (Exception e) {
        System.err.println (e.getMessage ());
        return true;
      }
    });
  }
}
