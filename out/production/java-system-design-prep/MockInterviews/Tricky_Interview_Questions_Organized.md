# Tricky Interview Questions and Answers

This document contains solutions to tricky Java interview questions that often catch candidates off-guard.

## Core Java Tricky Questions (Output-Based)

### 1. What will be the output of the following code?
```java
public class TrickyQuestion1 {
    public static void main(String[] args) {
        Integer a = 127;
        Integer b = 127;
        System.out.println(a == b);
        
        Integer c = 128;
        Integer d = 128;
        System.out.println(c == d);
    }
}
```

**Answer:**
```
true
false
```

**Explanation:** Java caches Integer objects with values between -128 and 127 (inclusive). When you create Integer objects within this range using autoboxing, you get the same objects from the cache. For values outside this range, new objects are created.

### 2. Will the following code compile?
```java
public class TrickyQuestion2 {
    public static void main(String[] args) {
        final int[] arr = {1, 2, 3};
        arr[0] = 4; // Line 1
        arr = new int[]{4, 5, 6}; // Line 2
    }
}
```

**Answer:** Line 1 will compile, but Line 2 will not.

**Explanation:** When an array is declared as final, the reference cannot be changed (Line 2 fails), but the contents of the array can be modified (Line 1 works).

### 3. What's the output of this code?
```java
public class TrickyQuestion3 {
    public static void main(String[] args) {
        String s1 = "Java";
        String s2 = "Java";
        String s3 = new String("Java");
        
        System.out.println(s1 == s2);
        System.out.println(s1 == s3);
        System.out.println(s1.equals(s3));
    }
}
```

**Answer:**
```
true
false
true
```

**Explanation:** String literals are stored in the String Pool. s1 and s2 refer to the same object from the pool. s3 is explicitly created as a new object. The == operator compares references, while equals() compares content.

## Collections Framework Tricky Questions

### 4. What happens when you try to add null to a TreeSet?
```java
public class TrickyQuestion4 {
    public static void main(String[] args) {
        Set<String> set = new TreeSet<>();
        set.add(null);
        System.out.println(set);
    }
}
```

**Answer:** NullPointerException will be thrown.

**Explanation:** TreeSet uses comparisons for ordering its elements. When you try to compare null with any other element, a NullPointerException is thrown.

### 5. What's the output of this HashMap code?
```java
public class TrickyQuestion5 {
    public static void main(String[] args) {
        Map<BadKey, String> map = new HashMap<>();
        map.put(new BadKey(1), "One");
        map.put(new BadKey(1), "Another One");
        System.out.println(map.size());
    }
    
    static class BadKey {
        private int id;
        
        public BadKey(int id) {
            this.id = id;
        }
        
        @Override
        public int hashCode() {
            return id;
        }
        
        // Note: equals is not overridden
    }
}
```

**Answer:** 2

**Explanation:** Even though both keys have the same hashCode, the equals() method is not overridden, so HashMap considers them as different keys.

## Multithreading Tricky Questions

### 6. What's wrong with this Singleton?
```java
public class TrickySingleton {
    private static TrickySingleton instance;
    
    private TrickySingleton() {}
    
    public static TrickySingleton getInstance() {
        if (instance == null) {
            instance = new TrickySingleton();
        }
        return instance;
    }
}
```

**Answer:** It's not thread-safe. Multiple threads could create multiple instances.

**Explanation:** Without proper synchronization, multiple threads could simultaneously check if instance is null and create separate instances, violating the singleton pattern.

### 7. What happens when this code runs?
```java
public class TrickyQuestion7 {
    public static void main(String[] args) {
        Thread t = new Thread() {
            public void run() {
                System.out.println("Thread running");
            }
        };
        t.run();
        System.out.println("Main thread");
    }
}
```

**Answer:**
```
Thread running
Main thread
```

**Explanation:** Calling run() directly does not start a new thread; it just calls the method in the current thread. To execute in a separate thread, t.start() should be used.

## Advanced Java Tricky Questions

### 8. What does this code print?
```java
public class TrickyQuestion8 {
    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        numbers.stream()
               .filter(n -> n % 2 == 0)
               .forEach(System.out::println);
        System.out.println(numbers);
    }
}
```

**Answer:**
```
2
4
[1, 2, 3, 4, 5]
```

**Explanation:** Streams don't modify the original collection. They create a new sequence of elements for processing.

### 9. What happens when this executes?
```java
public class TrickyQuestion9 {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("A");
        list.add("B");
        list.add("C");
        
        for (String s : list) {
            if (s.equals("B")) {
                list.remove(s);
            }
        }
        
        System.out.println(list);
    }
}
```

**Answer:** ConcurrentModificationException will be thrown.

**Explanation:** You cannot modify a collection (except through the iterator's remove method) while iterating through it using an enhanced for loop.

### 10. What's wrong with this memory leak example?
```java
public class TrickyMemoryLeak {
    private static final List<Object> CACHE = new ArrayList<>();
    
    public void process(Object obj) {
        // Process the object
        CACHE.add(obj); // Store for future reference
    }
}
```

**Answer:** Objects are added to a static list and never removed, causing a memory leak.

**Explanation:** Using a static collection that grows but never shrinks will eventually lead to OutOfMemoryError as objects cannot be garbage collected as long as they're referenced in the collection.

## Additional Output-Based Questions

### 11. What will be the output of the following code?
```java
public class StringPool {
    public static void main(String[] args) {
        String s1 = new String("hello");
        String s2 = new String("hello");
        String s3 = "hello";
        String s4 = "hello";
        
        System.out.println(s1 == s2);
        System.out.println(s1.equals(s2));
        System.out.println(s3 == s4);
        System.out.println(s1 == s3);
        System.out.println(s1.intern() == s3);
    }
}
```

**Answer:**
```
false
true
true
false
true
```

**Explanation:** 
- s1 and s2 are separate objects created with `new` so `==` returns false, but they have the same content so `equals()` returns true
- s3 and s4 are string literals that refer to the same object in the string pool, so `==` returns true
- s1 is a new object, s3 is from the string pool, so `==` returns false
- `s1.intern()` returns the canonical representation from the string pool which is the same object as s3, so `==` returns true

### 12. What will be the output of the following code?
```java
public class AutoboxingTrick {
    public static void main(String[] args) {
        Integer i1 = 1000;
        Integer i2 = 1000;
        Integer i3 = 100;
        Integer i4 = 100;
        
        System.out.println(i1 == i2);
        System.out.println(i3 == i4);
        
        System.out.println(i1.equals(i2));
        System.out.println(i3.equals(i4));
    }
}
```

**Answer:**
```
false
true
true
true
```

**Explanation:** 
- Java caches Integer objects in the range -128 to 127 (inclusive)
- i1 and i2 are outside this range, so two different objects are created (hence == is false)
- i3 and i4 are within the cache range, so they refer to the same cached object (== is true)
- equals() compares the value, so it returns true in both cases

### 13. What will be the output of the following code?
```java
public class FloatingPoint {
    public static void main(String[] args) {
        double d1 = 0.1 + 0.2;
        double d2 = 0.3;
        
        System.out.println(d1);
        System.out.println(d2);
        System.out.println(d1 == d2);
        
        BigDecimal bd1 = new BigDecimal("0.1").add(new BigDecimal("0.2"));
        BigDecimal bd2 = new BigDecimal("0.3");
        
        System.out.println(bd1);
        System.out.println(bd2);
        System.out.println(bd1.equals(bd2));
    }
}
```

**Answer:**
```
0.30000000000000004
0.3
false
0.3
0.3
true
```

**Explanation:**
- Floating-point arithmetic in Java (and most programming languages) is imprecise due to how binary floating-point works
- 0.1 and 0.2 cannot be precisely represented in binary floating-point, leading to a small error in the sum
- BigDecimal provides precise decimal arithmetic, avoiding floating-point errors

### 14. What will be the output of the following code?
```java
public class StringBuilderOutput {
    public static void main(String[] args) {
        StringBuilder sb1 = new StringBuilder("Java");
        StringBuilder sb2 = new StringBuilder("Java");
        
        System.out.println(sb1 == sb2);
        System.out.println(sb1.equals(sb2));
        System.out.println(sb1.toString().equals(sb2.toString()));
        
        sb1.append(" Interview");
        System.out.println(sb1);
    }
}
```

**Answer:**
```
false
false
true
Java Interview
```

**Explanation:**
- sb1 and sb2 are different objects, so == returns false
- StringBuilder does not override equals(), so it uses the Object.equals() implementation which checks object identity (like ==)
- Converting to String and then using equals() compares the actual content
- StringBuilder is mutable, so append() modifies the original object

### 15. What will be the output of the following code?
```java
public class OverloadingOutput {
    public static void main(String[] args) {
        print(null);
    }
    
    public static void print(Object o) {
        System.out.println("Object");
    }
    
    public static void print(String s) {
        System.out.println("String");
    }
}
```

**Answer:**
```
String
```

**Explanation:**
- When calling print(null), the compiler has to decide which overloaded method to call
- The most specific compatible type is chosen, which is String in this case (String is more specific than Object)
- If we had a print(Integer i) method as well, the call would be ambiguous and would not compile

### 16. What will be the output of the following code?
```java
public class InitializationOrder {
    private static int x = 10;
    
    static {
        x++;
        y++;
    }
    
    private static int y = 10;
    
    public static void main(String[] args) {
        System.out.println(x);
        System.out.println(y);
    }
}
```

**Answer:**
```
11
10
```

**Explanation:**
- Static variables are initialized in the order they appear in the class
- Static blocks are executed in the order they appear
- x is initialized to 10, then the static block executes, incrementing x to 11 and incrementing y (which has default value 0) to 1
- After the static block, y is initialized to 10, overwriting its previous value

### 17. What will be the output of the following code?
```java
public class CollectionsSorting {
    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(5, 2, 8, 1, 3);
        Collections.sort(numbers, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2.compareTo(o1); // Reverse order
            }
        });
        
        System.out.println(numbers);
        
        List<Integer> moreNumbers = Arrays.asList(5, 2, 8, 1, 3);
        moreNumbers.sort((a, b) -> a - b); // Warning: potential integer overflow
        
        System.out.println(moreNumbers);
    }
}
```

**Answer:**
```
[8, 5, 3, 2, 1]
[1, 2, 3, 5, 8]
```

**Explanation:**
- First list uses a custom Comparator that reverses the natural ordering (largest to smallest)
- Second list uses a lambda that implements ascending order (smallest to largest)
- The lambda (a, b) -> a - b can have integer overflow issues with large numbers, but works for this small example

### 18. What will be the output of the following code with exception handling?
```java
public class ExceptionOutput {
    public static void main(String[] args) {
        try {
            System.out.println("1");
            throw new RuntimeException();
        } catch (Exception e) {
            System.out.println("2");
            return;
        } finally {
            System.out.println("3");
        }
        
        System.out.println("4");
    }
}
```

**Answer:**
```
1
2
3
```

**Explanation:**
- "1" is printed, then an exception is thrown
- The catch block executes, printing "2"
- The finally block always executes, printing "3"
- The return statement in the catch block prevents "4" from being printed
- If there was no return in catch, "4" would be printed after "3"

### 19. What will be the output of this generics-related code?
```java
public class GenericsOutput {
    public static void main(String[] args) {
        List<? extends Number> list1 = new ArrayList<Integer>();
        List<? super Integer> list2 = new ArrayList<Number>();
        
        // list1.add(1); // Will this compile?
        list2.add(1); // This works
        
        List<Integer> integers = Arrays.asList(1, 2, 3);
        List<Double> doubles = Arrays.asList(1.1, 2.2, 3.3);
        
        printList(integers);
        printList(doubles);
    }
    
    public static void printList(List<? extends Number> list) {
        for (Number n : list) {
            System.out.print(n + " ");
        }
        System.out.println();
    }
}
```

**Answer:**
```
1 2 3 
1.1 2.2 3.3 
```

**Explanation:**
- `List<? extends Number>` can hold any list whose element type is Number or a subclass of Number, but you can't add to it because the compiler doesn't know which specific subtype it holds
- `List<? super Integer>` can hold any list whose element type is Integer or a superclass of Integer, and you can add Integer objects to it
- The commented line `list1.add(1)` would not compile because you cannot add elements to a list with an unknown extends-bounded type parameter
- The printList method accepts both List<Integer> and List<Double> because both Integer and Double extend Number

### 20. What will be the output of this varargs code?
```java
public class VarargsOutput {
    public static void main(String[] args) {
        test(1);
        test(1, 2);
        test(1, 2, 3);
        test();
    }
    
    public static void test(int... nums) {
        System.out.print("Count: " + nums.length + " Values: ");
        for (int num : nums) {
            System.out.print(num + " ");
        }
        System.out.println();
    }
}
```

**Answer:**
```
Count: 1 Values: 1 
Count: 2 Values: 1 2 
Count: 3 Values: 1 2 3 
Count: 0 Values: 
```

**Explanation:**
- Varargs (variable-length arguments) allow a method to accept zero or more arguments
- Internally, varargs are implemented as an array
- The length property gives the number of arguments passed
- You can pass zero arguments, which results in an empty array

# 200+ Java Tricky Questions Collection

This extensive collection of tricky Java interview questions is organized by category. Each section contains questions that focus on specific Java concepts and behaviors that are commonly tested in technical interviews.

## Table of Contents
1. [Core Java - Basic Output Questions](#core-java---basic-output-questions)
2. [String Manipulation](#string-manipulation)
3. [Collections and Generics](#collections-and-generics)
4. [Multithreading Edge Cases](#multithreading-edge-cases)
5. [Exception Handling](#exception-handling)
6. [Java Memory Model](#java-memory-model)
7. [Java 8+ Features](#java-8-features)
8. [Design Patterns Traps](#design-patterns-traps)
9. [Inheritance and Polymorphism](#inheritance-and-polymorphism)
10. [Final, Finally, Finalize](#final-finally-finalize)

## Core Java - Basic Output Questions

### 21. What will be the output of the following bitwise operations?
```java
public class BitwiseOperators {
    public static void main(String[] args) {
        int a = 5;  // 0101 in binary
        int b = 3;  // 0011 in binary
        
        System.out.println("a & b = " + (a & b));   // AND
        System.out.println("a | b = " + (a | b));   // OR
        System.out.println("a ^ b = " + (a ^ b));   // XOR
        System.out.println("~a = " + (~a));        // NOT
        System.out.println("a << 1 = " + (a << 1)); // Left shift
        System.out.println("a >> 1 = " + (a >> 1)); // Right shift
    }
}
```

**Answer:**
```
a & b = 1
a | b = 7
a ^ b = 6
~a = -6
a << 1 = 10
a >> 1 = 2
```

**Explanation:**
- a & b: 0101 & 0011 = 0001 (1 in decimal)
- a | b: 0101 | 0011 = 0111 (7 in decimal)
- a ^ b: 0101 ^ 0011 = 0110 (6 in decimal)
- ~a: ~0101 = 1010 with sign extension = -6 (in two's complement)
- a << 1: 0101 << 1 = 1010 (10 in decimal), equivalent to multiplying by 2
- a >> 1: 0101 >> 1 = 0010 (2 in decimal), equivalent to dividing by 2

### 22. What is the result of the following operations with char?
```java
public class CharOperations {
    public static void main(String[] args) {
        char a = 'a';
        char b = 'b';
        
        System.out.println(a + b);
        System.out.println("" + a + b);
        System.out.println(a - 'a');
        System.out.println((char)(a + 1));
    }
}
```

**Answer:**
```
195
ab
0
b
```

**Explanation:**
- a + b: When chars are used in arithmetic operations, they're promoted to int. 'a' is ASCII 97, 'b' is ASCII 98, so 97 + 98 = 195
- "" + a + b: String concatenation takes precedence, so 'a' and 'b' are converted to strings, resulting in "ab"
- a - 'a': ASCII 97 - ASCII 97 = 0
- (char)(a + 1): 'a' + 1 = 98, which is cast back to char, resulting in 'b'

### 23. What will happen when this code runs?
```java
public class DivisionByZero {
    public static void main(String[] args) {
        System.out.println(5/0);
        System.out.println(5.0/0.0);
    }
}
```

**Answer:**
The first line throws an `ArithmeticException: / by zero`, so the second line is never reached. If executed separately, the second line would print `Infinity`.

**Explanation:**
- Integer division by zero causes an ArithmeticException
- Floating-point division by zero results in special values: Infinity, -Infinity, or NaN, following IEEE 754 standard

### 24. What's the output of this increment/decrement code?
```java
public class IncrementDecrement {
    public static void main(String[] args) {
        int i = 10;
        System.out.println(i++);
        System.out.println(++i);
        System.out.println(i--);
        System.out.println(--i);
        
        int j = 20;
        int k = j++ + ++j;
        System.out.println("j = " + j + ", k = " + k);
    }
}
```

**Answer:**
```
10
12
12
10
j = 22, k = 42
```

**Explanation:**
- i++ is a post-increment: the original value (10) is returned, then i becomes 11
- ++i is a pre-increment: i becomes 12, then 12 is returned
- i-- is a post-decrement: the original value (12) is returned, then i becomes 11
- --i is a pre-decrement: i becomes 10, then 10 is returned
- j++ + ++j: j is first 20, post-incremented to 21, then pre-incremented to 22. So the expression is 20 + 22 = 42, and j ends up as 22

### 25. How does this enum behave?
```java
public class EnumTest {
    enum Day { MONDAY, TUESDAY, WEDNESDAY }
    
    public static void main(String[] args) {
        Day day = Day.MONDAY;
        switch(day) {
            case MONDAY:
                System.out.println("Monday");
                // No break statement
            case TUESDAY:
                System.out.println("Tuesday");
                break;
            case WEDNESDAY:
                System.out.println("Wednesday");
                break;
        }
        
        System.out.println(Day.MONDAY.ordinal());
        System.out.println(Day.WEDNESDAY.ordinal());
    }
}
```

**Answer:**
```
Monday
Tuesday
0
2
```

**Explanation:**
- In the switch case, there's no break after MONDAY case, causing fall-through to TUESDAY case
- ordinal() returns the position of an enum constant (0-based), so MONDAY is 0 and WEDNESDAY is 2

## String Manipulation

### 26. What's the result of String substring operations?
```java
public class SubstringTrick {
    public static void main(String[] args) {
        String str = "HelloWorld";
        System.out.println(str.substring(5));
        System.out.println(str.substring(0, 5));
        System.out.println(str.substring(5, 5));
        // System.out.println(str.substring(5, 4)); // What happens?
    }
}
```

**Answer:**
```
World
Hello

```

**Explanation:**
- substring(5) returns the substring from index 5 to the end: "World"
- substring(0, 5) returns the substring from index 0 (inclusive) to 5 (exclusive): "Hello"
- substring(5, 5) returns an empty string because the start and end are the same
- substring(5, 4) would throw StringIndexOutOfBoundsException because end index can't be less than start index

### 27. What happens with this regular expression?
```java
public class RegexTrick {
    public static void main(String[] args) {
        String text = "The quick brown fox jumps over the lazy dog";
        
        // Split by whitespace
        String[] words1 = text.split("\\s");
        System.out.println("Words1 length: " + words1.length);
        
        // Split by whitespace with limit
        String[] words2 = text.split("\\s", 3);
        System.out.println("Words2 length: " + words2.length);
        for (String word : words2) {
            System.out.println(word);
        }
    }
}
```

**Answer:**
```
Words1 length: 9
Words2 length: 3
The
quick
brown fox jumps over the lazy dog
```

**Explanation:**
- split("\\s") splits the string by whitespace, resulting in 9 words
- split("\\s", 3) splits the string by whitespace but with a limit of 3, meaning only 2 splits are performed, resulting in 3 parts

### 28. How does StringBuilder perform compared to String concatenation?
```java
public class StringVsBuilder {
    public static void main(String[] args) {
        String s = "";
        long startTime = System.nanoTime();
        for (int i = 0; i < 10000; i++) {
            s += "a";
        }
        long endTime = System.nanoTime();
        System.out.println("String concatenation: " + (endTime - startTime) / 1000000 + " ms");
        
        StringBuilder sb = new StringBuilder();
        startTime = System.nanoTime();
        for (int i = 0; i < 10000; i++) {
            sb.append("a");
        }
        endTime = System.nanoTime();
        System.out.println("StringBuilder: " + (endTime - startTime) / 1000000 + " ms");
    }
}
```

**Answer:**
Exact times will vary, but StringBuilder will be significantly faster (potentially 1000x or more).

**Explanation:**
- String is immutable, so each concatenation creates a new String object
- StringBuilder is mutable, so it's much more efficient for multiple concatenations
- The difference becomes more dramatic as the number of concatenations increases

## Collections and Generics

### 29. What happens when modifying a list while iterating?
```java
public class ConcurrentModification {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("A");
        list.add("B");
        list.add("C");
        
        // Using iterator
        Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()) {
            String item = iterator.next();
            if (item.equals("B")) {
                // list.remove(item); // What happens?
                iterator.remove(); // This is safe
            }
        }
        System.out.println(list);
        
        // Using for-each
        list = new ArrayList<>(Arrays.asList("A", "B", "C"));
        try {
            for (String item : list) {
                if (item.equals("B")) {
                    list.remove(item);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getClass().getSimpleName());
        }
    }
}
```

**Answer:**
```
[A, C]
ConcurrentModificationException
```

**Explanation:**
- Removing elements directly from a collection while iterating with for-each will throw ConcurrentModificationException
- Using Iterator's remove() method is the safe way to remove elements during iteration

### 30. What's the output of this HashMap behavior?
```java
public class HashMapTrick {
    public static void main(String[] args) {
        Map<Person, String> map = new HashMap<>();
        Person p1 = new Person("John", 25);
        Person p2 = new Person("John", 25);
        
        map.put(p1, "Person One");
        map.put(p2, "Person Two");
        
        System.out.println(map.size());
        System.out.println(map.get(new Person("John", 25)));
    }
    
    static class Person {
        String name;
        int age;
        
        Person(String name, int age) {
            this.name = name;
            this.age = age;
        }
        
        // No hashCode() and equals() overridden
    }
}
```

**Answer:**
```
2
null
```

**Explanation:**
- Without overriding hashCode() and equals(), two Person objects with the same name and age are considered different objects
- HashMap uses hashCode() for initial bucketing and equals() for comparing keys
- Since hashCode() is not overridden, each Person object gets a unique hash based on its memory address, resulting in different entries

## Multithreading Edge Cases

### 31. What happens with this volatile variable?
```java
public class VolatileMisuse {
    private static volatile int counter = 0;
    
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                counter++;
            }
        });
        
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                counter++;
            }
        });
        
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        
        System.out.println("Final counter value: " + counter);
    }
}
```

**Answer:**
The final counter value will be less than 2000, varying with each run.

**Explanation:**
- volatile ensures visibility of changes across threads but doesn't provide atomicity for compound operations like counter++
- counter++ is actually three operations: read counter, increment value, write back
- This can lead to race conditions when two threads perform these operations interleaved
- To fix this, we need atomic operations or synchronization

### 32. What happens with this wait/notify code?
```java
public class WaitNotifyTest {
    public static void main(String[] args) {
        Object lock = new Object();
        
        Thread waiter = new Thread(() -> {
            synchronized (lock) {
                try {
                    System.out.println("Waiter waiting");
                    lock.wait();
                    System.out.println("Waiter notified");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        
        Thread notifier = new Thread(() -> {
            synchronized (lock) {
                System.out.println("Notifier notifying");
                lock.notify();
                // Long operation after notify
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {}
                System.out.println("Notifier done");
            }
        });
        
        waiter.start();
        // Small delay to ensure waiter starts first
        try { Thread.sleep(100); } catch (InterruptedException e) {}
        notifier.start();
    }
}
```

**Answer:**
```
Waiter waiting
Notifier notifying
Notifier done
Waiter notified
```

**Explanation:**
- Even though notify() is called, the waiter thread can't proceed until the notifier thread releases the lock
- The notifier thread holds the lock until it completes all the code in its synchronized block, including the sleep
- This demonstrates that notify() doesn't immediately transfer control to the waiting thread

## Exception Handling

### 33. What happens with this multi-catch block?
```java
public class MultiCatchTest {
    public static void main(String[] args) {
        try {
            int[] arr = {1, 2, 3};
            System.out.println(arr[5]); // ArrayIndexOutOfBoundsException
            System.out.println(10/0);   // ArithmeticException (never reached)
        } catch (ArrayIndexOutOfBoundsException | ArithmeticException e) {
            System.out.println("Exception caught: " + e.getClass().getSimpleName());
            System.out.println(e instanceof RuntimeException);
        }
    }
}
```

**Answer:**
```
Exception caught: ArrayIndexOutOfBoundsException
true
```

**Explanation:**
- When an ArrayIndexOutOfBoundsException is thrown, execution jumps to the catch block
- The ArithmeticException code is never reached
- Multi-catch treats the caught exception as its actual type (ArrayIndexOutOfBoundsException)
- All exception types in a multi-catch must be related, and the variable (e) is effectively final

### 34. What's the output of this finally block behavior?
```java
public class FinallyReturn {
    public static void main(String[] args) {
        System.out.println(test());
    }
    
    public static int test() {
        try {
            return 1;
        } catch (Exception e) {
            return 2;
        } finally {
            // What happens?
            return 3;
        }
    }
}
```

**Answer:**
```
3
```

**Explanation:**
- finally block always executes, even after a return statement in try or catch
- If finally has a return statement, it overrides any return value from try or catch blocks
- This is generally considered bad practice as it makes code flow harder to understand

## Java Memory Model

### 35. What happens with this memory reference?
```java
public class MemoryReference {
    public static void main(String[] args) {
        Integer a = 127;
        Integer b = 127;
        System.out.println(a == b);
        
        Integer c = new Integer(127); // Deprecated but illustrative
        Integer d = new Integer(127);
        System.out.println(c == d);
        
        Integer e = Integer.valueOf(127);
        Integer f = Integer.valueOf(127);
        System.out.println(e == f);
        
        Integer g = 200;
        Integer h = 200;
        System.out.println(g == h);
    }
}
```

**Answer:**
```
true
false
true
false
```

**Explanation:**
- Java caches Integer objects for values between -128 and 127 by default
- a and b reference the same cached Integer object
- c and d are explicitly created as different objects using the constructor
- e and f use valueOf() which uses the cache for values in -128 to 127 range
- g and h are outside the cache range, so they reference different objects

## Java 8+ Features

### 36. What happens in this stream operation?
```java
public class StreamShortCircuit {
    public static void main(String[] args) {
        List<String> list = Arrays.asList("A", "B", "C", "D", "E");
        
        list.stream()
            .filter(s -> {
                System.out.println("Filter: " + s);
                return s.startsWith("C") || s.startsWith("D");
            })
            .findFirst()
            .ifPresent(s -> System.out.println("Result: " + s));
    }
}
```

**Answer:**
```
Filter: A
Filter: B
Filter: C
Result: C
```

**Explanation:**
- Stream operations are lazy and use short-circuit evaluation when possible
- findFirst() is a short-circuiting terminal operation that stops processing when a result is found
- After finding "C" which passes the filter, the stream stops and doesn't process "D" or "E"

### 37. What's the output of this method reference code?
```java
public class MethodReferenceTest {
    public static void main(String[] args) {
        List<String> list = Arrays.asList("apple", "banana", "cherry");
        
        // Using lambda
        list.forEach(s -> System.out.print(s.toUpperCase() + " "));
        System.out.println();
        
        // Using method reference
        list.forEach(System.out::println);
        
        // Using custom method reference
        list.forEach(MethodReferenceTest::customPrint);
    }
    
    public static void customPrint(String s) {
        System.out.print(s.charAt(0) + "* ");
    }
}
```

**Answer:**
```
APPLE BANANA CHERRY 
apple
banana
cherry
a* b* c* 
```

**Explanation:**
- The lambda explicitly calls toUpperCase() and prints with spaces
- The System.out::println method reference prints each item on a new line
- The customPrint method reference prints the first character followed by "* "
- Method references are shorthand for lambdas when simply passing the parameter to a method

### 38. What happens with this parallel stream operation?
```java
public class ParallelStreamOrder {
    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        
        System.out.println("Sequential stream:");
        list.stream()
            .forEach(n -> System.out.print(n + " "));
        
        System.out.println("\nParallel stream:");
        list.parallelStream()
            .forEach(n -> System.out.print(n + " "));
        
        System.out.println("\nParallel stream with forEachOrdered:");
        list.parallelStream()
            .forEachOrdered(n -> System.out.print(n + " "));
    }
}
```

**Answer:**
```
Sequential stream:
1 2 3 4 5 6 7 8 9 10 
Parallel stream:
[output in random order, e.g., 6 9 5 8 3 1 4 10 7 2]
Parallel stream with forEachOrdered:
1 2 3 4 5 6 7 8 9 10 
```

**Explanation:**
- Sequential stream processes elements in the order they appear in the source
- Parallel stream processes elements in parallel, so the order is non-deterministic when using forEach()
- forEachOrdered() preserves the original order even with parallel streams, but may reduce performance benefits of parallelism

## Design Patterns Traps

### 39. What's wrong with this lazy initialization?
```java
public class LazyInitialization {
    private static LazyInitialization instance;
    
    private LazyInitialization() {}
    
    // Not thread-safe
    public static LazyInitialization getInstance() {
        if (instance == null) {
            instance = new LazyInitialization();
        }
        return instance;
    }
    
    // Thread-safe but inefficient
    public static synchronized LazyInitialization getInstanceSync() {
        if (instance == null) {
            instance = new LazyInitialization();
        }
        return instance;
    }
    
    // Double-checked locking
    public static LazyInitialization getInstanceDCL() {
        if (instance == null) { // First check (no lock)
            synchronized(LazyInitialization.class) {
                if (instance == null) { // Second check (with lock)
                    instance = new LazyInitialization();
                }
            }
        }
        return instance;
    }
}
```

**Explanation:**
- The first method isn't thread-safe; multiple threads could create different instances
- The second method is thread-safe but inefficient; it locks on every call, even after initialization
- The third method uses double-checked locking (DCL) for efficiency, but without volatile, it could still have issues with instruction reordering
- The best practice is to use either a static holder class, enum singleton, or double-checked locking with volatile

### 40. What's the output of this factory pattern example?
```java
public class FactoryTrick {
    interface Animal { void speak(); }
    
    static class Dog implements Animal {
        public void speak() { System.out.println("Woof!"); }
    }
    
    static class Cat implements Animal {
        public void speak() { System.out.println("Meow!"); }
    }
    
    static class AnimalFactory {
        public static Animal getAnimal(String type) {
            if ("dog".equalsIgnoreCase(type)) {
                return new Dog();
            } else if ("cat".equalsIgnoreCase(type)) {
                return new Cat();
            }
            return null;
        }
    }
    
    public static void main(String[] args) {
        // What happens if we pass null?
        Animal animal = AnimalFactory.getAnimal(null);
        if (animal != null) {
            animal.speak();
        } else {
            System.out.println("Unknown animal type");
        }
    }
}
```

**Answer:**
```
Unknown animal type
```

**Explanation:**
- When null is passed to getAnimal(), neither condition in the if-else chain is satisfied
- The method returns null, so the null check in main() prevents a NullPointerException
- This demonstrates the importance of proper null handling in factory methods
