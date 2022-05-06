package us.actar.dina.is.is2;

import us.actar.dina.InstructionFactory;
import us.actar.dina.Program;

public abstract class Base implements InstructionFactory {

  protected final IS2InstructionGroup group;

  public Base (IS2InstructionGroup group) {
    this.group = group;
  }

  protected IS2Registers getRegisters (Program program) {
    return (IS2Registers) program.getRegisters ();
  }
}
