package us.actar.dina.is.is2;

import us.actar.dina.InstructionSet;

import static us.actar.dina.is.is2.IS2Randomizers.NOP;

public class Fault extends Base {

  public Fault () {
    super (NOP);
  }

  @Override
  public void register (InstructionSet registry) {
    registry.register (
      "fault",
      (machine, state) -> {
        throw new us.actar.dina.Fault ();
      }
    );
  }
}
