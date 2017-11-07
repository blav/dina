package us.actar.dina;

import com.google.inject.TypeLiteral;
import us.actar.dina.randomizers.RegisterRandomizer;

public interface Randomizer {

  TypeLiteral<Factory<Randomizer>> FACTORY_TYPE = new TypeLiteral<Factory<Randomizer>> () {
  };

  boolean nextBoolean ();

  int nextInt ();

  default RegisterRandomizer<?> getRegisterRandomizer (RegisterRandomizer.Name name) {
    return RegisterRandomizer.NOP;
  }

}
