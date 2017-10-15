package us.blav.dina.is.lib;

import us.blav.dina.Fault;
import us.blav.dina.InstructionRegistry;
import us.blav.dina.MemoryHeap;
import us.blav.dina.randomizers.RegisterRandomizer;

public class FindForward extends Base {

  private final int label;

  private final int register;

  public FindForward (RegisterRandomizer.Name randomizer, int label, int register) {
    super (randomizer);
    this.label = label;
    this.register = register;
  }

  @Override
  public void register (InstructionRegistry registry) {
    int opcode = Label.getOpcode (registry, label);
    registry.register (
      String.format ("find_forward_label%d_into_r%d", label, register),
      (machine, state) -> {
        MemoryHeap heap = machine.getHeap ();
        for (int i = state.getInstructionPointer (); i < heap.size (); i++) {
          if (opcode == heap.get (i)) {
            state.set (register, i, machine.getRandomizer (randomizer));
            return;
          }
        }

        throw new Fault ();
      });
  }
}
