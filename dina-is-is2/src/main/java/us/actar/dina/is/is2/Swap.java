package us.actar.dina.is.is2;

import us.actar.dina.Fault;
import us.actar.dina.Instruction;
import us.actar.dina.InstructionSet;
import us.actar.dina.Machine;
import us.actar.dina.Program;

import static us.actar.dina.is.is2.IS2InstructionGroup.SWAP;
import static us.actar.dina.randomizers.RegisterRandomizer.NOP;

public class Swap extends Base {

  public Swap () {
    super (SWAP);
  }


  @Override
  public void register (InstructionSet registry) {
    registry.register (
      new Instruction ("swap", group) {
        @Override
        public void process (Machine machine, Program state) throws Fault {
          IS2Registers registers = Swap.this.getRegisters (state);
          int a = registers.pop (NOP);
          int b = registers.pop (NOP);
          registers.push (a, NOP);
          registers.push (b, NOP);
          machine.getRandomizer (group).randomizeValue (state, 0);
        }
      });
  }
}
