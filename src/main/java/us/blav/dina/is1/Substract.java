package us.blav.dina.is1;

import static us.blav.dina.is1.Processor.Flags.auto_increment_ip;

public class Substract implements InstructionFactory {

  @Override
  public void register (Processor processor) {
    for (int left = 0; left < 4; left++) {
      for (int right = 0; right < 4; right++) {
        for (int result = 0; result < 4; result++) {
          final int fleft = left;
          final int fright = right;
          final int fresult = result;
          processor.register (
            1 << 6 | left << 4 | right << 2 | result << 0,
            String.format ("substract_r%d_from_r%d_into_r%d", fright, fleft, fresult),
            (machine, state) -> {
              state.set (fresult, state.get (fleft) - state.get (fright));
              state.setInstructionPointer (state.getInstructionPointer () + 1);
            }, auto_increment_ip);
        }
      }
    }
  }
}
