# Spring Framework and Spring Boot Theoretical Questions

This document contains theoretical interview questions related to Spring Framework and Spring Boot concepts, suitable for candidates with 3-5 years of experience.

## Table of Contents
1. [Core Spring Framework Concepts](#core-spring-framework-concepts)
2. [Dependency Injection and IoC](#dependency-injection-and-ioc)
3. [Spring Bean Lifecycle](#spring-bean-lifecycle)
4. [Aspect-Oriented Programming (AOP)](#aspect-oriented-programming-aop)
5. [Spring Annotations](#spring-annotations)
6. [Spring Boot Fundamentals](#spring-boot-fundamentals)
7. [Spring Boot Auto-Configuration](#spring-boot-auto-configuration)
8. [Spring Boot Starters](#spring-boot-starters)
9. [Spring Boot Properties](#spring-boot-properties)
10. [Spring Boot Actuator](#spring-boot-actuator)

## Core Spring Framework Concepts

### Q1: What is the Spring Framework and what are its core features?
**Answer:** Spring Framework is an open-source application framework for Java that provides comprehensive infrastructure support for developing Java applications. Its core features include:
- Dependency Injection (DI)
- Aspect-Oriented Programming (AOP)
- Spring MVC for web applications
- Transaction management
- JDBC abstraction layer
- Integration with ORM frameworks
- Spring Security
- Spring Batch processing
- Testing support

### Q2: What are the different modules in Spring Framework?
**Answer:** Spring Framework consists of several modules:
- Spring Core (IoC container, DI)
- Spring AOP (Aspect-oriented programming)
- Spring JDBC (Database access)
- Spring ORM (Object-relational mapping)
- Spring Web (Web application development)
- Spring MVC (Model-View-Controller implementation)
- Spring WebFlux (Reactive programming model)
- Spring Test (Testing support)
- Spring Security (Authentication and authorization)
- Spring Data (Data access)
- Spring Integration (Enterprise integration patterns)
- Spring Batch (Batch processing)

### Q3: What is the difference between Spring Framework and Spring Boot?
**Answer:** 
- Spring Framework provides the core features like DI, AOP, etc., but requires significant configuration.
- Spring Boot is built on top of Spring Framework and aims to simplify development by providing:
  - Auto-configuration that automatically configures Spring and third-party libraries
  - Standalone applications with embedded servers
  - Opinionated defaults to reduce boilerplate configuration
  - Production-ready features like metrics, health checks, etc.
  - No code generation and no XML configuration requirement

### Q4: What is the Spring IoC Container?
**Answer:** The Spring IoC (Inversion of Control) Container is the core of Spring Framework that creates, manages, and wires application objects (beans). It's responsible for:
- Instantiating beans
- Configuring beans
- Assembling dependencies between beans
- Managing the entire lifecycle of beans

Spring provides two types of IoC containers:
1. BeanFactory (basic container)
2. ApplicationContext (advanced container with enterprise features)

### Q5: What is the difference between BeanFactory and ApplicationContext?
**Answer:** 
- BeanFactory is the basic container that provides the fundamental functionality:
  - Lazy initialization of beans
  - Basic bean lifecycle management
  - Minimal memory footprint

- ApplicationContext extends BeanFactory and adds:
  - Eager initialization of singleton beans
  - Automatic BeanPostProcessor registration
  - Automatic BeanFactoryPostProcessor registration
  - Convenient access to resources
  - Message source for internationalization
  - Application event publication
  - Integration with AOP

ApplicationContext is preferred for most applications due to its enhanced features.

## Dependency Injection and IoC

### Q6: What is Dependency Injection and what are the different types?
**Answer:** Dependency Injection is a design pattern where the dependencies of a class are "injected" from the outside rather than created by the class itself. This promotes loose coupling and testability.

Types of Dependency Injection in Spring:
1. Constructor Injection: Dependencies are provided through a constructor
2. Setter Injection: Dependencies are provided through setter methods
3. Field Injection: Dependencies are injected directly into fields (using @Autowired)

Constructor injection is generally preferred as it ensures all required dependencies are available at object creation time.

### Q7: What is the difference between constructor and setter injection?
**Answer:**
- Constructor Injection:
  - Dependencies are provided at object creation time
  - Ensures required dependencies are available
  - Promotes immutability
  - Better for mandatory dependencies
  - Helps prevent circular dependencies at runtime

- Setter Injection:
  - Dependencies can be changed after object creation
  - Allows for optional dependencies
  - More flexible but less secure
  - Can resolve circular dependencies
  - Useful when there are many dependencies

### Q8: How does Spring resolve dependencies during autowiring?
**Answer:** Spring resolves dependencies during autowiring through several steps:
1. By Type: Spring looks for beans of the required type
2. By Name: If multiple beans of the same type exist, Spring tries to match by name
3. By Qualifier: @Qualifier annotation can be used to specify which bean to inject
4. By Primary: @Primary annotation can mark a bean as the primary candidate
5. By Profile: Active profiles can determine which beans are available

If Spring cannot resolve a dependency, it throws exceptions like NoSuchBeanDefinitionException or NoUniqueBeanDefinitionException.

### Q9: What is a circular dependency and how can it be resolved in Spring?
**Answer:** A circular dependency occurs when bean A depends on bean B, and bean B depends on bean A (directly or indirectly).

Ways to resolve circular dependencies:
1. Use setter injection instead of constructor injection
2. Use @Lazy annotation to break the cycle
3. Redesign the components to avoid the circular dependency
4. Use a third bean that holds references to both beans
5. Use ApplicationContext directly to get beans programmatically
6. Use @PostConstruct to set dependencies after bean creation

### Q10: What is the @Autowired annotation and what are its different modes?
**Answer:** @Autowired is a Spring annotation used for automatic dependency injection. It can be applied to:
- Constructors (preferred in Spring Boot 2.2+)
- Setter methods
- Fields
- Methods with arbitrary names and multiple arguments

Modes/attributes:
- required (default is true): Indicates whether the dependency is required
- When set to false, Spring will skip the injection if no matching bean is found

## Spring Bean Lifecycle

### Q11: Explain the lifecycle of a Spring Bean.
**Answer:** The lifecycle of a Spring bean includes:

1. Instantiation: Spring creates an instance of the bean
2. Populating Properties: Dependencies are injected
3. BeanNameAware's setBeanName(): If the bean implements BeanNameAware
4. BeanFactoryAware's setBeanFactory(): If the bean implements BeanFactoryAware
5. ApplicationContextAware's setApplicationContext(): If the bean implements ApplicationContextAware
6. Pre-initialization (BeanPostProcessor's postProcessBeforeInitialization())
7. InitializingBean's afterPropertiesSet()
8. Custom init-method
9. Post-initialization (BeanPostProcessor's postProcessAfterInitialization())
10. Bean is ready for use
11. DisposableBean's destroy() when container is shut down
12. Custom destroy-method when container is shut down

### Q12: What are the different bean scopes in Spring?
**Answer:** Spring supports the following bean scopes:
1. singleton (default): One instance per Spring container
2. prototype: New instance each time the bean is requested
3. request: One instance per HTTP request (web-aware contexts)
4. session: One instance per HTTP session (web-aware contexts)
5. application: One instance per ServletContext (web-aware contexts)
6. websocket: One instance per WebSocket session (web-aware contexts)
7. Custom scopes can also be defined

### Q13: What is a BeanPostProcessor and what is it used for?
**Answer:** BeanPostProcessor is an interface that allows for custom modification of new bean instances before and after initialization. It has two methods:
- postProcessBeforeInitialization(): Called before bean initialization methods
- postProcessAfterInitialization(): Called after bean initialization methods

Common uses:
- Checking for marker interfaces
- Wrapping beans with proxies (AOP)
- Configuring default properties
- Implementing custom dependency resolution
- Spring internally uses BeanPostProcessors for features like @Autowired, @Required, and AOP

### Q14: What is a BeanFactoryPostProcessor and how is it different from BeanPostProcessor?
**Answer:** 
- BeanFactoryPostProcessor operates on the bean configuration metadata before any beans are instantiated. It allows modification of bean definitions.
- BeanPostProcessor operates on bean instances after they've been created but before they're fully initialized.

Key differences:
- Timing: BeanFactoryPostProcessor runs earlier in the container lifecycle
- Target: BeanFactoryPostProcessor works with bean definitions, while BeanPostProcessor works with bean instances
- Common BeanFactoryPostProcessors include PropertyPlaceholderConfigurer and PropertySourcesPlaceholderConfigurer

### Q15: How can you control the order of bean initialization in Spring?
**Answer:** Several ways to control bean initialization order:
1. Using @DependsOn annotation to specify dependencies explicitly
2. Implementing the Ordered interface or using @Order annotation on configuration classes
3. Using @Priority annotation
4. Implementing InitializingBean and DisposableBean interfaces
5. Using @PostConstruct and @PreDestroy annotations
6. Specifying init-method and destroy-method in @Bean definitions
7. For BeanPostProcessors, implementing PriorityOrdered interface

## Aspect-Oriented Programming (AOP)

### Q16: What is AOP and what are its key concepts in Spring?
**Answer:** Aspect-Oriented Programming (AOP) is a programming paradigm that increases modularity by allowing the separation of cross-cutting concerns. In Spring AOP:

Key concepts:
- Aspect: A modularization of a concern that cuts across multiple classes (logging, security, etc.)
- Join Point: A point during the execution of a program (method execution, exception handling)
- Advice: Action taken by an aspect at a particular join point (before, after, around)
- Pointcut: A predicate that matches join points
- Introduction: Declaring additional methods or fields on behalf of a type
- Target object: Object being advised by one or more aspects
- AOP proxy: Object created by the AOP framework to implement the aspect contracts
- Weaving: Process of linking aspects with application types or objects

### Q17: What are the different types of advice in Spring AOP?
**Answer:** Spring AOP supports five types of advice:
1. Before Advice: Executes before a join point
2. After Returning Advice: Executes after a join point completes normally
3. After Throwing Advice: Executes if a method exits by throwing an exception
4. After (finally) Advice: Executes regardless of how a join point exits
5. Around Advice: Surrounds a join point such as a method invocation, can perform custom behavior before and after the method execution

### Q18: What is the difference between Spring AOP and AspectJ?
**Answer:**
- Spring AOP:
  - Proxy-based AOP implementation
  - Works only with Spring beans
  - Supports only method execution join points
  - Runtime weaving through proxies
  - Simpler to use but less powerful
  - No additional compilation step required

- AspectJ:
  - Full-featured AOP implementation
  - Works with any POJO, not just Spring beans
  - Supports all join points (method, constructor, field access, etc.)
  - Compile-time, post-compile-time, or load-time weaving
  - More powerful but more complex
  - Requires AspectJ compiler or agent

### Q19: How does Spring AOP use proxies?
**Answer:** Spring AOP creates proxies for target objects to implement aspects:
- For interfaces, Spring uses JDK dynamic proxies by default
- For classes, Spring uses CGLIB proxies
- The proxy intercepts calls to the target and applies advice as needed
- Proxies are created at runtime when the application context is loaded

Limitations:
- Only supports method execution join points
- Proxies only intercept external method calls, not internal calls within the same class
- Final methods and classes cannot be advised with CGLIB proxies

### Q20: What is a pointcut expression in Spring AOP?
**Answer:** A pointcut expression is a predicate that matches join points. Spring AOP uses AspectJ's pointcut expression language.

Common pointcut designators:
- execution(): Matches method execution join points
- within(): Matches join points within certain types
- this(): Matches join points where the bean reference is an instance of the given type
- target(): Matches join points where the target object is an instance of the given type
- @target(): Matches join points where the class of the executing object has an annotation
- @args(): Matches join points where the arguments are annotated with the given annotation
- @within(): Matches join points within types with the given annotation
- @annotation: Matches join points where the subject has the given annotation

Example: `execution(* com.example.service.*.*(..))`

## Spring Annotations

### Q21: What is the purpose of @Component, @Service, @Repository, and @Controller annotations?
**Answer:** These are stereotype annotations in Spring that mark classes as Spring-managed components:
- @Component: Generic stereotype for any Spring-managed component
- @Service: Indicates that the class provides business services, specialization of @Component
- @Repository: Indicates that the class is a Data Access Object, specialization of @Component with additional benefits like exception translation
- @Controller: Indicates that the class serves as a web controller, specialization of @Component
- @RestController: Combines @Controller and @ResponseBody, for RESTful web services

While functionally similar (all create Spring beans), they provide semantic meaning and enable specific behaviors.

### Q22: What is the difference between @Configuration and @Component?
**Answer:**
- @Configuration: Indicates that a class declares one or more @Bean methods and may be processed by the Spring container to generate bean definitions. @Configuration classes are typically designed to be the primary source of bean definitions.
  - Methods annotated with @Bean are processed specially to ensure that inter-bean references are satisfied
  - @Configuration classes are proxied by CGLIB to intercept method calls for proper bean lifecycle management

- @Component: A generic stereotype for any Spring-managed component, typically used for auto-detection and auto-configuration.
  - Can contain @Bean methods but they won't be processed the same way as in @Configuration classes
  - Inter-bean references may not work as expected without @Configuration

### Q23: What is the purpose of @Conditional annotations in Spring?
**Answer:** @Conditional annotations control whether a component or configuration should be registered based on certain conditions. Spring Boot heavily uses these for auto-configuration.

Common conditional annotations:
- @ConditionalOnBean: Condition matches when the specified bean exists
- @ConditionalOnMissingBean: Condition matches when the specified bean doesn't exist
- @ConditionalOnClass: Condition matches when the specified class is on the classpath
- @ConditionalOnMissingClass: Condition matches when the specified class is not on the classpath
- @ConditionalOnProperty: Condition matches when the specified property has a specific value
- @ConditionalOnResource: Condition matches when the specified resource exists
- @ConditionalOnWebApplication: Condition matches when the application is a web application
- @ConditionalOnExpression: Condition matches when the SpEL expression evaluates to true

### Q24: What is the difference between @Inject and @Autowired?
**Answer:**
- @Autowired is Spring's own annotation for dependency injection
- @Inject is from the Java CDI (Contexts and Dependency Injection) specification (JSR-330)

Similarities:
- Both can be used on constructors, methods, and fields
- Both support required/optional dependencies

Differences:
- @Autowired has the 'required' attribute to make dependencies optional
- @Inject requires @Nullable or Optional<> for optional dependencies
- @Autowired is Spring-specific, while @Inject is standard across DI frameworks
- @Qualifier works differently with each

Spring supports both, but @Autowired is more commonly used in Spring applications.

### Q25: What is the purpose of @Transactional annotation?
**Answer:** @Transactional annotation is used to specify the transaction attributes for methods that need to execute within a transactional context. It can be applied at the class or method level.

Key attributes:
- propagation: Defines how transactions relate to each other (REQUIRED, REQUIRES_NEW, etc.)
- isolation: Defines the isolation level (READ_UNCOMMITTED, READ_COMMITTED, etc.)
- timeout: Specifies how long the transaction may run before timing out
- readOnly: Hint that the transaction should be read-only
- rollbackFor/noRollbackFor: Specifies which exceptions should cause a rollback or not

When @Transactional is used, Spring creates a proxy around the bean that intercepts method calls and manages the transaction.

## Spring Boot Fundamentals

### Q26: What is Spring Boot and what problems does it solve?
**Answer:** Spring Boot is a project built on top of the Spring Framework that simplifies the development of Spring applications. It solves several problems:
- Reduces boilerplate configuration through auto-configuration
- Eliminates XML configuration with sensible defaults
- Provides embedded servers (Tomcat, Jetty, Undertow) for standalone applications
- Simplifies dependency management with starter POMs
- Offers production-ready features like metrics, health checks, and externalized configuration
- Facilitates microservices development with Spring Cloud integration
- Reduces the learning curve for Spring Framework

### Q27: What are the key components of a Spring Boot application?
**Answer:** Key components include:
1. @SpringBootApplication annotation (combines @Configuration, @EnableAutoConfiguration, and @ComponentScan)
2. Main application class with the main() method
3. Auto-configuration
4. Starter dependencies
5. application.properties/application.yml for configuration
6. Embedded server
7. Actuator for monitoring and management
8. Spring Boot CLI (optional)
9. Spring Initializr for project setup (optional)

### Q28: How does Spring Boot's auto-configuration work?
**Answer:** Spring Boot auto-configuration attempts to automatically configure a Spring application based on the dependencies present on the classpath. The process works as follows:
1. @EnableAutoConfiguration or @SpringBootApplication triggers the auto-configuration mechanism
2. Spring Boot scans the classpath for libraries and their META-INF/spring.factories files
3. These files list auto-configuration classes that should be considered
4. Each auto-configuration class contains @Conditional annotations that determine if it should be applied
5. If conditions are met (e.g., specific classes on classpath, beans not already defined), the configuration is applied
6. Configurations have sensible defaults but can be overridden via properties

This allows applications to work with minimal configuration while maintaining flexibility.

### Q29: What is the significance of @SpringBootApplication annotation?
**Answer:** @SpringBootApplication is a convenience annotation that combines:
1. @Configuration: Tags the class as a source of bean definitions
2. @EnableAutoConfiguration: Enables Spring Boot's auto-configuration mechanism
3. @ComponentScan: Enables component scanning in the package of the annotated class and sub-packages

It's typically applied to the main application class and serves as the entry point for Spring Boot applications. This single annotation reduces boilerplate code and clearly identifies the primary configuration class.

### Q30: How can you exclude specific auto-configuration classes in Spring Boot?
**Answer:** There are several ways to exclude auto-configuration classes:
1. Using the exclude attribute of @EnableAutoConfiguration or @SpringBootApplication:
   ```
   @SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
   ```

2. Using the excludeName attribute (when the class is not directly available):
   ```
   @SpringBootApplication(excludeName = {"org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration"})
   ```

3. Using the spring.autoconfigure.exclude property in application.properties/yml:
   ```properties
   spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
   ```

This is useful when you want to prevent certain auto-configurations from being applied, typically to provide your own custom configuration.

## Spring Boot Auto-Configuration

### Q31: How would you create your own auto-configuration in Spring Boot?
**Answer:** To create a custom auto-configuration:

1. Create a configuration class with @Configuration:
   ```java
   @Configuration
   @ConditionalOnClass(YourService.class)
   @EnableConfigurationProperties(YourProperties.class)
   public class YourAutoConfiguration {
       @Bean
       @ConditionalOnMissingBean
       public YourService yourService(YourProperties properties) {
           return new YourServiceImpl(properties.getProperty());
       }
   }
   ```

2. Create a properties class if needed:
   ```java
   @ConfigurationProperties(prefix = "your.prefix")
   public class YourProperties {
       private String property;
       // getters and setters
   }
   ```

3. Register the auto-configuration in META-INF/spring.factories:
   ```
   org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
   com.example.YourAutoConfiguration
   ```

4. Use appropriate @Conditional annotations to control when the auto-configuration is applied

5. Follow naming conventions: *AutoConfiguration for the class name

### Q32: What is the difference between @Bean and @Component?
**Answer:**
- @Component is a class-level annotation that marks a class as a Spring component, which will be auto-detected through classpath scanning when using component-scanning.

- @Bean is a method-level annotation within @Configuration classes that explicitly declares a single bean, rather than letting Spring automatically detect it. It gives you explicit control over the instantiation logic.

Key differences:
- Scope: @Component is used at the class level, @Bean at the method level
- Control: @Bean gives more control over initialization
- Usage: @Bean is often used for third-party classes you can't modify, @Component for your own classes
- Configuration: @Bean is typically used within @Configuration classes
- Lifecycle: @Bean methods can have custom initialization and destruction logic

### Q33: What is Spring Boot DevTools and what features does it provide?
**Answer:** Spring Boot DevTools is a set of tools that enhances the development experience:

Features:
1. Automatic application restart when files on the classpath change
2. LiveReload support to automatically refresh the browser
3. Remote development support
4. Development-specific properties (separate from production)
5. Automatic H2 console enablement if H2 is used
6. Caching disabled for template engines during development
7. Global DevTools configuration through .spring-boot-devtools.properties

DevTools is automatically disabled when running a packaged application (e.g., java -jar).

### Q34: How does Spring Boot handle different environments (dev, test, prod)?
**Answer:** Spring Boot provides several mechanisms for environment-specific configuration:

1. Profile-specific properties files:
   - application-{profile}.properties or application-{profile}.yml
   - Activated via spring.profiles.active property

2. @Profile annotation:
   - Applied to @Component, @Configuration, or @Bean to conditionally register beans
   - `@Profile("dev")` or `@Profile("!prod")`

3. Programmatic profile activation:
   - SpringApplication.setAdditionalProfiles()
   - ConfigurableEnvironment.setActiveProfiles()

4. External configuration with precedence:
   - Command-line arguments
   - SPRING_APPLICATION_JSON
   - System properties
   - Environment variables
   - Profile-specific properties outside the packaged application
   - Profile-specific properties packaged inside the application
   - Application properties outside the packaged application
   - Application properties packaged inside the application

### Q35: What is Spring Boot Actuator and what endpoints does it provide?
**Answer:** Spring Boot Actuator provides production-ready features for monitoring and managing Spring Boot applications.

Key endpoints:
- /health: Application health information
- /info: Application information
- /metrics: Application metrics
- /env: Environment properties
- /configprops: Configuration properties
- /mappings: Request mapping information
- /beans: All Spring beans in the application
- /threaddump: Thread dump
- /heapdump: Heap dump file
- /loggers: Logger configuration
- /auditevents: Audit events
- /scheduledtasks: Scheduled tasks
- /httptrace: HTTP trace information
- /sessions: Session information (for web applications)
- /shutdown: Triggers application shutdown (disabled by default)
- /caches: Available caches
- /flyway: Flyway database migrations
- /liquibase: Liquibase database migrations

Actuator endpoints can be secured and customized, and are accessible via JMX or HTTP.

## Spring Boot Starters

### Q36: What are Spring Boot Starters and why are they useful?
**Answer:** Spring Boot Starters are curated dependency descriptors that simplify dependency management for common application types.

Benefits:
- Simplify build configuration by grouping related dependencies
- Ensure compatible versions of dependencies
- Reduce the need to search for and configure individual dependencies
- Provide sensible defaults and auto-configuration
- Follow a naming convention: spring-boot-starter-*

Common starters:
- spring-boot-starter-web: For web applications (Spring MVC, Tomcat)
- spring-boot-starter-data-jpa: For JPA with Hibernate
- spring-boot-starter-security: For Spring Security
- spring-boot-starter-test: For testing with JUnit, Mockito, etc.
- spring-boot-starter-actuator: For monitoring and management
- spring-boot-starter-webflux: For reactive web applications
- spring-boot-starter-jdbc: For JDBC database access

### Q37: How would you create a custom Spring Boot Starter?
**Answer:** To create a custom starter:

1. Create an autoconfigure module (e.g., my-feature-spring-boot-autoconfigure):
   - Contains auto-configuration classes
   - Includes @Configuration classes with @Conditional annotations
   - Defines @ConfigurationProperties classes
   - Registers in META-INF/spring.factories

2. Create a starter module (e.g., my-feature-spring-boot-starter):
   - Provides a convenient dependency descriptor
   - Depends on the autoconfigure module
   - Includes necessary dependencies for the feature
   - Contains no actual code, just dependency management

3. Follow naming conventions:
   - Non-official starters should not start with "spring-boot"
   - Use the format "xxx-spring-boot-starter"

4. Document the starter with proper README and configuration properties

This separation allows users to use your auto-configuration without the starter if they need more control over dependencies.

### Q38: What is the difference between spring-boot-starter-web and spring-boot-starter-webflux?
**Answer:**
- spring-boot-starter-web: For building traditional servlet-based web applications using Spring MVC
  - Uses Tomcat as the default embedded server
  - Based on the Servlet API and blocking I/O
  - Synchronous, thread-per-request model
  - Mature and widely used

- spring-boot-starter-webflux: For building reactive web applications using Spring WebFlux
  - Uses Netty as the default server (non-blocking)
  - Based on Reactive Streams and non-blocking I/O
  - Asynchronous, event-loop model
  - Supports higher concurrency with fewer threads
  - Better suited for microservices with high concurrency needs

You typically choose one or the other, not both, as they represent different programming models.

### Q39: What is spring-boot-starter-test and what does it include?
**Answer:** spring-boot-starter-test is a starter for testing Spring Boot applications. It includes:

1. JUnit 5: The foundation for testing in Java
2. Spring Test & Spring Boot Test: Utilities and integration test support
3. AssertJ: Fluent assertion library
4. Hamcrest: Matcher objects for test expressions
5. Mockito: Mocking framework
6. JSONassert: Assertions for JSON
7. JsonPath: XPath for JSON

It provides annotations like:
- @SpringBootTest: For integration tests loading the full application context
- @WebMvcTest: For testing Spring MVC controllers
- @DataJpaTest: For testing JPA repositories
- @RestClientTest: For testing REST clients
- @JsonTest: For testing JSON serialization/deserialization
- @TestPropertySource: For customizing test properties

This starter is typically added with test scope in the build file.

### Q40: What is the purpose of spring-boot-starter-actuator?
**Answer:** spring-boot-starter-actuator adds production-ready features to a Spring Boot application:

1. Monitoring: Health checks, metrics, and information about the running application
2. Management: Endpoints to manage and interact with the application
3. Auditing: Tracking of security-related events
4. HTTP tracing: Information about HTTP requests and responses

Key features:
- Health status and application information
- Metrics integration (Micrometer)
- Environment details and configuration properties
- Logging configuration
- Thread dumps and heap dumps
- Audit events
- HTTP request tracing
- Integration with external monitoring systems (Prometheus, Graphite, etc.)

It's essential for operating applications in production environments and provides insights into the application's behavior and health.

## Spring Boot Properties

### Q41: What are the different ways to configure properties in Spring Boot?
**Answer:** Spring Boot offers multiple ways to configure properties:

1. application.properties or application.yml files:
   - In the classpath root
   - In a /config subdirectory of the classpath
   - In the current directory
   - In a /config subdirectory of the current directory

2. Profile-specific properties:
   - application-{profile}.properties or application-{profile}.yml

3. Command-line arguments:
   - `--server.port=8080`

4. Environment variables:
   - `SPRING_DATASOURCE_URL=jdbc:mysql://localhost/test`

5. System properties:
   - `-Dserver.port=8080`

6. JNDI attributes from java:comp/env

7. @PropertySource annotation for custom property files

8. Default properties specified using SpringApplication.setDefaultProperties

The order above roughly represents the precedence (later sources override earlier ones).

### Q42: What is the difference between application.properties and application.yml?
**Answer:** Both files serve the same purpose but use different formats:

application.properties:
- Uses key-value pairs with "." notation for hierarchy
- Simple format, one property per line
- Example:
  ```properties
  server.port=8080
  spring.datasource.url=jdbc:mysql://localhost/test
  spring.datasource.username=dbuser
  ```

application.yml:
- Uses YAML format with hierarchical structure
- More readable for complex configurations
- Supports lists and maps more naturally
- Example:
  ```yaml
  server:
    port: 8080
  spring:
    datasource:
      url: jdbc:mysql://localhost/test
      username: dbuser
  ```

Both are functionally equivalent, and Spring Boot loads both if present (with properties from .yml overriding those from .properties).

### Q43: How can you externalize configuration in Spring Boot?
**Answer:** Spring Boot provides several ways to externalize configuration:

1. Command-line arguments:
   - `java -jar app.jar --server.port=8080`

2. OS environment variables:
   - `SERVER_PORT=8080 java -jar app.jar`

3. External application.properties/yml:
   - `java -jar app.jar --spring.config.location=file:/path/to/config/`

4. Profile-specific files outside the application:
   - `java -jar app.jar --spring.profiles.active=prod`

5. Cloud config server (with Spring Cloud):
   - Centralized configuration management

6. Kubernetes ConfigMaps and Secrets:
   - For containerized applications

7. Using @Value and @ConfigurationProperties to bind external values:
   ```
   @Value("${server.port}")
   private int serverPort;
   ```

This approach allows you to keep environment-specific configuration separate from your application code, making it more portable across different environments and easier to manage configuration changes without rebuilding the application.
