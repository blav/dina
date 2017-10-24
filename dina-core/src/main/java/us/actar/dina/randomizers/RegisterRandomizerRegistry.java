package us.actar.dina.randomizers;

import com.google.inject.Binder;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.MapBinder;
import us.actar.commons.Injector;
import us.actar.commons.MapTypeResolver;

import java.util.HashMap;
import java.util.Map;

import static com.google.inject.multibindings.MapBinder.newMapBinder;

public class RegisterRandomizerRegistry {

  private static final TypeLiteral<Class<? extends RegisterRandomizerConfig>> KEY =
    new TypeLiteral<Class<? extends RegisterRandomizerConfig>> () {};

  private static final TypeLiteral<RegisterRandomizer.Factory<? extends RegisterRandomizerConfig>> VALUE =
    new TypeLiteral<RegisterRandomizer.Factory<? extends RegisterRandomizerConfig>> () {};

  private static final TypeLiteral<Map<Class<? extends RegisterRandomizerConfig>, RegisterRandomizer.Factory<? extends RegisterRandomizerConfig>>> MAP =
    new TypeLiteral<Map<Class<? extends RegisterRandomizerConfig>, RegisterRandomizer.Factory<? extends RegisterRandomizerConfig>>> () {};

  public static Map<Class<? extends RegisterRandomizerConfig>, RegisterRandomizer.Factory<? extends RegisterRandomizerConfig>> createRandomizers () {
    return Injector.getInstance (MAP);
  }

  public static RegisterRandomizer.RegistryBuilder newBuilder (Binder binder) {
    MapTypeResolver.RegistryBuilder<RegisterRandomizerConfig>
      resolvers = MapTypeResolver.newBuilder (binder, RegisterRandomizerConfig.TYPE);

    Map<Class<? extends RegisterRandomizerConfig>, RegisterRandomizer.Factory<? extends RegisterRandomizerConfig>>
      map = new HashMap<> ();

    return new RegisterRandomizer.RegistryBuilder () {
      @Override
      public <CONF extends RegisterRandomizerConfig> RegisterRandomizer.RegistryBuilder registerRandomizer (String typeId, Class<CONF> configClass, RegisterRandomizer.Factory<CONF> factory) {
        resolvers.registerType (typeId, configClass);
        map.put (configClass, factory);
        return this;
      }

      @Override
      public void done () {
        MapBinder<Class<? extends RegisterRandomizerConfig>, RegisterRandomizer.Factory<? extends RegisterRandomizerConfig>>
          mb = newMapBinder (binder, KEY, VALUE);

        map.forEach ((k, v) -> mb.addBinding (k).toInstance (v));
        resolvers.done ();
      }
    };
  }
}
