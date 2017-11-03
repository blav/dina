package us.actar.dina.console.commands;

import us.actar.dina.Program;
import us.actar.dina.console.Command;
import us.actar.dina.console.Context;

import java.util.Collection;
import java.util.Scanner;

import static java.util.Comparator.comparingInt;

public class DebugStart implements Command {
  @Override
  public boolean run (Context context, Scanner arguments) {
    return context.executePaused (c -> {
      context.getAttribute (DebugContext.class).install (context, arguments.nextLong ());
      return true;
    });
  }
}
