package us.blav.dina;

import static us.blav.dina.Injection.getInjector;

public class Main {
  public static void main (String... args) {
    VirtualMachine vm = getInjector ().getInstance (VirtualMachine.class);
    vm.start ();
  }
}
