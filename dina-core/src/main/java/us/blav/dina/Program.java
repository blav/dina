package us.blav.dina;

public interface Program {

  MemoryHeap.Cell getCell ();

  MemoryHeap.Cell getChild ();

  int getInstructionPointer ();

  int getId ();

  int getFaults ();

}
