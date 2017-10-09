package us.blav.dina;

import com.google.inject.*;
import com.google.inject.name.Names;
import us.blav.dina.is1.IS1Module;

public class Injection {

  private final Injector injector = Guice.createInjector (new AbstractModule () {
    @Override
    protected void configure () {
      install (new IS1Module ());
      bind (MemoryHeap.FACTORY_TYPE)
        .toInstance (config -> new MemoryHeap (config.getMemory ()));

      bind (Randomizer.FACTORY_TYPE)
        .annotatedWith (Names.named (Config.Randomizer.DEFAULT))
        .toInstance (config -> new DefaultRandomizer (config.getRandomizer ().getSeed ()));
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
}
