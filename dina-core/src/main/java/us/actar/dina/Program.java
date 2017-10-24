package us.actar.dina;

public interface Program {

  Heap.Cell getCell ();

  Heap.Cell getChild ();

  int getInstructionPointer ();

  int getId ();

  int getForks ();

  int getFaults ();

  int getCycles ();

}
