package us.actar.dina.randomizers;

import com.google.inject.AbstractModule;

public class RandomizersModule extends AbstractModule {

  @Override
  protected void configure () {
    RegisterRandomizerRegistry.newBuilder (binder ())
      .registerRandomizer ("nop", NopConfig.class, Nop.FACTORY)
      .registerRandomizer ("shuffle", ShuffleConfig.class, Shuffle.FACTORY)
      .registerRandomizer ("shift", ShiftConfig.class, Shift.FACTORY)
      .registerRandomizer ("bitflip", BitFlipConfig.class, BitFlip.FACTORY)
      .registerRandomizer ("fade", FadeConfig.class, Fade.FACTORY)
      .done ();
  }
}
