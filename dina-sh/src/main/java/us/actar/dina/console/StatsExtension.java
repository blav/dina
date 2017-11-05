package us.actar.dina.console;

import us.actar.commons.Chain;
import us.actar.dina.Extension;

public class StatsExtension implements Context.Extension, Extension {

  private int programs;

  @Override
  public void init (Context context) {
    context.getLoop ().getMachine ().install (this);
  }

  @Override
  public Chain.Filter<Launch> getLaunchFilter () {
    return (chain, payloadLaunch) -> {
      chain.next (payloadLaunch);
      programs = payloadLaunch.getMachine ().getPrograms ().size ();
    };
  }

  public int getPrograms () {
    return programs;
  }

}
