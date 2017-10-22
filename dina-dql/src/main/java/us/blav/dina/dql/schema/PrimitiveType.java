package us.blav.dina.dql.schema;

import java.util.EnumSet;

public enum PrimitiveType {

  STRING,
  LONG,
  BOOLEAN,
  DOUBLE
  ;

  public void ensureString () {
    ensureType (STRING);
  }

  public void ensureBoolean () {
    ensureType (BOOLEAN);
  }

  public void ensureLong () {
    ensureType (LONG);
  }

  public void ensureDouble () {
    ensureType (DOUBLE);
  }

  public void ensureNumber () {
    ensureType (DOUBLE, LONG);
  }

  public static PrimitiveType mergeNumbers (PrimitiveType left, PrimitiveType right) {
    left.ensureNumber ();
    right.ensureNumber ();
    return left == LONG && right == LONG ? LONG : DOUBLE;
  }

  public void ensureType (PrimitiveType first, PrimitiveType ... others) {
    if (EnumSet.of (first, others).contains (this) == false)
      throw new IllegalArgumentException (this.toString ());
  }

  public static PrimitiveType ensureSame (PrimitiveType left, PrimitiveType right) {
    if (left != right)
      throw new IllegalArgumentException (String.format ("type differ %s != %s", left, right));

    return left;
  }


}
