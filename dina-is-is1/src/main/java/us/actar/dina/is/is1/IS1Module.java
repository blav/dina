package us.actar.dina.is.is1;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import us.actar.dina.InstructionSetConfig;
import us.actar.commons.MapTypeResolver;
import us.actar.commons.ModuleSupplier;

public class IS1Module implements ModuleSupplier {
  @Override
  public Module get () {
    return new AbstractModule () {
      @Override
      protected void configure () {
        MapTypeResolver.newBuilder (binder (), InstructionSetConfig.TYPE)
          .registerType ("is1", IS1Config.class)
          .done ();
      }
    };
  }
}
