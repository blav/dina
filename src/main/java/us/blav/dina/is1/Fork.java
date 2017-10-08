package us.blav.dina.is1;

import us.blav.dina.Fault;

import static java.util.Optional.ofNullable;
import static us.blav.dina.is1.Processor.Flags.auto_increment_ip;

public class Fork implements InstructionFactory {

  @Override
  public void register (Processor processor) {
    processor.register (
      15 << 4 | 15 << 0,
      "fork",
      (machine, state) -> {
        State child = new State (ofNullable (state.getChild ()).orElseThrow (Fault::new));
        child.setInstructionPointer (child.getCell ().getOffset ());
        state.setChild (null);
        for (int i = 0; i < 4; i++)
          child.set (i, state.get (i));

        machine.launch (child);
      }, auto_increment_ip);
  }
}
