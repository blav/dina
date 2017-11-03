package us.actar.dina.console.commands;

import us.actar.commons.Chain;
import us.actar.dina.ExecutionStep;
import us.actar.dina.MachineFilters;
import us.actar.dina.console.Context;

import java.util.HashMap;
import java.util.Map;

public class DebugContext implements Context.Attribute, MachineFilters {

  private final Map<Long, Chain.Handle> handles;

  public DebugContext () {
    this.handles = new HashMap<> ();
  }

  public void install (Context context, long pid) {
    if (handles.containsKey (pid)) {
      context.getErr ().printf ("program %d already in debug mode.\n", pid);
    } else {
      this.handles.put (pid, context.getLoop ().getMachine ().install (this));
    }
  }

  public void uninstall (Context context, long pid) {
    Chain.Handle handle = handles.remove (pid);
    if (handle == null) {
      context.getErr ().printf ("program %d not in debug mode.\n", pid);
    } else {
      handle.uninstall ();
    }
  }

  @Override
  public void close () {
    handles.values ().forEach (Chain.Handle::uninstall);
  }

  @Override
  public Chain.Filter<ExecutionStep> getExecutionFilter () {
    return (chain, executionStep) -> {

    };
  }
}
