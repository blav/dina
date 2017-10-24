package us.actar.dina.console.commands;

import us.actar.dina.Heap;
import us.actar.dina.console.Command;
import us.actar.dina.ProgramState;
import us.actar.dina.Machine;
import us.actar.dina.console.Context;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Dump implements Command {

  public static <A extends Appendable> A dump (Machine machine, ProgramState state, A out) throws IOException {
    Heap.Cell cell = state.getCell ();
    for (int i = 0; i < cell.getSize (); i++) {
      int offset = cell.getOffset () + i;
      out.append (String.format ("%08d %-30s\n", offset,
        machine.getRegistry ().getInstruction (machine.getHeap ().get (offset)).getSymbol ()));
    }

    return out;
  }

  @Override
  public boolean run (Context context, Scanner scanner) {
    try {
      int pid = scanner.nextInt ();
      ProgramState p = context.getLoop ().getMachine ().getProgram (pid);
      if (p == null) {
        context.getErr ().printf ("no such program %d\n", pid);
      } else {
        dump (context.getLoop ().getMachine (), p, context.getOut ());
      }
    } catch (InputMismatchException e) {
      context.getErr ().printf ("The dump command expects an integer program id.\n");
    } catch (IOException e) {
      context.getErr ().printf ("Unexpected error: %s.\n", e.getMessage ());
    }

    return true;
  }
}
