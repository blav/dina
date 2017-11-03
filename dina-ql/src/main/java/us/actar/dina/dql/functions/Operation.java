package us.actar.dina.dql.functions;

import us.actar.dina.dql.EvaluationContext;
import us.actar.dina.dql.Value;

@FunctionalInterface
public interface Operation {

  Value compute (EvaluationContext context);

}
