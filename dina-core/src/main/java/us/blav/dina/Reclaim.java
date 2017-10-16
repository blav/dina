package us.blav.dina;

import com.google.inject.TypeLiteral;
import us.blav.commons.Chain;

import java.util.List;

public interface Reclaim {

  TypeLiteral<Chain.Filter<Reclaim>> FILTER_TYPE = new TypeLiteral<Chain.Filter<Reclaim>> () {};

  VirtualMachine getMachine ();

  List<Program> getReclaimList ();

}
