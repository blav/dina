package us.blav.dina;

public class ProgramState<REGISTERS> implements EnergyTracker {

  private final MemoryHeap.Cell cell;

  private MemoryHeap.Cell child;

  private int instructionPointer;

  public ProgramState (MemoryHeap.Cell cell) {
    this.cell = cell;
  }

  public MemoryHeap.Cell getCell () {
    return cell;
  }

  public MemoryHeap.Cell getChild () {
    return child;
  }

  public void setChild (MemoryHeap.Cell child) throws Fault {
    if (this.child != null)
      throw new Fault ();

    this.child = child;
  }

  public long getInstructionPointer () {
    return instructionPointer;
  }

  public void setInstructionPointer (int instructionPointer) throws Fault {
    if (instructionPointer < 0 || instructionPointer >= this.cell.getMemoryHeap ().size ())
      throw new Fault ();

    this.instructionPointer = instructionPointer;
  }

  @Override
  public void reportEnergySpent (long amount) {
  }
}
