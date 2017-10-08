package us.blav.dina;

public interface Factory<T> {

  T create (Config config);

}
