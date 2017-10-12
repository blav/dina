package us.blav.dina.randomizers;

import us.blav.dina.RegisterRandomizerConfig;
import us.blav.dina.VirtualMachine;

public class BitFlipConfig extends RegisterRandomizerConfig {

  public BitFlipConfig () {
    this.probability = 100;
  }

  public int getProbability () {
    return probability;
  }

  public BitFlipConfig setProbability (int probability) {
    this.probability = probability;
    return this;
  }

  private int probability;

}
