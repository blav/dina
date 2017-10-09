package us.blav.dina.filters;

import us.blav.dina.ExecutionChain;
import us.blav.dina.ExecutionFilter;
import us.blav.dina.Opcode;
import us.blav.dina.ProgramState;
import us.blav.dina.VirtualMachine;

import java.util.stream.IntStream;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;

public class TraceInstructionsFilter implements ExecutionFilter {
  @Override
  public void next (ExecutionChain chain, VirtualMachine machine, ProgramState state, Opcode opcode) {
    try {
      chain.next (machine, state, opcode);
    } finally {
      System.out.printf ("%08d %-30s ip=%08d  regs=%s\n",
        state.getId (),
        opcode.getSymbol (),
        state.getInstructionPointer (),
        IntStream.of (state.getRegisters ()).boxed ().map (r -> format ("%012d", r)).collect (joining (" "))
      );
    }
  }
}
