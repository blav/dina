package us.blav.dina;

import us.blav.commons.MapTypeResolver;
import us.blav.dina.randomizers.RegisterRandomizer;

public class InstructionSetConfigTypeResolver extends MapTypeResolver<InstructionSetConfig<? extends InstructionSetConfig<?, ?>, ? extends RegisterRandomizer.Name>> {
  public InstructionSetConfigTypeResolver () {
    super (InstructionSetConfig.TYPE);
  }
}
