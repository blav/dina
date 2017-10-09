package us.blav.dina.filters;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.MapBinder;
import us.blav.dina.ExecutionFilter;

import static com.google.inject.multibindings.MapBinder.newMapBinder;

public class FiltersModule extends AbstractModule {
  @Override
  protected void configure () {
    MapBinder<String, ExecutionFilter> filters = newMapBinder (binder (), String.class, ExecutionFilter.class);
    filters.addBinding ("trace-forks").to (TraceForksFilter.class);
    filters.addBinding ("trace-instructions").to (TraceInstructionsFilter.class);
  }
}
