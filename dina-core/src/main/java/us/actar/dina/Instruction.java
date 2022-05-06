package us.actar.dina;

public abstract class Instruction {

  private final String symbol;

  private final InstructionGroup group;

  public Instruction (String symbol, InstructionGroup group) {
    this.symbol = symbol;
    this.group = group;
  }

  public InstructionGroup getGroup () {
    return group;
  }

  public String getSymbol () {
    return symbol;
  }

  public abstract void process (Machine machine, Program state) throws Fault;

}
