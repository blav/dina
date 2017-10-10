package us.blav.dina.is.is1;

import us.blav.dina.Fault;
import us.blav.dina.InstructionFactory;
import us.blav.dina.InstructionRegistry;
import us.blav.dina.MemoryHeap;

import static us.blav.dina.InstructionProcessor.Decorator.auto_increment_ip;
import static us.blav.dina.is.is1.IS1Randomizers.GOTO;

public class GotoBackward implements InstructionFactory {

  @Override
  public void register (InstructionRegistry registry) {
    for (int label = 0; label < 4; label++) {
      final int opcode = Label.getOpcode (label);
      final int flabel = label;
      registry.register (
        13 << 4 | 3 << 2 | label << 0,
        String.format ("go_backward_to_label%d", flabel),
        (machine, state) -> {
          MemoryHeap heap = machine.getHeap ();
          for (int offset = state.getInstructionPointer (); offset >= 0; offset--) {
            if (heap.get (offset) == opcode) {
              state.setInstructionPointer (offset, machine.getRandomizer (GOTO));
              return;
            }
          }

          throw new Fault ();
        }, auto_increment_ip);
    }
  }
}
