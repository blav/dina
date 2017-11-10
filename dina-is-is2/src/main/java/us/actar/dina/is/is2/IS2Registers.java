package us.actar.dina.is.is2;

import us.actar.dina.Fault;
import us.actar.dina.Program;
import us.actar.dina.Registers;
import us.actar.dina.randomizers.RegisterRandomizer;

public class IS2Registers extends Registers {

  public enum Register {

    S (0),
    D (1),
    V (2);

    private final int index;

    Register (int index) {
      this.index = index;
    }

    public int get (IS2Registers registers) {
      return registers.registers[index];
    }

    public void set (IS2Registers registers, int value) {
      registers.registers[index] = value;
    }
  }

  private final Program program;

  private final int[] registers;

  private final int[] stack;

  private int position;

  public IS2Registers (IS2Config config, Program program) {
    this.program = program;
    this.registers = new int[Register.values ().length];
    this.stack = new int[config.getStackSize ()];
    this.position = 0;
  }

  public int get (Register register, RegisterRandomizer<?> randomizer) throws Fault {
    return randomizer.randomizeValue (program, register.get (this));
  }

  public void set (Register register, int value, RegisterRandomizer<?> randomizer) throws Fault {
    register.set (this, ensureValidValue (randomizer.randomizeValue (program, value)));
  }

  public void push (int value, RegisterRandomizer<?> randomizer) throws Fault {
    if (position >= stack.length)
      throw new Fault ();

    stack[position++] = ensureValidValue (randomizer.randomizeValue (program, value));
  }

  public int pop (RegisterRandomizer<?> randomizer) throws Fault {
    if (position <= 0)
      throw new Fault ();

    if (position < stack.length)
      stack[position] = 0;

    return ensureValidValue (randomizer.randomizeValue (program, stack[--position]));
  }

  public int peek (RegisterRandomizer<?> randomizer) throws Fault {
    if (position <= 0) {
      throw new Fault ();
    } else {
      return ensureValidValue (randomizer.randomizeValue (program, stack[position - 1]));
    }
  }

  private int ensureValidValue (int value) throws Fault {
    if (value < 0) {
      throw new Fault ();
    } else {
      return value;
    }
  }
}
