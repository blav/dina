package us.blav.dina;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Config {

  public Config () {
    this.bootstrapCode = new ArrayList<> ();
    this.randomizer = new Randomizer ().setName (Randomizer.DEFAULT).setSeed (0);
  }

  public static class Randomizer {

    public static final String DEFAULT = "default";

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

  public int getMemory () {
    return memory;
  }

  public Config setMemory (int memory) {
    this.memory = memory;
    return this;
  }

  public String getInstructionSet () {
    return instructionSet;
  }

  public Config setInstructionSet (String instructionSet) {
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

  public Config addBoostrapCode (String... code) {
    this.bootstrapCode.addAll (Arrays.asList (code));
    return this;
  }

  private Randomizer randomizer;

  private String instructionSet;

  private int memory;

  private List<String> bootstrapCode;

}
