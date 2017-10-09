package us.blav.dina;

import com.google.inject.TypeLiteral;

import java.util.List;

public interface HeapReclaimer {

  TypeLiteral<Factory<HeapReclaimer>> FACTORY_TYPE = new TypeLiteral<Factory<HeapReclaimer>> () {};

  List<Program> reclaim (VirtualMachine machine);

}
