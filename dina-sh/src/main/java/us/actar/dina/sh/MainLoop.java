package us.actar.dina.sh;

import us.actar.dina.Machine;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import static us.actar.dina.sh.MainLoop.State.paused;
import static us.actar.dina.sh.MainLoop.State.stopped;

public class MainLoop implements Runnable {

  private final ReentrantLock lock;

  private final Condition requested;

  private final Condition actualized;

  private final Machine machine;

  private State actualState;

  private State requestedState;

  public MainLoop (Machine machine) {
    this.machine = machine;
    this.lock = new ReentrantLock ();
    this.requested = this.lock.newCondition ();
    this.actualized = this.lock.newCondition ();
    this.actualState = stopped;
    this.requestedState = paused;
  }

  public State getActualState () {
    return actualState;
  }

  public State getRequestedState () {
    return requestedState;
  }

  public Machine getMachine () {
    return machine;
  }

  public void requestState (State state) {
    this.lock.lock ();
    try {
      while (state != this.requestedState) {
        this.requestedState = state;
        this.requested.signalAll ();
        this.actualized.await ();
      }
    } catch (InterruptedException e) {
    } finally {
      this.lock.unlock ();
    }
  }

  @Override
  public void run () {
    while (true) {
      lock.lock ();
      try {
        while (actualState != requestedState) {
          actualState = requestedState;
          actualized.signalAll ();
        }

        if (actualState == stopped)
          return;

        if (actualState == paused) {
          requested.await ();
          continue;
        }

        machine.update ();
      } catch (InterruptedException e) {
      } finally {
        lock.unlock ();
      }
    }
  }

  public enum State {
    stopped,
    running,
    paused,
  }
}
