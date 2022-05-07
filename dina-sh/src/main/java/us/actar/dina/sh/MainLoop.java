package us.actar.dina.sh;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;

import us.actar.dina.Machine;

import static us.actar.dina.sh.MainLoop.State.paused;
import static us.actar.dina.sh.MainLoop.State.running;
import static us.actar.dina.sh.MainLoop.State.stopped;

public class MainLoop implements Runnable {

  private final Machine machine;

  private State actualState;

  private final ArrayBlockingQueue<Request> requestQueue;

  interface Request {

    State getRequestedState ();

    void complete ();

  }

  public MainLoop (Machine machine) {
    this.machine = machine;
    this.actualState = paused;
    this.requestQueue = new ArrayBlockingQueue<> (1);
  }

  public State getActualState () {
    return actualState;
  }

  public Machine getMachine () {
    return machine;
  }

  public void requestState (State state) {
    try {
      CountDownLatch latch = new CountDownLatch (1);
      Request request = new Request () {
        @Override
        public State getRequestedState () {
          return state;
        }

        @Override
        public void complete () {
          latch.countDown ();
        }
      };

      requestQueue.put (request);
      latch.await ();
    } catch (InterruptedException e) {
      //
    }
  }

  @Override
  public void run () {
    while (true) {
      Request request;
      try {
        request = actualState == paused ?
          requestQueue.take () :
          requestQueue.poll ();
      } catch (InterruptedException e) {
        return;
      }

      if (request != null) {
        actualState = request.getRequestedState ();
        request.complete ();
      }

      if (actualState == stopped) {
        return;
      } else if (actualState == running) {
        machine.update ();
      }
    }
  }

  public enum State {
    stopped,
    running,
    paused,
  }
}
