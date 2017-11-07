package us.actar.dina.console.commands;

import us.actar.dina.console.Command;
import us.actar.dina.console.Context;

import java.util.Scanner;

public class DatabaseClose implements Command {

  @Override
  public boolean run (Context context, Scanner arguments) {
    ConnectionPoolExtension extension = context.getExtension (ConnectionPoolExtension.class);
    if (extension.isOpen () == false) {
      context.getErr ().println ("no open database.");
      return true;
    }

    extension.close ();
    return true;
  }
}
