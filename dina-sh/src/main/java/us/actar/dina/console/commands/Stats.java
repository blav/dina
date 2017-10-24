package us.actar.dina.console.commands;

import us.actar.dina.console.Command;
import us.actar.dina.Program;
import us.actar.dina.console.Context;

import java.util.Collection;
import java.util.Scanner;

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
