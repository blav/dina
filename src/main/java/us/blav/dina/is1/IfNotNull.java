package us.blav.dina.is1;

import us.blav.dina.InstructionFactory;
import us.blav.dina.InstructionRegistry;
import us.blav.dina.VirtualMachine;

import static us.blav.dina.InstructionProcessor.Decorator.auto_increment_ip;

public class IfNotNull implements InstructionFactory {

  @Override
  public void register (InstructionRegistry registry) {
    for (int value = 0; value < 4; value++) {
      final int fvalue = value;
      registry.register (
        13 << 4 | 1 << 2 | value << 0,
        String.format ("if_r%d_is_not_null", fvalue),
        (machine, state) -> {
          if (state.get (fvalue) == 0)
            state.incrementIP ();
        }, auto_increment_ip);
    }
  }
}
