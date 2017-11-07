package us.actar.dina.is.is2;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import us.actar.commons.MapTypeResolver;
import us.actar.commons.ModuleSupplier;
import us.actar.dina.InstructionSetConfig;

public class IS2Module implements ModuleSupplier {

  @Override
  public Module get () {
    return new AbstractModule () {
      @Override
      protected void configure () {
        MapTypeResolver.newBuilder (binder (), InstructionSetConfig.TYPE)
          .registerType ("is2", IS2Config.class)
          .done ();
      }
    };
  }
}
