package us.blav.dina;

import java.util.Collection;

public interface VirtualMachine {

  Config getConfig ();

  MemoryHeap getHeap ();

  Randomizer getRandomizer ();

  long launch (ProgramState state);

  void kill (long pid);

  Collection<Program> getPrograms ();

  HeapReclaimer getReclaimer ();

  RegisterRandomizer<?> getRandomizer (RegisterRandomizer.Name name);

}
