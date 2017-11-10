package us.actar.dina.sh.commands;

import org.jline.reader.impl.LineReaderImpl;
import us.actar.dina.sh.Command;
import us.actar.dina.sh.Context;

import java.util.Scanner;

public class Clear implements Command {

  @Override
  public boolean run (Context context, Scanner arguments) {
    ((LineReaderImpl) context.getReader ()).clearScreen ();
    return true;
  }
}
