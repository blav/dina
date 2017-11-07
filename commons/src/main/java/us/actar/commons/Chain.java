package us.actar.commons;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.max;
import static java.util.Arrays.stream;
import static java.util.Collections.unmodifiableList;

public class Chain<IN> {

  private final List<Filter<IN>> filters;

  private State<IN> state;

  public Chain (Filter<IN>... filters) {
    this.filters = new ArrayList<> ();
    this.state = new State<> (new ArrayList<> ());
    stream (filters).forEach (this::install);
  }

  public synchronized Handle install (Filter<IN> filter) {
    if (filter == null)
      throw new NullPointerException ();

    // wrap to prevent Handler.uninstall() failure when same filter is inserted multiple times
    Filter<IN> f = (chain, payload) -> filter.next (chain, payload);
    this.filters.add (max (this.filters.size () - 1, 0), f);
    this.updateState ();
    return () -> {
      synchronized (Chain.this) {
        this.filters.remove (f);
        this.updateState ();
      }
    };
  }

  public void next (IN in) {
    this.state.next (in);
  }

  private void updateState () {
    this.state = new State<> (unmodifiableList (new ArrayList<> (this.filters)));
  }

  public interface Handle {

    void uninstall ();

  }

  @FunctionalInterface
  public interface Filter<IN> {

    void next (State<IN> chain, IN in);

  }

  public static class State<IN> {

    private final List<Filter<IN>> filters;

    private int current;

    public State (List<Filter<IN>> filters) {
      this.filters = filters;
    }

    public void next (IN in) {
      if (filters.size () == 0)
        throw new IllegalStateException ("no filter");

      if (this.current == filters.size ()) {
        this.current = 0;
      } else {
        this.filters.get (current++).next (this, in);
      }
    }
  }
}

