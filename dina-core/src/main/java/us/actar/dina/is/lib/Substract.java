package us.actar.dina.is.lib;

import us.actar.dina.InstructionRegistry;
import us.actar.dina.randomizers.RegisterRandomizer;

import static us.actar.dina.randomizers.RegisterRandomizer.NOP;

public class Substract extends Base {

  private final int left;

  private final int right;

  private final int result;

  public Substract (RegisterRandomizer.Name randomizer, int left, int right, int result) {
    super (randomizer);
    this.left = left;
    this.right = right;
    this.result = result;
  }

  @Override
  public void register (InstructionRegistry registry) {
    registry.register (
      String.format ("substract_r%d_from_r%d_into_r%d", left, right, result),
      (machine, state) -> {
        state.set (result,
          state.get (right, NOP) -
          state.get (left, NOP), machine.getRandomizer (randomizer));
      });
  }
}
