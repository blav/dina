package us.blav.dina;

import java.util.List;

public interface InstructionProcessor {

  void process (VirtualMachine machine, ProgramState state);

  void launch (VirtualMachine vm, MemoryHeap.Cell cell, List<String> code);

}
