package us.blav.dina;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import us.blav.commons.ModuleSupplier;
import us.blav.dina.filters.FiltersModule;
import us.blav.dina.randomizers.RandomizersModule;

import static com.google.inject.name.Names.named;
import static us.blav.dina.Config.DEFAULT;

public class DinaCore implements ModuleSupplier {

  @Override
  public Module get () {
    return new AbstractModule () {
      @Override
      protected void configure () {
        install (new FiltersModule ());
        install (new RandomizersModule ());
        bind (MemoryHeap.FACTORY_TYPE)
          .toInstance (MemoryHeap::new);

        bind (Randomizer.FACTORY_TYPE)
          .annotatedWith (named (DEFAULT))
          .toInstance (RandomizerDefault::new);

        bind (HeapReclaimer.FACTORY_TYPE)
          .annotatedWith (named (DEFAULT))
          .toInstance (HeapReclaimerImpl::new);
      }
    };
  }
}
