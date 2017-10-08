package us.blav.dina.is1;

import static us.blav.dina.is1.Processor.Flags.auto_increment_ip;

public class Add implements InstructionFactory {

  @Override
  public void register (Processor processor) {
    for (int left = 0; left < 4; left++) {
      for (int right = 0; right < 4; right++) {
        for (int result = 0; result < 4; result++) {
          final int fleft = left;
          final int fright = right;
          final int fresult = result;
          processor.register (
            0 << 6 | left << 4 | right << 2 | result << 0,
            String.format ("add_r%d_to_r%d_into_r%d", fleft, fright, fresult),
            (machine, state) -> {
              state.set (fresult, state.get (fleft) + state.get (fright));
            }, auto_increment_ip);
        }
      }
    }
  }
}
