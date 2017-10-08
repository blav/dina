package us.blav.dina.is1;

import us.blav.dina.Fault;
import us.blav.dina.VirtualMachine;

interface Instruction {

  void process (VirtualMachine machine, State state) throws Fault;

}
