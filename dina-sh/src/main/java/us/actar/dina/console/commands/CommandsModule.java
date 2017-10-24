package us.actar.dina.console.commands;

import com.google.inject.AbstractModule;
import us.actar.dina.console.CommandsRegistry;

public class CommandsModule extends AbstractModule {
  @Override
  protected void configure () {
    CommandsRegistry.newBuilder (binder ())
      .registerCommand ("help", Help.class)
      .registerCommand ("pause", Pause.class)
      .registerCommand ("resume", Resume.class)
      .registerCommand ("exit", Exit.class)
      .registerCommand ("stats", Stats.class)
      .registerCommand ("dump", Dump.class)
      .registerCommand ("clear", Clear.class)
      .done ();
  }
}
