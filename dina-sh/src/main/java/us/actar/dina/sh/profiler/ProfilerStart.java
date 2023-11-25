package us.actar.dina.sh.profiler;

import us.actar.dina.sh.Command;
import us.actar.dina.sh.Context;

import java.util.Scanner;

public class ProfilerStart extends Command {

  ProfilerStart () {
    super ("Start the profiler.");
  }

  @Override
  public boolean run (Context context, Scanner arguments) {
    return context.executePaused (context1 -> {
      context.getExtension (ProfilerExtension.class).install (context);
      return true;
    });
  }
}
