package us.blav.dina.is1;

import us.blav.dina.Fault;
import us.blav.dina.VirtualMachine;

public class RaiseFault implements Instruction {

  @Override
  public void process (VirtualMachine machine, State state) throws Fault {
    throw new Fault ();
  }
}
