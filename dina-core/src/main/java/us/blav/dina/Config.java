package us.blav.dina;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Config {

  public static final String DEFAULT = "default";

  public Config () {
    this.bootstrapCode = new ArrayList<> ();
    this.executionFilters = new ArrayList<> ();
    this.reclaimerFilters = new ArrayList<> ();
    this.randomizer = new Randomizer ().setName (DEFAULT).setSeed (0);
    this.reclaimer = new Reclaimer ().setThresholdLow (.6).setThresholdHigh (.9);
  }

  public static class Randomizer {

    public String getName () {
      return name;
    }

    public Randomizer setName (String name) {
      this.name = name;
      return this;
    }

    public long getSeed () {
      return seed;
    }

    public Randomizer setSeed (long seed) {
      this.seed = seed;
      return this;
    }

    private String name;

    private long seed;

  }


  public static class Reclaimer {

    public String getName () {
      return name;
    }

    public Reclaimer setName (String name) {
      this.name = name;
      return this;
    }

    public double getThresholdHigh () {
      return thresholdHigh;
    }

    public Reclaimer setThresholdHigh (double thresholdHigh) {
      this.thresholdHigh = thresholdHigh;
      return this;
    }

    public double getThresholdLow () {
      return thresholdLow;
    }

    public Reclaimer setThresholdLow (double thresholdLow) {
      this.thresholdLow = thresholdLow;
      return this;
    }

    private String name;

    private double thresholdHigh;

    private double thresholdLow;

  }


  public int getMemory () {
    return memory;
  }

  public Config setMemory (int memory) {
    this.memory = memory;
    return this;
  }

  public InstructionSetConfig getInstructionSet () {
    return instructionSet;
  }

  public Config setInstructionSet (InstructionSetConfig instructionSet) {
    this.instructionSet = instructionSet;
    return this;
  }

  public Randomizer getRandomizer () {
    return randomizer;
  }

  public Config setRandomizer (Randomizer randomizer) {
    this.randomizer = randomizer;
    return this;
  }

  public List<String> getBootstrapCode () {
    return bootstrapCode;
  }

  public Reclaimer getReclaimer () {
    return reclaimer;
  }

  public Config setReclaimer (Reclaimer reclaimer) {
    this.reclaimer = reclaimer;
    return this;
  }

  public Config addBoostrapCode (String... code) {
    this.bootstrapCode.addAll (Arrays.asList (code));
    return this;
  }

  public List<String> getExecutionFilters () {
    return executionFilters;
  }

  public Config addExecutionFilters (String... filters) {
    this.executionFilters.addAll (Arrays.asList (filters));
    return this;
  }

  public List<String> getReclaimerFilters () {
    return reclaimerFilters;
  }

  public Config addReclaimerFilters (String... filters) {
    this.reclaimerFilters.addAll (Arrays.asList (filters));
    return this;
  }

  @JsonProperty("randomizer")
  private Randomizer randomizer;

  @JsonProperty("memory")
  private int memory;

  @JsonProperty("bootstrap")
  private List<String> bootstrapCode;

  @JsonProperty("reclaimer")
  private Reclaimer reclaimer;

  @JsonProperty("instruction-set")
  private InstructionSetConfig instructionSet;

  @JsonProperty("execution-filters")
  private final List<String> executionFilters;

  @JsonProperty("reclaimer-filters")
  private final List<String> reclaimerFilters;

}
