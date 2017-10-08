package us.blav.dina.is1;

import us.blav.dina.*;

import javax.inject.Inject;
import java.util.*;

import static java.util.Optional.ofNullable;

public class Processor implements InstructionProcessor {

  private static final RegisteredInstruction INSTANCE =
    new RegisteredInstruction (new RaiseFault (), 0, "fault");

  private final Map<Integer, RegisteredInstruction> opcodes;

  private final Map<String, RegisteredInstruction> symbols;

  private static class RegisteredInstruction {

    private final Instruction instruction;

    private final int opcode;

    private final String symbol;

    private RegisteredInstruction (Instruction instruction, int opcode, String symbol) {
      this.instruction = instruction;
      this.opcode = opcode;
      this.symbol = symbol;
    }
  }

  @Inject
  public Processor (Set<InstructionFactory> factories) {
    this.opcodes = new HashMap<> ();
    this.symbols = new HashMap<> ();
    factories.stream ().forEach (f -> f.register (this));
  }

  @Override
  public void process (VirtualMachine machine, ProgramState state) {
    int ip = state.getInstructionPointer ();
    try {
      int opcode = machine.getHeap ().get (ip);
      ofNullable (opcodes.get (opcode)).orElse (INSTANCE).instruction.process (machine, (State) state);
    } catch (Fault fault) {
      handleFault (fault, state);
    }
  }

  public enum Flags {

    auto_increment_ip {
      @Override
      public Instruction decorate (Instruction instruction) {
        return new AutoIncrementIPDecorator (instruction);
      }
    },;

    public abstract Instruction decorate (Instruction instruction);
  }

  public void register (int opcode, String symbol, Instruction instruction, Flags... flags) {
    for (Flags flag : flags)
      instruction = flag.decorate (instruction);

    RegisteredInstruction i = new RegisteredInstruction (instruction, opcode, symbol);
    if (this.opcodes.put (opcode, i) != null)
      throw new IllegalStateException ();

    if (this.symbols.put (symbol, i) != null)
      throw new IllegalStateException ();
  }

  @Override
  public void launch (VirtualMachine machine, MemoryHeap.Cell cell, List<String> code) {
    try {
      if (cell.getSize () != code.size ())
        throw new IllegalArgumentException ();

      MemoryHeap heap = machine.getHeap ();
      for (int i = 0; i < code.size (); i ++) {
        int opcode = getOpcode (code.get (i));
        heap.set (cell.getOffset () + i, opcode);
      }

      State state = new State (cell);
      state.setInstructionPointer (cell.getOffset ());
      machine.launch (state);
    } catch (Fault fault) {
      throw new RuntimeException ("", fault);
    }
  }

  public int getOpcode (String symbol) {
    return ofNullable (this.symbols.get (symbol))
      .orElseThrow (NoSuchElementException::new)
      .opcode;
  }

  private void handleFault (Fault fault, ProgramState state) {
    System.out.println ("fault");
  }

  private static final Instruction[] INSTRUCTIONS = new Instruction[]{
    // 00xx yyzz computes xx - yy and puts result into zz
    // 01xx yyzz computes xx + yy and puts result into zz
    // 1000 xxyy find_fw_labelxx_yy finds label_xx forward and put result into yy or fault if not found
    // 1001 xxyy find_bw_labelxx_yy finds label_xx backward and put result into yy or fault if not found
    // 1010 xxyy read_@xx_to_yy reads value at address xx into yy or fault if out of address space
    // 1011 xxyy write_yy_@xx reads value contained in yy at address xx or fault if out of address space or boundary
    // 1100 xxyy alloc_xx_yy allocates a new cell of size xx and puts address into yy
    // 1101 00xx if_null_xx
    // 1101 01xx if_not_null_xx
    // 1101 10xx goto_forward_xx
    // 1101 11xx goto_backward_xx
    // 1110 00xx labelxx
    // 1110 01xx increment_rxx
    // 1110 10xx decrement_rxx
    // 1111 1111 fork
  };
}
