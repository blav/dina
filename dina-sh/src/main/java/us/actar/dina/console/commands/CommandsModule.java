package us.actar.dina.console.commands;

import com.google.inject.AbstractModule;
import us.actar.dina.console.CommandsRegistry;
import us.actar.dina.console.Context;

import static us.actar.dina.console.Context.Attribute.registerAttribute;

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
      .registerCommand ("debug-start", DebugStart.class)
      .registerCommand ("debug-stop", DebugStop.class)
      .done ();

    registerAttribute (binder (), DebugContext.class);
  }
}
