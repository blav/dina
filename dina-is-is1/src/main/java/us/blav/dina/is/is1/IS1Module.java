package us.blav.dina.is.is1;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import us.blav.commons.MapTypeResolver;
import us.blav.commons.ModuleSupplier;
import us.blav.dina.InstructionSetConfig;

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
