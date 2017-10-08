package us.blav.dina.is1;

import static us.blav.dina.is1.Processor.Flags.auto_increment_ip;

public class Increment implements InstructionFactory {
  @Override
  public void register (Processor processor) {
    for (int register = 0; register < 4; register ++) {
      final int fregister = register;
      processor.register (
        14 << 4 | 1 << 2 | register,
        String.format ("increment_r%d", register),
        (machine, state) -> {
          state.set (fregister, state.get (fregister) + 1);
        },
        auto_increment_ip);
    }
  }
}
