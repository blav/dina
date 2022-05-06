package us.actar.dina.sh;

import java.io.PrintStream;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import us.actar.commons.Disposable;

import static us.actar.commons.Injector.getMap;

public class Context implements AutoCloseable {

  private final MainLoop loop;

  private final List<Completer> completers;

  private final PrintStream out;

  private final PrintStream err;

  private final LineReader reader;

  private final Map<Class<? extends Extension>, Extension> extensions;

  public Context (LineReader reader, MainLoop loop, List<Completer> completers) {
    this.reader = reader;
    this.loop = loop;
    this.completers = completers;
    this.out = System.out;
    this.err = System.err;
    this.extensions = getMap (CommandsRegistry.KEY, CommandsRegistry.VALUE);
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

  @SuppressWarnings ("unchecked")
  public <A extends Extension> A getExtension (Class<A> extensionClass) {
    return (A) extensions.get (extensionClass);
  }

  @Override
  public void close () {
    this.extensions.values ().forEach (Extension::close);
  }

  public Disposable addCompleter (Completer completer) {
    this.completers.add (completer);
    return () -> this.completers.remove (completer);
  }

  public interface Extension extends AutoCloseable {

    default void init (Context context) {
    }

    @Override
    default void close () {
    }
  }
}
