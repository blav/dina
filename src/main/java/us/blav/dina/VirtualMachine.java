package us.blav.dina;

public interface VirtualMachine {

  InstructionProcessor getProcessor ();

  MemoryHeap getHeap ();

  Randomizer getRandomizer ();

  FaultHandler getFaultHandler ();

  long launch (ProgramState state);

  void kill (long pid);
}
