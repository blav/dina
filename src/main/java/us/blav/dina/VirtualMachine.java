package us.blav.dina;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class VirtualMachine {

  private final MemoryHeap memoryHeap;

  private final InstructionProcessor instructionProcessor;

  private final List<ProgramState> programStates;

  @Inject
  public VirtualMachine (MemoryHeap memoryHeap, InstructionProcessor instructionProcessor) {
    this.memoryHeap = memoryHeap;
    this.instructionProcessor = instructionProcessor;
    this.programStates = new ArrayList<> ();
  }

  public InstructionProcessor getInstructionProcessor () {
    return instructionProcessor;
  }

  public MemoryHeap getMemoryHeap () {
    return memoryHeap;
  }

  public void start () {
  }
}
