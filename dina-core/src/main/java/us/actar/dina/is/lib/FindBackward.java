package us.actar.dina.is.lib;

import us.actar.dina.Fault;
import us.actar.dina.Heap;
import us.actar.dina.InstructionRegistry;
import us.actar.dina.randomizers.RegisterRandomizer;

public class FindBackward extends Base {

  private final int label;

  private final int register;

  public FindBackward (RegisterRandomizer.Name randomizer, int label, int register) {
    super (randomizer);
    this.label = label;
    this.register = register;
  }

  @Override
  public void register (InstructionRegistry registry) {
    int opcode = Label.getOpcode (registry, label);
    registry.register (
      String.format ("find_backward_label%d_into_r%d", label, register),
      (machine, state) -> {
        Heap heap = machine.getHeap ();
        for (int i = state.getInstructionPointer (); i >= 0; i--) {
          if (opcode == heap.get (i)) {
            state.set (register, i, machine.getRandomizer (randomizer));
            return;
          }
        }

        throw new Fault ();
      });
  }
}
