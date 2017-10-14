package us.blav.dina;

public interface Factory<T> {

  T create (VirtualMachine machine);

}
