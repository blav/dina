package us.blav.dina.is1;

import us.blav.dina.Fault;
import us.blav.dina.MemoryHeap;

import static us.blav.dina.is1.Processor.Flags.auto_increment_ip;

public class GotoForward implements InstructionFactory {

  @Override
  public void register (Processor processor) {
    for (int label = 0; label < 4; label++) {
      final int opcode = Label.getOpcode (label);
      final int flabel = label;
      processor.register (
        13 << 4 | 2 << 2 | label << 0,
        String.format ("go_forward_to_label%d", flabel),
        (machine, state) -> {
          MemoryHeap heap = machine.getHeap ();
          for (int offset = state.getInstructionPointer (); offset < heap.size (); offset++) {
            if (heap.get (offset) == opcode) {
              state.setInstructionPointer (offset);
              return;
            }
          }

          throw new Fault ();
        }, auto_increment_ip);
    }
  }
}
