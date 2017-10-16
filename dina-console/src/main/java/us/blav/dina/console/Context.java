package us.blav.dina.console;

import java.io.PrintStream;

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
