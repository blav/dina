package us.actar.dina.is.is2;

import us.actar.dina.Fault;
import us.actar.dina.Instruction;
import us.actar.dina.InstructionSet;
import us.actar.dina.Machine;
import us.actar.dina.Program;

import static us.actar.dina.is.is2.IS2InstructionGroup.DROP;
import static us.actar.dina.randomizers.RegisterRandomizer.NOP;

public class Drop extends Base {

  public Drop () {
    super (DROP);
  }

  @Override
  public void register (InstructionSet registry) {
    registry.register (
      new Instruction ("drop", group) {
        @Override
        public void process (Machine machine, Program state) throws Fault {
          Drop.this.getRegisters (state).pop (NOP);
        }
      });
  }
}
