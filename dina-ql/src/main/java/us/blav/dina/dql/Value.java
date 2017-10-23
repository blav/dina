package us.blav.dina.dql;

import us.blav.dina.dql.schema.PrimitiveType;

import java.util.Objects;

import static us.blav.dina.dql.schema.PrimitiveType.*;

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

  public Value (Double value) {
    this.type = DOUBLE;
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

  public Double getDouble () {
    return (Double) value;
  }

  public Long asLong () {
    type.ensureNumber ();
    return type == LONG ? (Long) value : ((Double) value).longValue ();
  }

  public Double asDouble () {
    type.ensureNumber ();
    return type == DOUBLE ? (Double) value : ((Long) value).doubleValue ();
  }

  @Override
  public String toString () {
    return String.valueOf (value);
  }

  public static Value add (Value left, Value right) {
    PrimitiveType type = mergeNumbers (left.getType (), right.getType ());
    return type == LONG ? new Value (left.asLong () + right.asLong ()) :
      new Value (left.asDouble () + right.asDouble ());
  }

  public static Value substract (Value left, Value right) {
    PrimitiveType type = mergeNumbers (left.getType (), right.getType ());
    return type == LONG ? new Value (left.asLong () - right.asLong ()) :
      new Value (left.asDouble () - right.asDouble ());
  }

  public static Value multiply (Value left, Value right) {
    PrimitiveType type = mergeNumbers (left.getType (), right.getType ());
    return type == LONG ? new Value (left.asLong () * right.asLong ()) :
      new Value (left.asDouble () * right.asDouble ());
  }

  public static Value divide (Value left, Value right) {
    PrimitiveType type = mergeNumbers (left.getType (), right.getType ());
    return type == LONG ? new Value (left.asLong () / right.asLong ()) :
      new Value (left.asDouble () / right.asDouble ());
  }

  public static Value greaterThan (Value left, Value right) {
    PrimitiveType type = mergeNumbers (left.getType (), right.getType ());
    return type == LONG ? new Value (left.asLong () > right.asLong ()) :
      new Value (left.asDouble () > right.asDouble ());
  }

  public static Value greaterEqualsTham (Value left, Value right) {
    PrimitiveType type = mergeNumbers (left.getType (), right.getType ());
    return type == LONG ? new Value (left.asLong () >= right.asLong ()) :
      new Value (left.asDouble () >= right.asDouble ());
  }

  public static Value minorThan (Value left, Value right) {
    PrimitiveType type = mergeNumbers (left.getType (), right.getType ());
    return type == LONG ? new Value (left.asLong () < right.asLong ()) :
      new Value (left.asDouble () < right.asDouble ());
  }

  public static Value minorEqualsTham (Value left, Value right) {
    PrimitiveType type = mergeNumbers (left.getType (), right.getType ());
    return type == LONG ? new Value (left.asLong () <= right.asLong ()) :
      new Value (left.asDouble () <= right.asDouble ());
  }

  public static boolean equals (Value left, Value right) {
    switch (ensureSame (left.getType (), right.getType ())) {
      case STRING:
        return Objects.equals (left.getString (), right.getString ());
      case LONG:
        return Objects.equals (left.getLong (), right.getLong ());
      case BOOLEAN:
        return Objects.equals (left.getBoolean (), right.getBoolean ());
      case DOUBLE:
        return Objects.equals (left.getDouble (), right.getDouble ());
      default:
        throw new IllegalStateException ();
    }
  }
}
