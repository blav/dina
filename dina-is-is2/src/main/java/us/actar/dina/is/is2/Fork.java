package us.actar.dina.is.is2;

import us.actar.dina.*;
import us.actar.dina.Fault;

import static java.util.Optional.ofNullable;
import static us.actar.dina.is.is2.IS2InstructionGroup.FORK;
import static us.actar.dina.randomizers.RegisterRandomizer.NOP;

public class Fork extends Base {

  public Fork () {
    super (FORK);
  }

  @Override
  public void register (InstructionSet registry) {
    registry.register (
      new Instruction ("fork", group) {
        @Override
        public void process (Machine machine, Program state) throws Fault {
          Program child = new Program (
            state, ofNullable (state.getChild ()).orElseThrow (Fault::new),
            machine.getConfig ().getInstructionSet ());

          child.setInstructionPointer (child.getCell ().getOffset (), NOP);
          state.setChild (null);
          state.incrementForks ();
          //for (int i = 0; i < 4; i++)
          //  child.set (i, state.get (i, NOP), machine.getRandomizer (randomizer));

          machine.launch (child);
        }
      });
  }
}
