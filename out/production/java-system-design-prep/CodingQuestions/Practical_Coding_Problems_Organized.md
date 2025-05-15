# Practical Coding Problems and Solutions

## 1. Implement a Custom HashMap
**Problem**: Implement a HashMap from scratch with put, get, and remove operations.

**Solution**:
```java
public class CustomHashMap<K, V> {
    private static class Entry<K, V> {
        K key;
        V value;
        Entry<K, V> next;
        
        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
    
    private Entry<K, V>[] buckets;
    private int size;
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    
    @SuppressWarnings("unchecked")
    public CustomHashMap() {
        buckets = new Entry[DEFAULT_CAPACITY];
        size = 0;
    }
    
    public void put(K key, V value) {
        // Implementation details
    }
    
    public V get(K key) {
        // Implementation details
        return null;
    }
    
    public V remove(K key) {
        // Implementation details
        return null;
    }
    
    private void resize() {
        // Implementation details
    }
}
```

## 2. Implement a ThreadSafe Singleton
**Problem**: Implement a thread-safe singleton pattern in Java.

**Solution**:
```java
// Double-checked locking with volatile keyword
public class Singleton {
    private static volatile Singleton instance;
    
    private Singleton() {
        // Private constructor
    }
    
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

// Using enum (preferred way)
public enum SingletonEnum {
    INSTANCE;
    
    // Add methods and fields
    public void doSomething() {
        // Implementation
    }
}
```

## 3. Implement a Custom ThreadPool
**Problem**: Create a custom thread pool that manages a fixed number of threads to execute tasks.

**Solution**:
```java
public class CustomThreadPool {
    private final BlockingQueue<Runnable> taskQueue;
    private final List<WorkerThread> threads;
    private volatile boolean isShutdown;
    
    public CustomThreadPool(int numThreads) {
        taskQueue = new LinkedBlockingQueue<>();
        threads = new ArrayList<>(numThreads);
        isShutdown = false;
        
        for (int i = 0; i < numThreads; i++) {
            WorkerThread thread = new WorkerThread();
            thread.start();
            threads.add(thread);
        }
    }
    
    public void submit(Runnable task) {
        if (isShutdown) {
            throw new IllegalStateException("ThreadPool is shutdown");
        }
        taskQueue.offer(task);
    }
    
    public void shutdown() {
        isShutdown = true;
        for (WorkerThread thread : threads) {
            thread.interrupt();
        }
    }
    
    private class WorkerThread extends Thread {
        @Override
        public void run() {
            while (!isShutdown) {
                try {
                    Runnable task = taskQueue.take();
                    task.run();
                } catch (InterruptedException e) {
                    // Thread was interrupted
                    break;
                }
            }
        }
    }
}
```

## 4. LRU Cache Implementation
**Problem**: Implement an LRU (Least Recently Used) Cache with get and put operations in O(1) time.

**Solution**:
```java
public class LRUCache<K, V> {
    private class Node {
        K key;
        V value;
        Node prev;
        Node next;
        
        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
    
    private final int capacity;
    private final Map<K, Node> cache;
    private Node head; // Most recently used
    private Node tail; // Least recently used
    
    public LRUCache(int capacity) {
        this.capacity = capacity;
        cache = new HashMap<>();
        head = new Node(null, null);
        tail = new Node(null, null);
        head.next = tail;
        tail.prev = head;
    }
    
    public V get(K key) {
        Node node = cache.get(key);
        if (node == null) {
            return null;
        }
        
        // Move to front (MRU position)
        removeNode(node);
        addToFront(node);
        
        return node.value;
    }
    
    public void put(K key, V value) {
        Node node = cache.get(key);
        
        if (node != null) {
            // Update existing value
            node.value = value;
            removeNode(node);
            addToFront(node);
        } else {
            // Add new node
            Node newNode = new Node(key, value);
            
            if (cache.size() >= capacity) {
                // Remove LRU item
                Node lru = tail.prev;
                removeNode(lru);
                cache.remove(lru.key);
            }
            
            cache.put(key, newNode);
            addToFront(newNode);
        }
    }
    
    private void addToFront(Node node) {
        node.next = head.next;
        node.prev = head;
        head.next.prev = node;
        head.next = node;
    }
    
    private void removeNode(Node node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }
}
```

## 5. Rate Limiter Implementation
**Problem**: Implement a rate limiter that allows only a certain number of requests within a time window.

**Solution**:
```java
public class TokenBucketRateLimiter {
    private final long maxBucketSize;
    private final long refillRate;
    private long currentBucketSize;
    private long lastRefillTimestamp;
    
    /**
     * @param maxBucketSize maximum number of tokens
     * @param refillRate tokens per second
     */
    public TokenBucketRateLimiter(long maxBucketSize, long refillRate) {
        this.maxBucketSize = maxBucketSize;
        this.refillRate = refillRate;
        this.currentBucketSize = maxBucketSize;
        this.lastRefillTimestamp = System.nanoTime();
    }
    
    public synchronized boolean allowRequest() {
        refill();
        
        if (currentBucketSize > 0) {
            currentBucketSize--;
            return true;
        }
        
        return false;
    }
    
    private void refill() {
        long now = System.nanoTime();
        long durationSinceLastRefill = now - lastRefillTimestamp;
        
        // Convert nanoseconds to seconds
        double secondsSinceLastRefill = durationSinceLastRefill / 1_000_000_000.0;
        
        // Calculate tokens to add
        long tokensToAdd = (long) (secondsSinceLastRefill * refillRate);
        
        if (tokensToAdd > 0) {
            currentBucketSize = Math.min(currentBucketSize + tokensToAdd, maxBucketSize);
            lastRefillTimestamp = now;
        }
    }
}
```
