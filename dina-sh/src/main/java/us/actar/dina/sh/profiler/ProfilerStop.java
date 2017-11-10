package us.actar.dina.sh.profiler;

import us.actar.dina.sh.Command;
import us.actar.dina.sh.Context;

import java.util.Scanner;

public class ProfilerStop implements Command {

  @Override
  public boolean run (Context context, Scanner arguments) {
    context.getExtension (ProfilerExtension.class).uninstall (context);
    return true;
  }
}
