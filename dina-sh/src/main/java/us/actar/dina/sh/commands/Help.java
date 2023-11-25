package us.actar.dina.sh.commands;

import us.actar.dina.sh.Command;
import us.actar.dina.sh.CommandsRegistry;
import us.actar.dina.sh.Context;

import java.util.Map;
import java.util.Scanner;

public class Help extends Command {

  public Help () {
    super ("Prints this message.");
  }

  @Override
  public boolean run (Context context, Scanner arguments) {
    CommandsRegistry.getCommands ().entrySet ()
      .stream ()
      .sorted (Map.Entry.comparingByKey ())
      .map (e -> String.format ("  %-20s %s", e.getKey (), e.getValue ().getDescription ()))
      .forEach (context.getOut ()::println);

    return true;
  }

}
