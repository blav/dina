package us.actar.dina.db;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import us.actar.commons.ModuleSupplier;
import us.actar.dina.console.CommandsRegistry;
import us.actar.dina.console.commands.*;

import java.util.Properties;

import static us.actar.dina.console.Context.registerExtension;

public class DatabaseModule implements ModuleSupplier {

  @Override
  public Module get () {
    return new AbstractModule () {
      @Override
      protected void configure () {
        CommandsRegistry.newBuilder (binder ())
          .registerCommand ("db-open", DatabaseOpen.class)
          .registerCommand ("db-close", DatabaseClose.class)
          .registerCommand ("db-snapshot", DatabaseSnapshot.class)
          .registerCommand ("db-select", DatabaseSelect.class)
          .registerCommand ("db-clear", DatabaseClear.class)
          .done ();

        registerExtension (binder (), ConnectionPoolExtension.class);

        // disable c3p0 logs
        Properties p = new Properties (System.getProperties ());
        p.put ("com.mchange.v2.log.MLog", "com.mchange.v2.log.FallbackMLog");
        p.put ("com.mchange.v2.log.FallbackMLog.DEFAULT_CUTOFF_LEVEL", "OFF");
        System.setProperties (p);
      }
    };
  }
}
