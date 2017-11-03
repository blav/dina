package us.actar.dina;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import us.actar.commons.ModuleSupplier;
import us.actar.dina.filters.FiltersModule;
import us.actar.dina.randomizers.RandomizersModule;
import us.actar.dina.reclaimers.ReclaimersModule;

import static com.google.inject.name.Names.named;

public class DinaCore implements ModuleSupplier {

  @Override
  public Module get () {
    return new AbstractModule () {
      @Override
      protected void configure () {
        install (new FiltersModule ());
        install (new RandomizersModule ());
        install (new ReclaimersModule ());
        bind (Heap.FACTORY_TYPE)
          .toInstance (Heap::new);

        bind (Randomizer.FACTORY_TYPE)
          .annotatedWith (named (Config.DEFAULT))
          .toInstance (RandomizerDefault::new);
      }
    };
  }
}
