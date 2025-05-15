# Java Collections Framework - Interview Questions

This file contains interview questions specifically focused on the Java Collections Framework, including practical implementation details, performance considerations, and common pitfalls.

## Maps and Sets

### 1. What will this HashMap iteration code output?
```java
public class HashMapIteration {
    public static void main(String[] args) {
        Map<String, Integer> map = new HashMap<>();
        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);
        
        // Method 1: Iterate over entries
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
        
        System.out.println("---");
        
        // Method 2: Iterate over keys and get values
        for (String key : map.keySet()) {
            System.out.println(key + ": " + map.get(key));
        }
    }
}
```

**Interview Follow-up Question**: Which iteration method is more efficient and why?

**Answer**: The output order is not guaranteed since HashMap doesn't maintain insertion order. The output might look like:
```
three: 3
one: 1
two: 2
---
three: 3
one: 1
two: 2
```

For the follow-up question: The first method (using entrySet()) is more efficient because it retrieves both key and value in a single operation. The second method requires a separate lookup (map.get(key)) for each key, which is an additional operation.

### 2. What happens with these HashSet operations?
```java
public class EmployeeSetExample {
    static class Employee {
        private String name;
        private int id;
        
        public Employee(String name, int id) {
            this.name = name;
            this.id = id;
        }
        
        // Only equals() is overridden, not hashCode()
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Employee employee = (Employee) o;
            return id == employee.id && Objects.equals(name, employee.name);
        }
        
        @Override
        public String toString() {
            return name + ":" + id;
        }
    }
    
    public static void main(String[] args) {
        Set<Employee> employees = new HashSet<>();
        
        Employee e1 = new Employee("Alice", 1001);
        Employee e2 = new Employee("Bob", 1002);
        Employee e3 = new Employee("Alice", 1001); // Same as e1
        
        employees.add(e1);
        employees.add(e2);
        employees.add(e3);
        
        System.out.println("Set size: " + employees.size());
        System.out.println("Contains e1: " + employees.contains(e1));
        System.out.println("Contains e3: " + employees.contains(e3));
        
        // Now let's search for a new but equivalent employee
        Employee e4 = new Employee("Alice", 1001);
        System.out.println("Contains e4: " + employees.contains(e4));
    }
}
```

**Interview Follow-up Question**: What's wrong with this implementation and how would you fix it?

**Answer**:
```
Set size: 3
Contains e1: true
Contains e3: true
Contains e4: false
```

The problem is that only equals() is overridden but not hashCode(). This violates the contract that equal objects must have equal hash codes. In a HashSet:
1. When adding e3, it goes into a different bucket than e1 (despite being equal by equals())
2. When checking for e4, HashSet first checks the hash code to find the bucket, but since e4's hash code differs from e1/e3, it doesn't find a match

The fix is to properly override both equals() and hashCode() methods:
```java
@Override
public int hashCode() {
    return Objects.hash(name, id);
}
```

### 3. Compare performance of ArrayList vs LinkedList operations:
```java
public class ListPerformanceComparison {
    public static void main(String[] args) {
        List<Integer> arrayList = new ArrayList<>();
        List<Integer> linkedList = new LinkedList<>();
        
        // Populate with 100,000 elements
        for (int i = 0; i < 100_000; i++) {
            arrayList.add(i);
            linkedList.add(i);
        }
        
        // Test 1: Get middle element
        long start = System.nanoTime();
        arrayList.get(50_000);
        long end = System.nanoTime();
        System.out.println("ArrayList get(50000): " + (end - start) + " ns");
        
        start = System.nanoTime();
        linkedList.get(50_000);
        end = System.nanoTime();
        System.out.println("LinkedList get(50000): " + (end - start) + " ns");
        
        // Test 2: Add to beginning
        start = System.nanoTime();
        arrayList.add(0, -1);
        end = System.nanoTime();
        System.out.println("ArrayList add at beginning: " + (end - start) + " ns");
        
        start = System.nanoTime();
        linkedList.add(0, -1);
        end = System.nanoTime();
        System.out.println("LinkedList add at beginning: " + (end - start) + " ns");
    }
}
```

**Interview Follow-up Question**: In what scenarios would you choose LinkedList over ArrayList?

**Answer**: The output will show that:
1. ArrayList's get() is much faster (typically ~10-100x) because it has O(1) random access
2. LinkedList's add at the beginning is much faster because it has O(1) insertion, while ArrayList must shift all elements (O(n))

For the follow-up: Choose LinkedList when your application frequently adds/removes elements at the beginning/middle of the list and rarely needs random access by index. Examples include:
- Implementing a queue or deque
- Frequently splitting or merging lists
- Applications with many insertions/deletions and few lookups

### 4. What happens with this ConcurrentModificationException example?
```java
public class ConcurrentModificationDemo {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>(Arrays.asList("A", "B", "C", "D"));
        
        try {
            // Problematic approach
            for (String element : list) {
                if (element.equals("B")) {
                    list.remove(element);
                }
            }
        } catch (Exception e) {
            System.out.println("Exception: " + e.getClass().getSimpleName());
        }
        
        // Reset list
        list = new ArrayList<>(Arrays.asList("A", "B", "C", "D"));
        
        // Safe approach 1: Using Iterator
        Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()) {
            String element = iterator.next();
            if (element.equals("B")) {
                iterator.remove();
            }
        }
        System.out.println("After Iterator removal: " + list);
        
        // Reset list
        list = new ArrayList<>(Arrays.asList("A", "B", "C", "D"));
        
        // Safe approach 2: Using CopyOnWriteArrayList
        List<String> concurrentList = new CopyOnWriteArrayList<>(list);
        for (String element : concurrentList) {
            if (element.equals("B")) {
                concurrentList.remove(element);
            }
        }
        System.out.println("After CopyOnWriteArrayList removal: " + concurrentList);
    }
}
```

**Interview Follow-up Question**: What other ways can you safely remove elements while iterating?

**Answer**:
```
Exception: ConcurrentModificationException
After Iterator removal: [A, C, D]
After CopyOnWriteArrayList removal: [A, C, D]
```

For the follow-up, other safe ways to remove elements while iterating include:
1. Using removeIf() method (Java 8+): `list.removeIf(element -> element.equals("B"));`
2. Creating a copy of the list and removing from original: 
   ```java
   List<String> toRemove = new ArrayList<>();
   for (String element : list) {
       if (element.equals("B")) toRemove.add(element);
   }
   list.removeAll(toRemove);
   ```
3. Using streams (Java 8+): 
   ```java
   list = list.stream()
         .filter(element -> !element.equals("B"))
         .collect(Collectors.toList());
   ```
4. Using ListIterator when you need bidirectional traversal

### 5. Explain the output of this TreeMap operation:
```java
public class TreeMapExample {
    static class CustomKey {
        private int id;
        
        public CustomKey(int id) {
            this.id = id;
        }
        
        @Override
        public String toString() {
            return "Key:" + id;
        }
        
        // No equals, hashCode, or Comparable implementation
    }
    
    public static void main(String[] args) {
        // Approach 1: Using natural ordering (will fail)
        try {
            Map<CustomKey, String> map1 = new TreeMap<>();
            map1.put(new CustomKey(1), "One");
        } catch (Exception e) {
            System.out.println("Exception 1: " + e.getClass().getSimpleName());
        }
        
        // Approach 2: Using custom comparator
        Map<CustomKey, String> map2 = new TreeMap<>((k1, k2) -> k1.id - k2.id);
        map2.put(new CustomKey(1), "One");
        map2.put(new CustomKey(3), "Three");
        map2.put(new CustomKey(2), "Two");
        
        for (Map.Entry<CustomKey, String> entry : map2.entrySet()) {
            System.out.println(entry.getKey() + " = " + entry.getValue());
        }
        
        // Will this find the key?
        CustomKey searchKey = new CustomKey(2);
        System.out.println("Contains key 2: " + map2.containsKey(searchKey));
    }
}
```

**Interview Follow-up Question**: How does TreeMap differ from HashMap in handling custom objects as keys?

**Answer**:
```
Exception 1: ClassCastException
Key:1 = One
Key:2 = Two
Key:3 = Three
Contains key 2: true
```

For the follow-up:
1. TreeMap requires keys to be comparable (either implementing Comparable or via a Comparator)
2. TreeMap uses the compare/compareTo method to determine equality, not equals()
3. HashMap uses hashCode() and equals() for key comparison
4. TreeMap maintains keys in sorted order, while HashMap has no guaranteed order
5. TreeMap operations are O(log n) whereas HashMap operations are typically O(1)
6. When using custom objects as keys, TreeMap can find them based on the sorting criteria even without proper equals/hashCode, while HashMap requires proper equals/hashCode implementations

### 6. What will this NavigableMap example output?
```java
public class NavigableMapExample {
    public static void main(String[] args) {
        NavigableMap<Integer, String> map = new TreeMap<>();
        map.put(1, "One");
        map.put(3, "Three");
        map.put(5, "Five");
        map.put(7, "Seven");
        map.put(9, "Nine");
        
        // Query operations
        System.out.println("Original map: " + map);
        System.out.println("lowerEntry(5): " + map.lowerEntry(5));    // Strictly less
        System.out.println("floorEntry(5): " + map.floorEntry(5));    // Less or equal
        System.out.println("floorEntry(4): " + map.floorEntry(4));
        System.out.println("ceilingEntry(5): " + map.ceilingEntry(5)); // Greater or equal
        System.out.println("ceilingEntry(6): " + map.ceilingEntry(6));
        System.out.println("higherEntry(5): " + map.higherEntry(5));   // Strictly greater
        
        // Navigation
        System.out.println("firstEntry(): " + map.firstEntry());
        System.out.println("lastEntry(): " + map.lastEntry());
        
        // Views
        System.out.println("headMap(5): " + map.headMap(5));         // Less than 5
        System.out.println("headMap(5, true): " + map.headMap(5, true)); // Less than or equal to 5
        System.out.println("tailMap(5): " + map.tailMap(5));         // Greater than or equal to 5
        System.out.println("subMap(3, 7): " + map.subMap(3, 7));     // 3 <= key < 7
        System.out.println("subMap(3, true, 7, true): " + map.subMap(3, true, 7, true)); // 3 <= key <= 7
        
        // Descending
        System.out.println("descendingMap(): " + map.descendingMap());
    }
}
```

**Interview Follow-up Question**: In what real-world scenarios would you use NavigableMap's specialized methods?

**Answer**:
```
Original map: {1=One, 3=Three, 5=Five, 7=Seven, 9=Nine}
lowerEntry(5): 3=Three
floorEntry(5): 5=Five
floorEntry(4): 3=Three
ceilingEntry(5): 5=Five
ceilingEntry(6): 7=Seven
higherEntry(5): 7=Seven
firstEntry(): 1=One
lastEntry(): 9=Nine
headMap(5): {1=One, 3=Three}
headMap(5, true): {1=One, 3=Three, 5=Five}
tailMap(5): {5=Five, 7=Seven, 9=Nine}
subMap(3, 7): {3=Three, 5=Five}
subMap(3, true, 7, true): {3=Three, 5=Five, 7=Seven}
descendingMap(): {9=Nine, 7=Seven, 5=Five, 3=Three, 1=One}
```

For the follow-up, real-world use cases for NavigableMap include:
1. **Range queries**: Finding all elements within a certain range (e.g., events between two dates)
2. **Nearest neighbor search**: Finding the closest match (e.g., finding the nearest service location)
3. **Time-series data**: Retrieving data points closest to a given time
4. **IP address lookup**: Finding which network range an IP belongs to
5. **Price breaks/thresholds**: Determining appropriate pricing tier based on quantity
6. **Version control**: Finding the nearest version/revision of a document
7. **Leaderboards**: Maintaining sorted scores and finding ranks

### 7. How does LinkedHashMap maintain insertion order?
```java
public class LinkedHashMapExample {
    public static void main(String[] args) {
        // Default LinkedHashMap: maintains insertion order
        Map<String, Integer> insertionOrderMap = new LinkedHashMap<>();
        insertionOrderMap.put("One", 1);
        insertionOrderMap.put("Two", 2);
        insertionOrderMap.put("Three", 3);
        insertionOrderMap.put("Four", 4);
        
        // LinkedHashMap with access-order: maintains order of access (LRU cache)
        Map<String, Integer> accessOrderMap = new LinkedHashMap<>(16, 0.75f, true);
        accessOrderMap.put("One", 1);
        accessOrderMap.put("Two", 2);
        accessOrderMap.put("Three", 3);
        accessOrderMap.put("Four", 4);
        
        // Access some elements
        accessOrderMap.get("One");   // Access
        accessOrderMap.get("Three"); // Access
        
        System.out.println("Insertion-order map: " + insertionOrderMap);
        System.out.println("Access-order map: " + accessOrderMap);
        
        // Implement a simple LRU cache with fixed size
        class LRUCache<K, V> extends LinkedHashMap<K, V> {
            private final int maxSize;
            
            public LRUCache(int maxSize) {
                super(16, 0.75f, true);
                this.maxSize = maxSize;
            }
            
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                return size() > maxSize;
            }
        }
        
        LRUCache<String, String> cache = new LRUCache<>(3);
        cache.put("A", "A-data");
        cache.put("B", "B-data");
        cache.put("C", "C-data");
        System.out.println("Cache after adding 3 items: " + cache);
        
        // Add one more - should evict the least recently used entry (A)
        cache.put("D", "D-data");
        System.out.println("Cache after adding 4th item: " + cache);
        
        // Access B - moves it to the end
        cache.get("B");
        // Add one more - should evict the least recently used entry (C)
        cache.put("E", "E-data");
        System.out.println("Cache after accessing B and adding E: " + cache);
    }
}
```

**Interview Follow-up Question**: How would you implement a cache that evicts based on frequency of access rather than recency?

**Answer**:
```
Insertion-order map: {One=1, Two=2, Three=3, Four=4}
Access-order map: {Two=2, Four=4, One=1, Three=3}
Cache after adding 3 items: {A=A-data, B=B-data, C=C-data}
Cache after adding 4th item: {B=B-data, C=C-data, D=D-data}
Cache after accessing B and adding E: {C=C-data, B=B-data, E=E-data}
```

For the follow-up, to implement a Least Frequently Used (LFU) cache:
```java
class LFUCache<K, V> {
    private final int capacity;
    private final Map<K, V> cache = new HashMap<>();
    private final Map<K, Integer> frequencies = new HashMap<>();
    private final Map<Integer, LinkedHashSet<K>> frequencyLists = new HashMap<>();
    private int minFrequency = 0;
    
    public LFUCache(int capacity) {
        this.capacity = capacity;
    }
    
    public V get(K key) {
        if (!cache.containsKey(key)) {
            return null;
        }
        
        // Increase frequency
        int oldFreq = frequencies.get(key);
        frequencies.put(key, oldFreq + 1);
        
        // Remove from old frequency list
        frequencyLists.get(oldFreq).remove(key);
        
        // Update min frequency if needed
        if (oldFreq == minFrequency && frequencyLists.get(oldFreq).isEmpty()) {
            minFrequency++;
        }
        
        // Add to new frequency list
        frequencyLists.computeIfAbsent(oldFreq + 1, k -> new LinkedHashSet<>()).add(key);
        
        return cache.get(key);
    }
    
    public void put(K key, V value) {
        if (capacity <= 0) return;
        
        // Key exists, just update value and frequency
        if (cache.containsKey(key)) {
            cache.put(key, value);
            get(key); // Update frequency
            return;
        }
        
        // Evict if full
        if (cache.size() >= capacity) {
            // Get least frequently used key
            K evictKey = frequencyLists.get(minFrequency).iterator().next();
            frequencyLists.get(minFrequency).remove(evictKey);
            cache.remove(evictKey);
            frequencies.remove(evictKey);
        }
        
        // Add new key with frequency 1
        cache.put(key, value);
        frequencies.put(key, 1);
        minFrequency = 1;
        frequencyLists.computeIfAbsent(1, k -> new LinkedHashSet<>()).add(key);
    }
}
```

### 8. How does EnumMap work internally?
```java
public class EnumMapExample {
    enum Day {
        MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
    }
    
    public static void main(String[] args) {
        // Create EnumMap
        EnumMap<Day, String> schedule = new EnumMap<>(Day.class);
        
        // Populate map
        schedule.put(Day.MONDAY, "Work");
        schedule.put(Day.TUESDAY, "Work");
        schedule.put(Day.WEDNESDAY, "Work");
        schedule.put(Day.THURSDAY, "Work");
        schedule.put(Day.FRIDAY, "Work");
        schedule.put(Day.SATURDAY, "Leisure");
        schedule.put(Day.SUNDAY, "Leisure");
        
        // Print map
        System.out.println("Schedule: " + schedule);
        
        // Retrieval
        System.out.println("Wednesday: " + schedule.get(Day.WEDNESDAY));
        
        // Compare performance with HashMap
        int iterations = 10_000_000;
        
        // EnumMap
        EnumMap<Day, String> enumMap = new EnumMap<>(Day.class);
        Map<Day, String> hashMap = new HashMap<>();
        
        // Populate both maps
        for (Day day : Day.values()) {
            enumMap.put(day, day.name());
            hashMap.put(day, day.name());
        }
        
        // Test EnumMap lookup
        long start = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            enumMap.get(Day.WEDNESDAY);
        }
        long end = System.nanoTime();
        System.out.println("EnumMap lookup: " + (end - start) / iterations + " ns per operation");
        
        // Test HashMap lookup
        start = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            hashMap.get(Day.WEDNESDAY);
        }
        end = System.nanoTime();
        System.out.println("HashMap lookup: " + (end - start) / iterations + " ns per operation");
    }
}
```

**Interview Follow-up Question**: When would you use EnumMap instead of HashMap, and why is EnumMap typically more efficient?

**Answer**:
```
Schedule: {MONDAY=Work, TUESDAY=Work, WEDNESDAY=Work, THURSDAY=Work, FRIDAY=Work, SATURDAY=Leisure, SUNDAY=Leisure}
Wednesday: Work
EnumMap lookup: [smaller value] ns per operation
HashMap lookup: [larger value] ns per operation
```

For the follow-up:
1. **Use EnumMap when**:
   - Your keys are enum values
   - Memory efficiency is important
   - You need guaranteed iteration order (by enum declaration order)
   - You want maximum performance

2. **EnumMap is more efficient because**:
   - It uses an array internally, with enum ordinals as indices
   - No hash calculation is needed - direct array access
   - No collision handling is required
   - EnumMap doesn't need to create Entry objects for each mapping
   - Memory footprint is smaller - just a simple array
   - The implementation is simpler and more optimized for the specific use case

3. **Additional benefits**:
   - EnumMap guarantees iteration in the natural order of enum constants
   - EnumMap can't contain null keys (compile-time safety)
   - Performance doesn't degrade with more entries (unlike HashMap which may need rehashing)

### 9. How does WeakHashMap handle garbage collection?
```java
public class WeakHashMapExample {
    public static void main(String[] args) throws InterruptedException {
        // Create a WeakHashMap
        Map<Key, String> weakMap = new WeakHashMap<>();
        Map<Key, String> regularMap = new HashMap<>();
        
        // Create a key
        Key key1 = new Key("Key1");
        Key key2 = new Key("Key2");
        
        // Put entries in both maps
        weakMap.put(key1, "Weak HashMap Value");
        regularMap.put(key2, "Regular HashMap Value");
        
        // Print the maps
        System.out.println("Before GC:");
        System.out.println("WeakHashMap: " + weakMap.size());
        System.out.println("HashMap: " + regularMap.size());
        
        // Remove strong references to keys
        key1 = null;
        key2 = null;
        
        // Suggest garbage collection
        System.gc();
        Thread.sleep(1000); // Wait for GC to run
        
        // Print the maps again
        System.out.println("\nAfter GC:");
        System.out.println("WeakHashMap: " + weakMap.size());
        System.out.println("HashMap: " + regularMap.size());
    }
    
    static class Key {
        private String id;
        
        public Key(String id) {
            this.id = id;
        }
        
        @Override
        public String toString() {
            return id;
        }
    }
}
```

**Interview Follow-up Question**: What are practical use cases for WeakHashMap in real-world applications?

**Answer**:
```
Before GC:
WeakHashMap: 1
HashMap: 1

After GC:
WeakHashMap: 0
HashMap: 1
```

For the follow-up, practical use cases for WeakHashMap include:
1. **Caching**: Implementing memory-sensitive caches where entries can be collected when memory is needed
2. **Resource management**: Association of metadata with objects without preventing garbage collection
3. **Object canonicalization**: Maintaining a pool of unique instances while allowing unused ones to be garbage collected
4. **Event listener management**: Storing listeners without creating memory leaks if a listener object is no longer referenced elsewhere
5. **Associated data**: Storing additional information about objects without extending their lifetime
6. **Memoization**: Caching calculation results tied to input objects, allowing garbage collection of results when inputs are no longer used
7. **Registry of observers**: Where the registry shouldn't prevent observers from being garbage collected

### 10. How does Comparator.comparing work?
```java
public class ComparatorExample {
    static class Person {
        private String firstName;
        private String lastName;
        private int age;
        
        public Person(String firstName, String lastName, int age) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.age = age;
        }
        
        public String getFirstName() { return firstName; }
        public String getLastName() { return lastName; }
        public int getAge() { return age; }
        
        @Override
        public String toString() {
            return firstName + " " + lastName + " (" + age + ")";
        }
    }
    
    public static void main(String[] args) {
        List<Person> people = Arrays.asList(
            new Person("John", "Doe", 25),
            new Person("Alice", "Smith", 30),
            new Person("Bob", "Johnson", 20),
            new Person("Alice", "Brown", 35)
        );
        
        // Sort by first name
        Collections.sort(people, Comparator.comparing(Person::getFirstName));
        System.out.println("Sorted by first name: " + people);
        
        // Sort by age
        Collections.sort(people, Comparator.comparing(Person::getAge));
        System.out.println("Sorted by age: " + people);
        
        // Multiple level sorting
        Collections.sort(people, 
            Comparator.comparing(Person::getFirstName)
                     .thenComparing(Person::getLastName));
        System.out.println("Sorted by first name, then last name: " + people);
        
        // Reverse sorting
        Collections.sort(people, Comparator.comparing(Person::getAge).reversed());
        System.out.println("Sorted by age (descending): " + people);
        
        // Null handling
        List<String> strings = Arrays.asList("B", null, "A", "C", null);
        Collections.sort(strings, Comparator.nullsFirst(Comparator.naturalOrder()));
        System.out.println("Strings with nulls first: " + strings);
        
        Collections.sort(strings, Comparator.nullsLast(Comparator.naturalOrder()));
        System.out.println("Strings with nulls last: " + strings);
    }
}
```

**Interview Follow-up Question**: How would you implement your own multi-field custom Comparator prior to Java 8's Comparator.comparing?

**Answer**:
```
Sorted by first name: [Alice Brown (35), Alice Smith (30), Bob Johnson (20), John Doe (25)]
Sorted by age: [Bob Johnson (20), John Doe (25), Alice Smith (30), Alice Brown (35)]
Sorted by first name, then last name: [Alice Brown (35), Alice Smith (30), Bob Johnson (20), John Doe (25)]
Sorted by age (descending): [Alice Brown (35), Alice Smith (30), John Doe (25), Bob Johnson (20)]
Strings with nulls first: [null, null, A, B, C]
Strings with nulls last: [A, B, C, null, null]
```

For the follow-up, implementing a multi-field custom Comparator before Java 8:
```java
// Prior to Java 8
Comparator<Person> comparator = new Comparator<Person>() {
    @Override
    public int compare(Person p1, Person p2) {
        // First compare by firstName
        int result = p1.getFirstName().compareTo(p2.getFirstName());
        if (result != 0) {
            return result;
        }
        
        // If firstName is the same, compare by lastName
        result = p1.getLastName().compareTo(p2.getLastName());
        if (result != 0) {
            return result;
        }
        
        // If lastName is also the same, compare by age
        return Integer.compare(p1.getAge(), p2.getAge());
    }
};
```

Challenges with pre-Java 8 Comparators:
1. More verbose and error-prone
2. Less reusable and composable
3. No built-in support for handling nulls or reverse ordering
4. Manual chaining of comparisons
5. Harder to read and maintain
