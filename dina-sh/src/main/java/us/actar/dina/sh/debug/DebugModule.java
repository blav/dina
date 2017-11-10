package us.actar.dina.sh.debug;

import com.google.inject.AbstractModule;
import us.actar.dina.sh.CommandsRegistry;

public class DebugModule extends AbstractModule {

  @Override
  protected void configure () {
    CommandsRegistry.newCommandBuilder (binder ())
      .registerCommand ("debug-start", DebugStart.class)
      .registerCommand ("debug-stop", DebugStop.class)
      .registerCommand ("debug-start", DebugStart.class)
      .done ();

    CommandsRegistry.newExtensionsBuilder (binder ())
      .registerExtension (DebugExtension.class)
      .done ();

  }
}
