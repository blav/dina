package us.blav.dina;

import java.util.*;

public class InstructionRegistry {

  private final Map<Integer, Opcode> opcodes;

  private final Map<String, Opcode> symbols;

  public InstructionRegistry (Collection<? extends InstructionFactory> factories) {
    this.opcodes = new HashMap<> ();
    this.symbols = new HashMap<> ();
    factories.stream ().forEach (f -> f.register (this));
  }

  public Opcode getInstruction (VirtualMachine machine, int opcode) {
    return Optional.ofNullable (opcodes.get (opcode))
      .orElseGet (() -> new Opcode (
        machine.getProcessor ().newUnknownIntruction (opcode), opcode, "unknown" + opcode));
  }

  public Opcode getInstruction (String symbol) {
    return Optional.ofNullable (this.symbols.get (symbol))
      .orElseThrow (NoSuchElementException::new);
  }

  public int getOpcode (String symbol) {
    return getInstruction (symbol).getOpcode ();
  }

  public void register (int opcode, String symbol, Instruction instruction, InstructionProcessor.Decorator... flags) {
    for (InstructionProcessor.Decorator flag : flags)
      instruction = flag.decorate (instruction);

    Opcode i = new Opcode (instruction, opcode, symbol);
    if (this.opcodes.put (opcode, i) != null)
      throw new IllegalStateException ();

    if (this.symbols.put (symbol, i) != null)
      throw new IllegalStateException ();
  }

}