package us.actar.dina.sh.commands;

import us.actar.dina.sh.Command;
import us.actar.dina.sh.Context;
import us.actar.dina.sh.MainLoop;

import java.util.Scanner;

public class Exit extends Command {

  public Exit () {
    super ("Exit the machine.");
  }

  @Override
  public boolean run (Context context, Scanner arguments) {
    context.getLoop ().requestState (MainLoop.State.stopped);
    System.out.println ("Machine stopped.");
    return false;
  }
}
