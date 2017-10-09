package us.blav.dina;

import static us.blav.dina.Config.Randomizer.DEFAULT;

public class Main {
  public static void main (String... args) {
    Config config = new Config ()
      .setInstructionSet ("is1")
      .setRandomizer (new Config.Randomizer ()
        .setName (DEFAULT)
        .setSeed (0))
      .setMemory (10000)
      .addBoostrapCode (
        "label0",
        "find_forward_label0_into_r0",
        "find_backward_label0_into_r1",
        "substract_r1_from_r0_into_r2",
        "increment_r0",
        "increment_r2",
        "alloc_r2_bytes_into_r1",
        "add_r2_to_r1_into_r1",
        "label1",
        "decrement_r0",
        "decrement_r1",
        "decrement_r2",
        "read_at_r0_into_r3",
        "write_r3_at_r1",
        "if_r2_is_not_null",
        "go_backward_to_label1",
        "fork",
        "go_backward_to_label0",
        "label0"
      );

    VirtualMachine vm = new VirtualMachine (config);
    vm.start ();
  }
}
