package us.blav.dina.is.is1;

import us.blav.dina.InstructionSetConfig;

import java.util.Arrays;
import java.util.Collection;

import static us.blav.dina.is.is1.IS1Module.MODULE_NAME;

public class ModuleConfig extends InstructionSetConfig<IS1Randomizers> {
  public ModuleConfig () {
    super (IS1Randomizers.class, MODULE_NAME);
  }

  @Override
  protected Collection<IS1Randomizers> getNames () {
    return Arrays.asList (IS1Randomizers.values ());
  }
}
