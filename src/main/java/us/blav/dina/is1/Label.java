package us.blav.dina.is1;

import static us.blav.dina.is1.Processor.Flags.auto_increment_ip;

public class Label implements InstructionFactory {
  @Override
  public void register (Processor processor) {
    for (int label = 0; label < 4; label ++) {
      processor.register (
        getOpcode (label),
        String.format ("label%d", label),
        (machine, state) -> {},
        auto_increment_ip);
    }
  }

  public static int getOpcode (int label) {
    return 14 << 4 | 0 << 2 | label;
  }
}
