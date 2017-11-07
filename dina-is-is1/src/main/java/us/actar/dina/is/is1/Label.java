package us.actar.dina.is.is1;

import us.actar.dina.InstructionSet;
import us.actar.dina.randomizers.RegisterRandomizer;

public class Label extends Base {

  private final int label;

  public Label (RegisterRandomizer.Name randomizer, int label) {
    super (randomizer);
    this.label = label;
  }

  public static String getSymbol (int label) {
    return "label" + label;
  }

  public static int getOpcode (InstructionSet registry, int label) {
    return registry.getOpcode (getSymbol (label));
  }

  @Override
  public void register (InstructionSet registry) {
    registry.register (
      getSymbol (label),
      (machine, state) -> {
      }
    );
  }
}
