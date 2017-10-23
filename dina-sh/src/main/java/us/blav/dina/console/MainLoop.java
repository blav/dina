package us.blav.dina.console;

import us.blav.dina.VirtualMachine;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import static us.blav.dina.console.MainLoop.State.paused;
import static us.blav.dina.console.MainLoop.State.running;
import static us.blav.dina.console.MainLoop.State.stopped;

public class MainLoop implements Runnable {

  public enum State {
    stopped,
    running,
    paused,
  }

  private State actualState;

  private State requestedState;

  private final ReentrantLock lock;

  private final Condition requested;

  private final Condition actualized;

  private final VirtualMachine machine;

  public MainLoop (VirtualMachine machine) {
    this.machine = machine;
    this.lock = new ReentrantLock ();
    this.requested = this.lock.newCondition ();
    this.actualized = this.lock.newCondition ();
    this.actualState = running;
    this.requestedState = running;
  }

  public State getActualState () {
    return actualState;
  }

  public State getRequestedState () {
    return requestedState;
  }

  public VirtualMachine getMachine () {
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
}
