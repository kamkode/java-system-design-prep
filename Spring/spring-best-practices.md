# Spring Framework and Spring Boot Best Practices and Design Questions

This document contains interview questions related to best practices and design principles in Spring Framework and Spring Boot, suitable for candidates with 3-5 years of experience. These questions cover SOLID principles, REST design, Spring Security configuration, microservice design, and more.

## Table of Contents
1. [SOLID Principles in Spring](#solid-principles-in-spring)
2. [REST API Design](#rest-api-design)
3. [Spring Security Best Practices](#spring-security-best-practices)
4. [Microservice Design](#microservice-design)
5. [Application Configuration](#application-configuration)
6. [Database Access and ORM](#database-access-and-orm)
7. [Exception Handling](#exception-handling)
8. [Testing Strategies](#testing-strategies)

## SOLID Principles in Spring

### Q1: How does Spring Framework support the Single Responsibility Principle (SRP)? Provide examples.

**Answer:** Spring Framework supports the Single Responsibility Principle through several mechanisms:

1. **Component-based architecture**: Spring encourages organizing code into specialized components with clear responsibilities:
   - `@Controller` for handling web requests
   - `@Service` for business logic
   - `@Repository` for data access
   - `@Component` for general-purpose components

2. **Aspect-Oriented Programming (AOP)**: Spring AOP allows cross-cutting concerns to be separated from business logic:
   ```
   @Aspect
   @Component
   public class LoggingAspect {
       @Before("execution(* com.example.service.*.*(..))")
       public void logBeforeMethodExecution(JoinPoint joinPoint) {
           // Logging logic
       }
   }
   ```

3. **Template pattern implementations**: Spring provides templates (JdbcTemplate, RestTemplate, etc.) that handle infrastructure concerns, allowing your code to focus on business logic:
   ```
   @Repository
   public class JdbcCustomerRepository implements CustomerRepository {
       private final JdbcTemplate jdbcTemplate;
       
       public JdbcCustomerRepository(JdbcTemplate jdbcTemplate) {
           this.jdbcTemplate = jdbcTemplate;
       }
       
       @Override
       public Customer findById(Long id) {
           // Only need to focus on the query, not connection handling
           return jdbcTemplate.queryForObject(
               "SELECT * FROM customers WHERE id = ?",
               new CustomerRowMapper(), id);
       }
   }
   ```

4. **Separation of configuration from business logic**: Spring Boot's externalized configuration allows application parameters to be managed separately from code:
   ```
   @ConfigurationProperties(prefix = "app.email")
   public class EmailProperties {
       private String host;
       private int port;
       private String username;
       // getters and setters
   }
   
   @Service
   public class EmailService {
       private final EmailProperties properties;
       
       public EmailService(EmailProperties properties) {
           this.properties = properties;
       }
       
       // Email sending logic
   }
   ```

By leveraging these Spring features, developers can create classes with single, well-defined responsibilities, making the codebase more maintainable, testable, and adaptable to change.

### Q2: Explain how Dependency Injection in Spring supports the Open/Closed Principle (OCP).

**Answer:** The Open/Closed Principle states that software entities should be open for extension but closed for modification. Spring's Dependency Injection (DI) supports this principle in several ways:

1. **Interface-based design**: Spring encourages programming to interfaces, allowing implementations to be swapped without modifying client code:

   ```
   // Client code depends on interface, not implementation
   @Service
   public class OrderService {
       private final PaymentProcessor paymentProcessor;
       
       public OrderService(PaymentProcessor paymentProcessor) {
           this.paymentProcessor = paymentProcessor;
       }
       
       public void processOrder(Order order) {
           // Uses payment processor through its interface
           paymentProcessor.processPayment(order.getPaymentDetails());
       }
   }
   
   // Different implementations can be injected
   @Component
   public class CreditCardProcessor implements PaymentProcessor {
       @Override
       public void processPayment(PaymentDetails details) {
           // Credit card specific logic
       }
   }
   
   @Component
   public class PayPalProcessor implements PaymentProcessor {
       @Override
       public void processPayment(PaymentDetails details) {
           // PayPal specific logic
       }
   }
   ```

2. **Configuration-based extension**: Spring allows new behavior to be added through configuration rather than modifying existing code:

   ```
   @Configuration
   public class PaymentConfig {
       @Bean
       @ConditionalOnProperty(name = "payment.gateway", havingValue = "stripe")
       public PaymentProcessor stripePaymentProcessor() {
           return new StripePaymentProcessor();
       }
       
       @Bean
       @ConditionalOnProperty(name = "payment.gateway", havingValue = "paypal")
       public PaymentProcessor paypalPaymentProcessor() {
           return new PayPalPaymentProcessor();
       }
       
       @Bean
       @ConditionalOnMissingBean
       public PaymentProcessor defaultPaymentProcessor() {
           return new DefaultPaymentProcessor();
       }
   }
   ```

3. **Profiles and conditional beans**: Spring allows different implementations to be activated based on environment or conditions:

   ```
   @Component
   @Profile("development")
   public class MockEmailService implements EmailService {
       // Mock implementation for development
   }
   
   @Component
   @Profile("production")
   public class SmtpEmailService implements EmailService {
       // Real implementation for production
   }
   ```

4. **AOP for extending behavior**: Spring AOP allows adding behavior to existing methods without modifying them:

   ```
   @Aspect
   @Component
   public class PerformanceMonitoringAspect {
       @Around("execution(* com.example.service.*.*(..))")
       public Object monitorPerformance(ProceedingJoinPoint joinPoint) throws Throwable {
           long start = System.currentTimeMillis();
           Object result = joinPoint.proceed();
           long executionTime = System.currentTimeMillis() - start;
           // Log or report execution time
           return result;
       }
   }
   ```

By leveraging these Spring features, developers can extend application behavior without modifying existing code, adhering to the Open/Closed Principle.

### Q3: How does Spring's approach to exception handling support the Liskov Substitution Principle (LSP)?

**Answer:** The Liskov Substitution Principle (LSP) states that objects of a superclass should be replaceable with objects of a subclass without affecting the correctness of the program. Spring's approach to exception handling supports LSP in several ways:

1. **Exception translation**: Spring's exception translation mechanism converts low-level exceptions to higher-level, more meaningful exceptions without breaking the substitution principle:

   ```
   // Spring's @Repository automatically translates persistence exceptions
   @Repository
   public class JpaUserRepository implements UserRepository {
       @Override
       public User findByUsername(String username) {
           // If a JPA exception occurs, Spring translates it to a DataAccessException
           // Client code doesn't need to change when the repository implementation changes
           return entityManager.createQuery("...", User.class)
               .setParameter("username", username)
               .getSingleResult();
       }
   }
   ```

2. **Consistent exception hierarchies**: Spring provides consistent exception hierarchies that allow substituting implementations without changing error handling:

   ```
   // Client code handles DataAccessException
   @Service
   public class UserService {
       private final UserRepository userRepository;
       
       public UserService(UserRepository userRepository) {
           this.userRepository = userRepository;
       }
       
       public User getUser(String username) {
           try {
               return userRepository.findByUsername(username);
           } catch (DataAccessException e) {
               // Handle data access exception consistently
               // Works with any repository implementation
               throw new ServiceException("Error retrieving user", e);
           }
       }
   }
   ```

3. **@ExceptionHandler and @ControllerAdvice**: Spring MVC's exception handling allows consistent error responses regardless of the specific controller implementation:

   ```
   @ControllerAdvice
   public class GlobalExceptionHandler {
       @ExceptionHandler(ResourceNotFoundException.class)
       public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
           // Consistent handling of ResourceNotFoundException
           // Works with any controller that might throw this exception
           return ResponseEntity.status(HttpStatus.NOT_FOUND)
               .body(new ErrorResponse(ex.getMessage()));
       }
   }
   ```

4. **Unchecked exceptions approach**: Spring generally favors unchecked exceptions, which don't force implementation details into method signatures, making substitution easier:

   ```
   // Interface doesn't expose implementation-specific exceptions
   public interface UserRepository {
       User findByUsername(String username);
   }
   
   // Implementations can throw different exceptions internally
   // but they get translated to common Spring exceptions
   @Repository
   public class JdbcUserRepository implements UserRepository {
       // Can throw JDBC exceptions internally
   }
   
   @Repository
   public class JpaUserRepository implements UserRepository {
       // Can throw JPA exceptions internally
   }
   ```

By using these approaches, Spring ensures that different implementations can be substituted without requiring changes to exception handling code, adhering to the Liskov Substitution Principle.

### Q4: How does Spring's bean container support the Interface Segregation Principle (ISP)?

**Answer:** The Interface Segregation Principle (ISP) states that clients should not be forced to depend on interfaces they don't use. Spring's bean container supports ISP in several ways:

1. **Fine-grained interfaces**: Spring encourages the use of role-specific interfaces rather than large, general-purpose ones:

   ```
   // Instead of one large service interface
   public interface UserService {
       User findById(Long id);
       void createUser(User user);
       void updateUser(User user);
       void deleteUser(Long id);
       void sendPasswordResetEmail(String email);
       boolean verifyCredentials(String username, String password);
       void lockAccount(Long userId);
       void unlockAccount(Long userId);
   }
   
   // Spring encourages smaller, focused interfaces
   public interface UserQueryService {
       User findById(Long id);
       User findByUsername(String username);
   }
   
   public interface UserManagementService {
       void createUser(User user);
       void updateUser(User user);
       void deleteUser(Long id);
   }
   
   public interface UserSecurityService {
       boolean verifyCredentials(String username, String password);
       void lockAccount(Long userId);
       void unlockAccount(Long userId);
   }
   
   public interface NotificationService {
       void sendPasswordResetEmail(String email);
   }
   ```

2. **Qualifier annotations**: Spring allows beans to be qualified, enabling clients to depend only on the specific implementation they need:

   ```
   @Service
   @Qualifier("email")
   public class EmailNotificationService implements NotificationService { ... }
   
   @Service
   @Qualifier("sms")
   public class SmsNotificationService implements NotificationService { ... }
   
   // Client only depends on the specific implementation it needs
   @Service
   public class PasswordResetService {
       private final NotificationService notificationService;
       
       public PasswordResetService(@Qualifier("email") NotificationService notificationService) {
           this.notificationService = notificationService;
       }
   }
   ```

3. **Method injection**: Spring supports method injection, allowing dependencies to be injected only where needed:

   ```
   @Component
   public abstract class ReportGenerator {
       // Only inject the dependency when the method is called
       @Lookup
       protected abstract PdfGenerator pdfGenerator();
       
       public void generateReport(ReportData data) {
           if (data.getFormat() == ReportFormat.PDF) {
               PdfGenerator generator = pdfGenerator();
               generator.generate(data);
           } else {
               // Other format handling
           }
       }
   }
   ```

4. **Spring Boot starters**: Spring Boot's starter dependencies are organized around specific functionalities, allowing applications to include only what they need:

   ```
   // Include only web functionality
   implementation 'org.springframework.boot:spring-boot-starter-web'
   
   // Include only data JPA functionality
   implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
   
   // Include only security functionality
   implementation 'org.springframework.boot:spring-boot-starter-security'
   ```

By using these approaches, Spring helps developers create and use interfaces that are tailored to specific client needs, adhering to the Interface Segregation Principle.

### Q5: How does Spring's configuration model support the Dependency Inversion Principle (DIP)?

**Answer:** The Dependency Inversion Principle (DIP) states that high-level modules should not depend on low-level modules; both should depend on abstractions. Spring's configuration model supports DIP in several ways:

1. **Dependency injection**: Spring's core DI mechanism allows high-level components to depend on abstractions rather than concrete implementations:

   ```
   // High-level component depends on abstraction
   @Service
   public class OrderService {
       private final PaymentGateway paymentGateway;
       
       public OrderService(PaymentGateway paymentGateway) {
           this.paymentGateway = paymentGateway;
       }
       
       public void processOrder(Order order) {
           // Uses payment gateway through its interface
           paymentGateway.processPayment(order.getAmount(), order.getPaymentDetails());
       }
   }
   
   // Low-level component implements the abstraction
   @Component
   public class StripePaymentGateway implements PaymentGateway {
       @Override
       public void processPayment(BigDecimal amount, PaymentDetails details) {
           // Stripe-specific implementation
       }
   }
   ```

2. **Java-based configuration**: Spring's `@Configuration` classes allow wiring dependencies without modifying the components themselves:

   ```
   @Configuration
   public class ApplicationConfig {
       @Bean
       public PaymentGateway paymentGateway(PaymentGatewayProperties properties) {
           if ("stripe".equals(properties.getProvider())) {
               return new StripePaymentGateway(properties.getApiKey());
           } else if ("paypal".equals(properties.getProvider())) {
               return new PayPalPaymentGateway(properties.getClientId(), properties.getClientSecret());
           } else {
               return new MockPaymentGateway();
           }
       }
       
       @Bean
       public OrderService orderService(PaymentGateway paymentGateway) {
           return new OrderService(paymentGateway);
       }
   }
   ```

3. **Component scanning**: Spring's component scanning allows implementations to be discovered and wired automatically:

   ```
   @SpringBootApplication
   @ComponentScan("com.example")
   public class Application {
       public static void main(String[] args) {
           SpringApplication.run(Application.class, args);
       }
   }
   ```

4. **Externalized configuration**: Spring Boot's property-based configuration allows implementation details to be determined at runtime:

   ```
   # application.properties
   payment.gateway.provider=stripe
   payment.gateway.api-key=${STRIPE_API_KEY}
   
   # Configuration properties class
   @ConfigurationProperties(prefix = "payment.gateway")
   public class PaymentGatewayProperties {
       private String provider;
       private String apiKey;
       private String clientId;
       private String clientSecret;
       // getters and setters
   }
   ```

5. **Factory beans**: Spring's factory beans allow complex instantiation logic to be encapsulated:

   ```
   @Component
   public class PaymentGatewayFactoryBean implements FactoryBean<PaymentGateway> {
       private final PaymentGatewayProperties properties;
       
       public PaymentGatewayFactoryBean(PaymentGatewayProperties properties) {
           this.properties = properties;
       }
       
       @Override
       public PaymentGateway getObject() throws Exception {
           // Factory logic to create the appropriate implementation
           if ("stripe".equals(properties.getProvider())) {
               return new StripePaymentGateway(properties.getApiKey());
           } else if ("paypal".equals(properties.getProvider())) {
               return new PayPalPaymentGateway(properties.getClientId(), properties.getClientSecret());
           } else {
               return new MockPaymentGateway();
           }
       }
       
       @Override
       public Class<?> getObjectType() {
           return PaymentGateway.class;
       }
   }
   ```

By using these approaches, Spring ensures that components depend on abstractions rather than concrete implementations, adhering to the Dependency Inversion Principle.

## REST API Design

### Q6: What are the best practices for designing RESTful APIs in Spring Boot? Explain with examples.

**Answer:** Best practices for designing RESTful APIs in Spring Boot include:

1. **Use appropriate HTTP methods**:
   ```
   // GET for retrieving resources
   @GetMapping("/users/{id}")
   public ResponseEntity<UserDTO> getUser(@PathVariable Long id) { ... }
   
   // POST for creating resources
   @PostMapping("/users")
   public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) { ... }
   
   // PUT for full updates
   @PutMapping("/users/{id}")
   public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) { ... }
   
   // PATCH for partial updates
   @PatchMapping("/users/{id}")
   public ResponseEntity<UserDTO> partialUpdateUser(@PathVariable Long id, @RequestBody Map<String, Object> updates) { ... }
   
   // DELETE for removing resources
   @DeleteMapping("/users/{id}")
   public ResponseEntity<Void> deleteUser(@PathVariable Long id) { ... }
   ```

2. **Use meaningful HTTP status codes**:
   ```
   // 200 OK for successful operations
   return ResponseEntity.ok(userDTO);
   
   // 201 Created for resource creation
   URI location = ServletUriComponentsBuilder
       .fromCurrentRequest()
       .path("/{id}")
       .buildAndExpand(createdUser.getId())
       .toUri();
   return ResponseEntity.created(location).body(createdUser);
   
   // 204 No Content for operations without response body
   return ResponseEntity.noContent().build();
   
   // 400 Bad Request for client errors
   return ResponseEntity.badRequest().body(new ErrorResponse("Invalid input"));
   
   // 404 Not Found for missing resources
   return ResponseEntity.notFound().build();
   
   // 409 Conflict for resource conflicts
   return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse("User already exists"));
   ```

3. **Implement proper error handling**:
   ```
   @RestControllerAdvice
   public class GlobalExceptionHandler {
       @ExceptionHandler(ResourceNotFoundException.class)
       public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
           ErrorResponse error = new ErrorResponse(
               HttpStatus.NOT_FOUND.value(),
               ex.getMessage(),
               LocalDateTime.now()
           );
           return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
       }
       
       @ExceptionHandler(MethodArgumentNotValidException.class)
       public ResponseEntity<ValidationErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
           Map<String, String> errors = new HashMap<>();
           ex.getBindingResult().getFieldErrors().forEach(error -> 
               errors.put(error.getField(), error.getDefaultMessage()));
           
           ValidationErrorResponse response = new ValidationErrorResponse(
               HttpStatus.BAD_REQUEST.value(),
               "Validation failed",
               errors
           );
           return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
       }
   }
   ```

4. **Use DTOs to decouple API from domain model**:
   ```
   // DTO for API
   public class UserDTO {
       private Long id;
       private String username;
       private String email;
       // No sensitive fields like password
       // getters and setters
   }
   
   // Domain entity
   @Entity
   public class User {
       @Id
       @GeneratedValue(strategy = GenerationType.IDENTITY)
       private Long id;
       private String username;
       private String email;
       private String passwordHash;
       private boolean enabled;
       // getters and setters
   }
   
   // Mapper to convert between entity and DTO
   @Component
   public class UserMapper {
       public UserDTO toDTO(User user) {
           UserDTO dto = new UserDTO();
           dto.setId(user.getId());
           dto.setUsername(user.getUsername());
           dto.setEmail(user.getEmail());
           return dto;
       }
       
       public User toEntity(UserDTO dto) {
           User user = new User();
           user.setUsername(dto.getUsername());
           user.setEmail(dto.getEmail());
           return user;
       }
   }
   ```

5. **Implement pagination for collections**:
   ```
   @GetMapping("/users")
   public ResponseEntity<Page<UserDTO>> getAllUsers(
           @RequestParam(defaultValue = "0") int page,
           @RequestParam(defaultValue = "20") int size,
           @RequestParam(defaultValue = "id") String sortBy) {
       
       Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
       Page<User> userPage = userRepository.findAll(pageable);
       Page<UserDTO> userDTOPage = userPage.map(userMapper::toDTO);
       
       return ResponseEntity.ok(userDTOPage);
   }
   ```

6. **Use HATEOAS for discoverability**:
   ```
   @GetMapping("/users/{id}")
   public EntityModel<UserDTO> getUser(@PathVariable Long id) {
       UserDTO user = userService.findById(id);
       
       return EntityModel.of(user,
           linkTo(methodOn(UserController.class).getUser(id)).withSelfRel(),
           linkTo(methodOn(UserController.class).getAllUsers(0, 20, "id")).withRel("users"),
           linkTo(methodOn(OrderController.class).getOrdersForUser(id)).withRel("orders")
       );
   }
   ```

7. **Implement proper validation**:
   ```
   @PostMapping("/users")
   public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userDTO) {
       UserDTO createdUser = userService.createUser(userDTO);
       URI location = ServletUriComponentsBuilder
           .fromCurrentRequest()
           .path("/{id}")
           .buildAndExpand(createdUser.getId())
           .toUri();
       return ResponseEntity.created(location).body(createdUser);
   }
   
   public class UserDTO {
       private Long id;
       
       @NotBlank(message = "Username is required")
       @Size(min = 4, max = 50, message = "Username must be between 4 and 50 characters")
       private String username;
       
       @NotBlank(message = "Email is required")
       @Email(message = "Email should be valid")
       private String email;
       
       // getters and setters
   }
   ```

8. **Use versioning when necessary**:
   ```
   // URI versioning
   @RestController
   @RequestMapping("/api/v1/users")
   public class UserControllerV1 { ... }
   
   @RestController
   @RequestMapping("/api/v2/users")
   public class UserControllerV2 { ... }
   
   // Header versioning
   @RestController
   @RequestMapping("/api/users")
   public class UserController {
       @GetMapping(headers = "X-API-VERSION=1")
       public ResponseEntity<UserDTOV1> getUserV1(@PathVariable Long id) { ... }
       
       @GetMapping(headers = "X-API-VERSION=2")
       public ResponseEntity<UserDTOV2> getUserV2(@PathVariable Long id) { ... }
   }
   ```

By following these best practices, you can create RESTful APIs that are intuitive, maintainable, and adhere to REST principles.

### Q7: What strategies would you use to version a REST API in Spring Boot? What are the trade-offs of each approach?

**Answer:** There are several strategies for versioning REST APIs in Spring Boot, each with its own trade-offs:

1. **URI Path Versioning**:
   
   Implementation:
   ```
   @RestController
   @RequestMapping("/api/v1/users")
   public class UserControllerV1 {
       @GetMapping("/{id}")
       public UserDTOV1 getUserV1(@PathVariable Long id) { ... }
   }
   
   @RestController
   @RequestMapping("/api/v2/users")
   public class UserControllerV2 {
       @GetMapping("/{id}")
       public UserDTOV2 getUserV2(@PathVariable Long id) { ... }
   }
   ```
   
   Pros:
   - Simple and explicit
   - Easy to understand and document
   - Works with all clients and proxies
   - Supports browser caching
   
   Cons:
   - Breaks URI uniqueness principle (same resource, different URIs)
   - Can lead to URI proliferation
   - Requires changing the URI for each version

2. **Request Parameter Versioning**:
   
   Implementation:
   ```
   @RestController
   @RequestMapping("/api/users")
   public class UserController {
       @GetMapping(value = "/{id}", params = "version=1")
       public UserDTOV1 getUserV1(@PathVariable Long id) { ... }
       
       @GetMapping(value = "/{id}", params = "version=2")
       public UserDTOV2 getUserV2(@PathVariable Long id) { ... }
   }
   ```
   
   Pros:
   - Maintains URI consistency
   - Simple to implement
   - Works with most clients
   
   Cons:
   - Clutters the URI
   - Less ideal for caching
   - Can be overlooked in documentation

3. **HTTP Header Versioning**:
   
   Implementation:
   ```
   @RestController
   @RequestMapping("/api/users")
   public class UserController {
       @GetMapping(value = "/{id}", headers = "X-API-Version=1")
       public UserDTOV1 getUserV1(@PathVariable Long id) { ... }
       
       @GetMapping(value = "/{id}", headers = "X-API-Version=2")
       public UserDTOV2 getUserV2(@PathVariable Long id) { ... }
   }
   ```
   
   Pros:
   - Maintains URI consistency
   - Cleaner URIs
   - Follows HTTP specification for content negotiation
   
   Cons:
   - Less visible in documentation
   - Harder to test with browsers
   - May be stripped by proxies
   - Requires custom headers

4. **Media Type Versioning (Content Negotiation)**:
   
   Implementation:
   ```
   @RestController
   @RequestMapping("/api/users")
   public class UserController {
       @GetMapping(value = "/{id}", 
                  produces = "application/vnd.company.app-v1+json")
       public UserDTOV1 getUserV1(@PathVariable Long id) { ... }
       
       @GetMapping(value = "/{id}", 
                  produces = "application/vnd.company.app-v2+json")
       public UserDTOV2 getUserV2(@PathVariable Long id) { ... }
   }
   ```
   
   Client usage:
   ```
   // HTTP request header
   Accept: application/vnd.company.app-v2+json
   ```
   
   Pros:
   - Maintains URI consistency
   - Follows HTTP content negotiation standards
   - Allows for different representations of the same resource
   
   Cons:
   - More complex to implement and use
   - Less intuitive for API consumers
   - Harder to test with browsers
   - May require custom media types

5. **Hybrid Approach**:
   
   Implementation:
   ```
   // Major versions in URI, minor versions in header
   @RestController
   @RequestMapping("/api/v1/users")
   public class UserControllerV1 {
       @GetMapping(value = "/{id}", headers = "X-API-Minor-Version=1")
       public UserDTOV1_1 getUserV1_1(@PathVariable Long id) { ... }
       
       @GetMapping(value = "/{id}", headers = "X-API-Minor-Version=2")
       public UserDTOV1_2 getUserV1_2(@PathVariable Long id) { ... }
   }
   ```
   
   Pros:
   - Balances visibility and flexibility
   - Can distinguish between breaking and non-breaking changes
   
   Cons:
   - More complex to manage
   - Potentially confusing for API consumers

**Recommended Approach**:
The best approach depends on your specific requirements, but URI path versioning is often recommended for its simplicity and explicitness, especially for public APIs. For internal APIs or microservices, header or media type versioning might be more appropriate.

**Implementation Considerations**:

1. **Version your DTOs, not your entities**:
   ```
   // Version 1 DTO
   public class UserDTOV1 {
       private Long id;
       private String fullName;  // Combined name
   }
   
   // Version 2 DTO
   public class UserDTOV2 {
       private Long id;
       private String firstName;  // Split name
       private String lastName;
   }
   
   // Entity (unchanged)
   @Entity
   public class User {
       @Id
       private Long id;
       private String firstName;
       private String lastName;
   }
   ```

2. **Use mappers to