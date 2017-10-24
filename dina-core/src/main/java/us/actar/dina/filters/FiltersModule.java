package us.actar.dina.filters;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.MapBinder;
import us.actar.dina.Reclaim;
import us.actar.commons.Chain.Filter;
import us.actar.dina.ExecutionStep;

import static com.google.inject.multibindings.MapBinder.newMapBinder;

public class FiltersModule extends AbstractModule {
  @Override
  protected void configure () {
    MapBinder<String, Filter<ExecutionStep>> filters = newMapBinder (binder (), TypeLiteral.get (String.class), ExecutionStep.FILTER_TYPE);
    filters.addBinding ("trace-forks").to (TraceForksFilter.class);
    filters.addBinding ("trace-instructions").to (TraceInstructionsFilter.class);

    MapBinder<String, Filter<Reclaim>> reclaim = newMapBinder (binder (), TypeLiteral.get (String.class), Reclaim.FILTER_TYPE);
    reclaim.addBinding ("trace-reclaims").to (TraceReclaimsFilter.class);
  }
}
