package us.actar.dina;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import com.google.inject.TypeLiteral;
import us.actar.dina.randomizers.RegisterRandomizer;
import us.actar.dina.randomizers.RegisterRandomizerConfig;

import static java.util.Optional.ofNullable;
import static us.actar.dina.randomizers.RegisterRandomizerConfig.createRandomizer;

@JsonTypeInfo (use = JsonTypeInfo.Id.CUSTOM, property = "type")
@JsonTypeIdResolver (InstructionSetConfigTypeResolver.class)
public abstract class InstructionSetConfig<THISTYPE extends InstructionSetConfig<THISTYPE, GROUP>, GROUP extends InstructionGroup> {

  public static final TypeLiteral<InstructionSetConfig<? extends InstructionSetConfig<?, ?>, ? extends InstructionGroup>> TYPE =
    new TypeLiteral<InstructionSetConfig<? extends InstructionSetConfig<?, ?>, ? extends InstructionGroup>> () {
    };

  @JsonProperty ("randomizers")
  private final Map<String, RegisterRandomizerConfig> registerRandomizers;

  @JsonProperty ("costs")
  private final Map<String, Integer> costs;

  @JsonProperty("cost-unknown")
  private int costUnknown;

  @JsonProperty("cost-default")
  private int costDefault;

  @JsonProperty("gain-per-cycle")
  private int gainPerCycle;

  @JsonProperty("initial-energy")
  private int initialEnergy;

  @JsonIgnore
  private final Class<GROUP> registerRandomizerClass;

  @JsonProperty("ip-randomizer")
  public RegisterRandomizerConfig ipRandomizer;

  @JsonProperty ("bootstraps")
  private List<Bootstrap> bootstraps;

  protected InstructionSetConfig (Class<GROUP> registerRandomizerClass) {
    this.registerRandomizerClass = registerRandomizerClass;
    this.registerRandomizers = new HashMap<> ();
    this.costs = new HashMap<> ();
    this.bootstraps = new ArrayList<> ();
  }

  @SuppressWarnings ("unused")
  public THISTYPE addBoostrapCode (String... code) {
    this.bootstraps.add (new Bootstrap ().setCode (code));
    return cast ();
  }

  public List<Bootstrap> getBootstraps () {
    return bootstraps;
  }

  @SuppressWarnings ("unused")
  public THISTYPE addRandomizer (GROUP name, RegisterRandomizerConfig randomizer) {
    this.registerRandomizers.put (name.name (), randomizer);
    return cast ();
  }

  @SuppressWarnings ("unchecked")
  protected THISTYPE cast () {
    return (THISTYPE) this;
  }

  public Map<? extends InstructionGroup, ? extends RegisterRandomizer<?>> createRandomizers (Machine machine) {
    Map<GROUP, RegisterRandomizer<?>> configs = new HashMap<> ();
    Map<String, ? extends RegisterRandomizerConfig> copy = new HashMap<> (this.registerRandomizers);
    for (GROUP n : getGroups ())
      configs.put (n, createRandomizer (machine, copy.remove (n.name ())));

    if (copy.size () > 0)
      throw new IllegalArgumentException ("no such randomizers in " +
        registerRandomizerClass.getSimpleName () + ": " +
        String.join (", ", copy.keySet ()));

    return configs;
  }

  public void registerInstructions (InstructionSet registry) {
    getInstructions ().forEach (f -> f.register (registry));
  }

  public RegisterRandomizerConfig getIpRandomizer () {
    return ipRandomizer;
  }

  @SuppressWarnings ({"unused", "unchecked"})
  public THISTYPE setIpRandomizer (RegisterRandomizerConfig ipRandomizer) {
    this.ipRandomizer = ipRandomizer;
    return (THISTYPE) this;
  }

  public abstract Registers createRegisters (Program program);

  @JsonIgnore
  public int getInstructionsCount () {
    return getInstructions ().size ();
  }

  @SuppressWarnings ("unused")
  public int getCostUnknown () {
    return costUnknown;
  }

  public int getGainPerCycle () {
    return gainPerCycle;
  }

  public int getInitialEnergy () {
    return initialEnergy;
  }

  public int getCost (String group) {
    return ofNullable (costs.get (group)).orElse (costDefault);
  }

  protected abstract Collection<GROUP> getGroups ();

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
