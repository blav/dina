package us.blav.dina.console;

import java.util.Scanner;

public interface Command {

  boolean run (Context context, Scanner arguments);

}
