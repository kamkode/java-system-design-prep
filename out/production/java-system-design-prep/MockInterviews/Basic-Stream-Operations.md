# Basic Stream Operations - Interview Questions

This file contains interview questions focused on basic Java 8+ Stream API operations including stream creation, filtering, mapping, and collecting results.

## Stream Creation and Simple Operations

### 1. How many ways can you create a Stream in Java?
```java
public class StreamCreationExample {
    public static void main(String[] args) {
        // Method 1: From Collection
        List<String> list = Arrays.asList("a", "b", "c");
        Stream<String> streamFromCollection = list.stream();
        System.out.println("From Collection: " + streamFromCollection.count());
        
        // Method 2: From Array
        String[] array = {"a", "b", "c"};
        Stream<String> streamFromArray = Arrays.stream(array);
        System.out.println("From Array: " + streamFromArray.count());
        
        // Method 3: Using Stream.of
        Stream<String> streamOfElements = Stream.of("a", "b", "c");
        System.out.println("Using Stream.of: " + streamOfElements.count());
        
        // Method 4: Empty Stream
        Stream<String> emptyStream = Stream.empty();
        System.out.println("Empty Stream: " + emptyStream.count());
        
        // Method 5: Using Stream.iterate
        Stream<Integer> infiniteStream = Stream.iterate(0, n -> n + 2);
        System.out.println("First 5 even numbers: ");
        infiniteStream.limit(5).forEach(System.out::println);
        
        // Method 6: Using Stream.generate
        Stream<Double> randomStream = Stream.generate(Math::random);
        System.out.println("5 random numbers: ");
        randomStream.limit(5).forEach(System.out::println);
        
        // Method 7: From String characters
        IntStream streamOfChars = "abc".chars();
        System.out.println("Characters as ints: ");
        streamOfChars.forEach(c -> System.out.print((char)c + " "));
        
        // Method 8: From File lines
        try {
            Stream<String> streamOfLines = Files.lines(Paths.get("file.txt"));
            System.out.println("\nFile has " + streamOfLines.count() + " lines");
            streamOfLines.close();
        } catch (IOException e) {
            System.out.println("File not found");
        }
    }
}
```

**Interview Follow-up Question**: What are the key differences between Stream.iterate() and Stream.generate()?

**Answer**: The output will show counts of various streams and examples of different creation methods. 

For the follow-up question:
1. **Stream.iterate()** 
   - Takes an initial value and a function to generate the next value
   - Each value depends on the previous value
   - Produces deterministic sequences
   - Example: `Stream.iterate(0, n -> n + 2)` produces even numbers 0, 2, 4, 6, ...

2. **Stream.generate()**
   - Takes a Supplier to generate each value
   - Each value is generated independently
   - Often used for random or non-deterministic sequences
   - Example: `Stream.generate(Math::random)` produces random numbers

The key difference is that iterate creates a sequence where each element depends on the previous one, while generate creates elements independently.

### 2. What happens when you use a Stream twice?
```java
public class StreamReuseExample {
    public static void main(String[] args) {
        List<String> list = Arrays.asList("apple", "banana", "cherry");
        Stream<String> stream = list.stream();
        
        // First operation
        System.out.println("Count: " + stream.count());
        
        try {
            // Second operation on the same stream
            System.out.println("First element: " + stream.findFirst().orElse("None"));
        } catch (Exception e) {
            System.out.println("Exception: " + e.getClass().getSimpleName());
            System.out.println("Message: " + e.getMessage());
        }
        
        // Creating a new stream for each operation works fine
        System.out.println("Count (new stream): " + list.stream().count());
        System.out.println("First element (new stream): " + 
                           list.stream().findFirst().orElse("None"));
    }
}
```

**Interview Follow-up Question**: What design pattern does the Stream API implement and why?

**Answer**:
```
Count: 3
Exception: IllegalStateException
Message: stream has already been operated upon or closed
Count (new stream): 3
First element (new stream): apple
```

For the follow-up question: The Stream API implements the **Builder Pattern**. This is evident in:

1. The API's fluent interface, allowing method chaining (`stream.filter().map().collect()`)
2. The separation between building the pipeline (intermediate operations) and executing it (terminal operation)
3. The immutability of each step - each operation returns a new Stream
4. The lazy evaluation - nothing happens until a terminal operation is called

This pattern provides a clear, readable syntax and allows for optimization of the execution plan. It also enforces the "use once" behavior, which is necessary for the pipeline execution model.

### 3. How does the filter operation work?
```java
public class StreamFilterExample {
    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        
        System.out.println("Original numbers: " + numbers);
        
        // Filter even numbers
        List<Integer> evenNumbers = numbers.stream()
                                          .filter(n -> {
                                              System.out.println("Filtering " + n);
                                              return n % 2 == 0;
                                          })
                                          .collect(Collectors.toList());
        
        System.out.println("Even numbers: " + evenNumbers);
        
        // Filter and limit
        List<Integer> firstTwoEven = numbers.stream()
                                           .filter(n -> {
                                               System.out.println("Filtering with limit: " + n);
                                               return n % 2 == 0;
                                           })
                                           .limit(2)
                                           .collect(Collectors.toList());
        
        System.out.println("First two even numbers: " + firstTwoEven);
    }
}
```

**Interview Follow-up Question**: How does the addition of the limit operation change the execution behavior of the stream?

**Answer**:
```
Original numbers: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
Filtering 1
Filtering 2
Filtering 3
Filtering 4
Filtering 5
Filtering 6
Filtering 7
Filtering 8
Filtering 9
Filtering 10
Even numbers: [2, 4, 6, 8, 10]
Filtering with limit: 1
Filtering with limit: 2
Filtering with limit: 3
Filtering with limit: 4
First two even numbers: [2, 4]
```

For the follow-up question:
1. Without limit, the filter operation processes all elements in the source
2. With limit, the stream stops processing elements once it has found enough elements that match the filter condition
3. This demonstrates stream's **short-circuiting** behavior
4. Short-circuiting operations (like limit, findFirst, anyMatch) can terminate processing early
5. This is especially valuable with large or infinite streams
6. In the example, the second stream stops after finding the first two even numbers (2 and 4) and doesn't process the remaining elements (5-10)

This behavior showcases the laziness and efficiency of the Stream API, evaluating only what's necessary to produce the required result.

### 4. How do map() and flatMap() differ?
```java
public class MapVsFlatMapExample {
    public static void main(String[] args) {
        // Example with map
        List<String> words = Arrays.asList("Java", "Streams", "API");
        
        // Using map to get lengths
        List<Integer> wordLengths = words.stream()
                                        .map(String::length)
                                        .collect(Collectors.toList());
        
        System.out.println("Original words: " + words);
        System.out.println("Word lengths using map: " + wordLengths);
        
        // Example of map with complex objects
        List<List<Integer>> listOfLists = Arrays.asList(
            Arrays.asList(1, 2, 3),
            Arrays.asList(4, 5, 6),
            Arrays.asList(7, 8, 9)
        );
        
        // Using map - gives a stream of streams
        List<Stream<Integer>> streamOfStreams = listOfLists.stream()
                                                         .map(List::stream)
                                                         .collect(Collectors.toList());
        
        System.out.println("Using map on nested lists: " + streamOfStreams);
        
        // Using flatMap - flattens to a single stream
        List<Integer> flattenedList = listOfLists.stream()
                                                .flatMap(List::stream)
                                                .collect(Collectors.toList());
        
        System.out.println("Using flatMap on nested lists: " + flattenedList);
        
        // Real world example: Getting all unique characters from a list of words
        List<String> uniqueChars = words.stream()
                                      .flatMap(word -> Arrays.stream(word.split("")))
                                      .distinct()
                                      .collect(Collectors.toList());
        
        System.out.println("All unique characters: " + uniqueChars);
    }
}
```

**Interview Follow-up Question**: In what real-world scenarios would you specifically need flatMap instead of map?

**Answer**:
```
Original words: [Java, Streams, API]
Word lengths using map: [4, 7, 3]
Using map on nested lists: [java.util.stream.ReferencePipeline$Head@...]
Using flatMap on nested lists: [1, 2, 3, 4, 5, 6, 7, 8, 9]
All unique characters: [J, a, v, S, t, r, e, m, s, A, P, I]
```

For the follow-up question, real-world scenarios where flatMap is necessary:

1. **Database queries returning one-to-many relationships**:
   - When each entity is associated with multiple related entities (e.g., each Order has multiple OrderItems)
   - `orders.stream().flatMap(order -> order.getItems().stream())`

2. **Processing nested collections**:
   - When dealing with multi-dimensional data like matrices or graph structures
   - Example: Processing all cells in a spreadsheet: `rows.stream().flatMap(row -> row.getCells().stream())`

3. **Text processing**:
   - Extracting words from multiple documents: `documents.stream().flatMap(doc -> Arrays.stream(doc.split("\\s+")))`
   - Analysis of multiple texts: `texts.stream().flatMap(text -> Pattern.compile("\\W").splitAsStream(text))`

4. **API responses with nested structures**:
   - JSON APIs often return nested arrays that need flattening
   - `apiResponses.stream().flatMap(response -> response.getResults().stream())`

5. **Optional handling in functional code**:
   - Using `flatMap` with `Optional` to avoid nested optionals
   - `userOptional.flatMap(User::getAddressOptional)` instead of map that would return `Optional<Optional<Address>>`

6. **Event processing systems**:
   - When one event generates multiple sub-events
   - `events.stream().flatMap(Event::generateSubEvents)`

### 5. How would you count the frequency of each word in a text?
```java
public class WordFrequencyExample {
    public static void main(String[] args) {
        String text = "Java Stream Stream API API API example example example";
        
        // Split into words and convert to lowercase
        String[] words = text.toLowerCase().split("\\s+");
        
        // Method 1: Using imperative approach
        Map<String, Long> wordCountImperative = new HashMap<>();
        for (String word : words) {
            if (wordCountImperative.containsKey(word)) {
                wordCountImperative.put(word, wordCountImperative.get(word) + 1);
            } else {
                wordCountImperative.put(word, 1L);
            }
        }
        System.out.println("Word frequency (imperative): " + wordCountImperative);
        
        // Method 2: Using Stream API with forEach
        Map<String, Long> wordCountStream1 = new HashMap<>();
        Arrays.stream(words).forEach(word -> 
            wordCountStream1.merge(word, 1L, Long::sum)
        );
        System.out.println("Word frequency (stream with forEach): " + wordCountStream1);
        
        // Method 3: Using Stream API with Collectors.groupingBy
        Map<String, Long> wordCountStream2 = Arrays.stream(words)
            .collect(Collectors.groupingBy(
                Function.identity(),
                Collectors.counting()
            ));
        System.out.println("Word frequency (stream with collectors): " + wordCountStream2);
        
        // Method 4: Getting most frequent words
        String mostFrequentWord = Arrays.stream(words)
            .collect(Collectors.groupingBy(
                Function.identity(),
                Collectors.counting()
            ))
            .entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse("");
        
        System.out.println("Most frequent word: " + mostFrequentWord);
    }
}
```

**Interview Follow-up Question**: How would you modify this code to find the top 3 most frequent words?

**Answer**:
```
Word frequency (imperative): {example=3, api=3, stream=2, java=1}
Word frequency (stream with forEach): {example=3, api=3, stream=2, java=1}
Word frequency (stream with collectors): {example=3, api=3, stream=2, java=1}
Most frequent word: example
```

For the follow-up question, to find the top 3 most frequent words:

```java
// Find top 3 most frequent words
List<String> top3Words = Arrays.stream(words)
    .collect(Collectors.groupingBy(
        Function.identity(),
        Collectors.counting()
    ))
    .entrySet().stream()
    .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
    .limit(3)
    .map(Map.Entry::getKey)
    .collect(Collectors.toList());

System.out.println("Top 3 most frequent words: " + top3Words);
```

This code:
1. Groups words and counts their occurrences (same as before)
2. Converts to a stream of Map.Entry objects
3. Sorts by value in reverse order (highest count first)
4. Limits to the first 3 entries
5. Extracts the key (word) from each entry
6. Collects the result to a list

The output would be:
```
Top 3 most frequent words: [example, api, stream]
```

Notice that "example" and "api" both appear 3 times, and "stream" appears 2 times. If there were a tie for the third place, this code would arbitrarily select based on the sorting behavior.

### 6. How can you join String elements from a Stream?
```java
public class StringJoinExample {
    public static void main(String[] args) {
        List<String> fruits = Arrays.asList("Apple", "Banana", "Cherry", "Date");
        
        // Method 1: Using Collectors.joining()
        String result1 = fruits.stream()
                              .collect(Collectors.joining());
        System.out.println("Simple joining: " + result1);
        
        // Method 2: Using Collectors.joining() with delimiter
        String result2 = fruits.stream()
                              .collect(Collectors.joining(", "));
        System.out.println("Joining with delimiter: " + result2);
        
        // Method 3: Using Collectors.joining() with prefix and suffix
        String result3 = fruits.stream()
                              .collect(Collectors.joining(", ", "[", "]"));
        System.out.println("Joining with delimiter, prefix, suffix: " + result3);
        
        // Method 4: Using String.join (static method)
        String result4 = String.join(" - ", fruits);
        System.out.println("Using String.join: " + result4);
        
        // Method 5: Joining with transformation
        String result5 = fruits.stream()
                              .map(String::toLowerCase)
                              .collect(Collectors.joining(", "));
        System.out.println("Joining with transformation: " + result5);
        
        // Method 6: Conditional joining
        String result6 = fruits.stream()
                              .filter(f -> f.length() > 5)  // Only longer fruit names
                              .collect(Collectors.joining(", "));
        System.out.println("Conditional joining: " + result6);
    }
}
```

**Interview Follow-up Question**: What are the performance implications of different String joining methods in Java, and when might StringBuilder be preferable?

**Answer**:
```
Simple joining: AppleBananaCharryDate
Joining with delimiter: Apple, Banana, Cherry, Date
Joining with delimiter, prefix, suffix: [Apple, Banana, Cherry, Date]
Using String.join: Apple - Banana - Cherry - Date
Joining with transformation: apple, banana, cherry, date
Conditional joining: Banana, Cherry
```

For the follow-up question:

1. **Performance considerations**:
   - `Collectors.joining()` uses a `StringBuilder` internally, making it efficient for streams
   - `String.join()` also uses a `StringBuilder` internally
   - String concatenation with `+` in a loop creates many intermediate String objects and should be avoided

2. **When StringBuilder is preferable**:
   - When joining strings in a loop with complex logic that doesn't fit well with streams
   - When building strings incrementally with varied content (not just joining similar elements)
   - When you need precise control over buffer sizing
   - When appending different types of data beyond just strings
   - When working with very large strings where controlling memory allocation is important

3. **Code example with StringBuilder**:
   ```java
   StringBuilder sb = new StringBuilder(256); // Pre-allocate buffer size
   for (int i = 0; i < fruits.size(); i++) {
       sb.append(fruits.get(i));
       if (i < fruits.size() - 1) {
           sb.append(", ");
       }
   }
   String result = sb.toString();
   ```

4. **Practical advice**:
   - For simple string joining from collections, `Collectors.joining()` is clean and efficient
   - For complex string building with mixed content types, `StringBuilder` gives more control
   - Micro-optimization is rarely needed as both approaches are efficient

### 7. How can you process a stream conditionally?
```java
public class ConditionalStreamProcessingExample {
    static class Person {
        private String name;
        private int age;
        private String gender;
        
        public Person(String name, int age, String gender) {
            this.name = name;
            this.age = age;
            this.gender = gender;
        }
        
        public String getName() { return name; }
        public int getAge() { return age; }
        public String getGender() { return gender; }
        
        @Override
        public String toString() {
            return name + " (" + age + ", " + gender + ")";
        }
    }
    
    public static void main(String[] args) {
        List<Person> people = Arrays.asList(
            new Person("Alice", 30, "F"),
            new Person("Bob", 25, "M"),
            new Person("Charlie", 35, "M"),
            new Person("Diana", 28, "F"),
            new Person("Edward", 17, "M"),
            new Person("Fiona", 16, "F")
        );
        
        System.out.println("All people: " + people);
        
        // Basic filtering
        List<Person> adults = people.stream()
                                   .filter(p -> p.getAge() >= 18)
                                   .collect(Collectors.toList());
        System.out.println("Adults: " + adults);
        
        // Partitioning - divides into two groups based on a predicate
        Map<Boolean, List<Person>> adultPartition = people.stream()
                                                         .collect(Collectors.partitioningBy(p -> p.getAge() >= 18));
        System.out.println("Adults (partitioning): " + adultPartition.get(true));
        System.out.println("Minors (partitioning): " + adultPartition.get(false));
        
        // Grouping - divides into multiple groups
        Map<String, List<Person>> byGender = people.stream()
                                                  .collect(Collectors.groupingBy(Person::getGender));
        System.out.println("Grouped by gender: " + byGender);
        
        // Conditional mapping
        List<String> names = people.stream()
                                  .map(p -> {
                                      if (p.getAge() >= 18) {
                                          return p.getName() + " (Adult)";
                                      } else {
                                          return p.getName() + " (Minor)";
                                      }
                                  })
                                  .collect(Collectors.toList());
        System.out.println("Conditional mapping: " + names);
        
        // Complex conditional processing
        Map<String, Double> averageAgeByCategory = people.stream()
            .collect(Collectors.groupingBy(
                p -> {
                    if (p.getAge() < 18) return "Minor";
                    else if (p.getAge() < 30) return "Young Adult";
                    else return "Adult";
                },
                Collectors.averagingInt(Person::getAge)
            ));
        System.out.println("Average age by category: " + averageAgeByCategory);
    }
}
```

**Interview Follow-up Question**: How would you refactor this code to use a more functional approach for the category determination?

**Answer**:
```
All people: [Alice (30, F), Bob (25, M), Charlie (35, M), Diana (28, F), Edward (17, M), Fiona (16, F)]
Adults: [Alice (30, F), Bob (25, M), Charlie (35, M), Diana (28, F)]
Adults (partitioning): [Alice (30, F), Bob (25, M), Charlie (35, M), Diana (28, F)]
Minors (partitioning): [Edward (17, M), Fiona (16, F)]
Grouped by gender: {F=[Alice (30, F), Diana (28, F), Fiona (16, F)], M=[Bob (25, M), Charlie (35, M), Edward (17, M)]}
Conditional mapping: [Alice (Adult), Bob (Adult), Charlie (Adult), Diana (Adult), Edward (Minor), Fiona (Minor)]
Average age by category: {Adult=32.5, Minor=16.5, Young Adult=26.5}
```

For the follow-up question, a more functional approach to determine categories:

```java
// Define a function to determine category
Function<Person, String> categoryFunction = person -> {
    if (person.getAge() < 18) return "Minor";
    else if (person.getAge() < 30) return "Young Adult";
    else return "Adult";
};

// Use the function in the stream
Map<String, Double> averageAgeByCategory = people.stream()
    .collect(Collectors.groupingBy(
        categoryFunction,
        Collectors.averagingInt(Person::getAge)
    ));
```

This refactoring improves the code by:
1. Separating the category determination logic from the stream pipeline
2. Making the function reusable across different stream operations
3. Improving readability by giving the function a descriptive name
4. Following functional programming principles by defining a pure function
5. Making it easier to test the category logic independently
6. Allowing the function to be passed as a parameter to other methods

### 8. How can you convert stream results to different collection types?
```java
public class StreamCollectionConversionExample {
    public static void main(String[] args) {
        List<String> sourceList = Arrays.asList("apple", "banana", "cherry", "date", "apple");
        
        System.out.println("Source list: " + sourceList);
        
        // Convert to List (default is ArrayList)
        List<String> resultList = sourceList.stream()
                                           .filter(s -> s.length() > 5)
                                           .collect(Collectors.toList());
        System.out.println("To List: " + resultList);
        System.out.println("List class: " + resultList.getClass().getName());
        
        // Convert to Set (removes duplicates)
        Set<String> resultSet = sourceList.stream()
                                         .collect(Collectors.toSet());
        System.out.println("To Set: " + resultSet);
        System.out.println("Set class: " + resultSet.getClass().getName());
        
        // Convert to specific Set implementation
        Set<String> resultTreeSet = sourceList.stream()
                                            .collect(Collectors.toCollection(TreeSet::new));
        System.out.println("To TreeSet: " + resultTreeSet);
        System.out.println("TreeSet class: " + resultTreeSet.getClass().getName());
        
        // Convert to Map
        Map<String, Integer> resultMap = sourceList.stream()
                                                 .distinct()
                                                 .collect(Collectors.toMap(
                                                     Function.identity(),  // key is the string itself
                                                     String::length        // value is the string length
                                                 ));
        System.out.println("To Map: " + resultMap);
        
        // Convert to custom collection type
        LinkedList<String> resultLinkedList = sourceList.stream()
                                                      .collect(Collectors.toCollection(LinkedList::new));
        System.out.println("To LinkedList: " + resultLinkedList);
        
        // Convert to array
        String[] resultArray = sourceList.stream()
                                        .toArray(String[]::new);
        System.out.println("To Array: " + Arrays.toString(resultArray));
        
        // Collect with joining
        String joined = sourceList.stream()
                                 .collect(Collectors.joining(", "));
        System.out.println("Joined: " + joined);
    }
}
```

**Interview Follow-up Question**: How would you handle duplicate keys when collecting to a Map using Collectors.toMap()?

**Answer**:
```
Source list: [apple, banana, cherry, date, apple]
To List: [banana, cherry]
List class: java.util.ArrayList
To Set: [apple, banana, cherry, date]
Set class: java.util.HashSet
To TreeSet: [apple, banana, cherry, date]
TreeSet class: java.util.TreeSet
To Map: {apple=5, banana=6, cherry=6, date=4}
To LinkedList: [apple, banana, cherry, date, apple]
To Array: [apple, banana, cherry, date, apple]
Joined: apple, banana, cherry, date, apple
```

For the follow-up question, handling duplicate keys in `Collectors.toMap()`:

```java
// Example with duplicate keys - this would throw IllegalStateException without a merge function
List<String> withDuplicates = Arrays.asList("apple", "banana", "apple", "date");

// Solution 1: Using merge function to keep the latest value
Map<String, Integer> map1 = withDuplicates.stream()
    .collect(Collectors.toMap(
        Function.identity(),     // key mapper
        String::length,          // value mapper
        (existing, replacement) -> replacement  // merge function - keep latest value
    ));
System.out.println("Map with latest values: " + map1);

// Solution 2: Using merge function to keep the first value
Map<String, Integer> map2 = withDuplicates.stream()
    .collect(Collectors.toMap(
        Function.identity(),
        String::length,
        (existing, replacement) -> existing  // merge function - keep first value
    ));
System.out.println("Map with first values: " + map2);

// Solution 3: Using merge function to combine values
Map<String, Integer> map3 = withDuplicates.stream()
    .collect(Collectors.toMap(
        Function.identity(),
        s -> 1,                            // Count 1 for each occurrence
        (count1, count2) -> count1 + count2  // merge function - sum the counts
    ));
System.out.println("Map with counts: " + map3);

// Solution 4: Using Collectors.groupingBy instead
Map<String, List<String>> map4 = withDuplicates.stream()
    .collect(Collectors.groupingBy(Function.identity()));
System.out.println("Grouped by key: " + map4);
```

This will output:
```
Map with latest values: {apple=5, banana=6, date=4}
Map with first values: {apple=5, banana=6, date=4}
Map with counts: {apple=2, banana=1, date=1}
Grouped by key: {apple=[apple, apple], banana=[banana], date=[date]}
```

The key points:
1. The third parameter of `toMap()` is a `BinaryOperator` that resolves conflicts
2. Without a merge function, `toMap()` throws an `IllegalStateException` on duplicate keys
3. Different merge strategies handle duplicates differently (keep first, keep last, combine values)
4. For scenarios requiring all values, `Collectors.groupingBy()` is a better choice
5. The choice of merge function depends on the business requirement
