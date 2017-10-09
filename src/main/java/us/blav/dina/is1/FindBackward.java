package us.blav.dina.is1;

import us.blav.dina.*;

import static us.blav.dina.InstructionProcessor.Decorator.auto_increment_ip;

public class FindBackward implements InstructionFactory {

  @Override
  public void register (InstructionRegistry registry) {
    for (int label = 0; label < 4; label++) {
      int opcode = Label.getOpcode (label);
      for (int register = 0; register < 4; register++) {
        final int fregister = register;
        registry.register (
          9 << 4 | label << 2 | register << 0,
          String.format ("find_backward_label%d_into_r%d", label, fregister),
          (machine, state) -> {
            MemoryHeap heap = machine.getHeap ();
            for (int i = state.getInstructionPointer (); i >= 0; i--) {
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
