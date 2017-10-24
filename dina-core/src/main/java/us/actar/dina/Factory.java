package us.actar.dina;

public interface Factory<T> {

  T create (Machine machine);

}
