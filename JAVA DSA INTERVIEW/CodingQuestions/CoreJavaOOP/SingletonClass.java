/**
 * Core Java & OOP - Question 6: Implement a Singleton class
 */
package CodingQuestions.CoreJavaOOP;

public class SingletonClass {

    public static void main(String[] args) {
        System.out.println("Testing Eager Initialization Singleton:");
        EagerInitializationSingleton instance1 = EagerInitializationSingleton.getInstance();
        EagerInitializationSingleton instance2 = EagerInitializationSingleton.getInstance();
        System.out.println("instance1 == instance2: " + (instance1 == instance2));
        
        System.out.println("\nTesting Lazy Initialization Singleton:");
        LazyInitializationSingleton instance3 = LazyInitializationSingleton.getInstance();
        LazyInitializationSingleton instance4 = LazyInitializationSingleton.getInstance();
        System.out.println("instance3 == instance4: " + (instance3 == instance4));
        
        System.out.println("\nTesting Thread-Safe Singleton:");
        ThreadSafeSingleton instance5 = ThreadSafeSingleton.getInstance();
        ThreadSafeSingleton instance6 = ThreadSafeSingleton.getInstance();
        System.out.println("instance5 == instance6: " + (instance5 == instance6));
        
        System.out.println("\nTesting Double-Checked Locking Singleton:");
        DoubleCheckedLockingSingleton instance7 = DoubleCheckedLockingSingleton.getInstance();
        DoubleCheckedLockingSingleton instance8 = DoubleCheckedLockingSingleton.getInstance();
        System.out.println("instance7 == instance8: " + (instance7 == instance8));
        
        System.out.println("\nTesting Enum Singleton:");
        EnumSingleton instance9 = EnumSingleton.INSTANCE;
        EnumSingleton instance10 = EnumSingleton.INSTANCE;
        System.out.println("instance9 == instance10: " + (instance9 == instance10));
        instance9.doSomething();
    }
}

/**
 * Problem Statement:
 * Implement a Singleton class in Java. A Singleton is a design pattern that restricts
 * the instantiation of a class to one single instance and provides a global point of access to it.
 * 
 * Solution 1: Eager Initialization
 * 
 * Time Complexity: O(1) for getInstance()
 * Space Complexity: O(1)
 */
class EagerInitializationSingleton {
    // Private static instance created at class loading time
    private static final EagerInitializationSingleton INSTANCE = new EagerInitializationSingleton();
    
    // Private constructor to prevent instantiation from outside
    private EagerInitializationSingleton() {
        System.out.println("EagerInitializationSingleton instance created");
    }
    
    // Public static method to get the instance
    public static EagerInitializationSingleton getInstance() {
        return INSTANCE;
    }
}

/**
 * Solution 2: Lazy Initialization
 * 
 * Time Complexity: O(1) for getInstance()
 * Space Complexity: O(1)
 * 
 * Note: This implementation is not thread-safe
 */
class LazyInitializationSingleton {
    // Private static instance, initially null
    private static LazyInitializationSingleton instance;
    
    // Private constructor to prevent instantiation from outside
    private LazyInitializationSingleton() {
        System.out.println("LazyInitializationSingleton instance created");
    }
    
    // Public static method to get the instance
    public static LazyInitializationSingleton getInstance() {
        if (instance == null) {
            instance = new LazyInitializationSingleton();
        }
        return instance;
    }
}

/**
 * Solution 3: Thread-Safe Singleton using synchronized method
 * 
 * Time Complexity: O(1) for getInstance(), but with thread synchronization overhead
 * Space Complexity: O(1)
 */
class ThreadSafeSingleton {
    // Private static instance, initially null
    private static ThreadSafeSingleton instance;
    
    // Private constructor to prevent instantiation from outside
    private ThreadSafeSingleton() {
        System.out.println("ThreadSafeSingleton instance created");
    }
    
    // Public static synchronized method to get the instance
    public static synchronized ThreadSafeSingleton getInstance() {
        if (instance == null) {
            instance = new ThreadSafeSingleton();
        }
        return instance;
    }
}

/**
 * Solution 4: Double-Checked Locking Singleton
 * 
 * Time Complexity: O(1) for getInstance(), with minimal synchronization overhead
 * Space Complexity: O(1)
 */
class DoubleCheckedLockingSingleton {
    // Private static volatile instance, initially null
    private static volatile DoubleCheckedLockingSingleton instance;
    
    // Private constructor to prevent instantiation from outside
    private DoubleCheckedLockingSingleton() {
        System.out.println("DoubleCheckedLockingSingleton instance created");
    }
    
    // Public static method to get the instance with double-checked locking
    public static DoubleCheckedLockingSingleton getInstance() {
        // First check (not synchronized)
        if (instance == null) {
            // Synchronize only if instance is null
            synchronized (DoubleCheckedLockingSingleton.class) {
                // Second check (synchronized)
                if (instance == null) {
                    instance = new DoubleCheckedLockingSingleton();
                }
            }
        }
        return instance;
    }
}

/**
 * Solution 5: Enum Singleton (recommended by Joshua Bloch)
 * 
 * Time Complexity: O(1) for accessing the instance
 * Space Complexity: O(1)
 */
enum EnumSingleton {
    INSTANCE;
    
    // Constructor is automatically private in enum
    EnumSingleton() {
        System.out.println("EnumSingleton instance created");
    }
    
    public void doSomething() {
        System.out.println("EnumSingleton is doing something");
    }
}

/**
 * Logic Explanation:
 * 
 * 1. Eager Initialization:
 *    - The instance is created when the class is loaded by the JVM.
 *    - It's thread-safe but may create unnecessary overhead if the instance is never used.
 *    - Good for cases where the singleton is always needed and creation is not resource-intensive.
 * 
 * 2. Lazy Initialization:
 *    - The instance is created only when it's first requested.
 *    - Not thread-safe - multiple threads could create multiple instances.
 *    - Good for single-threaded environments or when thread safety is not a concern.
 * 
 * 3. Thread-Safe Singleton:
 *    - Uses the synchronized keyword to ensure only one thread can execute the method at a time.
 *    - Thread-safe but has performance overhead due to synchronization for every getInstance() call.
 * 
 * 4. Double-Checked Locking:
 *    - Combines lazy initialization with thread safety.
 *    - Uses synchronization only for the first few calls when instance is null.
 *    - The volatile keyword ensures the instance variable is correctly published to other threads.
 *    - Good balance between thread safety and performance.
 * 
 * 5. Enum Singleton:
 *    - Leverages Java's enum features which guarantee thread safety and serialization safety.
 *    - Prevents issues with reflection and serialization that can break other singleton implementations.
 *    - Considered the best practice for implementing singletons in Java.
 * 
 * Real-world Use Cases:
 * - Database connection pools
 * - Logger instances
 * - Configuration managers
 * - Cache implementations
 * - Thread pools
 * - Device drivers (e.g., printer spoolers)
 * - File managers
 */