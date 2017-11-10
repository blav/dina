package us.actar.dina.sh.profiler;

import com.google.inject.AbstractModule;
import us.actar.dina.sh.CommandsRegistry;

public class ProfilerModule extends AbstractModule {

  @Override
  protected void configure () {
    CommandsRegistry.newCommandBuilder (binder ())
      .registerCommand ("profiler-stop", ProfilerStop.class)
      .registerCommand ("profiler-start", ProfilerStart.class)
      .registerCommand ("profiler-dump", ProfilerDump.class)
      .done ();

    CommandsRegistry.newExtensionsBuilder (binder ())
      .registerExtension (ProfilerExtension.class)
      .done ();
  }

}
