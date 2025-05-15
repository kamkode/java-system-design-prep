# Spring Framework and Spring Boot Testing Questions

This document contains interview questions related to testing in Spring Framework and Spring Boot, suitable for candidates with 3-5 years of experience. These questions focus on mocking beans, using testing annotations like @WebMvcTest, @MockBean, @DataJpaTest, and implementing effective test strategies.

## Table of Contents
1. [Testing Fundamentals in Spring](#testing-fundamentals-in-spring)
2. [Unit Testing with Spring](#unit-testing-with-spring)
3. [Integration Testing](#integration-testing)
4. [Web Layer Testing](#web-layer-testing)
5. [Data Access Layer Testing](#data-access-layer-testing)
6. [Mocking Strategies](#mocking-strategies)
7. [Test Configuration](#test-configuration)
8. [Testing Best Practices](#testing-best-practices)

## Testing Fundamentals in Spring

### Q1: What are the different types of tests you can write for a Spring Boot application and when would you use each?

**Answer:** Spring Boot applications can be tested at different levels, each serving a specific purpose:

1. **Unit Tests**:
   - **Purpose**: Test individual components in isolation
   - **Scope**: Single class or method
   - **Dependencies**: Mocked or stubbed
   - **Speed**: Very fast
   - **When to use**: For testing business logic, algorithms, and individual methods
   - **Example**:
     ```java
     @ExtendWith(MockitoExtension.class)
     public class UserServiceTest {
         @Mock
         private UserRepository userRepository;
         
         @InjectMocks
         private UserServiceImpl userService;
         
         @Test
         void shouldReturnUserWhenUserExists() {
             // Arrange
             User expectedUser = new User(1L, "john@example.com");
             when(userRepository.findById(1L)).thenReturn(Optional.of(expectedUser));
             
             // Act
             User actualUser = userService.getUserById(1L);
             
             // Assert
             assertEquals(expectedUser, actualUser);
         }
     }
     ```

2. **Integration Tests**:
   - **Purpose**: Test interaction between components
   - **Scope**: Multiple components working together
   - **Dependencies**: Real or test doubles
   - **Speed**: Moderate
   - **When to use**: For testing how components interact with each other
   - **Example**:
     ```java
     @SpringBootTest
     public class UserServiceIntegrationTest {
         @Autowired
         private UserService userService;
         
         @Autowired
         private UserRepository userRepository;
         
         @BeforeEach
         void setup() {
             userRepository.deleteAll();
         }
         
         @Test
         void shouldSaveAndRetrieveUser() {
             // Arrange
             User user = new User(null, "john@example.com");
             
             // Act
             User savedUser = userService.createUser(user);
             User retrievedUser = userService.getUserById(savedUser.getId());
             
             // Assert
             assertNotNull(savedUser.getId());
             assertEquals(user.getEmail(), retrievedUser.getEmail());
         }
     }
     ```

3. **Web Layer Tests**:
   - **Purpose**: Test controllers and REST endpoints
   - **Scope**: Web layer only
   - **Dependencies**: Mocked service layer
   - **Speed**: Fast
   - **When to use**: For testing API contracts, request/response handling, and validation
   - **Example**:
     ```java
     @WebMvcTest(UserController.class)
     public class UserControllerTest {
         @Autowired
         private MockMvc mockMvc;
         
         @MockBean
         private UserService userService;
         
         @Test
         void shouldReturnUserWhenGetUserById() throws Exception {
             // Arrange
             User user = new User(1L, "john@example.com");
             when(userService.getUserById(1L)).thenReturn(user);
             
             // Act & Assert
             mockMvc.perform(get("/api/users/1")
                     .contentType(MediaType.APPLICATION_JSON))
                 .andExpect(status().isOk())
                 .andExpect(jsonPath("$.id").value(1))
                 .andExpect(jsonPath("$.email").value("john@example.com"));
         }
     }
     ```

4. **Data Layer Tests**:
   - **Purpose**: Test repository and database interactions
   - **Scope**: Data access layer
   - **Dependencies**: Test database (in-memory or containerized)
   - **Speed**: Moderate
   - **When to use**: For testing database queries, mappings, and transactions
   - **Example**:
     ```java
     @DataJpaTest
     public class UserRepositoryTest {
         @Autowired
         private UserRepository userRepository;
         
         @Test
         void shouldFindUserByEmail() {
             // Arrange
             User user = new User(null, "john@example.com");
             userRepository.save(user);
             
             // Act
             Optional<User> foundUser = userRepository.findByEmail("john@example.com");
             
             // Assert
             assertTrue(foundUser.isPresent());
             assertEquals(user.getEmail(), foundUser.get().getEmail());
         }
     }
     ```

5. **End-to-End Tests**:
   - **Purpose**: Test the entire application flow
   - **Scope**: Full application
   - **Dependencies**: Real dependencies (or containerized)
   - **Speed**: Slow
   - **When to use**: For testing critical user journeys and full system behavior
   - **Example**:
     ```java
     @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
     public class UserRegistrationE2ETest {
         @Autowired
         private TestRestTemplate restTemplate;
         
         @Autowired
         private UserRepository userRepository;
         
         @BeforeEach
         void setup() {
             userRepository.deleteAll();
         }
         
         @Test
         void shouldRegisterUserAndLogin() {
             // Register user
             UserRegistrationRequest registrationRequest = new UserRegistrationRequest("john@example.com", "password");
             ResponseEntity<UserResponse> registrationResponse = restTemplate.postForEntity(
                 "/api/users/register", registrationRequest, UserResponse.class);
             
             assertEquals(HttpStatus.CREATED, registrationResponse.getStatusCode());
             
             // Login
             LoginRequest loginRequest = new LoginRequest("john@example.com", "password");
             ResponseEntity<TokenResponse> loginResponse = restTemplate.postForEntity(
                 "/api/auth/login", loginRequest, TokenResponse.class);
             
             assertEquals(HttpStatus.OK, loginResponse.getStatusCode());
             assertNotNull(loginResponse.getBody().getToken());
         }
     }
     ```

6. **Slice Tests**:
   - **Purpose**: Test specific slices of the application
   - **Scope**: Specific layer or technology
   - **Dependencies**: Depends on the slice
   - **Speed**: Fast to moderate
   - **When to use**: For focused testing of specific parts of the application
   - **Examples**:
     - `@WebMvcTest` - For testing MVC controllers
     - `@DataJpaTest` - For testing JPA repositories
     - `@JsonTest` - For testing JSON serialization/deserialization
     - `@RestClientTest` - For testing REST clients
     - `@WebFluxTest` - For testing WebFlux controllers

7. **Contract Tests**:
   - **Purpose**: Verify that API contracts are maintained
   - **Scope**: API boundaries
   - **Dependencies**: Contract definition
   - **Speed**: Fast
   - **When to use**: For ensuring API compatibility between services
   - **Example**:
     ```java
     @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
     @AutoConfigureMessageVerifier
     public class UserServiceContractTest {
         @Autowired
         private UserService userService;
         
         @Test
         @PactTestFor(pactMethod = "createUserPact")
         void shouldCreateUser() {
             // Test implementation using Spring Cloud Contract or Pact
         }
         
         @Pact(consumer = "user-service")
         public RequestResponsePact createUserPact(PactDslWithProvider builder) {
             // Define the contract
         }
     }
     ```

**Testing Pyramid Strategy**:

A well-balanced testing strategy follows the testing pyramid:
- Many unit tests at the base (fast, focused)
- Fewer integration tests in the middle (validate component interactions)
- Few end-to-end tests at the top (validate critical paths)

This approach provides:
- Fast feedback during development (unit tests)
- Confidence in component interactions (integration tests)
- Validation of critical user journeys (end-to-end tests)

By using the appropriate test type for each scenario, you can build a comprehensive test suite that provides confidence in your application while maintaining reasonable test execution times.

### Q2: Explain the difference between @SpringBootTest, @WebMvcTest, and @DataJpaTest annotations.

**Answer:** These annotations are part of Spring Boot's testing support and are used to configure different types of tests:

1. **@SpringBootTest**:
   
   **Purpose**: Loads the full application context for integration testing.
   
   **Key Features**:
   - Loads the complete Spring application context
   - Can start an embedded server with `webEnvironment` property
   - Provides access to all beans in the application
   - Can use `@MockBean` to replace specific beans with mocks
   
   **Configuration Options**:
   ```java
   // Basic usage
   @SpringBootTest
   public class FullApplicationTest { ... }
   
   // With web environment configuration
   @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
   public class WebIntegrationTest { ... }
   
   // With specific properties
   @SpringBootTest(properties = {"spring.datasource.url=jdbc:h2:mem:testdb"})
   public class CustomPropertiesTest { ... }
   
   // With specific configuration classes
   @SpringBootTest(classes = {AppConfig.class, TestConfig.class})
   public class SpecificConfigTest { ... }
   ```
   
   **When to Use**:
   - For integration tests that require most or all of the application context
   - When testing interactions between multiple components
   - For end-to-end testing with a running server
   
   **Drawbacks**:
   - Slower test startup due to loading the full context
   - Tests may be less focused due to the broad scope

2. **@WebMvcTest**:
   
   **Purpose**: Tests only the web layer (controllers) without starting a full server.
   
   **Key Features**:
   - Loads only web-related components (controllers, filters, etc.)
   - Provides `MockMvc` for simulating HTTP requests
   - Does not load service or repository beans by default
   - Requires `@MockBean` for dependencies of controllers
   
   **Configuration Options**:
   ```java
   // Test a specific controller
   @WebMvcTest(UserController.class)
   public class UserControllerTest {
       @Autowired
       private MockMvc mockMvc;
       
       @MockBean
       private UserService userService;
       
       @Test
       void shouldGetUser() throws Exception {
           when(userService.getUser(1L)).thenReturn(new User(1L, "John"));
           
           mockMvc.perform(get("/api/users/1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.name").value("John"));
       }
   }
   
   // Test multiple controllers
   @WebMvcTest({UserController.class, OrderController.class})
   public class MultipleControllersTest { ... }
   
   // Include additional components
   @WebMvcTest(controllers = UserController.class, includeFilters = {
       @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = GlobalExceptionHandler.class)
   })
   public class UserControllerWithExceptionHandlerTest { ... }
   ```
   
   **When to Use**:
   - For testing controller logic, request mapping, and response handling
   - When testing input validation and error handling
   - For testing controller-specific security rules
   
   **Drawbacks**:
   - Limited to web layer components
   - Requires mocking all service dependencies

3. **@DataJpaTest**:
   
   **Purpose**: Tests JPA repositories with an in-memory database.
   
   **Key Features**:
   - Configures an in-memory database
   - Scans for `@Entity` classes and JPA repositories
   - Configures Hibernate, Spring Data, and DataSource
   - Enables transaction rollback after each test
   
   **Configuration Options**:
   ```java
   // Basic usage
   @DataJpaTest
   public class UserRepositoryTest {
       @Autowired
       private UserRepository userRepository;
       
       @Test
       void shouldFindByEmail() {
           // Given
           User user = new User("john@example.com", "John");
           userRepository.save(user);
           
           // When
           User found = userRepository.findByEmail("john@example.com").orElse(null);
           
           // Then
           assertNotNull(found);
           assertEquals("John", found.getName());
       }
   }
   
   // With test properties
   @DataJpaTest
   @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
   @TestPropertySource(properties = {
       "spring.datasource.url=jdbc:postgresql://localhost:5432/test"
   })
   public class RepositoryTestWithRealDatabase { ... }
   ```
   
   **When to Use**:
   - For testing repository queries and methods
   - When testing entity mappings and relationships
   - For testing database constraints and validations
   
   **Drawbacks**:
   - Limited to JPA components
   - May not catch issues with real database dialects if using H2

**Key Differences**:

| Feature | @SpringBootTest | @WebMvcTest | @DataJpaTest |
|---------|----------------|-------------|--------------|
| Context Loading | Full application context | Web layer only | JPA components only |
| Test Scope | Integration/E2E | Controller layer | Repository layer |
| Database | Configurable (real or in-memory) | None by default | In-memory by default |
| Server | Optional embedded server | MockMvc (no server) | None |
| Speed | Slowest | Fast | Moderate |
| Dependencies | All application beans | Web components only | JPA components only |

**Choosing the Right Annotation**:

- Use **@SpringBootTest** when you need to test the interaction between multiple components or need a fully configured application context.
- Use **@WebMvcTest** when you want to test controller behavior, request/response handling, and web-specific configurations.
- Use **@DataJpaTest** when you want to test repository methods, entity mappings, and database interactions.

By selecting the appropriate test annotation, you can optimize your tests for both speed and effectiveness, focusing on the specific layer you want to test while minimizing unnecessary context loading.

### Q3: What is the role of @MockBean and @SpyBean annotations in Spring Boot testing?

**Answer:** `@MockBean` and `@SpyBean` are powerful annotations in Spring Boot testing that help control the behavior of dependencies in your tests:

### @MockBean

**Definition**: `@MockBean` creates a Mockito mock of a bean and adds it to the Spring application context, replacing any existing bean of the same type.

**Key Features**:
1. **Complete Mocking**: The bean's behavior is entirely defined by your test setup
2. **Default Behavior**: Returns default values (null, 0, false, empty collections) for methods unless stubbed
3. **Context Integration**: The mock is added to the Spring context and injected where needed
4. **Reset Between Tests**: Mocks are automatically reset between test methods

**Example Usage**:
```java
@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;
    
    @MockBean
    private UserRepository userRepository;
    
    @Test
    void shouldReturnUserWhenUserExists() {
        // Arrange
        User expectedUser = new User(1L, "john@example.com");
        when(userRepository.findById(1L)).thenReturn(Optional.of(expectedUser));
        
        // Act
        User actualUser = userService.getUserById(1L);
        
        // Assert
        assertEquals(expectedUser, actualUser);
        verify(userRepository).findById(1L);
    }
    
    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> userService.getUserById(1L));
        verify(userRepository).findById(1L);
    }
}
```

**When to Use @MockBean**:
- When you want complete control over the behavior of a dependency
- When the real implementation of a dependency is complex or has external dependencies
- When you want to isolate the component under test from its dependencies
- When testing error scenarios that are difficult to trigger with real implementations

### @SpyBean

**Definition**: `@SpyBean` creates a Mockito spy of a bean and adds it to the Spring application context, replacing any existing bean of the same type.

**Key Features**:
1. **Partial Mocking**: Calls real methods by default but allows stubbing specific methods
2. **Real Implementation**: Uses the actual bean implementation unless a method is explicitly stubbed
3. **Context Integration**: The spy is added to the Spring context and injected where needed
4. **Reset Between Tests**: Spies are automatically reset between test methods

**Example Usage**:
```java
@SpringBootTest
public class EmailServiceTest {
    @Autowired
    private NotificationService notificationService;
    
    @SpyBean
    private EmailService emailService;
    
    @Test
    void shouldSendEmailAndLogSuccess() {
        // Arrange
        String to = "user@example.com";
        String subject = "Test";
        String body = "Test email";
        
        // The real sendEmail method will be called, but we can verify it was called
        doNothing().when(emailService).sendEmail(anyString(), anyString(), anyString());
        
        // Act
        notificationService.notifyUser(to, "Test notification");
        
        // Assert
        verify(emailService).sendEmail(to, subject, body);
        // Other assertions...
    }
    
    @Test
    void shouldHandleEmailFailure() {
        // Arrange
        doThrow(new EmailException("SMTP error"))
            .when(emailService).sendEmail(anyString(), anyString(), anyString());
        
        // Act & Assert
        assertThrows(NotificationException.class, 
            () -> notificationService.notifyUser("user@example.com", "Test"));
    }
}
```

**When to Use @SpyBean**:
- When you want to use the real implementation but need to override or verify specific methods
- When you want to test the integration with a real bean but need to control certain behaviors
- When you want to verify that certain methods were called with specific parameters
- When you need to simulate failures in specific methods while keeping the rest of the behavior intact

### Key Differences Between @MockBean and @SpyBean

| Feature | @MockBean | @SpyBean |
|---------|-----------|----------|
| Default Behavior | Returns null/empty/zero/false | Calls real method |
| Method Stubbing | Must stub to get meaningful returns | Only stub methods you want to override |
| Use Case | Complete isolation from real implementation | Partial mocking with real behavior |
| Performance | Generally faster (no real methods called) | May be slower (calls real methods) |
| Testing Focus | Unit testing with isolation | Integration-like testing with selective mocking |

### Best Practices

1. **Use @MockBean for Unit Tests**:
   ```java
   @WebMvcTest(UserController.class)
   public class UserControllerTest {
       @MockBean
       private UserService userService; // Complete control for unit testing
   }
   ```

2. **Use @SpyBean for Integration Tests with Controlled Behavior**:
   ```java
   @SpringBootTest
   public class NotificationIntegrationTest {
       @SpyBean
       private EmailService emailService; // Real behavior with selective overrides
   }
   ```

3. **Avoid Over-Mocking**:
   - Don't mock everything; consider what really needs to be controlled
   - Use real beans when their behavior is simple and doesn't have external dependencies

4. **Be Explicit About Behavior**:
   ```java
   // For mocks, be explicit about all expected behaviors
   when(userRepository.findById(1L)).thenReturn(Optional.of(user));
   
   // For spies, only override what's necessary
   doNothing().when(emailService).sendEmail(anyString(), anyString(), anyString());
   ```

5. **Reset When Necessary**:
   - Mocks/spies are reset between test methods, but you can reset manually if needed:
   ```java
   @Test
   void complexTest() {
       // First part of test
       when(service.doSomething()).thenReturn("result1");
       // Assertions...
       
       // Reset for second part
       reset(service);
       
       // Second part with different behavior
       when(service.doSomething()).thenReturn("result2");
       // More assertions...
   }
   ```

By understanding the differences between `@MockBean` and `@SpyBean` and applying them appropriately, you can create more effective and maintainable tests that properly isolate the components you're testing while maintaining realistic behavior where needed.

## Unit Testing with Spring

### Q4: How would you write a unit test for a Spring service that uses dependency injection?

**Answer:** Writing unit tests for Spring services with dependency injection involves isolating the service from its dependencies using mocking frameworks like Mockito. Here's a comprehensive approach:

### 1. Basic Service Unit Test Structure

Let's consider a `UserService` that depends on a `UserRepository` and an `EmailService`:

```java
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final EmailService emailService;
    
    @Autowired
    public UserServiceImpl(UserRepository userRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
    }
    
    @Override
    public User registerUser(UserRegistrationRequest request) {
        // Check if user already exists
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("User with email " + request.getEmail() + " already exists");
        }
        
        // Create new user
        User newUser = new User();
        newUser.setEmail(request.getEmail());
        newUser.setName(request.getName());
        newUser.setPasswordHash(hashPassword(request.getPassword()));
        newUser.setCreatedAt(LocalDateTime.now());
        newUser.setActive(true);
        
        // Save user
        User savedUser = userRepository.save(newUser);
        
        // Send welcome email
        emailService.sendWelcomeEmail(savedUser.getEmail(), savedUser.getName());
        
        return savedUser;
    }
    
    private String hashPassword(String password) {
        // Password hashing logic
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
    
    // Other methods...
}
```

### 2. Unit Test Using JUnit 5 and Mockito

```java
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    
    @Mock
    private EmailService emailService;
    
    @InjectMocks
    private UserServiceImpl userService;
    
    private UserRegistrationRequest validRequest;
    
    @BeforeEach
    void setUp() {
        validRequest = new UserRegistrationRequest();
        validRequest.setEmail("john@example.com");
        validRequest.setName("John Doe");
        validRequest.setPassword("securePassword123");
    }
    
    @Test
    void registerUser_WithValidRequest_ShouldCreateAndReturnUser() {
        // Arrange
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            savedUser.setId(1L); // Simulate DB setting ID
            return savedUser;
        });
        doNothing().when(emailService).sendWelcomeEmail(anyString(), anyString());
        
        // Act
        User result = userService.registerUser(validRequest);
        
        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(validRequest.getEmail(), result.getEmail());
        assertEquals(validRequest.getName(), result.getName());
        assertTrue(result.isActive());
        assertNotNull(result.getPasswordHash());
        assertNotNull(result.getCreatedAt());
        
        // Verify interactions
        verify(userRepository).findByEmail(validRequest.getEmail());
        verify(userRepository).save(any(User.class));
        verify(emailService).sendWelcomeEmail(validRequest.getEmail(), validRequest.getName());
    }
    
    @Test
    void registerUser_WithExistingEmail_ShouldThrowException() {
        // Arrange
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setEmail(validRequest.getEmail());
        
        when(userRepository.findByEmail(validRequest.getEmail())).thenReturn(Optional.of(existingUser));
        
        // Act & Assert
        UserAlreadyExistsException exception = assertThrows(
            UserAlreadyExistsException.class,
            () -> userService.registerUser(validRequest)
        );
        
        assertEquals("User with email " + validRequest.getEmail() + " already exists", exception.getMessage());
        
        // Verify interactions
        verify(userRepository).findByEmail(validRequest.getEmail());
        verify(userRepository, never()).save(any(User.class));
        verify(emailService, never()).sendWelcomeEmail(anyString(), anyString());
    }
    
    @Test
    void registerUser_WhenEmailServiceFails_ShouldStillSaveUser() {
        // Arrange
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            savedUser.setId(1L);
            return savedUser;
        });
        doThrow(new RuntimeException("Email service unavailable"))
            .when(emailService).sendWelcomeEmail(anyString(), anyString());
        
        // Act & Assert
        assertThrows(
            RuntimeException.class,
            () -> userService.registerUser(validRequest)
        );
        
        // Verify user was still saved despite email failure
        verify(userRepository).save(any(User.class));
    }
}
```

### 3. Alternative Approach Using Spring's Testing Support

While the above approach is pure unit testing with Mockito, you can also use Spring's testing support:

```java
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(classes = UserServiceImpl.class) // Only load this specific class
class UserServiceSpringTest {

    @MockBean
    private UserRepository userRepository;
    
    @MockBean
    private EmailService emailService;
    
    @Autowired
    private UserService userService;
    
    @Test
    void registerUser_WithValidRequest_ShouldCreateAndReturnUser() {
        // Test implementation similar to above
    }
    
    // Other tests...
}
```

### 4. Testing Services with @Value or Environment Properties

If your service uses properties injection:

```java
@Service
public class ConfigurableUserService {
    private final UserRepository userRepository;
    
    @Value("${user.registration.requireEmailVerification:true}")
    private boolean requireEmailVerification;
    
    // Constructor, methods...
}
```

You can test it with:

```java
@ExtendWith(MockitoExtension.class)
class ConfigurableUserServiceTest {

    @Mock
    private UserRepository userRepository;
    
    private ConfigurableUserService userService;