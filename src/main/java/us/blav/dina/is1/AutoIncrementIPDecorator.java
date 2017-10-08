package us.blav.dina.is1;

import us.blav.dina.Fault;
import us.blav.dina.VirtualMachine;

public class AutoIncrementIPDecorator implements Instruction {
  
  private final Instruction nested;

  public AutoIncrementIPDecorator (Instruction nested) {
    this.nested = nested;
  }

  @Override
  public void process (VirtualMachine machine, State state) throws Fault {
    try {
      nested.process (machine, state);
    } finally {
      state.incrementIP ();
    }
  }
}
