package us.actar.dina;

import us.actar.commons.Chain.Filter;

import java.util.List;

public interface Extension {

  interface Action {

    Machine getMachine ();

  }

  interface Kill extends Action {

    int getProgram ();

  }

  interface Execute extends Action {

    Opcode getOpcode ();

    ProgramState getState ();

  }

  interface Launch extends Action {

    ProgramState getProgram ();

    void setLaunchedId (int id);

    int getLaunchedId ();

  }

  interface Reclaim extends Action {

    List<Program> getReclaimList ();

  }

  default Filter<Reclaim> getReclaimFilter () {
    return null;
  }

  default Filter<Execute> getExecuteFilter () {
    return null;
  }

  default Filter<Kill> getKillFilter () {
    return null;
  }

  default Filter<Launch> getLaunchFilter () {
    return null;
  }
}
