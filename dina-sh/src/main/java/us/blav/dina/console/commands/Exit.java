package us.blav.dina.console.commands;

import us.blav.dina.console.Command;
import us.blav.dina.console.Context;

import java.util.Scanner;

import static us.blav.dina.console.MainLoop.State.stopped;

public class Exit implements Command {
  @Override
  public boolean run (Context context, Scanner arguments) {
    context.getLoop ().requestState (stopped);
    System.out.println ("Machine stopped.");
    return false;
  }
}
