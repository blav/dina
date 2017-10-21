package us.blav.dina.dql.schema;

public enum PrimitiveType {

  STRING,
  LONG,
  BOOLEAN,;

  public void ensureString () {
    ensureType (STRING);
  }

  public void ensureBoolean () {
    ensureType (BOOLEAN);
  }

  public void ensureLong () {
    ensureType (LONG);
  }

  public void ensureType (PrimitiveType expected) {
    if (this != expected)
      throw new IllegalArgumentException (this.toString ());
  }

  public static PrimitiveType ensureSame (PrimitiveType left, PrimitiveType right) {
    if (left != right)
      throw new IllegalArgumentException (String.format ("type differ %s != %s", left, right));

    return left;
  }


}
