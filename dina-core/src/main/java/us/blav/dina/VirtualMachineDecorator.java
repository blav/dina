package us.blav.dina;

import us.blav.dina.randomizers.RegisterRandomizer;

import java.util.Collection;

public class VirtualMachineDecorator implements VirtualMachine {

  private final VirtualMachine machine;

  public VirtualMachineDecorator (VirtualMachine machine) {
    this.machine = machine;
  }

  @Override
  public MemoryHeap getHeap () {
    return machine.getHeap ();
  }

  @Override
  public Randomizer getRandomizer () {
    return machine.getRandomizer ();
  }

  @Override
  public long launch (ProgramState state) {
    return machine.launch (state);
  }

  @Override
  public void kill (long pid) {
    machine.kill (pid);
  }

  @Override
  public Collection<Program> getPrograms () {
    return machine.getPrograms ();
  }

  @Override
  public RegisterRandomizer<?> getRandomizer (RegisterRandomizer.Name name) {
    return machine.getRandomizer (name);
  }

  @Override
  public Config getConfig () {
    return machine.getConfig ();
  }
}
