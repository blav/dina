package us.blav.dina;

public class Main {
  public static void main (String... args) {
    Config config = new Config ()
      .setInstructionSet ("is1")
      .setRandomizer ("default")
      .setMemory (10000)
      .addBoostrapCode (
//        "label0",
//        "find_backward_label0_into_r0",
//        "find_forward_label0_into_r1",
//        "substract_r0_from_r1_into_r2",
//        "alloc_r2_bytes_into_r1",
//        "label1",
//        "read_at_r%d_into_r%d",
//        "",
//        "",
//        "",
        "label0",
        "if_r0_is_null",
        "go_backward_to_label0"
      );

    VirtualMachine vm = new VirtualMachine (config);
    vm.start ();
  }
}
