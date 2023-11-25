package us.actar.dina.sh.commands;

import org.jline.builtins.Completers;
import us.actar.dina.sh.Command;
import us.actar.dina.sh.Context;
import us.actar.dina.sh.extensions.DatabaseExtension;

import java.util.Scanner;

import static org.jline.builtins.Completers.TreeCompleter.node;


public class DatabaseOption extends Command {

  public DatabaseOption () {
    super ("Set database options.");
  }

  @Override
  public boolean run (Context context, Scanner arguments) {
    DatabaseExtension extension = context.getExtension (DatabaseExtension.class);
    String option = arguments.next ();
    //noinspection SwitchStatementWithTooFewBranches
    switch (option) {
      case "compact":
        try {
          extension.setCompact (arguments.nextBoolean ());
        } catch (Exception e) {
          context.getErr ().println ("Please enter true or false.");
        }
        break;

      default:
        context.getErr ().printf ("No such option %s.\n", option);
    }

    return true;
  }

  @Override
  public Completers.TreeCompleter.Node getCompletions (String commandName) {
    return node (
      commandName,
      node (
        "compact",
        node (
          "true",
          "false"
        )
      )
    );
  }
}
