package us.actar.dina.is.is2;

import us.actar.dina.Fault;
import us.actar.dina.Instruction;
import us.actar.dina.InstructionSet;
import us.actar.dina.Machine;
import us.actar.dina.Program;

import static us.actar.dina.is.is2.IS2InstructionGroup.DUP;
import static us.actar.dina.randomizers.RegisterRandomizer.NOP;

public class Dup extends Base {

  public Dup () {
    super (DUP);
  }


  @Override
  public void register (InstructionSet registry) {
    registry.register (
      new Instruction ("dup", group) {
        @Override
        public void process (Machine machine, Program state) throws Fault {
          IS2Registers registers = Dup.this.getRegisters (state);
          registers.push (registers.peek (NOP), machine.getRandomizer (group));
        }
      });
  }
}
