package us.actar.dina.is.is2;

import us.actar.dina.*;
import us.actar.dina.Fault;

import static us.actar.dina.is.is2.IS2InstructionGroup.READ;
import static us.actar.dina.randomizers.RegisterRandomizer.NOP;

public class Read extends Base {


  public Read () {
    super (READ);
  }

  @Override
  public void register (InstructionSet registry) {
    registry.register (
      new Instruction ("read", group) {
        @Override
        public void process (Machine machine, Program state) throws Fault {
          IS2Registers registers = Read.this.getRegisters (state);
          int vaddress = registers.pop (NOP);
          Heap heap = machine.getHeap ();
          try {
            registers.push (heap.get (vaddress), machine.getRandomizer (group));
          } catch (IllegalStateException x) {
            throw new Fault (x);
          }
        }
      });
  }
}
