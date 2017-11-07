package us.actar.dina.is.is2;

import us.actar.dina.Fault;
import us.actar.dina.Heap;
import us.actar.dina.InstructionSet;
import us.actar.dina.randomizers.RegisterRandomizer;

import static us.actar.dina.is.is2.IS2Randomizers.FIND;

public class FindForward extends Base {

  private final int label;

  public FindForward (int label) {
    super (FIND);
    this.label = label;
  }

  @Override
  public void register (InstructionSet registry) {
    int opcode = Label.getOpcode (registry, label);
    registry.register (
      String.format ("find_forward_label%d", label),
      (machine, state) -> {
        Heap heap = machine.getHeap ();
        for (int i = state.getInstructionPointer (); i < heap.size (); i++) {
          if (opcode == heap.get (i)) {
            getRegisters (state).push (i, machine.getRandomizer (randomizer));
            return;
          }
        }

        throw new Fault ();
      });
  }
}
