package us.actar.commons;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.google.inject.util.Types;

import java.util.Map;
import java.util.ServiceLoader;

import static com.google.inject.name.Names.named;

public class Injector {

  private static final Injector singleton = new Injector ();

  private final com.google.inject.Injector injector = Guice.createInjector (new AbstractModule () {
    @Override
    protected void configure () {
      ServiceLoader.load (ModuleSupplier.class).forEach (ms -> install (ms.get ()));
    }
  });

  public static com.google.inject.Injector getInjector () {
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

  @SuppressWarnings ("unchecked")
  public static <K, V> Map<K, V> getMap (Class<K> key, Class<V> value) {
    return (Map<K, V>) getInjector ().getInstance (Key.get (Types.mapOf (key, value)));
  }

  @SuppressWarnings ("unchecked")
  public static <K, V> Map<K, V> getMap (TypeLiteral<K> key, TypeLiteral<V> value) {
    return (Map<K, V>) getInjector ().getInstance (Key.get (Types.mapOf (key.getType (), value.getType ())));
  }
}
