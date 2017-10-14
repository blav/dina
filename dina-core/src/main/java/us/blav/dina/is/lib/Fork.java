package us.blav.dina.is.lib;

import us.blav.dina.*;
import us.blav.dina.randomizers.RegisterRandomizer;

import static java.util.Optional.ofNullable;
import static us.blav.dina.randomizers.RegisterRandomizer.NOP;

public class Fork extends Base {

  public Fork (RegisterRandomizer.Name randomizer) {
    super (randomizer);
  }

  @Override
  public void register (InstructionRegistry registry) {
    registry.register (
      "fork",
      (machine, state) -> {
        ProgramState child = new ProgramState (
          ofNullable (state.getChild ()).orElseThrow (Fault::new),
          machine.getConfig ().getInstructionSet ().getRegisters ());

        child.setInstructionPointer (child.getCell ().getOffset (), NOP);
        state.setChild (null);
        for (int i = 0; i < 4; i++)
          child.set (i, state.get (i, NOP), machine.getRandomizer (randomizer));

        machine.launch (child);
      });
  }
}
