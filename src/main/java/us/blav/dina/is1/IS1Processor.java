package us.blav.dina.is1;

import us.blav.dina.InstructionProcessor;
import us.blav.dina.MemoryHeap;
import us.blav.dina.ProgramState;

public class IS1Processor implements InstructionProcessor {

  public static final int REGISTERS = 4;

  public IS1Processor () {
  }


  @Override
  public ProgramState newProgram (MemoryHeap.Cell cell) {
    return new ProgramState (cell, REGISTERS);
  }

  // 00xx yyzz computes xx - yy and puts result into zz
  // 01xx yyzz computes xx + yy and puts result into zz
  // 1000 xxyy find_fw_labelxx_yy finds label_xx forward and put result into yy or fault if not found
  // 1001 xxyy find_bw_labelxx_yy finds label_xx backward and put result into yy or fault if not found
  // 1010 xxyy read_@xx_to_yy reads value at address xx into yy or fault if out of address space
  // 1011 xxyy write_yy_@xx reads value contained in yy at address xx or fault if out of address space or boundary
  // 1100 xxyy alloc_xx_yy allocates a new cell of size xx and puts address into yy
  // 1101 00xx if_null_xx
  // 1101 01xx if_not_null_xx
  // 1101 10xx goto_forward_xx
  // 1101 11xx goto_backward_xx
  // 1110 00xx labelxx
  // 1110 01xx increment_rxx
  // 1110 10xx decrement_rxx
  // 1111 1111 fork

}
