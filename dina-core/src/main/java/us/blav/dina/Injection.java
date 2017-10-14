package us.blav.dina;

import com.google.inject.*;
import com.google.inject.util.Types;
import us.blav.dina.filters.FiltersModule;
import us.blav.dina.randomizers.RandomizersModule;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;

import static com.google.inject.name.Names.named;
import static us.blav.dina.Config.DEFAULT;

public class Injection {

  private final Injector injector = Guice.createInjector (new AbstractModule () {
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
  });

  private static final Injection singleton = new Injection ();

  public static Injector getInjector () {
    return singleton.injector;
  }

  public static <T> T getInstance (TypeLiteral<T> type) {
    return getInjector ().getInstance (Key.get (type));
  }

  public static <T> T getInstance (String name, TypeLiteral<T> type) {
    return getInjector ().getInstance (Key.get (type, named (name)));
  }

  public static <T> T getInstance (Class<T> type) {
    return getInjector ().getInstance (type);
  }

  public static <T> T getInstance (String name, Class<T> type) {
    return getInjector ().getInstance (Key.get (type, named (name)));
  }

  public static <T> Set<T> getInstanceSet (String name, Class<T> type) {
    return (Set<T>) getInjector ().getInstance (Key.get (Types.setOf (type), named (name)));
  }

  public static <K, V> Map<K, V> getInstanceMap (Class<K> key, Class<V> value) {
    return (Map<K, V>) getInjector ().getInstance (Key.get (Types.mapOf (key, value)));
  }

  public static <K, V> Map<K, V> getInstanceMap (TypeLiteral<K> key, TypeLiteral<V> value) {
    return (Map<K, V>) getInjector ().getInstance (Key.get (Types.mapOf (key.getType (), value.getType ())));
  }
}
