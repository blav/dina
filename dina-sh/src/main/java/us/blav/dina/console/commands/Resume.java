package us.blav.dina.console.commands;

import us.blav.dina.console.Command;
import us.blav.dina.console.Context;
import us.blav.dina.console.MainLoop;

import java.util.Scanner;

import static us.blav.dina.console.MainLoop.State.running;
import static us.blav.dina.console.MainLoop.State.stopped;

public class Resume implements Command {
  @Override
  public boolean run (Context context, Scanner arguments) {
    MainLoop machine = context.getLoop ();
    MainLoop.State state = machine.getActualState ();
    if (state == running) {
      context.getOut ().println ("Machine already running.");
    } else if (state == stopped) {
      context.getOut ().println ("Machine already stopped.");
    } else {
      machine.requestState (running);
      System.out.println ("Machine resumed.");
    }

    return true;
  }
}
