package us.actar.dina.is.is1;

import us.actar.dina.InstructionSet;
import us.actar.dina.randomizers.RegisterRandomizer;

import static us.actar.dina.randomizers.RegisterRandomizer.NOP;

public class Add extends Base {

  private final int left;

  private final int right;

  private final int result;

  public Add (RegisterRandomizer.Name randomizer, int left, int right, int result) {
    super (randomizer);
    this.left = left;
    this.right = right;
    this.result = result;
  }

  public void register (InstructionSet registry) {
    registry.register (
      String.format ("add_r%d_to_r%d_into_r%d", left, right, result),
      (machine, state) -> {
        getRegisters (state).set (result, getRegisters (state).get (left, NOP) +
          getRegisters (state).get (right, NOP), machine.getRandomizer (randomizer));
      });
  }
}
