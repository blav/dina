package us.actar.dina.reclaimers;

import com.google.inject.AbstractModule;
import us.actar.dina.Config;
import us.actar.dina.HeapReclaimer;

import static com.google.inject.name.Names.named;

public class ReclaimersModule extends AbstractModule {

  @Override
  protected void configure () {
    bind (HeapReclaimer.FACTORY_TYPE)
      .annotatedWith (named (Config.DEFAULT))
      .toInstance (CumulativePenalty::new);

    bind (HeapReclaimer.FACTORY_TYPE)
      .annotatedWith (named ("cumulative"))
      .toInstance (CumulativePenalty::new);

    bind (HeapReclaimer.FACTORY_TYPE)
      .annotatedWith (named ("immediate"))
      .toInstance (ImmediatePenalty::new);

    bind (HeapReclaimer.FACTORY_TYPE)
      .annotatedWith (named ("fault-aging"))
      .toInstance (FaultAgingPenalty::new);
  }
}
