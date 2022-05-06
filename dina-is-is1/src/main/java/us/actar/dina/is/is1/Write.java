package us.actar.dina.is.is1;

import us.actar.dina.*;
import us.actar.dina.randomizers.RegisterRandomizer;

public class Write extends Base {

  private final int address;

  private final int value;

  public Write (InstructionGroup randomizer, int address, int value) {
    super (randomizer);
    this.address = address;
    this.value = value;
  }

  @Override
  public void register (InstructionSet registry) {
    registry.register (
      new Instruction (String.format ("write_r%d_at_r%d", value, address), group) {
        @Override
        public void process (Machine machine, Program state) throws Fault {
          Heap heap = machine.getHeap ();
          int v = Write.this.getRegisters (state).get (value, RegisterRandomizer.NOP);
          if (v >> 8 > 0)
            throw new Fault ();

          int vaddress = Write.this.getRegisters (state).get (address, machine.getRandomizer (group));
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
