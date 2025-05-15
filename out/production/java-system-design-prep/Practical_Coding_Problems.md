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
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }
        
        int index = getIndex(key);
        Entry<K, V> entry = buckets[index];
        
        while (entry != null) {
            if (key.equals(entry.key)) {
                entry.value = value;
                return;
            }
            entry = entry.next;
        }
        
        Entry<K, V> newEntry = new Entry<>(key, value);
        newEntry.next = buckets[index];
        buckets[index] = newEntry;
        size++;
        
        if (size > buckets.length * LOAD_FACTOR) {
            resize();
        }
    }
    
    public V get(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }
        
        int index = getIndex(key);
        Entry<K, V> entry = buckets[index];
        
        while (entry != null) {
            if (key.equals(entry.key)) {
                return entry.value;
            }
            entry = entry.next;
        }
        
        return null;
    }
    
    public V remove(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }
        
        int index = getIndex(key);
        Entry<K, V> entry = buckets[index];
        Entry<K, V> prev = null;
        
        while (entry != null) {
            if (key.equals(entry.key)) {
                if (prev == null) {
                    buckets[index] = entry.next;
                } else {
                    prev.next = entry.next;
                }
                size--;
                return entry.value;
            }
            prev = entry;
            entry = entry.next;
        }
        
        return null;
    }
    
    private int getIndex(K key) {
        return Math.abs(key.hashCode() % buckets.length);
    }
    
    @SuppressWarnings("unchecked")
    private void resize() {
        Entry<K, V>[] oldBuckets = buckets;
        buckets = new Entry[oldBuckets.length * 2];
        size = 0;
        
        for (Entry<K, V> entry : oldBuckets) {
            while (entry != null) {
                put(entry.key, entry.value);
                entry = entry.next;
            }
        }
    }
}
```

## 2. Implement a Thread-Safe Blocking Queue
**Problem**: Implement a thread-safe blocking queue with put and take operations.

**Solution**:
```java
public class BlockingQueue<T> {
    private final Queue<T> queue;
    private final int capacity;
    private final Object lock;
    
    public BlockingQueue(int capacity) {
        this.queue = new LinkedList<>();
        this.capacity = capacity;
        this.lock = new Object();
    }
    
    public void put(T element) throws InterruptedException {
        synchronized (lock) {
            while (queue.size() == capacity) {
                lock.wait();
            }
            queue.offer(element);
            lock.notifyAll();
        }
    }
    
    public T take() throws InterruptedException {
        synchronized (lock) {
            while (queue.isEmpty()) {
                lock.wait();
            }
            T element = queue.poll();
            lock.notifyAll();
            return element;
        }
    }
    
    public int size() {
        synchronized (lock) {
            return queue.size();
        }
    }
}
```

## 3. Implement a Custom Thread Pool
**Problem**: Implement a thread pool that can execute tasks concurrently.

**Solution**:
```java
public class CustomThreadPool {
    private final BlockingQueue<Runnable> taskQueue;
    private final List<WorkerThread> threads;
    private volatile boolean isShutdown;
    
    public CustomThreadPool(int numThreads) {
        this.taskQueue = new LinkedBlockingQueue<>();
        this.threads = new ArrayList<>();
        this.isShutdown = false;
        
        for (int i = 0; i < numThreads; i++) {
            WorkerThread thread = new WorkerThread();
            thread.start();
            threads.add(thread);
        }
    }
    
    public void execute(Runnable task) {
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
            while (!isShutdown || !taskQueue.isEmpty()) {
                try {
                    Runnable task = taskQueue.poll(1, TimeUnit.SECONDS);
                    if (task != null) {
                        task.run();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }
}
```

## 4. Implement a Custom Read-Write Lock
**Problem**: Implement a read-write lock that allows multiple readers but only one writer.

**Solution**:
```java
public class CustomReadWriteLock {
    private int readers;
    private int writers;
    private int writeRequests;
    private final Object lock;
    
    public CustomReadWriteLock() {
        this.readers = 0;
        this.writers = 0;
        this.writeRequests = 0;
        this.lock = new Object();
    }
    
    public void readLock() throws InterruptedException {
        synchronized (lock) {
            while (writers > 0 || writeRequests > 0) {
                lock.wait();
            }
            readers++;
        }
    }
    
    public void readUnlock() {
        synchronized (lock) {
            readers--;
            if (readers == 0) {
                lock.notifyAll();
            }
        }
    }
    
    public void writeLock() throws InterruptedException {
        synchronized (lock) {
            writeRequests++;
            while (readers > 0 || writers > 0) {
                lock.wait();
            }
            writeRequests--;
            writers++;
        }
    }
    
    public void writeUnlock() {
        synchronized (lock) {
            writers--;
            lock.notifyAll();
        }
    }
}
```

## 5. Implement a Custom Circular Buffer
**Problem**: Implement a circular buffer with fixed capacity.

**Solution**:
```java
public class CircularBuffer<T> {
    private final T[] buffer;
    private int head;
    private int tail;
    private int size;
    private final int capacity;
    
    @SuppressWarnings("unchecked")
    public CircularBuffer(int capacity) {
        this.capacity = capacity;
        this.buffer = (T[]) new Object[capacity];
        this.head = 0;
        this.tail = 0;
        this.size = 0;
    }
    
    public synchronized void put(T element) throws InterruptedException {
        while (size == capacity) {
            wait();
        }
        
        buffer[tail] = element;
        tail = (tail + 1) % capacity;
        size++;
        notifyAll();
    }
    
    public synchronized T take() throws InterruptedException {
        while (size == 0) {
            wait();
        }
        
        T element = buffer[head];
        head = (head + 1) % capacity;
        size--;
        notifyAll();
        return element;
    }
    
    public synchronized int size() {
        return size;
    }
    
    public synchronized boolean isEmpty() {
        return size == 0;
    }
    
    public synchronized boolean isFull() {
        return size == capacity;
    }
}
```

These practical coding problems cover important concepts like:
1. Data Structure Implementation
2. Concurrency
3. Thread Safety
4. Resource Management
5. Synchronization

Each solution includes:
- Complete implementation
- Thread safety considerations
- Error handling
- Performance optimizations

Key points to remember:
1. Always consider thread safety in concurrent implementations
2. Handle edge cases and error conditions
3. Optimize for performance where possible
4. Write clean and maintainable code
5. Document your implementation decisions 