package us.blav.dina.is1;

import static us.blav.dina.is1.Processor.Flags.auto_increment_ip;

public class IfNull implements InstructionFactory {

  @Override
  public void register (Processor processor) {
    for (int value = 0; value < 4; value++) {
      final int fvalue = value;
      processor.register (
        13 << 4 | 0 << 2 | value << 0,
        String.format ("if_r%d_is_null", fvalue),
        (machine, state) -> {
          if (state.get (fvalue) != 0)
            state.incrementIP ();
        }, auto_increment_ip);
    }
  }
}
