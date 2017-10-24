package us.actar.dina.randomizers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import com.google.inject.TypeLiteral;
import us.actar.dina.Machine;

@JsonIgnoreProperties (ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.CUSTOM, include = JsonTypeInfo.As.PROPERTY)
@JsonTypeIdResolver (RegisterRandomizerConfigTypeResolver.class)
public class RegisterRandomizerConfig {

  public static final TypeLiteral<RegisterRandomizerConfig> TYPE = new TypeLiteral<RegisterRandomizerConfig> () {};

  public static <CONF extends RegisterRandomizerConfig> RegisterRandomizer<CONF> createRandomizer (Machine machine, RegisterRandomizer.Factory<?> factory, CONF conf) {
    RegisterRandomizer.Factory<CONF> f = (RegisterRandomizer.Factory<CONF>) factory;
    return f.create (machine, conf);
  }
}