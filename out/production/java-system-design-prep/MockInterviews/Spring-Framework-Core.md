# Spring Framework Core Interview Questions

This document contains interview questions and answers focused on Spring Framework core concepts that are commonly asked in Java full stack developer interviews (3-5 YOE).

## Dependency Injection and IoC

### 1. What is Dependency Injection and how does Spring implement it?

```java
// Without Dependency Injection
public class OrderService {
    private PaymentProcessor paymentProcessor = new PaymentProcessorImpl();
    
    public void processOrder(Order order) {
        // Use paymentProcessor
    }
}

// With Dependency Injection
public class OrderService {
    private final PaymentProcessor paymentProcessor;
    
    public OrderService(PaymentProcessor paymentProcessor) {
        this.paymentProcessor = paymentProcessor;
    }
    
    public void processOrder(Order order) {
        // Use paymentProcessor
    }
}
```

**Answer:**
Dependency Injection is a design pattern where objects receive their dependencies from external sources rather than creating them internally.

Spring implements DI through:
1. Constructor injection (preferred)
2. Setter injection
3. Field injection (not recommended for production code)

The Spring container manages object lifecycles and injects dependencies as needed.

**Explanation:**
- In the second example, the OrderService no longer creates its own PaymentProcessor
- The dependency is provided from outside, making it more flexible and testable
- This decouples the OrderService from specific PaymentProcessor implementations
- It enables easier unit testing by allowing mock implementations to be injected

**Follow-up Question:** What are the advantages of constructor injection over field injection?

### 2. Explain the difference between BeanFactory and ApplicationContext

```java
// Using BeanFactory
BeanFactory factory = new XmlBeanFactory(new FileSystemResource("beans.xml"));
MyBean bean = (MyBean) factory.getBean("myBean");

// Using ApplicationContext
ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
MyBean bean = context.getBean("myBean", MyBean.class);
```

**Answer:**
BeanFactory is the basic container interface, while ApplicationContext is an enhanced container that extends BeanFactory.

Key differences:
- ApplicationContext eagerly initializes beans; BeanFactory lazily initializes
- ApplicationContext provides enterprise features like AOP, event handling, i18n
- ApplicationContext has better integration with Spring's infrastructure
- BeanFactory is lighter weight but has fewer features

**Explanation:**
- BeanFactory is the root interface for accessing beans
- ApplicationContext adds more enterprise-specific functionality
- Most modern Spring applications use ApplicationContext
- BeanFactory might be used in memory-constrained environments

**Follow-up Question:** In what scenarios might you choose BeanFactory over ApplicationContext?

### 3. What happens if you have multiple beans of the same type?

```java
@Configuration
public class PaymentConfig {
    @Bean
    public PaymentProcessor creditCardProcessor() {
        return new CreditCardProcessor();
    }
    
    @Bean
    public PaymentProcessor paypalProcessor() {
        return new PayPalProcessor();
    }
}

// Injection point
@Service
public class OrderService {
    private final PaymentProcessor paymentProcessor;
    
    @Autowired
    public OrderService(PaymentProcessor paymentProcessor) {
        this.paymentProcessor = paymentProcessor;
    }
}
```

**Answer:**
When multiple beans of the same type exist, Spring throws a `NoUniqueBeanDefinitionException` unless you specify which bean to inject using:

1. `@Qualifier` annotation
2. `@Primary` annotation on one of the beans
3. Using `@Autowired` on a collection (List, Map) to inject all beans of that type

**Explanation:**
- Spring needs to know which bean to inject when multiple candidates exist
- `@Primary` marks a bean as the default choice when multiple exist
- `@Qualifier` lets you specify which bean to inject by name
- Injecting a collection gets all beans of that type

**Follow-up Question:** How would you modify the code to specify which processor to use?

### 4. What is the bean lifecycle in Spring?

```java
public class DatabaseInitializer implements InitializingBean, DisposableBean {
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("Database connection established");
    }
    
    @Override
    public void destroy() throws Exception {
        System.out.println("Database connection closed");
    }
    
    @PostConstruct
    public void init() {
        System.out.println("Bean is being initialized");
    }
    
    @PreDestroy
    public void cleanup() {
        System.out.println("Bean is being destroyed");
    }
}
```

**Answer:**
The Spring bean lifecycle follows these key phases:

1. **Bean Instantiation**: Spring creates instances of beans
2. **Dependency Injection**: Properties and dependencies are set
3. **Awareness interfaces callbacks**: If beans implement interfaces like BeanNameAware, BeanFactoryAware
4. **Bean Post-Processing**: BeanPostProcessors perform pre-initialization processing
5. **Initialization callbacks**: @PostConstruct, InitializingBean, or init-method
6. **Post-initialization processing**: BeanPostProcessors complete initialization
7. **Bean is ready for use**
8. **Destruction callbacks**: @PreDestroy, DisposableBean, or destroy-method when container shuts down

**Explanation:**
- The example shows multiple ways to hook into the lifecycle
- @PostConstruct and @PreDestroy are the most common annotations
- InitializingBean and DisposableBean interfaces are older approaches
- Custom init-method and destroy-method can also be specified in XML or @Bean annotation

**Follow-up Question:** What order would the methods execute in the example code?

### 5. What is the difference between @Component, @Service, @Repository, and @Controller?

```java
@Component
public class GenericComponent {
    // Generic component logic
}

@Repository
public class UserRepository {
    // Data access logic
}

@Service
public class UserService {
    // Business logic
}

@Controller
public class UserController {
    // Web request handling
}
```

**Answer:**
These are all stereotypes that mark classes as Spring beans, but with different specialized purposes:

- **@Component**: Base annotation indicating a Spring-managed component
- **@Repository**: Specialization of @Component for data access objects (DAO), adds automatic persistence exception translation
- **@Service**: Specialization of @Component for service layer, business logic
- **@Controller**: Specialization of @Component for web controllers, handles web requests

**Explanation:**
- All of these annotations mark classes for component scanning
- They are functionally equivalent for dependency injection
- They differ in semantics and additional behavior
- @Repository adds exception translation for data access exceptions
- @Controller is used with @RequestMapping for web endpoints

**Follow-up Question:** Can you replace all @Service annotations with @Component and expect the application to work the same?

## Spring Configuration

### 6. What's the difference between @Bean and @Component?

```java
// Using @Component
@Component
public class PaymentProcessorImpl implements PaymentProcessor {
    // Implementation
}

// Using @Bean
@Configuration
public class PaymentConfig {
    @Bean
    public PaymentProcessor paymentProcessor() {
        PaymentProcessor processor = new PaymentProcessorImpl();
        // Additional configuration
        return processor;
    }
}
```

**Answer:**
@Bean and @Component serve similar purposes but in different contexts:

- **@Component** is a class-level annotation that marks a class for auto-detection through component scanning
- **@Bean** is a method-level annotation used within @Configuration classes to explicitly define beans and their dependencies
- @Bean gives you more control over the instantiation and configuration process
- @Component is simpler but offers less control

**Explanation:**
- @Bean lets you create beans from classes you don't own (third-party libraries)
- @Bean allows more programmatic configuration during bean creation
- @Bean methods can have custom logic for creating beans with specific parameters
- @Component is used when the class is under your control

**Follow-up Question:** When would you prefer using @Bean over @Component?

### 7. What is the Spring Expression Language (SpEL)?

```java
@Component
public class UserSettings {
    @Value("${app.timeout:30}")
    private int timeout;
    
    @Value("#{systemProperties['user.region'] ?: 'US'}")
    private String region;
    
    @Value("#{userService.findUserById(1)?.name}")
    private String adminName;
}
```

**Answer:**
Spring Expression Language (SpEL) is a powerful expression language that supports querying and manipulating object graphs at runtime. It can be used for:

- Referencing beans and their properties
- Method invocation
- Mathematical operations
- Logical operations
- Accessing collections and arrays
- Conditional expressions (ternary operator)
- Regular expressions

**Explanation:**
- It's typically used within @Value annotations or XML configurations
- ${...} is for property placeholders from property files
- #{...} indicates a SpEL expression
- In the example, SpEL is used to:
  1. Get a property with default value (timeout)
  2. Get a system property with fallback (region)
  3. Call a method on another bean and access a property safely (adminName)

**Follow-up Question:** What is the difference between ${...} and #{...} in Spring annotations?

### 8. How does Spring's @Conditional annotation work?

```java
@Configuration
public class DataSourceConfig {
    @Bean
    @ConditionalOnProperty(name = "db.type", havingValue = "mysql")
    public DataSource mysqlDataSource() {
        return new MysqlDataSource();
    }
    
    @Bean
    @ConditionalOnProperty(name = "db.type", havingValue = "postgres")
    public DataSource postgresDataSource() {
        return new PostgresDataSource();
    }
    
    @Bean
    @ConditionalOnMissingBean(DataSource.class)
    public DataSource h2DataSource() {
        return new H2DataSource(); // Default
    }
}
```

**Answer:**
@Conditional and its derivatives control whether a bean should be registered based on certain conditions. Spring Boot expands this with specialized conditionals:

- @ConditionalOnProperty: Based on property presence/value
- @ConditionalOnBean/@ConditionalOnMissingBean: Based on presence/absence of other beans
- @ConditionalOnClass/@ConditionalOnMissingClass: Based on presence/absence of classes
- @ConditionalOnExpression: Based on SpEL expression
- @Profile: Based on active profiles

**Explanation:**
- In the example, the created DataSource depends on the "db.type" property
- If db.type=mysql, a MySQL DataSource is created
- If db.type=postgres, a Postgres DataSource is created
- If neither property value matches, an H2 DataSource is created as a fallback

**Follow-up Question:** How would you create a custom conditional annotation?

### 9. What are Spring Profiles and how do they work?

```java
@Configuration
@Profile("development")
public class DevConfig {
    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.H2)
            .build();
    }
}

@Configuration
@Profile("production")
public class ProdConfig {
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl("jdbc:mysql://prod-server:3306/myapp");
        // Set other properties
        return dataSource;
    }
}
```

**Answer:**
Spring Profiles provide a way to register different beans for different environments. They help:

- Define beans that should only be active in specific environments
- Separate configuration for development, testing, and production
- Enable/disable features based on deployment context

Profiles can be activated via:
- spring.profiles.active property
- SpringApplication.setAdditionalProfiles()
- @ActiveProfiles in tests
- JVM system properties
- Environment variables

**Explanation:**
- In the example, different DataSource beans are defined for dev and prod
- Only one will be active based on the active profile
- This allows environment-specific configuration without code changes

**Follow-up Question:** How would you test a component that depends on a profile-specific bean?

### 10. How does Spring handle property configuration?

```java
@Configuration
@PropertySource("classpath:application.properties")
public class AppConfig {
    @Value("${app.name}")
    private String appName;
    
    @Autowired
    private Environment environment;
    
    public String getDatabaseUrl() {
        return environment.getProperty("database.url");
    }
}

// application.properties
app.name=My Application
database.url=jdbc:mysql://localhost:3306/mydb
```

**Answer:**
Spring provides several mechanisms for handling property configuration:

- **PropertySource**: Loads properties from specified resources
- **Environment**: Centralizes access to property sources
- **@Value**: Injects property values directly into fields
- **@ConfigurationProperties**: Binds properties to structured classes
- Properties can come from various sources with a defined precedence (command line args, application.properties, etc.)

**Explanation:**
- The example shows loading properties from application.properties
- @Value is used for direct property injection
- Environment provides programmatic access to properties
- Spring Boot adds additional auto-configuration for properties

**Follow-up Question:** What is the default precedence order for property sources in Spring Boot?

## Aspect-Oriented Programming (AOP)

### 11. What is AOP and how does Spring implement it?

```java
@Aspect
@Component
public class PerformanceMonitoringAspect {
    @Around("execution(* com.example.service.*.*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long end = System.currentTimeMillis();
        System.out.println(joinPoint.getSignature() + " executed in " + (end - start) + "ms");
        return result;
    }
}
```

**Answer:**
Aspect-Oriented Programming (AOP) is a programming paradigm that aims to increase modularity by separating cross-cutting concerns. Spring AOP allows you to:

- Define aspects (modular units of cross-cutting logic)
- Apply aspects to specific points in your code (joinpoints)
- Define pointcuts (expressions that select joinpoints)
- Define advice (actions taken at joinpoints)

Spring AOP uses either:
- JDK dynamic proxies (for interface-based proxies)
- CGLIB proxies (for class-based proxies)

**Explanation:**
- The example creates an aspect that measures method execution time
- It uses @Around advice to wrap method calls
- The pointcut expression selects all methods in service classes
- This centralizes performance monitoring logic without modifying service code

**Follow-up Question:** What is the difference between Spring AOP and AspectJ?

### 12. Explain the different types of advice in Spring AOP

```java
@Aspect
@Component
public class LoggingAspect {
    @Before("execution(* com.example.service.UserService.find*(..))")
    public void logBefore(JoinPoint joinPoint) {
        System.out.println("Before method: " + joinPoint.getSignature());
    }
    
    @After("execution(* com.example.service.UserService.find*(..))")
    public void logAfter(JoinPoint joinPoint) {
        System.out.println("After method: " + joinPoint.getSignature());
    }
    
    @AfterReturning(
        pointcut = "execution(* com.example.service.UserService.find*(..))",
        returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        System.out.println("Method returned: " + result);
    }
    
    @AfterThrowing(
        pointcut = "execution(* com.example.service.UserService.find*(..))",
        throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Exception exception) {
        System.out.println("Method threw exception: " + exception.getMessage());
    }
    
    @Around("execution(* com.example.service.UserService.find*(..))")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("Before proceeding");
        Object result = joinPoint.proceed();
        System.out.println("After proceeding");
        return result;
    }
}
```

**Answer:**
Spring AOP provides five types of advice:

1. **@Before**: Runs before the joinpoint execution
2. **@After**: Runs after the joinpoint completes (regardless of outcome)
3. **@AfterReturning**: Runs after the joinpoint completes successfully
4. **@AfterThrowing**: Runs if the joinpoint throws an exception
5. **@Around**: Surrounds the joinpoint execution, can control if/when it executes

**Explanation:**
- @Before and @After are simpler but provide less control
- @AfterReturning gives access to the method's return value
- @AfterThrowing lets you handle exceptions in the aspect
- @Around is the most powerful, giving complete control over the method execution
- The pointcut expression in the example targets all methods in UserService that start with "find"

**Follow-up Question:** If multiple advices are defined for the same joinpoint, in what order do they execute?

### 13. What are pointcut expressions in Spring AOP?

```java
@Aspect
@Component
public class SecurityAspect {
    // Match any public method in the service package
    @Before("execution(public * com.example.service.*.*(..))")
    public void logServiceAccess(JoinPoint joinPoint) {
        // Implementation
    }
    
    // Match methods with @Secured annotation
    @Before("@annotation(com.example.Secured)")
    public void checkSecurity(JoinPoint joinPoint) {
        // Implementation
    }
    
    // Match methods in classes with @Service annotation
    @Before("within(@org.springframework.stereotype.Service *)")
    public void logServiceLayerAccess(JoinPoint joinPoint) {
        // Implementation
    }
    
    // Combining pointcuts
    @Before("execution(* com.example.service.*.*(..)) && args(id)")
    public void logAccessById(JoinPoint joinPoint, Long id) {
        System.out.println("Accessing entity with ID: " + id);
    }
}
```

**Answer:**
Pointcut expressions define where advice should be applied in Spring AOP. Common pointcut designators include:

- **execution**: Match method execution joinpoints
- **within**: Match joinpoints within certain types
- **this**: Match joinpoints where the proxy implements a given type
- **target**: Match joinpoints where the target object implements a given type
- **args**: Match joinpoints where arguments are of certain types
- **@annotation**: Match joinpoints where the method has a specific annotation
- **@within/@target**: Match joinpoints where the class has a specific annotation
- **bean**: Match joinpoints on a specific named bean

**Explanation:**
- Pointcut expressions can be combined using &&, ||, and !
- In the example, different pointcuts target methods based on:
  1. Package and visibility
  2. Presence of a specific annotation
  3. Being in a class with @Service annotation
  4. Method signature and parameter type

**Follow-up Question:** How would you create a reusable pointcut expression?

## Spring Event System

### 14. How does Spring's event handling system work?

```java
// Event class
public class UserCreatedEvent extends ApplicationEvent {
    private final User user;
    
    public UserCreatedEvent(Object source, User user) {
        super(source);
        this.user = user;
    }
    
    public User getUser() {
        return user;
    }
}

// Publisher
@Service
public class UserService {
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    
    public User createUser(String username) {
        User user = new User(username);
        // Save user to database
        
        // Publish event
        eventPublisher.publishEvent(new UserCreatedEvent(this, user));
        
        return user;
    }
}

// Listener
@Component
public class UserEventListener {
    @EventListener
    public void handleUserCreatedEvent(UserCreatedEvent event) {
        System.out.println("User created: " + event.getUser().getUsername());
        // Send welcome email, etc.
    }
    
    @EventListener
    @Async
    public void handleUserCreatedEventAsync(UserCreatedEvent event) {
        // Process asynchronously
    }
    
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleUserCreatedEventTransactional(UserCreatedEvent event) {
        // Only process if transaction commits
    }
}
```

**Answer:**
Spring's event system allows components to communicate with each other without direct coupling. Key components include:

- **ApplicationEvent**: Base class for events
- **ApplicationEventPublisher**: Interface for publishing events
- **ApplicationListener**: Interface for listening to events
- **@EventListener**: Annotation-based alternative to ApplicationListener
- **@TransactionalEventListener**: For transaction-bound events

**Explanation:**
- The example shows creating, publishing, and handling a custom event
- Different listeners can handle the same event in different ways
- @Async allows event handling to happen in a separate thread
- @TransactionalEventListener binds event processing to transaction phases

**Follow-up Question:** How would you implement a custom event publisher that isn't coupled to Spring's ApplicationEventPublisher?

### 15. What is a TransactionalEventListener and when would you use it?

```java
@Service
@Transactional
public class OrderService {
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    
    @Autowired
    private OrderRepository orderRepository;
    
    public Order placeOrder(Order order) {
        Order savedOrder = orderRepository.save(order);
        eventPublisher.publishEvent(new OrderCreatedEvent(this, savedOrder));
        return savedOrder;
    }
}

@Component
public class OrderEventHandler {
    @Autowired
    private EmailService emailService;
    
    // This might fail if the transaction hasn't committed yet
    @EventListener
    public void handleOrderCreated(OrderCreatedEvent event) {
        Order order = event.getOrder();
        emailService.sendOrderConfirmation(order);
    }
    
    // This will only execute after the transaction commits
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleOrderCreatedAfterCommit(OrderCreatedEvent event) {
        Order order = event.getOrder();
        emailService.sendOrderConfirmation(order);
    }
}
```

**Answer:**
@TransactionalEventListener is a specialized event listener that binds event handling to a specific phase of a transaction:

- **AFTER_COMMIT**: Default; executes if the transaction commits successfully
- **AFTER_ROLLBACK**: Executes if the transaction rolls back
- **AFTER_COMPLETION**: Executes after the transaction completes (commit or rollback)
- **BEFORE_COMMIT**: Executes before the transaction commits

It's useful when:
- You need to ensure database changes are committed before processing events
- You want to avoid processing events for transactions that fail
- You need to perform different actions based on transaction outcome

**Explanation:**
- In the example, sending an email confirmation should only happen after the order is successfully committed to the database
- The regular @EventListener might try to reference data that isn't fully committed yet
- The @TransactionalEventListener ensures proper sequencing of operations

**Follow-up Question:** What happens if an event is published outside of a transaction context when using @TransactionalEventListener?
