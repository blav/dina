package us.blav.dina.console;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import us.blav.commons.ModuleSupplier;
import us.blav.dina.console.commands.CommandsModule;

public class ConsoleModule implements ModuleSupplier {
  @Override
  public Module get () {
    return new AbstractModule () {
      @Override
      protected void configure () {
        install (new CommandsModule ());
      }
    };
  }
}
