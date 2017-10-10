package us.blav.dina;

import static us.blav.dina.RegisterRandomizer.NOP;

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

  public void setInstructionPointer (int instructionPointer, RegisterRandomizer<?> randomizer) throws Fault {
    instructionPointer = randomizer.randomizeValue (this, instructionPointer);
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

  public void set (int register, int value, RegisterRandomizer<?> randomizer) throws Fault {
    value = randomizer.randomizeValue (this, value);
    ensureValidRegister (register);
    ensureValidValue (value);
    this.registers[register] = value;
  }

  public void incrementIP () throws Fault {
    setInstructionPointer (getInstructionPointer () + 1, NOP);
  }

  private void ensureValidValue (int value) throws Fault {
    if (value < 0)
      throw new Fault ();
  }

  public int get (int register, RegisterRandomizer<?> randomizer) {
    ensureValidRegister (register);
    return randomizer.randomizeValue (this, this.registers [register]);
  }

  public int[] getRegisters () {
    return registers;
  }

  @Override
  public int getFaults () {
    return faults;
  }

  public void incrementFaults () {
    this.faults ++;
  }

  private void ensureValidRegister (int register) {
    if (register < 0 || register >= registers.length)
      throw new IllegalArgumentException ();
  }

  private final int [] registers;

  private int faults;

}
