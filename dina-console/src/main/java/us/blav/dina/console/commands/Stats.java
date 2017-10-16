package us.blav.dina.console.commands;

import us.blav.dina.Program;
import us.blav.dina.console.Command;
import us.blav.dina.console.Context;
import us.blav.dina.console.MainLoop;

import java.util.Collection;
import java.util.Scanner;

import static java.util.Comparator.comparingInt;
import static us.blav.dina.console.MainLoop.State.paused;

public class Stats implements Command {
  @Override
  public boolean run (Context context, Scanner arguments) {
    MainLoop loop = context.getLoop ();
    MainLoop.State state = loop.getActualState ();
    try {
      loop.requestState (paused);
      Collection<Program> programs = loop.getMachine ().getPrograms ();
      context.getOut ().printf ("%d running programs\n", programs.size ());
      context.getOut ().printf ("oldest program %d\n", programs.stream ()
        .min (comparingInt (Program::getId))
        .map (Program::getId).orElse (0));

      context.getOut ().printf ("youngest program %d\n", programs.stream ()
        .max (comparingInt (Program::getId))
        .map (Program::getId).orElse (0));

      return true;
    } finally {
      loop.requestState (state);
    }
  }
}
