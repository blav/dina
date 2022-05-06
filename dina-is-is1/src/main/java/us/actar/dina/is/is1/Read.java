package us.actar.dina.is.is1;

import us.actar.dina.*;

import static us.actar.dina.randomizers.RegisterRandomizer.NOP;

public class Read extends Base {

  private final int address;

  private final int result;

  public Read (InstructionGroup randomizer, int address, int result) {
    super (randomizer);
    this.address = address;
    this.result = result;
  }

  @Override
  public void register (InstructionSet registry) {
    registry.register (
      new Instruction (String.format ("read_at_r%d_into_r%d", address, result), group) {
        @Override
        public void process (Machine machine, Program state) throws Fault {
          int vaddress = Read.this.getRegisters (state).get (address, NOP);
          Heap heap = machine.getHeap ();
          try {
            Read.this.getRegisters (state).set (result, heap.get (vaddress), machine.getRandomizer (group));
          } catch (IllegalStateException x) {
            throw new Fault (x);
          }
        }
      });
  }
}
