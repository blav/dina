package us.actar.dina;

import us.actar.dina.randomizers.RegisterRandomizer;

import java.util.Optional;

import static java.util.Optional.ofNullable;

public class Program {

  private final Heap.Cell cell;

  private final Registers registers;

  private Heap.Cell child;

  private int instructionPointer;

  private int id;

  private int cycles;

  private int forks;

  private int faults;

  private final int parentId;

  public Program (Program parent, Heap.Cell cell, InstructionSetConfig registers) {
    this.cell = cell;
    this.registers = registers.createRegisters (this);
    this.parentId = ofNullable (parent).map (Program::getId).orElse (0);
  }

  public Heap.Cell getCell () {
    return cell;
  }

  public Heap.Cell getChild () {
    return child;
  }

  public void setChild (Heap.Cell child) throws Fault {
    if (child != null && this.child != null)
      throw new Fault ();

    this.child = child;
  }

  public Registers getRegisters () {
    return registers;
  }

  public int getInstructionPointer () {
    return instructionPointer;
  }

  public void setInstructionPointer (int instructionPointer, RegisterRandomizer<?> randomizer) throws Fault {
    instructionPointer = randomizer.randomizeValue (this, instructionPointer);
    if (instructionPointer < 0 || instructionPointer >= this.cell.getMemoryHeap ().size ())
      throw new Fault ();

    this.instructionPointer = instructionPointer;
  }

  public int getId () {
    return id;
  }

  public int getParentId () {
    return parentId;
  }

  public void setId (int id) {
    this.id = id;
  }

  public int getCycles () {
    return cycles;
  }

  public void incrementCycles () {
    this.cycles++;
  }

  public int getForks () {
    return forks;
  }

  public void incrementForks () {
    this.forks++;
  }

  public void incrementIP () throws Fault {
    setInstructionPointer (getInstructionPointer () + 1, RegisterRandomizer.NOP);
  }

  public int getFaults () {
    return faults;
  }

  public void incrementFaults () {
    this.faults++;
  }
}
