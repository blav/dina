package us.blav.dina.is1;

import us.blav.dina.Fault;
import us.blav.dina.InstructionFactory;
import us.blav.dina.InstructionRegistry;
import us.blav.dina.MemoryHeap;

import static us.blav.dina.InstructionProcessor.Decorator.auto_increment_ip;

public class FindForward implements InstructionFactory {

  @Override
  public void register (InstructionRegistry registry) {
    for (int label = 0; label < 4; label++) {
      int opcode = Label.getOpcode (label);
      for (int register = 0; register < 4; register++) {
        final int fregister = register;
        registry.register (
          8 << 4 | label << 2 | register << 0,
          String.format ("find_forward_label%d_into_r%d", label, fregister),
          (machine, state) -> {
            MemoryHeap heap = machine.getHeap ();
            for (int i = state.getInstructionPointer (); i < heap.size (); i++) {
              if (opcode == heap.get (i)) {
                state.set (fregister, i);
                return;
              }
            }

            throw new Fault ();
          }, auto_increment_ip);
      }
    }
  }
}
