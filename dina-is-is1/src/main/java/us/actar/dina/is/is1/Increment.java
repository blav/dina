package us.actar.dina.is.is1;

import us.actar.dina.*;

import static us.actar.dina.randomizers.RegisterRandomizer.NOP;

public class Increment extends Base {

  private final int register;

  public Increment (InstructionGroup randomizer, int register) {
    super (randomizer);
    this.register = register;
  }

  @Override
  public void register (InstructionSet registry) {
    registry.register (
      new Instruction (String.format ("increment_r%d", register), group) {
        @Override
        public void process (Machine machine, Program state) throws Fault {
          Increment.this.getRegisters (state).set (Increment.this.register, Increment.this.getRegisters (state).get (Increment.this.register, NOP) + 1, machine.getRandomizer (group));
        }
      }
    );
  }
}
