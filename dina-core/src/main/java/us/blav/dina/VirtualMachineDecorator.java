package us.blav.dina;

import com.google.inject.TypeLiteral;
import us.blav.commons.Chain;
import us.blav.commons.Chain.Filter;
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
  public void kill (int pid) {
    machine.kill (pid);
  }

  @Override
  public ProgramState getProgram (int pid) {
    return machine.getProgram (pid);
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

  @Override
  public void update () {
    machine.update ();
  }

  @Override
  public <TYPE> Chain.Handle install (TypeLiteral<Filter<TYPE>> type, Filter<TYPE> filter) {
    return machine.install (type, filter);
  }

  @Override
  public InstructionRegistry getRegistry () {
    return machine.getRegistry ();
  }
}
