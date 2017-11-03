package us.actar.dina;

import com.google.inject.TypeLiteral;
import us.actar.commons.Chain.Filter;
import us.actar.commons.Chain.Handle;
import us.actar.dina.randomizers.RegisterRandomizer;

import java.util.Collection;

public interface Machine {

  Config getConfig ();

  Handle install (MachineFilters mf);

  Heap getHeap ();

  Randomizer getRandomizer ();

  long launch (ProgramState state);

  void kill (int pid);

  Collection<Program> getPrograms ();

  ProgramState getProgram (int pid);

  RegisterRandomizer<?> getRandomizer (RegisterRandomizer.Name name);

  InstructionRegistry getRegistry ();

  <TYPE> Handle install (TypeLiteral<Filter<TYPE>> type, Filter<TYPE> filter);

  void update ();

}
