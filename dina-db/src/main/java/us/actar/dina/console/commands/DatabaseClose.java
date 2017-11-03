package us.actar.dina.console.commands;

import us.actar.dina.console.Command;
import us.actar.dina.console.Context;

import java.util.Scanner;

public class DatabaseClose implements Command {
  @Override
  public boolean run (Context context, Scanner arguments) {
    ConnectionPool attribute = context.getAttribute (ConnectionPool.class);
    if (attribute.isOpen () == false) {
      context.getErr ().println ("no open database.");
      return true;
    }

    attribute.close ();
    return true;
  }
}
