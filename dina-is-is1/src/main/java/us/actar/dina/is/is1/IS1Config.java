package us.actar.dina.is.is1;

import com.fasterxml.jackson.annotation.JsonIgnore;
import us.actar.dina.InstructionFactory;
import us.actar.dina.InstructionSetConfig;
import us.actar.dina.Program;
import us.actar.dina.Registers;

import java.util.Arrays;
import java.util.Collection;

public class IS1Config extends InstructionSetConfig<IS1Config, IS1InstructionGroup> {

  private static final InstructionFactory[] INSTRUCTIONS = {
    new Nop (IS1InstructionGroup.NOP),
    new Label (IS1InstructionGroup.LABEL, 0),
    new Label (IS1InstructionGroup.LABEL, 1),
    new Label (IS1InstructionGroup.LABEL, 2),
    new Label (IS1InstructionGroup.LABEL, 3),
    new FindForward (IS1InstructionGroup.FIND, 0, 0),
    new FindForward (IS1InstructionGroup.FIND, 1, 0),
    new FindForward (IS1InstructionGroup.FIND, 2, 0),
    new FindForward (IS1InstructionGroup.FIND, 3, 0),
    new FindBackward (IS1InstructionGroup.FIND, 0, 1),
    new FindBackward (IS1InstructionGroup.FIND, 1, 1),
    new FindBackward (IS1InstructionGroup.FIND, 2, 1),
    new FindBackward (IS1InstructionGroup.FIND, 3, 1),
    new Substract (IS1InstructionGroup.SUBSTRACT, 1, 0, 2),
    new Increment (IS1InstructionGroup.INCREMENT, 0),
    new Increment (IS1InstructionGroup.INCREMENT, 2),
    new Alloc (IS1InstructionGroup.ALLOC, 1, 2),
    new Add (IS1InstructionGroup.ADD, 2, 1, 1),
    new Decrement (IS1InstructionGroup.DECREMENT, 0),
    new Decrement (IS1InstructionGroup.DECREMENT, 1),
    new Decrement (IS1InstructionGroup.DECREMENT, 2),
    new Read (IS1InstructionGroup.READ, 0, 3),
    new Write (IS1InstructionGroup.WRITE, 1, 3),
    new IfNotNull (IS1InstructionGroup.IF, 2),
    new Fork (IS1InstructionGroup.FORK),
    new GotoBackward (IS1InstructionGroup.GOTO, 0),
    new GotoBackward (IS1InstructionGroup.GOTO, 1),
    new GotoBackward (IS1InstructionGroup.GOTO, 2),
    new GotoBackward (IS1InstructionGroup.GOTO, 3),
    new GotoForward (IS1InstructionGroup.GOTO, 0),
    new GotoForward (IS1InstructionGroup.GOTO, 1),
    new GotoForward (IS1InstructionGroup.GOTO, 2),
    new GotoForward (IS1InstructionGroup.GOTO, 3),
  };

  @JsonIgnore
  private final int registers;

  public IS1Config () {
    super (IS1InstructionGroup.class);
    this.registers = 4;
  }

  @Override
  protected Collection<IS1InstructionGroup> getGroups () {
    return Arrays.asList (IS1InstructionGroup.values ());
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
