package us.actar.dina;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeSet;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toSet;
import static java.util.stream.IntStream.range;

public class InstructionSet {

  private final TreeSet<Integer> availableOpcodes;

  private final Map<Integer, Opcode> opcodes;

  private final Map<String, Opcode> symbols;

  public InstructionSet () {
    this.opcodes = new HashMap<> ();
    this.symbols = new HashMap<> ();
    this.availableOpcodes = new TreeSet<> (range (0, 256).boxed ().collect (toSet ()));
  }

  public Opcode getInstruction (int opcode) {
    return ofNullable (opcodes.get (opcode))
      .orElseGet (() -> new Opcode (newUnknownIntruction (opcode), opcode, "unknown" + opcode));
  }

  public int getOpcode (String symbol) {
    return getInstruction (symbol).getOpcode ();
  }

  public void register (String symbol, Instruction instruction) {
    Integer opcode = this.availableOpcodes.pollFirst ();
    if (opcode == null)
      throw new IllegalStateException ("registry full");

    Instruction decorator = (machine, state) -> {
      try {
        instruction.process (machine, state);
      } finally {
        state.incrementIP ();
      }
    };

    Opcode i = new Opcode (decorator, opcode, symbol);
    if (this.opcodes.put (opcode, i) != null)
      throw new IllegalStateException ();

    if (this.symbols.put (symbol, i) != null)
      throw new IllegalStateException ();
  }

  private Opcode getInstruction (String symbol) {
    return ofNullable (this.symbols.get (symbol))
      .orElseThrow (NoSuchElementException::new);
  }

  private Instruction newUnknownIntruction (@SuppressWarnings ("unused") int opcode) {
    return (machine, state) -> {
    };
  }
}