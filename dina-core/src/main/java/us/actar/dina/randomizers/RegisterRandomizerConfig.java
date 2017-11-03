package us.actar.dina.randomizers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import com.google.inject.TypeLiteral;
import us.actar.dina.Machine;
import us.actar.dina.randomizers.RegisterRandomizer.Factory;

import java.util.Map;

import static java.util.Optional.ofNullable;
import static us.actar.dina.randomizers.NopConfig.INSTANCE;

@JsonIgnoreProperties (ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.CUSTOM, include = JsonTypeInfo.As.PROPERTY)
@JsonTypeIdResolver (RegisterRandomizerConfigTypeResolver.class)
public class RegisterRandomizerConfig {

  public static final TypeLiteral<RegisterRandomizerConfig> TYPE = new TypeLiteral<RegisterRandomizerConfig> () {};

  public static RegisterRandomizer<? extends RegisterRandomizerConfig> createRandomizer (Machine machine, RegisterRandomizerConfig config) {
    Class<? extends RegisterRandomizerConfig>
      configClass = ofNullable (config).orElse (INSTANCE).getClass ();

    Map<Class<? extends RegisterRandomizerConfig>, Factory<?>>
      registry = RegisterRandomizerRegistry.getRandomizers ();

    Factory<RegisterRandomizerConfig> f = (Factory<RegisterRandomizerConfig>) registry.get (configClass);
    return f.create (machine, config);
  }
}
