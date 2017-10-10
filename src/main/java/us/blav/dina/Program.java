package us.blav.dina;

public interface Program {

  MemoryHeap.Cell getCell ();

  MemoryHeap.Cell getChild ();

  int getInstructionPointer ();

  long getId ();

  int getFaults ();

}
