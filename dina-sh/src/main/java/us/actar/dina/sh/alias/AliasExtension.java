package us.actar.dina.sh.alias;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.jline.reader.Candidate;
import us.actar.commons.Handle;
import us.actar.dina.Utils;
import us.actar.dina.sh.Context;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class AliasExtension implements Context.Extension {

  private Aliases aliases;

  private static final ObjectMapper MAPPER = new ObjectMapper (new YAMLFactory ())
    .setDefaultPropertyInclusion (JsonInclude.Include.NON_NULL);

  private Handle completer;

  public Aliases getAliases () {
    return aliases;
  }

  public static class Aliases {

    private final Map<String, String> aliases;

    public Aliases () {
      this.aliases = new HashMap<> ();
    }

    public Map<String, String> getAliases () {
      return this.aliases;
    }
  }

  @Override
  public void init (Context context) {
    Path aliases = getAliasesPath ();
    if (Files.exists (aliases) == false) {
      this.aliases = new Aliases ();
      return;
    }

    try (Reader reader = Files.newBufferedReader (aliases)) {
      this.aliases = MAPPER.reader ().forType (Aliases.class).readValue (reader);
    } catch (IOException e) {
      context.getErr ().printf ("Could ot load .aliases file: %s\n", e.getMessage ());
      this.aliases = new Aliases ();
    }

    this.completer = context.addCompleter ((reader, line, candidates) -> {
      candidates.addAll (
        getAliases ().aliases.keySet ().stream ()
          .map (Candidate::new).collect (toList ()));
    });
  }

  private Path getAliasesPath () {
    return Utils.getBasePath ().resolve ("aliases");
  }

  public void addAlias (Context context, String alias, String command) {
    this.aliases.getAliases ().put (alias, command);
    save ();
  }

  public void removeAlias (Context context, String alias) {
    this.aliases.getAliases ().remove (alias);
    save ();
  }

  public String getCommand (Context context, String alias) {
    return this.aliases.getAliases ().get (alias);
  }

  @Override
  public void close () {
    if (this.completer != null) {
      this.completer.uninstall ();
      this.completer = null;
    }
  }

  private void save () {
    try (Writer writer = Files.newBufferedWriter (getAliasesPath ())) {
      MAPPER.writer ().writeValue (writer, this.aliases);
    } catch (IOException e) {
      e.printStackTrace ();
    }
  }
}
