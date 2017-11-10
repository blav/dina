package us.actar.dina;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.jline.builtins.Completers.TreeCompleter;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.impl.completer.AggregateCompleter;
import org.jline.reader.impl.history.DefaultHistory;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;
import us.actar.dina.sh.CommandsRegistry;
import us.actar.dina.sh.Context;
import us.actar.dina.sh.MainLoop;
import us.actar.dina.sh.commands.Error;
import us.actar.dina.sh.alias.AliasExtension;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.Collections.synchronizedList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static us.actar.dina.Utils.getBasePath;

public class Main {

  public static void main (String... args) throws Exception {
    ObjectMapper mapper = new ObjectMapper (new YAMLFactory ())
      .setDefaultPropertyInclusion (JsonInclude.Include.NON_NULL);

    Config config = mapper.reader ().forType (Config.class)
      .readValue (Main.class.getClassLoader ().getResourceAsStream ("sample-config.yaml"));

    Machine vm = new Machine (config);
    MainLoop loop = new MainLoop (vm);
    ExecutorService executor = Executors.newFixedThreadPool (1);
    executor.execute (loop);

    List<Completer> completers = synchronizedList (new ArrayList<Completer> ());
    completers.add (new TreeCompleter (CommandsRegistry.getCommands ().entrySet ().stream ()
      .map (e -> e.getValue ().getCompletions (e.getKey ())).collect (toList ())));

    LineReader reader = LineReaderBuilder.builder ()
      .appName ("dina")
      .completer (new AggregateCompleter (completers))
      .history (new DefaultHistory ())
      .terminal (TerminalBuilder.terminal ())
      .variable (LineReader.HISTORY_FILE, getBasePath ().resolve ("history").toString ())
      .build ();

    reader.setOpt (LineReader.Option.HISTORY_INCREMENTAL);
    try (Context context = new Context (reader, loop, completers)) {
      System.out.println ("Enter a command (h for help)");
      for (boolean run = true; run; ) {
        try {
          String prompt = new AttributedStringBuilder ()
            .style (AttributedStyle.DEFAULT.foreground (AttributedStyle.GREEN))
            .append ("dina> ")
            .toAnsi ();

          Scanner line = new Scanner (reader.readLine (prompt));
          String cmd = line.next ();
          String aliased = context.getExtension (AliasExtension.class).getCommand (context, cmd);
          if (aliased != null) {
            line = new Scanner (aliased);
            cmd = line.next ();
          }

          run = ofNullable (CommandsRegistry.getCommand (cmd)).orElse (new Error (cmd)).run (context, line);
        } catch (NoSuchElementException e) {
          continue;
        }
      }
    }

    loop.requestState (MainLoop.State.stopped);
    executor.shutdown ();
  }
}
