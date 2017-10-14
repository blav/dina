package us.blav.dina;

import com.google.inject.TypeLiteral;

import java.util.*;

import static java.lang.Math.max;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static us.blav.dina.Injection.getInstanceMap;

public class Chain<IN> {

  @FunctionalInterface
  public interface Filter<IN> {

    void next (Chain<IN> chain, VirtualMachine machine, IN in);

  }

  private final TypeLiteral<Filter<IN>> type;

  private final VirtualMachine machine;

  private final List<Filter<IN>> filters;

  private int current;

  interface Handle {

    void uninstall ();

  }

  public Chain (TypeLiteral<Filter<IN>> type, VirtualMachine machine) {
    this.type = type;
    this.machine = machine;
    this.filters = new ArrayList<> ();
  }

  public Handle install (Filter<IN> filter) {
    if (filter == null)
      throw new NullPointerException ();

    // wrap to prevent Handler.uninstall() failure when same filter is inserted multiple times
    Filter<IN> f = (chain, machine, payload) -> filter.next (chain, machine, payload);
    this.filters.add (max (this.filters.size () - 1, 0), f);
    return () -> {
      this.filters.remove (f);
    };
  }

  public List<Handle> installAll (List<String> filters) {
    Map<String, Filter<IN>> map = getInstanceMap (TypeLiteral.get (String.class), type);
    return filters.stream ().map (filter -> ofNullable (map.get (filter))
      .map (this::install).orElseThrow (() -> new NoSuchElementException (filter)))
      .collect (toList ());
  }

  public void next (VirtualMachine machine, IN in) {
    if (filters.size () == 0)
      throw new IllegalStateException ("no filter");

    if (this.current == filters.size ()) {
      this.current = 0;
    } else {
      this.filters.get (current ++).next (this, machine, in);
    }
  }
}
