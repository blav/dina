package us.blav.dina;

import com.google.inject.*;
import com.google.inject.name.Names;
import com.google.inject.util.Types;
import us.blav.dina.filters.FiltersModule;
import us.blav.dina.is1.IS1Module;

import java.util.Map;
import java.util.Set;

public class Injection {

  private final Injector injector = Guice.createInjector (new AbstractModule () {
    @Override
    protected void configure () {
      install (new IS1Module ());
      install (new FiltersModule ());
      bind (MemoryHeap.FACTORY_TYPE)
        .toInstance (config -> new MemoryHeap (config.getMemory ()));

      bind (Randomizer.FACTORY_TYPE)
        .annotatedWith (Names.named (Config.DEFAULT))
        .toInstance (config -> new DefaultRandomizer (config.getRandomizer ().getSeed ()));

      bind (HeapReclaimer.FACTORY_TYPE)
        .annotatedWith (Names.named (Config.DEFAULT))
        .toInstance (config -> new HeapReclaimerImpl (config));
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
    return getInjector ().getInstance (Key.get (type, Names.named (name)));
  }

  public static <T> T getInstance (Class<T> type) {
    return getInjector ().getInstance (type);
  }

  public static <T> T getInstance (String name, Class<T> type) {
    return getInjector ().getInstance (Key.get (type, Names.named (name)));
  }

  public static <T> Set<T> getInstanceSet (String name, Class<T> type) {
    return (Set<T>) getInjector ().getInstance (Key.get (Types.setOf (type), Names.named (name)));
  }

  public static <K, V> Map<K, V> getInstanceMap (Class<K> key, Class<V> value) {
    return (Map<K, V>) getInjector ().getInstance (Key.get (Types.mapOf (key, value)));
  }
}
