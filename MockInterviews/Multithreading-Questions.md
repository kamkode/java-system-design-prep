# Java Multithreading - 40+ Tricky Questions

This file contains challenging multithreading questions that assess a candidate's understanding of Java's concurrency model.

## Thread Fundamentals

### 1. What will this code output?
```java
public class ThreadStates {
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        
        System.out.println("Before starting: " + thread.getState());
        thread.start();
        System.out.println("After starting: " + thread.getState());
        Thread.sleep(100);
        System.out.println("After sleep: " + thread.getState());
        thread.join();
        System.out.println("After completion: " + thread.getState());
    }
}
```

**Answer:**
```
Before starting: NEW
After starting: RUNNABLE
After sleep: TIMED_WAITING
After completion: TERMINATED
```

**Explanation:**
- A newly created thread is in the NEW state
- After calling start(), the thread enters the RUNNABLE state
- When the thread executes Thread.sleep(), it enters the TIMED_WAITING state
- After the thread completes execution, it enters the TERMINATED state
- This shows all the major states in a thread's lifecycle

### 2. What happens when you call start() twice?
```java
public class StartTwice {
    public static void main(String[] args) {
        Thread thread = new Thread(() -> {
            System.out.println("Thread running");
        });
        
        thread.start();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        try {
            thread.start(); // Second call to start()
        } catch (Exception e) {
            System.out.println("Exception: " + e.getClass().getSimpleName());
        }
    }
}
```

**Answer:**
```
Thread running
Exception: IllegalThreadStateException
```

**Explanation:**
- A thread can be started only once
- Attempting to start a thread that has already been started throws an IllegalThreadStateException
- This is because once a thread has been started, it can never return to the NEW state

### 3. What's the output of this thread interruption code?
```java
public class InterruptedExample {
    public static void main(String[] args) throws InterruptedException {
        Thread sleeper = new Thread(() -> {
            try {
                System.out.println("Sleeper going to sleep");
                Thread.sleep(5000);
                System.out.println("Sleeper woke up naturally");
            } catch (InterruptedException e) {
                System.out.println("Sleeper was interrupted");
                System.out.println("Interrupted status after catch: " + Thread.currentThread().isInterrupted());
            }
        });
        
        Thread looper = new Thread(() -> {
            System.out.println("Looper starting");
            while (!Thread.currentThread().isInterrupted()) {
                // Just loop
            }
            System.out.println("Looper detected interruption");
            System.out.println("Interrupted status: " + Thread.currentThread().isInterrupted());
        });
        
        sleeper.start();
        looper.start();
        
        Thread.sleep(1000);
        System.out.println("Main thread interrupting both threads");
        sleeper.interrupt();
        looper.interrupt();
        
        sleeper.join();
        looper.join();
    }
}
```

**Answer:**
```
Sleeper going to sleep
Looper starting
Main thread interrupting both threads
Sleeper was interrupted
Interrupted status after catch: false
Looper detected interruption
Interrupted status: true
```

**Explanation:**
- Calling interrupt() on a thread that's blocked in a method like sleep() will throw InterruptedException
- When an InterruptedException is caught, the interrupted status is cleared (set to false)
- For a thread that's running normally, interrupt() sets the interrupted flag but doesn't stop the thread
- The thread needs to check its interrupted status and take appropriate action

### 4. What will happen with these daemon threads?
```java
public class DaemonThreads {
    public static void main(String[] args) {
        Thread daemon = new Thread(() -> {
            try {
                System.out.println("Daemon thread starts");
                Thread.sleep(5000);
                System.out.println("Daemon thread completes");
            } catch (InterruptedException e) {
                System.out.println("Daemon interrupted");
            }
        });
        daemon.setDaemon(true);
        
        Thread nonDaemon = new Thread(() -> {
            try {
                System.out.println("Non-daemon thread starts");
                Thread.sleep(2000);
                System.out.println("Non-daemon thread completes");
            } catch (InterruptedException e) {
                System.out.println("Non-daemon interrupted");
            }
        });
        
        daemon.start();
        nonDaemon.start();
        System.out.println("Main thread finishes");
    }
}
```

**Answer:**
```
Main thread finishes
Daemon thread starts
Non-daemon thread starts
Non-daemon thread completes
```

**Explanation:**
- Daemon threads are terminated when all non-daemon threads have completed
- The main thread finishes quickly but the JVM doesn't exit because the non-daemon thread is still running
- The non-daemon thread runs for 2 seconds and completes normally
- The daemon thread starts but never completes its sleep because the JVM exits after the non-daemon thread completes
- "Daemon thread completes" is never printed

### 5. What happens when these threads modify a shared variable?
```java
public class SharedVariable {
    private static int count = 0;
    
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                count++;
            }
        });
        
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                count++;
            }
        });
        
        t1.start();
        t2.start();
        
        t1.join();
        t2.join();
        
        System.out.println("Final count: " + count);
    }
}
```

**Answer:**
The final count will be less than 20000, varying with each run.

**Explanation:**
- This code has a race condition because the count++ operation is not atomic
- count++ is actually three operations: read count, increment value, write back
- When two threads execute this operation concurrently, one thread might overwrite the other's update
- To fix this, we need synchronization, atomic variables, or locks

### 6. What's the output of this join example?
```java
public class JoinExample {
    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            System.out.println("T1 started");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {}
            System.out.println("T1 completed");
        });
        
        Thread t2 = new Thread(() -> {
            System.out.println("T2 started");
            try {
                t1.join(); // Wait for t1 to complete
                System.out.println("T1 joined");
            } catch (InterruptedException e) {}
            System.out.println("T2 completed");
        });
        
        Thread t3 = new Thread(() -> {
            System.out.println("T3 started");
            try {
                t2.join(); // Wait for t2 to complete
                System.out.println("T2 joined");
            } catch (InterruptedException e) {}
            System.out.println("T3 completed");
        });
        
        t3.start();
        t2.start();
        t1.start();
    }
}
```

**Answer:**
```
T3 started
T2 started
T1 started
T1 completed
T1 joined
T2 completed
T2 joined
T3 completed
```

**Explanation:**
- All three threads start nearly simultaneously, though the exact order may vary
- T1 sleeps for 1 second and then completes
- T2 is waiting for T1 to complete via join(), so it continues after T1 completes
- T3 is waiting for T2 to complete via join(), so it continues after T2 completes
- This creates a dependency chain: T3 → T2 → T1, which is reflected in the completion order

### 7. What will happen with this synchronized method?
```java
public class SynchronizationExample {
    private static int count = 0;
    
    public static synchronized void increment() {
        count++;
    }
    
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                increment();
            }
        });
        
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                increment();
            }
        });
        
        long start = System.currentTimeMillis();
        
        t1.start();
        t2.start();
        
        t1.join();
        t2.join();
        
        long end = System.currentTimeMillis();
        
        System.out.println("Final count: " + count);
        System.out.println("Time taken: " + (end - start) + "ms");
    }
}
```

**Answer:**
```
Final count: 20000
Time taken: [varying, but typically 10-30]ms
```

**Explanation:**
- The synchronized keyword ensures that only one thread can execute the increment() method at a time
- This prevents the race condition from the previous example
- With synchronization, the final count is guaranteed to be 20000
- However, synchronization adds overhead, so this version is slower than an unsynchronized version (but correct)
- The time taken will vary depending on the system, but will be longer than if no synchronization was used

### 8. How does this atomic variable behave?
```java
import java.util.concurrent.atomic.AtomicInteger;

public class AtomicExample {
    private static AtomicInteger count = new AtomicInteger(0);
    
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                count.incrementAndGet();
            }
        });
        
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                count.incrementAndGet();
            }
        });
        
        long start = System.currentTimeMillis();
        
        t1.start();
        t2.start();
        
        t1.join();
        t2.join();
        
        long end = System.currentTimeMillis();
        
        System.out.println("Final count: " + count.get());
        System.out.println("Time taken: " + (end - start) + "ms");
    }
}
```

**Answer:**
```
Final count: 20000
Time taken: [varying, but typically 5-15]ms
```

**Explanation:**
- AtomicInteger provides atomic operations like incrementAndGet() which are thread-safe without explicit synchronization
- This eliminates the race condition from the first example
- AtomicInteger is generally faster than using synchronized methods because it uses lower-level CPU operations (compare-and-swap)
- The final count is guaranteed to be 20000, and the time taken is typically less than the synchronized version

### 9. What happens with this volatile variable?
```java
public class VolatileExample {
    private static volatile boolean flag = false;
    private static int counter = 0;
    
    public static void main(String[] args) throws InterruptedException {
        Thread writer = new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {}
            
            counter = 42;
            flag = true;
            System.out.println("Writer thread: values updated");
        });
        
        Thread reader = new Thread(() -> {
            while (!flag) {
                // Wait for flag to become true
            }
            System.out.println("Reader thread: flag is true, counter = " + counter);
        });
        
        reader.start();
        writer.start();
        
        reader.join();
        writer.join();
    }
}
```

**Answer:**
```
Writer thread: values updated
Reader thread: flag is true, counter = 42
```

**Explanation:**
- volatile ensures that reads and writes to the variable are visible to all threads
- The reader thread spins until flag becomes true
- When the writer thread sets flag to true, the reader thread sees the update immediately
- volatile also creates a happens-before relationship, ensuring that the write to counter is visible after flag is set
- Without volatile, the reader might see flag as true but counter as 0 due to reordering or caching

### 10. What's the output of this ThreadLocal example?
```java
public class ThreadLocalExample {
    private static ThreadLocal<Integer> threadLocal = ThreadLocal.withInitial(() -> 0);
    
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            System.out.println("T1 initial value: " + threadLocal.get());
            threadLocal.set(1);
            System.out.println("T1 after set: " + threadLocal.get());
        });
        
        Thread t2 = new Thread(() -> {
            System.out.println("T2 initial value: " + threadLocal.get());
            threadLocal.set(2);
            System.out.println("T2 after set: " + threadLocal.get());
        });
        
        t1.start();
        t2.start();
        
        t1.join();
        t2.join();
        
        System.out.println("Main thread value: " + threadLocal.get());
    }
}
```

**Answer:**
```
T1 initial value: 0
T2 initial value: 0
T1 after set: 1
T2 after set: 2
Main thread value: 0
```

**Explanation:**
- ThreadLocal provides thread-isolated variables where each thread has its own copy
- Each thread starts with the initial value 0 as specified in withInitial()
- When t1 sets the value to 1, it only affects t1's copy
- When t2 sets the value to 2, it only affects t2's copy
- The main thread's copy remains at 0 since it never modified its copy
- ThreadLocal is useful for thread-specific context or state that shouldn't be shared
