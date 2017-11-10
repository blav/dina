package us.actar.dina.sh.profiler;

import us.actar.commons.Chain;
import us.actar.commons.Handle;
import us.actar.commons.Profiler;
import us.actar.commons.Profiler.Summary;
import us.actar.dina.Extension;
import us.actar.dina.Machine;
import us.actar.dina.Opcode;
import us.actar.dina.sh.Context;

import java.util.Collection;
import java.util.Map;

import static java.util.Comparator.comparingLong;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class ProfilerExtension implements Context.Extension {

  private class Session implements Extension, AutoCloseable {

    private final Handle handle;

    private final Profiler profiler;

    private Session (Context context) {
      Machine machine = context.getLoop ().getMachine ();
      this.handle = machine.install (this);
      this.profiler = new Profiler ();
      Collection<Opcode> opcodes = machine.getInstructionSet ().getOpcodes ();
      opcodes.stream ().forEach (o -> profiler.addTimer (o.getSymbol (), () -> o.getOpcode ()));
      int defaultTimer = opcodes.stream ().mapToInt (Opcode::getOpcode).max ().orElse (0) + 1;
      this.profiler.addTimer ("default", () -> defaultTimer);
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
      handle.uninstall ();
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

  public void dump (Context context) {
    if (this.session == null) {
      context.getErr ().println ("Profiler not enabled.");
      return;
    }

    Map<String, Summary> timers = session.profiler.getSummary (MILLISECONDS);
    long total = timers.values ().stream ().mapToLong (Summary::getElapsed).sum ();
    timers.entrySet ().stream ()
      .sorted (comparingLong ((Map.Entry<String, Summary> e) -> e.getValue ().getElapsed ())
        .reversed ()
        .thenComparing (Map.Entry::getKey))
      .forEach (e -> {
        long elapsed = e.getValue ().getElapsed ();
        long count = e.getValue ().getCount ();
        context.getOut ().printf ("%-20s %7d %6d%% %10d   %5.5f\n",
          e.getKey (),
          elapsed,
          total == 0 ? 0 : (100 * elapsed) / total,
          count,
          count == 0 ? 0 : elapsed / (double) count
        );
      });
  }

  @Override
  public void close () {
    if (session != null) {
      session.close ();
      session = null;
    }
  }
}

