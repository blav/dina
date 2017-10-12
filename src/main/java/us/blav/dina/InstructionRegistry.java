package us.blav.dina;

import java.util.*;

import static java.util.stream.Collectors.toSet;
import static java.util.stream.IntStream.range;

public class InstructionRegistry {

  private final TreeSet<Integer> availableOpcodes;

  private final Map<Integer, Opcode> opcodes;

  private final Map<String, Opcode> symbols;

  public InstructionRegistry () {
    this.opcodes = new HashMap<> ();
    this.symbols = new HashMap<> ();
    this.availableOpcodes = new TreeSet<> (range (0, 256).boxed ().collect (toSet ()));
  }

  public Opcode getInstruction (VirtualMachine machine, int opcode) {
    return Optional.ofNullable (opcodes.get (opcode))
      .orElseGet (() -> new Opcode (newUnknownIntruction (opcode), opcode, "unknown" + opcode));
  }

  public Opcode getInstruction (String symbol) {
    return Optional.ofNullable (this.symbols.get (symbol))
      .orElseThrow (NoSuchElementException::new);
  }

  public int getOpcode (String symbol) {
    return getInstruction (symbol).getOpcode ();
  }

  public void register (String symbol, Instruction instruction) {
    Integer opcode = this.availableOpcodes.pollFirst ();
    if (opcode == null)
      throw new IllegalStateException ("registry full");

    instruction = new AutoIncrementIPDecorator (instruction);
    Opcode i = new Opcode (instruction, opcode, symbol);
    if (this.opcodes.put (opcode, i) != null)
      throw new IllegalStateException ();

    if (this.symbols.put (symbol, i) != null)
      throw new IllegalStateException ();
  }

  private Instruction newUnknownIntruction (int opcode) {
    return (machine, state) -> {
    };
  }
}