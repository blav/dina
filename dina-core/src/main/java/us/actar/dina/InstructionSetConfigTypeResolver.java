package us.actar.dina;

import us.actar.commons.MapTypeResolver;

public class InstructionSetConfigTypeResolver extends MapTypeResolver<InstructionSetConfig<? extends InstructionSetConfig<?, ?>, ? extends InstructionGroup>> {

  public InstructionSetConfigTypeResolver () {
    super (InstructionSetConfig.TYPE);
  }
}
