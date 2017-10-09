package us.blav.dina;

import java.util.*;

public class InstructionRegistry {

  private final Map<Integer, RegisteredInstruction> opcodes;

  private final Map<String, RegisteredInstruction> symbols;

  public InstructionRegistry (Collection<? extends InstructionFactory> factories) {
    this.opcodes = new HashMap<> ();
    this.symbols = new HashMap<> ();
    factories.stream ().forEach (f -> f.register (this));
  }

  public RegisteredInstruction getInstruction (VirtualMachine machine, int opcode) {
    return Optional.ofNullable (opcodes.get (opcode))
      .orElseGet (() -> new RegisteredInstruction (
        machine.getProcessor ().newUnknownIntruction (opcode), opcode, "unknown" + opcode));
  }

  public RegisteredInstruction getInstruction (String symbol) {
    return Optional.ofNullable (this.symbols.get (symbol))
      .orElseThrow (NoSuchElementException::new);
  }

  public int getOpcode (String symbol) {
    return getInstruction (symbol).getOpcode ();
  }

  public void register (int opcode, String symbol, Instruction instruction, InstructionProcessor.Decorator... flags) {
    for (InstructionProcessor.Decorator flag : flags)
      instruction = flag.decorate (instruction);

    RegisteredInstruction i = new RegisteredInstruction (instruction, opcode, symbol);
    if (this.opcodes.put (opcode, i) != null)
      throw new IllegalStateException ();

    if (this.symbols.put (symbol, i) != null)
      throw new IllegalStateException ();
  }

  public static class RegisteredInstruction {

    private final Instruction instruction;

    private final int opcode;

    private final String symbol;

    public Instruction getInstruction () {
      return instruction;
    }

    public int getOpcode () {
      return opcode;
    }

    public String getSymbol () {
      return symbol;
    }

    private RegisteredInstruction (Instruction instruction, int opcode, String symbol) {
      this.instruction = instruction;
      this.opcode = opcode;
      this.symbol = symbol;
    }
  }
}