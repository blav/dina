package us.actar.dina.is.is2;

import us.actar.dina.Fault;
import us.actar.dina.Instruction;
import us.actar.dina.InstructionSet;
import us.actar.dina.Machine;
import us.actar.dina.Program;

import static us.actar.dina.is.is2.IS2InstructionGroup.JUMP;
import static us.actar.dina.randomizers.RegisterRandomizer.NOP;

public class Jump extends Base {

  public Jump () {
    super (JUMP);
  }


  @Override
  public void register (InstructionSet registry) {
    registry.register (
      new Instruction ("jump", group) {
        @Override
        public void process (Machine machine, Program state) throws Fault {
          IS2Registers registers = Jump.this.getRegisters (state);
          int ip = state.getInstructionPointer ();
          state.setInstructionPointer (registers.pop (NOP), machine.getRandomizer (group));
          registers.push (ip, NOP);
        }
      });
  }
}
