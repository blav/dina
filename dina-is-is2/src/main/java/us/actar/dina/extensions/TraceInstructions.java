package us.actar.dina.extensions;

import us.actar.commons.Chain;
import us.actar.dina.Extension;

public class TraceInstructions implements Extension {
  @Override
  public Chain.Filter<Execute> getExecuteFilter () {
    return (chain, execute) -> {
      
    };
  }
}
