package us.actar.dina.randomizers;

import us.actar.dina.Fault;
import us.actar.dina.Machine;
import us.actar.dina.Program;

public interface RegisterRandomizer<CONF extends RegisterRandomizerConfig> {

  RegisterRandomizer<?> NOP = new Nop ();

  int randomizeValue (Program state, int value) throws Fault;

  interface Factory<CONF extends RegisterRandomizerConfig> {

    RegisterRandomizer<CONF> create (Machine machine, CONF create);

  }

  interface RegistryBuilder {

    <CONF extends RegisterRandomizerConfig> RegistryBuilder registerRandomizer (String typeId, Class<CONF> configClass, RegisterRandomizer.Factory<CONF> factory);

    void done ();

  }

}
