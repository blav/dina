package us.blav.dina.console.commands;

import de.vandermeer.asciitable.AT_Context;
import de.vandermeer.asciitable.AsciiTable;
import net.sf.jsqlparser.JSQLParserException;
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

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

public class Select implements Command {

  @Override
  public boolean run (Context context, Scanner arguments) {
    return context.executePaused (c -> {
      try {
        PlainSelect select = new PlainSelectMatcher ().match (
          CCJSqlParserUtil.parse ("select " + arguments.nextLine ()));

        if (select == null)
          throw new IllegalArgumentException ("not a select statement");

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
          .orElse (Long.MAX_VALUE);

        long offset = ofNullable (select.getLimit ())
          .map (Limit::getOffset)
          .map (x -> EvaluableFactory.evaluate (machine, x))
          .map (Value::getLong)
          .orElse (0L);

        Evaluable v = ofNullable (new EvaluableFactory ().create (select.getWhere ()))
          .orElse (new EvaluableConstant (true));

        v.getType ().ensureBoolean ();
        AT_Context asciiContext = new AT_Context ();
        asciiContext.setWidth (160);

        AsciiTable asciiTable = new AsciiTable (asciiContext);
        asciiTable.addRule ();
        asciiTable.addRow (columns.stream ().map (Column::getName).collect (toList ()));
        asciiTable.addRule ();

        machine.getPrograms ().stream ()
          .map (p -> new EvaluationContext (machine, defaultTable, p))
          .filter (ec -> v.evaluate (ec).getBoolean ())
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
