package us.actar.dina.console.commands;

import us.actar.dina.console.Command;
import us.actar.dina.console.Context;

import java.util.Scanner;

public class DebugStop implements Command {
  @Override
  public boolean run (Context context, Scanner arguments) {
    return context.executePaused (c -> {
      context.getAttribute (DebugContext.class).uninstall (context, arguments.nextLong ());
      return true;
    });
  }
}
