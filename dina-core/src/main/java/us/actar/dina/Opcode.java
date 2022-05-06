package us.actar.dina;

public class Opcode {

  private final Instruction instruction;

  private final int opcode;

  private final String symbol;

  Opcode (Instruction instruction, int opcode, String symbol) {
    this.instruction = instruction;
    this.opcode = opcode;
    this.symbol = symbol;
  }

  @Override
  public String toString () {
    return symbol;
  }

  public InstructionGroup getGroup () {
    return instruction.getGroup ();
  }

  public Instruction getInstruction () {
    return instruction;
  }

  public int getOpcode () {
    return opcode;
  }

  public String getSymbol () {
    return symbol;
  }
}
