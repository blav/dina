package us.actar.dina.is.is2;

import us.actar.dina.InstructionSet;
import us.actar.dina.is.is2.IS2Registers.Register;

import static us.actar.dina.is.is2.IS2Randomizers.POP;
import static us.actar.dina.randomizers.RegisterRandomizer.NOP;

public class Pop extends Base {

  private final Register register;

  public Pop (Register register) {
    super (POP);
    this.register = register;
  }

  public void register (InstructionSet registry) {
    registry.register (
      String.format ("pop_%s", register),
      (machine, state) -> {
        IS2Registers registers = getRegisters (state);
        registers.set (register, registers.pop (NOP), machine.getRandomizer (randomizer));
      });
  }
}
