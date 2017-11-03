package us.actar.dina.dql.functions;

import us.actar.dina.console.commands.Dump;
import us.actar.dina.dql.EvaluationContext;
import us.actar.dina.dql.Value;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UncheckedIOException;

public class Code implements Operation {

  @Override
  public Value compute (EvaluationContext context) {
    return new Value (Dump.dump (context.getMachine (), context.getCurrentProgram (), new StringWriter ()).toString ());
  }
}
