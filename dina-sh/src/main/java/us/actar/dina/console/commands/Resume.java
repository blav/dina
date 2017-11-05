package us.actar.dina.console.commands;

import us.actar.dina.console.Command;
import us.actar.dina.console.Context;
import us.actar.dina.console.MainLoop;

import java.util.Scanner;

public class Resume implements Command {
  @Override
  public boolean run (Context context, Scanner arguments) {
    MainLoop machine = context.getLoop ();
    MainLoop.State state = machine.getActualState ();
    if (state == MainLoop.State.running) {
      context.getOut ().println ("Machine already running.");
    } else if (state == MainLoop.State.stopped) {
      context.getOut ().println ("Machine already stopped.");
    } else {
      machine.requestState (MainLoop.State.running);
      System.out.println ("Machine resumed.");
    }

    return true;
  }
}
