package us.actar.dina.randomizers;

import us.actar.dina.Machine;

public abstract class AbstractRegisterRandomizer<CONF extends RegisterRandomizerConfig> implements RegisterRandomizer<CONF> {

  private final Machine machine;

  private final CONF config;

  public AbstractRegisterRandomizer (Machine machine, CONF config) {
    this.machine = machine;
    this.config = config;
  }

  protected Machine getMachine () {
    return machine;
  }

  protected CONF getConfig () {
    return config;
  }
}
