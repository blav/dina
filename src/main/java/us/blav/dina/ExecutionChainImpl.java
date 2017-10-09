package us.blav.dina;

import us.blav.dina.InstructionRegistry.RegisteredInstruction;

import java.util.ArrayList;
import java.util.List;

public class ExecutionChainImpl implements ExecutionChain {

  private final VirtualMachine machine;

  private final List<ExecutionFilter> filters;

  private int current;

  interface Handle {

    void uninstall ();

  }

  public ExecutionChainImpl (VirtualMachine machine) {
    this.machine = machine;
    this.filters = new ArrayList<> ();
    this.filters.add ((chain, machin, state, instruction) -> {
      try {
        instruction.getInstruction ().process (machin, state);
      } catch (Fault fault) {
        machin.getFaultHandler ().handleFault (fault, state);
      }
    });
  }

  public Handle install (ExecutionFilter filter) {
    if (filter == null)
      throw new NullPointerException ();

    // wrap to prevent Handler.uninstall() failure when same filter is inserted multiple times
    ExecutionFilter f = (chain, machine, state, instruction) -> filter.next (chain, machine, state, instruction);
    this.filters.add (this.filters.size () - 1, f);
    return () -> {
      this.filters.remove (f);
    };
  }

  @Override
  public void next (VirtualMachine machine, ProgramState state, RegisteredInstruction instruction) {
    if (this.current == filters.size ()) {
      this.current = 0;
      return;
    } else {
      this.filters.get (current ++).next (this, machine, state, instruction);
    }
  }
}
