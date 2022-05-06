package us.actar.dina.is.is2;

import us.actar.dina.*;
import us.actar.dina.Fault;
import us.actar.dina.randomizers.RegisterRandomizer;

import static us.actar.dina.is.is2.IS2InstructionGroup.*;

public class Write extends Base {

  public Write () {
    super (WRITE);
  }

  @Override
  public void register (InstructionSet registry) {
    registry.register (
      new Instruction ("write", group) {
        @Override
        public void process (Machine machine, Program state) throws Fault {
          Heap heap = machine.getHeap ();
          IS2Registers registers = Write.this.getRegisters (state);
          int vaddress = registers.pop (machine.getRandomizer (group));
          int v = registers.pop (RegisterRandomizer.NOP);
          if (v >> 8 > 0)
            throw new Fault ();

          boolean ok = state.getCell ().contains (vaddress);
          if (ok == false && state.getChild () != null)
            ok = state.getChild ().contains (vaddress);

          if (ok) {
            heap.set (vaddress, (byte) v);
            return;
          }

          throw new Fault ();
        }
      });
  }
}
