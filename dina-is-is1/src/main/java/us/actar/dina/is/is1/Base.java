package us.actar.dina.is.is1;

import us.actar.dina.InstructionFactory;
import us.actar.dina.Program;
import us.actar.dina.randomizers.RegisterRandomizer;

public abstract class Base implements InstructionFactory {

  protected final RegisterRandomizer.Name randomizer;

  public Base (RegisterRandomizer.Name randomizer) {
    this.randomizer = randomizer;
  }

  protected IS1Registers getRegisters (Program program) {
    return (IS1Registers) program.getRegisters ();
  }
}
