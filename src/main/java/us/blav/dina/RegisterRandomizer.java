package us.blav.dina;

import us.blav.dina.randomizers.Nop;

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
