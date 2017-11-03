package us.actar.dina.randomizers;

import us.actar.dina.Fault;
import us.actar.dina.Machine;
import us.actar.dina.ProgramState;

import java.util.Map;

import static java.util.Optional.ofNullable;
import static us.actar.dina.randomizers.NopConfig.INSTANCE;

public interface RegisterRandomizer<CONF extends RegisterRandomizerConfig> {

  interface Factory<CONF extends RegisterRandomizerConfig> {

    RegisterRandomizer<CONF> create (Machine machine, CONF create);

  }

  RegisterRandomizer<?> NOP = new Nop ();

  int randomizeValue (ProgramState state, int value) throws Fault;

  interface Name {

    String name ();

  }

  interface RegistryBuilder {

    <CONF extends RegisterRandomizerConfig> RegistryBuilder registerRandomizer (String typeId, Class<CONF> configClass, RegisterRandomizer.Factory<CONF> factory);

    void done ();

  }

}
