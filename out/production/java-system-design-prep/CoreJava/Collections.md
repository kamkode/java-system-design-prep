# Collections Framework Interview Questions and Answers

This document contains common interview questions related to the Java Collections Framework, including lists, sets, maps, queues, and their implementations.

## Table of Contents
1. [Collections Framework Overview](#q1-what-is-the-java-collections-framework)
2. [Collection Interfaces](#q2-what-are-the-main-interfaces-in-the-java-collections-framework)
3. [List Interface](#q3-what-is-the-list-interface-and-its-implementations)
4. [Set Interface](#q4-what-is-the-set-interface-and-its-implementations)
5. [Map Interface](#q5-what-is-the-map-interface-and-its-implementations)
6. [Queue Interface](#q6-what-is-the-queue-interface-and-its-implementations)
7. [ArrayList vs LinkedList](#q7-what-is-the-difference-between-arraylist-and-linkedlist)
8. [HashSet vs TreeSet](#q8-what-is-the-difference-between-hashset-and-treeset)
9. [HashMap vs Hashtable](#q9-what-is-the-difference-between-hashmap-and-hashtable)
10. [Concurrent Collections](#q10-what-are-concurrent-collections-in-java)
11. [Comparable vs Comparator](#q11-what-is-the-difference-between-comparable-and-comparator)
12. [Fail-Fast vs Fail-Safe Iterators](#q12-what-are-fail-fast-and-fail-safe-iterators)
13. [Collections Utility Class](#q13-what-is-the-collections-utility-class)
14. [Generics in Collections](#q14-how-are-generics-used-in-collections)
15. [Performance Considerations](#q15-what-are-the-performance-characteristics-of-different-collection-implementations)
16. [Concurrent Collections in Detail](#q16-what-are-concurrent-collections-in-java)
17. [Fail-Fast vs Fail-Safe Iterators](#q17-what-are-fail-fast-and-fail-safe-iterators)
18. [Collections Utility Class](#q18-what-is-the-collections-utility-class)
19. [Java 8+ Features in Collections](#q19-what-are-the-key-java-8-features-in-collections)
20. [Common Pitfalls and Best Practices](#q20-what-are-common-pitfalls-and-best-practices-when-using-collections)

### Q1: What is the Java Collections Framework?

The Java Collections Framework (JCF) is a unified architecture for representing and manipulating collections in Java. It provides a set of interfaces, implementations, and algorithms that allow developers to work with groups of objects.

**Key components of the Collections Framework**:

1. **Interfaces**: Define the abstract data types that represent collections
   - Core interfaces: Collection, List, Set, Queue, Deque, Map
   - Legacy interfaces: Enumeration, Dictionary, Vector, Stack, Hashtable

2. **Implementations**: Concrete classes that implement the collection interfaces
   - General-purpose implementations: ArrayList, LinkedList, HashSet, TreeSet, HashMap, TreeMap
   - Special-purpose implementations: EnumSet, WeakHashMap, IdentityHashMap
   - Concurrent implementations: ConcurrentHashMap, CopyOnWriteArrayList

3. **Algorithms**: Static methods that perform useful operations on collections
   - Sorting, searching, shuffling, reversing
   - Finding min/max values, frequency counting
   - Making collections unmodifiable or synchronized

**Benefits of the Collections Framework**:

1. **Reduced Programming Effort**: Provides ready-to-use data structures and algorithms
2. **Increased Performance**: High-performance implementations of data structures
3. **Interoperability**: Common interfaces allow collections to work together
4. **Reduced Effort to Learn and Use**: Consistent API across all collections
5. **Reusability**: Generic implementations that can be used with any data type

**Hierarchy of Collection Interfaces**:

```
                  Collection
                      |
        +-------------+-------------+
        |             |             |
       List           Set          Queue
        |             |             |
   +----+----+    +---+---+    +---+---+
   |         |    |       |    |       |
ArrayList LinkedList HashSet TreeSet PriorityQueue Deque
                                        |
                                    ArrayDeque
```

**Map Interface Hierarchy** (separate from Collection):

```
                    Map
                     |
        +------------+------------+
        |            |            |
    HashMap       TreeMap      Hashtable
        |
 LinkedHashMap
```

Example of using collections:
```java
import java.util.*;

public class CollectionsExample {
    public static void main(String[] args) {
        // List example
        List<String> list = new ArrayList<>();
        list.add("Apple");
        list.add("Banana");
        list.add("Cherry");
        System.out.println("List: " + list);  // [Apple, Banana, Cherry]
        
        // Set example
        Set<Integer> set = new HashSet<>();
        set.add(1);
        set.add(2);
        set.add(1);  // Duplicate, won't be added
        System.out.println("Set: " + set);  // [1, 2]
        
        // Map example
        Map<String, Integer> map = new HashMap<>();
        map.put("Apple", 10);
        map.put("Banana", 20);
        map.put("Cherry", 30);
        System.out.println("Map: " + map);  // {Apple=10, Banana=20, Cherry=30}
        
        // Queue example
        Queue<String> queue = new LinkedList<>();
        queue.offer("First");
        queue.offer("Second");
        queue.offer("Third");
        System.out.println("Queue: " + queue);  // [First, Second, Third]
        System.out.println("Removed: " + queue.poll());  // First
    }
}
```

### Q2: What are the main interfaces in the Java Collections Framework?

The Java Collections Framework is built around a set of interfaces that define the functionality of different types of collections. These interfaces form a hierarchy that allows for a consistent and interoperable API.

**Core Collection Interfaces**:

1. **Collection<E>**:
   - The root interface in the collection hierarchy
   - Represents a group of objects known as elements
   - Basic operations: add, remove, contains, size, isEmpty, iterator
   - Extended by List, Set, and Queue

2. **List<E>**:
   - An ordered collection (sequence)
   - Elements can be accessed by their integer index
   - May contain duplicate elements
   - Examples: ArrayList, LinkedList, Vector

3. **Set<E>**:
   - A collection that cannot contain duplicate elements
   - Models the mathematical set abstraction
   - Examples: HashSet, TreeSet, LinkedHashSet

4. **Queue<E>**:
   - A collection designed for holding elements prior to processing
   - Typically ordered in FIFO (first-in-first-out) manner
   - Examples: LinkedList, PriorityQueue

5. **Deque<E>**:
   - A double-ended queue that supports element insertion and removal at both ends
   - Can be used as both FIFO (queue) and LIFO (stack)
   - Examples: ArrayDeque, LinkedList

6. **ConcurrentHashMap**:
   - Thread-safe implementation designed for high concurrency
   - Divides the map into segments for better performance during concurrent access
   - Does not allow null keys or values
   - Better performance than Hashtable for concurrent access
   - Since Java 8, uses a different strategy with finer-grained locking

7. **WeakHashMap**:
   - Keys are stored as weak references
   - Entries are automatically removed when keys are no longer referenced
   - Useful for implementing caches or mappings where garbage collection should not be prevented

8. **IdentityHashMap**:
   - Uses reference equality (==) instead of object equality (equals())
   - Useful when maintaining a separate copy of an object graph
   - Not meant for general-purpose use

9. **EnumMap**:
   - Specialized Map implementation for enum keys
   - Very compact and efficient implementation
   - All keys must be of the same enum type
   - Does not allow null keys

6. **Map<K,V>**:
   - An object that maps keys to values
   - Cannot contain duplicate keys
   - Each key can map to at most one value
   - Not a true collection but part of the Collections Framework
   - Examples: HashMap, TreeMap, LinkedHashMap

**Specialized Interfaces**:

1. **SortedSet<E>**:
   - A Set that maintains its elements in ascending order
   - Elements must implement Comparable or use a Comparator
   - Example: TreeSet

2. **NavigableSet<E>**:
   - A SortedSet with navigation methods (finding closest matches)
   - Example: TreeSet

3. **SortedMap<K,V>**:
   - A Map that maintains its keys in ascending order
   - Keys must implement Comparable or use a Comparator
   - Example: TreeMap

4. **NavigableMap<K,V>**:
   - A SortedMap with navigation methods
   - Example: TreeMap

5. **BlockingQueue<E>**:
   - A Queue that supports operations that wait for the queue to become non-empty or non-full
   - Used for producer-consumer patterns
   - Examples: ArrayBlockingQueue, LinkedBlockingQueue

**Legacy Interfaces**:

1. **Enumeration<E>**:
   - An older version of Iterator
   - Used by legacy classes like Vector and Hashtable

2. **Dictionary<K,V>**:
   - An abstract class representing a key-value mapping
   - Superseded by Map
   - Example: Hashtable

**Example of using different interfaces**:
```java
import java.util.*;

public class CollectionInterfacesExample {
    public static void main(String[] args) {
        // Collection interface
        Collection<String> collection = new ArrayList<>();
        collection.add("One");
        collection.add("Two");
        collection.add("Three");
        System.out.println("Collection: " + collection);
        
        // List interface
        List<String> list = new ArrayList<>();
        list.add("Apple");
        list.add("Banana");
        list.add(1, "Orange");  // Insert at index 1
        System.out.println("List: " + list);
        System.out.println("Element at index 1: " + list.get(1));
        
        // Set interface
        Set<Integer> set = new HashSet<>();
        set.add(1);
        set.add(2);
        set.add(1);  // Duplicate, won't be added
        System.out.println("Set: " + set);
        System.out.println("Contains 2? " + set.contains(2));
        
        // Queue interface
        Queue<String> queue = new LinkedList<>();
        queue.offer("First");
        queue.offer("Second");
        System.out.println("Queue: " + queue);
        System.out.println("Peek: " + queue.peek());  // View head without removing
        System.out.println("Poll: " + queue.poll());  // Remove and return head
        
        // Deque interface
        Deque<String> deque = new ArrayDeque<>();
        deque.addFirst("First");
        deque.addLast("Last");
        System.out.println("Deque: " + deque);
        System.out.println("First element: " + deque.getFirst());
        System.out.println("Last element: " + deque.getLast());
        
        // Map interface
        Map<String, Integer> map = new HashMap<>();
        map.put("One", 1);
        map.put("Two", 2);
        map.put("Three", 3);
        System.out.println("Map: " + map);
        System.out.println("Value for 'Two': " + map.get("Two"));
        System.out.println("Keys: " + map.keySet());
        System.out.println("Values: " + map.values());
        
        // SortedSet interface
        SortedSet<String> sortedSet = new TreeSet<>();
        sortedSet.add("C");
        sortedSet.add("A");
        sortedSet.add("B");
        System.out.println("SortedSet: " + sortedSet);  // [A, B, C]
        System.out.println("First: " + sortedSet.first());
        System.out.println("Last: " + sortedSet.last());
        
        // SortedMap interface
        SortedMap<Integer, String> sortedMap = new TreeMap<>();
        sortedMap.put(3, "Three");
        sortedMap.put(1, "One");
        sortedMap.put(2, "Two");
        System.out.println("SortedMap: " + sortedMap);  // {1=One, 2=Two, 3=Three}
        System.out.println("First key: " + sortedMap.firstKey());
        System.out.println("Last key: " + sortedMap.lastKey());
    }
}
```

### Q3: What is the List interface and its implementations?

The List interface in Java represents an ordered collection (sequence) of elements. It extends the Collection interface and adds methods for positional access, search, iteration, and range operations.

**Key characteristics of List**:

1. **Ordered**: Elements maintain their insertion order
2. **Indexed**: Elements can be accessed by their integer index (position)
3. **Allows Duplicates**: The same element can appear multiple times in a list
4. **Allows null**: Can contain null elements (implementation dependent)

**Key methods in the List interface**:

1. **Positional Access**:
   - `get(int index)`: Returns the element at the specified position
   - `set(int index, E element)`: Replaces the element at the specified position
   - `add(int index, E element)`: Inserts an element at the specified position
   - `remove(int index)`: Removes the element at the specified position

2. **Search**:
   - `indexOf(Object o)`: Returns the index of the first occurrence of the specified element
   - `lastIndexOf(Object o)`: Returns the index of the last occurrence of the specified element

3. **Range Operations**:
   - `subList(int fromIndex, int toIndex)`: Returns a view of the portion of this list

4. **List Iterators**:
   - `listIterator()`: Returns a list iterator over the elements
   - `listIterator(int index)`: Returns a list iterator starting at the specified position

**Main implementations of the List interface**:

1. **ArrayList**:
   - Backed by a resizable array
   - Fast random access (constant time)
   - Slow insertions and deletions in the middle (elements need to be shifted)
   - Good for scenarios with frequent access and infrequent modifications
   - Not synchronized (not thread-safe)

2. **LinkedList**:
   - Implemented as a doubly-linked list
   - Slow random access (linear time)
   - Fast insertions and deletions (constant time once the position is found)
   - Implements both List and Deque interfaces
   - Good for scenarios with frequent modifications
   - Not synchronized (not thread-safe)

3. **Vector**:
   - Legacy class, similar to ArrayList but synchronized (thread-safe)
   - Less efficient than ArrayList due to synchronization overhead
   - Generally avoided in favor of ArrayList or concurrent alternatives

4. **Stack**:
   - Legacy class that extends Vector
   - Represents a last-in-first-out (LIFO) stack of objects
   - Generally avoided in favor of ArrayDeque

5. **CopyOnWriteArrayList**:
   - Thread-safe variant of ArrayList
   - All mutative operations (add, set, remove, etc.) create a fresh copy of the underlying array
   - Designed for cases where reads vastly outnumber writes
   - Provides good concurrency for traversal operations

**Example of using different List implementations**:
```java
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class ListImplementationsExample {
    public static void main(String[] args) {
        // ArrayList example
        List<String> arrayList = new ArrayList<>();
        arrayList.add("Apple");
        arrayList.add("Banana");
        arrayList.add("Cherry");
        arrayList.add(1, "Orange");  // Insert at index 1
        System.out.println("ArrayList: " + arrayList);
        System.out.println("Element at index 2: " + arrayList.get(2));
        arrayList.remove(0);  // Remove first element
        System.out.println("After removal: " + arrayList);
        
        // LinkedList example
        List<String> linkedList = new LinkedList<>();
        linkedList.add("Red");
        linkedList.add("Green");
        linkedList.add("Blue");
        System.out.println("\nLinkedList: " + linkedList);
        
        // Using LinkedList as a Queue
        LinkedList<String> linkedListAsQueue = (LinkedList<String>) linkedList;
        linkedListAsQueue.addFirst("Yellow");  // Add to the beginning
        linkedListAsQueue.addLast("Purple");   // Add to the end
        System.out.println("LinkedList as Queue: " + linkedListAsQueue);
        System.out.println("First element: " + linkedListAsQueue.getFirst());
        System.out.println("Last element: " + linkedListAsQueue.getLast());
        
        // Vector example (legacy, synchronized)
        List<String> vector = new Vector<>();
        vector.add("One");
        vector.add("Two");
        vector.add("Three");
        System.out.println("\nVector: " + vector);
        
        // Stack example (legacy, extends Vector)
        Stack<Integer> stack = new Stack<>();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        System.out.println("\nStack: " + stack);
        System.out.println("Popped: " + stack.pop());  // Removes and returns the top element
        System.out.println("Peek: " + stack.peek());   // Returns the top element without removing
        System.out.println("Stack after operations: " + stack);
        
        // CopyOnWriteArrayList example (thread-safe)
        List<String> cowList = new CopyOnWriteArrayList<>();
        cowList.add("Safe");
        cowList.add("Thread");
        cowList.add("List");
        System.out.println("\nCopyOnWriteArrayList: " + cowList);
        
        // Iterating through a List
        System.out.println("\nIterating through ArrayList:");
        for (String fruit : arrayList) {
            System.out.println("- " + fruit);
        }
        
        // Using ListIterator
        System.out.println("\nUsing ListIterator on LinkedList:");
        ListIterator<String> iterator = linkedList.listIterator();
        while (iterator.hasNext()) {
            int index = iterator.nextIndex();
            String color = iterator.next();
            System.out.println("Index " + index + ": " + color);
        }
    }
}
```

**Performance comparison of List implementations**:

| Operation | ArrayList | LinkedList | Vector |
|-----------|-----------|------------|--------|
| get(index) | O(1) | O(n) | O(1) |
| add(E) (at end) | O(1)* | O(1) | O(1)* |
| add(index, E) | O(n) | O(n) | O(n) |
| remove(index) | O(n) | O(n) | O(n) |
| Iterator.remove() | O(n) | O(1) | O(n) |
| ListIterator.add(E) | O(n) | O(1) | O(n) |

*Amortized constant time - occasionally O(n) when the array needs to be resized

**When to use which List implementation**:

- Use **ArrayList** when:
  - You need frequent random access to elements
  - You mostly append elements at the end
  - You rarely insert or remove elements from the middle

- Use **LinkedList** when:
  - You frequently add or remove elements from the beginning, middle, or end
  - You don't need random access
  - You need a List that also implements Queue or Deque

- Use **Vector** when:
  - You need thread-safety and don't want to use Collections.synchronizedList()
  - You're working with legacy code

- Use **CopyOnWriteArrayList** when:
  - You need thread-safety
  - Reads vastly outnumber writes
  - You need to prevent ConcurrentModificationException during iteration

### Q4: What is the Set interface and its implementations?

The Set interface in Java represents a collection that cannot contain duplicate elements. It models the mathematical set abstraction and extends the Collection interface without adding any new methods.

**Key characteristics of Set**:

1. **No Duplicates**: A Set cannot contain duplicate elements
2. **At Most One Null**: Most implementations allow at most one null element
3. **No Guaranteed Order**: Unless the implementation specifically provides ordering
4. **Equality Based on Content**: Two sets are equal if they contain the same elements

**Main implementations of the Set interface**:

1. **HashSet**:
   - Backed by a HashMap
   - Uses hash codes of elements for storage and retrieval
   - No guaranteed order of elements
   - Allows one null element
   - Provides constant-time performance for basic operations (add, remove, contains)
   - Not synchronized (not thread-safe)

2. **LinkedHashSet**:
   - Extends HashSet with a linked list implementation
   - Maintains insertion order of elements
   - Slightly slower than HashSet for adding and removing
   - Good when iteration order is important

3. **TreeSet**:
   - Implements SortedSet and NavigableSet interfaces
   - Backed by a TreeMap (Red-Black tree)
   - Elements are stored in sorted order (natural ordering or by a Comparator)
   - Does not allow null elements
   - Provides log(n) time for basic operations
   - Good for maintaining elements in sorted order

4. **EnumSet**:
   - Specialized Set implementation for enum types
   - Very compact and efficient implementation
   - All elements must be of the same enum type
   - Does not allow null elements
   - Not synchronized (not thread-safe)

5. **CopyOnWriteArraySet**:
   - Thread-safe variant backed by CopyOnWriteArrayList
   - All mutative operations create a fresh copy of the underlying array
   - Designed for cases where reads vastly outnumber writes
   - Provides good concurrency for traversal operations

**Example of using different Set implementations**:
```java
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

public class SetImplementationsExample {
    public static void main(String[] args) {
        // HashSet example
        Set<String> hashSet = new HashSet<>();
        hashSet.add("Apple");
        hashSet.add("Banana");
        hashSet.add("Cherry");
        hashSet.add("Apple");  // Duplicate, won't be added
        System.out.println("HashSet: " + hashSet);
        System.out.println("Contains 'Banana': " + hashSet.contains("Banana"));
        
        // LinkedHashSet example
        Set<String> linkedHashSet = new LinkedHashSet<>();
        linkedHashSet.add("Red");
        linkedHashSet.add("Green");
        linkedHashSet.add("Blue");
        linkedHashSet.add("Red");  // Duplicate, won't be added
        System.out.println("\nLinkedHashSet (maintains insertion order): " + linkedHashSet);
        
        // TreeSet example
        Set<String> treeSet = new TreeSet<>();
        treeSet.add("Zebra");
        treeSet.add("Elephant");
        treeSet.add("Lion");
        treeSet.add("Tiger");
        System.out.println("\nTreeSet (sorted order): " + treeSet);
        
        // Using TreeSet with a custom Comparator
        Set<String> customTreeSet = new TreeSet<>(Comparator.comparing(String::length).thenComparing(s -> s));
        customTreeSet.add("Dog");
        customTreeSet.add("Cat");
        customTreeSet.add("Elephant");
        customTreeSet.add("Bird");
        System.out.println("\nTreeSet with custom Comparator (by length, then alphabetically): " + customTreeSet);
        
        // EnumSet example
        enum Days { MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY }
        
        Set<Days> weekdays = EnumSet.range(Days.MONDAY, Days.FRIDAY);
        Set<Days> weekend = EnumSet.of(Days.SATURDAY, Days.SUNDAY);
        
        System.out.println("\nEnumSet weekdays: " + weekdays);
        System.out.println("EnumSet weekend: " + weekend);
        
        // CopyOnWriteArraySet example (thread-safe)
        Set<String> cowSet = new CopyOnWriteArraySet<>();
        cowSet.add("Safe");
        cowSet.add("Thread");
        cowSet.add("Set");
        System.out.println("\nCopyOnWriteArraySet: " + cowSet);
        
        // Set operations
        Set<Integer> set1 = new HashSet<>(Arrays.asList(1, 2, 3, 4, 5));
        Set<Integer> set2 = new HashSet<>(Arrays.asList(3, 4, 5, 6, 7));
        
        // Union
        Set<Integer> union = new HashSet<>(set1);
        union.addAll(set2);
        System.out.println("\nUnion of sets: " + union);
        
        // Intersection
        Set<Integer> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);
        System.out.println("Intersection of sets: " + intersection);
        
        // Difference (set1 - set2)
        Set<Integer> difference = new HashSet<>(set1);
        difference.removeAll(set2);
        System.out.println("Difference of sets (set1 - set2): " + difference);
        
        // Symmetric difference (elements in either set but not in both)
        Set<Integer> symmetricDifference = new HashSet<>(set1);
        symmetricDifference.addAll(set2);
        Set<Integer> temp = new HashSet<>(set1);
        temp.retainAll(set2);
        symmetricDifference.removeAll(temp);
        System.out.println("Symmetric difference: " + symmetricDifference);
    }
}
```

**Performance comparison of Set implementations**:

| Operation | HashSet | LinkedHashSet | TreeSet | EnumSet |
|-----------|---------|---------------|---------|---------|
| add(E) | O(1) | O(1) | O(log n) | O(1) |
| remove(Object) | O(1) | O(1) | O(log n) | O(1) |
| contains(Object) | O(1) | O(1) | O(log n) | O(1) |
| size() | O(1) | O(1) | O(1) | O(1) |
| iteration | O(n) | O(n) | O(n) | O(n) |

**When to use which Set implementation**:

- Use **HashSet** when:
  - You need the fastest possible performance for add, remove, contains operations
  - You don't care about the iteration order
  - You need to allow null elements

- Use **LinkedHashSet** when:
  - You want to maintain insertion order
  - You need predictable iteration order
  - You can afford slightly slower performance than HashSet

- Use **TreeSet** when:
  - You need elements to be sorted (either naturally or with a custom Comparator)
  - You need to find ranges of elements or the closest matches
  - You need to retrieve the first or last element efficiently

- Use **EnumSet** when:
  - All elements are of the same enum type
  - You need the most memory-efficient implementation

- Use **CopyOnWriteArraySet** when:
  - You need thread-safety
  - Reads vastly outnumber writes
  - You need to prevent ConcurrentModificationException during iteration

### Q5: What is the Map interface and its implementations?

The Map interface in Java represents a mapping between keys and values. It is not a true collection but is part of the Collections Framework. Each key can map to at most one value, and keys must be unique within a map.

**Key characteristics of Map**:

1. **Key-Value Pairs**: Maps store data as key-value pairs
2. **Unique Keys**: Each key can appear at most once in a map
3. **Values Can Be Duplicates**: Multiple keys can map to the same value
4. **No Guaranteed Order**: Unless the implementation specifically provides ordering
5. **Null Support**: Varies by implementation (some allow null keys and values, others don't)

**Main methods in the Map interface**:

1. **Basic Operations**:
   - `put(K key, V value)`: Associates the value with the key
   - `get(Object key)`: Returns the value associated with the key
   - `remove(Object key)`: Removes the mapping for the key
   - `containsKey(Object key)`: Returns true if the map contains the key
   - `containsValue(Object value)`: Returns true if the map maps one or more keys to the value

2. **Bulk Operations**:
   - `putAll(Map<? extends K, ? extends V> m)`: Copies all mappings from the specified map
   - `clear()`: Removes all mappings

3. **Collection Views**:
   - `keySet()`: Returns a Set view of the keys
   - `values()`: Returns a Collection view of the values
   - `entrySet()`: Returns a Set view of the key-value mappings

4. **Default Methods (Java 8+)**:
   - `getOrDefault(Object key, V defaultValue)`: Returns the value or the default if the key is not present
   - `forEach(BiConsumer<? super K, ? super V> action)`: Performs the action for each entry
   - `replaceAll(BiFunction<? super K, ? super V, ? extends V> function)`: Replaces each value with the result of the function
   - `putIfAbsent(K key, V value)`: Puts the value if the key is not already present
   - `remove(Object key, Object value)`: Removes the entry if the key maps to the value
   - `replace(K key, V oldValue, V newValue)`: Replaces the value if the key maps to the old value
   - `computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction)`: Computes a value if the key is not present
   - `computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction)`: Computes a new value if the key is present
   - `compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction)`: Computes a new value for the key
   - `merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction)`: Merges the value with the existing value

**Main implementations of the Map interface**:

1. **HashMap**:
   - Uses hash table for storage
   - Allows one null key and multiple null values
   - No guaranteed order for iteration
   - Provides constant-time performance for basic operations
   - Not synchronized (not thread-safe)

2. **LinkedHashMap**:
   - Extends HashMap with a linked list implementation
   - Maintains insertion order or access order (LRU cache)
   - Slightly slower than HashMap for adding and removing
   - Good when iteration order is important

3. **TreeMap**:
   - Implements SortedMap and NavigableMap interfaces
   - Red-Black tree implementation
   - Keys are stored in sorted order (natural ordering or by a Comparator)
   - Does not allow null keys (values can be null)
   - Provides log(n) time for basic operations
   - Good for maintaining keys in sorted order

4. **Hashtable**:
   - Legacy class, similar to HashMap but synchronized (thread-safe)
   - Does not allow null keys or values
   - Less efficient than HashMap due to synchronization overhead
   - Generally avoided in favor of HashMap or concurrent alternatives

5. **Properties**:
   - Subclass of Hashtable
   - Used for storing string key-value pairs
   - Often used for configuration

6. **ConcurrentHashMap**:
   - Thread-safe implementation designed for high concurrency
   - Divides the map into segments for better performance during concurrent access
   - Does not allow null keys or values
   - Better performance than Hashtable for concurrent access
   - Since Java 8, uses a different strategy with finer-grained locking

7. **WeakHashMap**:
   - Keys are stored as weak references
   - Entries are automatically removed when keys are no longer referenced
   - Useful for implementing caches or mappings where garbage collection should not be prevented

8. **IdentityHashMap**:
   - Uses reference equality (==) instead of object equality (equals())
   - Useful when maintaining a separate copy of an object graph
   - Not meant for general-purpose use

9. **EnumMap**:
   - Specialized Map implementation for enum keys
   - Very compact and efficient implementation
   - All keys must be of the same enum type
   - Does not allow null keys

**Example of using different Map implementations**:
```java
import java.util.*;
import java.util.concurrent.*;

public class MapImplementationsExample {
    public static void main(String[] args) {
        // HashMap example
        Map<String, Integer> hashMap = new HashMap<>();
        hashMap.put("Apple", 10);
        hashMap.put("Banana", 20);
        hashMap.put("Cherry", 30);
        hashMap.put(null, 0);  // HashMap allows null keys
        System.out.println("HashMap: " + hashMap);
        System.out.println("Value for 'Banana': " + hashMap.get("Banana"));
        
        // LinkedHashMap example (maintains insertion order)
        Map<String, Integer> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put("Red", 100);
        linkedHashMap.put("Green", 200);
        linkedHashMap.put("Blue", 300);
        System.out.println("\nLinkedHashMap (maintains insertion order): " + linkedHashMap);
        
        // TreeMap example (sorted by keys)
        Map<String, Integer> treeMap = new TreeMap<>();
        treeMap.put("Zebra", 5);
        treeMap.put("Elephant", 10);
        treeMap.put("Lion", 15);
        treeMap.put("Tiger", 20);
        // treeMap.put(null, 0);  // TreeMap doesn't allow null keys
        System.out.println("\nTreeMap (sorted by keys): " + treeMap);
        
        // TreeMap with custom Comparator
        Map<String, Integer> customTreeMap = new TreeMap<>(Comparator.comparing(String::length).thenComparing(s -> s));
        customTreeMap.put("Dog", 1);
        customTreeMap.put("Cat", 2);
        customTreeMap.put("Elephant", 3);
        customTreeMap.put("Bird", 4);
        System.out.println("\nTreeMap with custom Comparator (by length, then alphabetically): " + customTreeMap);
        
        // Hashtable example (legacy, synchronized)
        Map<String, Integer> hashtable = new Hashtable<>();
        hashtable.put("One", 1);
        hashtable.put("Two", 2);
        hashtable.put("Three", 3);
        // hashtable.put(null, 0);  // Hashtable doesn't allow null keys or values
        System.out.println("\nHashtable: " + hashtable);
        
        // Properties example (for string key-value pairs)
        Properties properties = new Properties();
        properties.setProperty("host", "localhost");
        properties.setProperty("port", "8080");
        properties.setProperty("username", "admin");
        System.out.println("\nProperties: " + properties);
        System.out.println("Host: " + properties.getProperty("host"));
        
        // ConcurrentHashMap example (thread-safe)
        Map<String, Integer> concurrentMap = new ConcurrentHashMap<>();
        concurrentMap.put("Safe", 1);
        concurrentMap.put("Thread", 2);
        concurrentMap.put("Map", 3);
        // concurrentMap.put(null, 0);  // ConcurrentHashMap doesn't allow null keys or values
        System.out.println("\nConcurrentHashMap: " + concurrentMap);
        
        // WeakHashMap example
        Map<String, Integer> weakHashMap = new WeakHashMap<>();
        String key = new String("WeakKey");  // Creating a new String object to demonstrate weak references
        weakHashMap.put(key, 100);
        System.out.println("\nWeakHashMap: " + weakHashMap);
        
        // EnumMap example
        enum Days { MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY }
        
        Map<Days, String> enumMap = new EnumMap<>(Days.class);
        enumMap.put(Days.MONDAY, "Start of work week");
        enumMap.put(Days.FRIDAY, "End of work week");
        enumMap.put(Days.SATURDAY, "Weekend");
        System.out.println("\nEnumMap: " + enumMap);
        
        // IdentityHashMap example
        Map<String, Integer> identityHashMap = new IdentityHashMap<>();
        String s1 = new String("Key");
        String s2 = new String("Key");
        identityHashMap.put(s1, 1);
        identityHashMap.put(s2, 2);  // Different object, same value
        System.out.println("\nIdentityHashMap (uses reference equality): " + identityHashMap);
        System.out.println("s1 == s2: " + (s1 == s2));  // false
        System.out.println("s1.equals(s2): " + s1.equals(s2));  // true
        
        // Java 8+ Map features
        System.out.println("\nJava 8+ Map features:");
        
        // getOrDefault
        System.out.println("getOrDefault: " + hashMap.getOrDefault("Missing", -1));
        
        // putIfAbsent
        hashMap.putIfAbsent("Banana", 50);  // Won't change existing value
        hashMap.putIfAbsent("Mango", 40);   // Will add new entry
        System.out.println("After putIfAbsent: " + hashMap);
        
        // compute
        hashMap.compute("Apple", (k, v) -> v + 5);
        System.out.println("After compute: " + hashMap);
        
        // computeIfPresent
        hashMap.computeIfPresent("Cherry", (k, v) -> v + 10);
        System.out.println("After computeIfPresent: " + hashMap);
        
        // computeIfAbsent
        hashMap.computeIfAbsent("Pear", k -> 25);
        System.out.println("After computeIfAbsent: " + hashMap);
        
        // merge
        hashMap.merge("Apple", 5, (oldValue, value) -> oldValue + value);
        System.out.println("After merge: " + hashMap);
        
        // forEach
        System.out.println("\nUsing forEach:");
        hashMap.forEach((k, v) -> System.out.println(k + " -> " + v));
    }
}
```

**Performance comparison of Map implementations**:

| Operation | HashMap | LinkedHashMap | TreeMap | Hashtable | ConcurrentHashMap |
|-----------|---------|---------------|---------|-----------|-------------------|
| get(Object) | O(1) | O(1) | O(log n) | O(1) | O(1) |
| put(K,V) | O(1) | O(1) | O(log n) | O(1) | O(1) |
| remove(Object) | O(1) | O(1) | O(log n) | O(1) | O(1) |
| containsKey(Object) | O(1) | O(1) | O(log n) | O(1) | O(1) |
| iteration | O(n) | O(n) | O(n) | O(n) | O(n) |

**When to use which Map implementation**:

- Use **HashMap** when:
  - You need the fastest possible performance for add, remove, contains operations
  - You don't care about the iteration order
  - You need to allow null keys and values
  - You don't need thread safety

- Use **LinkedHashMap** when:
  - You want to maintain insertion order or access order (LRU cache)
  - You need predictable iteration order
  - You can afford slightly slower performance than HashMap

- Use **TreeMap** when:
  - You need keys to be sorted (either naturally or with a custom Comparator)
  - You need to find ranges of keys or the closest matches
  - You need to retrieve the first or last key efficiently

- Use **Hashtable** when:
  - You need thread-safety and are working with legacy code
  - Otherwise, prefer ConcurrentHashMap for better performance

- Use **ConcurrentHashMap** when:
  - You need thread-safety with high concurrency
  - You need better performance than Hashtable or synchronized maps

- Use **WeakHashMap** when:
  - You need keys to be garbage collected when no longer referenced elsewhere
  - You're implementing a cache where entries should be removed when keys are no longer used

- Use **IdentityHashMap** when:
  - You need to compare keys by reference (==) instead of equals()
  - You're maintaining object identity in serialization/deserialization or similar scenarios

- Use **EnumMap** when:
  - All keys are of the same enum type
  - You need the most memory-efficient implementation for enum keys

### Q6: What is the Queue interface and its implementations?

## Advanced Collections Framework Questions for 3+ Years Experience

### Q21: How would you design a thread-safe cache with expiring elements using Java Collections?

This is a common scenario in enterprise applications where you need to cache data but also ensure elements expire after a certain time.

**Solution approach**:

1. **Using ConcurrentHashMap with a background thread**:
   ```java
   public class ExpiringCache<K, V> {
       private final ConcurrentHashMap<K, CacheEntry<V>> cache = new ConcurrentHashMap<>();
       private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
       private final long defaultExpiryTimeMillis;

       public ExpiringCache(long defaultExpiryTimeMillis) {
           this.defaultExpiryTimeMillis = defaultExpiryTimeMillis;
           startCleanupTask();
       }

       public void put(K key, V value) {
           put(key, value, defaultExpiryTimeMillis);
       }

       public void put(K key, V value, long expiryTimeMillis) {
           long expiryTime = System.currentTimeMillis() + expiryTimeMillis;
           cache.put(key, new CacheEntry<>(value, expiryTime));
       }

       public V get(K key) {
           CacheEntry<V> entry = cache.get(key);
           if (entry != null && !entry.isExpired()) {
               return entry.getValue();
           } else {
               cache.remove(key);
               return null;
           }
       }

       private void startCleanupTask() {
           scheduler.scheduleAtFixedRate(() -> {
               for (Map.Entry<K, CacheEntry<V>> entry : cache.entrySet()) {
                   if (entry.getValue().isExpired()) {
                       cache.remove(entry.getKey());
                   }
               }
           }, 1, 1, TimeUnit.MINUTES);
       }

       private static class CacheEntry<V> {
           private final V value;
           private final long expiryTime;

           CacheEntry(V value, long expiryTime) {
               this.value = value;
               this.expiryTime = expiryTime;
           }

           boolean isExpired() {
               return System.currentTimeMillis() > expiryTime;
           }

           V getValue() {
               return value;
           }
       }
   }
   ```

2. **Using Caffeine Cache (modern solution)**:
   ```java
   Cache<Key, Value> cache = Caffeine.newBuilder()
       .expireAfterWrite(10, TimeUnit.MINUTES)
       .maximumSize(10_000)
       .build();
   ```

**Key considerations in this design**:
- Thread-safety is ensured by using ConcurrentHashMap
- Expiration is handled by a background cleanup thread
- The solution avoids memory leaks by automatically removing expired entries

### Q22: How would you implement a fixed-size thread-safe LRU (Least Recently Used) cache in Java?

This is a common interview question that tests your understanding of both collections and thread-safety.

**Solution approach using LinkedHashMap with access order**:

```java
public class LRUCache<K, V> {
    private final Map<K, V> cache;
    private final int capacity;
    private final Object lock = new Object();

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.cache = Collections.synchronizedMap(
            new LinkedHashMap<K, V>(capacity + 1, 0.75f, true) {
                @Override
                protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                    return size() > capacity;
                }
            }
        );
    }

    public V get(K key) {
        synchronized (lock) {
            return cache.get(key);
        }
    }

    public void put(K key, V value) {
        synchronized (lock) {
            cache.put(key, value);
        }
    }
}
```

**Key points to discuss**:
- LinkedHashMap with `accessOrder=true` maintains entries in order of access (most recently used at the end)
- Overriding `removeEldestEntry` automatically removes the least recently used entry when capacity is exceeded
- The implementation is thread-safe using synchronized methods
- For higher concurrency, consider using ConcurrentHashMap with a custom eviction policy

### Q23: What are the challenges when using collections in a concurrent environment, and how do you address them?

In a concurrent environment, regular collections can lead to several issues:

1. **ConcurrentModificationException**:
   - Occurs when one thread modifies a collection while another thread is iterating over it
   - Solutions:
     - Use CopyOnWriteArrayList/Set for read-heavy scenarios
     - Use concurrent collections (ConcurrentHashMap, etc.)
     - Use synchronized wrappers with external synchronization during iteration

2. **Thread Interference and Memory Consistency Errors**:
   - Multiple threads accessing/modifying collections without proper synchronization
   - Solutions:
     - Use concurrent collections (ConcurrentHashMap, ConcurrentSkipListMap, etc.)
     - Use Collections.synchronizedXXX wrappers with proper external synchronization
     - Use thread-local collections when appropriate

3. **Performance Bottlenecks**:
   - Excessive synchronization can lead to contention and reduced performance
   - Solutions:
     - Use specialized concurrent collections based on access patterns
     - Consider partitioning data to reduce contention
     - Use lock striping (as implemented in ConcurrentHashMap)

**Code example demonstrating the problem and solution**:

```java
// Problem: ConcurrentModificationException
List<String> list = new ArrayList<>();
// Add elements
list.add("A"); list.add("B"); list.add("C");

// Thread 1: Iterating
for (String item : list) {
    // Thread 2: Simultaneously modifying
    list.remove("B"); // This will cause ConcurrentModificationException in Thread 1
}

// Solution: Use CopyOnWriteArrayList
List<String> concurrentList = new CopyOnWriteArrayList<>();
concurrentList.add("A"); concurrentList.add("B"); concurrentList.add("C");

// Thread 1: Iterating
for (String item : concurrentList) {
    // Thread 2: Simultaneously modifying - No exception
    concurrentList.remove("B"); // Iterator sees a snapshot, no exception
}
```

### Q24: How would you implement a custom collection that enforces elements of a specific type at runtime?

This question tests your understanding of type safety and potential limitations of generics in Java due to type erasure.

```java
public class TypeSafeCollection<T> {
    private final Class<T> type;
    private final List<T> elements = new ArrayList<>();

    public TypeSafeCollection(Class<T> type) {
        this.type = type;
    }

    public void add(Object element) {
        if (element == null || type.isAssignableFrom(element.getClass())) {
            elements.add(type.cast(element));
        } else {
            throw new ClassCastException("Cannot add " + element.getClass().getName() 
                + " to collection of " + type.getName());
        }
    }

    public List<T> getElements() {
        return Collections.unmodifiableList(elements);
    }
}

// Usage
TypeSafeCollection<String> strings = new TypeSafeCollection<>(String.class);
strings.add("Hello"); // Works fine
strings.add(123); // Throws ClassCastException at runtime
```

**Discussion points**:
- Java generics use type erasure, meaning type information is not available at runtime
- This solution preserves and checks types at runtime
- Collections.checkedCollection provides similar functionality in the standard library
- Trade-offs between this approach and using generics alone

### Q25: How would you implement a multi-value map using Java collections?

This question tests your ability to compose collections to create more complex data structures.

```java
public class MultiValueMap<K, V> {
    private final Map<K, List<V>> map = new HashMap<>();

    public void add(K key, V value) {
        map.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
    }

    public List<V> get(K key) {
        return map.getOrDefault(key, Collections.emptyList());
    }

    public boolean remove(K key, V value) {
        List<V> values = map.get(key);
        if (values != null) {
            boolean removed = values.remove(value);
            if (values.isEmpty()) {
                map.remove(key);
            }
            return removed;
        }
        return false;
    }

    public Set<K> keySet() {
        return map.keySet();
    }
    
    public int size() {
        return map.values().stream().mapToInt(List::size).sum();
    }
}
```

**Alternative implementations**:
- Using `Multimap` from Guava library
- Using Java 8 Streams with `Collectors.groupingBy`

**This data structure is useful for**:
- Representing one-to-many relationships
- HTTP headers (where one header can have multiple values)
- Query parameters in URLs

### Q26: What are some common memory and performance pitfalls when working with large collections in Java?

This question tests your practical knowledge of optimizing collection usage in real-world scenarios.

**Common pitfalls and optimizations**:

1. **Excessive Memory Usage**:
   - Pre-size collections when size is known: `new ArrayList<>(expectedSize)`
   - Use primitive collections (e.g., TroveJ, Eclipse Collections, Koloboke) instead of boxing
   - Consider using bit sets for compact representation of boolean flags
   
2. **Inefficient Iteration**:
   - Use enhanced for-loop or iterators instead of indexed access for linked structures
   - Prefer the Streams API for functional operations on large collections
   - Use indexed access for random access structures (ArrayList, arrays)

3. **Inappropriate Collection Choice**:
   - Using LinkedList for random access operations
   - Using ArrayList for frequent insertions/deletions at arbitrary positions
   - Using HashMap when order matters (use LinkedHashMap instead)

4. **Memory Leaks**:
   - Forgetting to remove references when using as a cache
   - Not cleaning up ThreadLocal collections
   - Not handling strong references in WeakHashMap values

**Memory optimization example**:
```java
// Inefficient - excessive boxing/unboxing and memory overhead
List<Integer> numbers = new ArrayList<>();
for (int i = 0; i < 1_000_000; i++) {
    numbers.add(i);
}

// More efficient with known size
List<Integer> numbers = new ArrayList<>(1_000_000);
for (int i = 0; i < 1_000_000; i++) {
    numbers.add(i);
}

// Most efficient - no boxing, minimal memory overhead
int[] numbers = new int[1_000_000];
for (int i = 0; i < numbers.length; i++) {
    numbers[i] = i;
}
```

### Q27: Implement a custom thread-safe blocking bounded queue with a specified capacity

This tests understanding of concurrency, blocking operations, and collection design.

```java
public class BlockingBoundedQueue<T> {
    private final Queue<T> queue = new LinkedList<>();
    private final int capacity;
    private final Object lock = new Object();

    public BlockingBoundedQueue(int capacity) {
        this.capacity = capacity;
    }

    public void put(T item) throws InterruptedException {
        synchronized (lock) {
            while (queue.size() == capacity) {
                lock.wait(); // Block until space is available
            }
            queue.add(item);
            lock.notifyAll(); // Notify consumers that an item is available
        }
    }

    public T take() throws InterruptedException {
        synchronized (lock) {
            while (queue.isEmpty()) {
                lock.wait(); // Block until an item is available
            }
            T item = queue.remove();
            lock.notifyAll(); // Notify producers that space is available
            return item;
        }
    }

    public int size() {
        synchronized (lock) {
            return queue.size();
        }
    }
}
```

**Discussion points**:
- This implementation uses intrinsic locking and condition waiting
- Java provides BlockingQueue implementations like ArrayBlockingQueue
- Trade-offs between using built-in vs. custom implementations
- How to handle interruptions and timeouts

### Q28: How do collectors work in Java Stream API, and how would you implement a custom collector?

This question tests deep understanding of the Stream API and functional programming concepts.

**Understanding Collectors**:
Collectors are used with Stream's terminal `collect()` operation to accumulate elements into a collection or to perform a reduction operation.

**How to implement a custom Collector**:
```java
public class CustomCollectors {
    public static <T> Collector<T, ?, LinkedList<T>> toLinkedList() {
        Supplier<LinkedList<T>> supplier = LinkedList::new;
        BiConsumer<LinkedList<T>, T> accumulator = LinkedList::add;
        BiConsumer<LinkedList<T>, LinkedList<T>> combiner = 
            (list1, list2) -> list1.addAll(list2);
        
        return Collector.of(supplier, accumulator, combiner);
    }
    
    // Usage example
    public static void main(String[] args) {
        List<String> list = Stream.of("a", "b", "c")
            .collect(toLinkedList());
        
        System.out.println(list.getClass()); // class java.util.LinkedList
    }
}
```

**More complex example - frequency counter collector**:
```java
public static <T> Collector<T, ?, Map<T, Long>> toFrequencyMap() {
    return Collectors.groupingBy(
        Function.identity(), 
        Collectors.counting()
    );
}

// Usage
Map<String, Long> frequency = Stream.of("a", "b", "a", "c", "b", "a")
    .collect(toFrequencyMap());
// Result: {a=3, b=2, c=1}
```

### Q29: Compare ArrayList vs LinkedList operations with time complexity analysis

Create a scenario-based question that demonstrates the importance of choosing the right collection:

```java
public class ListPerformanceDemo {
    public static void main(String[] args) {
        int size = 100000;
        
        // Initialize lists
        List<Integer> arrayList = new ArrayList<>();
        List<Integer> linkedList = new LinkedList<>();
        
        // 1. Add elements at the end
        long start = System.nanoTime();
        for (int i = 0; i < size; i++) {
            arrayList.add(i);
        }
        long arrayListAddEnd = System.nanoTime() - start;
        
        start = System.nanoTime();
        for (int i = 0; i < size; i++) {
            linkedList.add(i);
        }
        long linkedListAddEnd = System.nanoTime() - start;
        
        // 2. Add elements at the beginning
        arrayList.clear();
        linkedList.clear();
        
        start = System.nanoTime();
        for (int i = 0; i < size; i++) {
            arrayList.add(0, i);
        }
        long arrayListAddBegin = System.nanoTime() - start;
        
        start = System.nanoTime();
        for (int i = 0; i < size; i++) {
            linkedList.add(0, i);
        }
        long linkedListAddBegin = System.nanoTime() - start;
        
        // 3. Get elements by index
        start = System.nanoTime();
        for (int i = 0; i < 10000; i++) {
            arrayList.get(i);
        }
        long arrayListGet = System.nanoTime() - start;
        
        start = System.nanoTime();
        for (int i = 0; i < 10000; i++) {
            linkedList.get(i);
        }
        long linkedListGet = System.nanoTime() - start;
        
        // Print results
        System.out.println("ArrayList add at end: " + arrayListAddEnd + " ns");
        System.out.println("LinkedList add at end: " + linkedListAddEnd + " ns");
        System.out.println("ArrayList add at beginning: " + arrayListAddBegin + " ns");
        System.out.println("LinkedList add at beginning: " + linkedListAddBegin + " ns");
        System.out.println("ArrayList get by index: " + arrayListGet + " ns");
        System.out.println("LinkedList get by index: " + linkedListGet + " ns");
    }
}
```

**Time complexity analysis**:

| Operation | ArrayList | LinkedList |
|-----------|-----------|------------|
| add(E) (at end) | O(1)* | O(1) |
| add(0, E) (at beginning) | O(n) | O(1) |
| add(i, E) (in middle) | O(n-i) | O(i) |
| get(i) | O(1) | O(i) |
| remove(0) | O(n) | O(1) |
| remove(size-1) | O(1) | O(1) |
| remove(i) | O(n-i) | O(i) |
| contains(Object) | O(n) | O(n) |

*Amortized constant time - occasionally O(n) when the array needs to be resized

**Key insights for interviews**:
- ArrayList excels at random access and adding elements at the end
- LinkedList excels at adding/removing elements at both ends and at arbitrary positions (if you already have a reference to the position)
- The theoretical time complexity doesn't always translate directly to performance due to factors like cache locality

### Q30: How would you implement a priority queue with custom priority handling?

This question tests understanding of priority queues and comparators.

```java
class Task {
    private final String name;
    private final int priority;
    private final long creationTime;

    public Task(String name, int priority) {
        this.name = name;
        this.priority = priority;
        this.creationTime = System.currentTimeMillis();
    }

    public String getName() { return name; }
    public int getPriority() { return priority; }
    public long getCreationTime() { return creationTime; }

    @Override
    public String toString() {
        return "Task{" + "name='" + name + "', priority=" + priority + '}';
    }
}

public class CustomPriorityQueueExample {
    public static void main(String[] args) {
        // Priority Queue with custom comparator - higher priority tasks first
        // If same priority, older tasks first (FIFO within same priority)
        PriorityQueue<Task> taskQueue = new PriorityQueue<>(
            Comparator.comparing(Task::getPriority).reversed()
                .thenComparing(Task::getCreationTime)
        );
        
        // Add tasks
        taskQueue.add(new Task("Low priority task 1", 1));
        taskQueue.add(new Task("High priority task", 10));
        taskQueue.add(new Task("Medium priority task", 5));
        taskQueue.add(new Task("Low priority task 2", 1));
        
        // Process tasks in priority order
        while (!taskQueue.isEmpty()) {
            Task task = taskQueue.poll();
            System.out.println("Processing: " + task);
        }
        
        /* Output would be:
           Processing: Task{name='High priority task', priority=10}
           Processing: Task{name='Medium priority task', priority=5}
           Processing: Task{name='Low priority task 1', priority=1}
           Processing: Task{name='Low priority task 2', priority=1}
        */
    }
}
```

**Discussion points**:
- PriorityQueue in Java is implemented as a binary heap
- The implementation is not thread-safe (use PriorityBlockingQueue for thread safety)
- Time complexity: O(log n) for add and remove, O(1) for peek
- Can be used for scheduling, task management, and algorithms like Dijkstra's