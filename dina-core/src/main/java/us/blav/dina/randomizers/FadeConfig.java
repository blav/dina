package us.blav.dina.randomizers;

public class FadeConfig extends RegisterRandomizerConfig {

  public FadeConfig () {
    this.from = 20;
    this.to = 200;
    this.probability = 100;
  }

  public int getFrom () {
    return from;
  }

  public FadeConfig setFrom (int from) {
    this.from = from;
    return this;
  }

  public int getTo () {
    return to;
  }

  public FadeConfig setTo (int to) {
    this.to = to;
    return this;
  }

  public int getProbability () {
    return probability;
  }

  public FadeConfig setProbability (int probability) {
    this.probability = probability;
    return this;
  }

  private int probability;

  private int from;

  private int to;

}
