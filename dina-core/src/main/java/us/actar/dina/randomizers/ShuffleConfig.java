package us.actar.dina.randomizers;

import us.actar.dina.Machine;

public class ShuffleConfig extends RegisterRandomizerConfig {

  private int probability;

  private Range range;

  public ShuffleConfig () {
    this.range = Range.ADDRESS;
    this.probability = 100;
  }

  public Range getRange () {
    return range;
  }

  public ShuffleConfig setRange (Range range) {
    this.range = range;
    return this;
  }

  public int getProbability () {
    return probability;
  }

  public ShuffleConfig setProbability (int probability) {
    this.probability = probability;
    return this;
  }

  public enum Range {
    BYTE {
      @Override
      public int range (Machine machine) {
        return machine.getConfig ().getInstructionSet ().getInstructionsCount ();
      }
    },
    ADDRESS {
      @Override
      public int range (Machine machine) {
        return machine.getHeap ().getTotal ();
      }
    };

    public abstract int range (Machine machine);

  }

}
