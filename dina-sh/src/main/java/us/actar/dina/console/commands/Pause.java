package us.actar.dina.console.commands;

import us.actar.dina.console.Command;
import us.actar.dina.console.Context;
import us.actar.dina.console.MainLoop;

import java.util.Scanner;

public class Pause implements Command {
  @Override
  public boolean run (Context context, Scanner arguments) {
    MainLoop machine = context.getLoop ();
    MainLoop.State state = machine.getActualState ();
    if (state == MainLoop.State.paused) {
      context.getOut ().println ("Machine already paused.");
    } else if (state == MainLoop.State.stopped) {
      context.getOut ().println ("Machine already stopped.");
    } else {
      machine.requestState (MainLoop.State.paused);
      System.out.println ("Machine paused.");
    }

    return true;
  }
}
