package us.actar.dina;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Config {

  public static final String DEFAULT = "default";

  @JsonProperty ("randomizer")
  private Randomizer randomizer;

  @JsonProperty ("memory")
  private int memory;

  @JsonProperty ("reclaimer")
  private Reclaimer reclaimer;

  @JsonProperty ("instruction-set")
  private InstructionSetConfig<?, ?> instructionSet;

  public Config () {
    this.randomizer = new Randomizer ().setName (DEFAULT).setSeed (0);
    this.reclaimer = new Reclaimer ().setThresholdLow (.6).setThresholdHigh (.9);
  }

  public int getMemory () {
    return memory;
  }

  public Config setMemory (int memory) {
    this.memory = memory;
    return this;
  }

  public InstructionSetConfig<?, ?> getInstructionSet () {
    return instructionSet;
  }

  public Config setInstructionSet (InstructionSetConfig<?, ?> instructionSet) {
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

  public Reclaimer getReclaimer () {
    return reclaimer;
  }

  public Config setReclaimer (Reclaimer reclaimer) {
    this.reclaimer = reclaimer;
    return this;
  }

  public static class Randomizer {

    private String name;

    private long seed;

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

  }

  public static class Reclaimer {

    private String name;

    private double thresholdHigh;

    private double thresholdLow;

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

  }

}
