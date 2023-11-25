package us.actar.dina.sh.profiler;

import us.actar.dina.sh.Command;
import us.actar.dina.sh.Context;

import java.util.Scanner;

public class ProfilerDump extends Command {

  public ProfilerDump () {
    super ("Dump the profiler.");
  }

  @Override
  public boolean run (Context context, Scanner arguments) {
    context.getExtension (ProfilerExtension.class).dump (context);
    return true;
  }
}
