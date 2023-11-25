package us.actar.dina.sh.commands;

import us.actar.dina.sh.Command;
import us.actar.dina.sh.Context;
import us.actar.dina.sh.extensions.DatabaseExtension;

import java.util.Scanner;

public class DatabaseClose extends Command {

  public DatabaseClose () {
    super ("Close the database.");
  }

  @Override
  public boolean run (Context context, Scanner arguments) {
    DatabaseExtension extension = context.getExtension (DatabaseExtension.class);
    if (!extension.isOpen ()) {
      context.getErr ().println ("no open database.");
      return true;
    }

    extension.close ();
    return true;
  }
}
