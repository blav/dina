package us.blav.dina.filters;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.MapBinder;
import us.blav.commons.Chain.Filter;
import us.blav.dina.ExecutionStep;
import us.blav.dina.HeapReclaimer;
import us.blav.dina.Reclaim;

import static com.google.inject.multibindings.MapBinder.newMapBinder;

public class FiltersModule extends AbstractModule {
  @Override
  protected void configure () {
    MapBinder<String, Filter<ExecutionStep>> filters = newMapBinder (binder (), TypeLiteral.get (String.class), ExecutionStep.FILTER_TYPE);
    filters.addBinding ("trace-forks").to (TraceForksFilter.class);
    filters.addBinding ("trace-instructions").to (TraceInstructionsFilter.class);

    MapBinder<String, Filter<Reclaim>> reclaim = newMapBinder (binder (), TypeLiteral.get (String.class), HeapReclaimer.FILTER_TYPE);
    reclaim.addBinding ("trace-reclaims").to (TraceReclaimsFilter.class);
  }
}
