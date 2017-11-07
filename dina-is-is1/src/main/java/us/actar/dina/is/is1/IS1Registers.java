package us.actar.dina.is.is1;

import us.actar.dina.Fault;
import us.actar.dina.Program;
import us.actar.dina.Registers;
import us.actar.dina.randomizers.RegisterRandomizer;

public class IS1Registers extends Registers {

  private final Program program;

  private final int[] registers;

  public IS1Registers (Program program, int registers) {
    this.program = program;
    this.registers = new int[registers];
  }

  public int get (int register, RegisterRandomizer<?> randomizer) throws Fault {
    ensureValidRegister (register);
    return randomizer.randomizeValue (program, this.registers[register]);
  }

  public void set (int register, int value, RegisterRandomizer<?> randomizer) throws Fault {
    value = randomizer.randomizeValue (program, value);
    ensureValidRegister (register);
    ensureValidValue (value);
    this.registers[register] = value;
  }

  public int[] getRegisters () {
    return registers;
  }

  private void ensureValidRegister (int register) {
    if (register < 0 || register >= registers.length)
      throw new IllegalArgumentException ();
  }

  private void ensureValidValue (int value) throws Fault {
    if (value < 0)
      throw new Fault ();
  }
}
