package us.actar.dina.console;

import org.jline.builtins.Completers.TreeCompleter.Node;

import java.util.Scanner;

import static org.jline.builtins.Completers.TreeCompleter.node;

public interface Command {

  boolean run (Context context, Scanner arguments);

  default Node getCompletions (String commandName) {
    return node (commandName);
  }
}
