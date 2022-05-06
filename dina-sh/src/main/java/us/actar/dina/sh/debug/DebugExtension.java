package us.actar.dina.sh.debug;

import us.actar.commons.Chain.Filter;
import us.actar.commons.Disposable;
import us.actar.dina.Extension;
import us.actar.dina.Machine;
import us.actar.dina.Program;
import us.actar.dina.sh.Context;
import us.actar.dina.sh.commands.Dump;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.Map;

import static java.nio.file.Files.newBufferedWriter;
import static java.util.Optional.ofNullable;
import static us.actar.dina.Utils.getSubPath;

public class DebugExtension implements Context.Extension, Extension {

  private final Map<Integer, ProgramDebugger> handles;

  private Disposable disposable;

  public DebugExtension () {
    this.handles = new HashMap<> ();
  }

  @Override
  public void init (Context context) {
    this.disposable = context.getLoop ().getMachine ().install (DebugExtension.this);
  }

  public boolean install (Machine machine, int pid) {
    if (handles.containsKey (pid)) {
      return false;
    } else {
      this.handles.put (pid, new ProgramDebugger (machine, pid));
      return true;
    }
  }

  public boolean uninstall (int pid) {
    ProgramDebugger handle = handles.remove (pid);
    if (handle == null) {
      return false;
    } else {
      handle.close ();
      return true;
    }
  }

  @Override
  public void close () {
    this.handles.values ().forEach (ProgramDebugger::close);
    this.handles.clear ();
    this.disposable.dispose ();
    this.disposable = null;
  }

  @Override
  public Filter<Kill> getKillFilter () {
    return (chain, kill) -> {
      int pid = kill.getProgram ();
      Program state = kill.getMachine ().getProgram (pid);
      ofNullable (handles.get (state.getId ()))
        .ifPresent (d -> {
          d.output.printf ("%8d %8d %8d - program was killed.\n",
            state.getInstructionPointer (), state.getCycles (), state.getFaults ());

          uninstall (pid);
        });

      chain.next (kill);
    };
  }

  @Override
  public Filter<Execute> getExecuteFilter () {
    return (chain, execute) -> {
      Program state = execute.getState ();
      chain.next (execute);
      ofNullable (this.handles.get (state.getId ()))
        .ifPresent (d -> {
          d.output.printf ("%8d %8d %8d - %s\n", state.getInstructionPointer (),
            state.getCycles (), state.getFaults (), execute.getOpcode ().getSymbol ());
        });
    };
  }

  private class ProgramDebugger implements AutoCloseable {

    private PrintWriter output;

    private ProgramDebugger (Machine machine, int pid) {
      try {
        this.output = new PrintWriter (newBufferedWriter (getSubPath ("debug").resolve ("" + pid)));
        this.output.println ("Code");
        Dump.dump (machine, machine.getProgram (pid), this.output);
        this.output.println ();
        this.output.println ("Execution log");
        this.output.flush ();
      } catch (IOException e) {
        throw new UncheckedIOException (e);
      }
    }

    @Override
    public void close () {
      this.output.close ();
      this.output = null;
    }
  }
}
