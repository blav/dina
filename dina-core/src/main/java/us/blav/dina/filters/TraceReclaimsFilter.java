package us.blav.dina.filters;

import us.blav.dina.Chain;
import us.blav.dina.Program;
import us.blav.dina.VirtualMachine;

import java.util.List;

public class TraceReclaimsFilter implements Chain.Filter<List<Program>> {
  @Override
  public void next (Chain<List<Program>> chain, VirtualMachine machine, List<Program> programs) {
    chain.next (machine, programs);
    if (programs.size () > 0)
      System.out.printf ("reclaimed %d programs\n", programs.size ());
  }
}
