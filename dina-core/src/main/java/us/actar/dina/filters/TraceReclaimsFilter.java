package us.actar.dina.filters;

import us.actar.commons.Chain;
import us.actar.dina.Reclaim;

public class TraceReclaimsFilter implements Chain.Filter<Reclaim> {
  @Override
  public void next (Chain<Reclaim> chain, Reclaim reclaim) {
    chain.next (reclaim);
    int size = reclaim.getReclaimList ().size ();
    if (size > 0)
      System.out.printf ("reclaimed %d reclaim\n", size);
  }
}
