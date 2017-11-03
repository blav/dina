package us.actar.dina.console;

import com.google.inject.Binder;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.MapBinder;
import org.jline.reader.LineReader;

import java.io.PrintStream;
import java.util.Map;
import java.util.function.Function;

import static com.google.inject.multibindings.MapBinder.newMapBinder;
import static us.actar.commons.Injector.*;

public class Context implements AutoCloseable {

  private final MainLoop loop;

  private final Stats stats;

  private final PrintStream out;

  private final PrintStream err;

  private final LineReader reader;

  private final Map<Class<? extends Attribute>, Attribute> attributes;

  public interface Attribute extends AutoCloseable {

    @Override
    void close ();

    TypeLiteral<Class<? extends Attribute>> KEY = new TypeLiteral<Class<? extends Attribute>> () {};

    TypeLiteral<Attribute> VALUE = TypeLiteral.get (Attribute.class);

    static void registerAttribute (Binder binder, Class<? extends Attribute> attribute) {
      MapBinder<Class<? extends Attribute>, Attribute> mb = newMapBinder (binder, KEY, VALUE);
      mb.addBinding (attribute).to (attribute);
    }
  }

  public Context (LineReader reader, MainLoop loop, Stats stats) {
    this.reader = reader;
    this.loop = loop;
    this.stats = stats;
    this.out = System.out;
    this.err = System.err;
    this.attributes = getMap (Attribute.KEY, Attribute.VALUE);
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

  public Stats getStats () {
    return stats;
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

  public <A extends Attribute> A getAttribute (Class<A> attributeClass) {
    return (A) attributes.get (attributeClass);
  }

  @Override
  public void close () throws Exception {
    this.attributes.values ().forEach (Attribute::close);
  }
}
