# Spring and Spring Boot Interview Questions (3-5 Years Experience)
**Author**: xAI Team  
**Last Updated**: May 11, 2025  
**Audience**: Mid-level Java Developers

> 📚 **Prepare for your next interview** with 50 curated Spring and Spring Boot questions, complete with explanations, code examples, and outcomes.

## Table of Contents
<details><summary>Click to expand</summary>  

- [Spring Core and Dependency Injection](#spring-core-and-dependency-injection) ![Medium](https://img.shields.io/badge/Difficulty-Medium-yellow)
    - Bean Management (Q1-Q5, Q27-Q31)
    - Annotations and Scopes (Q6, Q28-Q29)
- [Spring Boot Configuration](#spring-boot-configuration) ![Medium](https://img.shields.io/badge/Difficulty-Medium-yellow)
    - Application Setup (Q7-Q12, Q33, Q39, Q42, Q46)
    - Customization (Q9-Q10, Q43, Q45, Q49)
- [Spring Data JPA](#spring-data-jpa) ![Advanced](https://img.shields.io/badge/Difficulty-Advanced-red)
    - Entity Management (Q18-Q22, Q25-Q26)
    - Repository Operations (Q23-Q24, Q36-Q38, Q40)
- [Spring REST Controllers](#spring-rest-controllers) ![Medium](https://img.shields.io/badge/Difficulty-Medium-yellow)
    - REST API Development (Q13-Q17, Q34-Q35)
    - Testing and Validation (Q44, Q47-Q48)
- [Summary Table](#summary-table)

</details>  

---

## Spring Core and Dependency Injection
> 🛠️ Core concepts for building robust Spring applications, focusing on bean lifecycle and dependency injection.

### Q1: What happens if a Spring bean has a private constructor? ![Medium](https://img.shields.io/badge/Difficulty-Medium-yellow)
> 📌 **Key Concept**: Reflection-based bean instantiation

<details><summary>📝 **Explanation**</summary>  
A private constructor restricts external instantiation, often used for singleton patterns. Spring’s IoC container uses reflection to bypass access modifiers, creating the bean regardless. If a singleton pattern uses `getInstance()`, Spring ignores it unless configured with a `@Bean` method.  

⚠️ **Warning**: Misconfiguring singleton enforcement may create multiple instances.
</details>  

#### Java Code
```java
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class SingletonBean {
    private static final SingletonBean INSTANCE = new SingletonBean();

    // Private constructor for singleton
    private SingletonBean() {
        System.out.println("Private constructor invoked");
    }

    public static SingletonBean getInstance() {
        return INSTANCE;
    }

    public void doSomething() {
        System.out.println("SingletonBean method called");
    }
}

@Component
public class ClientBean {
    @Autowired
    private SingletonBean singletonBean;

    public void execute() {
        singletonBean.doSomething();
    }
}
```  

#### Outcome
<span style="color: green">Spring instantiates `SingletonBean` via reflection, ignoring `getInstance()`.</span> The bean is injected into `ClientBean`.

| ℹ️ **Tip** |  
| Define a `@Bean` method returning `getInstance()` to enforce the singleton pattern. |

---

### Q2: What’s the difference between constructor and setter injection? ![Medium](https://img.shields.io/badge/Difficulty-Medium-yellow)
> 📌 **Key Concept**: Dependency injection strategies

<details><summary>📝 **Explanation**</summary>  
- **Constructor Injection**: Injects dependencies via a constructor, ensuring immutability and mandatory dependencies. Ideal for required dependencies but may complicate circular dependencies.  
- **Setter Injection**: Uses setter methods, allowing flexibility for optional dependencies but risking incomplete initialization.  

⚠️ **Warning**: Setter injection may lead to partial initialization if setters are not called.
</details>  

#### Java Code
```java
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class ServiceA {
    public String serve() {
        return "Service A";
    }
}

@Component
public class ConstructorInjectedBean {
    private final ServiceA serviceA;

    @Autowired // Injects via constructor
    public ConstructorInjectedBean(ServiceA serviceA) {
        this.serviceA = serviceA;
    }

    public String execute() {
        return serviceA.serve();
    }
}

@Component
public class SetterInjectedBean {
    private ServiceA serviceA;

    @Autowired // Injects via setter
    public void setServiceA(ServiceA serviceA) {
        this.serviceA = serviceA;
    }

    public String execute() {
        return serviceA.serve();
    }
}
```  

#### Outcome
<span style="color: green">Constructor injection ensures immutability and thread-safety; setter injection allows flexibility but risks null dependencies.</span>

| **Comparison** | Constructor Injection | Setter Injection |  
|----------------|-----------------------|------------------|  
| Immutability   | Yes                   | No               |  
| Mandatory      | Enforced              | Optional         |  
| Circular Dependency | Needs `@Lazy`       | Easier to handle |  
| Thread Safety  | High                  | Lower            |  

| ℹ️ **Tip** |  
| Use `@Lazy` with constructor injection to resolve circular dependencies. |

---

### Q3: Can we create a Spring bean without using `@Component`? ![Medium](https://img.shields.io/badge/Difficulty-Medium-yellow)
> 📌 **Key Concept**: Alternative bean creation methods

<details><summary>📝 **Explanation**</summary>  
Yes, Spring beans can be created without `@Component` using:  
- `@Bean`: Define a method in a `@Configuration` class.  
- XML Configuration: Declare beans in an XML file (legacy).  
- `FactoryBean`: Implement for complex bean creation.  
- Programmatic Registration: Use `BeanDefinitionRegistry`.  

⚠️ **Warning**: XML configuration is outdated and less common in modern Spring applications.
</details>  

#### Java Code
```java
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;

@Configuration
public class AppConfig {
    @Bean
    public MyBean myBean() {
        return new MyBean();
    }
}

public class MyBean {
    public String process() {
        return "Bean created without @Component";
    }
}
```  

#### Outcome
<span style="color: green">`MyBean` is registered as a Spring bean via the `@Bean` method.</span> Ideal for third-party classes or custom initialization.

| ℹ️ **Tip** |  
| Use `@Bean` for integrating external libraries or controlling bean instantiation. |

---

### Q4: Can we have multiple `@Bean` methods of the same type in a `@Configuration` class? ![Medium](https://img.shields.io/badge/Difficulty-Medium-yellow)
> 📌 **Key Concept**: Bean name uniqueness and resolution

<details><summary>📝 **Explanation**</summary>  
Multiple `@Bean` methods can return the same type, but each must have a unique bean name (default is the method name). Spring throws a `NoUniqueBeanDefinitionException` if multiple beans of the same type exist during injection, unless resolved with `@Primary` or `@Qualifier`.  

⚠️ **Warning**: Ambiguous beans cause runtime errors without proper configuration.
</details>  

#### Java Code
```java
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Configuration
public class AppConfig {
    @Bean
    public MyService serviceOne() {
        return new MyService("Service One");
    }

    @Bean
    @Primary
    public MyService serviceTwo() {
        return new MyService("Service Two");
    }
}

@Component
public class ClientBean {
    @Autowired
    private MyService myService; // Injects serviceTwo (@Primary)

    public String getServiceName() {
        return myService.getName();
    }
}

public class MyService {
    private final String name;

    public MyService(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
```  

#### Outcome
<span style="color: green">Both `serviceOne` and `serviceTwo` are registered; `ClientBean` injects `serviceTwo` due to `@Primary`.</span>

| ℹ️ **Tip** |  
| Use `@Qualifier` to explicitly select a bean by name if `@Primary` isn’t suitable. |

---

### Q5: What happens when more than one bean of the same type is available? ![Medium](https://img.shields.io/badge/Difficulty-Medium-yellow)
> 📌 **Key Concept**: Bean resolution strategies

<details><summary>📝 **Explanation**</summary>  
If multiple beans of the same type exist, Spring throws a `NoUniqueBeanDefinitionException` unless:  
- One bean is marked `@Primary`.  
- `@Qualifier` specifies the bean name.  
- A collection (e.g., `List<MyService>`) injects all matching beans.  

⚠️ **Warning**: Unresolved bean ambiguity halts application startup.
</details>  

#### Java Code
```java
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import java.util.List;

public interface MyService {
    String serve();
}

@Component
public class ServiceA implements MyService {
    public String serve() {
        return "Service A";
    }
}

@Component
public class ServiceB implements MyService {
    public String serve() {
        return "Service B";
    }
}

@Component
public class ClientBean {
    @Autowired
    @Qualifier("serviceA")
    private MyService myService; // Injects ServiceA

    @Autowired
    private List<MyService> allServices; // Injects all MyService beans

    public String execute() {
        return myService.serve();
    }

    public List<String> executeAll() {
        return allServices.stream().map(MyService::serve).toList();
    }
}
```  

#### Outcome
<span style="color: green">`@Qualifier("serviceA")` injects `ServiceA`; `List<MyService>` includes both `ServiceA` and `ServiceB`.</span>

| ℹ️ **Tip** |  
| Inject a `List` or `Map` to work with all beans of a type dynamically. |

---

### Q6: What’s the difference between `@Service`, `@Repository`, and `@Component`? ![Medium](https://img.shields.io/badge/Difficulty-Medium-yellow)
> 📌 **Key Concept**: Stereotype annotations

<details><summary>📝 **Explanation**</summary>  
These are stereotype annotations for Spring-managed beans:  
- `@Component`: General-purpose, marks any class as a Spring bean.  
- `@Service`: Indicates a service-layer bean for business logic, semantically distinct but functionally identical to `@Component`.  
- `@Repository`: Marks a data-access bean, providing exception translation (e.g., `SQLException` to `DataAccessException`).  

⚠️ **Warning**: Use `@Repository` for persistence to enable exception translation.
</details>  

#### Java Code
```java
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.stereotype.Repository;

@Component
public class GeneralComponent {
    public String process() {
        return "General Component";
    }
}

@Service
public class BusinessService {
    public String process() {
        return "Business Service";
    }
}

@Repository
public class UserRepository {
    public String findUser() {
        return "User from Repository";
    }
}
```  

#### Outcome
<span style="color: green">All classes are registered as beans; `@Repository` adds exception translation.</span>

| **Comparison** | Purpose | Exception Translation | Layer |  
|----------------|---------|-----------------------|-------|  
| `@Component`   | General-purpose | No | Any |  
| `@Service`     | Business logic | No | Service Layer |  
| `@Repository`  | Data access | Yes | Persistence Layer |  

| ℹ️ **Tip** |  
| Use `@Service` and `@Repository` for semantic clarity in service and data layers. |

---

## Spring Boot Configuration
> 🛠️ Configuration techniques for flexible Spring Boot deployments.

### Q7: Can we create a Spring Boot application without `@SpringBootApplication`? ![Medium](https://img.shields.io/badge/Difficulty-Medium-yellow)
> 📌 **Key Concept**: Manual configuration setup

<details><summary>📝 **Explanation**</summary>  
Yes, `@SpringBootApplication` combines `@Configuration`, `@EnableAutoConfiguration`, and `@ComponentScan`. Without it, you must manually apply these annotations to replicate its functionality.  

⚠️ **Warning**: Omitting any annotation (e.g., `@EnableAutoConfiguration`) disables corresponding features.
</details>  

#### Java Code
```java
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = "com.example")
public class MyApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class, args);
    }
}
```  

#### Outcome
<span style="color: green">The application functions like one with `@SpringBootApplication`, enabling auto-configuration and scanning.</span>

| ℹ️ **Tip** |  
| Use `@SpringBootApplication` for simplicity unless specific customization is needed. |

---

### Q8: Can we run a Spring Boot application without an embedded server? ![Medium](https://img.shields.io/badge/Difficulty-Medium-yellow)
> 📌 **Key Concept**: Non-web application setup

<details><summary>📝 **Explanation**</summary>  
Yes, Spring Boot can run without an embedded server by:  
- Packaging as a WAR for external servers (e.g., Tomcat).  
- Excluding web dependencies (e.g., `spring-boot-starter-web`).  
- Using Spring Boot for non-web applications (e.g., batch jobs).  

⚠️ **Warning**: Ensure dependencies align with the application’s purpose to avoid unnecessary overhead.
</details>  

#### XML Code (POM)
```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter</artifactId>
    </dependency>
    <!-- Exclude spring-boot-starter-web -->
</dependencies>
```  

#### Java Code
```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NonWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(NonWebApplication.class, args);
        System.out.println("Running without embedded server");
    }
}
```  

#### Outcome
<span style="color: green">The application runs without an embedded server, suitable for command-line or batch tasks.</span>

| ℹ️ **Tip** |  
| Use `spring-boot-starter` for minimal dependencies in non-web applications. |

---

### Q9: How do you disable auto-configuration in Spring Boot? ![Medium](https://img.shields.io/badge/Difficulty-Medium-yellow)
> 📌 **Key Concept**: Auto-configuration control

<details><summary>📝 **Explanation**</summary>  
Disable auto-configuration using:  
- `@SpringBootApplication(exclude = {ClassName.class})` to exclude specific configurations.  
- `spring.autoconfigure.exclude` in `application.properties`.  
- Omitting `@EnableAutoConfiguration` (not recommended).  

⚠️ **Warning**: Excluding critical configurations may break application functionality.
</details>  

#### Java Code
```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```  

#### Properties File
```properties
spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
```  

#### Outcome
<span style="color: green">`DataSourceAutoConfiguration` is disabled, preventing automatic database setup.</span>

| ℹ️ **Tip** |  
| Use `exclude` for fine-grained control over auto-configuration. |

---

### Q10: Can we change the port of a Spring Boot application without modifying `application.properties`? ![Medium](https://img.shields.io/badge/Difficulty-Medium-yellow)
> 📌 **Key Concept**: Dynamic port configuration

<details><summary>📝 **Explanation**</summary>  
Yes, change the port via:  
- Command-line: `--server.port=8081`.  
- Programmatic configuration: Set `SpringApplication` properties.  
- Environment variable: `SERVER_PORT=8081`.  

⚠️ **Warning**: Command-line arguments override programmatic settings.
</details>  

#### Java Code
```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.Collections;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        app.setDefaultProperties(Collections.singletonMap("server.port", "8081"));
        app.run(args);
    }
}
```  

#### Outcome
<span style="color: green">The application runs on port 8081.</span>

| ℹ️ **Tip** |  
| Use environment variables for flexible deployment configurations. |

---

### Q11: What happens if we have multiple `application.properties` files in a Spring Boot application? ![Medium](https://img.shields.io/badge/Difficulty-Medium-yellow)
> 📌 **Key Concept**: Property file precedence

<details><summary>📝 **Explanation**</summary>  
Spring Boot loads `application.properties` in order, with later files overriding earlier ones:  
- `classpath:/application.properties` (default).  
- `classpath:/application-{profile}.properties` (profile-specific).  
- `file:./application.properties` (external).  
- Command-line arguments or environment variables (highest precedence).  

⚠️ **Warning**: Unspecified properties fall back to the default file, which may cause unexpected behavior.
</details>  

#### Properties File (Default)
```properties
server.port=8080
app.name=MyApp
```  

#### Properties File (Dev Profile)
```properties
server.port=8081
```  

#### Java Code
```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, "--spring.profiles.active=dev");
    }
}
```  

#### Outcome
<span style="color: green">With `spring.profiles.active=dev`, `application-dev.properties` sets `server.port=8081`.</span>

| ℹ️ **Tip** |  
| Use profile-specific properties for environment-specific configurations. |

---

### Q12: What happens if we use `@SpringBootApplication` on an interface? ![Medium](https://img.shields.io/badge/Difficulty-Medium-yellow)
> 📌 **Key Concept**: Annotation applicability

<details><summary>📝 **Explanation**</summary>  
`@SpringBootApplication` is designed for classes, combining `@Configuration`, `@EnableAutoConfiguration`, and `@ComponentScan`. Applying it to an interface causes a compilation error, as interfaces cannot be annotated with `@Configuration`.  

⚠️ **Warning**: Always apply `@SpringBootApplication` to a class with a `main` method.
</details>  

#### Java Code
```java
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public interface InvalidApplication { // Compilation error
    void someMethod();
}
```  

#### Outcome
<span style="color: green">The code fails to compile, as `@SpringBootApplication` is invalid on interfaces.</span>

| ℹ️ **Tip** |  
| Ensure the main application class is annotated correctly to avoid startup issues. |

---

## Spring REST Controllers
> 🛠️ Building and managing REST APIs with Spring, including endpoint configuration and error handling.

### Q13: What’s the difference between `@Controller` and `@RestController`? ![Medium](https://img.shields.io/badge/Difficulty-Medium-yellow)
> 📌 **Key Concept**: MVC vs. REST controllers

<details><summary>📝 **Explanation**</summary>  
- `@Controller`: Marks a class as a web controller for MVC applications, typically returning view names (e.g., for Thymeleaf).  
- `@RestController`: Combines `@Controller` and `@ResponseBody`, serializing return values to JSON/XML for REST APIs.  

⚠️ **Warning**: Use `@RestController` for APIs to avoid manual `@ResponseBody` annotations.
</details>  

#### Java Code
```java
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class MvcController {
    @GetMapping("/view")
    public String getView() {
        return "index"; // Returns view name
    }
}

@RestController
public class ApiController {
    @GetMapping("/api")
    public String getData() {
        return "Data"; // Returns JSON
    }
}
```  

#### Outcome
<span style="color: green">`MvcController` returns a view name; `ApiController` returns serialized JSON.</span>

| ℹ️ **Tip** |  
| Use `@RestController` for REST APIs to simplify response handling. |

---

### Q14: What happens if we use `@RequestMapping` on a method without specifying a path? ![Medium](https://img.shields.io/badge/Difficulty-Medium-yellow)
> 📌 **Key Concept**: URL mapping behavior

<details><summary>📝 **Explanation**</summary>  
Without a path, `@RequestMapping` maps to the class-level path (if defined) or the root path (`/`) relative to the application context. This risks ambiguous mappings if multiple methods lack paths.  

⚠️ **Warning**: Ambiguous mappings may cause runtime errors.
</details>  

#### Java Code
```java
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiController {
    @RequestMapping
    public String noPath() {
        return "No path specified";
    }
}
```  

#### Outcome
<span style="color: green">The method maps to `/api`.</span> Without a class-level path, it maps to `/`, risking conflicts.

| ℹ️ **Tip** |  
| Always specify method-level paths to avoid ambiguity. |

---

### Q15: What happens if a `@GetMapping` method in a REST controller returns null? ![Medium](https://img.shields.io/badge/Difficulty-Medium-yellow)
> 📌 **Key Concept**: Response handling

<details><summary>📝 **Explanation**</summary>  
If a `@GetMapping` method returns `null`, Spring’s JSON serializer (e.g., Jackson) produces an HTTP 200 OK response with an empty body. To return a specific status (e.g., 404), use `ResponseEntity`.  

⚠️ **Warning**: Returning `null` may confuse clients expecting meaningful responses.
</details>  

#### Java Code
```java
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {
    @GetMapping("/data")
    public String getData() {
        return null; // Empty response body
    }
}
```  

#### Outcome
<span style="color: green">Returns HTTP 200 with an empty body (`{}`).</span>

| ℹ️ **Tip** |  
| Use `ResponseEntity.notFound().build()` for 404 responses. |

---

### Q16: What happens if we send a POST request to a `@GetMapping` endpoint? ![Medium](https://img.shields.io/badge/Difficulty-Medium-yellow)
> 📌 **Key Concept**: HTTP method restrictions

<details><summary>📝 **Explanation**</summary>  
`@GetMapping` restricts the endpoint to GET requests. Sending a POST request results in an HTTP 405 Method Not Allowed response.  

⚠️ **Warning**: Ensure endpoints support intended HTTP methods to avoid client errors.
</details>  

#### Java Code
```java
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {
    @GetMapping("/data")
    public String getData() {
        return "GET response";
    }
}
```  

#### Outcome
<span style="color: green">A POST request to `/data` returns HTTP 405.</span>

| ℹ️ **Tip** |  
| Use `@PostMapping` for POST requests or `@RequestMapping` for multiple methods. |

---

### Q17: What happens if two REST endpoint methods annotated with `@GetMapping` have the same path? ![Medium](https://img.shields.io/badge/Difficulty-Medium-yellow)
> 📌 **Key Concept**: Endpoint uniqueness

<details><summary>📝 **Explanation**</summary>  
Spring detects ambiguous mappings at startup and throws an `IllegalStateException`, as it cannot resolve which method to invoke for the same path and HTTP method.  

⚠️ **Warning**: Ambiguous mappings prevent application startup.
</details>  

#### Java Code
```java
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {
    @GetMapping("/data")
    public String getData1() {
        return "Data 1";
    }

    @GetMapping("/data")
    public String getData2() {
        return "Data 2";
    }
}
```  

#### Outcome
<span style="color: green">The application fails to start with an `IllegalStateException`: Ambiguous mapping.</span>

| ℹ️ **Tip** |  
| Use unique paths or differentiate with query parameters/headers. |

---

## Spring Data JPA
> 🛠️ Database operations with Spring Data JPA, including entity relationships and queries.

### Q18: What happens if we don’t define an `@Id` field in a JPA entity? ![Advanced](https://img.shields.io/badge/Difficulty-Advanced-red)
> 📌 **Key Concept**: Entity identification

<details><summary>📝 **Explanation**</summary>  
JPA requires an `@Id` field to uniquely identify entities. Without it, Hibernate throws a `MappingException` at startup, as it cannot map the entity to a primary key.  

⚠️ **Warning**: Missing `@Id` prevents application startup.
</details>  

#### Java Code
```java
import jakarta.persistence.Entity;

@Entity
public class InvalidEntity { // Missing @Id
    private String name;
}
```  

#### Outcome
<span style="color: green">The application fails with `MappingException`: No identifier specified.</span>

| ℹ️ **Tip** |  
| Always define an `@Id` field for JPA entities. |

---

### Q19: What’s the difference between `@Fetch = FetchType.LAZY` and `FetchType.EAGER`? ![Advanced](https://img.shields.io/badge/Difficulty-Advanced-red)
> 📌 **Key Concept**: Fetch strategies

<details><summary>📝 **Explanation**</summary>  
- `FetchType.LAZY`: Loads related entities only when accessed, minimizing query overhead but risking `LazyInitializationException` outside a session.  
- `FetchType.EAGER`: Loads related entities immediately, increasing query complexity but ensuring data availability.  

⚠️ **Warning**: EAGER fetching may cause performance issues with large datasets.
</details>  

#### Java Code
```java
import jakarta.persistence.*;
import java.util.List;

@Entity
public class Order {
    @Id
    private Long id;

    @OneToMany(fetch = FetchType.LAZY)
    private List<OrderItem> items;
}

@Entity
public class Customer {
    @Id
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    private Address address;
}
```  

#### Outcome
<span style="color: green">`Order.items` loads on access; `Customer.address` loads immediately.</span>

| ℹ️ **Tip** |  
| Use LAZY for large collections, EAGER for small, mandatory relationships. |

---

### Q20: What should we use if we don’t want to save a property in the database? ![Advanced](https://img.shields.io/badge/Difficulty-Advanced-red)
> 📌 **Key Concept**: Non-persistent fields

<details><summary>📝 **Explanation**</summary>  
Use `@Transient` to exclude a field from database mapping. The field remains available for business logic but is not persisted.  

⚠️ **Warning**: Avoid using `@Transient` for fields requiring persistence.
</details>  

#### Java Code
```java
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;

@Entity
public class User {
    @Id
    private Long id;

    private String name;

    @Transient
    private String tempToken; // Not saved in DB
}
```  

#### Outcome
<span style="color: green">`tempToken` is not mapped to a database column.</span>

| ℹ️ **Tip** |  
| Use DTOs for non-persistent fields to separate concerns. |

---

### Q21: How do you store a list of String values in a database using Spring Data JPA? ![Advanced](https://img.shields.io/badge/Difficulty-Advanced-red)
> 📌 **Key Concept**: Collection mapping

<details><summary>📝 **Explanation**</summary>  
Use `@ElementCollection` to map a collection of basic types (e.g., `List<String>`) to a separate table, with each element linked to the parent entity via a foreign key.  

⚠️ **Warning**: Ensure the collection table is properly configured to avoid mapping errors.
</details>  

#### Java Code
```java
import jakarta.persistence.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

@Entity
public class User {
    @Id
    private Long id;

    @ElementCollection
    @CollectionTable(name = "user_emails", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "email")
    private List<String> emails;
}

public interface UserRepository extends JpaRepository<User, Long> {}
```  

#### Outcome
<span style="color: green">The `user_emails` table stores each email with a `user_id` foreign key.</span>

| ℹ️ **Tip** |  
| Persist via `userRepository.save(user)` for automatic collection handling. |

---

### Q22: How do you save a child entity automatically while saving the parent entity? ![Advanced](https://img.shields.io/badge/Difficulty-Advanced-red)
> 📌 **Key Concept**: Cascading persistence

<details><summary>📝 **Explanation**</summary>  
Use `cascade = CascadeType.ALL` (or `PERSIST`) on the parent’s relationship to propagate persistence operations to child entities.  

⚠️ **Warning**: Overusing cascades may lead to unintended database operations.
</details>  

#### Java Code
```java
import jakarta.persistence.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

@Entity
public class Order {
    @Id
    private Long id;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items;
}

@Entity
public class OrderItem {
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
}

public interface OrderRepository extends JpaRepository<Order, Long> {}
```  

#### Outcome
<span style="color: green">Saving an `Order` with items via `orderRepository.save(order)` saves `OrderItem` entities.</span>

| ℹ️ **Tip** |  
| Use specific cascade types (e.g., `PERSIST`) for precise control. |

---

### Q23: Can we execute a native SQL query in Spring Data JPA? ![Advanced](https://img.shields.io/badge/Difficulty-Advanced-red)
> 📌 **Key Concept**: Native SQL queries

<details><summary>📝 **Explanation**</summary>  
Yes, use `@Query` with `nativeQuery = true` in a repository or `EntityManager` for native SQL queries.  

⚠️ **Warning**: Native queries bypass JPQL’s type safety, increasing error risk.
</details>  

#### Java Code
```java
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = "SELECT * FROM users WHERE name = :name", nativeQuery = true)
    List<User> findByNameNative(@Param("name") String name);
}
```  

#### Outcome
<span style="color: green">The query executes as raw SQL, returning `User` entities.</span>

| ℹ️ **Tip** |  
| Use native queries for complex SQL not supported by JPQL. |

---

### Q24: Can we execute a stored procedure using Spring Data JPA? ![Advanced](https://img.shields.io/badge/Difficulty-Advanced-red)
> 📌 **Key Concept**: Stored procedure execution

<details><summary>📝 **Explanation**</summary>  
Yes, use `@Query` with `nativeQuery = true` or `@Procedure` to call stored procedures. `EntityManager` supports dynamic calls.  

⚠️ **Warning**: Ensure the stored procedure exists in the database.
</details>  

#### Java Code
```java
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = "CALL get_user_count(:name, @count)", nativeQuery = true)
    void callStoredProcedure(@Param("name") String name);
}
```  

#### Outcome
<span style="color: green">The `get_user_count` stored procedure is executed with the `name` parameter.</span>

| ℹ️ **Tip** |  
| Use `@Procedure` for simpler stored procedure calls. |

---

### Q25: What happens if we don’t use `mappedBy` in a bidirectional relationship? ![Advanced](https://img.shields.io/badge/Difficulty-Advanced-red)
> 📌 **Key Concept**: Bidirectional relationship management

<details><summary>📝 **Explanation**</summary>  
In a bidirectional relationship, `mappedBy` identifies the owning side (with the foreign key). Without `mappedBy`, JPA treats both sides as independent, creating separate relationships (e.g., a join table and a foreign key), leading to redundancy or data inconsistency.  

⚠️ **Warning**: Missing `mappedBy` may cause database schema issues.
</details>  

#### Java Code
```java
import jakarta.persistence.*;
import java.util.List;

@Entity
public class Order {
    @Id
    private Long id;

    @OneToMany // Missing mappedBy
    private List<OrderItem> items;
}

@Entity
public class OrderItem {
    @Id
    private Long id;

    @ManyToOne
    private Order order;
}
```  

#### Outcome
<span style="color: green">JPA creates a join table for `Order.items` and a foreign key in `OrderItem`, causing redundancy.</span>

| ℹ️ **Tip** |  
| Use `mappedBy = "order"` on the non-owning side to avoid redundancy. |

---

### Q26: What happens if we use `@OneToOne` and `@OneToMany` in the same class? ![Advanced](https://img.shields.io/badge/Difficulty-Advanced-red)
> 📌 **Key Concept**: Multiple relationship types

<details><summary>📝 **Explanation**</summary>  
Using `@OneToOne` and `@OneToMany` in the same class is valid if they map to different entities or fields. They define independent relationships, and JPA processes them separately. Misconfiguration (e.g., same target entity) may cause runtime errors.  

⚠️ **Warning**: Ensure distinct target entities to avoid mapping errors.
</details>  

#### Java Code
```java
import jakarta.persistence.*;
import java.util.List;

@Entity
public class User {
    @Id
    private Long id;

    @OneToOne
    private Address address; // One-to-one with Address

    @OneToMany
    private List<Order> orders; // One-to-many with Order
}
```  

#### Outcome
<span style="color: green">`User` has a one-to-one with `Address` and a one-to-many with `Order`.</span>

| ℹ️ **Tip** |  
| Configure `mappedBy` and foreign keys correctly for each relationship. |

---

### Q27: What’s the purpose of `@Autowired` annotation? ![Medium](https://img.shields.io/badge/Difficulty-Medium-yellow)
> 📌 **Key Concept**: Dependency injection

<details><summary>📝 **Explanation**</summary>  
`@Autowired` enables automatic dependency injection by type, applicable to fields, constructors, or setters. It resolves beans from the Spring context, throwing `NoSuchBeanDefinitionException` if no matching bean exists (unless `required = false`).  

⚠️ **Warning**: Missing beans cause startup failures unless handled.
</details>  

#### Java Code
```java
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class ClientBean {
    @Autowired
    private MyService myService;

    public String execute() {
        return myService.serve();
    }
}

@Component
public class MyService {
    public String serve() {
        return "Service executed";
    }
}
```  

#### Outcome
<span style="color: green">`myService` is injected if a `MyService` bean exists; otherwise, Spring throws `NoSuchBeanDefinitionException`.</span>

| ℹ️ **Tip** |  
| Use `required = false` for optional dependencies. |

---

### Q28: What’s the difference between `@Bean` and `@Component`? ![Medium](https://img.shields.io/badge/Difficulty-Medium-yellow)
> 📌 **Key Concept**: Bean definition methods

<details><summary>📝 **Explanation**</summary>  
- `@Bean`: Used in `@Configuration` classes to define beans programmatically, offering control over instantiation.  
- `@Component`: A stereotype annotation for automatic component scanning, applied directly to classes.  

⚠️ **Warning**: `@Bean` requires a `@Configuration` class to work correctly.
</details>  

#### Java Code
```java
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Configuration
public class AppConfig {
    @Bean
    public MyService myService() {
        return new MyService();
    }
}

@Component
public class MyService {
    public String serve() {
        return "Service";
    }
}
```  

#### Outcome
<span style="color: green">`@Bean` enables programmatic bean creation; `@Component` relies on scanning.</span>

| ℹ️ **Tip** |  
| Use `@Bean` for third-party classes or custom setup. |

---

### Q29: What happens if a Spring bean is prototype-scoped? ![Medium](https://img.shields.io/badge/Difficulty-Medium-yellow)
> 📌 **Key Concept**: Bean scope

<details><summary>📝 **Explanation**</summary>  
A prototype-scoped bean (`@Scope("prototype")`) creates a new instance each time it’s requested, unlike the default singleton scope. Useful for stateful beans but not managed post-creation (no destruction callbacks).  

⚠️ **Warning**: Prototype beans increase memory usage if not managed carefully.
</details>  

#### Java Code
```java
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

@Component
@Scope("prototype")
public class PrototypeBean {
    private int counter = 0;

    public void increment() {
        counter++;
    }

    public int getCounter() {
        return counter;
    }
}

@Component
public class ClientBean {
    @Autowired
    private PrototypeBean bean1;
    @Autowired
    private PrototypeBean bean2;

    public int test() {
        bean1.increment();
        return bean2.getCounter(); // Different instance
    }
}
```  

#### Outcome
<span style="color: green">`bean1` and `bean2` are distinct instances; `bean2.getCounter()` returns 0.</span>

| ℹ️ **Tip** |  
| Use prototype scope for stateful beans requiring unique instances. |

---

### Q30: What’s the role of `ApplicationContext` in Spring? ![Medium](https://img.shields.io/badge/Difficulty-Medium-yellow)
> 📌 **Key Concept**: IoC container

<details><summary>📝 **Explanation**</summary>  
`ApplicationContext` is Spring’s IoC container, managing bean lifecycle, dependencies, and features like event publishing and internationalization.  

⚠️ **Warning**: Misconfiguring the context may prevent bean access.
</details>  

#### Java Code
```java
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        MyService service = context.getBean(MyService.class);
        System.out.println(service.serve());
    }
}
```  

#### Outcome
<span style="color: green">`ApplicationContext` initializes beans from `AppConfig`, enabling access via `getBean`.</span>

| ℹ️ **Tip** |  
| Use `ApplicationContext` for programmatic access to beans and events. |

---

### Q31: How do you handle circular dependencies in Spring? ![Medium](https://img.shields.io/badge/Difficulty-Medium-yellow)
> 📌 **Key Concept**: Circular dependency resolution

<details><summary>📝 **Explanation**</summary>  
Circular dependencies (e.g., Bean A needs Bean B, and B needs A) cause a `BeanCurrentlyInCreationException`. Resolve using:  
- `@Lazy` on one dependency.  
- Setter injection instead of constructor injection.  
- Redesign to avoid circularity.  

⚠️ **Warning**: Unresolved circular dependencies prevent application startup.
</details>  

#### Java Code
```java
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

@Component
public class BeanA {
    private final BeanB beanB;

    @Autowired
    public BeanA(@Lazy BeanB beanB) {
        this.beanB = beanB;
    }
}

@Component
public class BeanB {
    @Autowired
    private BeanA beanA;
}
```  

#### Outcome
<span style="color: green">`@Lazy` delays `BeanB` initialization, resolving the cycle.</span>

| ℹ️ **Tip** |  
| Redesign to eliminate circular dependencies for cleaner architecture. |

---

### Q32: What’s the purpose of `@Transactional` annotation? ![Medium](https://img.shields.io/badge/Difficulty-Medium-yellow)
> 📌 **Key Concept**: Transaction management

<details><summary>📝 **Explanation**</summary>  
`@Transactional` manages database transactions, ensuring ACID properties. It defines transaction boundaries, isolation, and propagation for methods or classes.  

⚠️ **Warning**: Misconfiguring transaction boundaries may lead to data inconsistencies.
</details>  

#### Java Code
```java
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    @Transactional
    public void saveUser(User user) {
        userRepository.save(user);
        // Commits or rolls back
    }
}
```  

#### Outcome
<span style="color: green">`saveUser` runs in a transaction; exceptions trigger rollback.</span>

| ℹ️ **Tip** |  
| Use `@Transactional` at the service layer for consistent transaction management. |

---

### Q33: How do you customize Spring Boot’s embedded Tomcat? ![Medium](https://img.shields.io/badge/Difficulty-Medium-yellow)
> 📌 **Key Concept**: Server customization

<details><summary>📝 **Explanation**</summary>  
Customize Tomcat via:  
- Properties in `application.properties` (e.g., `server.tomcat.*`).  
- `WebServerFactoryCustomizer` for programmatic changes.  

⚠️ **Warning**: Incorrect settings may degrade server performance.
</details>  

#### Properties File
```properties
server.tomcat.max-threads=400
server.tomcat.accesslog.enabled=true
```  

#### Java Code
```java
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;

@Component
public class TomcatCustomizer implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {
    @Override
    public void customize(TomcatServletWebServerFactory factory) {
        factory.setPort(8082);
    }
}
```  

#### Outcome
<span style="color: green">Tomcat runs with 400 max threads, access logging, and port 8082.</span>

| ℹ️ **Tip** |  
| Use properties for simple tweaks and `WebServerFactoryCustomizer` for complex customization. |

---

### Q34: What’s the difference between `@PathVariable` and `@RequestParam`? ![Medium](https://img.shields.io/badge/Difficulty-Medium-yellow)
> 📌 **Key Concept**: Parameter extraction

<details><summary>📝 **Explanation**</summary>  
- `@PathVariable`: Extracts values from the URL path (e.g., `/users/{id}`).  
- `@RequestParam`: Extracts query parameters (e.g., `/users?name=John`).  

⚠️ **Warning**: Missing required parameters cause 400 errors.
</details>  

#### Java Code
```java
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @GetMapping("/users/{id}")
    public String getUserById(@PathVariable Long id) {
        return "User ID: " + id;
    }

    @GetMapping("/users")
    public String getUserByName(@RequestParam String name) {
        return "User Name: " + name;
    }
}
```  

#### Outcome
<span style="color: green">`/users/123` extracts `id=123`; `/users?name=John` extracts `name=John`.</span>

| ℹ️ **Tip** |  
| Use `@PathVariable` for RESTful URLs and `@RequestParam` for query-based filtering. |

---

### Q35: How do you handle exceptions in a Spring Boot REST API? ![Medium](https://img.shields.io/badge/Difficulty-Medium-yellow)
> 📌 **Key Concept**: Global exception handling

<details><summary>📝 **Explanation**</summary>  
Use `@ControllerAdvice` with `@ExceptionHandler` to handle exceptions globally, returning custom responses.  

⚠️ **Warning**: Unhandled exceptions may expose sensitive information.
</details>  

#### Java Code
```java
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
```  

#### Outcome
<span style="color: green">`IllegalArgumentException` triggers a 400 response with the exception message.</span>

| ℹ️ **Tip** |  
| Centralize error handling with `@ControllerAdvice` for consistent responses. |

---

### Q36: What’s the purpose of `@EnableJpaRepositories`? ![Advanced](https://img.shields.io/badge/Difficulty-Advanced-red)
> 📌 **Key Concept**: Repository activation

<details><summary>📝 **Explanation**</summary>  
`@EnableJpaRepositories` enables Spring Data JPA repositories, scanning for interfaces extending `JpaRepository` and creating implementations.  

⚠️ **Warning**: Incorrect `basePackages` may prevent repository detection.
</details>  

#### Java Code
```java
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.example.repository")
public class JpaConfig {}
```  

#### Outcome
<span style="color: green">Spring creates proxy implementations for repositories in `com.example.repository`.</span>

| ℹ️ **Tip** |  
| Specify `basePackages` to limit repository scanning. |

---

### Q37: How do you implement pagination in Spring Data JPA? ![Advanced](https://img.shields.io/badge/Difficulty-Advanced-red)
> 📌 **Key Concept**: Paginated queries

<details><summary>📝 **Explanation**</summary>  
Use `Pageable` in repository methods to return paginated results, typically with `Page` or `Slice`.  

⚠️ **Warning**: Large page sizes may impact performance.
</details>  

#### Java Code
```java
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

public interface UserRepository extends JpaRepository<User, Long> {
    Page<User> findByNameContaining(String name, Pageable pageable);
}

@RestController
public class UserController {
    @Autowired
    private UserRepository repository;

    @GetMapping("/users")
    public Page<User> getUsers(@RequestParam String name, Pageable pageable) {
        return repository.findByNameContaining(name, pageable);
    }
}
```  

#### Outcome
<span style="color: green">`/users?name=John&page=0&size=10` returns 10 users per page.</span>

| ℹ️ **Tip** |  
| Use `Page` for total count or `Slice` for lighter pagination checks. |

---

### Q38: What’s the difference between `CrudRepository` and `JpaRepository`? ![Advanced](https://img.shields.io/badge/Difficulty-Advanced-red)
> 📌 **Key Concept**: Repository interfaces

<details><summary>📝 **Explanation**</summary>  
- `CrudRepository`: Offers basic CRUD operations (create, read, update, delete).  
- `JpaRepository`: Extends `CrudRepository`, adding JPA-specific methods like `flush()`, `deleteInBatch`, and pagination.  

⚠️ **Warning**: `JpaRepository` includes more methods, increasing complexity.
</details>  

#### Java Code
```java
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCrudRepository extends CrudRepository<User, Long> {}
public interface UserJpaRepository extends JpaRepository<User, Long> {}
```  

#### Outcome
<span style="color: green">`UserJpaRepository` includes all `CrudRepository` methods plus JPA features.</span>

| ℹ️ **Tip** |  
| Use `CrudRepository` for simple CRUD; `JpaRepository` for advanced JPA features. |

---

### Q39: How do you configure multiple data sources in Spring Boot? ![Advanced](https://img.shields.io/badge/Difficulty-Advanced-red)
> 📌 **Key Concept**: Multi-database setup

<details><summary>📝 **Explanation**</summary>  
Define multiple `DataSource` beans, configure `EntityManagerFactory` and `TransactionManager` for each, and use `@EntityScan` and `@EnableJpaRepositories` to separate entities and repositories.  

⚠️ **Warning**: Misconfiguring data sources may cause transaction issues.
</details>  

#### Java Code
```java
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.primary")
    public DataSource primaryDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.secondary")
    public DataSource secondaryDataSource() {
        return DataSourceBuilder.create().build();
    }
}
```  

#### Outcome
<span style="color: green">Two `DataSource` beans are configured for different databases.</span>

| ℹ️ **Tip** |  
| Use separate `EntityManagerFactory` instances for each data source. |

---

### Q40: What happens if you use `@Transactional` on a private method? ![Advanced](https://img.shields.io/badge/Difficulty-Advanced-red)
> 📌 **Key Concept**: Transaction scope

<details><summary>📝 **Explanation**</summary>  
`@Transactional` on a private method is ignored, as Spring’s proxies (CGLIB or JDK dynamic) only intercept public methods. Use `@Transactional` on public methods or self-inject for private transactions.  

⚠️ **Warning**: Ignoring `@Transactional` may lead to uncommitted changes.
</details>  

#### Java Code
```java
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    @Transactional
    private void saveUser(User user) { // Ignored
        userRepository.save(user);
    }
}
```  

#### Outcome
<span style="color: green">No transaction is created for `saveUser`.</span>

| ℹ️ **Tip** |  
| Move `@Transactional` to a public method for proper transaction management. |

---

### Q41: How do you secure a Spring Boot REST API? ![Medium](https://img.shields.io/badge/Difficulty-Medium-yellow)
> 📌 **Key Concept**: API security

<details><summary>📝 **Explanation**</summary>  
Use Spring Security with:  
- `spring-boot-starter-security` dependency.  
- Configure authentication (e.g., JWT, OAuth2).  
- Use `@PreAuthorize` or `HttpSecurity` to restrict endpoints.  

⚠️ **Warning**: Insecure APIs may expose sensitive data.
</details>  

#### Java Code
```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/admin/**").hasRole("ADMIN")
            .anyRequest().authenticated()
        ).httpBasic();
        return http.build();
    }
}
```  

#### Outcome
<span style="color: green">`/api/admin` requires the ADMIN role; other endpoints require authentication.</span>

| ℹ️ **Tip** |  
| Use JWT for stateless API authentication. |

---

### Q42: What’s the purpose of `@Profile` in Spring? ![Medium](https://img.shields.io/badge/Difficulty-Medium-yellow)
> 📌 **Key Concept**: Environment-specific configuration

<details><summary>📝 **Explanation**</summary>  
`@Profile` activates beans or configurations for specific environments (e.g., dev, prod), set via `spring.profiles.active`.  

⚠️ **Warning**: Incorrect profile settings may load wrong configurations.
</details>  

#### Java Code
```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import javax.sql.DataSource;

@Configuration
@Profile("dev")
public class DevConfig {
    @Bean
    public DataSource devDataSource() {
        return new DriverManagerDataSource("jdbc:h2:mem:dev");
    }
}
```  

#### Outcome
<span style="color: green">`devDataSource` is created only when `spring.profiles.active=dev`.</span>

| ℹ️ **Tip** |  
| Set profiles via environment variables for flexibility. |

---

### Q43: How do you implement caching in Spring Boot? ![Medium](https://img.shields.io/badge/Difficulty-Medium-yellow)
> 📌 **Key Concept**: Caching

<details><summary>📝 **Explanation**</summary>  
Use `@EnableCaching` and annotations like `@Cacheable`, `@CachePut`, or `@CacheEvict` with a cache provider (e.g., EhCache, Redis).  

⚠️ **Warning**: Improper cache configuration may cause stale data.
</details>  

#### Java Code
```java
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

@Configuration
@EnableCaching
public class CacheConfig {}

@Service
public class UserService {
    @Cacheable("users")
    public User getUser(Long id) {
        return userRepository.findById(id).orElse(null);
    }
}
```  

#### Outcome
<span style="color: green">`getUser` caches results, reducing database calls.</span>

| ℹ️ **Tip** |  
| Use Redis for distributed caching in production. |

---

### Q44: What’s the difference between `@SpringBootTest` and `@WebMvcTest`? ![Medium](https://img.shields.io/badge/Difficulty-Medium-yellow)
> 📌 **Key Concept**: Testing scopes

<details><summary>📝 **Explanation**</summary>  
- `@SpringBootTest`: Loads the full application context, ideal for integration tests.  
- `@WebMvcTest`: Loads only MVC components (controllers, filters), faster for testing REST endpoints.  

⚠️ **Warning**: `@SpringBootTest` is slower due to full context loading.
</details>  

#### Java Code
```java
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class ApplicationTests {
    @Test
    void contextLoads() {}
}

@WebMvcTest(UserController.class)
class UserControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetUser() throws Exception {
        mockMvc.perform(get("/users/1")).andExpect(status().isOk());
    }
}
```  

#### Outcome
<span style="color: green">`@SpringBootTest` tests the entire app; `@WebMvcTest` focuses on MVC layers.</span>

| ℹ️ **Tip** |  
| Use `@WebMvcTest` for controller-focused unit tests. |

---

### Q45: How do you schedule tasks in Spring Boot? ![Medium](https://img.shields.io/badge/Difficulty-Medium-yellow)
> 📌 **Key Concept**: Task scheduling

<details><summary>📝 **Explanation**</summary>  
Use `@EnableScheduling` and `@Scheduled` to run tasks at fixed intervals or cron expressions.  

⚠️ **Warning**: Heavy tasks may block the scheduler; use `@Async` for long-running tasks.
</details>  

#### Java Code
```java
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.Date;

@Configuration
@EnableScheduling
public class SchedulerConfig {}

@Service
public class TaskService {
    @Scheduled(fixedRate = 5000)
    public void runTask() {
        System.out.println("Task executed at " + new Date());
    }
}
```  

#### Outcome
<span style="color: green">`runTask` executes every 5 seconds.</span>

| ℹ️ **Tip** |  
| Use cron expressions for complex scheduling patterns. |

---

### Q46: What’s the purpose of `@ConditionalOnProperty`? ![Medium](https://img.shields.io/badge/Difficulty-Medium-yellow)
> 📌 **Key Concept**: Conditional bean creation

<details><summary>📝 **Explanation**</summary>  
`@ConditionalOnProperty` creates a bean only if a property matches a specified value, enabling conditional configuration.  

⚠️ **Warning**: Missing properties may prevent bean creation.
</details>  

#### Java Code
```java
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeatureConfig {
    @Bean
    @ConditionalOnProperty(name = "feature.enabled", havingValue = "true")
    public MyService myService() {
        return new MyService();
    }
}
```  

#### Outcome
<span style="color: green">`myService` is created only if `feature.enabled=true`.</span>

| ℹ️ **Tip** |  
| Combine with `application.properties` for dynamic feature toggling. |

---

### Q47: How do you implement custom validation in Spring? ![Medium](https://img.shields.io/badge/Difficulty-Medium-yellow)
> 📌 **Key Concept**: Custom validation

<details><summary>📝 **Explanation**</summary>  
Create a custom annotation with a `ConstraintValidator` to enforce validation rules.  

⚠️ **Warning**: Invalid validator logic may reject valid data.
</details>  

#### Java Code
```java
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CustomValidator.class)
public @interface CustomValid {
    String message() default "Invalid value";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

public class CustomValidator implements ConstraintValidator<CustomValid, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && value.startsWith("valid");
    }
}

public class UserDTO {
    @CustomValid
    private String code;
}
```  

#### Outcome
<span style="color: green">`code` must start with "valid", or a validation error occurs.</span>

| ℹ️ **Tip** |  
| Use custom validators for domain-specific rules. |

---

### Q48: What happens if you use `@Async` on a method? ![Medium](https://img.shields.io/badge/Difficulty-Medium-yellow)
> 📌 **Key Concept**: Asynchronous execution

<details><summary>📝 **Explanation**</summary>  
`@Async` runs a method asynchronously in a separate thread, requiring `@EnableAsync`. The method must be public and return `void` or `Future`.  

⚠️ **Warning**: `@Async` requires `@EnableAsync` to function.
</details>  

#### Java Code
```java
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.util.concurrent.CompletableFuture;

@Configuration
@EnableAsync
public class AsyncConfig {}

@Service
public class AsyncService {
    @Async
    public CompletableFuture<String> asyncTask() throws InterruptedException {
        Thread.sleep(1000);
        return CompletableFuture.completedFuture("Done");
    }
}
```  

#### Outcome
<span style="color: green">`asyncTask` runs in a separate thread, not blocking the caller.</span>

| ℹ️ **Tip** |  
| Use `CompletableFuture` for asynchronous results. |

---

### Q49: How do you monitor a Spring Boot application? ![Medium](https://img.shields.io/badge/Difficulty-Medium-yellow)
> 📌 **Key Concept**: Application monitoring

<details><summary>📝 **Explanation**</summary>  
Use Spring Boot Actuator to expose endpoints (e.g., `/actuator/health`, `/actuator/metrics`) for monitoring.  

⚠️ **Warning**: Exposing sensitive endpoints requires security measures.
</details>  

#### XML Code (POM)
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```  

#### Properties File
```properties
management.endpoints.web.exposure.include=health,metrics
```  

#### Outcome
<span style="color: green">Access `/actuator/health` for status and `/actuator/metrics` for metrics.</span>

| ℹ️ **Tip** |  
| Secure actuator endpoints with Spring Security. |

---

### Q50: What’s the purpose of `@Value` annotation? ![Medium](https://img.shields.io/badge/Difficulty-Medium-yellow)
> 📌 **Key Concept**: Property injection

<details><summary>📝 **Explanation**</summary>  
`@Value` injects values from properties files, environment variables, or defaults into fields or parameters.  

⚠️ **Warning**: Missing properties may cause injection failures.
</details>  

#### Properties File
```properties
app.name=MyApp
```  

#### Java Code
```java
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConfigBean {
    @Value("${app.name}")
    private String appName;

    public String getAppName() {
        return appName;
    }
}
```  

#### Outcome
<span style="color: green">`appName` is set to "MyApp" from `application.properties`.</span>

| ℹ️ **Tip** |  
| Use default values (e.g., `${app.name:Default}`) to handle missing properties. |

---

## Summary Table
| Question # | Topic Area       | Key Concept                     | Difficulty |  
|------------|------------------|---------------------------------|------------|  
| Q1-Q6      | Spring Core      | Bean Management, Dependency Injection | Medium     |  
| Q7-Q12     | Spring Boot      | Application Setup, Customization | Medium     |  
| Q13-Q17    | REST Controllers | REST API Development, Error Handling | Medium     |  
| Q18-Q26    | Spring Data JPA  | Entity Relationships, Queries   | Advanced   |  
| Q27-Q50    | Mixed Topics     | Annotations, Testing, Advanced Features | Medium-Advanced |  

## References
- [Spring Framework Documentation](https://docs.spring.io/spring-framework/docs/current/reference/html/)
- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Spring Data JPA Documentation](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
- [Baeldung Spring Tutorials](https://www.baeldung.com/spring)

> 🚀 **Next Steps**: Practice the code examples and review outcomes to master Spring and Spring Boot for your interview.