package us.blav.dina.is1;

import us.blav.dina.Fault;
import us.blav.dina.MemoryHeap;

import static us.blav.dina.is1.Processor.Flags.auto_increment_ip;

public class FindBackward implements InstructionFactory {

  @Override
  public void register (Processor processor) {
    for (int label = 0; label < 4; label++) {
      for (int register = 0; register < 4; register++) {
        final int flabel = label;
        final int fregister = register;
        processor.register (
          9 << 4 | label << 2 | register << 0,
          String.format ("find_backward_label%d_into_r%d", flabel, fregister),
          (machine, state) -> {
            MemoryHeap heap = machine.getHeap ();
            int opcode = Label.getOpcode (flabel);
            for (int i = state.getInstructionPointer (); i >= 0; i--)
              if (opcode == heap.get (i))
                state.set (fregister, i);

            throw new Fault ();
          }, auto_increment_ip);
      }
    }
  }
}
