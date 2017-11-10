package us.actar.dina.is.is2;

import us.actar.dina.InstructionSet;
import us.actar.dina.is.is2.IS2Registers.Register;

import static us.actar.dina.is.is2.IS2Randomizers.DECREMENT;
import static us.actar.dina.is.is2.IS2Randomizers.INCREMENT;
import static us.actar.dina.randomizers.RegisterRandomizer.NOP;

public class IncrementR extends Base {

  private final Register register;

  public IncrementR (Register register) {
    super (INCREMENT);
    this.register = register;
  }

  @Override
  public void register (InstructionSet registry) {
    registry.register (
      String.format ("increment_%s", register.name ()),
      (machine, state) -> {
        IS2Registers registers = getRegisters (state);
        registers.set (register, registers.get (register, NOP) + 1, machine.getRandomizer (randomizer));
      }
    );
  }
}
