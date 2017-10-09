package us.blav.dina;

public class ProgramState implements EnergyTracker, Program {

  private final MemoryHeap.Cell cell;

  private MemoryHeap.Cell child;

  private int instructionPointer;

  private long id;

  public ProgramState (MemoryHeap.Cell cell, int registers) {
    this.cell = cell;
    this.registers = new int[registers];
  }

  @Override
  public MemoryHeap.Cell getCell () {
    return cell;
  }

  @Override
  public MemoryHeap.Cell getChild () {
    return child;
  }

  public void setChild (MemoryHeap.Cell child) throws Fault {
    if (child != null && this.child != null)
      throw new Fault ();

    this.child = child;
  }

  @Override
  public int getInstructionPointer () {
    return instructionPointer;
  }

  public void setInstructionPointer (int instructionPointer) throws Fault {
    if (instructionPointer < 0 || instructionPointer >= this.cell.getMemoryHeap ().size ())
      throw new Fault ();

    this.instructionPointer = instructionPointer;
  }

  @Override
  public long getId () {
    return id;
  }

  public void setId (long id) {
    this.id = id;
  }

  @Override
  public void reportEnergySpent (long amount) {
  }

  public void set (int register, int value) throws Fault {
    ensureValidRegister (register);
    ensureValidValue (value);
    this.registers[register] = value;
  }

  public void incrementIP () throws Fault {
    setInstructionPointer (getInstructionPointer () + 1);
  }

  private void ensureValidValue (int value) throws Fault {
    if (value < 0)
      throw new Fault ();
  }

  public int get (int register) {
    ensureValidRegister (register);
    return this.registers [register];
  }

  public int[] getRegisters () {
    return registers;
  }

  private void ensureValidRegister (int register) {
    if (register < 0 || register >= registers.length)
      throw new IllegalArgumentException ();
  }

  private final int [] registers;

}
