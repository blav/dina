package us.blav.dina.console;

import com.google.inject.Binder;
import com.google.inject.multibindings.MapBinder;
import us.blav.commons.Injector;

import java.util.HashMap;
import java.util.Map;

import static java.util.Arrays.stream;

public class CommandsRegistry {

  public interface RegistryBuilder {

    RegistryBuilder registerCommand (Class<? extends Command> command, String... names);

    void done ();

  }

  public static Command getCommand (String name) {
    return Injector.getMap (String.class, Command.class).get (name);
  }

  public static RegistryBuilder newBuilder (Binder binder) {
    Map<String, Class<? extends Command>> commands = new HashMap<> ();
    return new RegistryBuilder () {
      @Override
      public RegistryBuilder registerCommand (Class<? extends Command> command, String... names) {
        stream (names).forEach (n -> commands.put (n, command));
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
