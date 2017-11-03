package us.actar.dina;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.jline.builtins.Completers.TreeCompleter;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.impl.history.DefaultHistory;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;
import us.actar.dina.console.CommandsRegistry;
import us.actar.dina.console.Context;
import us.actar.dina.console.MainLoop;
import us.actar.dina.console.Stats;
import us.actar.dina.console.commands.Error;
import us.actar.dina.is.is1.IS1Config;
import us.actar.dina.is.is1.IS1Randomizers;
import us.actar.dina.randomizers.FadeConfig;
import us.actar.dina.randomizers.ShiftConfig;
import us.actar.dina.randomizers.ShuffleConfig;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static us.actar.dina.Utils.getBasePath;

public class Main {

  public static void main (String... args) throws Exception {
    Config config = new Config ()
      .setInstructionSet (new IS1Config ()
          .setIpRandomizer (
            new FadeConfig ().setDistance (5).setProbability (10))
          .addRandomizer (IS1Randomizers.WRITE,
            new ShuffleConfig ().setProbability (100).setRange (ShuffleConfig.Range.BYTE))
          .addRandomizer (IS1Randomizers.INCREMENT,
            new ShiftConfig ().setProbability (200).setValue (2))
          .addRandomizer (IS1Randomizers.DECREMENT,
            new ShiftConfig ().setProbability (200).setValue (2))
          .addRandomizer (IS1Randomizers.GOTO,
            new FadeConfig ().setProbability (10).setDistance (10))
          .addRandomizer (IS1Randomizers.FIND,
            new FadeConfig ().setProbability (10).setDistance (10))
        //.addRandomizer (IS1Randomizers.SUBSTRACT, new ShiftConfig ()
        //  .setProbability (200))
        //.addRandomizer (IS1Randomizers.SUBSTRACT, new NopConfig ())
      )
      .setRandomizer (new Config.Randomizer ()
        .setName (Config.DEFAULT)
        .setSeed (0))
      .setReclaimer (new Config.Reclaimer ()
        .setName ("immediate")
        .setThresholdHigh (.75)
        .setThresholdLow (.7)
      )
      .setMemory (100000)
      .addExecutionFilters (
        //"trace-forks"
        //, "trace-instructions"
      )
      .addReclaimerFilters (
        //"trace-reclaims"
      )
      .addBoostrapCode (
        "label0",
        "find_forward_label0_into_r0",
        "find_backward_label0_into_r1",
        "substract_r1_from_r0_into_r2",
        "increment_r0",
        "increment_r2",
        "alloc_r2_bytes_into_r1",
        "add_r2_to_r1_into_r1",
        "label1",

        "increment_r0",
        "decrement_r0",
        "increment_r0",
        "decrement_r0",

        "decrement_r0",
        "decrement_r1",
        "decrement_r2",
        "read_at_r0_into_r3",
        "write_r3_at_r1",
        "if_r2_is_not_null",
        "go_backward_to_label1",
        "fork",
        "go_backward_to_label0",
        "label0"
      )
      .addBoostrapCode (
        "label0",
        "find_forward_label0_into_r0",
        "find_backward_label0_into_r1",
        "substract_r1_from_r0_into_r2",
        "increment_r0",
        "increment_r2",
        "alloc_r2_bytes_into_r1",
        "add_r2_to_r1_into_r1",
        "go_backward_to_label1",
        "fork",
        "go_backward_to_label0",
        "label0"
      )
      ;

    ObjectMapper mapper = new ObjectMapper (new YAMLFactory ()).setDefaultPropertyInclusion (JsonInclude.Include.NON_NULL);
    StringWriter out = new StringWriter ();
    mapper.writer ().writeValue (out, config);
    System.out.println (out.toString ());

    config = mapper.reader ().forType (Config.class).readValue (new StringReader (out.toString ()));

    MachineImpl vm = new MachineImpl (config);
    Stats stats = new Stats ();
    vm.install (ExecutionStep.FILTER_TYPE, (chain, step) -> {
      chain.next (new ExecutionStepDecorator (step) {
        @Override
        public Machine getMachine () {
          return new MachineDecorator (step.getMachine ()) {
            @Override
            public long launch (ProgramState state) {
              long launch = super.launch (state);
              stats.programs = getMachine ().getPrograms ().size ();
              return launch;
            }
          };
        }
      });
    });

    MainLoop loop = new MainLoop (vm);
    ExecutorService executor = Executors.newFixedThreadPool (1);
    executor.execute (loop);

    LineReader reader = LineReaderBuilder.builder ()
      .appName ("dina")
      .completer (new TreeCompleter (CommandsRegistry.getCommands ().entrySet ().stream ()
        .map (e -> e.getValue ().getCompletions (e.getKey ())).collect (toList ())))
      .history (new DefaultHistory ())
      .terminal (TerminalBuilder.terminal ())
      .variable (LineReader.HISTORY_FILE, getBasePath ().resolve ("history").toString ())
      .build ();

    reader.setOpt (LineReader.Option.HISTORY_INCREMENTAL);
    try (Context context = new Context (reader, loop, stats)) {
      System.out.println ("Enter a command (h for help)");
      for (boolean run = true; run; ) {
        try {
          String prompt = new AttributedStringBuilder ()
            .style (AttributedStyle.DEFAULT.foreground (AttributedStyle.GREEN))
            .append ("dina> ")
            .toAnsi ();

          Scanner line = new Scanner (reader.readLine (prompt));
          String cmd = line.next ();
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
