package us.actar.dina.sh;

import org.jline.builtins.Completers.TreeCompleter.Node;
import org.jline.reader.Candidate;

import java.util.Scanner;

import static org.jline.builtins.Completers.TreeCompleter.node;

public abstract class Command {

  private final String description;

  protected Command () {
    this ("");
  }

  protected Command (String description) {
    this.description = description;
  }

  public abstract boolean run (Context context, Scanner arguments);

  public String getDescription () {
    return description;
  }

  public Node getCompletions (String commandName) {
    return node (new Candidate (
      commandName,
      commandName,
      null,
      null,
      null,
      null,
      true));
  }
}
