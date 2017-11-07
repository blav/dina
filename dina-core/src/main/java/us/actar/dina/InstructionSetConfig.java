package us.actar.dina;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import com.google.inject.TypeLiteral;
import us.actar.dina.randomizers.RegisterRandomizer;
import us.actar.dina.randomizers.RegisterRandomizer.Name;
import us.actar.dina.randomizers.RegisterRandomizerConfig;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;
import static us.actar.dina.randomizers.RegisterRandomizerConfig.createRandomizer;

@JsonTypeInfo (use = JsonTypeInfo.Id.CUSTOM, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonTypeIdResolver (InstructionSetConfigTypeResolver.class)
public abstract class InstructionSetConfig<THISTYPE extends InstructionSetConfig<THISTYPE, NAME>, NAME extends Name> {

  public static final TypeLiteral<InstructionSetConfig<? extends InstructionSetConfig<?, ?>, ? extends Name>> TYPE =
    new TypeLiteral<InstructionSetConfig<? extends InstructionSetConfig<?, ?>, ? extends Name>> () {
    };

  @JsonProperty ("randomizers")
  private final Map<String, RegisterRandomizerConfig> registerRandomizers;

  @JsonIgnore
  private final Class<NAME> registerRandomizerClass;

  @JsonProperty("ip-randomizer")
  public RegisterRandomizerConfig ipRandomizer;

  @JsonProperty ("bootstraps")
  private List<Bootstrap> bootstraps;

  protected InstructionSetConfig (Class<NAME> registerRandomizerClass) {
    this.registerRandomizerClass = registerRandomizerClass;
    this.registerRandomizers = new HashMap<> ();
    this.bootstraps = new ArrayList<> ();
  }

  public THISTYPE addBoostrapCode (String... code) {
    this.bootstraps.add (new Bootstrap ().setCode (code));
    return cast ();
  }

  public List<Bootstrap> getBootstraps () {
    return bootstraps;
  }

  public THISTYPE addRandomizer (NAME name, RegisterRandomizerConfig randomizer) {
    this.registerRandomizers.put (name.name (), randomizer);
    return cast ();
  }

  protected THISTYPE cast () {
    return (THISTYPE) this;
  }

  public Map<? extends Name, ? extends RegisterRandomizer> createRandomizers (Machine machine) {
    Map<NAME, RegisterRandomizer<?>> configs = new HashMap<> ();
    Map<String, ? extends RegisterRandomizerConfig> copy = new HashMap<> (this.registerRandomizers);
    for (NAME n : getNames ())
      configs.put (n, createRandomizer (machine, copy.remove (n.name ())));

    if (copy.size () > 0)
      throw new IllegalArgumentException ("no such randomizers in " +
        registerRandomizerClass.getSimpleName () + ": " +
        copy.keySet ().stream ().collect (joining (", ")));

    return configs;
  }

  public void registerInstructions (InstructionSet registry) {
    getInstructions ().forEach (f -> f.register (registry));
  }

  public RegisterRandomizerConfig getIpRandomizer () {
    return ipRandomizer;
  }

  public THISTYPE setIpRandomizer (RegisterRandomizerConfig ipRandomizer) {
    this.ipRandomizer = ipRandomizer;
    return (THISTYPE) this;
  }

  public abstract Registers createRegisters (Program program);

  @JsonIgnore
  public int getInstructionsCount () {
    return getInstructions ().size ();
  }

  protected abstract Collection<NAME> getNames ();

  @JsonIgnore
  protected abstract Collection<InstructionFactory> getInstructions ();

  public static class Bootstrap {

    @JsonProperty ("code")
    private final List<String> code;

    public Bootstrap () {
      this.code = new ArrayList<> ();
    }

    public List<String> getCode () {
      return code;
    }

    public Bootstrap setCode (String... code) {
      this.code.clear ();
      this.code.addAll (Arrays.asList (code));
      return this;
    }
  }

}
