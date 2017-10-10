package us.blav.dina.is.is1;

import us.blav.dina.InstructionFactory;
import us.blav.dina.InstructionRegistry;

import static us.blav.dina.InstructionProcessor.Decorator.auto_increment_ip;
import static us.blav.dina.RegisterRandomizer.NOP;
import static us.blav.dina.is.is1.IS1Randomizers.DECREMENT;

public class Decrement implements InstructionFactory {

  @Override
  public void register (InstructionRegistry registry) {
    for (int register = 0; register < 4; register ++) {
      final int fregister = register;
      registry.register (
        14 << 4 | 2 << 2 | register,
        String.format ("decrement_r%d", register),
        (machine, state) -> {
          state.set (fregister, state.get (fregister, NOP) - 1, machine.getRandomizer (DECREMENT));
        },
        auto_increment_ip);
    }
  }
}
