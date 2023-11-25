package us.actar.dina.sh.commands;

import us.actar.dina.sh.Command;
import us.actar.dina.sh.Context;

import java.util.Scanner;

public class Error extends Command {

  private final String command;

  public Error (String command) {
    super ();
    this.command = command;
  }

  @Override
  public boolean run (Context context, Scanner arguments) {
    context.getOut ().printf ("No such command %s.\n", command);
    return true;
  }
}
