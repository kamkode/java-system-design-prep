# Advanced Java Interview Questions and Answers

This document provides a comprehensive guide to advanced Java concepts commonly tested in technical interviews. For in-depth coverage of each topic, please refer to the dedicated markdown files linked below.

## Table of Contents

### Core Advanced Topics
1. [Java Reflection API](Reflection.md) - In-depth coverage of reflection capabilities
2. [Java Annotations](Annotations.md) - Comprehensive guide to annotations and their usage
3. [Java Generics](Generics.md) - Complete reference for generic programming in Java
4. [Lambda Expressions & Streams](LambdaStreams.md) - Detailed guide to functional programming in Java
5. [Concurrency](Concurrency.md) - Advanced threading and concurrency concepts

### Additional Advanced Topics
6. [CompletableFuture](#completablefuture) - Asynchronous programming in Java
7. [Java 9+ Features](#java-9-features) - Modules, JShell, and other modern Java features
8. [Virtual Threads](#virtual-threads) - Project Loom and lightweight concurrency
9. [Java Records](#records) - Immutable data carriers
10. [Sealed Classes](#sealed-classes) - Restricted class hierarchies

# Java Reflection API

Java Reflection is a powerful feature that allows examining and modifying the runtime behavior of applications running in the Java Virtual Machine. It enables Java code to discover information about the fields, methods, and constructors of loaded classes, and to use reflected fields, methods, and constructors to operate on their underlying counterparts.

## 🔑 Core Concepts

### 1. Class Objects
- Entry point for all reflection operations
- Three ways to get Class object:
  ```java
  Class<?> c1 = String.class;           // Using class literal
  Class<?> c2 = "hello".getClass();    // Using getClass()
  Class<?> c3 = Class.forName("java.lang.String");  // Using Class.forName()
  ```

### 2. Reflection Classes
- **Field**: Access and modify fields
- **Method**: Invoke methods
- **Constructor**: Create new instances
- **Modifier**: Access modifiers and properties
- **Array**: Create and manipulate arrays
- **Proxy**: Create dynamic proxy classes

## 💻 Code Examples

### Basic Reflection Operations

```java
// 1. Get class information
Class<?> clazz = String.class;
String className = clazz.getName();  // "java.lang.String"

// 2. Working with fields
Field[] fields = clazz.getDeclaredFields();  // All fields including private
Field valueField = clazz.getDeclaredField("value");
valueField.setAccessible(true);  // Required for private fields

// 3. Working with methods
Method[] methods = clazz.getMethods();  // Public methods including inherited
Method toUpper = clazz.getMethod("toUpperCase");
String result = (String) toUpper.invoke("hello");  // "HELLO"

// 4. Working with constructors
Constructor<?> constructor = clazz.getConstructor(String.class);
String str = (String) constructor.newInstance("Hello");
```

### Advanced Reflection: Dynamic Proxies

```java
interface Greeter {
    String greet(String name);
}

// Create proxy instance
Greeter greeter = (Greeter) Proxy.newProxyInstance(
    Greeter.class.getClassLoader(),
    new Class<?>[] { Greeter.class },
    (proxy, method, args) -> {
        if (method.getName().equals("greet")) {
            return "Hello, " + args[0] + "!";
        }
        return null;
    }
);

System.out.println(greeter.greet("World"));  // "Hello, World!"
```

## 🎯 Common Interview Questions

### 1. What is Java Reflection and when should it be used?
- **Answer**: Reflection allows examining and modifying runtime behavior of applications. 
- **Use Cases**: 
  - Frameworks (Spring, Hibernate)
  - IDEs and development tools
  - Testing frameworks
  - Class browsers
  - Debuggers

### 2. What are the performance implications of using Reflection?
- **Performance Impact**:
  - 50-100x slower than direct method calls
  - No method inlining by JIT
  - Security checks overhead
- **Mitigation**:
  - Cache reflective objects
  - Use `setAccessible(true)`
  - Consider `MethodHandle` in Java 7+

### 3. How does Reflection handle generics?
- **Type Erasure**: Generic type information is removed at runtime
- **Example**:
  ```java
  List<String> list = new ArrayList<>();
  // At runtime, list is just a raw List
  ```
- **Accessing Generic Info**:
  ```java
  Method method = MyClass.class.getMethod("getList");
  Type returnType = method.getGenericReturnType();
  if (returnType instanceof ParameterizedType) {
      Type[] typeArgs = ((ParameterizedType) returnType).getActualTypeArguments();
  }
  ```

### 4. What is the difference between getFields() and getDeclaredFields()?
- **getFields()**: Returns all public fields including inherited ones
- **getDeclaredFields()**: Returns all fields (including private/protected) but only from the current class

### 5. How can you access private fields/methods using Reflection?
```java
public class ReflectionDemo {
    private String secret = "Confidential";
    
    private String getSecret() {
        return secret;
    }
}

// Accessing private field and method
ReflectionDemo obj = new ReflectionDemo();

// Access private field
Field field = obj.getClass().getDeclaredField("secret");
field.setAccessible(true);
String value = (String) field.get(obj);

// Access private method
Method method = obj.getClass().getDeclaredMethod("getSecret");
method.setAccessible(true);
String result = (String) method.invoke(obj);
```

## 🚀 Real-world Applications

### 1. Dependency Injection (Spring Framework)
```java
@Component
public class MyService {
    @Autowired
    private MyRepository repository;
    
    // Spring uses reflection to inject the dependency
}
```

### 2. ORM Frameworks (Hibernate)
```java
@Entity
public class User {
    @Id
    @GeneratedValue
    private Long id;
    
    @Column(name = "username")
    private String name;
    
    // Hibernate uses reflection to map database rows to objects
}
```

### 3. Testing Frameworks (JUnit, Mockito)
```java
public class CalculatorTest {
    @Test
    public void testAdd() {
        Calculator calc = new Calculator();
        assertEquals(5, calc.add(2, 3));
    }
    
    @Mock
    private UserService userService;
}
```

## ⚠️ Security Considerations
1. **Security Manager**: Can restrict reflection operations
2. **Access Control**: `setAccessible(true)` can bypass access controls
3. **Performance**: Reflection operations are not optimized by JIT
4. **Maintainability**: Makes code harder to understand and maintain

## 📊 Performance Comparison

| Operation | Direct Call | Reflection |
|-----------|-------------|------------|
| Method Call | 1x | 50-100x slower |
| Field Access | 1x | 20-50x slower |
| Constructor | 1x | 15-20x slower |

## 🔄 Alternatives to Reflection
1. **Method Handles (Java 7+)**
   ```java
   MethodHandles.Lookup lookup = MethodHandles.lookup();
   MethodHandle mh = lookup.findVirtual(String.class, "length", 
       MethodType.methodType(int.class));
   int len = (int) mh.invokeExact("Hello");  // 5
   ```

2. **LambdaMetafactory (Java 8+)**
   ```java
   MethodHandles.Lookup lookup = MethodHandles.lookup();
   MethodType type = MethodType.methodType(int.class);
   CallSite site = LambdaMetafactory.metafactory(
       lookup, "getAsInt",
       MethodType.methodType(IntSupplier.class, String.class),
       type.changeReturnType(int.class),
       lookup.findVirtual(String.class, "length", type),
       type);
   IntSupplier func = (IntSupplier) site.getTarget().invokeExact("Hello");
   int len = func.getAsInt();  // 5
   ```

## 📝 Best Practices
1. **Cache** reflection objects when possible
2. **Avoid** using reflection in performance-critical sections
3. **Prefer** interfaces over reflection when possible
4. **Handle** exceptions properly (NoSuchMethodException, IllegalAccessException, etc.)
5. **Document** reflective code thoroughly

## Java Annotations

Annotations provide metadata about a program that is not part of the program itself. They have no direct effect on the operation of the code they annotate but can be processed at compile time or runtime.

### Annotation Types:
1. **Marker Annotations**: No elements (e.g., `@Override`)
2. **Single-Value Annotations**: Single element (e.g., `@SuppressWarnings("unchecked")`)
3. **Full Annotations**: Multiple elements (e.g., `@RequestMapping(method = RequestMethod.GET, path = "/api")`)

### Built-in Annotations:
- **`@Override`**: Indicates method overrides superclass method
- **`@Deprecated`**: Marks program elements as deprecated
- **`@SuppressWarnings`**: Suppresses compiler warnings
- **`@SafeVarargs`**: Suppresses warnings about varargs
- **`@FunctionalInterface`**: Indicates an interface with exactly one abstract method
- **`@Repeatable`**: Allows multiple annotations of the same type
- **`@Retention`**: Specifies how long annotations are retained
- **`@Target`**: Specifies where the annotation can be used

### Creating Custom Annotations:

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface LogExecutionTime {
    String value() default "";
    boolean logParameters() default false;
}

// Usage
public class Service {
    @LogExecutionTime("Processing data")
    public void process() {
        // Method implementation
    }
}
```

### Processing Annotations at Runtime:

```java
Method method = obj.getClass().getMethod("process");
if (method.isAnnotationPresent(LogExecutionTime.class)) {
    LogExecutionTime annotation = method.getAnnotation(LogExecutionTime.class);
    String message = annotation.value();
    // Process annotation
}
```

### Framework Usage Examples:

**Spring Framework:**
```java
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }
}
```

**Hibernate/JPA:**
```java
@Entity
@Table(name = "employees")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "first_name", nullable = false)
    private String firstName;
    
    @Column(name = "last_name", nullable = false)
    private String lastName;
    
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private Set<Address> addresses = new HashSet<>();
}
```

**JUnit 5:**
```java
@DisplayName("User Service Tests")
class UserServiceTest {
    
    @Mock
    private UserRepository userRepository;
    
    @InjectMocks
    private UserService userService;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    @DisplayName("Should save user successfully")
    void shouldSaveUser() {
        // Test implementation
    }
}
```

## Java Generics

Generics add stability to your code by making more of your bugs detectable at compile time. They enable types (classes and interfaces) to be parameters when defining classes, interfaces, and methods.

### Key Benefits:
1. **Type Safety**: Catches invalid types at compile time
2. **Eliminates Casting**: No need for explicit type casting
3. **Enables Generic Algorithms**: Write methods/classes that work on different types
4. **Better Code Reuse**: Write once, use with any type
5. **Improved Readability**: Makes code more self-documenting

### Basic Syntax:

```java
// Generic class
public class Box<T> {
    private T content;
    
    public void set(T content) {
        this.content = content;
    }
    
    public T get() {
        return content;
    }
}

// Usage
Box<String> stringBox = new Box<>();
stringBox.set("Hello");
String value = stringBox.get(); // No casting needed

Box<Integer> intBox = new Box<>();
intBox.set(42);
int number = intBox.get();
```

### Bounded Type Parameters:

```java
// Upper bound
public <T extends Number> double sum(List<T> numbers) {
    return numbers.stream()
                 .mapToDouble(Number::doubleValue)
                 .sum();
}

// Multiple bounds
public static <T extends Number & Comparable<T>> T max(T a, T b) {
    return a.compareTo(b) > 0 ? a : b;
}
```

### Wildcards:

```java
// Unbounded wildcard
public void printList(List<?> list) {
    for (Object elem : list) {
        System.out.println(elem);
    }
}

// Upper bounded wildcard
public double sumOfList(List<? extends Number> list) {
    return list.stream()
              .mapToDouble(Number::doubleValue)
              .sum();
}

// Lower bounded wildcard
public void addNumbers(List<? super Integer> list) {
    for (int i = 1; i <= 10; i++) {
        list.add(i);
    }
}
```

### Type Erasure and Reification:
- Generics are implemented using type erasure
- Type parameters are removed and replaced with their bounds or Object
- Enables backward compatibility with pre-generics code
- Some type information is available at runtime through reflection

### Common Generic Collections:

```java
List<String> names = new ArrayList<>();
Map<String, Integer> wordCounts = new HashMap<>();
Set<Number> numbers = new HashSet<>();
```

### Generic Methods:

```java
public static <T> T getFirst(List<T> list) {
    return list.get(0);
}

// Type inference
String first = getFirst(Arrays.asList("a", "b"));
```

### Real-world Use Cases:
1. **Collections Framework**: `List<T>`, `Map<K,V>`, `Set<T>`
2. **Functional Interfaces**: `Function<T,R>`, `Predicate<T>`, `Supplier<T>`
3. **DAO Pattern**: `interface Repository<T, ID> { ... }`
4. **Builder Pattern**: `Builder<T> { T build(); }`
5. **Caching**: `Cache<K,V> { V get(K key); void put(K key, V value); }`

### Advanced Topics:
- Recursive type bounds
- Self-bounded types
- Bridge methods
- Type tokens
- Reifiable types

## Lambda Expressions & Streams

### Lambda Expressions

Lambda expressions, introduced in Java 8, provide a clear and concise way to represent one method interface (functional interface) using an expression. They enable functional programming in Java.

#### Key Features:
- **Concise Syntax**: Reduces boilerplate code
- **Functional Programming**: Enables functional programming constructs
- **Method References**: Shorthand for lambda expressions
- **Variable Capture**: Can access effectively final variables from the enclosing scope
- **`this` Reference**: Refers to the enclosing instance in a lambda

#### Syntax:
```java
(parameters) -> expression

(parameters) -> { statements; }

() -> expression
```

#### Examples:
```java
// 1. No parameters
Runnable r = () -> System.out.println("Hello, Lambda!");

// 2. Single parameter with type inference
Consumer<String> c = msg -> System.out.println(msg);

// 3. Multiple parameters
Comparator<String> comp = (s1, s2) -> s1.compareToIgnoreCase(s2);

// 4. With explicit parameter types
BinaryOperator<Long> add = (Long x, Long y) -> x + y;

// 5. Multiple statements in body
Function<String, String> makeGreeting = name -> {
    String greeting = "Hello, " + name + "!";
    return greeting.toUpperCase();
};
```

### Functional Interfaces

A functional interface is an interface with exactly one abstract method. They can be implemented using lambda expressions.

#### Built-in Functional Interfaces:

1. **`Function<T,R>`**: Takes one argument of type T and returns a result of type R
   ```java
   Function<String, Integer> length = s -> s.length();
   ```

2. **`Predicate<T>`**: Takes one argument of type T and returns a boolean
   ```java
   Predicate<String> isLong = s -> s.length() > 10;
   ```

3. **`Consumer<T>`**: Takes one argument of type T and returns no result
   ```java
   Consumer<String> printer = s -> System.out.println(s);
   ```

4. **`Supplier<T>`**: Takes no arguments and returns a result of type T
   ```java
   Supplier<Double> random = () -> Math.random();
   ```

5. **`UnaryOperator<T>`**: Takes one argument of type T and returns a result of the same type
   ```java
   UnaryOperator<String> toUpper = s -> s.toUpperCase();
   ```

6. **`BinaryOperator<T>`**: Takes two arguments of type T and returns a result of the same type
   ```java
   BinaryOperator<Integer> sum = (a, b) -> a + b;
   ```

#### Method References:

```java
// Static method reference
Function<String, Integer> parseInt = Integer::parseInt;

// Instance method reference on a particular instance
String str = "Hello";
Supplier<Integer> length = str::length;

// Instance method reference on an arbitrary instance
Function<String, String> toUpper = String::toUpperCase;

// Constructor reference
Supplier<List<String>> listSupplier = ArrayList::new;
```

### Streams API

The Streams API provides a functional approach to processing collections of objects. It allows for operations like filter, map, reduce, and collect in a declarative way.

#### Key Characteristics:
- **Not a data structure**: Doesn't store data
- **Functional in nature**: Operations produce results but don't modify the source
- **Lazy evaluation**: Intermediate operations are not executed until a terminal operation is invoked
- **Consumable**: Elements can only be traversed once

#### Common Operations:

1. **Creating Streams**:
   ```java
   // From Collection
   List<String> list = Arrays.asList("a", "b", "c");
   Stream<String> stream = list.stream();
   
   // From values
   Stream<String> values = Stream.of("a", "b", "c");
   
   // From array
   String[] array = {"a", "b", "c"};
   Stream<String> arrayStream = Arrays.stream(array);
   
   // Infinite streams
   Stream<Integer> numbers = Stream.iterate(0, n -> n + 1);
   Stream<Double> randoms = Stream.generate(Math::random);
   ```

2. **Intermediate Operations**:
   ```java
   // Filter elements
   Stream<String> longNames = names.stream()
                                 .filter(name -> name.length() > 5);
   
   // Transform elements
   Stream<Integer> nameLengths = names.stream()
                                   .map(String::length);
   
   // Flatten nested structures
   List<List<String>> nestedList = ...;
   Stream<String> flatStream = nestedList.stream()
                                       .flatMap(List::stream);
   
   // Remove duplicates
   Stream<String> unique = Stream.of("a", "b", "a").distinct();
   
   // Sort elements
   Stream<String> sorted = names.stream().sorted();
   
   // Limit and skip
   Stream<Integer> limited = Stream.iterate(0, n -> n + 1).limit(10);
   Stream<Integer> skipped = Stream.iterate(0, n -> n + 1).skip(5);
   
   // Peek (for debugging)
   Stream.of("one", "two", "three")
         .filter(e -> e.length() > 3)
         .peek(e -> System.out.println("Filtered value: " + e))
         .map(String::toUpperCase)
         .peek(e -> System.out.println("Mapped value: " + e))
         .collect(Collectors.toList());
   ```

3. **Terminal Operations**:
   ```java
   // Collect to collection
   List<String> result = stream.collect(Collectors.toList());
   
   // Count elements
   long count = stream.count();
   
   // Find any/first element
   Optional<String> any = stream.findAny();
   Optional<String> first = stream.findFirst();
   
   // Check if any/all/none match
   boolean anyMatch = stream.anyMatch(s -> s.startsWith("a"));
   boolean allMatch = stream.allMatch(s -> s.length() > 3);
   boolean noneMatch = stream.noneMatch(s -> s.isEmpty());
   
   // Reduce to single value
   Optional<String> concatenated = stream.reduce((a, b) -> a + "," + b);
   
   // For each element
   stream.forEach(System.out::println);
   
   // Min/max
   Optional<String> min = stream.min(Comparator.naturalOrder());
   Optional<String> max = stream.max(String::compareToIgnoreCase);
   
   // To array
   String[] array = stream.toArray(String[]::new);
   ```

4. **Collectors**:
   ```java
   // To list/set
   List<String> list = stream.collect(Collectors.toList());
   Set<String> set = stream.collect(Collectors.toSet());
   
   // To map
   Map<String, Integer> map = stream.collect(
       Collectors.toMap(Function.identity(), String::length));
   
   // Grouping by
   Map<Integer, List<String>> groupedByLength = stream.collect(
       Collectors.groupingBy(String::length));
   
   // Joining
   String joined = stream.collect(Collectors.joining(", ", "[", "]"));
   
   // Summarizing
   IntSummaryStatistics stats = stream.collect(
       Collectors.summarizingInt(String::length));
   ```

#### Parallel Streams:

```java
// Create parallel stream
Stream<String> parallelStream = list.parallelStream();

// Convert existing stream to parallel
Stream<String> parallel = stream.parallel();

// Example: Parallel processing
long count = list.parallelStream()
                .filter(s -> s.length() > 5)
                .count();
```

#### Best Practices:
1. **Prefer Method References**: Make code more readable
2. **Avoid Side Effects**: Keep lambdas pure when possible
3. **Use Primitive Streams**: For better performance with primitives
4. **Limit Parallelism**: Not all operations benefit from parallel streams
5. **Close Resources**: Use try-with-resources for I/O operations
6. **Avoid Stateful Operations**: In parallel streams
7. **Consider Ordering**: Parallel streams may process elements out of order

## Concurrency

Java provides comprehensive support for concurrent programming through its built-in concurrency utilities. Understanding concurrency is crucial for building high-performance, responsive applications.

### Thread Fundamentals

#### Creating Threads

1. **Extending Thread class**:
   ```java
   class MyThread extends Thread {
       @Override
       public void run() {
           System.out.println("Thread running");
       }
   }
   
   // Usage
   Thread thread = new MyThread();
   thread.start();
   ```

2. **Implementing Runnable**:
   ```java
   class MyRunnable implements Runnable {
       @Override
       public void run() {
           System.out.println("Runnable running");
       }
   }
   
   // Usage
   Thread thread = new Thread(new MyRunnable());
   thread.start();
   
   // Or with lambda
   new Thread(() -> System.out.println("Lambda runnable")).start();
   ```

#### Thread Lifecycle

1. **NEW**: Thread created but not yet started
2. **RUNNABLE**: Thread is executing in JVM
3. **BLOCKED**: Thread blocked waiting for a monitor lock
4. **WAITING**: Thread waiting indefinitely for another thread
5. **TIMED_WAITING**: Thread waiting for a specified time
6. **TERMINATED**: Thread has completed execution

#### Thread Priorities
- Thread.MIN_PRIORITY (1)
- Thread.NORM_PRIORITY (5) - default
- Thread.MAX_PRIORITY (10)

```java
thread.setPriority(Thread.MAX_PRIORITY);
```

### Synchronization

#### Synchronized Methods
```java
public synchronized void increment() {
    count++;  // Thread-safe increment
}
```

#### Synchronized Blocks
```java
public void addName(String name) {
    synchronized(this) {
        names.add(name);
    }
}
```

#### Static Synchronization
```java
public static synchronized void staticMethod() {
    // Synchronized on class object
}

// Equivalent to
public static void staticMethod() {
    synchronized(MyClass.class) {
        // ...
    }
}
```

### java.util.concurrent Package

#### Executor Framework
```java
// Single thread executor
ExecutorService executor = Executors.newSingleThreadExecutor();

// Fixed thread pool
ExecutorService executor = Executors.newFixedThreadPool(4);


// Cached thread pool
ExecutorService executor = Executors.newCachedThreadPool();

// Scheduled executor
ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

scheduler.schedule(() -> System.out.println("Delayed task"), 5, TimeUnit.SECONDS);

// Submit tasks
executor.submit(() -> System.out.println("Task running"));

// Shutdown
executor.shutdown();
```

#### Callable and Future
```java
Callable<String> task = () -> {
    TimeUnit.SECONDS.sleep(1);
    return "Task completed";
};

ExecutorService executor = Executors.newSingleThreadExecutor();
Future<String> future = executor.submit(task);

// Block and get result
String result = future.get();

// Check if done
if (future.isDone()) {
    // Get result
}

// Cancel task
future.cancel(true);
```

### Concurrent Collections

#### ConcurrentHashMap
```java
ConcurrentMap<String, Integer> map = new ConcurrentHashMap<>();
map.put("key", 1);
map.computeIfAbsent("key", k -> 1);
```

#### CopyOnWriteArrayList
```java
List<String> list = new CopyOnWriteArrayList<>();
list.add("item1");
list.addIfAbsent("item1");
```

#### BlockingQueue
```java
BlockingQueue<String> queue = new LinkedBlockingQueue<>();
queue.put("item");  // Blocks if full
String item = queue.take();  // Blocks if empty
```

### Locks and Conditions

#### ReentrantLock
```java
Lock lock = new ReentrantLock();
try {
    lock.lock();
    // Critical section
} finally {
    lock.unlock();
}

// Try lock
if (lock.tryLock(1, TimeUnit.SECONDS)) {
    try {
        // Critical section
    } finally {
        lock.unlock();
    }
}
```

#### ReadWriteLock
```java
ReadWriteLock rwLock = new ReentrantReadWriteLock();

// Read lock
rwLock.readLock().lock();
try {
    // Multiple readers can enter
} finally {
    rwLock.readLock().unlock();
}

// Write lock
rwLock.writeLock().lock();
try {
    // Only one writer can enter
} finally {
    rwLock.writeLock().unlock();
}
```

### Atomic Variables
```java
AtomicInteger counter = new AtomicInteger(0);
counter.incrementAndGet();
counter.compareAndSet(expectedValue, newValue);

// For compound operations
AtomicReference<BigInteger> lastNumber = new AtomicReference<>();
lastNumber.updateAndGet(x -> x.multiply(BigInteger.TWO));
```

### CountDownLatch
```java
CountDownLatch latch = new CountDownLatch(3);

// Worker thread
new Thread(() -> {
    // Do work
    latch.countDown();
}).start();

// Main thread
latch.await();  // Blocks until count reaches zero
```

### CyclicBarrier
```java
CyclicBarrier barrier = new CyclicBarrier(3, 
    () -> System.out.println("All threads reached barrier"));

// Worker thread
new Thread(() -> {
    // Do work
    barrier.await();  // Waits for all threads
}).start();
```

### Semaphore
```java
Semaphore semaphore = new Semaphore(3);  // 3 permits

try {
    semaphore.acquire();
    // Access shared resource
} finally {
    semaphore.release();
}

// Try acquire
if (semaphore.tryAcquire(1, TimeUnit.SECONDS)) {
    try {
        // Access shared resource
    } finally {
        semaphore.release();
    }
}
```

### CompletableFuture

#### Basic Usage
```java
CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
    // Long computation
    return "Result";
});


// Chain operations
future.thenApply(result -> result + " processed")
      .thenAccept(System.out::println);

// Handle exceptions
future.exceptionally(ex -> {
    System.err.println("Error: " + ex.getMessage());
    return "default";
});

// Combine futures
CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> "Hello");
CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> "World");
future1.thenCombine(future2, (a, b) -> a + " " + b)
      .thenAccept(System.out::println);
```

### Best Practices

1. **Prefer High-Level APIs**: Use `java.util.concurrent` over wait/notify
2. **Use Thread Pools**: Avoid creating threads directly
3. **Avoid Deadlocks**: Always acquire locks in the same order
4. **Minimize Lock Contention**: Keep critical sections small
5. **Use Immutable Objects**: Thread-safe by design
6. **Beware of Liveness Issues**: Deadlock, livelock, and starvation
7. **Use volatile for Visibility**: When only visibility is needed, not atomicity
8. **Prefer Concurrent Collections**: Over synchronized collections
9. **Use Atomic Variables**: For simple atomic operations
10. **Document Thread Safety**: Clearly document thread-safety guarantees

## CompletableFuture

`CompletableFuture` is a powerful class introduced in Java 8 that represents a future result of an asynchronous computation. It provides a rich API for composing, combining, and executing asynchronous operations in a non-blocking way.

### Key Features

1. **Asynchronous Execution**: Execute tasks asynchronously using various thread pools
2. **Chaining**: Chain multiple operations using methods like `thenApply`, `thenAccept`, `thenRun`
3. **Combining**: Combine multiple futures with `thenCombine`, `thenCompose`
4. **Exception Handling**: Handle exceptions with `exceptionally`, `handle`, `whenComplete`
5. **Timeouts**: Add timeouts with `completeOnTimeout` and `orTimeout`
6. **Manual Completion**: Complete futures manually with `complete` or `completeExceptionally`
7. **Composition**: Compose multiple futures sequentially or in parallel

### Creating CompletableFuture

```java
// Create a completed future
CompletableFuture<String> completed = CompletableFuture.completedFuture("Done");

// Create and complete manually
CompletableFuture<String> future = new CompletableFuture<>();
future.complete("Manually completed");

// Run async with default ForkJoinPool
CompletableFuture<Void> runAsync = CompletableFuture.runAsync(() -> {
    System.out.println("Running async task");
});

// Supply async with custom executor
ExecutorService executor = Executors.newFixedThreadPool(2);
CompletableFuture<String> supplyAsync = CompletableFuture.supplyAsync(() -> {
    return "Result from custom executor";
}, executor);
```

### Chaining Operations

```java
CompletableFuture.supplyAsync(() -> {
        // Simulate long computation
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
        return "Hello";
    })
    .thenApply(s -> s + " World")           // Transform result
    .thenApply(String::toUpperCase)           // Transform result
    .thenAccept(System.out::println)          // Consume result
    .thenRun(() -> System.out.println("Done")) // Run after completion
    .join();                                  // Block until complete
```

### Combining Futures

#### thenCompose (flatMap)
```java
CompletableFuture<String> future = getUser(userId)
    .thenCompose(user -> getProfile(user));

private CompletableFuture<User> getUser(String id) {
    return CompletableFuture.supplyAsync(() -> userRepository.findById(id));
}

private CompletableFuture<String> getProfile(User user) {
    return CompletableFuture.supplyAsync(() -> profileService.getProfile(user));
}
```

#### thenCombine (zip)
```java
CompletableFuture<String> future1 = getDataFromSource1();
CompletableFuture<String> future2 = getDataFromSource2();

future1.thenCombine(future2, (result1, result2) -> {
    return result1 + " " + result2;
}).thenAccept(System.out::println);
```

#### allOf / anyOf
```java
// Wait for all to complete
CompletableFuture<Void> all = CompletableFuture.allOf(
    future1, future2, future3
);
all.thenRun(() -> System.out.println("All completed"));

// Complete when any completes
CompletableFuture<Object> any = CompletableFuture.anyOf(
    future1, future2, future3
);
any.thenAccept(result -> System.out.println("First result: " + result));
```

### Exception Handling

```java
CompletableFuture.supplyAsync(() -> {
        // May throw exception
        return processData();
    })
    .exceptionally(ex -> {
        System.err.println("Exception: " + ex.getMessage());
        return "default value";
    })
    .handle((result, ex) -> {
        if (ex != null) {
            return "handled error: " + ex.getMessage();
        }
        return "success: " + result;
    })
    .whenComplete((result, ex) -> {
        // Always executed
        if (ex != null) {
            System.err.println("Completed with exception: " + ex.getMessage());
        } else {
            System.out.println("Completed successfully: " + result);
        }
    });
```

### Timeouts

```java
// Complete with timeout
CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
    try {
        Thread.sleep(2000);
        return "Result";
    } catch (InterruptedException e) {
        throw new RuntimeException(e);
    }
});

// Complete with default value after timeout
future.completeOnTimeout("Default", 1, TimeUnit.SECONDS)
      .thenAccept(System.out::println);

// Complete exceptionally after timeout
future.orTimeout(1, TimeUnit.SECONDS)
      .exceptionally(ex -> {
          System.out.println("Timeout: " + ex.getMessage());
          return null;
      });
```

### Best Practices

1. **Always handle exceptions**: Use `exceptionally`, `handle`, or `whenComplete`
2. **Use timeouts**: Prevent hanging operations with `orTimeout` or `completeOnTimeout`
3. **Prefer non-blocking**: Use `then*` methods instead of `get()` when possible
4. **Use custom executors**: For better control over thread pools
5. **Avoid blocking in callbacks**: Keep callbacks short and non-blocking
6. **Clean up resources**: Shutdown custom executors when done
7. **Be careful with `join()`**: Only use when you need to block the current thread
8. **Use `supplyAsync` for computations**: Prefer over `runAsync` when you need a result
9. **Combine operations**: Use `thenCompose` and `thenCombine` for better composition
10. **Monitor performance**: Keep an eye on thread pool usage and task execution times

### Real-world Example: Parallel API Calls

```java
public CompletableFuture<SearchResults> search(String query) {
    // Execute searches in parallel
    CompletableFuture<List<Product>> products = searchProducts(query);
    CompletableFuture<List<Review>> reviews = fetchReviews(query);
    CompletableFuture<Map<String, Price>> prices = getPrices(query);
    
    // Combine results when all complete
    return CompletableFuture.allOf(products, reviews, prices)
        .thenApply(v -> {
            // All futures are complete, get results
            SearchResults results = new SearchResults();
            results.setProducts(products.join());
            results.setReviews(reviews.join());
            results.setPrices(prices.join());
            return results;
        })
        .exceptionally(ex -> {
            // Handle any errors
            System.err.println("Search failed: " + ex.getMessage());
            return new SearchResults(); // Return empty results
        });
}

// Usage
search("laptop")
    .thenAccept(results -> {
        // Update UI with results
        updateUI(results);
    })
    .exceptionally(ex -> {
        showError("Failed to load search results");
        return null;
    });
```

### Advanced: Custom CompletionStage

```java
public <T> CompletableFuture<T> withRetry(
        Supplier<CompletableFuture<T>> task, 
        int maxRetries) {
    
    CompletableFuture<T> result = new CompletableFuture<>();
    attempt(task, maxRetries, result);
    return result;
}

private <T> void attempt(
        Supplier<CompletableFuture<T>> task,
        int remaining,
        CompletableFuture<T> result) {
        
    task.get()
        .whenComplete((r, ex) -> {
            if (ex == null) {
                result.complete(r);
            } else if (remaining <= 0) {
                result.completeExceptionally(ex);
            } else {
                // Retry after delay
                ForkJoinPool.commonPool().schedule(
                    () -> attempt(task, remaining - 1, result),
                    1, TimeUnit.SECONDS);
            }
        });
}

// Usage
withRetry(() -> flakyService.call(), 3)
    .thenAccept(System.out::println)
    .exceptionally(ex -> {
        System.err.println("All retries failed");
        return null;
    });
```

## Java 9 Modules (JPMS)

The Java Platform Module System (JPMS), introduced in Java 9, is a fundamental change to the Java platform that brings modularity to the Java SE Platform. It helps developers create more maintainable and scalable applications by enforcing strong encapsulation and explicit dependencies between components.

### Core Concepts

1. **Module**: A self-describing collection of code and data with a unique name
2. **Module Descriptor**: `module-info.java` file that defines a module's characteristics
3. **Module Path**: Replaces the classpath for modules
4. **Automatic Modules**: Legacy JARs that are automatically converted to modules
5. **Unnamed Module**: The module that contains all classes loaded from the classpath

### Module Descriptor Syntax

```java
module com.example.mymodule {
    // Required modules
    requires java.sql;
    requires org.apache.commons.lang3;
    
    // Optional dependencies
    requires static lombok;
    
    // Exported packages (public API)
    exports com.example.mymodule.api;
    exports com.example.mymodule.util to com.example.othermodule;
    
    // Open packages for reflection
    opens com.example.mymodule.internal;
    
    // Service consumption
    uses com.example.spi.ServiceInterface;
    
    // Service provision
    provides com.example.spi.ServiceInterface with
        com.example.mymodule.MyServiceImpl;
}
```

### Key Directives

#### 1. `requires`
```java
requires <module>;           // Mandatory dependency
requires static <module>;    // Optional dependency at runtime
requires transitive <module>; // Transitive dependency
```

#### 2. `exports`
```java
exports <package>;                     // Export to all modules
exports <package> to <module1>, <module2>; // Qualified export
```

#### 3. `opens`
```java
opens <package>;                     // Open for deep reflection
exports <package> to <module1>;      // Open to specific modules
open module com.example.mymodule {   // Open module (all packages)
    // Module directives
}
```

#### 4. Service Loading
```java
uses <service.Interface>;                      // Service consumer
provides <service.Interface> with <impl.Class>; // Service provider
```

### Module Types

1. **Named Modules**: Explicit modules with a module-info.java
2. **Automatic Modules**: Legacy JARs on the module path
3. **Unnamed Module**: The catch-all module for code on the classpath
4. **Platform Modules**: Modules in the Java SE Platform (java.*, jdk.*)

### Module Resolution

1. **Root Modules**: Initial modules specified at launch
2. **Readability**: A module can read another module if it requires it
3. **Accessibility**: A package is accessible if it's exported and the module reading it has the proper requires
4. **Layer**: A configuration of modules with a parent layer

### Migration Strategies

#### 1. Bottom-Up Migration
1. Start with leaf dependencies
2. Add module-info.java to each library
3. Work your way up the dependency tree

#### 2. Top-Down Migration
1. Create an automatic module for each JAR
2. Create module-info.java for your application
3. Gradually modularize dependencies

### Command Line Tools

```bash
# Compile modules
javac -d mods --module-source-path src $(find src -name "*.java")

# Create modular JAR
jar --create --file=mlib/com.example.mymodule@1.0.jar \
    --module-version=1.0 \
    -C mods/com.example.mymodule .

# Run application
java --module-path mods -m com.example.mymodule/com.example.Main

# List all modules
java --list-modules

# Describe a module
java --describe-module java.base

# Print module resolution
java --show-module-resolution -m com.example.mymodule
```

### Best Practices

1. **Strong Encapsulation**:
   - Only export necessary packages
   - Use `exports ... to` for qualified exports
   - Keep implementation packages internal

2. **Dependency Management**:
   - Use `requires static` for optional dependencies
   - Use `requires transitive` for API modules
   - Avoid split packages

3. **Migration**:
   - Start with automatic modules
   - Use `--add-exports` and `--add-opens` for incremental migration
   - Test thoroughly after each change

4. **Naming**:
   - Use reverse domain name pattern
   - Keep module names consistent with the main package

### Example: Multi-Module Project

Project structure:
```
myapp/
├── app/
│   ├── src/
│   │   └── com.example.app/
│   │       └── Main.java
│   └── module-info.java
├── api/
│   ├── src/
│   │   └── com.example.api/
│   │       └── Service.java
│   └── module-info.java
└── impl/
    ├── src/
    │   └── com.example.impl/
    │       └── ServiceImpl.java
    └── module-info.java
```

api/module-info.java:
```java
module com.example.api {
    exports com.example.api;
}
```

impl/module-info.java:
```java
module com.example.impl {
    requires transitive com.example.api;
    provides com.example.api.Service with com.example.impl.ServiceImpl;
}
```

app/module-info.java:
```java
module com.example.app {
    requires com.example.impl;
    uses com.example.api.Service;
}
```

### Advanced Topics

#### 1. Layers
```java
ModuleFinder finder = ModuleFinder.of(path1, path2);
ModuleLayer parent = ModuleLayer.boot();
Configuration cf = parent.configuration().resolve(finder, ModuleFinder.of(), Set.of("com.example.mymodule"));
ModuleLayer layer = parent.defineModulesWithOneLoader(cf, ClassLoader.getSystemClassLoader());
```

#### 2. Multi-Release JARs
```bash
jar --create --file=mlib/mymodule.jar \
    --main-class=com.example.Main \
    -C mods/com.example.main . \
    --release 11 -C mods/com.example.main.jdk11 .
```

#### 3. JLink (Custom Runtime Images)
```bash
jlink --module-path $JAVA_HOME/jmods:mlib \
      --add-modules com.example.app \
      --launcher myapp=com.example.app/com.example.Main \
      --output myapp-runtime
```

### Common Issues and Solutions

1. **Illegal Access Errors**:
   - Add `--add-opens` or `--add-exports`
   - Use `opens` in module-info.java
   - Consider refactoring to use proper APIs

2. **Module Not Found**:
   - Check module path
   - Verify module name in requires
   - Ensure automatic module names are correct

3. **Split Packages**:
   - Refactor to avoid split packages
   - Use `--patch-module` as a temporary workaround

4. **ServiceLoader Issues**:
   - Ensure `provides` and `uses` are correctly specified
   - Check module path includes provider modules

### Migration from Java 8 and Earlier

1. **Classpath to Module Path**:
   - Move JARs to module path
   - Use automatic modules for non-modular JARs
   - Add module-info.java for your code

2. **Reflection**:
   - Add `opens` for packages accessed via reflection
   - Consider using `MethodHandles.privateLookupIn()`

3. **Internal APIs**:
   - Replace usage of internal APIs with public APIs
   - Use `--add-exports` as a temporary measure

### Tools and IDEs

1. **jdeprscan**: Find uses of deprecated APIs
2. **jdeps**: Analyze dependencies
3. **jlink**: Create custom runtime images
4. **jmod**: Work with JMOD files
5. **IDE Support**: IntelliJ IDEA, Eclipse, and NetBeans have excellent module support

### Real-world Example: Modular Spring Boot

```java
open module com.example.springapp {
    requires spring.context;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires static lombok;
    
    opens com.example.springapp to spring.core, spring.beans, spring.context;
}
```

### Performance Considerations

1. **Startup Time**:
   - Modules improve startup time through reliable configuration
   - Class loading is more efficient

2. **Memory Usage**:
   - Strong encapsulation reduces memory footprint
   - Only required modules are resolved and loaded

3. **JIT Optimization**:
   - Better optimization through more precise dependency information
   - Improved method inlining and dead code elimination

### Security Benefits

1. **Strong Encapsulation**:
   - Internal APIs are not accessible by default
   - Reduces attack surface

2. **Dependency Verification**:
   - Explicit dependencies prevent classpath issues
   - No more "JAR hell"

3. **Reduced Surface Area**:
   - Only required modules are available
   - Custom runtime images include only necessary modules

### Future of Java Modules

1. **Project Jigsaw**: The original project that delivered modules
2. **Project JEP 200**: Modular JDK
3. **Project JEP 220**: Modular Run-Time Images
4. **Project JEP 282**: jlink: The Java Linker
5. **Future Enhancements**: Continued improvements in tooling and performance
    exports com.example.mymodule.api;
    
    provides com.example.service.MyService 
        with com.example.mymodule.internal.MyServiceImpl;
}
```

### Q8: What are Java Virtual Threads (Project Loom) and how do they differ from platform threads?

Virtual Threads, introduced as a preview feature in Java 19 and part of Project Loom, are lightweight threads that are managed by the JVM rather than the operating system.

Key differences from platform threads:
- Lightweight: Virtual threads consume much less memory (few KB vs MB for platform threads)
- Scalability: Can create millions of virtual threads vs thousands of platform threads
- Scheduling: Managed by the JVM scheduler, not the OS scheduler
- Blocking: When a virtual thread blocks, only that thread is affected, not the carrier thread

Use cases:
- Server applications handling many concurrent connections
- Applications with high I/O operations
- Replacing complex asynchronous code with simpler synchronous code

Example:
```java
// Creating a platform thread
Thread platformThread = new Thread(() -> {
    System.out.println("Running in platform thread");
});

// Creating a virtual thread (Java 19+)
Thread virtualThread = Thread.startVirtualThread(() -> {
    System.out.println("Running in virtual thread");
});
```

### Q9: What are Java Records and how are they used?

Records, introduced in Java 14 as a preview feature and finalized in Java 16, are a special kind of class that acts as a transparent carrier for immutable data.

Key features:
- Immutable data classes with minimal boilerplate
- Automatically generated equals(), hashCode(), and toString() methods
- Automatically generated constructor and accessor methods
- Cannot extend other classes (implicitly extends Record)
- Can implement interfaces
- Can have static fields and methods
- Can have instance methods but not instance fields beyond the record components

Example:
```java
// Without records
public final class Person {
    private final String name;
    private final int age;
    
    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
    
    public String name() { return name; }
    public int age() { return age; }
    
    // equals, hashCode, toString methods...
}

// With records
public record Person(String name, int age) {}
```

### Q10: What are Java Sealed Classes and how are they used?

Sealed Classes, introduced in Java 15 as a preview feature and finalized in Java 17, restrict which other classes or interfaces may extend or implement them.

Key features:
- Control over class hierarchy
- Explicit declaration of permitted subclasses
- Subclasses must be either final, sealed, or non-sealed
- Work well with pattern matching

Example:
```java
// Sealed class with permitted subclasses
public sealed class Shape permits Circle, Rectangle, Triangle {
    // Common shape methods
}

// Permitted subclasses must be final, sealed, or non-sealed
public final class Circle extends Shape {
    private final double radius;
    
    public Circle(double radius) {
        this.radius = radius;
    }
    private final double height;
    
    // Rectangle implementation
}

public final class Triangle extends Shape {
    private final double a, b, c;
    
    // Triangle implementation
}
```

Sealed classes are particularly useful with pattern matching and switch expressions.