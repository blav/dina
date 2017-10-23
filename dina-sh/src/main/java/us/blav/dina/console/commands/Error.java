package us.blav.dina.console.commands;

import us.blav.dina.console.Command;
import us.blav.dina.console.Context;

import java.util.Scanner;

public class Error implements Command {

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
