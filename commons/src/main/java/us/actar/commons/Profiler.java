//File              $Source: /var/lib/cvs2/o3-core/src/com/one2team/server/profiler/Profiler.java,v $
//Last modified by  $Author: dbr $
//Revision Date     $Revision: 1.7 $
//Tag Name          $Name:  $
//
//Copyright (c) 2000 One2team All Rights Reserved.

package us.actar.commons;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.IntSupplier;

import static java.lang.System.nanoTime;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.TimeUnit.NANOSECONDS;


public class Profiler {

  public class FakeLapse extends Lapse {
    @Override
    public void close () {
    }
  }

  public interface Metric {

    interface Measure {

      long getValue (Thread thread);

    }

    Measure measure ();

    TimeUnit getUnit ();

  }

  public static class Nanos implements Metric {

    @Override
    public Measure measure () {
      return thread -> nanoTime ();
    }

    @Override
    public TimeUnit getUnit () {
      return NANOSECONDS;
    }
  }

  public Profiler () {
    this (null);
  }

  public Lapse profile (Integer timerId) {
    if (timerId == null)
      return this.fakeLapse;

    start (timerId);
    return this.lapse;
  }
  
  public Profiler (Metric metric) {
    this.registered = Collections.synchronizedMap (new WeakHashMap<Thread, ThreadProfiler> ());
    this.metric = metric != null ? metric : new Nanos ();
    this.globalTimers = new HashMap<> ();
    this.globalTimerIds = new HashMap<> ();
    this.lapse = new Lapse ();
    this.fakeLapse = new FakeLapse ();
    this.profilers = ThreadLocal.withInitial (() -> new ThreadProfiler ());
  }
  
  public int addTimer (String name) {
    return addTimer (name, () -> this.generator++);
  }

  public int addTimer (String name, IntSupplier generator) {
    name = strip (name, 0);
    Integer id = this.globalTimerIds.get (name);
    if (id != null)
      return id;

    synchronized (this) {
      id = this.globalTimerIds.get (name);
      if (id != null)
        return id;

      Map<Integer, GlobalTimer> gtm = new HashMap<> (this.globalTimers);
      Map<String, Integer> gti = new HashMap<> (this.globalTimerIds);
      GlobalTimer gt = new GlobalTimer (name, generator);
      id = gt.id;
      gtm.put (id, gt);
      gti.put (name, id);
      this.globalTimers = gtm;
      this.globalTimerIds = gti;
      return id;
    }
  }

  public void stopStart (int timerId) {
    stop ();
    start (timerId);
  }
  
  public void start (int timerId) {
    this.profilers.get ().start (timerId, metric.measure ());
  }
  
  public void stop () {
    this.profilers.get ().stop (metric.measure ());
  }
  
  public void lap () {
    Metric.Measure m = metric.measure ();
    for (ThreadProfiler p : new HashSet<> (registered.values ()))
      p.lap (m);
  }
  
  public Map<String, Long> getElapsedTimes (TimeUnit unit) {
    Map<String,Long> m = new HashMap<> ();
    for (GlobalTimer t : globalTimers.values ())
      m.put (t.name, unit.convert (t.elapsed.get (), metric.getUnit ()));
    
    return m;
  }

  public static class Summary {
    public Summary (long count, long elapsed) {
      this.count = count;
      this.elapsed = elapsed;
    }

    public long getCount () {
      return count;
    }

    public long getElapsed () {
      return elapsed;
    }

    private final long count;

    private final long elapsed;

  }

  public Map<String, Summary> getSummary (TimeUnit unit) {
    Map<String, Summary> m = new HashMap<> ();
    for (GlobalTimer t : globalTimers.values ())
      m.put (t.name,
        new Summary (t.count.get (), unit.convert (t.elapsed.get (), metric.getUnit ())));

    return m;
  }

  public void reset () {
    for (GlobalTimer t : globalTimers.values ()) {
      t.elapsed.set (0);
      t.count.set (0);
    }
  }

  public void setDefaultTimer (int timerId) {
    this.defaultTimer = ofNullable (globalTimers.get (timerId))
      .orElseThrow (NoSuchElementException::new);
  }

  private class ThreadProfiler {
    
    public ThreadProfiler () {
      this.thread = Thread.currentThread ();
      Profiler.this.registered.put (thread, this);
      this.timers = new HashMap<> ();
      for (GlobalTimer e : globalTimers.values ())
        this.timers.put (e.id, new ThreadTimer (e));
    }
    
    private void start (int timerId, Metric.Measure measure) {
      ThreadTimer t = getThreadTimer (timerId);
      long currentValue = measure.getValue (thread);
      StackFrame f = null;
      if (pool != null) {
        f = pool;
        pool = f.next;
      } else {
        f = new StackFrame ();
      }
      
      f.init (head, t).start (currentValue);
      if (head != null)
        head.stop (currentValue, 0);
      
      head = f;
    }

    private void lap (Metric.Measure measure) {
      long currentValue = measure.getValue (thread);
      Map<ThreadTimer, Object> done = new IdentityHashMap<> ();
      for (StackFrame cur = head; cur != null; cur = cur.next) {
        ThreadTimer t = cur.timer;
        if (t != null && done.put (t, this) == null)
          t.lap (currentValue);
      }
    }

    private void stop (Metric.Measure measure) {
      long currentValue = measure.getValue (thread);
      if (head == null)
        throw new RuntimeException ("profiler is not started");
      
      StackFrame n = head.next;
      if (n != null)
        n.start (currentValue);
      
      head.stop (currentValue, 1);
      head.init (pool, null);
      pool = head;
      head = n;
    }
    
    private ThreadTimer getThreadTimer (int timerId) {
      ThreadTimer t = timers.get (timerId);
      if (t != null)
        return t;
      
      synchronized (this) {
        t = timers.get (timerId);
        if (t != null)
          return t;
        
        GlobalTimer gt = globalTimers.get (timerId);
        if (gt == null)
          gt = defaultTimer;

        if (gt == null)
          throw new RuntimeException ("no such timer " + timerId);
        
        Map<Integer,ThreadTimer> tm = new HashMap<> (timers);
        tm.put (timerId, t = new ThreadTimer (gt));
        timers.put (timerId, t);
        return t;
      }
    }

    private StackFrame head;
    
    private StackFrame pool;
    
    private final Map<Integer, ThreadTimer> timers;

    private Thread thread;
    
  }
  
  private class ThreadTimer {

    public ThreadTimer (GlobalTimer globalTimer) {
      this.globalTimer = globalTimer;
      this.startedTime = new AtomicLong (0);
      this.startCount = 0;
    }
    
    private void start (long currentValue) {
      if (++ startCount == 1)
        startedTime.set (currentValue);
    }
    
    private void stop (long currentValue, long count) {
      if (-- startCount == 0) {
        globalTimer.elapsed.addAndGet (currentValue - startedTime.getAndSet (0));
        globalTimer.count.addAndGet (count);
      }
    }
    
    private void lap (long currentValue) {
      for (int i = 0; i < 50; i ++) {
        long st = startedTime.get ();
        if (st == 0)
          return;
        
        if (startedTime.compareAndSet (st, currentValue)) {
          globalTimer.elapsed.addAndGet (currentValue - st);
          return;
        }
        
        try {
          Thread.sleep (0, 5);
        } catch (Exception x) {
        }
      }
    }
    
    private final GlobalTimer globalTimer;
    
    private int startCount;
    
    private final AtomicLong startedTime;
    
  }
  
  private class StackFrame {
    
    public StackFrame init (StackFrame next, ThreadTimer timer) {
      this.next = next;
      this.timer = timer;
      return this;
    }
    
    public StackFrame start (long currentValue) {
      this.timer.start (currentValue);
      return this;
    }
    
    public StackFrame stop (long currentValue, long count) {
      this.timer.stop (currentValue, count);
      return this;
    }
    
    private ThreadTimer timer;
    
    private StackFrame next;
    
  }
  
  private class GlobalTimer {
    
    public GlobalTimer (String name, IntSupplier generator) {
      this.name = name;
      this.elapsed = new AtomicLong ();
      this.count = new AtomicLong ();
      this.id = generator.getAsInt ();
    }

    private final String name;
    
    private final Integer id;

    private final AtomicLong elapsed;

    private final AtomicLong count;

  }
  
  private String strip (String name, int level) {
    if (level <= 0)
      return name;
    
    String [] split = name.split (".");
    if (split.length < level)
      return name;
    
    StringBuilder b = new StringBuilder (split [0]);
    for (int i = 1; i < level; i ++)
      b.append ('.').append (split [i]);
    
    return b.toString ();
  }

  private final Map<Thread, ThreadProfiler> registered;
  
  private int generator;
  
  private final Metric metric;

  private final ThreadLocal<ThreadProfiler> profilers;
  
  private Map<Integer, GlobalTimer> globalTimers;

  private GlobalTimer defaultTimer;
  
  private Map<String, Integer> globalTimerIds;

  private final Lapse lapse;

  private final Lapse fakeLapse;

  public class Lapse implements AutoCloseable {
    @Override
    public void close () {
      stop ();
    }
  }
}
