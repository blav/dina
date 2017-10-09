package us.blav.dina;

public interface FaultHandler {

  void handleFault (Fault fault, ProgramState state);

}
