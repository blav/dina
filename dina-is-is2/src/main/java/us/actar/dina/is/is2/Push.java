package us.actar.dina.is.is2;

import us.actar.dina.InstructionSet;
import us.actar.dina.is.is2.IS2Registers.Register;

import static us.actar.dina.is.is2.IS2Randomizers.PUSH;
import static us.actar.dina.randomizers.RegisterRandomizer.NOP;

public class Push extends Base {

  private final Register register;

  public Push (Register register) {
    super (PUSH);
    this.register = register;
  }

  public void register (InstructionSet registry) {
    registry.register (
      String.format ("push_%s", register),
      (machine, state) -> {
        IS2Registers registers = getRegisters (state);
        registers.push (registers.get (register, NOP), machine.getRandomizer (randomizer));
      });
  }
}
