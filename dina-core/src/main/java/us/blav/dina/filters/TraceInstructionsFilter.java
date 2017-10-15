package us.blav.dina.filters;

import us.blav.commons.Chain;
import us.blav.dina.ExecutionStep;
import us.blav.dina.ProgramState;

import java.util.stream.IntStream;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;

public class TraceInstructionsFilter implements Chain.Filter<ExecutionStep> {
  @Override
  public void next (Chain<ExecutionStep> chain, ExecutionStep step) {
    try {
      chain.next (step);
    } finally {
      ProgramState state = step.getState ();
      System.out.printf ("%08d %-30s ip=%08d  regs=%s\n",
        state.getId (),
        step.getOpcode ().getSymbol (),
        state.getInstructionPointer (),
        IntStream.of (state.getRegisters ()).boxed ().map (r -> format ("%012d", r)).collect (joining (" "))
      );
    }
  }
}
