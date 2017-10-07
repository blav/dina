package us.blav.dina;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class Injection {

  private final Injector injector = Guice.createInjector (new AbstractModule () {
    @Override
    protected void configure () {
      bind (MemoryHeap.class).toProvider (() -> new MemoryHeap (1000));
      bind (InstructionProcessor.class).toInstance (new InstructionProcessor1 ());
    }
  });

  private static final Injection singleton = new Injection ();

  public static Injector getInjector () {
    return singleton.injector;
  }
}
