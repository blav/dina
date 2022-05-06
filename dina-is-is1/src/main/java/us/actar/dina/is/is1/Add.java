package us.actar.dina.is.is1;

import us.actar.dina.*;

import static us.actar.dina.randomizers.RegisterRandomizer.NOP;

public class Add extends Base {

  private final int left;

  private final int right;

  private final int result;

  public Add (InstructionGroup randomizer, int left, int right, int result) {
    super (randomizer);
    this.left = left;
    this.right = right;
    this.result = result;
  }

  public void register (InstructionSet registry) {
    registry.register (
      new Instruction (String.format ("add_r%d_to_r%d_into_r%d", left, right, result), group) {
        @Override
        public void process (Machine machine, Program state) throws Fault {
          Add.this.getRegisters (state).set (result, Add.this.getRegisters (state).get (left, NOP) +
            Add.this.getRegisters (state).get (right, NOP), machine.getRandomizer (group));
        }
      });
  }
}
