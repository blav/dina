package us.actar.dina.console;

import com.google.inject.Binder;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.MapBinder;
import org.jline.reader.LineReader;

import java.io.PrintStream;
import java.util.Map;
import java.util.function.Function;

import static com.google.inject.multibindings.MapBinder.newMapBinder;
import static us.actar.commons.Injector.getMap;

public class Context implements AutoCloseable {

  private final MainLoop loop;

  private final PrintStream out;

  private final PrintStream err;

  private final LineReader reader;

  private final Map<Class<? extends Extension>, Extension> extensions;

  private static final TypeLiteral<Class<? extends Extension>>
    KEY = new TypeLiteral<Class<? extends Extension>> () {};

  private static final TypeLiteral<Extension>
    VALUE = TypeLiteral.get (Extension.class);

  public static void registerExtension (Binder binder, Class<? extends Extension> extension) {
    MapBinder<Class<? extends Extension>, Extension> mb = newMapBinder (binder, KEY, VALUE);
    mb.addBinding (extension).to (extension);
  }

  public interface Extension extends AutoCloseable {
    default void init (Context context) {
    }

    @Override
    default void close () {
    }
  }

  public Context (LineReader reader, MainLoop loop) {
    this.reader = reader;
    this.loop = loop;
    this.out = System.out;
    this.err = System.err;
    this.extensions = getMap (KEY, VALUE);
    this.extensions.values ().forEach (e -> e.init (this));
  }

  public boolean executePaused (Function<Context, Boolean> action) {
    MainLoop loop = getLoop ();
    MainLoop.State state = loop.getActualState ();
    try {
      loop.requestState (MainLoop.State.paused);
      return action.apply (this);
    } finally {
      loop.requestState (state);
    }
  }

  public MainLoop getLoop () {
    return loop;
  }

  public LineReader getReader () {
    return reader;
  }

  public PrintStream getErr () {
    return err;
  }

  public PrintStream getOut () {
    return out;
  }

  public <A extends Extension> A getExtension (Class<A> extensionClass) {
    return (A) extensions.get (extensionClass);
  }

  @Override
  public void close () throws Exception {
    this.extensions.values ().forEach (Extension::close);
  }
}
