package us.blav.dina.is1;

import us.blav.dina.Fault;
import us.blav.dina.MemoryHeap;
import us.blav.dina.ProgramState;

public class State extends ProgramState {

  public State (MemoryHeap.Cell cell) {
    super (cell);
    this.registers = new int[4];
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

  private void ensureValidRegister (int register) {
    if (register < 0 || register >= 4)
      throw new IllegalArgumentException ();
  }

  private final int [] registers;

}
