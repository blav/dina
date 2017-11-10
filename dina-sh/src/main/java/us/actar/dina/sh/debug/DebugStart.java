package us.actar.dina.sh.debug;

import us.actar.dina.sh.Command;
import us.actar.dina.sh.Context;

import java.util.Scanner;

public class DebugStart implements Command {

  @Override
  public boolean run (Context context, Scanner arguments) {
    return context.executePaused (c -> {
      int pid = arguments.nextInt ();
      if (context.getExtension (DebugExtension.class).install (context.getLoop ().getMachine (), pid) == false)
        context.getErr ().printf ("program %d already in debug mode.\n", pid);

      return true;
    });
  }
}
