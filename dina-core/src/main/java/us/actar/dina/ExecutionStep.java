package us.actar.dina;

import com.google.inject.TypeLiteral;
import us.actar.commons.Chain.Filter;

public interface ExecutionStep {

  TypeLiteral<Filter<ExecutionStep>> FILTER_TYPE = new TypeLiteral<Filter<ExecutionStep>> () {};

  Opcode getOpcode ();

  ProgramState getState ();

  Machine getMachine ();

}
