package us.actar.dina.sh.coupling;

import us.actar.dina.InstructionSet;
import us.actar.dina.Opcode;
import us.actar.dina.sh.Command;
import us.actar.dina.sh.Context;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.singleton;
import static java.util.stream.Collectors.*;

public class CouplingDump extends Command {

  public CouplingDump () {
    super ("Dump the coupling matrix.");
  }

  @Override
  public boolean run (Context context, Scanner arguments) {
    return context.executePaused (c -> {
      InstructionSet set = c.getLoop ().getMachine ().getInstructionSet ();
      Map<Integer, String> symbols = set.getOpcodes ().stream ()
        .collect (toMap (Opcode::getOpcode, Opcode::getSymbol));

      symbols.put (- 1, "_first");
      symbols.put (set.getOpcodes ().size (), "_last");

      c.getOut ().println (Stream.of (
        Arrays.asList ("symbol", "_first"),
        set.getOpcodes ().stream ().map (Opcode::getSymbol).collect (toList ()),
        singleton ("_last"))
        .flatMap (Collection::stream)
        .collect (Collectors.joining (";")));

      int[][] coupling = Coupling.coupling (c.getLoop ().getMachine ());
      for (int i = 0; i < coupling.length; i++) {
        context.getOut ().printf ("%s;%s\n",
          symbols.get (i - 1),
          Arrays.stream (coupling[i]).boxed ()
            .map (Object::toString)
            .map (s -> s.equals ("0") ? "" : s)
            .collect (joining (";"))
        );
      }

      context.getOut ().flush ();
      return true;
    });
  }
}
