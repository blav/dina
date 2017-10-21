package us.blav.dina.dql;

import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.expression.operators.arithmetic.*;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.*;
import net.sf.jsqlparser.statement.select.SubSelect;
import us.blav.dina.VirtualMachine;
import us.blav.dina.dql.schema.Column;
import us.blav.dina.dql.schema.Schema;
import us.blav.dina.dql.schema.Table;

import java.util.Optional;
import java.util.Stack;
import java.util.function.BiFunction;

import static us.blav.dina.dql.schema.PrimitiveType.BOOLEAN;
import static us.blav.dina.dql.schema.PrimitiveType.LONG;
import static us.blav.dina.dql.schema.PrimitiveType.ensureSame;
import static us.blav.dina.dql.schema.Schema.SCHEMA;

public class EvaluableFactory {

  public Evaluable create (Expression expression) {
    if (expression == null)
      return null;

    Stack<Evaluable> stack = new Stack<> ();
    expression.accept (new Visitor (stack));
    if (stack.size () != 1)
      throw new IllegalStateException ("" + stack.size ());

    return stack.pop ();
  }

  public static Value evaluate (VirtualMachine machine, Expression expression) {
    return Optional.ofNullable (expression)
      .map (x -> new EvaluableFactory ().create (x))
      .map (e -> e.evaluate (new EvaluationContext (machine, null, null)))
      .orElse (null);
  }

  private static class Visitor implements ExpressionVisitor {

    private final Stack<Evaluable> stack;

    public Visitor (Stack<Evaluable> stack) {
      this.stack = stack;
    }

    @Override
    public void visit (LongValue longValue) {
      stack.push (new EvaluableConstant (longValue.getValue ()));
    }

    @Override
    public void visit (StringValue stringValue) {
      stack.push (new EvaluableConstant (stringValue.getValue ()));
    }

    @Override
    public void visit (Parenthesis parenthesis) {
      parenthesis.getExpression ().accept (this);
      if (parenthesis.isNot () == false)
        return;

      Evaluable x = stack.pop ().ensureBoolean ();
      stack.push (new EvaluableFunction (BOOLEAN,
        c -> new Value (!x.evaluate (c).getBoolean ())));
    }

    @Override
    public void visit (AndExpression andExpression) {
      combine (andExpression, (left, right) -> {
        left.ensureBoolean ();
        right.ensureBoolean ();
        return new EvaluableFunction (BOOLEAN,
          c -> new Value (left.evaluate (c).getBoolean () && right.evaluate (c).getBoolean ()));
      });
    }

    @Override
    public void visit (OrExpression orExpression) {
      combine (orExpression, (left, right) -> {
        left.ensureBoolean ();
        right.ensureBoolean ();
        return new EvaluableFunction (BOOLEAN,
          c -> new Value (left.evaluate (c).getBoolean () || right.evaluate (c).getBoolean ()));
      });
    }

    @Override
    public void visit (EqualsTo equalsTo) {
      combine (equalsTo, (left, right) -> {
        ensureSame (left.getType (), right.getType ());
        return new EvaluableFunction (BOOLEAN,
          c -> new Value (Value.equals (left.evaluate (c), right.evaluate (c))));
      });
    }

    @Override
    public void visit (NotEqualsTo notEqualsTo) {
      combine (notEqualsTo, (left, right) -> {
        ensureSame (left.getType (), right.getType ());
        return new EvaluableFunction (BOOLEAN,
          c -> new Value (!Value.equals (left.evaluate (c), right.evaluate (c))));
      });
    }

    @Override
    public void visit (GreaterThan greaterThan) {
      combine (greaterThan, (left, right) -> {
        left.ensureLong ();
        right.ensureLong ();
        return new EvaluableFunction (BOOLEAN,
          c -> new Value (left.evaluate (c).getLong () > right.evaluate (c).getLong ()));
      });
    }

    @Override
    public void visit (GreaterThanEquals greaterThanEquals) {
      combine (greaterThanEquals, (left, right) -> {
        left.ensureLong ();
        right.ensureLong ();
        return new EvaluableFunction (BOOLEAN,
          c -> new Value (left.evaluate (c).getLong () >= right.evaluate (c).getLong ()));
      });
    }

    @Override
    public void visit (MinorThan minorThan) {
      combine (minorThan, (left, right) -> {
        left.ensureLong ();
        right.ensureLong ();
        return new EvaluableFunction (BOOLEAN,
          c -> new Value (left.evaluate (c).getLong () < right.evaluate (c).getLong ()));
      });
    }

    @Override
    public void visit (MinorThanEquals minorThanEquals) {
      combine (minorThanEquals, (left, right) -> {
        left.ensureLong ();
        right.ensureLong ();
        return new EvaluableFunction (BOOLEAN,
          c -> new Value (left.evaluate (c).getLong () <= right.evaluate (c).getLong ()));
      });
    }

    @Override
    public void visit (Addition addition) {
      combine (addition, (left, right) -> {
        left.ensureLong ();
        right.ensureLong ();
        return new EvaluableFunction (LONG,
          c -> new Value (left.evaluate (c).getLong () + right.evaluate (c).getLong ()));
      });
    }

    @Override
    public void visit (Division division) {
      combine (division, (left, right) -> {
        left.ensureLong ();
        right.ensureLong ();
        return new EvaluableFunction (LONG,
          c -> new Value (left.evaluate (c).getLong () / right.evaluate (c).getLong ()));
      });
    }

    @Override
    public void visit (Multiplication multiplication) {
      combine (multiplication, (left, right) -> {
        left.ensureLong ();
        right.ensureLong ();
        return new EvaluableFunction (LONG,
          c -> new Value (left.evaluate (c).getLong () * right.evaluate (c).getLong ()));
      });
    }

    @Override
    public void visit (Subtraction subtraction) {
      combine (subtraction, (left, right) -> {
        left.ensureLong ();
        right.ensureLong ();
        return new EvaluableFunction (LONG,
          c -> new Value (left.evaluate (c).getLong () - right.evaluate (c).getLong ()));
      });
    }

    @Override
    public void visit (NotExpression notExpression) {
      notExpression.getExpression ().accept (this);
      Evaluable not = stack.pop ().ensureBoolean ();
      stack.push (new EvaluableFunction (BOOLEAN, c -> new Value (!not.evaluate (c).getBoolean ())));
    }

    @Override
    public void visit (net.sf.jsqlparser.schema.Column tableColumn) {
      String tableName = Optional.ofNullable (tableColumn.getTable ().getName ()).orElse (Schema.TABLE_PROGRAMS);
      Table table = SCHEMA.getTable (tableName);
      if (table == null)
        throw new IllegalArgumentException ("no such table " + tableName);

      Column column = table.getColumn (tableColumn.getName (false));
      if (column == null)
        throw new IllegalArgumentException ("no such column " + tableColumn.getName (false));

      stack.push (new EvaluableFunction (column.getEvaluable ().getType (),
        c -> column.getEvaluable ().evaluate (c)));
    }

    @Override
    public void visit (SignedExpression signedExpression) {
      signedExpression.getExpression ().accept (this);
      if (!signedExpression.equals ('-'))
        return;

      Evaluable x = stack.pop ().ensureLong ();
      stack.push (new EvaluableFunction (LONG, c -> new Value (-x.evaluate (c).getLong ())));
    }

    private void combine (BinaryExpression binary, BiFunction<Evaluable, Evaluable, EvaluableFunction> combiner) {
      binary.getLeftExpression ().accept (this);
      Evaluable left = stack.pop ();

      binary.getRightExpression ().accept (this);
      Evaluable right = stack.pop ();

      stack.push (combiner.apply (left, right));
    }

    @Override
    public void visit (Function function) {
      throw new UnsupportedOperationException ();
    }

    @Override
    public void visit (IsNullExpression isNullExpression) {
      throw new UnsupportedOperationException ();
    }

    @Override
    public void visit (LikeExpression likeExpression) {
      throw new UnsupportedOperationException ();
    }

    @Override
    public void visit (InExpression inExpression) {
      throw new UnsupportedOperationException ();
    }

    @Override
    public void visit (NullValue nullValue) {
      throw new UnsupportedOperationException ();
    }

    @Override
    public void visit (JdbcParameter jdbcParameter) {
      throw new UnsupportedOperationException ();
    }

    @Override
    public void visit (JdbcNamedParameter jdbcNamedParameter) {
      throw new UnsupportedOperationException ();
    }

    @Override
    public void visit (DoubleValue doubleValue) {
      throw new UnsupportedOperationException ();
    }

    @Override
    public void visit (HexValue hexValue) {
      throw new UnsupportedOperationException ();
    }

    @Override
    public void visit (DateValue dateValue) {
      throw new UnsupportedOperationException ();
    }

    @Override
    public void visit (TimeValue timeValue) {
      throw new UnsupportedOperationException ();
    }

    @Override
    public void visit (TimestampValue timestampValue) {
      throw new UnsupportedOperationException ();
    }

    @Override
    public void visit (Between between) {
      throw new UnsupportedOperationException ();
    }

    @Override
    public void visit (SubSelect subSelect) {
      throw new UnsupportedOperationException ();
    }

    @Override
    public void visit (CaseExpression caseExpression) {
      throw new UnsupportedOperationException ();
    }

    @Override
    public void visit (WhenClause whenClause) {
      throw new UnsupportedOperationException ();
    }

    @Override
    public void visit (ExistsExpression existsExpression) {
      throw new UnsupportedOperationException ();
    }

    @Override
    public void visit (AllComparisonExpression allComparisonExpression) {
      throw new UnsupportedOperationException ();
    }

    @Override
    public void visit (AnyComparisonExpression anyComparisonExpression) {
      throw new UnsupportedOperationException ();
    }

    @Override
    public void visit (Concat concat) {
      throw new UnsupportedOperationException ();
    }

    @Override
    public void visit (Matches matches) {
      throw new UnsupportedOperationException ();
    }

    @Override
    public void visit (BitwiseAnd bitwiseAnd) {
      throw new UnsupportedOperationException ();
    }

    @Override
    public void visit (BitwiseOr bitwiseOr) {
      throw new UnsupportedOperationException ();
    }

    @Override
    public void visit (BitwiseXor bitwiseXor) {
      throw new UnsupportedOperationException ();
    }

    @Override
    public void visit (CastExpression cast) {
      throw new UnsupportedOperationException ();
    }

    @Override
    public void visit (Modulo modulo) {
      throw new UnsupportedOperationException ();
    }

    @Override
    public void visit (AnalyticExpression aexpr) {
      throw new UnsupportedOperationException ();
    }

    @Override
    public void visit (WithinGroupExpression wgexpr) {
      throw new UnsupportedOperationException ();
    }

    @Override
    public void visit (ExtractExpression eexpr) {
      throw new UnsupportedOperationException ();
    }

    @Override
    public void visit (IntervalExpression iexpr) {
      throw new UnsupportedOperationException ();
    }

    @Override
    public void visit (OracleHierarchicalExpression oexpr) {
      throw new UnsupportedOperationException ();
    }

    @Override
    public void visit (RegExpMatchOperator rexpr) {
      throw new UnsupportedOperationException ();
    }

    @Override
    public void visit (JsonExpression jsonExpr) {
      throw new UnsupportedOperationException ();
    }

    @Override
    public void visit (JsonOperator jsonExpr) {
      throw new UnsupportedOperationException ();
    }

    @Override
    public void visit (RegExpMySQLOperator regExpMySQLOperator) {
      throw new UnsupportedOperationException ();
    }

    @Override
    public void visit (UserVariable var) {
      throw new UnsupportedOperationException ();
    }

    @Override
    public void visit (NumericBind bind) {
      throw new UnsupportedOperationException ();
    }

    @Override
    public void visit (KeepExpression aexpr) {
      throw new UnsupportedOperationException ();
    }

    @Override
    public void visit (MySQLGroupConcat groupConcat) {
      throw new UnsupportedOperationException ();
    }

    @Override
    public void visit (RowConstructor rowConstructor) {
      throw new UnsupportedOperationException ();
    }

    @Override
    public void visit (OracleHint hint) {
      throw new UnsupportedOperationException ();
    }

    @Override
    public void visit (TimeKeyExpression timeKeyExpression) {
      throw new UnsupportedOperationException ();
    }

    @Override
    public void visit (DateTimeLiteralExpression literal) {
      throw new UnsupportedOperationException ();
    }
  }
}
