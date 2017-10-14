package us.blav.dina.randomizers;

import us.blav.dina.VirtualMachine;

public class ShuffleConfig extends RegisterRandomizerConfig {

  public enum Range {
    BYTE {
      @Override
      public int range (VirtualMachine machine) {
        return machine.getConfig ().getInstructionSet ().getInstructionsCount ();
      }
    },
    ADDRESS {
      @Override
      public int range (VirtualMachine machine) {
        return machine.getHeap ().getTotal ();
      }
    };

    public abstract int range (VirtualMachine machine);

  }

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

  private int probability;

  private Range range;

}
