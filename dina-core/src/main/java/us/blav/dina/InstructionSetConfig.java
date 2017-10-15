package us.blav.dina;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import com.google.inject.TypeLiteral;
import us.blav.dina.randomizers.RegisterRandomizer;
import us.blav.dina.randomizers.RegisterRandomizer.Name;
import us.blav.dina.randomizers.RegisterRandomizerConfig;
import us.blav.dina.randomizers.RegisterRandomizerRegistry;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.joining;
import static us.blav.dina.randomizers.NopConfig.INSTANCE;
import static us.blav.dina.randomizers.RegisterRandomizerConfig.createRandomizer;

@JsonTypeInfo(use = JsonTypeInfo.Id.CUSTOM, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonTypeIdResolver (InstructionSetConfigTypeResolver.class)
public abstract class InstructionSetConfig<NAME extends Name> {

  public static final TypeLiteral<InstructionSetConfig<? extends Name>> TYPE =
    new TypeLiteral<InstructionSetConfig<? extends Name>> () {};

  @JsonProperty("randomizers")
  private final Map<String, RegisterRandomizerConfig> registerRandomizers;

  @JsonIgnore
  private final Class<NAME> registerRandomizerClass;

  @JsonIgnore
  private final int registers;

  protected InstructionSetConfig (Class<NAME> registerRandomizerClass, int registers) {
    this.registerRandomizerClass = registerRandomizerClass;
    this.registers = registers;
    this.registerRandomizers = new HashMap<> ();
  }

  public InstructionSetConfig addRandomizer (NAME name, RegisterRandomizerConfig randomizer) {
    this.registerRandomizers.put (name.name (), randomizer);
    return this;
  }

  public Map<? extends Name, ? extends RegisterRandomizer> createRandomizers (VirtualMachine machine) {
    Map<NAME, RegisterRandomizer<?>> configs = new HashMap<> ();
    Map<String, ? extends RegisterRandomizerConfig> copy = new HashMap<> (this.registerRandomizers);
    Map<Class<? extends RegisterRandomizerConfig>, RegisterRandomizer.Factory<?>>
      registry = RegisterRandomizerRegistry.createRandomizers ();

    for (NAME n : getNames ()) {
      RegisterRandomizerConfig config = copy.remove (n.name ());
      Class<? extends RegisterRandomizerConfig> configClass = ofNullable (config).orElse (INSTANCE).getClass ();
      configs.put (n, createRandomizer (machine, registry.get (configClass), config));
    }

    if (copy.size () > 0)
      throw new IllegalArgumentException ("no such randomizers in " +
        registerRandomizerClass.getSimpleName () + ": " +
        copy.keySet ().stream ().collect (joining (", ")));

    return configs;
  }

  public void registerInstructions (InstructionRegistry registry) {
    stream (getInstructions ()).forEach (f -> f.register (registry));
  }

  @JsonIgnore
  public int getRegisters () {
    return registers;
  }

  @JsonIgnore
  public int getInstructionsCount () {
    return getInstructions ().length;
  }

  protected abstract Collection<NAME> getNames ();

  @JsonIgnore
  protected abstract InstructionFactory [] getInstructions ();

}
