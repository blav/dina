package us.blav.dina.randomizers;

import us.blav.dina.VirtualMachine;

public abstract class AbstractRegisterRandomizer<CONF extends RegisterRandomizerConfig> implements RegisterRandomizer<CONF> {

  private final VirtualMachine machine;

  private final CONF config;

  public AbstractRegisterRandomizer (VirtualMachine machine, CONF config) {
    this.machine = machine;
    this.config = config;
  }

  protected VirtualMachine getMachine () {
    return machine;
  }

  protected CONF getConfig () {
    return config;
  }
}
