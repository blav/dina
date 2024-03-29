package us.actar.dina.is.is1;

import us.actar.dina.*;

import static us.actar.dina.randomizers.RegisterRandomizer.NOP;

public class Substract extends Base {

  private final int left;

  private final int right;

  private final int result;

  public Substract (InstructionGroup randomizer, int left, int right, int result) {
    super (randomizer);
    this.left = left;
    this.right = right;
    this.result = result;
  }

  @Override
  public void register (InstructionSet registry) {
    registry.register (
      new Instruction (String.format ("substract_r%d_from_r%d_into_r%d", left, right, result), group) {
        @Override
        public void process (Machine machine, Program state) throws Fault {
          Substract.this.getRegisters (state).set (result, Substract.this.getRegisters (state).get (right, NOP) -
            Substract.this.getRegisters (state).get (left, NOP), machine.getRandomizer (group));
        }
      });
  }
}
