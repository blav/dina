package us.actar.dina;

import us.actar.commons.Chain.Filter;

import java.util.List;

public interface Extension {

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

  interface Action {

    Machine getMachine ();

  }

  interface Kill extends Action {

    int getProgram ();

  }

  interface Execute extends Action {

    Opcode getOpcode ();

    Program getState ();

  }

  interface Launch extends Action {

    Program getProgram ();

    int getLaunchedId ();

    void setLaunchedId (int id);

  }

  interface Reclaim extends Action {

    List<Program> getReclaimList ();

  }
}
