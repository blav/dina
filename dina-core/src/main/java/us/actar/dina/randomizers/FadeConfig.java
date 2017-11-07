package us.actar.dina.randomizers;

public class FadeConfig extends RegisterRandomizerConfig {

  private int probability;

  private int distance;

  public FadeConfig () {
    this.distance = 200;
    this.probability = 100;
  }

  public int getDistance () {
    return distance;
  }

  public FadeConfig setDistance (int distance) {
    this.distance = distance;
    return this;
  }

  public int getProbability () {
    return probability;
  }

  public FadeConfig setProbability (int probability) {
    this.probability = probability;
    return this;
  }

}
