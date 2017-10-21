package us.blav.dina.dql;

import us.blav.dina.dql.schema.PrimitiveType;

import java.util.Objects;

import static us.blav.dina.dql.schema.PrimitiveType.BOOLEAN;
import static us.blav.dina.dql.schema.PrimitiveType.LONG;
import static us.blav.dina.dql.schema.PrimitiveType.STRING;

public class Value {

  private final Object value;

  private final PrimitiveType type;

  public Value (String value) {
    this.type = STRING;
    this.value = value;
  }

  public Value (Boolean value) {
    this.type = BOOLEAN;
    this.value = value;
  }

  public Value (Long value) {
    this.type = LONG;
    this.value = value;
  }

  public PrimitiveType getType () {
    return type;
  }

  public String getString () {
    type.ensureString ();
    return (String) value;
  }

  public Long getLong () {
    type.ensureLong ();
    return (Long) value;
  }

  public Boolean getBoolean () {
    type.ensureBoolean ();
    return (Boolean) value;
  }

  @Override
  public String toString () {
    return String.valueOf (value);
  }

  public static boolean equals (Value left, Value right) {
    PrimitiveType type = PrimitiveType.ensureSame (left.getType (), right.getType ());
    switch (type) {
      case STRING:
        return Objects.equals (left.getString (), right.getString ());
      case LONG:
        return left.getLong () == right.getLong ();
      case BOOLEAN:
        return left.getLong () == right.getLong ();
      default:
        throw new IllegalStateException ();
    }
  }
}
