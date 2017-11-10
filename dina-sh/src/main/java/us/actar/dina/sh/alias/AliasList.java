package us.actar.dina.sh.alias;

import us.actar.dina.sh.Command;
import us.actar.dina.sh.Context;

import java.util.Map;
import java.util.Scanner;

import static java.util.Comparator.comparing;

public class AliasList implements Command {

  @Override
  public boolean run (Context context, Scanner arguments) {
    context.getExtension (AliasExtension.class).getAliases ().getAliases ().entrySet ().stream ()
      .sorted (comparing (Map.Entry<String, String>::getKey))
      .forEach (e -> System.out.printf ("%-20s%s\n", e.getKey (), e.getValue ()));

    return true;
  }
}
