package us.actar.dina.sh;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import us.actar.commons.ModuleSupplier;
import us.actar.dina.sh.alias.AliasModule;
import us.actar.dina.sh.commands.*;
import us.actar.dina.sh.coupling.CouplingModule;
import us.actar.dina.sh.debug.DebugModule;
import us.actar.dina.sh.profiler.ProfilerModule;

public class DinaShModule implements ModuleSupplier {

  @Override
  public Module get () {
    return new AbstractModule () {
      @Override
      protected void configure () {
        install (new AliasModule ());
        install (new DebugModule ());
        install (new ProfilerModule ());
        install (new CouplingModule ());
        CommandsRegistry.newCommandBuilder (binder ())
          .registerCommand ("help", Help.class)
          .registerCommand ("pause", Pause.class)
          .registerCommand ("resume", Resume.class)
          .registerCommand ("exit", Exit.class)
          .registerCommand ("stats", Stats.class)
          .registerCommand ("dump", Dump.class)
          .registerCommand ("clear", Clear.class)
          .done ();
      }
    };
  }
}
