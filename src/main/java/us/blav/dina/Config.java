package us.blav.dina;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Config {

  public Config () {
    this.bootstrapCode = new ArrayList<> ();
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

  public String getRandomizer () {
    return randomizer;
  }

  public Config setRandomizer (String randomizer) {
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

  private String randomizer;

  private String instructionSet;

  private int memory;

  private List<String> bootstrapCode;

}
