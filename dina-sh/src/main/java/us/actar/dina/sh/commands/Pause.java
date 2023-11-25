package us.actar.dina.sh.commands;

import us.actar.dina.sh.Command;
import us.actar.dina.sh.Context;
import us.actar.dina.sh.MainLoop;

import java.util.Scanner;

public class Pause extends Command {

  public Pause () {
    super ("Pause the machine.");
  }

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
