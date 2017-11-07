package us.actar.dina;

import com.google.inject.TypeLiteral;

public interface HeapReclaimer extends Extension {

  TypeLiteral<Factory<HeapReclaimer>> FACTORY_TYPE = new TypeLiteral<Factory<HeapReclaimer>> () {
  };

}
