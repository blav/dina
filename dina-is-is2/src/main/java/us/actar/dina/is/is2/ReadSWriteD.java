package us.actar.dina.is.is2;

import us.actar.dina.*;
import us.actar.dina.Fault;

import static us.actar.dina.is.is2.IS2InstructionGroup.WRITE;
import static us.actar.dina.is.is2.IS2Registers.Register.D;
import static us.actar.dina.is.is2.IS2Registers.Register.S;
import static us.actar.dina.randomizers.RegisterRandomizer.NOP;

public class ReadSWriteD extends Base {


  public ReadSWriteD () {
    super (WRITE);
  }

  @Override
  public void register (InstructionSet registry) {
    registry.register (
      new Instruction ("read_at_S_write_at_D", group) {
        @Override
        public void process (Machine machine, Program state) throws Fault {
          IS2Registers registers = ReadSWriteD.this.getRegisters (state);
          Heap heap = machine.getHeap ();
          try {
            heap.set (registers.get (D, NOP),
              machine.getRandomizer (group).randomizeValue (state, heap.get (registers.get (S, NOP))));
          } catch (IllegalStateException x) {
            throw new Fault (x);
          }
        }
      });
  }
}
