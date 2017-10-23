package us.blav.dina.console.commands;

import us.blav.dina.Program;
import us.blav.dina.console.Command;
import us.blav.dina.console.Context;

import java.util.Collection;
import java.util.Scanner;
import java.util.function.Function;

import static java.util.Comparator.comparingInt;

public class Stats implements Command {
  @Override
  public boolean run (Context context, Scanner arguments) {
    return context.executePaused (c -> {
      Collection<Program> programs = c.getLoop ().getMachine ().getPrograms ();
      c.getOut ().printf ("%d running programs\n", programs.size ());
      c.getOut ().printf ("oldest program %d\n", programs.stream ()
        .min (comparingInt (Program::getId))
        .map (Program::getId).orElse (0));

      c.getOut ().printf ("youngest program %d\n", programs.stream ()
        .max (comparingInt (Program::getId))
        .map (Program::getId).orElse (0));

      return true;
    });
  }
}
