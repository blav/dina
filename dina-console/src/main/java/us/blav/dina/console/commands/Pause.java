package us.blav.dina.console.commands;

import us.blav.dina.console.Command;
import us.blav.dina.console.Context;
import us.blav.dina.console.MainLoop;

import java.util.Scanner;

import static us.blav.dina.console.MainLoop.State.paused;
import static us.blav.dina.console.MainLoop.State.stopped;

public class Pause implements Command {
  @Override
  public boolean run (Context context, Scanner arguments) {
    MainLoop machine = context.getLoop ();
    MainLoop.State state = machine.getActualState ();
    if (state == paused) {
      context.getOut ().println ("Machine already paused.");
    } else if (state == stopped) {
      context.getOut ().println ("Machine already stopped.");
    } else {
      machine.requestState (paused);
      System.out.println ("Machine paused.");
    }

    return true;
  }
}
