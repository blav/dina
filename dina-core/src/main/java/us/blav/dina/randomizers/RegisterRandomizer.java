package us.blav.dina.randomizers;

import us.blav.dina.ProgramState;
import us.blav.dina.VirtualMachine;

public interface RegisterRandomizer<CONF extends RegisterRandomizerConfig> {

  interface Factory<CONF extends RegisterRandomizerConfig> {

    RegisterRandomizer<CONF> create (VirtualMachine machine, CONF create);

  }

  RegisterRandomizer<?> NOP = new Nop ();

  int randomizeValue (ProgramState state, int value);

  interface Name {

    String name ();

  }

}
