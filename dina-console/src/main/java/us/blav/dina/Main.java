package us.blav.dina;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import us.blav.dina.console.Context;
import us.blav.dina.console.MainLoop;
import us.blav.dina.console.Stats;
import us.blav.dina.console.commands.Error;
import us.blav.dina.is.is1.IS1Config;
import us.blav.dina.is.is1.IS1Randomizers;
import us.blav.dina.randomizers.ShiftConfig;
import us.blav.dina.randomizers.ShuffleConfig;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.Optional.ofNullable;
import static us.blav.dina.Config.DEFAULT;
import static us.blav.dina.console.CommandsRegistry.getCommand;
import static us.blav.dina.console.MainLoop.State.stopped;
import static us.blav.dina.randomizers.ShuffleConfig.Range.BYTE;

public class Main {

  public static void main (String... args) throws Exception {
    Config config = new Config ()
      .setInstructionSet (new IS1Config ()
          .addRandomizer (IS1Randomizers.WRITE,
            new ShuffleConfig ().setProbability (100).setRange (BYTE))
          .addRandomizer (IS1Randomizers.INCREMENT,
            new ShiftConfig ().setProbability (1000).setValue (1))
          .addRandomizer (IS1Randomizers.DECREMENT,
            new ShiftConfig ().setProbability (1000).setValue (1))
        //.addRandomizer (IS1Randomizers.GOTO, new NopConfig ())
        //.addRandomizer (IS1Randomizers.SUBSTRACT, new ShiftConfig ()
        //  .setProbability (200))
        //.addRandomizer (IS1Randomizers.SUBSTRACT, new NopConfig ())
      )
      .setRandomizer (new Config.Randomizer ()
        .setName (DEFAULT)
        .setSeed (0))
      .setReclaimer (new Config.Reclaimer ()
        .setName (DEFAULT)
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
      );

    ObjectMapper mapper = new ObjectMapper (new YAMLFactory ()).setDefaultPropertyInclusion (JsonInclude.Include.NON_NULL);
    StringWriter out = new StringWriter ();
    mapper.writer ().writeValue (out, config);
    System.out.println (out.toString ());

    config = mapper.reader ().forType (Config.class).readValue (new StringReader (out.toString ()));

    VirtualMachineImpl vm = new VirtualMachineImpl (config);
    Stats stats = new Stats ();
    vm.install (ExecutionStep.FILTER_TYPE, (chain, step) -> {
      chain.next (new ExecutionStepDecorator (step) {
        @Override
        public VirtualMachine getMachine () {
          return new VirtualMachineDecorator (step.getMachine ()) {
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

    Scanner scanner = new Scanner (System.in);
    Context context = new Context (loop, stats);
    System.out.println ("Enter a command (h for help)");
    for (boolean run = true; run; ) {
      System.out.printf ("> ");
      String cmd = scanner.next ();
      run = ofNullable (getCommand (cmd)).orElse (new Error (cmd)).run (context, scanner);
    }

    loop.requestState (stopped);
    executor.shutdown ();
  }
}
