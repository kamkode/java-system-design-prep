# Hash Tables

Hash tables are fundamental data structures that provide efficient insertion, deletion, and lookup operations. This document covers key concepts, operations, and common interview questions related to hash tables.

## Table of Contents
1. [Introduction to Hash Tables](#introduction-to-hash-tables)
2. [Hash Functions](#hash-functions)
3. [Collision Resolution](#collision-resolution)
4. [Operations and Time Complexity](#operations-and-time-complexity)
5. [Implementation in Java](#implementation-in-java)
6. [Common Interview Questions](#common-interview-questions)
7. [Tips and Tricks](#tips-and-tricks)
8. [Practice Resources](#practice-resources)

## Introduction to Hash Tables

A hash table (also known as a hash map) is a data structure that implements an associative array abstract data type, a structure that can map keys to values. It uses a hash function to compute an index into an array of buckets or slots, from which the desired value can be found.

### Key Features
- Fast lookups: Ideally O(1) time complexity for search, insert, and delete operations
- Key-value storage: Each value is associated with a unique key
- Dynamic size: Can grow or shrink as needed (in most implementations)
- Widely used: Found in many applications including databases, caches, and symbol tables

### Real-world Applications
- Database indexing
- Caching
- Symbol tables in compilers
- Associative arrays
- Counting frequencies (e.g., word count)
- Detecting duplicates
- Implementing sets

## Hash Functions

A hash function is a function that maps data of arbitrary size to fixed-size values. The values returned by a hash function are called hash values, hash codes, digests, or simply hashes.

### Properties of a Good Hash Function
1. **Deterministic**: The same input should always produce the same output
2. **Efficient**: Should be fast to compute
3. **Uniform Distribution**: Should distribute keys uniformly across the hash table
4. **Low Collision Rate**: Different keys should rarely hash to the same value

### Common Hash Functions

#### Division Method
```java
int hash(K key) {
    return key.hashCode() % tableSize;
}
```

#### Multiplication Method
```java
int hash(K key) {
    double A = 0.6180339887; // (sqrt(5) - 1) / 2
    double val = key.hashCode() * A;
    return (int) (tableSize * (val - Math.floor(val)));
}
```

#### Universal Hashing
```java
int hash(K key, int a, int b, int p, int m) {
    // a and b are random numbers
    // p is a prime number larger than the universe of keys
    // m is the size of the hash table
    return ((a * key.hashCode() + b) % p) % m;
}
```

## Collision Resolution

A collision occurs when two different keys hash to the same index. There are several methods to handle collisions:

### 1. Separate Chaining
In separate chaining, each bucket of the hash table points to a linked list of elements that hash to the same index. If multiple keys hash to the same index, they are stored in the linked list.

```java
class HashTableSeparateChaining<K, V> {
    private class Node {
        K key;
        V value;
        Node next;
        
        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
    
    private Node[] buckets;
    private int size;
    private int capacity;
    
    public HashTableSeparateChaining(int capacity) {
        this.capacity = capacity;
        this.buckets = new Node[capacity];
        this.size = 0;
    }
    
    private int hash(K key) {
        return Math.abs(key.hashCode()) % capacity;
    }
    
    public void put(K key, V value) {
        int index = hash(key);
        Node head = buckets[index];
        
        // Check if key already exists
        while (head != null) {
            if (head.key.equals(key)) {
                head.value = value; // Update value
                return;
            }
            head = head.next;
        }
        
        // Key doesn't exist, add new node at the beginning of the list
        Node newNode = new Node(key, value);
        newNode.next = buckets[index];
        buckets[index] = newNode;
        size++;
    }
    
    public V get(K key) {
        int index = hash(key);
        Node head = buckets[index];
        
        while (head != null) {
            if (head.key.equals(key)) {
                return head.value;
            }
            head = head.next;
        }
        
        return null; // Key not found
    }
    
    public void remove(K key) {
        int index = hash(key);
        Node head = buckets[index];
        Node prev = null;
        
        while (head != null) {
            if (head.key.equals(key)) {
                if (prev == null) {
                    buckets[index] = head.next; // Remove first node
                } else {
                    prev.next = head.next; // Remove node in the middle or end
                }
                size--;
                return;
            }
            prev = head;
            head = head.next;
        }
    }
    
    public boolean containsKey(K key) {
        return get(key) != null;
    }
    
    public int size() {
        return size;
    }
    
    public boolean isEmpty() {
        return size == 0;
    }
}
```

### 2. Open Addressing
In open addressing, all elements are stored in the hash table itself. When a collision occurs, we find another place in the table by using a probing sequence.

#### Linear Probing
```java
int hash(K key, int i) {
    return (hash(key) + i) % tableSize;
}
```

#### Quadratic Probing
```java
int hash(K key, int i) {
    return (hash(key) + i*i) % tableSize;
}
```

#### Double Hashing
```java
int hash(K key, int i) {
    return (hash1(key) + i * hash2(key)) % tableSize;
}
```

### 3. Robin Hood Hashing
A variation of open addressing where elements are moved to minimize the variance of probe sequence lengths.

### 4. Cuckoo Hashing
Uses multiple hash functions and moves elements between their possible locations to make room for new elements.

## Operations and Time Complexity

| Operation | Average Case | Worst Case |
|-----------|--------------|------------|
| Insert    | O(1)         | O(n)       |
| Delete    | O(1)         | O(n)       |
| Search    | O(1)         | O(n)       |

The worst-case time complexity is O(n) when all keys hash to the same index, creating a long chain (for separate chaining) or requiring many probes (for open addressing).

## Implementation in Java

Java provides built-in hash table implementations:

### HashMap
```java
import java.util.HashMap;

public class HashMapExample {
    public static void main(String[] args) {
        // Create a HashMap
        HashMap<String, Integer> map = new HashMap<>();
        
        // Add key-value pairs
        map.put("apple", 5);
        map.put("banana", 10);
        map.put("orange", 15);
        
        // Get a value
        int appleCount = map.get("apple");
        System.out.println("Apple count: " + appleCount);
        
        // Check if a key exists
        boolean hasGrape = map.containsKey("grape");
        System.out.println("Has grape: " + hasGrape);
        
        // Remove a key-value pair
        map.remove("banana");
        
        // Iterate through the map
        for (String key : map.keySet()) {
            System.out.println(key + ": " + map.get(key));
        }
    }
}
```

### HashSet
```java
import java.util.HashSet;

public class HashSetExample {
    public static void main(String[] args) {
        // Create a HashSet
        HashSet<String> set = new HashSet<>();
        
        // Add elements
        set.add("apple");
        set.add("banana");
        set.add("orange");
        
        // Check if an element exists
        boolean hasApple = set.contains("apple");
        System.out.println("Has apple: " + hasApple);
        
        // Remove an element
        set.remove("banana");
        
        // Iterate through the set
        for (String fruit : set) {
            System.out.println(fruit);
        }
    }
}
```

## Common Interview Questions

1. **Two Sum**: Given an array of integers and a target sum, find two numbers that add up to the target.
   ```java
   public int[] twoSum(int[] nums, int target) {
       HashMap<Integer, Integer> map = new HashMap<>();
       for (int i = 0; i < nums.length; i++) {
           int complement = target - nums[i];
           if (map.containsKey(complement)) {
               return new int[] { map.get(complement), i };
           }
           map.put(nums[i], i);
       }
       return new int[] {};
   }
   ```

2. **Group Anagrams**: Group strings that are anagrams of each other.
   ```java
   public List<List<String>> groupAnagrams(String[] strs) {
       HashMap<String, List<String>> map = new HashMap<>();
       for (String s : strs) {
           char[] chars = s.toCharArray();
           Arrays.sort(chars);
           String key = new String(chars);
           if (!map.containsKey(key)) {
               map.put(key, new ArrayList<>());
           }
           map.get(key).add(s);
       }
       return new ArrayList<>(map.values());
   }
   ```

3. **LRU Cache**: Implement a Least Recently Used (LRU) cache.
   ```java
   class LRUCache {
       private class Node {
           int key;
           int value;
           Node prev;
           Node next;
       }
       
       private HashMap<Integer, Node> map;
       private Node head, tail;
       private int capacity;
       
       public LRUCache(int capacity) {
           this.capacity = capacity;
           map = new HashMap<>();
           head = new Node();
           tail = new Node();
           head.next = tail;
           tail.prev = head;
       }
       
       public int get(int key) {
           if (!map.containsKey(key)) {
               return -1;
           }
           
           Node node = map.get(key);
           removeNode(node);
           addToHead(node);
           return node.value;
       }
       
       public void put(int key, int value) {
           if (map.containsKey(key)) {
               Node node = map.get(key);
               node.value = value;
               removeNode(node);
               addToHead(node);
           } else {
               if (map.size() == capacity) {
                   map.remove(tail.prev.key);
                   removeNode(tail.prev);
               }
               
               Node newNode = new Node();
               newNode.key = key;
               newNode.value = value;
               map.put(key, newNode);
               addToHead(newNode);
           }
       }
       
       private void removeNode(Node node) {
           node.prev.next = node.next;
           node.next.prev = node.prev;
       }
       
       private void addToHead(Node node) {
           node.next = head.next;
           node.prev = head;
           head.next.prev = node;
           head.next = node;
       }
   }
   ```

4. **First Unique Character**: Find the first non-repeating character in a string.
   ```java
   public int firstUniqChar(String s) {
       HashMap<Character, Integer> map = new HashMap<>();
       
       // Count frequency of each character
       for (char c : s.toCharArray()) {
           map.put(c, map.getOrDefault(c, 0) + 1);
       }
       
       // Find the first character with frequency 1
       for (int i = 0; i < s.length(); i++) {
           if (map.get(s.charAt(i)) == 1) {
               return i;
           }
       }
       
       return -1;
   }
   ```

5. **Longest Substring Without Repeating Characters**: Find the length of the longest substring without repeating characters.
   ```java
   public int lengthOfLongestSubstring(String s) {
       HashMap<Character, Integer> map = new HashMap<>();
       int maxLength = 0;
       int start = 0;
       
       for (int end = 0; end < s.length(); end++) {
           char c = s.charAt(end);
           if (map.containsKey(c)) {
               start = Math.max(start, map.get(c) + 1);
           }
           map.put(c, end);
           maxLength = Math.max(maxLength, end - start + 1);
       }
       
       return maxLength;
   }
   ```

6. **Valid Sudoku**: Determine if a 9x9 Sudoku board is valid.
7. **Isomorphic Strings**: Determine if two strings are isomorphic.
8. **Word Pattern**: Given a pattern and a string, find if the string follows the same pattern.
9. **Subarray Sum Equals K**: Find the number of subarrays that sum to k.
10. **Design HashMap**: Implement a HashMap without using any built-in hash table libraries.

## Tips and Tricks

1. **Use Hash Tables for O(1) Lookups**: When you need to quickly check if an element exists or retrieve a value associated with a key, hash tables are often the best choice.

2. **Handle Collisions Properly**: Choose an appropriate collision resolution strategy based on your use case.

3. **Choose a Good Hash Function**: A good hash function distributes keys uniformly across the table, reducing collisions.

4. **Consider Load Factor**: The load factor (number of entries / table size) affects performance. Typically, hash tables resize when the load factor exceeds a threshold (e.g., 0.75).

5. **Use HashSet for Deduplication**: HashSet is perfect for removing duplicates from a collection.

6. **Two-Pass Algorithm with Hash Table**: Many problems can be solved by making two passes through the data, using a hash table to store information from the first pass.

7. **Frequency Counting**: Hash tables are excellent for counting frequencies of elements.
   ```java
   HashMap<Integer, Integer> frequencyMap = new HashMap<>();
   for (int num : nums) {
       frequencyMap.put(num, frequencyMap.getOrDefault(num, 0) + 1);
   }
   ```

8. **Custom Objects as Keys**: When using custom objects as keys in a hash table, make sure to override both `hashCode()` and `equals()` methods.
   ```java
   class Person {
       String name;
       int age;
       
       // Constructor, getters, setters...
       
       @Override
       public boolean equals(Object o) {
           if (this == o) return true;
           if (o == null || getClass() != o.getClass()) return false;
           Person person = (Person) o;
           return age == person.age && Objects.equals(name, person.name);
       }
       
       @Override
       public int hashCode() {
           return Objects.hash(name, age);
       }
   }
   ```

9. **Caching Results**: Use hash tables to cache results of expensive operations.

10. **Sliding Window with Hash Table**: For substring problems, combine the sliding window technique with a hash table.

## Practice Resources

- [LeetCode Hash Table](https://leetcode.com/tag/hash-table/)
- [HackerRank Hash Tables](https://www.hackerrank.com/domains/data-structures?filters%5Bsubdomains%5D%5B%5D=hash-tables)
- [GeeksforGeeks Hash](https://www.geeksforgeeks.org/hashing-data-structure/)