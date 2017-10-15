package us.blav.dina.filters;

import us.blav.commons.Chain;
import us.blav.dina.Reclaim;

public class TraceReclaimsFilter implements Chain.Filter<Reclaim> {
  @Override
  public void next (Chain<Reclaim> chain, Reclaim reclaim) {
    chain.next (reclaim);
    int size = reclaim.getReclaimList ().size ();
    if (size > 0)
      System.out.printf ("reclaimed %d reclaim\n", size);
  }
}
