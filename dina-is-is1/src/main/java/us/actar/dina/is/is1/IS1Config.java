package us.actar.dina.is.is1;

import com.fasterxml.jackson.annotation.JsonIgnore;
import us.actar.dina.InstructionFactory;
import us.actar.dina.InstructionSetConfig;
import us.actar.dina.Program;
import us.actar.dina.Registers;

import java.util.Arrays;
import java.util.Collection;

public class IS1Config extends InstructionSetConfig<IS1Config, IS1Randomizers> {

  private static final InstructionFactory[] INSTRUCTIONS = {
    new Nop (IS1Randomizers.NOP),
    new Label (IS1Randomizers.LABEL, 0),
    new Label (IS1Randomizers.LABEL, 1),
    new Label (IS1Randomizers.LABEL, 2),
    new Label (IS1Randomizers.LABEL, 3),
    new FindForward (IS1Randomizers.FIND, 0, 0),
    new FindForward (IS1Randomizers.FIND, 1, 0),
    new FindForward (IS1Randomizers.FIND, 2, 0),
    new FindForward (IS1Randomizers.FIND, 3, 0),
    new FindBackward (IS1Randomizers.FIND, 0, 1),
    new FindBackward (IS1Randomizers.FIND, 1, 1),
    new FindBackward (IS1Randomizers.FIND, 2, 1),
    new FindBackward (IS1Randomizers.FIND, 3, 1),
    new Substract (IS1Randomizers.SUBSTRACT, 1, 0, 2),
    new Increment (IS1Randomizers.INCREMENT, 0),
    new Increment (IS1Randomizers.INCREMENT, 2),
    new Alloc (IS1Randomizers.ALLOC, 1, 2),
    new Add (IS1Randomizers.ADD, 2, 1, 1),
    new Decrement (IS1Randomizers.DECREMENT, 0),
    new Decrement (IS1Randomizers.DECREMENT, 1),
    new Decrement (IS1Randomizers.DECREMENT, 2),
    new Read (IS1Randomizers.READ, 0, 3),
    new Write (IS1Randomizers.WRITE, 1, 3),
    new IfNotNull (IS1Randomizers.IF, 2),
    new Fork (IS1Randomizers.FORK),
    new GotoBackward (IS1Randomizers.GOTO, 0),
    new GotoBackward (IS1Randomizers.GOTO, 1),
    new GotoBackward (IS1Randomizers.GOTO, 2),
    new GotoBackward (IS1Randomizers.GOTO, 3),
    new GotoForward (IS1Randomizers.GOTO, 0),
    new GotoForward (IS1Randomizers.GOTO, 1),
    new GotoForward (IS1Randomizers.GOTO, 2),
    new GotoForward (IS1Randomizers.GOTO, 3),
  };

  @JsonIgnore
  private final int registers;

  public IS1Config () {
    super (IS1Randomizers.class);
    this.registers = 4;
  }

  @Override
  protected Collection<IS1Randomizers> getNames () {
    return Arrays.asList (IS1Randomizers.values ());
  }

  @Override
  protected Collection<InstructionFactory> getInstructions () {
    return Arrays.asList (INSTRUCTIONS);
  }

  @Override
  public Registers createRegisters (Program program) {
    return new IS1Registers (program, getRegisters ());
  }

  @JsonIgnore
  public int getRegisters () {
    return registers;
  }
}
