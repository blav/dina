package us.actar.dina.is.is1;

import us.actar.dina.*;

import static us.actar.dina.randomizers.RegisterRandomizer.NOP;

public class Decrement extends Base {

  private final int register;

  public Decrement (InstructionGroup randomizer, int register) {
    super (randomizer);
    this.register = register;
  }

  @Override
  public void register (InstructionSet registry) {
    registry.register (
      new Instruction (String.format ("decrement_r%d", register), group) {
        @Override
        public void process (Machine machine, Program state) throws Fault {
          Decrement.this.getRegisters (state).set (register, Decrement.this.getRegisters (state).get (register, NOP) - 1, machine.getRandomizer (group));
        }
      }
    );
  }
}
