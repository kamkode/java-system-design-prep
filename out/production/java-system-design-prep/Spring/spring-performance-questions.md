# Spring Framework and Spring Boot Performance and Optimization Questions

This document contains interview questions related to performance and optimization in Spring Framework and Spring Boot, suitable for candidates with 3-5 years of experience. These questions focus on memory management, lazy loading, query optimization, caching, and other performance considerations.

## Table of Contents
1. [Memory Management](#memory-management)
2. [Lazy Loading and Initialization](#lazy-loading-and-initialization)
3. [Database and Query Optimization](#database-and-query-optimization)
4. [Caching Strategies](#caching-strategies)
5. [Application Startup Optimization](#application-startup-optimization)
6. [Web Application Performance](#web-application-performance)
7. [Reactive Programming](#reactive-programming)
8. [Monitoring and Profiling](#monitoring-and-profiling)

## Memory Management

### Q1: What are common causes of memory leaks in Spring applications and how can they be prevented?

**Answer:** Memory leaks in Spring applications can occur for several reasons:

1. **Static References to Spring Beans**:
   - **Problem**: Storing Spring beans in static fields creates permanent references that prevent garbage collection.
   ```java
   // Problematic code
   public class StaticHolderService {
       private static UserService userService;
       
       @Autowired
       public void setUserService(UserService userService) {
           StaticHolderService.userService = userService; // Creates a static reference
       }
   }
   ```
   - **Solution**: Avoid static references to Spring beans. Use dependency injection properly.
   ```java
   // Better approach
   @Service
   public class HolderService {
       private final UserService userService;
       
       @Autowired
       public HolderService(UserService userService) {
           this.userService = userService;
       }
   }
   ```

2. **Unclosed Resources**:
   - **Problem**: Not closing JDBC connections, file streams, or other resources.
   ```java
   // Problematic code
   public void readData() throws IOException {
       InputStream is = new FileInputStream("data.txt");
       // Use the stream but never close it
   }
   ```
   - **Solution**: Use try-with-resources or ensure proper closing in finally blocks.
   ```java
   // Better approach
   public void readData() throws IOException {
       try (InputStream is = new FileInputStream("data.txt")) {
           // Use the stream - automatically closed
       }
   }
   ```

3. **Improper Cache Management**:
   - **Problem**: Unbounded caches that grow indefinitely.
   ```java
   // Problematic code
   @Service
   public class ProductService {
       private Map<Long, Product> productCache = new HashMap<>();
       
       public Product getProduct(Long id) {
           if (!productCache.containsKey(id)) {
               productCache.put(id, productRepository.findById(id).orElse(null));
           }
           return productCache.get(id);
       }
   }
   ```
   - **Solution**: Use bounded caches with eviction policies.
   ```java
   // Better approach
   @Service
   public class ProductService {
       private final LoadingCache<Long, Product> productCache;
       
       public ProductService(ProductRepository productRepository) {
           this.productCache = CacheBuilder.newBuilder()
               .maximumSize(1000)
               .expireAfterWrite(10, TimeUnit.MINUTES)
               .build(new CacheLoader<Long, Product>() {
                   @Override
                   public Product load(Long id) {
                       return productRepository.findById(id).orElse(null);
                   }
               });
       }
       
       public Product getProduct(Long id) {
           return productCache.getUnchecked(id);
       }
   }
   ```

4. **ThreadLocal Variables Not Cleaned**:
   - **Problem**: ThreadLocal variables not removed after use in application servers with thread pools.
   ```java
   // Problematic code
   public class UserContext {
       private static ThreadLocal<User> currentUser = new ThreadLocal<>();
       
       public static void setCurrentUser(User user) {
           currentUser.set(user);
       }
       
       public static User getCurrentUser() {
           return currentUser.get();
       }
       
       // Missing cleanup method
   }
   ```
   - **Solution**: Always clean ThreadLocal variables after use.
   ```java
   // Better approach
   public class UserContext {
       private static ThreadLocal<User> currentUser = new ThreadLocal<>();
       
       public static void setCurrentUser(User user) {
           currentUser.set(user);
       }
       
       public static User getCurrentUser() {
           return currentUser.get();
       }
       
       public static void clear() {
           currentUser.remove();
       }
   }
   
   @Component
   public class UserContextFilter implements Filter {
       @Override
       public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
               throws IOException, ServletException {
           try {
               chain.doFilter(request, response);
           } finally {
               UserContext.clear(); // Always clear ThreadLocal
           }
       }
   }
   ```

5. **Event Listeners Not Unregistered**:
   - **Problem**: Event listeners that are registered but never unregistered.
   ```java
   // Problematic code
   @Component
   public class EventListenerComponent {
       @Autowired
       public void registerListeners(EventPublisher publisher) {
           publisher.addListener(new DataChangeListener()); // Never removed
       }
   }
   ```
   - **Solution**: Implement proper lifecycle methods to unregister listeners.
   ```java
   // Better approach
   @Component
   public class EventListenerComponent implements DisposableBean {
       private final EventPublisher publisher;
       private final DataChangeListener listener = new DataChangeListener();
       
       @Autowired
       public EventListenerComponent(EventPublisher publisher) {
           this.publisher = publisher;
           this.publisher.addListener(listener);
       }
       
       @Override
       public void destroy() {
           publisher.removeListener(listener); // Clean up on bean destruction
       }
   }
   ```

6. **Circular References with Non-Proxy Objects**:
   - **Problem**: Objects referencing each other in a way that prevents garbage collection.
   - **Solution**: Redesign to avoid circular references or use weak references.

7. **Large HTTP Sessions**:
   - **Problem**: Storing too much data in HTTP sessions.
   ```java
   // Problematic code
   @Controller
   public class LargeDataController {
       @GetMapping("/data")
       public String handleData(HttpSession session, Model model) {
           // Store large objects in session
           session.setAttribute("largeData", fetchVeryLargeDataSet());
           return "dataView";
       }
   }
   ```
   - **Solution**: Minimize session data and set appropriate timeouts.
   ```java
   // Better approach
   @Controller
   public class OptimizedDataController {
       @GetMapping("/data")
       public String handleData(Model model) {
           // Store only necessary identifiers in session
           model.addAttribute("data", fetchVeryLargeDataSet());
           return "dataView";
       }
   }
   ```

8. **Hibernate/JPA Collection Caching**:
   - **Problem**: Caching large collections without proper boundaries.
   - **Solution**: Use pagination, lazy loading, and appropriate fetch strategies.

To detect and diagnose memory leaks:
1. Use memory profilers like VisualVM, JProfiler, or YourKit
2. Enable heap dumps on OutOfMemoryError
3. Analyze heap dumps with tools like Eclipse Memory Analyzer (MAT)
4. Use Spring Boot Actuator's metrics to monitor memory usage
5. Implement regular load testing with memory monitoring

### Q2: How can you optimize memory usage in a Spring Boot application with a large number of concurrent users?

**Answer:** Optimizing memory usage in a Spring Boot application with many concurrent users involves several strategies:

1. **Optimize Session Management**:
   - Use session timeouts to release resources for inactive users:
   ```properties
   # In application.properties
   server.servlet.session.timeout=30m
   ```
   - Consider using session replication or sticky sessions in clustered environments
   - Store minimal data in sessions:
   ```java
   // Instead of storing entire user profile
   session.setAttribute("userId", user.getId());
   
   // Then retrieve when needed
   Long userId = (Long) session.getAttribute("userId");
   User user = userService.findById(userId);
   ```
   - Use server-side session stores like Redis for better memory management:
   ```xml
   <dependency>
       <groupId>org.springframework.session</groupId>
       <artifactId>spring-session-data-redis</artifactId>
   </dependency>
   ```

2. **Configure Thread Pools Appropriately**:
   - Set appropriate thread pool sizes for server and application executors:
   ```properties
   # Tomcat thread pool configuration
   server.tomcat.threads.max=200
   server.tomcat.threads.min-spare=20
   
   # For async tasks
   spring.task.execution.pool.max-size=50
   spring.task.execution.pool.core-size=20
   spring.task.execution.pool.queue-capacity=100
   ```

3. **Optimize JVM Memory Settings**:
   - Configure heap size appropriately:
   ```
   -Xms2g -Xmx2g
   ```
   - Consider using G1GC for large heaps:
   ```
   -XX:+UseG1GC
   ```
   - Tune garbage collection parameters:
   ```
   -XX:MaxGCPauseMillis=200
   -XX:ParallelGCThreads=20
   -XX:ConcGCThreads=5
   ```
   - Monitor and adjust based on GC logs:
   ```
   -Xlog:gc*:file=gc.log:time,uptime:filecount=5,filesize=100m
   ```

4. **Use Pagination for Large Data Sets**:
   ```java
   @GetMapping("/users")
   public Page<UserDTO> getUsers(
           @RequestParam(defaultValue = "0") int page,
           @RequestParam(defaultValue = "20") int size) {
       
       Pageable pageable = PageRequest.of(page, size);
       return userRepository.findAll(pageable).map(userMapper::toDTO);
   }
   ```

5. **Implement Data Transfer Objects (DTOs)**:
   ```java
   // Instead of returning full entities
   @GetMapping("/users/{id}")
   public UserDTO getUser(@PathVariable Long id) {
       User user = userRepository.findById(id)
           .orElseThrow(() -> new ResourceNotFoundException("User not found"));
       return userMapper.toDTO(user); // Convert to lightweight DTO
   }
   ```

6. **Use Lazy Loading for JPA Relationships**:
   ```java
   @Entity
   public class User {
       @Id
       private Long id;
       
       // Other fields
       
       @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
       private List<Order> orders;
   }
   ```

7. **Implement Connection Pooling Correctly**:
   ```properties
   # HikariCP settings
   spring.datasource.hikari.maximum-pool-size=10
   spring.datasource.hikari.minimum-idle=5
   spring.datasource.hikari.idle-timeout=600000
   spring.datasource.hikari.connection-timeout=30000
   spring.datasource.hikari.max-lifetime=1800000
   ```

8. **Use Caching Strategically**:
   ```java
   @Service
   @CacheConfig(cacheNames = "users")
   public class UserService {
       @Cacheable(key = "#id")
       public User findById(Long id) {
           // Method implementation
       }
       
       @CacheEvict(key = "#user.id")
       public void updateUser(User user) {
           // Method implementation
       }
   }
   ```

9. **Consider Reactive Programming for I/O-bound Applications**:
   ```java
   @RestController
   public class ReactiveUserController {
       private final ReactiveUserRepository userRepository;
       
       @GetMapping("/users")
       public Flux<UserDTO> getAllUsers() {
           return userRepository.findAll()
               .map(userMapper::toDTO);
       }
   }
   ```

10. **Use Stateless Design Where Possible**:
    - Avoid storing state in beans with session or request scope
    - Use JWT or similar tokens instead of session-based authentication

11. **Implement Resource Pooling**:
    - Pool expensive resources like HTTP clients:
    ```java
    @Bean
    public RestTemplate restTemplate() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(3000);
        
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(100);
        cm.setDefaultMaxPerRoute(20);
        
        HttpClient httpClient = HttpClients.custom()
            .setConnectionManager(cm)
            .build();
        
        factory.setHttpClient(httpClient);
        
        return new RestTemplate(factory);
    }
    ```

12. **Use Compression for Responses**:
    ```properties
    server.compression.enabled=true
    server.compression.min-response-size=2048
    server.compression.mime-types=application/json,application/xml,text/html,text/plain
    ```

13. **Monitor and Profile Regularly**:
    - Use Spring Boot Actuator to expose memory metrics
    - Set up alerts for memory thresholds
    - Perform regular load testing to identify memory issues before production

By implementing these strategies, you can significantly reduce memory usage and improve the scalability of your Spring Boot application for large numbers of concurrent users.

### Q3: How do you identify and fix memory leaks related to Hibernate/JPA in a Spring Boot application?

**Answer:** Identifying and fixing memory leaks related to Hibernate/JPA in Spring Boot applications requires a systematic approach:

**Identification Techniques:**

1. **Monitor Memory Usage**:
   - Use Spring Boot Actuator to expose memory metrics:
   ```properties
   management.endpoints.web.exposure.include=health,info,metrics
   ```
   - Look for steadily increasing heap usage that doesn't return to baseline after GC

2. **Enable Detailed Hibernate Logging**:
   ```properties
   logging.level.org.hibernate.SQL=DEBUG
   logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
   logging.level.org.hibernate.stat=DEBUG
   
   # Enable statistics
   spring.jpa.properties.hibernate.generate_statistics=true
   ```

3. **Capture and Analyze Heap Dumps**:
   - Configure JVM to generate heap dumps on OOM:
   ```
   -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/path/to/dumps
   ```
   - Use tools like Eclipse Memory Analyzer (MAT) to analyze heap dumps
   - Look for large collections of entities or detached entities

4. **Use Profiling Tools**:
   - JVisualVM, YourKit, or JProfiler to identify memory growth
   - Focus on Hibernate session and entity collections

**Common JPA/Hibernate Memory Leak Causes and Solutions:**

1. **N+1 Query Problem**:
   - **Problem**: Fetching a collection one element at a time.
   ```java
   // This code will generate N+1 queries
   List<Department> departments = departmentRepository.findAll();
   for (Department dept : departments) {
       // This triggers a separate query for each department
       System.out.println("Employees in " + dept.getName() + ": " + dept.getEmployees().size());
   }
   ```
   
   - **Solution**: Use join fetches or entity graphs.
   ```java
   // Using JPQL join fetch
   @Query("SELECT d FROM Department d LEFT JOIN FETCH d.employees")
   List<Department> findAllWithEmployees();
   
   // Using entity graph
   @EntityGraph(attributePaths = {"employees"})
   List<Department> findAll();
   ```

2. **Large Result Sets**:
   - **Problem**: Loading too many entities into memory.
   ```java
   // Loading all records at once
   List<User> allUsers = userRepository.findAll(); // Dangerous with millions of users
   ```
   
   - **Solution**: Use pagination, streaming, or native queries with projections.
   ```java
   // Pagination
   Page<User> userPage = userRepository.findAll(PageRequest.of(0, 100));
   
   // Streaming (Spring Data JPA 2.x+)
   @Transactional(readOnly = true)
   public void processAllUsers() {
       try (Stream<User> userStream = userRepository.findAllAsStream()) {
           userStream.forEach(this::processUser);
       }
   }
   
   // Projection
   @Query("SELECT new com.example.dto.UserSummary(u.id, u.name) FROM User u")
   List<UserSummary> findAllUserSummaries();
   ```

3. **Session-Managed Collections**:
   - **Problem**: Large collections managed by Hibernate session.
   ```java
   @Entity
   public class Department {
       @OneToMany(mappedBy = "department", fetch = FetchType.EAGER)
       private Set<Employee> employees = new HashSet<>(); // EAGER loading all employees
   }
   ```
   
   - **Solution**: Use lazy loading and access collections only when needed.
   ```java
   @Entity
   public class Department {
       @OneToMany(mappedBy = "department", fetch = FetchType.LAZY)
       private Set<Employee> employees = new HashSet<>();
   }
   
   // Then use only when needed
   @Transactional
   public void processEmployees(Long departmentId) {
       Department dept = departmentRepository.findById(departmentId).orElseThrow();
       // Only now the employees are loaded
       dept.getEmployees().forEach(this::processEmployee);
   }
   ```

4. **Detached Entities with Lazy Collections**:
   - **Problem**: Accessing lazy collections outside a transaction.
   ```java
   @Service
   public class UserService {
       public List<User> getUsers() {
           List<User> users = userRepository.findAll();
           return users; // Detached entities returned
       }
   }
   
   // Later, outside transaction
   List<User> users = userService.getUsers();
   for (User user : users) {
       // LazyInitializationException here
       user.getRoles().forEach(System.out::println);
   }
   ```
   
   - **Solution**: Use DTOs, fetch joins, or keep the transaction open.
   ```java
   // DTO approach
   @Service
   public class UserService {
       public List<UserDTO> getUsers() {
           return userRepository.findAll().stream()
               .map(userMapper::toDTO)
               .collect(Collectors.toList());
       }
   }
   
   // Or fetch join approach
   @Service
   public class UserService {
       @Transactional(readOnly = true)
       public List<User> getUsersWithRoles() {
           return userRepository.findAllWithRoles();
       }
   }
   
   @Repository
   public interface UserRepository extends JpaRepository<User, Long> {
       @Query("SELECT u FROM User u LEFT JOIN FETCH u.roles")
       List<User> findAllWithRoles();
   }
   ```

5. **Second-Level Cache Misuse**:
   - **Problem**: Caching large entities or collections without proper configuration.
   ```java
   @Entity
   @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
   public class Product {
       // Entity fields
       
       @OneToMany(mappedBy = "product")
       @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
       private List<Review> reviews; // Potentially thousands of reviews
   }
   ```
   
   - **Solution**: Cache selectively and configure size limits.
   ```java
   @Entity
   @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
   public class Product {
       // Entity fields - cache the entity
       
       @OneToMany(mappedBy = "product")
       // Don't cache the collection
       private List<Review> reviews;
   }
   
   // In application.properties
   spring.jpa.properties.hibernate.cache.use_second_level_cache=true
   spring.jpa.properties.hibernate.cache.region.factory_class=org.hibernate.cache.jcache.JCacheRegionFactory
   spring.jpa.properties.hibernate.javax.cache.provider=org.ehcache.jsr107.EhcacheCachingProvider
   spring.jpa.properties.hibernate.javax.cache.uri=classpath:ehcache.xml
   
   <!-- In ehcache.xml -->
   <cache alias="com.example.Product">
       <expiry>
           <ttl unit="minutes">30</ttl>
       </expiry>
       <heap unit="entries">1000</heap>
   </cache>
   ```

6. **Open Session in View Anti-Pattern**:
   - **Problem**: Keeping Hibernate session open during view rendering.
   ```properties
   # Default is true in Spring Boot
   spring.jpa.open-in-view=true
   ```
   
   - **Solution**: Disable OSIV and load all necessary data in the service layer.
   ```properties
   spring.jpa.open-in-view=false
   ```
   
   ```java
   @Service
   @Transactional(readOnly = true)
   public class ProductService {
       public ProductDTO getProductWithDetails(Long id) {
           Product product = productRepository.findById(id).orElseThrow();
           
           // Explicitly initialize collections needed for the view
           Hibernate.initialize(product.getCategories());
           Hibernate.initialize(product.getReviews());
           
           return productMapper.toDTO(product);
       }
   }
   ```

7. **Entity Listeners Holding References**:
   - **Problem**: Entity listeners holding references to entities.
   ```java
   @Entity
   @EntityListeners(ProductListener.class)
   public class Product { /* ... */ }
   
   public class ProductListener {
       private static List<Product> modifiedProducts = new ArrayList<>(); // Memory leak
       
       @PostUpdate
       public void onPostUpdate(Product product) {
           modifiedProducts.add(product); // Continuously growing list
       }
   }
   ```
   
   - **Solution**: Avoid static collections in listeners or ensure proper cleanup.
   ```java
   @Component
   public class ProductListener {
       private final ProductAuditService auditService;
       
       public ProductListener(ProductAuditService auditService) {
           this.auditService = auditService;
       }
       
       @PostUpdate
       public void onPostUpdate(Product product) {
           auditService.logProductUpdate(product.getId()); // Just log the ID
       }
   }
   ```

8. **Improper Transaction Management**:
   - **Problem**: Very long transactions keeping entities in session.
   ```java
   @Service
   @Transactional
   public class ImportService {
       public void importLargeFile(InputStream is) {
           // Process thousands of records in a single transaction
           // All entities stay in the session until commit
       }
   }
   ```
   
   - **Solution**: Use batch processing and clear the session periodically.
   ```java
   @Service
   public class ImportService {
       @Autowired
       private EntityManager entityManager;
       
       @Transactional
       public void importLargeFile(InputStream is) {
           int batchSize = 50;
           int count = 0;
           
           for (Record record : readRecords(is)) {
               Entity entity = convertToEntity(record);
               entityManager.persist(entity);
               
               if (++count % batchSize == 0) {
                   entityManager.flush();
                   entityManager.clear(); // Clear the session
               }
           }
       }
   }
   ```

**Best Practices to Prevent JPA/Hibernate Memory Leaks:**

1. **Use DTOs for Service Layer Returns**:
   - Convert entities to DTOs before returning from services
   - Prevents accidental lazy loading and detached entity issues

2. **Configure Fetch Types Appropriately**:
   - Use LAZY as the default fetch type for collections
   - Use EAGER only when you know you'll always need the related entities

3. **Use Pagination for Large Result Sets**:
   - Never load all entities from large tables
   - Use Spring Data's Pageable interface

4. **Implement Database-Level Pagination for Reports**:
   - For large reports, use native queries with LIMIT/OFFSET
   - Stream results or process in batches

5. **Monitor Query Performance**:
   - Use p6spy or similar tools to log slow queries
   - Analyze and optimize frequently executed queries

6. **Disable Open Session in View**:
   - Set spring.jpa.open-in-view=false
   - Load all necessary data explicitly in the service layer

7. **Use Read-Only Transactions When Possible**:
   - Mark read-only operations with @Transactional(readOnly = true)
   - Reduces memory usage by skipping dirty checking

8. **Clear the Hibernate Session Periodically During Batch Operations**:
   - Use entityManager.clear() after processing batches of entities

By following these practices and addressing the common issues, you can effectively identify and fix memory leaks related to Hibernate/JPA in your Spring Boot applications.

## Lazy Loading and Initialization

### Q4: What are the different strategies for lazy loading in Spring and when should each be used?

**Answer:** Lazy loading in Spring can be implemented at different levels, each with its own use cases and trade-offs:

### 1. JPA Entity Relationship Lazy Loading

**Implementation:**
```java
@Entity
public class Department {
    @Id
    private Long id;
    
    private String name;
    
    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY)
    private List<Employee> employees;
}

@Entity
public class Employee {
    @Id
    private Long id;
    
    private String name;
    
    @ManyToOne(fetch = FetchType.LAZY)
    private Department department;
}
```

**When to use:**
- For entity relationships where the related entities are not always needed
- When loading the entire object graph would be inefficient
- For large collections or heavy entities

**Considerations:**
- Requires an active Hibernate session when accessing lazy properties
- Can lead to LazyInitializationException if accessed outside a transaction
- Performance benefit only realized if the lazy properties are not accessed

### 2. Spring Bean Lazy Initialization

**Implementation:**
```java
// Individual bean lazy loading
@Component
@Lazy
public class ExpensiveService {
    public ExpensiveService() {
        // Expensive initialization
    }
}

// Injecting a lazy bean
@Service
public class UserService {
    private final ExpensiveService expensiveService;
    
    @Autowired
    public UserService(@Lazy ExpensiveService expensiveService) {
        this.expensiveService = expensiveService;
    }
}

// Global lazy initialization
@SpringBootApplication
@EnableConfigurationProperties
public class Application {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        app.setLazyInitialization(true);
        app.run(args);
    }
}

// Or in application.properties
spring.main.lazy-initialization=true
```

**When to use:**
- For beans with expensive initialization that aren't always needed
- To reduce application startup time
- For beans only used in specific scenarios
- For conditional beans that might not be used in all environments

**Considerations:**
- First use of a lazy bean will incur the initialization cost
- Can make debugging more difficult as errors might occur at runtime rather than startup
- May hide configuration issues until runtime

### 3. Proxy-Based Lazy Loading

**Implementation:**
```java
// Using @Lookup for method injection
@Component
public abstract class DocumentProcessor {
    // This will be implemented by Spring to return a prototype bean
    @Lookup
    protected abstract ExpensiveDocumentParser getParser();
    
    public void processDocument(Document doc) {
        if (doc.needsParsing()) {
            ExpensiveDocumentParser parser = getParser();
            parser.parse(doc);
        }
    }
}

// Using ObjectFactory for lazy dependency injection
@Service
public class ReportService {
    private final ObjectFactory<ExpensiveReportGenerator> reportGeneratorFactory;
    
    @Autowired
    public ReportService(ObjectFactory<ExpensiveReportGenerator> reportGeneratorFactory) {
        this.reportGeneratorFactory = reportGeneratorFactory;
    }
    
    public Report generateReport() {
        // Only create when needed
        ExpensiveReportGenerator generator = reportGeneratorFactory.getObject();
        return generator.createReport();
    }
}
```

**When to use:**
- When you need to inject prototype-scoped beans into singleton beans
- For dependencies that should be obtained fresh each time they're used
- When you want to defer creation until the dependency is actually needed

**Considerations:**
- Adds complexity to the code
- Creates a new instance each time for prototype beans
- May be harder to test due to the dynamic nature

### 4. JPA Entity Graph for Selective Loading

**Implementation:**
```java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Static entity graph defined in the entity
    @EntityGraph(attributePaths = {"roles", "preferences"})
    Optional<User> findWith