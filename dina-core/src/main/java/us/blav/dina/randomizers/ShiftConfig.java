package us.blav.dina.randomizers;

public class ShiftConfig extends RegisterRandomizerConfig {

  public ShiftConfig () {
    this.probability = 100;
    this.value = 1;
  }

  public int getProbability () {
    return probability;
  }

  public ShiftConfig setProbability (int probability) {
    this.probability = probability;
    return this;
  }

  public int getValue () {
    return value;
  }

  public ShiftConfig setValue (int value) {
    this.value = value;
    return this;
  }

  private int probability;

  private int value;

}
