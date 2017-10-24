package us.actar.dina;

import us.actar.dina.randomizers.RegisterRandomizer;

public class ProgramState implements Program {

  private final Heap.Cell cell;

  private Heap.Cell child;

  private int instructionPointer;

  private int id;

  private int cycles;

  private int forks;

  public ProgramState (Heap.Cell cell, int registers) {
    this.cell = cell;
    this.registers = new int[registers];
  }

  @Override
  public Heap.Cell getCell () {
    return cell;
  }

  @Override
  public Heap.Cell getChild () {
    return child;
  }

  public void setChild (Heap.Cell child) throws Fault {
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
  public int getId () {
    return id;
  }

  public void setId (int id) {
    this.id = id;
  }

  @Override
  public int getCycles () {
    return cycles;
  }

  public void incrementCycles () {
    this.cycles ++;
  }

  @Override
  public int getForks () {
    return forks;
  }

  public void incrementForks () {
    this.forks ++;
  }

  public void set (int register, int value, RegisterRandomizer<?> randomizer) throws Fault {
    value = randomizer.randomizeValue (this, value);
    ensureValidRegister (register);
    ensureValidValue (value);
    this.registers[register] = value;
  }

  public void incrementIP () throws Fault {
    setInstructionPointer (getInstructionPointer () + 1, RegisterRandomizer.NOP);
  }

  private void ensureValidValue (int value) throws Fault {
    if (value < 0)
      throw new Fault ();
  }

  public int get (int register, RegisterRandomizer<?> randomizer) throws Fault {
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
