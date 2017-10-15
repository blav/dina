package us.blav.dina;

import com.google.inject.TypeLiteral;
import us.blav.commons.Chain.Filter;

import java.util.List;

public interface HeapReclaimer {

  TypeLiteral<Factory<HeapReclaimer>> FACTORY_TYPE = new TypeLiteral<Factory<HeapReclaimer>> () {};

  TypeLiteral<Filter<Reclaim>> FILTER_TYPE = new TypeLiteral<Filter<Reclaim>> () {};

  List<Program> reclaim (VirtualMachine machine);

}
