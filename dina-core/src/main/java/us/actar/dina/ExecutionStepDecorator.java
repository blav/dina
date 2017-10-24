package us.actar.dina;

public class ExecutionStepDecorator implements ExecutionStep {

  private final ExecutionStep step;

  public ExecutionStepDecorator (ExecutionStep step) {
    this.step = step;
  }

  @Override
  public Opcode getOpcode () {
    return step.getOpcode ();
  }

  @Override
  public ProgramState getState () {
    return step.getState ();
  }

  @Override
  public Machine getMachine () {
    return step.getMachine ();
  }
}
