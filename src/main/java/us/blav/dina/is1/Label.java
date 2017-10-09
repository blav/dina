package us.blav.dina.is1;

import us.blav.dina.InstructionFactory;
import us.blav.dina.InstructionRegistry;

import static us.blav.dina.InstructionProcessor.Decorator.auto_increment_ip;

public class Label implements InstructionFactory {
  @Override
  public void register (InstructionRegistry registry) {
    for (int label = 0; label < 4; label ++) {
      registry.register (
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
