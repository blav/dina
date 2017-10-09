package us.blav.dina.is1;

import us.blav.dina.Fault;
import us.blav.dina.InstructionFactory;
import us.blav.dina.InstructionRegistry;
import us.blav.dina.ProgramState;

import static java.util.Optional.ofNullable;
import static us.blav.dina.InstructionProcessor.Decorator.auto_increment_ip;

public class Fork implements InstructionFactory {

  @Override
  public void register (InstructionRegistry registry) {
    registry.register (
      15 << 4 | 15 << 0,
      "fork",
      (machine, state) -> {
        ProgramState child = machine.getProcessor ().newProgram (ofNullable (state.getChild ()).orElseThrow (Fault::new));
        child.setInstructionPointer (child.getCell ().getOffset ());
        state.setChild (null);
        for (int i = 0; i < 4; i++)
          child.set (i, state.get (i));

        machine.launch (child);
      }, auto_increment_ip);
  }
}
