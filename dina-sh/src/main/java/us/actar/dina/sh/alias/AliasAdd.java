package us.actar.dina.sh.alias;

import org.jline.builtins.Completers;
import us.actar.dina.sh.Command;
import us.actar.dina.sh.Context;

import java.util.Scanner;

import static org.jline.builtins.Completers.TreeCompleter.node;

public class AliasAdd extends Command {

  AliasAdd () {
    super ("Add an alias.");
  }

  @Override
  public boolean run (Context context, Scanner arguments) {
    context.getExtension (AliasExtension.class).addAlias (context, arguments.next (), arguments.nextLine ());
    return true;
  }

  @Override
  public Completers.TreeCompleter.Node getCompletions (String commandName) {
    return node (commandName);
  }
}
