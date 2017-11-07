package us.actar.dina.console.commands;

import us.actar.dina.console.Command;
import us.actar.dina.console.Context;

import java.util.Scanner;

public class Help implements Command {

  @Override
  public boolean run (Context context, Scanner arguments) {
    context.getOut ().println ("Prints this message.");
    return true;
  }
}
