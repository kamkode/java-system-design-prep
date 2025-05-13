<div align="center">
  <h1>🏗️ System Design Interview Questions</h1>
  <p>Comprehensive guide to system design interview questions with solutions</p>
  
  [![System Design](https://img.shields.io/badge/System%20Design-Intermediate-9cf?style=flat-square&logo=diagram3&logoColor=white)](https://github.com/donnemartin/system-design-primer)
  [![License](https://img.shields.io/badge/License-MIT-blue.svg?style=flat-square)](https://opensource.org/licenses/MIT)
  [![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg?style=flat-square)](https://github.com/yourusername/system-design/pulls)
</div>

## 📋 Table of Contents

- [1. URL Shortener](#1-design-a-url-shortening-service-tinyurl)
- [2. Distributed Cache](#2-design-a-distributed-cache)
- [3. Rate Limiter](#3-design-a-rate-limiter)
- [4. Distributed Lock](#4-design-a-distributed-lock)
- [5. Chat Application](#5-design-a-chat-application)
- [6. Payment System](#6-design-a-payment-system)
- [7. Video Streaming](#7-design-a-video-streaming-service)
- [8. Distributed Logging](#8-design-a-distributed-logging-system)

---

## 🔗 1. Design a URL Shortening Service (TinyURL)

### 📌 Problem Statement
Design a service that takes long URLs and converts them into shorter, more manageable URLs.

### 🎯 Requirements
- **Functional**
  - Shorten long URLs
  - Redirect short URLs to original URLs
  - Custom short URLs (optional)
  - URL expiration (optional)
- **Non-Functional**
  - High availability
  - Low latency
  - Scalability
  - Analytics (clicks, referrers, etc.)

### 🏗️ High-Level Design
```
Client → Load Balancer → Web Servers → Application Layer → Cache → Database
                              ↑
                              ↓
                        Analytics Service
```

### 🔑 Key Components

#### 1. URL Shortening Algorithm
```java
public class URLShortener {
    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int BASE = ALPHABET.length();
    private static final String DOMAIN = "https://short.ly/";
    
    // In-memory storage (replace with distributed cache in production)
    private Map<String, String> urlMap = new ConcurrentHashMap<>();
    private Map<String, String> reverseUrlMap = new ConcurrentHashMap<>();
    
    /**
     * Shortens a long URL
     * @param longUrl The original URL to be shortened
     * @return Shortened URL
     */
    public String shorten(String longUrl) {
        // Check if URL already exists
        if (reverseUrlMap.containsKey(longUrl)) {
            return DOMAIN + reverseUrlMap.get(longUrl);
        }
        
        // Generate short URL
        String shortCode = generateShortCode();
        
        // Store mapping
        urlMap.put(shortCode, longUrl);
        reverseUrlMap.put(longUrl, shortCode);
        
        return DOMAIN + shortCode;
    }
    
    /**
     * Expands a short URL to the original URL
     * @param shortUrl The short URL to expand
     * @return Original long URL or null if not found
     */
    public String expand(String shortUrl) {
        // Extract short code from URL
        String shortCode = shortUrl.replace(DOMAIN, "");
        return urlMap.get(shortCode);
    }
    
    /**
     * Generates a random short code
     */
    private String generateShortCode() {
        StringBuilder sb = new StringBuilder();
        SecureRandom random = new SecureRandom();
        
        for (int i = 0; i < 6; i++) {
            int index = random.nextInt(BASE);
            sb.append(ALPHABET.charAt(index));
        }
        
        // Handle collisions (in production, use distributed ID generation)
        while (urlMap.containsKey(sb.toString())) {
            return generateShortCode();
        }
        
        return sb.toString();
    }
}
```

#### 2. Database Schema
```sql
CREATE TABLE urls (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    short_code VARCHAR(10) UNIQUE NOT NULL,
    long_url TEXT NOT NULL,
    user_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP NULL,
    is_custom BOOLEAN DEFAULT FALSE,
    click_count BIGINT DEFAULT 0,
    INDEX idx_short_code (short_code)
);

CREATE TABLE analytics (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    short_code VARCHAR(10) NOT NULL,
    referrer TEXT,
    user_agent TEXT,
    ip_address VARCHAR(45),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (short_code) REFERENCES urls(short_code)
);
```

### ⚡ Performance Optimizations

1. **Caching**
   - Cache popular URLs in Redis/Memcached
   - Use LRU (Least Recently Used) eviction policy
   
2. **Load Balancing**
   - Distribute traffic across multiple servers
   - Use consistent hashing for cache sharding
   
3. **Database**
   - Use read replicas for scaling reads
   - Shard by URL hash or user ID
   
4. **CDN**
   - Cache static assets and frequently accessed resources
   - Distribute content globally for lower latency

### 📈 Scaling Considerations

1. **Read-heavy Workload**
   - Implement multi-level caching (in-memory, distributed)
   - Use CDN for static assets
   
2. **Write-heavy Workload**
   - Use queue-based architecture for writes
   - Implement asynchronous processing
   
3. **Data Storage**
   - Consider NoSQL for horizontal scaling
   - Implement TTL for expired URLs

### 🔒 Security Considerations

1. **Prevent Abuse**
   - Rate limiting per IP/user
   - URL validation
   - Malware scanning
   
2. **Privacy**
   - Anonymize IP addresses in analytics
   - GDPR compliance
   
3. **Authentication**
   - OAuth for third-party access
   - API keys for programmatic access

### 📝 Example API Endpoints

```http
POST /api/shorten
Content-Type: application/json

{
  "long_url": "https://example.com/very/long/url/...",
  "custom_code": "mylink",  // optional
  "expires_in": 86400  // optional, in seconds
}

Response:
{
  "short_url": "https://short.ly/abc123",
  "long_url": "https://example.com/very/long/url/...",
  "expires_at": "2024-06-10T12:00:00Z"
}

GET /{shortCode}

Response: 301 Moved Permanently
Location: https://example.com/original/long/url
```

### 🔍 Real-world Examples
- Bit.ly
- TinyURL
- Google URL Shortener
- Rebrandly

---

## 🗃️ 2. Design a Distributed Cache

### 📌 Problem Statement
Design a distributed caching system that provides high-performance, low-latency access to frequently used data while maintaining consistency and fault tolerance across multiple cache nodes.

### 🎯 Requirements
- **Functional**
  - Get/Set/Delete operations
  - TTL (Time To Live) support
  - Cache invalidation
  - High availability
- **Non-Functional**
  - Sub-millisecond read latency
  - High throughput (100K+ QPS per node)
  - 99.99% availability
  - Linear scalability

### 🏗️ High-Level Design
```
Clients → Load Balancer → Cache Nodes (Sharded) → Persistent Storage (Optional)
      ↑                    ↑          ↑
      └─── Monitoring ←───┘          └─── Replication
```

### 🔑 Key Components

#### 1. Consistent Hashing Ring
```java
public class ConsistentHasher {
    private final SortedMap<Long, String> ring = new TreeMap<>();
    private final int numberOfReplicas;
    private final HashFunction hashFunction;

    public ConsistentHasher(int numberOfReplicas) {
        this(numberOfReplicas, new MD5Hash());
    }

    public ConsistentHasher(int numberOfReplicas, HashFunction hashFunction) {
        this.numberOfReplicas = numberOfReplicas;
        this.hashFunction = hashFunction;
    }

    public void addNode(String node) {
        for (int i = 0; i < numberOfReplicas; i++) {
            long hash = hashFunction.hash(node + "#" + i);
            ring.put(hash, node);
        }
    }

    public void removeNode(String node) {
        for (int i = 0; i < numberOfReplicas; i++) {
            long hash = hashFunction.hash(node + "#" + i);
            ring.remove(hash);
        }
    }

    public String getNode(String key) {
        if (ring.isEmpty()) {
            return null;
        }
        long hash = hashFunction.hash(key);
        if (!ring.containsKey(hash)) {
            SortedMap<Long, String> tailMap = ring.tailMap(hash);
            hash = tailMap.isEmpty() ? ring.firstKey() : tailMap.firstKey();
        }
        return ring.get(hash);
    }
}
```

#### 2. Cache Node Implementation
```java
public class DistributedCache {
    private final List<String> nodes;
    private final ConsistentHasher hasher;
    private final Map<String, CacheNode> nodeMap = new ConcurrentHashMap<>();
    private final int replicas;
    private final ExecutorService backgroundExecutor;

    public DistributedCache(List<String> nodes, int replicas) {
        this.nodes = new ArrayList<>(nodes);
        this.replicas = replicas;
        this.hasher = new ConsistentHasher(replicas);
        this.backgroundExecutor = Executors.newCachedThreadPool();
        
        // Initialize all nodes
        for (String node : nodes) {
            addNode(node);
        }
    }

    public void addNode(String node) {
        hasher.addNode(node);
        nodeMap.put(node, new CacheNode(10000)); // 10K entries per node
        
        // Trigger rebalancing in background
        backgroundExecutor.submit(this::rebalanceData);
    }

    public void removeNode(String node) {
        hasher.removeNode(node);
        CacheNode removedNode = nodeMap.remove(node);
        
        // Rebalance data in background
        if (removedNode != null) {
            backgroundExecutor.submit(this::rebalanceData);
        }
    }

    public void put(String key, Object value) {
        put(key, value, -1); // Default: no expiration
    }

    public void put(String key, Object value, long ttlMs) {
        String node = hasher.getNode(key);
        CacheNode cacheNode = nodeMap.get(node);
        if (cacheNode != null) {
            cacheNode.put(key, value, ttlMs);
        }
    }

    public Object get(String key) {
        String node = hasher.getNode(key);
        CacheNode cacheNode = nodeMap.get(node);
        return cacheNode != null ? cacheNode.get(key) : null;
    }

    private void rebalanceData() {
        // Implementation of data rebalancing when nodes are added/removed
        // This would involve moving data between nodes based on the new hash ring
    }

    private static class CacheNode {
        private final Map<String, CacheEntry> cache;
        private final int capacity;
        private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
        
        public CacheNode(int capacity) {
            this.capacity = capacity;
            // Using LinkedHashMap with access order for LRU eviction
            this.cache = new LinkedHashMap<String, CacheEntry>(16, 0.75f, true) {
                @Override
                protected boolean removeEldestEntry(Map.Entry<String, CacheEntry> eldest) {
                    return size() > capacity || (eldest.getValue().isExpired() && !cache.isEmpty());
                }
            };
        }
        
        public void put(String key, Object value, long ttlMs) {
            lock.writeLock().lock();
            try {
                // Check if we need to evict entries
                if (cache.size() >= capacity) {
                    // Trigger eviction by accessing the map
                    cache.entrySet().iterator().next();
                }
                cache.put(key, new CacheEntry(value, ttlMs));
            } finally {
                lock.writeLock().unlock();
            }
        }
        
        public Object get(String key) {
            lock.readLock().lock();
            try {
                CacheEntry entry = cache.get(key);
                if (entry == null) {
                    return null;
                }
                if (entry.isExpired()) {
                    // Remove expired entry asynchronously
                    removeAsync(key);
                    return null;
                }
                return entry.getValue();
            } finally {
                lock.readLock().unlock();
            }
        }
        
        private void removeAsync(String key) {
            CompletableFuture.runAsync(() -> {
                lock.writeLock().lock();
                try {
                    cache.remove(key);
                } finally {
                    lock.writeLock().unlock();
                }
            });
        }
    
    private static class CacheEntry {
        private final Object value;
        private final long expiryTime;
        private final long createdAt;
        
        public CacheEntry(Object value, long ttlMs) {
            this.value = value;
            this.createdAt = System.currentTimeMillis();
            this.expiryTime = ttlMs > 0 ? (createdAt + ttlMs) : Long.MAX_VALUE;
        }
        
        public boolean isExpired() {
            return System.currentTimeMillis() > expiryTime;
        }
        
        public Object getValue() {
            return value;
        }
        
        public long getTTL() {
            return expiryTime == Long.MAX_VALUE ? -1 : (expiryTime - System.currentTimeMillis());
        }
    }
}
```

### ⚡ Performance Optimizations

1. **Consistent Hashing**
   - Minimizes key redistribution when nodes are added/removed
   - Provides even distribution of keys across nodes
   - Supports virtual nodes for better load balancing

2. **Concurrency**
   - Uses fine-grained locking with `ReentrantReadWriteLock`
   - Read operations don't block other reads
   - Write operations are serialized for thread safety

3. **Memory Management**
   - LRU (Least Recently Used) eviction policy
   - Automatic cleanup of expired entries
   - Asynchronous removal of expired entries

4. **Scalability**
   - Horizontal scaling by adding more nodes
   - Background rebalancing of data
   - Configurable number of replicas for high availability

### 📈 Scaling Considerations

1. **Read-heavy Workload**
   - Implement multi-level caching (L1/L2)
   - Use read replicas for frequently accessed data
   - Cache popular items in client-side caches

2. **Write-heavy Workload**
   - Implement write-through/write-behind caching
   - Batch updates to reduce I/O operations
   - Use asynchronous replication for better write performance

3. **Data Consistency**
   - Choose appropriate consistency model (strong/eventual)
   - Implement versioning for conflict resolution
   - Use vector clocks for causal ordering

### 🔒 Security Considerations

1. **Data Protection**
   - Encrypt sensitive data at rest
   - Use TLS for data in transit
   - Implement proper authentication and authorization

2. **Prevent Abuse**
   - Rate limiting per client
   - Request validation
   - Cache size limits

3. **Monitoring**
   - Track cache hit/miss ratios
   - Monitor memory usage and eviction rates
   - Set up alerts for abnormal patterns

### 📝 Example Usage

```java
// Initialize cache with 3 nodes and 2 replicas per node
List<String> nodes = Arrays.asList("cache1", "cache2", "cache3");
DistributedCache cache = new DistributedCache(nodes, 2);

// Store data with TTL (5 minutes)
cache.put("user:123", userData, 300_000);

// Retrieve data
UserData data = (UserData) cache.get("user:123");

// Add a new node (rebalancing happens in background)
cache.addNode("cache4");

// Remove a node (rebalancing happens in background)
cache.removeNode("cache1");
```

### 🔍 Real-world Examples
- Redis Cluster
- Memcached
- Hazelcast
- Apache Ignite
    
    private List<CacheNode> nodes;
    private int nodeCount;
    
    public DistributedCache(int nodeCount, int capacityPerNode) {
        this.nodeCount = nodeCount;
        this.nodes = new ArrayList<>();
        for (int i = 0; i < nodeCount; i++) {
            nodes.add(new CacheNode(capacityPerNode));
        }
    }
    
    public void put(String key, Object value) {
        int nodeIndex = getNodeIndex(key);
        nodes.get(nodeIndex).put(key, value);
    }
    
    public Object get(String key) {
        int nodeIndex = getNodeIndex(key);
        return nodes.get(nodeIndex).get(key);
    }
    
    private int getNodeIndex(String key) {
        return Math.abs(key.hashCode() % nodeCount);
    }
}
```

## 3. Design a Rate Limiter
**Problem**: Design a rate limiter that can handle multiple clients and different rate limits.

**Key Components**:
1. Rate Limiting Algorithm
2. Storage for Rate Limits
3. Client Identification
4. Distributed Rate Limiting

**Solution**:
```java
public class RateLimiter {
    private static class TokenBucket {
        private final int capacity;
        private final double refillRate;
        private double tokens;
        private long lastRefillTime;
        
        public TokenBucket(int capacity, double refillRate) {
            this.capacity = capacity;
            this.refillRate = refillRate;
            this.tokens = capacity;
            this.lastRefillTime = System.currentTimeMillis();
        }
        
        public synchronized boolean tryConsume() {
            refill();
            if (tokens >= 1) {
                tokens -= 1;
                return true;
            }
            return false;
        }
        
        private void refill() {
            long now = System.currentTimeMillis();
            double tokensToAdd = (now - lastRefillTime) * refillRate / 1000.0;
            tokens = Math.min(capacity, tokens + tokensToAdd);
            lastRefillTime = now;
        }
    }
    
    private Map<String, TokenBucket> clientBuckets;
    
    public RateLimiter() {
        this.clientBuckets = new ConcurrentHashMap<>();
    }
    
    public boolean allowRequest(String clientId, int requestsPerSecond) {
        TokenBucket bucket = clientBuckets.computeIfAbsent(clientId,
            k -> new TokenBucket(requestsPerSecond, requestsPerSecond));
        return bucket.tryConsume();
    }
}
```

## 4. Design a Task Scheduler
**Problem**: Design a task scheduler that can handle periodic tasks and one-time tasks.

**Key Components**:
1. Task Queue
2. Worker Threads
3. Task Priority
4. Error Handling

**Solution**:
```java
public class TaskScheduler {
    private static class Task implements Comparable<Task> {
        private final Runnable task;
        private final long executionTime;
        private final int priority;
        
        public Task(Runnable task, long executionTime, int priority) {
            this.task = task;
            this.executionTime = executionTime;
            this.priority = priority;
        }
        
        @Override
        public int compareTo(Task other) {
            int timeCompare = Long.compare(this.executionTime, other.executionTime);
            if (timeCompare != 0) return timeCompare;
            return Integer.compare(other.priority, this.priority);
        }
    }
    
    private final PriorityBlockingQueue<Task> taskQueue;
    private final ExecutorService executor;
    private volatile boolean running;
    
    public TaskScheduler(int threadPoolSize) {
        this.taskQueue = new PriorityBlockingQueue<>();
        this.executor = Executors.newFixedThreadPool(threadPoolSize);
        this.running = true;
        startScheduler();
    }
    
    public void schedule(Runnable task, long delayMillis, int priority) {
        long executionTime = System.currentTimeMillis() + delayMillis;
        taskQueue.offer(new Task(task, executionTime, priority));
    }
    
    public void scheduleAtFixedRate(Runnable task, long initialDelay, long period, int priority) {
        schedule(task, initialDelay, priority);
        schedule(new Runnable() {
            @Override
            public void run() {
                task.run();
                schedule(this, period, priority);
            }
        }, initialDelay + period, priority);
    }
    
    private void startScheduler() {
        Thread schedulerThread = new Thread(() -> {
            while (running) {
                try {
                    Task task = taskQueue.peek();
                    if (task != null) {
                        long currentTime = System.currentTimeMillis();
                        if (currentTime >= task.executionTime) {
                            task = taskQueue.poll();
                            executor.execute(task.task);
                        } else {
                            Thread.sleep(task.executionTime - currentTime);
                        }
                    } else {
                        Thread.sleep(100);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        schedulerThread.start();
    }
    
    public void shutdown() {
        running = false;
        executor.shutdown();
    }
}
```

## 5. Design a Distributed Lock
**Problem**: Design a distributed locking mechanism that can be used across multiple servers.

**Key Components**:
1. Lock Acquisition
2. Lock Release
3. Deadlock Prevention
4. Fault Tolerance

**Solution**:
```java
public class DistributedLock {
    private static class LockInfo {
        private final String lockId;
        private final long expiryTime;
        
        public LockInfo(String lockId, long expiryTime) {
            this.lockId = lockId;
            this.expiryTime = expiryTime;
        }
    }
    
    private final Map<String, LockInfo> locks;
    private final long lockTimeout;
    
    public DistributedLock(long lockTimeoutMillis) {
        this.locks = new ConcurrentHashMap<>();
        this.lockTimeout = lockTimeoutMillis;
    }
    
    public boolean acquireLock(String resource, String lockId) {
        long currentTime = System.currentTimeMillis();
        LockInfo existingLock = locks.get(resource);
        
        if (existingLock != null) {
            if (currentTime < existingLock.expiryTime) {
                return false;
            }
            // Lock has expired, we can take it
        }
        
        LockInfo newLock = new LockInfo(lockId, currentTime + lockTimeout);
        return locks.putIfAbsent(resource, newLock) == null;
    }
    
    public boolean releaseLock(String resource, String lockId) {
        LockInfo existingLock = locks.get(resource);
        if (existingLock != null && existingLock.lockId.equals(lockId)) {
            return locks.remove(resource, existingLock);
        }
        return false;
    }
    
    public boolean refreshLock(String resource, String lockId) {
        LockInfo existingLock = locks.get(resource);
        if (existingLock != null && existingLock.lockId.equals(lockId)) {
            LockInfo newLock = new LockInfo(lockId, System.currentTimeMillis() + lockTimeout);
            return locks.replace(resource, existingLock, newLock);
        }
        return false;
    }
}
```

These system design questions cover important concepts like:
1. Scalability
2. Distributed Systems
3. Caching
4. Rate Limiting
5. Task Scheduling
6. Distributed Locking

Each solution includes:
- Complete implementation
- Key components explanation
- Design considerations
- Best practices

Remember to consider:
1. Scalability requirements
2. Fault tolerance
3. Consistency requirements
4. Performance optimization
5. Security considerations 