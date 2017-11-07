package us.actar.dina;

public class Fault extends Exception {

  public Fault () {
  }

  public Fault (Throwable x) {
    super (x);
  }
}
