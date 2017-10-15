package us.blav.dina;

import com.google.inject.TypeLiteral;
import us.blav.commons.Chain.Filter;

public interface ExecutionStep {

  TypeLiteral<Filter<ExecutionStep>> FILTER_TYPE = new TypeLiteral<Filter<ExecutionStep>> () {};

  Opcode getOpcode ();

  ProgramState getState ();

  VirtualMachine getMachine ();

}
