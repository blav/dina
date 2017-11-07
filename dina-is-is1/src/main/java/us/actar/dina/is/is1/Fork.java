package us.actar.dina.is.is1;

import us.actar.dina.Fault;
import us.actar.dina.InstructionSet;
import us.actar.dina.Program;
import us.actar.dina.randomizers.RegisterRandomizer;

import static java.util.Optional.ofNullable;
import static us.actar.dina.randomizers.RegisterRandomizer.NOP;

public class Fork extends Base {

  public Fork (RegisterRandomizer.Name randomizer) {
    super (randomizer);
  }

  @Override
  public void register (InstructionSet registry) {
    registry.register (
      "fork",
      (machine, state) -> {
        Program child = new Program (
          ofNullable (state.getChild ()).orElseThrow (Fault::new),
          machine.getConfig ().getInstructionSet ());

        child.setInstructionPointer (child.getCell ().getOffset (), NOP);
        state.setChild (null);
        state.incrementForks ();
        //for (int i = 0; i < 4; i++)
        //  child.set (i, state.get (i, NOP), machine.getRandomizer (randomizer));

        machine.launch (child);
      });
  }
}
