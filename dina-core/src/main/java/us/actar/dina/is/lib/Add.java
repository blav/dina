package us.actar.dina.is.lib;

import us.actar.dina.InstructionRegistry;
import us.actar.dina.randomizers.RegisterRandomizer;

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

  public void register (InstructionRegistry registry) {
    registry.register (
      String.format ("add_r%d_to_r%d_into_r%d", left, right, result),
      (machine, state) -> {
        state.set (result, state.get (left, RegisterRandomizer.NOP) +
          state.get (right, RegisterRandomizer.NOP), machine.getRandomizer (randomizer));
      });
  }
}
