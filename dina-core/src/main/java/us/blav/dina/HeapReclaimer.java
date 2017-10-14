package us.blav.dina;

import com.google.inject.TypeLiteral;
import us.blav.dina.Chain.Filter;

import java.util.List;

public interface HeapReclaimer {

  TypeLiteral<Factory<HeapReclaimer>> FACTORY_TYPE = new TypeLiteral<Factory<HeapReclaimer>> () {};

  TypeLiteral<Filter<List<Program>>> FILTER_TYPE = new TypeLiteral<Filter<List<Program>>> () {};

  List<Program> reclaim (VirtualMachine machine);

}
