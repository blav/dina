package us.actar.dina;

import us.actar.commons.Chain.Filter;

public interface MachineFilters {
  default Filter<Reclaim> getReclaimFilter () {
    return null;
  }

  default Filter<ExecutionStep> getExecutionFilter () {
    return null;
  }
}
