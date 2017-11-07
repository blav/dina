package us.actar.dina.is.is2;

import us.actar.dina.InstructionSet;
import us.actar.dina.is.is2.IS2Registers.Register;
import us.actar.dina.randomizers.RegisterRandomizer;

import static us.actar.dina.is.is2.IS2Randomizers.SET;
import static us.actar.dina.randomizers.RegisterRandomizer.NOP;

public class Set extends Base {

  private final Register register;

  public Set (Register register) {
    super (SET);
    this.register = register;
  }

  public void register (InstructionSet registry) {
    registry.register (
      String.format ("set_%s", register),
      (machine, state) -> {
        IS2Registers registers = getRegisters (state);
        registers.set (register, registers.pop (NOP), machine.getRandomizer (randomizer));
      });
  }
}
