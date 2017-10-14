package us.blav.dina;

public class Opcode {

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

  Opcode (Instruction instruction, int opcode, String symbol) {
    this.instruction = instruction;
    this.opcode = opcode;
    this.symbol = symbol;
  }
}
