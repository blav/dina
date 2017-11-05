package us.actar.dina.extensions;

import us.actar.commons.Chain;
import us.actar.dina.Extension;

public class TraceReclaims implements Extension {

  @Override
  public Chain.Filter<Reclaim> getReclaimFilter () {
    return (chain, reclaim) -> {
      chain.next (reclaim);
      int size = reclaim.getReclaimList ().size ();
      if (size > 0)
        System.out.printf ("reclaimed %d reclaim\n", size);
    };
  }
}
