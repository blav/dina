package us.blav.dina;

import com.google.inject.TypeLiteral;
import us.blav.commons.Chain.Filter;
import us.blav.commons.Chain.Handle;
import us.blav.dina.randomizers.RegisterRandomizer;

import java.util.Collection;

public interface VirtualMachine {

  Config getConfig ();

  MemoryHeap getHeap ();

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
