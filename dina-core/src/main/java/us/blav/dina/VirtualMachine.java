package us.blav.dina;

import us.blav.dina.randomizers.RegisterRandomizer;

import java.util.Collection;

public interface VirtualMachine {

  Config getConfig ();

  MemoryHeap getHeap ();

  Randomizer getRandomizer ();

  long launch (ProgramState state);

  void kill (long pid);

  Collection<Program> getPrograms ();

  RegisterRandomizer<?> getRandomizer (RegisterRandomizer.Name name);

}
