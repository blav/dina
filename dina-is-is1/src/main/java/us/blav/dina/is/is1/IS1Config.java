package us.blav.dina.is.is1;

import us.blav.dina.InstructionFactory;
import us.blav.dina.InstructionSetConfig;
import us.blav.dina.is.lib.*;

import java.util.Arrays;
import java.util.Collection;

import static us.blav.dina.is.is1.IS1Randomizers.*;

public class IS1Config extends InstructionSetConfig<IS1Config, IS1Randomizers> {

  private static final InstructionFactory[] INSTRUCTIONS = {
    new Nop (NOP),
    new Label (LABEL, 0),
    new Label (LABEL, 1),
    new Label (LABEL, 2),
    new Label (LABEL, 3),
    new FindForward (FIND, 0, 0),
    new FindForward (FIND, 1, 0),
    new FindForward (FIND, 2, 0),
    new FindForward (FIND, 3, 0),
    new FindBackward (FIND, 0, 1),
    new FindBackward (FIND, 1, 1),
    new FindBackward (FIND, 2, 1),
    new FindBackward (FIND, 3, 1),
    new Substract (SUBSTRACT, 1, 0, 2),
    new Increment (INCREMENT, 0),
    new Increment (INCREMENT, 2),
    new Alloc (ALLOC, 1, 2),
    new Add (ADD, 2, 1, 1),
    new Decrement (DECREMENT, 0),
    new Decrement (DECREMENT, 1),
    new Decrement (DECREMENT, 2),
    new Read (READ, 0, 3),
    new Write (WRITE, 1, 3),
    new IfNotNull (IF, 2),
    new Fork (FORK),
    new GotoBackward (GOTO, 0),
    new GotoBackward (GOTO, 1),
    new GotoBackward (GOTO, 2),
    new GotoBackward (GOTO, 3),
    new GotoForward (GOTO, 0),
    new GotoForward (GOTO, 1),
    new GotoForward (GOTO, 2),
    new GotoForward (GOTO, 3),
  };

  public IS1Config () {
    super (IS1Randomizers.class, 4);
  }

  @Override
  protected Collection<IS1Randomizers> getNames () {
    return Arrays.asList (IS1Randomizers.values ());
  }

  @Override
  protected InstructionFactory[] getInstructions () {
    return INSTRUCTIONS;
  }
}
