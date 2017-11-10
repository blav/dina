package us.actar.dina.sh.commands;

import us.actar.dina.sh.Command;
import us.actar.dina.sh.Context;

import java.util.Scanner;

public class Help implements Command {

  @Override
  public boolean run (Context context, Scanner arguments) {
    context.getOut ().println ("Prints this message.");
    return true;
  }
}
