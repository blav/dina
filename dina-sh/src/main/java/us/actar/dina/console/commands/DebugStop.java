package us.actar.dina.console.commands;

import us.actar.dina.console.Command;
import us.actar.dina.console.Context;

import java.util.Scanner;

public class DebugStop implements Command {

  @Override
  public boolean run (Context context, Scanner arguments) {
    return context.executePaused (c -> {
      int pid = arguments.nextInt ();
      if (context.getExtension (DebugExtension.class).uninstall (pid) == false)
        context.getErr ().printf ("program %d not in debug mode.\n", pid);
      return true;
    });
  }
}
