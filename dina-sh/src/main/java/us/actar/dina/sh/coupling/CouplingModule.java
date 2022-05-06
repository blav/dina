package us.actar.dina.sh.coupling;

import com.google.inject.AbstractModule;
import us.actar.dina.sh.CommandsRegistry;

public class CouplingModule extends AbstractModule {

  @Override
  protected void configure () {
    CommandsRegistry.newCommandBuilder (binder ())
      .registerCommand ("coupling-dump", CouplingDump.class)
      //.registerCommand ("profiler-start", ProfilerStart.class)
      //.registerCommand ("profiler-dump", ProfilerDump.class)
      .done ();
  }
}
