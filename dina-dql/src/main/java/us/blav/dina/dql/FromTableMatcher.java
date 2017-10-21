package us.blav.dina.dql;

import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.FromItemVisitorAdapter;

import java.util.ArrayList;

public class FromTableMatcher {

  public Table match (FromItem from) {
    ArrayList<Table> tables = new ArrayList<> ();
    from.accept (new FromItemVisitorAdapter () {
      @Override
      public void visit (Table table) {
        tables.add (table);
      }
    });

    return tables.size () == 1 ? tables.get (0) : null;
  }
}
