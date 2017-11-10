package us.actar.dina.is.is2;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import us.actar.dina.*;
import us.actar.dina.is.is2.IS2Registers.Register;

import java.util.*;

import static java.util.Arrays.stream;
import static java.util.stream.IntStream.range;

public class IS2Config extends InstructionSetConfig<IS2Config, IS2Randomizers> {

  private static final List<InstructionFactory> INSTRUCTIONS;
  static {
    INSTRUCTIONS = new ArrayList<> ();
    INSTRUCTIONS.add (new Fault ());
    INSTRUCTIONS.add (new Nop ());
    INSTRUCTIONS.add (new Substract ());
    INSTRUCTIONS.add (new Increment ());
    INSTRUCTIONS.add (new Alloc ());
    INSTRUCTIONS.add (new Add ());
    INSTRUCTIONS.add (new Decrement ());
    INSTRUCTIONS.add (new Read ());
    INSTRUCTIONS.add (new Write ());
    INSTRUCTIONS.add (new ReadSWriteD ());
    INSTRUCTIONS.add (new IfNotNull ());
    INSTRUCTIONS.add (new IfNull ());
    INSTRUCTIONS.add (new Fork ());
    INSTRUCTIONS.add (new Jump ());
    INSTRUCTIONS.add (new Reset ());
    INSTRUCTIONS.add (new Swap ());
    INSTRUCTIONS.add (new Dup ());
    INSTRUCTIONS.add (new Drop ());
    range (0, 4).forEach (i -> {
      INSTRUCTIONS.add (new Label (i));
      INSTRUCTIONS.add (new FindForward (i));
      INSTRUCTIONS.add (new FindBackward (i));
    });

    stream (Register.values ()).map (Push::new).forEach (INSTRUCTIONS::add);
    stream (Register.values ()).map (Pop::new).forEach (INSTRUCTIONS::add);
    stream (Register.values ()).map (IncrementR::new).forEach (INSTRUCTIONS::add);
    stream (Register.values ()).map (DecrementR::new).forEach (INSTRUCTIONS::add);
    range (0, 4).boxed ().map (PushN::new).forEach (INSTRUCTIONS::add);
  };

  @JsonProperty("stack-size")
  private final int stackSize;

  public IS2Config () {
    super (IS2Randomizers.class);
    this.stackSize = 4;
  }

  @Override
  protected Collection<IS2Randomizers> getNames () {
    return Arrays.asList (IS2Randomizers.values ());
  }

  @Override
  protected Collection<InstructionFactory> getInstructions () {
    return INSTRUCTIONS;
  }

  @Override
  public Registers createRegisters (Program program) {
    return new IS2Registers (this, program);
  }

  @JsonIgnore
  public int getStackSize () {
    return stackSize;
  }
}
