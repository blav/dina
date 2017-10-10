package us.blav.dina.randomizers;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.MapBinder;
import us.blav.dina.Injection;
import us.blav.dina.RegisterRandomizer;
import us.blav.dina.RegisterRandomizerConfig;

import java.util.Map;

import static com.google.inject.multibindings.MapBinder.newMapBinder;

public class RandomizersModule extends AbstractModule {

  private static final TypeLiteral<Class<? extends RegisterRandomizerConfig>> KEY =
    new TypeLiteral<Class<? extends RegisterRandomizerConfig>> () {};

  private static final TypeLiteral<RegisterRandomizer.Factory<? extends RegisterRandomizerConfig>> VALUE =
    new TypeLiteral<RegisterRandomizer.Factory<? extends RegisterRandomizerConfig>> () {};

  private static final TypeLiteral<Map<Class<? extends RegisterRandomizerConfig>, RegisterRandomizer.Factory<? extends RegisterRandomizerConfig>>> MAP =
    new TypeLiteral<Map<Class<? extends RegisterRandomizerConfig>, RegisterRandomizer.Factory<? extends RegisterRandomizerConfig>>> () {};

  public static Map<Class<? extends RegisterRandomizerConfig>, RegisterRandomizer.Factory<? extends RegisterRandomizerConfig>> createRandomizers () {
    return Injection.getInstance (MAP);
  }

  @Override
  protected void configure () {
    MapBinder<Class<? extends RegisterRandomizerConfig>, RegisterRandomizer.Factory<? extends RegisterRandomizerConfig>> map =
      newMapBinder (binder (), KEY, VALUE);

    map.addBinding (NopConfig.class).toInstance (Nop.FACTORY);
    map.addBinding (ShuffleConfig.class).toInstance (Shuffle.FACTORY);
  }
}
