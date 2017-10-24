package us.actar.commons;

import com.google.inject.TypeLiteral;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static java.lang.Math.max;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static us.actar.commons.Injector.getMap;

public class Chain<IN> {

  @FunctionalInterface
  public interface Filter<IN> {

    void next (Chain<IN> chain, IN in);

  }

  private final TypeLiteral<Filter<IN>> type;

  private final List<Filter<IN>> filters;

  private int current;

  public interface Handle {

    void uninstall ();

  }

  public Chain (TypeLiteral<Filter<IN>> type) {
    this.type = type;
    this.filters = new ArrayList<> ();
  }

  public Handle install (Filter<IN> filter) {
    if (filter == null)
      throw new NullPointerException ();

    // wrap to prevent Handler.uninstall() failure when same filter is inserted multiple times
    Filter<IN> f = (chain, payload) -> filter.next (chain, payload);
    this.filters.add (max (this.filters.size () - 1, 0), f);
    return () -> {
      this.filters.remove (f);
    };
  }

  public List<Handle> installAll (List<String> filters) {
    Map<String, Filter<IN>> map = getMap (TypeLiteral.get (String.class), type);
    return filters.stream ().map (filter -> ofNullable (map.get (filter))
      .map (this::install).orElseThrow (() -> new NoSuchElementException (filter)))
      .collect (toList ());
  }

  public void next (IN in) {
    if (filters.size () == 0)
      throw new IllegalStateException ("no filter");

    if (this.current == filters.size ()) {
      this.current = 0;
    } else {
      this.filters.get (current ++).next (this, in);
    }
  }
}
