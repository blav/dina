package us.actar.dina;

public class AutoIncrementIPDecorator implements Instruction {
  
  private final Instruction nested;

  public AutoIncrementIPDecorator (Instruction nested) {
    this.nested = nested;
  }

  @Override
  public void process (Machine machine, ProgramState state) throws Fault {
    try {
      nested.process (machine, state);
    } finally {
      state.incrementIP ();
    }
  }
}
