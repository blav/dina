package us.blav.dina;

import us.blav.dina.InstructionRegistry.RegisteredInstruction;

import java.util.stream.IntStream;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;

public class TraceInstructionsFilter implements ExecutionFilter {
  @Override
  public void next (ExecutionChain chain, VirtualMachine machine, ProgramState state, RegisteredInstruction instruction) {
    try {
      chain.next (machine, state, instruction);
    } finally {
      System.out.printf ("%08d %-30s ip=%08d  regs=%s\n",
        state.getId (),
        instruction.getSymbol (),
        state.getInstructionPointer (),
        IntStream.of (state.getRegisters ()).boxed ().map (r -> format ("%012d", r)).collect (joining (" "))
      );
    }
  }
}
