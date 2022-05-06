package us.actar.dina.is.is1;

import us.actar.dina.*;

public class Label extends Base {

  private final int label;

  public Label (InstructionGroup randomizer, int label) {
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
      new Instruction (getSymbol (label), group) {
        @Override
        public void process (Machine machine, Program state) throws Fault {
        }
      }
    );
  }
}
