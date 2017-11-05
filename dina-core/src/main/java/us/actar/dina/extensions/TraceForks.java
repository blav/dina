package us.actar.dina.extensions;

import us.actar.commons.Chain.Filter;
import us.actar.dina.Extension;

public class TraceForks implements Extension {

  @Override
  public Filter<Launch> getLaunchFilter () {
    return (chain, launch) -> {
      chain.next (launch);
      System.out.printf ("launched program %d - size=%d\n",
        launch.getLaunchedId (), launch.getProgram ().getCell ().getSize ());
    };
  }
}
