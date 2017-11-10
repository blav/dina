package us.actar.dina.is.is2;

import us.actar.dina.Fault;
import us.actar.dina.Heap;
import us.actar.dina.InstructionSet;
import us.actar.dina.randomizers.RegisterRandomizer;

import static us.actar.dina.is.is2.IS2Randomizers.JUMP;
import static us.actar.dina.randomizers.RegisterRandomizer.NOP;

public class Jump extends Base {

  public Jump () {
    super (JUMP);
  }


  @Override
  public void register (InstructionSet registry) {
    registry.register (
      "jump",
      (machine, state) -> {
        IS2Registers registers = getRegisters (state);
        int ip = state.getInstructionPointer ();
        state.setInstructionPointer (registers.pop (NOP), machine.getRandomizer (randomizer));
        registers.push (ip, NOP);
      });
  }
}
