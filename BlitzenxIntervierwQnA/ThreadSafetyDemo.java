package BlitzenxIntervierwQnA;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/*
 * PROBLEM STATEMENT:
 * Write a Java program to demonstrate how to make code thread-safe using various synchronization
 * techniques. The program should illustrate common thread safety issues and their solutions.
 * 
 * Requirements:
 * - Demonstrate multiple thread synchronization techniques (synchronized methods/blocks, locks, etc.)
 * - Illustrate common thread safety issues (race conditions, visibility, etc.)
 * - Show proper use of thread-safe collections
 * - Implement atomic operations for safe counter implementation
 * - Demonstrate deadlock prevention techniques
 * - Use thread pools and executors for proper thread management
 * - Show comparison between non-thread-safe and thread-safe implementations
 * - Implement proper resource cleanup with thread termination
 * - Include timeouts to prevent indefinite waiting
 * - Provide a clear explanation of each synchronization approach
 *
 * EXPLANATION:
 * Class: ThreadSafetyDemo
 * Purpose: Demonstrates various techniques to make code thread-safe in Java.
 *
 * CONCEPTS INVOLVED:
 * 1. Concurrency Issues:
 *    - Race conditions: When multiple threads access shared data and try to change it simultaneously
 *    - Visibility problems: When changes made by one thread may not be visible to others
 *    - Atomicity violations: When operations that should be atomic are interrupted
 *    - Deadlocks: When two or more threads are blocked forever, waiting for each other
 *
 * 2. Synchronization Techniques:
 *    - synchronized methods and blocks: Intrinsic locking using the monitor lock of an object
 *    - Explicit locks: More flexible locking using Lock interface (ReentrantLock)
 *    - Read-write locks: Allow multiple readers but exclusive writers
 *    - Atomic variables: Classes that support atomic operations without locking
 *    - Volatile keyword: Ensures visibility of changes across threads
 *    - Immutable objects: Inherently thread-safe because they cannot be changed
 *
 * 3. Thread-Safe Collections:
 *    - Synchronized collections: Using Collections.synchronizedXxx wrappers
 *    - Concurrent collections: ConcurrentHashMap, CopyOnWriteArrayList, etc.
 *    - Thread-safe design patterns for collections
 *
 * 4. Thread Management:
 *    - Thread pools using ExecutorService
 *    - Proper thread termination and resource cleanup
 *    - Thread coordination with CountDownLatch, CyclicBarrier, Phaser
 *
 * IMPLEMENTATION DETAILS:
 * - The program demonstrates several counter implementations with varying thread safety:
 *   1. UnsafeCounter: No synchronization, prone to race conditions
 *   2. SynchronizedCounter: Uses synchronized methods for thread safety
 *   3. LockBasedCounter: Uses explicit ReentrantLock for synchronization
 *   4. ReadWriteLockCounter: Uses ReadWriteLock for better read performance
 *   5. AtomicCounter: Uses AtomicInteger for lock-free thread safety
 *   6. VolatileCounter: Uses volatile for visibility (but not atomicity)
 *
 * - The program also demonstrates thread-safe collection usage:
 *   1. Regular HashMap vs. ConcurrentHashMap
 *   2. ArrayList vs. synchronized List
 *
 * - Additional demonstrations include:
 *   1. Deadlock prevention techniques
 *   2. Proper resource management with ExecutorService
 *   3. Performance comparison of different synchronization approaches
 */

// Base interface for all counter implementations
interface Counter {
    void increment();
    int getValue();
    String getName();
}

// Counter implementation with no thread safety measures
class UnsafeCounter implements Counter {
    private int count = 0;
    
    @Override
    public void increment() {
        count++; // Not thread-safe: read-modify-write operation is not atomic
    }
    
    @Override
    public int getValue() {
        return count;
    }
    
    @Override
    public String getName() {
        return "UnsafeCounter";
    }
}

// Counter implementation using synchronized methods
class SynchronizedCounter implements Counter {
    private int count = 0;
    
    @Override
    public synchronized void increment() {
        count++; // Thread-safe: synchronized method ensures atomicity
    }
    
    @Override
    public synchronized int getValue() {
        return count;
    }
    
    @Override
    public String getName() {
        return "SynchronizedCounter";
    }
}

// Counter implementation using explicit lock
class LockBasedCounter implements Counter {
    private int count = 0;
    private final Lock lock = new ReentrantLock();
    
    @Override
    public void increment() {
        lock.lock();
        try {
            count++; // Thread-safe: explicit lock ensures atomicity
        } finally {
            lock.unlock(); // Always release lock in finally block
        }
    }
    
    @Override
    public int getValue() {
        lock.lock();
        try {
            return count;
        } finally {
            lock.unlock();
        }
    }
    
    @Override
    public String getName() {
        return "LockBasedCounter";
    }
}

// Counter implementation using read-write lock for better read performance
class ReadWriteLockCounter implements Counter {
    private int count = 0;
    private final ReadWriteLock rwLock = new ReentrantReadWriteLock();
    private final Lock readLock = rwLock.readLock();
    private final Lock writeLock = rwLock.writeLock();
    
    @Override
    public void increment() {
        writeLock.lock();
        try {
            count++; // Thread-safe: write lock ensures exclusive access
        } finally {
            writeLock.unlock();
        }
    }
    
    @Override
    public int getValue() {
        readLock.lock();
        try {
            return count; // Multiple threads can read simultaneously
        } finally {
            readLock.unlock();
        }
    }
    
    @Override
    public String getName() {
        return "ReadWriteLockCounter";
    }
}

// Counter implementation using AtomicInteger
class AtomicCounter implements Counter {
    private final AtomicInteger count = new AtomicInteger(0);
    
    @Override
    public void increment() {
        count.incrementAndGet(); // Thread-safe: atomic operation
    }
    
    @Override
    public int getValue() {
        return count.get();
    }
    
    @Override
    public String getName() {
        return "AtomicCounter";
    }
}

// Counter using volatile (note: increment is still not atomic)
class VolatileCounter implements Counter {
    private volatile int count = 0;
    
    @Override
    public void increment() {
        count++; // NOT thread-safe for increment, but changes are visible to all threads
    }
    
    @Override
    public int getValue() {
        return count; // Guaranteed to see the latest value
    }
    
    @Override
    public String getName() {
        return "VolatileCounter";
    }
}

// Main class to demonstrate thread safety
public class ThreadSafetyDemo {
    
    // Number of threads to use in tests
    private static final int NUM_THREADS = 10;
    
    // Number of increments per thread
    private static final int INCREMENTS_PER_THREAD = 100000;
    
    public static void main(String[] args) {
        ThreadSafetyDemo demo = new ThreadSafetyDemo();
        Scanner scanner = new Scanner(System.in);
        
        try {
            System.out.println("=== Thread Safety Demonstration ===");
            System.out.println("This program shows various techniques to make code thread-safe.");
            
            // Counter demonstrations
            System.out.println("\n--- Counter Implementations ---");
            
            List<Counter> counters = new ArrayList<>();
            counters.add(new UnsafeCounter());
            counters.add(new SynchronizedCounter());
            counters.add(new LockBasedCounter());
            counters.add(new ReadWriteLockCounter());
            counters.add(new AtomicCounter());
            counters.add(new VolatileCounter());
            
            System.out.println("Testing counter implementations with " + NUM_THREADS + 
                              " threads and " + INCREMENTS_PER_THREAD + " increments per thread");
            System.out.println("Expected final count: " + (NUM_THREADS * INCREMENTS_PER_THREAD));
            
            for (Counter counter : counters) {
                demo.testCounter(counter);
            }
            
            // Choose a demonstration to run
            System.out.println("\nChoose a specific thread safety demonstration:");
            System.out.println("1. Thread-safe collections");
            System.out.println("2. Deadlock prevention");
            System.out.println("3. Run all demonstrations");
            System.out.print("Enter your choice (1-3): ");
            
            int choice = 3; // Default to all
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
            }
            scanner.nextLine(); // Consume newline
            
            if (choice == 1 || choice == 3) {
                // Thread-safe collections demo
                System.out.println("\n--- Thread-Safe Collections ---");
                demo.demonstrateThreadSafeCollections();
            }
            
            if (choice == 2 || choice == 3) {
                // Deadlock prevention demo
                System.out.println("\n--- Deadlock Prevention ---");
                demo.demonstrateDeadlockPrevention();
            }
            
            // Display best practices
            System.out.println("\n=== Thread Safety Best Practices ===");
            demo.displayBestPractices();
            
        } finally {
            scanner.close();
            System.out.println("\nProgram completed.");
        }
    }
    
    /**
     * Tests a counter implementation with multiple threads
     * 
     * @param counter The counter to test
     */
    public void testCounter(Counter counter) {
        System.out.println("\nTesting " + counter.getName());
        
        // Reset counter by creating new instance
        
        // Create and start threads
        Thread[] threads = new Thread[NUM_THREADS];
        for (int i = 0; i < NUM_THREADS; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < INCREMENTS_PER_THREAD; j++) {
                    counter.increment();
                }
            });
            threads[i].start();
        }
        
        // Wait for all threads to complete
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.out.println("Thread interrupted: " + e.getMessage());
                Thread.currentThread().interrupt();
            }
        }
        
        // Check final count
        int finalCount = counter.getValue();
        int expectedCount = NUM_THREADS * INCREMENTS_PER_THREAD;
        boolean accurate = finalCount == expectedCount;
        
        System.out.println("Final count: " + finalCount + 
                          " (Expected: " + expectedCount + ")");
        System.out.println("Result: " + (accurate ? "CORRECT ✓" : "INCORRECT ✗ - Race condition detected!"));
    }
    
    /**
     * Demonstrates thread-safe collections
     */
    public void demonstrateThreadSafeCollections() {
        // Regular HashMap vs ConcurrentHashMap
        System.out.println("\n1. HashMap vs ConcurrentHashMap");
        
        // Create collections
        final Map<Integer, String> regularMap = new HashMap<>();
        final Map<Integer, String> concurrentMap = new ConcurrentHashMap<>();
        final Map<Integer, String> synchronizedMap = 
            Collections.synchronizedMap(new HashMap<>());
        
        // Fill maps with initial data
        for (int i = 0; i < 100; i++) {
            String value = "Value-" + i;
            regularMap.put(i, value);
            concurrentMap.put(i, value);
            synchronizedMap.put(i, value);
        }
        
        // Test with multiple threads
        System.out.println("Running concurrent access test...");
        
        // Thread counts
        final AtomicInteger regularMapExceptions = new AtomicInteger(0);
        final AtomicInteger concurrentMapExceptions = new AtomicInteger(0);
        final AtomicInteger synchronizedMapExceptions = new AtomicInteger(0);
        
        // Create thread pool
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        
        // Submit tasks that read and write to the maps
        for (int i = 0; i < NUM_THREADS; i++) {
            final int threadNum = i;
            
            executor.submit(() -> {
                try {
                    Random random = new Random();
                    
                    // Each thread performs 1000 operations
                    for (int j = 0; j < 1000; j++) {
                        int key = random.nextInt(100);
                        
                        // Try updating regular HashMap
                        try {
                            // Might cause ConcurrentModificationException
                            regularMap.put(key, "UpdatedByThread-" + threadNum + "-" + j);
                            
                            // Read operation
                            regularMap.get(random.nextInt(100));
                            
                            // Iterate (likely to fail with concurrent modification)
                            if (random.nextInt(10) == 0) { // Do this less frequently
                                for (Map.Entry<Integer, String> entry : regularMap.entrySet()) {
                                    // Just access to cause potential ConcurrentModificationException
                                    entry.getKey();
                                    entry.getValue();
                                }
                            }
                        } catch (Exception e) {
                            regularMapExceptions.incrementAndGet();
                        }
                        
                        // Update ConcurrentHashMap (should be safe)
                        try {
                            concurrentMap.put(key, "UpdatedByThread-" + threadNum + "-" + j);
                            concurrentMap.get(random.nextInt(100));
                            
                            // Iteration is safe with ConcurrentHashMap
                            if (random.nextInt(10) == 0) {
                                for (Map.Entry<Integer, String> entry : concurrentMap.entrySet()) {
                                    entry.getKey();
                                    entry.getValue();
                                }
                            }
                        } catch (Exception e) {
                            concurrentMapExceptions.incrementAndGet();
                        }
                        
                        // Update synchronized HashMap (safe but less concurrent)
                        try {
                            synchronizedMap.put(key, "UpdatedByThread-" + threadNum + "-" + j);
                            synchronizedMap.get(random.nextInt(100));
                            
                            // Need external synchronization for iteration
                            if (random.nextInt(10) == 0) {
                                synchronized (synchronizedMap) {
                                    for (Map.Entry<Integer, String> entry : synchronizedMap.entrySet()) {
                                        entry.getKey();
                                        entry.getValue();
                                    }
                                }
                            }
                        } catch (Exception e) {
                            synchronizedMapExceptions.incrementAndGet();
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Thread " + threadNum + " encountered error: " + e.getMessage());
                }
            });
        }
        
        // Shutdown executor and wait for completion
        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
        
        // Report results
        System.out.println("Regular HashMap exceptions: " + regularMapExceptions.get());
        System.out.println("ConcurrentHashMap exceptions: " + concurrentMapExceptions.get());
        System.out.println("Synchronized HashMap exceptions: " + synchronizedMapExceptions.get());
        
        // Size check
        System.out.println("Regular HashMap final size: " + regularMap.size());
        System.out.println("ConcurrentHashMap final size: " + concurrentMap.size());
        System.out.println("Synchronized HashMap final size: " + synchronizedMap.size());
        
        System.out.println("\nKey Observations:");
        System.out.println("- Regular HashMap is not thread-safe and throws exceptions during concurrent access");
        System.out.println("- ConcurrentHashMap is designed for concurrent access with high throughput");
        System.out.println("- Synchronized HashMap is thread-safe but less concurrent as it locks the entire map");
    }
    
    /**
     * Demonstrates deadlock prevention techniques
     */
    public void demonstrateDeadlockPrevention() {
        System.out.println("\nDeadlock occurs when two or more threads wait forever for a lock held by the other");
        System.out.println("Here are techniques to prevent deadlocks:");
        
        // Define resources (locks)
        final Object resource1 = new Object();
        final Object resource2 = new Object();
        
        System.out.println("\n1. Lock Ordering - Example of potential deadlock:");
        System.out.println("   Thread 1: Lock A then Lock B");
        System.out.println("   Thread 2: Lock B then Lock A");
        System.out.println("   Solution: Always acquire locks in the same order");
        
        // Demonstrate proper lock ordering
        System.out.println("\n   Demonstrating proper lock ordering...");
        
        Thread thread1 = new Thread(() -> {
            System.out.println("   Thread 1: Acquiring locks in order: resource1 -> resource2");
            synchronized (resource1) {
                System.out.println("   Thread 1: Acquired resource1");
                
                // Simulate some work
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                
                synchronized (resource2) {
                    System.out.println("   Thread 1: Acquired resource2");
                    System.out.println("   Thread 1: Working with both resources");
                }
            }
            System.out.println("   Thread 1: Released all resources");
        });
        
        Thread thread2 = new Thread(() -> {
            System.out.println("   Thread 2: Acquiring locks in same order: resource1 -> resource2");
            synchronized (resource1) {
                System.out.println("   Thread 2: Acquired resource1");
                
                // Simulate some work
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                
                synchronized (resource2) {
                    System.out.println("   Thread 2: Acquired resource2");
                    System.out.println("   Thread 2: Working with both resources");
                }
            }
            System.out.println("   Thread 2: Released all resources");
        });
        
        thread1.start();
        thread2.start();
        
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println("   Both threads completed successfully");
        
        // Demonstrate timeout-based lock acquisition
        System.out.println("\n2. Lock Timeouts - Prevent indefinite waiting:");
        System.out.println("   Use tryLock() with timeout to avoid indefinite blocking");
        
        ExecutorService executor = Executors.newFixedThreadPool(2);
        
        final Lock lockA = new ReentrantLock();
        final Lock lockB = new ReentrantLock();
        
        // A runnable that attempts to acquire locks with timeout
        Runnable lockWithTimeout = () -> {
            String threadName = Thread.currentThread().getName();
            System.out.println("   " + threadName + ": Starting, will attempt locks with timeout");
            
            try {
                // Try to get lockA with timeout
                if (lockA.tryLock(500, TimeUnit.MILLISECONDS)) {
                    try {
                        System.out.println("   " + threadName + ": Acquired lockA");
                        
                        // Simulate work
                        Thread.sleep(100);
                        
                        // Try to get lockB with timeout
                        if (lockB.tryLock(500, TimeUnit.MILLISECONDS)) {
                            try {
                                System.out.println("   " + threadName + ": Acquired lockB");
                                System.out.println("   " + threadName + ": Working with both locks");
                            } finally {
                                lockB.unlock();
                                System.out.println("   " + threadName + ": Released lockB");
                            }
                        } else {
                            System.out.println("   " + threadName + ": Could not acquire lockB, giving up");
                        }
                    } finally {
                        lockA.unlock();
                        System.out.println("   " + threadName + ": Released lockA");
                    }
                } else {
                    System.out.println("   " + threadName + ": Could not acquire lockA, giving up");
                }
            } catch (InterruptedException e) {
                System.out.println("   " + threadName + ": Interrupted while waiting for lock");
                Thread.currentThread().interrupt();
            }
        };
        
        // Submit the task to executor
        executor.submit(lockWithTimeout);
        executor.submit(lockWithTimeout);
        
        // Shutdown executor and wait for completion
        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
        
        System.out.println("   Lock timeout demonstration completed");
    }
    
    /**
     * Displays best practices for thread safety
     */
    private void displayBestPractices() {
        System.out.println("1. Prefer immutable objects when possible - they're inherently thread-safe");
        System.out.println("2. Use thread-safe collections (ConcurrentHashMap, CopyOnWriteArrayList) for shared data");
        System.out.println("3. Keep critical sections (synchronized blocks) as small as possible");
        System.out.println("4. Acquire locks in a consistent order to prevent deadlocks");
        System.out.println("5. Use java.util.concurrent.atomic classes for simple counters and flags");
        System.out.println("6. Prefer higher-level concurrency utilities over wait/notify");
        System.out.println("7. Document thread safety characteristics of your classes");
        System.out.println("8. Use thread pools (ExecutorService) rather than creating threads directly");
        System.out.println("9. Use timeouts when waiting for locks to avoid indefinite blocking");
        System.out.println("10. Consider using ThreadLocal for thread-confined mutable data");
    }
}
