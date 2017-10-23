package us.blav.dina.console.commands;

import org.jline.reader.impl.LineReaderImpl;
import us.blav.dina.Program;
import us.blav.dina.console.Command;
import us.blav.dina.console.Context;

import java.io.IOException;
import java.util.Collection;
import java.util.Scanner;

import static java.util.Comparator.comparingInt;

public class Clear implements Command {

  @Override
  public boolean run (Context context, Scanner arguments) {
    ((LineReaderImpl) context.getReader ()).clearScreen ();
    return true;
  }
}
