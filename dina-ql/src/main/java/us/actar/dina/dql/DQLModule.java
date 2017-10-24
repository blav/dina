package us.actar.dina.dql;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import us.actar.commons.ModuleSupplier;
import us.actar.dina.console.CommandsRegistry;
import us.actar.dina.console.commands.Select;

public class DQLModule implements ModuleSupplier {
  @Override
  public Module get () {
    return new AbstractModule () {
      @Override
      protected void configure () {
        CommandsRegistry.newBuilder (binder ())
          .registerCommand ("select", Select.class)
          .done ();
      }
    };
  }
}
