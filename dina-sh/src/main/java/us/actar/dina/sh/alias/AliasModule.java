package us.actar.dina.sh.alias;

import com.google.inject.AbstractModule;
import us.actar.dina.sh.CommandsRegistry;

public class AliasModule extends AbstractModule {

  @Override
  protected void configure () {
    CommandsRegistry.newCommandBuilder (binder ())
      .registerCommand ("alias", AliasAdd.class)
      .registerCommand ("unalias", AliasRemove.class)
      .registerCommand ("alias-list", AliasList.class)
      .done ();

    CommandsRegistry.newExtensionsBuilder (binder ())
      .registerExtension (AliasExtension.class)
      .done ();

  }

}
