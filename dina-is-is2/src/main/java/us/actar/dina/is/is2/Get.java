package us.actar.dina.is.is2;

import us.actar.dina.InstructionSet;
import us.actar.dina.is.is2.IS2Registers.Register;
import us.actar.dina.randomizers.RegisterRandomizer;

import static us.actar.dina.is.is2.IS2Randomizers.GET;
import static us.actar.dina.randomizers.RegisterRandomizer.NOP;

public class Get extends Base {

  private final Register register;

  public Get (Register register) {
    super (GET);
    this.register = register;
  }

  public void register (InstructionSet registry) {
    registry.register (
      String.format ("get_%s", register),
      (machine, state) -> {
        IS2Registers registers = getRegisters (state);
        registers.push (registers.get (register, NOP), machine.getRandomizer (randomizer));
      });
  }
}
