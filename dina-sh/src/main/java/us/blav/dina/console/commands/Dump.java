package us.blav.dina.console.commands;

import us.blav.dina.MemoryHeap;
import us.blav.dina.Program;
import us.blav.dina.ProgramState;
import us.blav.dina.VirtualMachine;
import us.blav.dina.console.Command;
import us.blav.dina.console.Context;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Dump implements Command {

  public static <A extends Appendable> A dump (VirtualMachine machine, ProgramState state, A out) throws IOException {
    MemoryHeap.Cell cell = state.getCell ();
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
