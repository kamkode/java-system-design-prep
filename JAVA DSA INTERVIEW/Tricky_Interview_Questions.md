# Tricky DSA Interview Questions and Solutions

## 1. Find the Missing Number in an Array
**Problem**: Given an array of n-1 integers in the range of 1 to n, find the missing number.

**Solution**:
```java
public class MissingNumber {
    public static int findMissingNumber(int[] arr) {
        int n = arr.length + 1;
        int expectedSum = (n * (n + 1)) / 2;
        int actualSum = 0;
        
        for (int num : arr) {
            actualSum += num;
        }
        
        return expectedSum - actualSum;
    }
}
```

## 2. Detect Cycle in a Linked List
**Problem**: Given a linked list, determine if it has a cycle in it.

**Solution**:
```java
public class LinkedListCycle {
    public boolean hasCycle(ListNode head) {
        if (head == null || head.next == null) {
            return false;
        }
        
        ListNode slow = head;
        ListNode fast = head;
        
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            
            if (slow == fast) {
                return true;
            }
        }
        
        return false;
    }
}
```

## 3. Find the Kth Largest Element
**Problem**: Find the kth largest element in an unsorted array.

**Solution**:
```java
public class KthLargestElement {
    public int findKthLargest(int[] nums, int k) {
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        
        for (int num : nums) {
            minHeap.offer(num);
            if (minHeap.size() > k) {
                minHeap.poll();
            }
        }
        
        return minHeap.peek();
    }
}
```

## 4. LRU Cache Implementation
**Problem**: Design and implement a data structure for Least Recently Used (LRU) cache.

**Solution**:
```java
public class LRUCache {
    private class Node {
        int key;
        int value;
        Node prev;
        Node next;
        
        Node(int key, int value) {
            this.key = key;
            this.value = value;
        }
    }
    
    private Map<Integer, Node> cache;
    private int capacity;
    private Node head;
    private Node tail;
    
    public LRUCache(int capacity) {
        this.capacity = capacity;
        cache = new HashMap<>();
        head = new Node(0, 0);
        tail = new Node(0, 0);
        head.next = tail;
        tail.prev = head;
    }
    
    public int get(int key) {
        if (cache.containsKey(key)) {
            Node node = cache.get(key);
            remove(node);
            addToHead(node);
            return node.value;
        }
        return -1;
    }
    
    public void put(int key, int value) {
        if (cache.containsKey(key)) {
            Node node = cache.get(key);
            node.value = value;
            remove(node);
            addToHead(node);
        } else {
            Node newNode = new Node(key, value);
            cache.put(key, newNode);
            addToHead(newNode);
            if (cache.size() > capacity) {
                Node last = tail.prev;
                remove(last);
                cache.remove(last.key);
            }
        }
    }
    
    private void remove(Node node) {
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

## 5. Word Break Problem
**Problem**: Given a string s and a dictionary of words, determine if s can be segmented into a space-separated sequence of dictionary words.

**Solution**:
```java
public class WordBreak {
    public boolean wordBreak(String s, List<String> wordDict) {
        Set<String> wordSet = new HashSet<>(wordDict);
        boolean[] dp = new boolean[s.length() + 1];
        dp[0] = true;
        
        for (int i = 1; i <= s.length(); i++) {
            for (int j = 0; j < i; j++) {
                if (dp[j] && wordSet.contains(s.substring(j, i))) {
                    dp[i] = true;
                    break;
                }
            }
        }
        
        return dp[s.length()];
    }
}
```

## 6. Implement a Thread-Safe Singleton
**Problem**: Implement a thread-safe singleton class in Java.

**Solution**:
```java
public class Singleton {
    private static volatile Singleton instance;
    
    private Singleton() {}
    
    public static Singleton getInstance() {
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
}
```

## 7. Find the Longest Palindromic Substring
**Problem**: Given a string s, find the longest palindromic substring in s.

**Solution**:
```java
public class LongestPalindromicSubstring {
    public String longestPalindrome(String s) {
        if (s == null || s.length() < 1) return "";
        
        int start = 0, end = 0;
        for (int i = 0; i < s.length(); i++) {
            int len1 = expandAroundCenter(s, i, i);
            int len2 = expandAroundCenter(s, i, i + 1);
            int len = Math.max(len1, len2);
            
            if (len > end - start) {
                start = i - (len - 1) / 2;
                end = i + len / 2;
            }
        }
        
        return s.substring(start, end + 1);
    }
    
    private int expandAroundCenter(String s, int left, int right) {
        while (left >= 0 && right < s.length() && s.charAt(left) == s.charAt(right)) {
            left--;
            right++;
        }
        return right - left - 1;
    }
}
```

## 8. Implement a Rate Limiter
**Problem**: Design a rate limiter that allows at most N requests per second.

**Solution**:
```java
public class RateLimiter {
    private final int maxRequests;
    private final long timeWindow;
    private final Queue<Long> requestTimestamps;
    
    public RateLimiter(int maxRequests, long timeWindowInSeconds) {
        this.maxRequests = maxRequests;
        this.timeWindow = timeWindowInSeconds * 1000; // Convert to milliseconds
        this.requestTimestamps = new LinkedList<>();
    }
    
    public synchronized boolean allowRequest() {
        long currentTime = System.currentTimeMillis();
        
        // Remove timestamps older than the time window
        while (!requestTimestamps.isEmpty() && 
               currentTime - requestTimestamps.peek() > timeWindow) {
            requestTimestamps.poll();
        }
        
        if (requestTimestamps.size() < maxRequests) {
            requestTimestamps.offer(currentTime);
            return true;
        }
        
        return false;
    }
}
```

## 9. Design a Circular Queue
**Problem**: Design your implementation of the circular queue.

**Solution**:
```java
public class CircularQueue {
    private int[] queue;
    private int front;
    private int rear;
    private int size;
    private int capacity;
    
    public CircularQueue(int k) {
        capacity = k;
        queue = new int[k];
        front = 0;
        rear = -1;
        size = 0;
    }
    
    public boolean enQueue(int value) {
        if (isFull()) return false;
        
        rear = (rear + 1) % capacity;
        queue[rear] = value;
        size++;
        return true;
    }
    
    public boolean deQueue() {
        if (isEmpty()) return false;
        
        front = (front + 1) % capacity;
        size--;
        return true;
    }
    
    public int Front() {
        return isEmpty() ? -1 : queue[front];
    }
    
    public int Rear() {
        return isEmpty() ? -1 : queue[rear];
    }
    
    public boolean isEmpty() {
        return size == 0;
    }
    
    public boolean isFull() {
        return size == capacity;
    }
}
```

## 10. Implement a Trie (Prefix Tree)
**Problem**: Implement a Trie data structure with insert, search, and startsWith operations.

**Solution**:
```java
public class Trie {
    private class TrieNode {
        private TrieNode[] children;
        private boolean isEndOfWord;
        
        public TrieNode() {
            children = new TrieNode[26];
            isEndOfWord = false;
        }
    }
    
    private TrieNode root;
    
    public Trie() {
        root = new TrieNode();
    }
    
    public void insert(String word) {
        TrieNode current = root;
        for (char c : word.toCharArray()) {
            int index = c - 'a';
            if (current.children[index] == null) {
                current.children[index] = new TrieNode();
            }
            current = current.children[index];
        }
        current.isEndOfWord = true;
    }
    
    public boolean search(String word) {
        TrieNode node = searchNode(word);
        return node != null && node.isEndOfWord;
    }
    
    public boolean startsWith(String prefix) {
        return searchNode(prefix) != null;
    }
    
    private TrieNode searchNode(String word) {
        TrieNode current = root;
        for (char c : word.toCharArray()) {
            int index = c - 'a';
            if (current.children[index] == null) {
                return null;
            }
            current = current.children[index];
        }
        return current;
    }
}
```

These questions cover various important data structures and algorithms concepts that are commonly asked in interviews. Each solution includes:
1. Problem statement
2. Complete solution with implementation
3. Time and space complexity considerations
4. Edge cases handling

The questions range from basic to advanced level and cover topics like:
- Arrays and Strings
- Linked Lists
- Trees and Graphs
- Dynamic Programming
- Design Patterns
- Concurrency
- Data Structure Implementation

Remember to:
1. Understand the problem thoroughly before coding
2. Consider edge cases
3. Optimize for time and space complexity
4. Write clean and maintainable code
5. Be ready to explain your solution and discuss alternatives 