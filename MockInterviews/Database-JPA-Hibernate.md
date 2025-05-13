# Database-JPA-Hibernate Interview Questions

This document contains interview questions and answers focused on JPA, Hibernate, and database concepts that are commonly asked in Java full stack developer interviews (3-5 YOE).

## JPA Fundamentals

### 1. What's the difference between FetchType.EAGER and FetchType.LAZY?

```java
@Entity
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    
    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY)
    private List<Employee> employees = new ArrayList<>();
}

@Entity
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "department_id")
    private Department department;
}
```

**Answer:**
FetchType determines when JPA providers like Hibernate should load related entities:

- **EAGER**: Associated entities are loaded immediately when the owning entity is loaded
- **LAZY**: Associated entities are loaded only when explicitly accessed (proxy pattern)

Default fetch types:
- @OneToMany and @ManyToMany default to LAZY
- @OneToOne and @ManyToOne default to EAGER

**Explanation:**
- In the example, whenever an Employee is loaded, its Department will be loaded immediately (EAGER)
- When a Department is loaded, its Employees won't be loaded until the getEmployees() method is called (LAZY)
- Lazy loading requires an open session/persistence context when the association is accessed
- Using EAGER for all associations can lead to performance issues with large object graphs

**Follow-up Question:** What is the "N+1 query problem" and how does it relate to fetch types?

### 2. Explain the JPA entity lifecycle and its callback methods

```java
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private BigDecimal price;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }
    
    @PostLoad
    protected void onLoad() {
        // Audit logging or data validation
    }
    
    @PreRemove
    protected void onDelete() {
        // Pre-deletion logic
    }
}
```

**Answer:**
The JPA entity lifecycle consists of four states:
1. **New/Transient**: Entity instance not associated with persistence context
2. **Managed**: Entity associated with persistence context, changes are tracked
3. **Detached**: Entity was managed but no longer associated with persistence context
4. **Removed**: Entity marked for removal from database

JPA provides callback annotations to hook into lifecycle events:
- **@PrePersist**: Before entity is persisted (inserted)
- **@PostPersist**: After entity is persisted
- **@PreUpdate**: Before entity is updated
- **@PostUpdate**: After entity is updated
- **@PreRemove**: Before entity is deleted
- **@PostRemove**: After entity is deleted
- **@PostLoad**: After entity is loaded from database

**Explanation:**
- In the example, the Product entity automatically sets createdAt on first save and updatedAt on any update
- These callbacks allow for automatic timestamp management, validation, audit logging
- They can also trigger business logic that should always occur during state transitions
- Methods with these annotations can be in the entity class or in separate EntityListener classes

**Follow-up Question:** How would you implement a universal audit logging system for all entities using JPA callbacks?

### 3. What are the different types of inheritance strategies in JPA?

```java
// SINGLE_TABLE strategy
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "payment_type")
public abstract class Payment {
    @Id
    @GeneratedValue
    private Long id;
    private BigDecimal amount;
    // Common fields and methods
}

@Entity
@DiscriminatorValue("CC")
public class CreditCardPayment extends Payment {
    private String cardNumber;
    private String expiryDate;
    // Credit card specific fields
}

@Entity
@DiscriminatorValue("BANK")
public class BankTransferPayment extends Payment {
    private String accountNumber;
    private String bankCode;
    // Bank transfer specific fields
}
```

**Answer:**
JPA provides four inheritance mapping strategies:

1. **SINGLE_TABLE** (default):
   - All classes in hierarchy mapped to one table
   - Uses a discriminator column to distinguish between types
   - Pros: Best performance, simplest queries
   - Cons: Non-nullable columns in subclasses must be nullable in DB

2. **JOINED**:
   - Base class and each subclass have their own tables
   - Joined via foreign keys
   - Pros: Normalized schema, supports non-null constraints
   - Cons: Requires joins for queries, potentially slower

3. **TABLE_PER_CLASS**:
   - Each concrete class gets its own table with all fields
   - No discriminator column needed
   - Pros: Simple queries for concrete classes
   - Cons: Duplicate columns, polymorphic queries use UNION operations (slow)

4. **MAPPED_SUPERCLASS**:
   - Base class not an entity, just provides common fields
   - Not a true inheritance strategy as base class isn't queryable
   - Used for common fields/behavior without polymorphic queries

**Explanation:**
- The example uses SINGLE_TABLE with a payment_type discriminator column
- All payment types will be in one table with potential NULL values for subclass-specific fields
- This strategy offers the best performance but may have database schema limitations
- The choice depends on your requirements for normalization vs. performance

**Follow-up Question:** In which scenarios would you choose JOINED over SINGLE_TABLE despite the performance impact?

## Transaction Management

### 4. Explain the various propagation levels in @Transactional

```java
@Service
public class OrderService {
    @Autowired
    private PaymentService paymentService;
    
    @Autowired
    private InventoryService inventoryService;
    
    @Transactional
    public void placeOrder(Order order) {
        // Create order in DB
        
        // Calls another transactional method
        paymentService.processPayment(order);
        
        // Update inventory
        inventoryService.updateStock(order.getItems());
    }
}

@Service
public class PaymentService {
    @Transactional(propagation = Propagation.REQUIRED)
    public void processPayment(Order order) {
        // Process payment
    }
}

@Service
public class InventoryService {
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateStock(List<OrderItem> items) {
        // Update inventory
    }
}
```

**Answer:**
Spring's @Transactional annotation supports several propagation levels that define how transactions relate to each other:

1. **REQUIRED** (default): 
   - Uses current transaction if one exists, otherwise creates a new one
   - Multiple methods share the same transaction

2. **REQUIRES_NEW**: 
   - Always creates a new transaction, suspending current transaction if one exists
   - Independent transaction that can commit/rollback without affecting the caller

3. **SUPPORTS**: 
   - Uses current transaction if one exists, otherwise runs non-transactionally
   - Flexible for methods that can work with or without transactions

4. **NOT_SUPPORTED**: 
   - Runs non-transactionally, suspending current transaction if one exists
   - Used when transactional context might cause issues

5. **MANDATORY**: 
   - Must run within an existing transaction, throws exception if none exists
   - Enforces that caller handles transaction management

6. **NEVER**: 
   - Must run non-transactionally, throws exception if transaction exists
   - Ensures method is never part of a transaction

7. **NESTED**: 
   - Creates a savepoint in current transaction if one exists
   - Can rollback to that savepoint without affecting outer transaction

**Explanation:**
- In the example, OrderService.placeOrder creates the main transaction
- PaymentService.processPayment runs in the same transaction (REQUIRED)
- InventoryService.updateStock creates a new independent transaction (REQUIRES_NEW)
- If payment fails, the order creation is rolled back but inventory updates might still commit
- This allows more granular control over transaction boundaries and error handling

**Follow-up Question:** What would happen if an exception is thrown in the updateStock method with different propagation levels (REQUIRED vs REQUIRES_NEW)?

### 5. What is the difference between optimistic and pessimistic locking?

```java
// Optimistic locking example
@Entity
public class Account {
    @Id
    private Long id;
    
    private BigDecimal balance;
    
    @Version
    private Integer version;
    
    public void withdraw(BigDecimal amount) {
        this.balance = this.balance.subtract(amount);
    }
}

// Pessimistic locking example
@Repository
public class AccountRepository extends JpaRepository<Account, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM Account a WHERE a.id = :id")
    Account findByIdWithPessimisticLock(@Param("id") Long id);
}

@Service
public class TransferService {
    @Autowired
    private AccountRepository accountRepository;
    
    @Transactional
    public void transfer(Long fromId, Long toId, BigDecimal amount) {
        // Using pessimistic locking
        Account fromAccount = accountRepository.findByIdWithPessimisticLock(fromId);
        Account toAccount = accountRepository.findByIdWithPessimisticLock(toId);
        
        fromAccount.withdraw(amount);
        toAccount.deposit(amount);
    }
}
```

**Answer:**
JPA provides two main concurrency control strategies:

**Optimistic Locking**:
- Assumes conflicts are rare
- Uses a version field (@Version) that's incremented on each update
- If version differs at update time, throws OptimisticLockException
- No database locks held during transaction
- Better scalability, less database contention

**Pessimistic Locking**:
- Assumes conflicts are likely
- Acquires actual database locks when data is read
- Prevents others from reading/modifying data until transaction completes
- Types: PESSIMISTIC_READ, PESSIMISTIC_WRITE, PESSIMISTIC_FORCE_INCREMENT
- More reliable for high-contention scenarios but reduced scalability

**Explanation:**
- In the optimistic example, the @Version field automatically tracks entity changes
- If two users modify the same entity concurrently, the second commit will fail
- In the pessimistic example, explicit locks are acquired using LockModeType
- Other transactions attempting to access these records will block until the first transaction completes
- Optimistic locking is generally preferred for web applications due to better scalability

**Follow-up Question:** What strategies would you use to handle OptimisticLockException in a web application?

## Hibernate Caching

### 6. Explain Hibernate's first and second-level cache

```java
// Enabling second-level cache in persistence.xml
<persistence-unit name="myPersistenceUnit">
    <properties>
        <property name="hibernate.cache.use_second_level_cache" value="true"/>
        <property name="hibernate.cache.region.factory_class"
                  value="org.hibernate.cache.ehcache.EhCacheRegionFactory"/>
    </properties>
</persistence-unit>

// Entity configuration with cache settings
@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Product {
    @Id
    private Long id;
    private String name;
    private BigDecimal price;
    
    // Getters and setters
}

// Usage in repository
@Repository
public class ProductService {
    @PersistenceContext
    private EntityManager em;
    
    public Product findById(Long id) {
        // Uses both first and second level cache
        return em.find(Product.class, id);
    }
    
    public void clearFirstLevelCache() {
        // Clears first-level cache
        em.clear();
    }
}
```

**Answer:**
Hibernate implements a multi-level caching system:

**First-Level Cache**:
- Session/EntityManager-scoped cache (transaction-level)
- Always enabled, cannot be disabled
- Guarantees that the same entity instance is returned for a given ID within a session
- Cleared when session is closed or em.clear() is called
- Prevents duplicate object loading within a transaction

**Second-Level Cache**:
- SessionFactory-scoped cache (application-level)
- Shared across all sessions/transactions
- Must be explicitly enabled and configured
- Requires a cache provider (EhCache, Caffeine, etc.)
- Configured at entity or collection level using @Cache annotation
- Different concurrency strategies available (READ_ONLY, NONSTRICT_READ_WRITE, READ_WRITE, TRANSACTIONAL)

**Explanation:**
- In the example, Product entities are cached in both levels
- For repeated lookups within the same session, only the first-level cache is used
- For lookups across different sessions, the second-level cache prevents database hits
- The READ_WRITE strategy allows for cached entities to be updated safely
- When an entity is updated, the cache is automatically invalidated or updated

**Follow-up Question:** What is the difference between Hibernate's second-level cache and query cache?

### 7. How do you solve the N+1 query problem in Hibernate?

```java
// N+1 problem example
@Entity
public class Author {
    @Id
    private Long id;
    private String name;
    
    @OneToMany(mappedBy = "author")
    private List<Book> books = new ArrayList<>();
}

@Entity
public class Book {
    @Id
    private Long id;
    private String title;
    
    @ManyToOne
    private Author author;
}

// Causing N+1 queries
List<Author> authors = entityManager.createQuery("SELECT a FROM Author a", Author.class)
                                   .getResultList();
for (Author author : authors) {
    System.out.println(author.getName() + " has written " + author.getBooks().size() + " books");
}

// Solution 1: JOIN FETCH
List<Author> authors = entityManager.createQuery(
    "SELECT a FROM Author a LEFT JOIN FETCH a.books", Author.class)
    .getResultList();
    
// Solution 2: EntityGraph
@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    @EntityGraph(attributePaths = {"books"})
    List<Author> findAll();
}

// Solution 3: Batch fetching
@Entity
public class Author {
    @Id
    private Long id;
    private String name;
    
    @OneToMany(mappedBy = "author")
    @BatchSize(size = 20)
    private List<Book> books = new ArrayList<>();
}
```

**Answer:**
The N+1 query problem occurs when an ORM loads N child entities individually after loading a parent entity, resulting in N+1 database queries (1 for the parent list, N for each child collection).

Solutions to the N+1 query problem:

1. **JOIN FETCH**: 
   - Uses JPQL to eagerly fetch the association in a single query
   - Creates a Cartesian product which may lead to duplicate parent entities
   - Can use DISTINCT to remove duplicates (memory overhead)

2. **EntityGraph**:
   - JPA feature for defining which associations to fetch
   - More flexible than fetch joins for complex scenarios
   - Can be defined as named graphs or dynamic graphs

3. **@BatchSize**:
   - Loads collections in batches instead of individually
   - Reduces N+1 to N/batch_size+1 queries
   - Good compromise when full eager loading causes too much data

4. **@Fetch(FetchMode.SUBSELECT)**:
   - Loads all collections for queried entities using a subselect
   - Results in exactly 2 queries regardless of result size
   - Useful for larger collections

**Explanation:**
- The initial example loads all Authors and then makes a separate query for each Author's Books
- JOIN FETCH loads both Authors and Books in a single query, but may duplicate Author records
- EntityGraph provides a more elegant way to specify eager loading paths
- BatchSize is a middle ground that reduces query count without loading everything at once

**Follow-up Question:** What are the trade-offs between using JOIN FETCH and @BatchSize for large datasets?

## Query Techniques

### 8. Compare JPQL, Criteria API, and native SQL queries

```java
// JPQL example
String jpql = "SELECT e FROM Employee e WHERE e.department.name = :deptName AND e.salary > :minSalary";
List<Employee> employees = entityManager.createQuery(jpql, Employee.class)
                                      .setParameter("deptName", "Engineering")
                                      .setParameter("minSalary", 50000)
                                      .getResultList();

// Criteria API example
CriteriaBuilder cb = entityManager.getCriteriaBuilder();
CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
Root<Employee> employee = query.from(Employee.class);
Join<Employee, Department> department = employee.join("department");

query.select(employee)
     .where(
         cb.equal(department.get("name"), "Engineering"),
         cb.greaterThan(employee.get("salary"), 50000)
     );

List<Employee> employees = entityManager.createQuery(query).getResultList();

// Native SQL example
String sql = "SELECT e.* FROM employees e " +
             "JOIN departments d ON e.department_id = d.id " +
             "WHERE d.name = ? AND e.salary > ?";
             
List<Employee> employees = entityManager.createNativeQuery(sql, Employee.class)
                                      .setParameter(1, "Engineering")
                                      .setParameter(2, 50000)
                                      .getResultList();
```

**Answer:**
JPA offers three main approaches for querying:

**JPQL (Java Persistence Query Language)**:
- String-based, entity-oriented query language
- Similar to SQL but operates on entities, not tables
- Pros: Simple, readable, portable across JPA providers
- Cons: Not type-safe, syntax errors caught only at runtime

**Criteria API**:
- Type-safe, programmatic query construction
- Compile-time syntax checking
- Better for dynamic queries built at runtime
- Pros: Type safety, refactoring-friendly, dynamic query building
- Cons: More verbose, steeper learning curve, less readable

**Native SQL**:
- Direct SQL queries to the database
- Bypasses JPA's entity mapping
- Pros: Full access to database-specific features
- Cons: Not portable, tightly coupled to database schema

**Explanation:**
- All three examples achieve the same result but with different approaches
- JPQL uses a SQL-like syntax but references entity and property names
- Criteria API builds the query programmatically with explicit type checking
- Native SQL operates directly at the database level with table and column names
- The choice depends on query complexity, dynamism, and portability requirements

**Follow-up Question:** How would you implement a complex dynamic search feature with multiple optional filters using each approach?

### 9. What is the purpose of @NamedQuery and when would you use it?

```java
// Defining named queries on the entity
@Entity
@NamedQueries({
    @NamedQuery(
        name = "Product.findByCategory",
        query = "SELECT p FROM Product p WHERE p.category.name = :categoryName"
    ),
    @NamedQuery(
        name = "Product.findOutOfStock",
        query = "SELECT p FROM Product p WHERE p.stockQuantity = 0"
    )
})
public class Product {
    @Id
    private Long id;
    private String name;
    private int stockQuantity;
    
    @ManyToOne
    private Category category;
    
    // Getters and setters
}

// Using named queries
@Repository
public class ProductRepository {
    @PersistenceContext
    private EntityManager entityManager;
    
    public List<Product> findByCategory(String categoryName) {
        return entityManager.createNamedQuery("Product.findByCategory", Product.class)
                          .setParameter("categoryName", categoryName)
                          .getResultList();
    }
    
    public List<Product> findOutOfStock() {
        return entityManager.createNamedQuery("Product.findOutOfStock", Product.class)
                          .getResultList();
    }
}
```

**Answer:**
@NamedQuery is a JPA annotation that allows defining and naming JPQL queries at the entity level. Key characteristics:

- Queries are parsed and validated at application startup
- Syntax errors cause immediate failure rather than runtime exceptions
- Queries are cached by the persistence provider for better performance
- Creates a central repository of commonly used queries
- Can be referenced by name throughout the application

**Explanation:**
- In the example, two named queries are defined on the Product entity
- They are given meaningful names that describe their purpose
- The repository uses createNamedQuery with the query name to execute them
- This approach separates query definition from query execution
- Benefits include early validation, reusability, and potential performance improvements

**Follow-up Question:** What are the advantages and disadvantages of defining queries with @NamedQuery versus using a repository interface with Spring Data JPA's query derivation?

### 10. How do you implement pagination with JPA repositories?

```java
// Basic pagination with JPA
public List<Product> findProductsPaginated(int page, int pageSize) {
    return entityManager.createQuery("SELECT p FROM Product p ORDER BY p.name", Product.class)
                      .setFirstResult((page - 1) * pageSize)
                      .setMaxResults(pageSize)
                      .getResultList();
}

// Pagination with Spring Data JPA
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Method returns Page with metadata (total pages, elements, etc.)
    Page<Product> findByCategory(String category, Pageable pageable);
    
    // Method returns just the content slice
    Slice<Product> findByPriceGreaterThan(BigDecimal price, Pageable pageable);
}

// Using the repository
@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    
    public Page<Product> findProductsByCategory(String category, int page, int size) {
        return productRepository.findByCategory(
            category, 
            PageRequest.of(page, size, Sort.by("name").ascending())
        );
    }
    
    // Implementing infinite scroll
    public Slice<Product> getNextProductBatch(BigDecimal minPrice, int page, int size) {
        return productRepository.findByPriceGreaterThan(
            minPrice,
            PageRequest.of(page, size)
        );
    }
}
```

**Answer:**
JPA provides built-in pagination support that translates to database-specific paging mechanisms. There are two main approaches:

**Basic JPA Pagination**:
- Uses setFirstResult() to specify the offset
- Uses setMaxResults() to specify the page size
- Requires manual calculation of the offset based on page number
- Doesn't provide metadata about total results or pages

**Spring Data JPA Pagination**:
- Uses Pageable interface to encapsulate pagination information
- Returns Page object with content and metadata (total elements, pages, etc.)
- Alternatively returns Slice for more efficient "next page" scenarios
- Supports sorting through Sort object
- Integrates with query methods through method signatures

**Explanation:**
- In the example, the pure JPA approach manually calculates offsets
- The Spring Data JPA approach uses PageRequest.of() to create a Pageable
- Page returns complete pagination metadata but requires a count query
- Slice only checks if there's a next page, which is more efficient for infinite scroll UIs
- Sorting can be combined with pagination using the Sort parameter

**Follow-up Question:** What are the performance implications of counting total records for pagination with very large tables? How would you optimize this?
