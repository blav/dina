package us.blav.dina.dql;

import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.StatementVisitorAdapter;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectVisitorAdapter;

import java.util.ArrayList;

public class PlainSelectMatcher {
  public PlainSelect match (Statement statement) {
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

    return ret.size () != 1 ? null : ret.get (0);
  }
}
