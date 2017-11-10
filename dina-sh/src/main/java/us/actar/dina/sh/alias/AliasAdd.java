package us.actar.dina.sh.alias;

import us.actar.dina.sh.Command;
import us.actar.dina.sh.Context;

import java.util.Scanner;

public class AliasAdd implements Command {

  @Override
  public boolean run (Context context, Scanner arguments) {
    context.getExtension (AliasExtension.class).addAlias (context, arguments.next (), arguments.nextLine ());
    return true;
  }
}
