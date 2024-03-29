package us.actar.dina.is.is2;

import us.actar.dina.Fault;
import us.actar.dina.Instruction;
import us.actar.dina.InstructionSet;
import us.actar.dina.Machine;
import us.actar.dina.Program;

import static us.actar.dina.is.is2.IS2InstructionGroup.LABEL;

public class Label extends Base {

  private final int label;

  public Label (int label) {
    super (LABEL);
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
