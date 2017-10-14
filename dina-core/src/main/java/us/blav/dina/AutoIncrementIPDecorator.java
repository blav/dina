package us.blav.dina;

public class AutoIncrementIPDecorator implements Instruction {
  
  private final Instruction nested;

  public AutoIncrementIPDecorator (Instruction nested) {
    this.nested = nested;
  }

  @Override
  public void process (VirtualMachine machine, ProgramState state) throws Fault {
    try {
      nested.process (machine, state);
    } finally {
      state.incrementIP ();
    }
  }
}
