package us.blav.dina.dql;

import us.blav.dina.ProgramState;
import us.blav.dina.dql.Evaluable;
import us.blav.dina.dql.EvaluationContext;
import us.blav.dina.dql.Value;

import java.util.HashMap;
import java.util.Map;

import static java.util.Optional.ofNullable;

public class EvaluableCache extends Evaluable {

  private final Evaluable evaluable;

  private final Map<Integer, Value> cache;

  public EvaluableCache (Evaluable evaluable) {
    super (evaluable.getType ());
    this.evaluable = evaluable;
    this.cache = new HashMap<> ();
  }

  @Override
  public Value evaluate (EvaluationContext context) {
    Integer id = ofNullable (context.getCurrentProgram ()).map (ProgramState::getId).orElse (0);
    Value v = cache.get (id);
    if (v != null)
      return v;

    cache.put (id, v = evaluable.evaluate (context));
    return v;
  }
}
