package us.blav.dina.console.commands;

import com.google.inject.AbstractModule;
import us.blav.dina.console.CommandsRegistry;

public class CommandsModule extends AbstractModule {
  @Override
  protected void configure () {
    CommandsRegistry.newBuilder (binder ())
      .registerCommand (Help.class, "h", "help")
      .registerCommand (Pause.class, "p", "pause")
      .registerCommand (Resume.class, "r", "resume")
      .registerCommand (Exit.class, "x", "exit")
      .registerCommand (Stats.class, "s", "stats")
      .registerCommand (Dump.class, "d", "dump")
      .done ();
  }
}
