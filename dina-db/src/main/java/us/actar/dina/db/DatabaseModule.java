package us.actar.dina.db;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import us.actar.commons.ModuleSupplier;
import us.actar.dina.sh.CommandsRegistry;
import us.actar.dina.sh.commands.*;
import us.actar.dina.sh.extensions.DatabaseExtension;

import java.util.Properties;

public class DatabaseModule implements ModuleSupplier {

  @Override
  public Module get () {
    return new AbstractModule () {
      @Override
      protected void configure () {
        CommandsRegistry.newCommandBuilder (binder ())
          .registerCommand ("db-open", DatabaseOpen.class)
          .registerCommand ("db-close", DatabaseClose.class)
          .registerCommand ("db-snapshot", DatabaseSnapshot.class)
          .registerCommand ("db-select", DatabaseSelect.class)
          .registerCommand ("db-clear", DatabaseClear.class)
          .registerCommand ("db-option", DatabaseOption.class)
          .done ();

        CommandsRegistry.newExtensionsBuilder (binder ())
          .registerExtension (DatabaseExtension.class)
          .done ();

        // disable c3p0 logs
        Properties p = new Properties (System.getProperties ());
        p.put ("com.mchange.v2.log.MLog", "com.mchange.v2.log.FallbackMLog");
        p.put ("com.mchange.v2.log.FallbackMLog.DEFAULT_CUTOFF_LEVEL", "OFF");
        System.setProperties (p);
      }
    };
  }
}
