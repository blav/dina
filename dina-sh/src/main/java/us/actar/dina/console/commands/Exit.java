package us.actar.dina.console.commands;

import us.actar.dina.console.Command;
import us.actar.dina.console.Context;
import us.actar.dina.console.MainLoop;

import java.util.Scanner;

public class Exit implements Command {

  @Override
  public boolean run (Context context, Scanner arguments) {
    context.getLoop ().requestState (MainLoop.State.stopped);
    System.out.println ("Machine stopped.");
    return false;
  }
}
