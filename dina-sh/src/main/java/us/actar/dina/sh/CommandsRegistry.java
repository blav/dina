package us.actar.dina.sh;

import com.google.inject.Binder;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.MapBinder;
import us.actar.commons.Injector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.inject.multibindings.MapBinder.newMapBinder;

public class CommandsRegistry {

  static final TypeLiteral<Class<? extends Context.Extension>>
    KEY = new TypeLiteral<Class<? extends Context.Extension>> () {
  };

  static final TypeLiteral<Context.Extension>
    VALUE = TypeLiteral.get (Context.Extension.class);

  public static Command getCommand (String name) {
    return getCommands ().get (name);
  }

  public static Map<String, Command> getCommands () {
    return Injector.getMap (String.class, Command.class);
  }

  public static RegistryBuilder newCommandBuilder (Binder binder) {
    Map<String, Class<? extends Command>> commands = new HashMap<> ();
    return new RegistryBuilder () {
      @Override
      public RegistryBuilder registerCommand (String name, Class<? extends Command> command) {
        commands.put (name, command);
        return this;
      }

      @Override
      public void done () {
        MapBinder<String, Command> mb = MapBinder.newMapBinder (binder, String.class, Command.class);
        commands.forEach ((k, v) -> mb.addBinding (k).to (v));
      }
    };
  }

  public static ExtensionBuilder newExtensionsBuilder (Binder binder) {
    return new ExtensionBuilder (binder);
  }

  public interface RegistryBuilder {

    RegistryBuilder registerCommand (String names, Class<? extends Command> command);

    void done ();

  }

  public static class ExtensionBuilder {

    private final List<Class<? extends Context.Extension>> extensions;

    private final Binder binder;

    private ExtensionBuilder (Binder binder) {
      this.binder = binder;
      this.extensions = new ArrayList<> ();
    }

    public ExtensionBuilder registerExtension (Class<? extends Context.Extension> extension) {
      this.extensions.add (extension);
      return this;
    }

    public void done () {
      MapBinder<Class<? extends Context.Extension>, Context.Extension> mb = newMapBinder (binder, KEY, VALUE);
      this.extensions.forEach (e -> mb.addBinding (e).to (e));
    }
  }
}
