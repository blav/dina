package us.actar.dina.is.is2;

import us.actar.dina.InstructionFactory;
import us.actar.dina.Program;
import us.actar.dina.randomizers.RegisterRandomizer;

public abstract class Base implements InstructionFactory {

  protected final IS2Randomizers randomizer;

  public Base (IS2Randomizers randomizer) {
    this.randomizer = randomizer;
  }

  protected IS2Registers getRegisters (Program program) {
    return (IS2Registers) program.getRegisters ();
  }
}
