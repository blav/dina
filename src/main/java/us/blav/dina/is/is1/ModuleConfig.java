package us.blav.dina.is.is1;

import us.blav.dina.InstructionFactory;
import us.blav.dina.InstructionSetConfig;
import us.blav.dina.is.lib.*;

import java.util.Arrays;
import java.util.Collection;

import static us.blav.dina.is.is1.IS1Randomizers.*;

public class ModuleConfig extends InstructionSetConfig<IS1Randomizers> {

  private static final InstructionFactory[] INSTRUCTIONS = {
    new Label (LABEL, 0),
    new FindForward (FIND, 0, 0),
    new FindBackward (FIND, 0, 1),
    new Substract (SUBSTRACT, 1, 0, 2),
    new Increment (INCREMENT, 0),
    new Increment (INCREMENT, 2),
    new Alloc (ALLOC, 1, 2),
    new Add (ADD, 2, 1, 1),
    new Label (LABEL, 1),
    new Decrement (DECREMENT, 0),
    new Decrement (DECREMENT, 1),
    new Decrement (DECREMENT, 2),
    new Read (READ, 0, 3),
    new Write (WRITE, 1, 3),
    new IfNotNull (IF, 2),
    new GotoBackward (GOTO, 1),
    new Fork (FORK),
    new GotoBackward (GOTO, 0)
  };

  public ModuleConfig () {
    super (IS1Randomizers.class, "is1", 4);
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
