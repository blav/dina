package us.actar.dina.extensions;

import us.actar.commons.Chain.Filter;
import us.actar.dina.Extension;
import us.actar.dina.Program;
import us.actar.dina.is.is1.IS1Registers;

import java.util.stream.IntStream;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;

public class TraceInstructions implements Extension {

  @Override
  public Filter<Execute> getExecuteFilter () {
    return (chain, execute) -> {
      try {
        chain.next (execute);
      } finally {
        Program state = execute.getState ();
        System.out.printf ("%08d %-30s ip=%08d  regs=%s\n",
          state.getId (),
          execute.getOpcode ().getSymbol (),
          state.getInstructionPointer (),
          IntStream.of (((IS1Registers) state.getRegisters ()).getRegisters ())
            .boxed ().map (r -> format ("%012d", r)).collect (joining (" "))
        );
      }
    };
  }
}
