package us.actar.dina.console;

import com.google.inject.Binder;
import com.google.inject.multibindings.MapBinder;
import us.actar.commons.Injector;

import java.util.HashMap;
import java.util.Map;

public class CommandsRegistry {

  public interface RegistryBuilder {

    RegistryBuilder registerCommand (String names, Class<? extends Command> command);

    void done ();

  }

  public static Command getCommand (String name) {
    return getCommands ().get (name);
  }

  public static Map<String, Command> getCommands () {
    return Injector.getMap (String.class, Command.class);
  }

  public static RegistryBuilder newBuilder (Binder binder) {
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
}
