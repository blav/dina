package us.actar.dina.console.commands;

import de.vandermeer.asciitable.AT_Context;
import de.vandermeer.asciitable.AsciiTable;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.StatementVisitorAdapter;
import net.sf.jsqlparser.statement.select.Limit;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectVisitorAdapter;
import us.actar.dina.console.Command;
import us.actar.dina.console.Context;

import java.io.BufferedReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class DatabaseSelect implements Command {

  @Override
  public boolean run (Context context, Scanner arguments) {
    ConnectionPoolExtension extension = context.getExtension (ConnectionPoolExtension.class);
    if (extension.isOpen () == false) {
      context.getErr ().println ("no open database.");
      return true;
    }

    Statement statement;
    try {
      statement = CCJSqlParserUtil.parse ("select " + arguments.nextLine ());
    } catch (JSQLParserException e) {
      String m = e.getMessage ();
      for (Throwable c = e; c != null && m == null; ) {
        c = c.getCause ();
        m = c.getMessage ();
      }

      context.getErr ().println (m);
      return true;
    }

    ArrayList<PlainSelect> ret = new ArrayList<> ();
    statement.accept (new StatementVisitorAdapter () {
      @Override
      public void visit (Select select) {
        select.getSelectBody ().accept (new SelectVisitorAdapter () {
          @Override
          public void visit (PlainSelect plainSelect) {
            ret.add (plainSelect);
          }
        });
      }
    });

    if (ret.size () != 1) {
      context.getErr ().printf ("statement is not a plain select: %s.\n", statement);
      return true;
    }

    PlainSelect select = ret.get (0);
    Limit limit = select.getLimit ();
    if (limit == null) {
      limit = new Limit ();
      limit.setRowCount (new LongValue (10));
      select.setLimit (limit);
    }

    if (select.getFromItem () == null)
      select.setFromItem (new Table (Schema.TABLE_PROGRAM));

    try (Connection c = extension.getConnection ()) {
      try (PreparedStatement ps = c.prepareStatement (statement.toString ())) {
        ResultSet rs = ps.executeQuery ();
        AT_Context asciiContext = new AT_Context ();
        asciiContext.setWidth (160);
        asciiContext.setLineSeparator ("\n");

        AsciiTable asciiTable = new AsciiTable (asciiContext);
        asciiTable.addRule ();
        asciiTable.addRow (IntStream.range (0, rs.getMetaData ().getColumnCount ())
          .boxed ()
          .map (i -> {
            try {
              return rs.getMetaData ().getColumnName (i + 1);
            } catch (SQLException e) {
              return "error: " + e.getMessage ();
            }
          })
          .collect (toList ())
        );

        asciiTable.addRule ();
        while (rs.next ()) {
          asciiTable.addRow (IntStream.range (0, rs.getMetaData ().getColumnCount ())
            .boxed ()
            .map (i -> {
              try {
                Object o = rs.getObject (i + 1);
                if (o instanceof Clob) {
                  Clob clob = (Clob) o;
                  return new BufferedReader (clob.getCharacterStream ()).lines ().collect (joining ("\n"));
                } else {
                  return String.valueOf (o);
                }
              } catch (SQLException e) {
                return "error: " + e.getMessage ();
              }
            })
            .collect (toList ()));

          asciiTable.addRule ();
        }

        context.getOut ().println (asciiTable.render (context.getReader ().getTerminal ().getWidth ()));
      }
    } catch (SQLException e) {
      context.getErr ().println (e.getMessage ());
      return true;
    }

    return true;
  }
}
