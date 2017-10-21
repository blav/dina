package us.blav.dina.dql;

import net.sf.jsqlparser.statement.select.*;
import us.blav.dina.dql.schema.Column;
import us.blav.dina.dql.schema.Table;

import java.util.ArrayList;
import java.util.List;

public class SelectColumns {

  public List<Column> getColumns (Table defaultTable, PlainSelect select) {
    ArrayList<Column> list = new ArrayList<> ();
    select.getSelectItems ().forEach ((SelectItem i) -> {
      i.accept (new SelectItemVisitor () {
        @Override
        public void visit (AllColumns allColumns) {
          list.addAll (defaultTable.getColumns ());
        }

        @Override
        public void visit (AllTableColumns allTableColumns) {
          list.addAll (defaultTable.getColumns ());
        }

        @Override
        public void visit (SelectExpressionItem selectExpressionItem) {
          list.add (new Column (selectExpressionItem.toString (),
            new EvaluableFactory ().create (selectExpressionItem.getExpression ())));
        }
      });
    });

    return list;
  };

}
