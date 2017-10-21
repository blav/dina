package us.blav.dina.console;

import java.io.PrintStream;
import java.util.function.Function;

import static us.blav.dina.console.MainLoop.State.paused;

public class Context {

  private final MainLoop loop;

  private final Stats stats;

  private final PrintStream out;

  private final PrintStream err;

  public Context (MainLoop loop, Stats stats) {
    this.loop = loop;
    this.stats = stats;
    this.out = System.out;
    this.err = System.err;
  }

  public boolean executePaused (Function<Context, Boolean> action) {
    MainLoop loop = getLoop ();
    MainLoop.State state = loop.getActualState ();
    try {
      loop.requestState (paused);
      return action.apply (this);
    } finally {
      loop.requestState (state);
    }
  }

  public MainLoop getLoop () {
    return loop;
  }

  public Stats getStats () {
    return stats;
  }

  public PrintStream getErr () {
    return err;
  }

  public PrintStream getOut () {
    return out;
  }
}
