package us.blav.dina.is.lib;

import us.blav.dina.InstructionFactory;
import us.blav.dina.InstructionRegistry;
import us.blav.dina.RegisterRandomizer;

public class Label extends Base {

  private final int label;

  public Label (RegisterRandomizer.Name randomizer, int label) {
    super (randomizer);
    this.label = label;
  }

  @Override
  public void register (InstructionRegistry registry) {
    registry.register (
      getSymbol (label),
      (machine, state) -> {}
    );
  }

  public static String getSymbol (int label) {
    return "label" + label;
  }

  public static int getOpcode (InstructionRegistry registry, int label) {
    return registry.getOpcode (getSymbol (label));
  }
}
