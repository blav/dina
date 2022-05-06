package us.actar.dina.sh.profiler;

import us.actar.commons.Chain;
import us.actar.commons.Disposable;
import us.actar.commons.Profiler;
import us.actar.commons.Profiler.Summary;
import us.actar.dina.Extension;
import us.actar.dina.Machine;
import us.actar.dina.Opcode;
import us.actar.dina.sh.Context;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Comparator.comparingLong;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class ProfilerExtension implements Context.Extension {

  private class Session implements Extension, AutoCloseable {

    private final Disposable disposable;

    private final Profiler profiler;

    private Session (Context context) {
      Machine machine = context.getLoop ().getMachine ();
      this.disposable = machine.install (this);
      this.profiler = new Profiler ();
      Collection<Opcode> opcodes = machine.getInstructionSet ().getOpcodes ();
      opcodes.stream ().forEach (o -> profiler.addTimer (o.getGroup () + "." + o.getSymbol (), () -> o.getOpcode ()));
      int defaultTimer = opcodes.stream ().mapToInt (Opcode::getOpcode).max ().orElse (0) + 1;
      this.profiler.addTimer ("-.-", () -> defaultTimer);
      this.profiler.setDefaultTimer (defaultTimer);
    }

    @Override
    public Chain.Filter<Execute> getExecuteFilter () {
      return (chain, execute) -> {
        if (profiler == null) {
          chain.next (execute);
          return;
        }

        try (Profiler.Lapse ignored = profiler.profile (execute.getOpcode ().getOpcode ())) {
          chain.next (execute);
        }
      };
    }

    @Override
    public void close () {
      disposable.dispose ();
    }
  }

  private Session session;

  public void install (Context context) {
    if (this.session != null) {
      context.getErr ().println ("Profiler already enabled.");
    } else {
      this.session = new Session (context);
    }
  }

  public void uninstall (Context context) {
    if (this.session == null) {
      context.getErr ().println ("Profiler not enabled.");
    } else {
      close ();
    }
  }

  private static class Entry {

    protected String key;

    private final Summary summary;

    public static Entry fromSymbol (Map.Entry<String, Summary> entry) {
      return new Entry (entry.getKey ().split ("\\.")[0], entry.getValue ());
    }

    public static Entry fromGroup (Map.Entry<String, Summary> entry) {
      return new Entry (entry.getKey (), entry.getValue ());
    }

    public static Entry fromEntry (Map.Entry<String, Summary> entry) {
      return new Entry (entry.getKey ().split ("\\.")[1], entry.getValue ());
    }

    public Entry (String key, Summary summary) {
      this.key = key;
      this.summary = summary;
    }

    public String getKey () {
      return key;
    }

    public Summary getSummary () {
      return summary;
    }

    public long getElapsed () {
      return summary.getElapsed ();
    }

    public long getCount () {
      return summary.getCount ();
    }

    public void print (Context context, long total) {
      context.getOut ().printf ("%-20s %7d %6d%% %10d   %5.5f\n",
        key,
        getElapsed (),
        total == 0 ? 0 : (100 * getElapsed ()) / total,
        getCount (),
        getCount () == 0 ? 0 : getElapsed () / (double) getCount ()
      );
    }
  }

  public void dump (Context context) {
    if (this.session == null) {
      context.getErr ().println ("Profiler not enabled.");
      return;
    }


    System.out.println ("Instruction groups timings");
    System.out.println ("--------------------------");
    Map<String, Summary> timers = session.profiler.getSummary (MILLISECONDS);
    long total = timers.values ().stream ().mapToLong (Summary::getElapsed).sum ();
    timers.entrySet ().stream ()
      .map (Entry::fromSymbol)
      .collect (Collectors.toMap (Entry::getKey, Entry::getSummary, Summary.SUM))
      .entrySet ()
      .stream ()
      .map (Entry::fromGroup)
      .sorted (comparingLong (Entry::getElapsed)
        .reversed ()
        .thenComparing (Entry::getKey))
      .forEach (e -> e.print (context, total));

    System.out.println ("");
    System.out.println ("Instruction timings");
    System.out.println ("-------------------");
    timers.entrySet ().stream ()
      .map (Entry::fromEntry)
      .sorted (comparingLong (Entry::getElapsed)
        .reversed ()
        .thenComparing (Entry::getKey))
      .forEach (e -> e.print (context, total));
    ;
  }

  @Override
  public void close () {
    if (session != null) {
      session.close ();
      session = null;
    }
  }
}

