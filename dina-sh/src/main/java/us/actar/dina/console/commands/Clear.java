package us.actar.dina.console.commands;

import org.jline.reader.impl.LineReaderImpl;
import us.actar.dina.console.Command;
import us.actar.dina.console.Context;

import java.util.Scanner;

public class Clear implements Command {

  @Override
  public boolean run (Context context, Scanner arguments) {
    ((LineReaderImpl) context.getReader ()).clearScreen ();
    return true;
  }
}
