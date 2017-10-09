package us.blav.dina;

import com.google.inject.TypeLiteral;

public interface Randomizer {

  TypeLiteral<Factory<Randomizer>> FACTORY_TYPE = new TypeLiteral<Factory<Randomizer>> () {};

  boolean nextBoolean ();

}
