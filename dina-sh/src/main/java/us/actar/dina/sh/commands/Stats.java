package us.actar.dina.sh.commands;

import us.actar.dina.Machine;
import us.actar.dina.Program;
import us.actar.dina.sh.Command;
import us.actar.dina.sh.Context;

import java.util.Collection;
import java.util.Scanner;

import static java.util.Comparator.comparingInt;

public class Stats implements Command {

  @Override
  public boolean run (Context context, Scanner arguments) {
    return context.executePaused (c -> {
      Machine machine = c.getLoop ().getMachine ();
      Collection<Program> programs = machine.getPrograms ();
      c.getOut ().printf ("%d running programs\n", programs.size ());
      c.getOut ().printf ("oldest program %d\n", programs.stream ()
        .min (comparingInt (Program::getId))
        .map (Program::getId).orElse (0));

      c.getOut ().printf ("youngest program %d\n", programs.stream ()
        .max (comparingInt (Program::getId))
        .map (Program::getId).orElse (0));

      c.getOut ().printf ("%d cycles\n", machine.getCycles ());

      return true;
    });
  }
}
