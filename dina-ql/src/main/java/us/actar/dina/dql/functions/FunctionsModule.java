package us.actar.dina.dql.functions;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.MapBinder;

public class FunctionsModule extends AbstractModule {
  @Override
  protected void configure () {
    MapBinder<String, Operation> operations = MapBinder.newMapBinder (binder (), String.class, Operation.class);
    operations.addBinding ("ip").to (Ip.class);
    operations.addBinding ("id").to (Id.class);
    operations.addBinding ("size").to (Size.class);
    operations.addBinding ("faults").to (Faults.class);
    operations.addBinding ("forks").to (Forks.class);
    operations.addBinding ("position").to (Position.class);
    operations.addBinding ("entropy").to (Entropy.class);
    operations.addBinding ("cycles").to (Cycles.class);
  }
}
