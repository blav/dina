package us.actar.dina;

import us.actar.commons.MapTypeResolver;
import us.actar.dina.randomizers.RegisterRandomizer;

public class InstructionSetConfigTypeResolver extends MapTypeResolver<InstructionSetConfig<? extends InstructionSetConfig<?, ?>, ? extends RegisterRandomizer.Name>> {
  public InstructionSetConfigTypeResolver () {
    super (InstructionSetConfig.TYPE);
  }
}
