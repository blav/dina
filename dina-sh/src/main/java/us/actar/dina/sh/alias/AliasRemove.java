package us.actar.dina.sh.alias;

import us.actar.dina.sh.Command;
import us.actar.dina.sh.Context;

import java.util.Scanner;

public class AliasRemove extends Command {

  public AliasRemove () {
    super ("Remove an alias.");
  }

  @Override
  public boolean run (Context context, Scanner arguments) {
    context.getExtension (AliasExtension.class).removeAlias (context, arguments.next ());
    return true;
  }
}
