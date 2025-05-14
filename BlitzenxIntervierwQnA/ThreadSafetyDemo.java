package BlitzenxIntervierwQnA;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.Scanner;

/**
 * Question 11: Illustrate how to make code thread safe in the solution for one of the above
 * 
 * This program demonstrates different approaches to thread safety by implementing
 * counter classes with various synchronization mechanisms.
 */
public class ThreadSafetyDemo {
    
    /**
     * Unsafe counter - not thread safe
     */
    public static class UnsafeCounter {
        private int count = 0;
        
        public void increment() {
            count++;
        }
        
        public int getCount() {
            return count;
        }
    }
    
    /**
     * Synchronized counter - thread safe using synchronized methods
     */
    public static class SynchronizedCounter {
        private int count = 0;
        
        public synchronized void increment() {
            count++;
        }
        
        public synchronized int getCount() {
            return count;
        }
    }
    
    /**
     * Lock-based counter - thread safe using explicit locks
     */
    public static class LockBasedCounter {
        private int count = 0;
        private Lock lock = new ReentrantLock();
        
        public void increment() {
            lock.lock();
            try {
                count++;
            } finally {
                lock.unlock();
            }
        }
        
        public int getCount() {
            lock.lock();
            try {
                return count;
            } finally {
                lock.unlock();
            }
        }
    }
    
    /**
     * Atomic counter - thread safe using atomic variables
     */
    public static class AtomicCounter {
        private AtomicInteger count = new AtomicInteger(0);
        
        public void increment() {
            count.incrementAndGet();
        }
        
        public int getCount() {
            return count.get();
        }
    }
    
    /**
     * Tests a counter with multiple threads
     */
    public static void testCounter(String counterType, Runnable incrementTask, Supplier<Integer> getCountTask) {
        System.out.println("\nTesting " + counterType + ":");
        
        // Create and start threads
        Thread[] threads = new Thread[10];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    incrementTask.run();
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
            }
        }
        
        // Get final count
        int finalCount = getCountTask.get();
        System.out.println("Expected count: 10,000");
        System.out.println("Actual count: " + finalCount);
        
        if (finalCount == 10000) {
            System.out.println("Result: THREAD SAFE ✓");
        } else {
            System.out.println("Result: NOT THREAD SAFE ✗");
        }
    }
    
    /**
     * Functional interface for getting a value
     */
    public interface Supplier<T> {
        T get();
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Thread Safety Demonstration");
        System.out.println("-------------------------");
        System.out.println("This program demonstrates different approaches to thread safety");
        System.out.println("by incrementing a counter 10,000 times using 10 threads.");
        
        try {
            System.out.println("\nChoose a counter to test:");
            System.out.println("1. Unsafe Counter (not thread safe)");
            System.out.println("2. Synchronized Counter (thread safe using synchronized methods)");
            System.out.println("3. Lock-Based Counter (thread safe using explicit locks)");
            System.out.println("4. Atomic Counter (thread safe using atomic variables)");
            System.out.println("5. Test All Counters");
            System.out.print("Enter your choice (1-5): ");
            
            int choice = scanner.nextInt();
            
            switch (choice) {
                case 1:
                    testUnsafeCounter();
                    break;
                case 2:
                    testSynchronizedCounter();
                    break;
                case 3:
                    testLockBasedCounter();
                    break;
                case 4:
                    testAtomicCounter();
                    break;
                case 5:
                    testAllCounters();
                    break;
                default:
                    System.out.println("Invalid choice. Testing all counters.");
                    testAllCounters();
            }
            
            // Explain thread safety
            System.out.println("\nThread Safety Explanation:");
            System.out.println("1. Thread safety ensures correct behavior when multiple threads access shared resources");
            System.out.println("2. Unsafe code can lead to race conditions and incorrect results");
            System.out.println("3. Common thread safety techniques:");
            System.out.println("   - Synchronization: Using synchronized keyword to lock methods or blocks");
            System.out.println("   - Explicit locks: Using Lock interface for more flexible locking");
            System.out.println("   - Atomic variables: Using atomic classes for lock-free thread safety");
            System.out.println("   - Immutable objects: Objects that cannot be modified after creation");
            System.out.println("   - Thread confinement: Restricting access to a thread");
            System.out.println("4. Choose the appropriate technique based on performance and complexity needs");
            
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
    
    private static void testUnsafeCounter() {
        UnsafeCounter counter = new UnsafeCounter();
        testCounter("Unsafe Counter", counter::increment, counter::getCount);
    }
    
    private static void testSynchronizedCounter() {
        SynchronizedCounter counter = new SynchronizedCounter();
        testCounter("Synchronized Counter", counter::increment, counter::getCount);
    }
    
    private static void testLockBasedCounter() {
        LockBasedCounter counter = new LockBasedCounter();
        testCounter("Lock-Based Counter", counter::increment, counter::getCount);
    }
    
    private static void testAtomicCounter() {
        AtomicCounter counter = new AtomicCounter();
        testCounter("Atomic Counter", counter::increment, counter::getCount);
    }
    
    private static void testAllCounters() {
        testUnsafeCounter();
        testSynchronizedCounter();
        testLockBasedCounter();
        testAtomicCounter();
    }
}
