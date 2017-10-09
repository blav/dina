package us.blav.dina.is1;

import us.blav.dina.InstructionFactory;
import us.blav.dina.InstructionRegistry;
import us.blav.dina.VirtualMachine;

import static us.blav.dina.InstructionProcessor.Decorator.auto_increment_ip;

public class Increment implements InstructionFactory {
  @Override
  public void register (InstructionRegistry registry) {
    for (int register = 0; register < 4; register ++) {
      final int fregister = register;
      registry.register (
        14 << 4 | 1 << 2 | register,
        String.format ("increment_r%d", register),
        (machine, state) -> {
          state.set (fregister, state.get (fregister) + 1);
        },
        auto_increment_ip);
    }
  }
}
