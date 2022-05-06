package us.actar.dina.is.is1;

import us.actar.dina.InstructionFactory;
import us.actar.dina.InstructionGroup;
import us.actar.dina.Program;

public abstract class Base implements InstructionFactory {

  protected final InstructionGroup group;

  public Base (InstructionGroup group) {
    this.group = group;
  }

  protected IS1Registers getRegisters (Program program) {
    return (IS1Registers) program.getRegisters ();
  }
}
