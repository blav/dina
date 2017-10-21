package us.blav.dina.dql;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import us.blav.commons.ModuleSupplier;
import us.blav.dina.console.CommandsRegistry;
import us.blav.dina.console.commands.*;

public class DQLModule implements ModuleSupplier {
  @Override
  public Module get () {
    return new AbstractModule () {
      @Override
      protected void configure () {
        CommandsRegistry.newBuilder (binder ())
          .registerCommand (Select.class, "select")
          .done ();
      }
    };
  }
}
