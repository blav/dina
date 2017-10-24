package us.actar.dina;

import com.google.inject.TypeLiteral;
import us.actar.commons.Chain;

import java.util.List;

public interface Reclaim {

  TypeLiteral<Chain.Filter<Reclaim>> FILTER_TYPE = new TypeLiteral<Chain.Filter<Reclaim>> () {};

  Machine getMachine ();

  List<Program> getReclaimList ();

}
