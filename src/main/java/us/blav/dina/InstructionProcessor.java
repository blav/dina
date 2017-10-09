package us.blav.dina;

public interface InstructionProcessor {

  ProgramState newProgram (MemoryHeap.Cell cell);

  default Instruction newUnknownIntruction (int opcode) {
    return (machine, state) -> {
    };
  }

  enum Decorator {

    auto_increment_ip {
      @Override
      public Instruction decorate (Instruction instruction) {
        return new AutoIncrementIPDecorator (instruction);
      }
    },;

    public abstract Instruction decorate (Instruction instruction);

  }
}
