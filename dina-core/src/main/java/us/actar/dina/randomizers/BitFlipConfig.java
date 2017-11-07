package us.actar.dina.randomizers;

public class BitFlipConfig extends RegisterRandomizerConfig {

  private int probability;

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

}
