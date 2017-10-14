package us.blav.dina;

import com.google.inject.TypeLiteral;
import us.blav.dina.randomizers.RegisterRandomizer;

public interface Randomizer {

  TypeLiteral<Factory<Randomizer>> FACTORY_TYPE = new TypeLiteral<Factory<Randomizer>> () {};

  boolean nextBoolean ();

  int nextInt ();

  default RegisterRandomizer<?> getRegisterRandomizer (RegisterRandomizer.Name name)  {
    return RegisterRandomizer.NOP;
  }

}
