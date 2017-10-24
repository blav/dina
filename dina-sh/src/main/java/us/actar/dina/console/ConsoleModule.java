package us.actar.dina.console;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import us.actar.commons.ModuleSupplier;
import us.actar.dina.console.commands.CommandsModule;

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
