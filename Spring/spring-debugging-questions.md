# Spring Framework and Spring Boot Error Handling and Debugging Questions

This document contains interview questions related to error handling and debugging in Spring Framework and Spring Boot, suitable for candidates with 3-5 years of experience. These questions focus on common bugs, exception flows, Spring-specific stack traces, and troubleshooting techniques.

## Table of Contents
1. [Exception Handling Patterns](#exception-handling-patterns)
2. [Common Spring Boot Errors](#common-spring-boot-errors)
3. [Debugging Techniques](#debugging-techniques)
4. [Logging Best Practices](#logging-best-practices)
5. [Spring Boot Actuator for Troubleshooting](#spring-boot-actuator-for-troubleshooting)
6. [Transaction Management Issues](#transaction-management-issues)
7. [Spring Security Debugging](#spring-security-debugging)
8. [Microservices Debugging](#microservices-debugging)

## Exception Handling Patterns

### Q1: Explain the different approaches to handle exceptions in a Spring MVC or Spring Boot REST application.

**Answer:** Spring provides several approaches to handle exceptions in web applications:

1. **Controller-level exception handling with @ExceptionHandler**:
   ```java
   @RestController
   public class UserController {
       // Regular controller methods
       
       @ExceptionHandler(UserNotFoundException.class)
       public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex) {
           ErrorResponse error = new ErrorResponse(
               HttpStatus.NOT_FOUND.value(),
               ex.getMessage(),
               LocalDateTime.now()
           );
           return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
       }
   }
   ```
   This approach is good for controller-specific exception handling but doesn't scale well across multiple controllers.

2. **Global exception handling with @ControllerAdvice or @RestControllerAdvice**:
   ```java
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
       
       @ExceptionHandler(ValidationException.class)
       public ResponseEntity<ErrorResponse> handleValidation(ValidationException ex) {
           ErrorResponse error = new ErrorResponse(
               HttpStatus.BAD_REQUEST.value(),
               ex.getMessage(),
               LocalDateTime.now()
           );
           return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
       }
       
       @ExceptionHandler(Exception.class)
       public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
           ErrorResponse error = new ErrorResponse(
               HttpStatus.INTERNAL_SERVER_ERROR.value(),
               "An unexpected error occurred",
               LocalDateTime.now()
           );
           return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
       }
   }
   ```
   This approach provides centralized exception handling across all controllers.

3. **HandlerExceptionResolver implementation**:
   ```java
   @Component
   public class CustomExceptionResolver implements HandlerExceptionResolver {
       @Override
       public ModelAndView resolveException(
               HttpServletRequest request, 
               HttpServletResponse response, 
               Object handler, 
               Exception ex) {
           
           if (ex instanceof ResourceNotFoundException) {
               response.setStatus(HttpStatus.NOT_FOUND.value());
               // Handle the exception
           }
           
           // Return appropriate ModelAndView or null
           return new ModelAndView();
       }
   }
   ```
   This is a lower-level approach that gives more control but requires more boilerplate code.

4. **@ResponseStatus annotation on custom exceptions**:
   ```java
   @ResponseStatus(HttpStatus.NOT_FOUND)
   public class ResourceNotFoundException extends RuntimeException {
       public ResourceNotFoundException(String message) {
           super(message);
       }
   }
   ```
   This is a simple approach for mapping exceptions to HTTP status codes but provides limited customization.

5. **Spring Boot's ErrorController implementation**:
   ```java
   @Controller
   public class CustomErrorController implements ErrorController {
       @RequestMapping("/error")
       public ResponseEntity<ErrorResponse> handleError(HttpServletRequest request) {
           // Get error details from request attributes
           Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
           Object message = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
           
           ErrorResponse error = new ErrorResponse(
               status != null ? Integer.parseInt(status.toString()) : 500,
               message != null ? message.toString() : "Unknown error",
               LocalDateTime.now()
           );
           
           return new ResponseEntity<>(error, 
               HttpStatus.valueOf(error.getStatus()));
       }
   }
   ```
   This approach handles errors that occur outside of controllers, like 404s or filter exceptions.

6. **Exception handling for validation errors**:
   ```java
   @RestControllerAdvice
   public class ValidationExceptionHandler {
       @ExceptionHandler(MethodArgumentNotValidException.class)
       public ResponseEntity<ValidationErrorResponse> handleValidationExceptions(
               MethodArgumentNotValidException ex) {
           
           Map<String, String> errors = new HashMap<>();
           ex.getBindingResult().getFieldErrors().forEach(error -> 
               errors.put(error.getField(), error.getDefaultMessage()));
           
           ValidationErrorResponse response = new ValidationErrorResponse(
               HttpStatus.BAD_REQUEST.value(),
               "Validation failed",
               LocalDateTime.now(),
               errors
           );
           
           return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
       }
   }
   ```
   This approach specifically handles validation errors from @Valid annotations.

Best practices for exception handling in Spring:

1. **Create a hierarchy of custom exceptions**:
   ```java
   // Base exception
   public abstract class BaseException extends RuntimeException {
       private final HttpStatus status;
       
       protected BaseException(String message, HttpStatus status) {
           super(message);
           this.status = status;
       }
       
       public HttpStatus getStatus() {
           return status;
       }
   }
   
   // Specific exceptions
   public class ResourceNotFoundException extends BaseException {
       public ResourceNotFoundException(String resource, String id) {
           super(String.format("%s not found with id: %s", resource, id), 
                 HttpStatus.NOT_FOUND);
       }
   }
   
   public class BadRequestException extends BaseException {
       public BadRequestException(String message) {
           super(message, HttpStatus.BAD_REQUEST);
       }
   }
   ```

2. **Use consistent error response structure**:
   ```java
   public class ErrorResponse {
       private int status;
       private String message;
       private LocalDateTime timestamp;
       private String path;
       private List<String> errors;
       
       // Constructors, getters, setters
   }
   ```

3. **Log exceptions appropriately**:
   ```java
   @ExceptionHandler(Exception.class)
   public ResponseEntity<ErrorResponse> handleException(Exception ex, WebRequest request) {
       // Log the exception
       logger.error("Unhandled exception", ex);
       
       // Return appropriate response
       ErrorResponse error = new ErrorResponse(/* ... */);
       return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
   }
   ```

4. **Consider security implications**:
   - Don't expose sensitive information in error messages
   - Don't return stack traces to clients
   - Use different error details for development vs. production

### Q2: What are the common pitfalls when implementing exception handling in Spring applications and how would you avoid them?

**Answer:** Common pitfalls in Spring exception handling and their solutions:

1. **Exposing Sensitive Information**:
   
   **Pitfall**: Returning exception stack traces or sensitive details to clients.
   ```java
   // BAD PRACTICE
   @ExceptionHandler(Exception.class)
   public ResponseEntity<String> handleException(Exception ex) {
       StringWriter sw = new StringWriter();
       ex.printStackTrace(new PrintWriter(sw));
       return new ResponseEntity<>(sw.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
   }
   ```
   
   **Solution**: Create sanitized error responses and log the full details server-side.
   ```java
   @ExceptionHandler(Exception.class)
   public ResponseEntity<ErrorResponse> handleException(Exception ex) {
       // Log the full exception for debugging
       logger.error("Unhandled exception", ex);
       
       // Return sanitized response to client
       ErrorResponse error = new ErrorResponse(
           HttpStatus.INTERNAL_SERVER_ERROR.value(),
           "An unexpected error occurred",
           LocalDateTime.now()
       );
       return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
   }
   ```

2. **Inconsistent Error Response Structure**:
   
   **Pitfall**: Different error handlers returning different response formats.
   
   **Solution**: Define a common error response structure and use it consistently.
   ```java
   // Define a common error response class
   public class ErrorResponse {
       private int status;
       private String message;
       private LocalDateTime timestamp;
       private String path;
       
       // Constructors, getters, setters
   }
   
   // Use it consistently across all exception handlers
   @RestControllerAdvice
   public class GlobalExceptionHandler {
       @ExceptionHandler(ResourceNotFoundException.class)
       public ResponseEntity<ErrorResponse> handleResourceNotFound(
               ResourceNotFoundException ex, WebRequest request) {
           ErrorResponse error = new ErrorResponse(
               HttpStatus.NOT_FOUND.value(),
               ex.getMessage(),
               LocalDateTime.now(),
               request.getDescription(false)
           );
           return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
       }
       
       @ExceptionHandler(BadRequestException.class)
       public ResponseEntity<ErrorResponse> handleBadRequest(
               BadRequestException ex, WebRequest request) {
           // Same structure, different status and message
           ErrorResponse error = new ErrorResponse(/* ... */);
           return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
       }
   }
   ```

3. **Missing Exception Types**:
   
   **Pitfall**: Not handling all possible exceptions, leading to default error pages or inconsistent responses.
   
   **Solution**: Implement a catch-all handler and categorize exceptions properly.
   ```java
   @RestControllerAdvice
   public class GlobalExceptionHandler {
       // Specific handlers for known exceptions
       // ...
       
       // Catch-all for any unhandled exceptions
       @ExceptionHandler(Exception.class)
       public ResponseEntity<ErrorResponse> handleUnknownException(
               Exception ex, WebRequest request) {
           logger.error("Unhandled exception", ex);
           
           ErrorResponse error = new ErrorResponse(
               HttpStatus.INTERNAL_SERVER_ERROR.value(),
               "An unexpected error occurred",
               LocalDateTime.now(),
               request.getDescription(false)
           );
           return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
       }
   }
   ```

4. **Ignoring Validation Errors**:
   
   **Pitfall**: Not properly handling validation errors from @Valid annotations.
   
   **Solution**: Add specific handlers for validation exceptions.
   ```java
   @RestControllerAdvice
   public class ValidationExceptionHandler {
       @ExceptionHandler(MethodArgumentNotValidException.class)
       public ResponseEntity<ValidationErrorResponse> handleValidationExceptions(
               MethodArgumentNotValidException ex, WebRequest request) {
           
           Map<String, String> fieldErrors = new HashMap<>();
           ex.getBindingResult().getFieldErrors().forEach(error -> 
               fieldErrors.put(error.getField(), error.getDefaultMessage()));
           
           ValidationErrorResponse response = new ValidationErrorResponse(
               HttpStatus.BAD_REQUEST.value(),
               "Validation failed",
               LocalDateTime.now(),
               request.getDescription(false),
               fieldErrors
           );
           
           return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
       }
       
       @ExceptionHandler(ConstraintViolationException.class)
       public ResponseEntity<ValidationErrorResponse> handleConstraintViolation(
               ConstraintViolationException ex, WebRequest request) {
           // Similar handling for constraint violations
           // ...
       }
   }
   ```

5. **Conflicting Exception Handlers**:
   
   **Pitfall**: Multiple handlers that could process the same exception, leading to unpredictable behavior.
   
   **Solution**: Use a clear hierarchy and be specific about which exceptions each handler processes.
   ```java
   @RestControllerAdvice
   public class GlobalExceptionHandler {
       // More specific exception handler
       @ExceptionHandler(ResourceNotFoundException.class)
       public ResponseEntity<ErrorResponse> handleResourceNotFound(/* ... */) {
           // ...
       }
       
       // Less specific exception handler
       @ExceptionHandler(RuntimeException.class)
       public ResponseEntity<ErrorResponse> handleRuntimeException(/* ... */) {
           // ...
       }
   }
   ```

6. **Not Handling Exceptions from Filters and Interceptors**:
   
   **Pitfall**: Exceptions thrown in filters or interceptors bypass @ExceptionHandler methods.
   
   **Solution**: Implement a custom ErrorController or use a filter-level exception handling mechanism.
   ```java
   @Component
   public class ExceptionHandlingFilter extends OncePerRequestFilter {
       @Override
       protected void doFilterInternal(HttpServletRequest request, 
                                      HttpServletResponse response, 
                                      FilterChain filterChain) 
               throws ServletException, IOException {
           try {
               filterChain.doFilter(request, response);
           } catch (Exception e) {
               // Handle the exception
               response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
               response.setContentType(MediaType.APPLICATION_JSON_VALUE);
               
               ErrorResponse error = new ErrorResponse(/* ... */);
               response.getWriter().write(new ObjectMapper().writeValueAsString(error));
           }
       }
   }
   ```

7. **Forgetting Content Negotiation**:
   
   **Pitfall**: Returning error responses in a format the client doesn't accept.
   
   **Solution**: Respect the Accept header and return errors in the appropriate format.
   ```java
   @RestControllerAdvice
   public class ContentNegotiationAwareExceptionHandler {
       @ExceptionHandler(ResourceNotFoundException.class)
       public ResponseEntity<?> handleResourceNotFound(
               ResourceNotFoundException ex, WebRequest request) {
           
           ErrorResponse error = new ErrorResponse(/* ... */);
           
           // Check if client accepts XML
           if (request.getHeader("Accept").contains(MediaType.APPLICATION_XML_VALUE)) {
               // Return XML response
               return ResponseEntity
                   .status(HttpStatus.NOT_FOUND)
                   .contentType(MediaType.APPLICATION_XML)
                   .body(error);
           }
           
           // Default to JSON
           return ResponseEntity
               .status(HttpStatus.NOT_FOUND)
               .contentType(MediaType.APPLICATION_JSON)
               .body(error);
       }
   }
   ```

8. **Not Handling Async Exceptions**:
   
   **Pitfall**: Exceptions in async methods (@Async) or CompletableFuture are not caught by standard exception handlers.
   
   **Solution**: Use AsyncUncaughtExceptionHandler and proper CompletableFuture exception handling.
   ```java
   @Configuration
   public class AsyncConfig implements AsyncConfigurer {
       @Override
       public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
           return new CustomAsyncExceptionHandler();
       }
   }
   
   public class CustomAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {
       @Override
       public void handleUncaughtException(Throwable ex, Method method, Object... params) {
           logger.error("Async exception in method: " + method.getName(), ex);
           // Additional handling
       }
   }
   
   // For CompletableFuture
   @Service
   public class AsyncService {
       public CompletableFuture<Result> processAsync() {
           return CompletableFuture.supplyAsync(() -> {
               // Processing logic
           }).exceptionally(ex -> {
               logger.error("Async processing failed", ex);
               // Return fallback or rethrow
               return fallbackResult();
           });
       }
   }
   ```

9. **Ignoring Exception Hierarchy**:
   
   **Pitfall**: Not considering the exception hierarchy when defining handlers.
   
   **Solution**: Design a clear exception hierarchy and handle exceptions at the appropriate level.
   ```java
   // Base exception
   public abstract class BaseException extends RuntimeException {
       private final HttpStatus status;
       
       protected BaseException(String message, HttpStatus status) {
           super(message);
           this.status = status;
       }
       
       public HttpStatus getStatus() {
           return status;
       }
   }
   
   // Specific exceptions
   public class ResourceNotFoundException extends BaseException {
       public ResourceNotFoundException(String resource, String id) {
           super(String.format("%s not found with id: %s", resource, id), 
                 HttpStatus.NOT_FOUND);
       }
   }
   
   // Handler that uses the hierarchy
   @RestControllerAdvice
   public class HierarchicalExceptionHandler {
       @ExceptionHandler(BaseException.class)
       public ResponseEntity<ErrorResponse> handleBaseException(BaseException ex) {
           ErrorResponse error = new ErrorResponse(
               ex.getStatus().value(),
               ex.getMessage(),
               LocalDateTime.now()
           );
           return new ResponseEntity<>(error, ex.getStatus());
       }
   }
   ```

10. **Not Testing Exception Handling**:
    
    **Pitfall**: Assuming exception handling works without testing it.
    
    **Solution**: Write tests specifically for exception scenarios.
    ```java
    @WebMvcTest(UserController.class)
    public class UserControllerExceptionTest {
        @Autowired
        private MockMvc mockMvc;
        
        @MockBean
        private UserService userService;
        
        @Test
        public void whenUserNotFound_thenReturns404() throws Exception {
            // Arrange
            when(userService.findById(anyLong()))
                .thenThrow(new ResourceNotFoundException("User", "1"));
            
            // Act & Assert
            mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("User not found with id: 1"));
        }
    }
    ```

By avoiding these pitfalls, you can create a robust, consistent, and secure exception handling system in your Spring applications.

### Q3: How would you implement a custom error page for a specific HTTP status code in a Spring Boot application?

**Answer:** There are several ways to implement custom error pages for specific HTTP status codes in Spring Boot:

1. **Using ErrorController (Spring Boot 2.x+)**:

```java
@Controller
public class CustomErrorController implements ErrorController {
    
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        // Get error status code
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        
        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            
            // Add error details to model
            model.addAttribute("statusCode", statusCode);
            model.addAttribute("timestamp", LocalDateTime.now());
            model.addAttribute("path", request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI));
            model.addAttribute("message", request.getAttribute(RequestDispatcher.ERROR_MESSAGE));
            
            // Return appropriate view based on status code
            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                return "error/404";
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                return "error/500";
            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                return "error/403";
            }
        }
        
        // Default error page
        return "error/general";
    }
}
```

2. **Using @ControllerAdvice for specific exceptions**:

```java
@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleResourceNotFoundException(
            ResourceNotFoundException ex, Model model) {
        model.addAttribute("timestamp", LocalDateTime.now());
        model.addAttribute("message", ex.getMessage());
        return "error/404";
    }
    
    @ExceptionHandler(AccessDeniedException.class)
    public String handleAccessDeniedException(
            AccessDeniedException ex, Model model) {
        model.addAttribute("timestamp", LocalDateTime.now());
        model.addAttribute("message", ex.getMessage());
        return "error/403";
    }
    
    @ExceptionHandler(Exception.class)
    public String handleGenericException(Exception ex, Model model) {
        model.addAttribute("timestamp", LocalDateTime.now());
        model.addAttribute("message", "An unexpected error occurred");
        return "error/general";
    }
}
```

3. **Using error.html templates in the classpath**:

The simplest approach is to create error templates in the `src/main/resources/templates/error/` directory:

- `src/main/resources/templates/error/404.html` - For 404 errors
- `src/main/resources/templates/error/403.html` - For 403 errors
- `src/main/resources/templates/error/500.html` - For 500 errors
- `src/main/resources/templates/error.html` - Default error page

Example 404.html template (using Thymeleaf):
```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Page Not Found</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="/css/main.css">
</head>
<body>
    <div class="error-container">
        <h1>404 - Page Not Found</h1>
        <p>The page you are looking for does not exist.</p>
        <p th:if="${message}" th:text="${message}">Error Message</p>
        <p>
            <a href="/" class="btn">Go to Homepage</a>
        </p>
    </div>
</body>
</html>
```

4. **Using application.properties/yml configuration**:

You can also configure custom error pages in application.properties:

```properties
server.error.whitelabel.enabled=false
server.error.include-stacktrace=never
server.error.include-message=always
```

5. **For REST APIs - Custom JSON error responses**:

```java
@RestController
@RequestMapping("/error")
public class CustomErrorController implements ErrorController {
    
    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ErrorResponse> handleError(HttpServletRequest request) {
        // Get error status
        Integer statusCode = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        String message = (String) request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        String path = (String) request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
        
        if (statusCode == null) {
            statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
        }
        
        if (StringUtils.isEmpty(message)) {
            message = "No message available";
        }
        
        ErrorResponse errorResponse = new ErrorResponse(
            statusCode,
            message,
            LocalDateTime.now(),
            path
        );
        
        return ResponseEntity
            .status(statusCode)
            .body(errorResponse);
    }
}
```

6. **Combining approaches for content negotiation**:

```java
@Controller
@RequestMapping("/error")
public class ContentNegotiationErrorController implements ErrorController {
    
    @RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
    public String handleErrorHtml(HttpServletRequest request, Model model) {
        // Get error details
        Integer status = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        String path = (String) request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
        String message = (String) request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        
        // Add to model
        model.addAttribute("status", status);
        model.addAttribute("path", path);
        model.addAttribute("message", message);
        model.addAttribute("timestamp", LocalDateTime.now());
        
        // Return appropriate view
        if (status != null) {
            if (status == 404) {
                return "error/404";
            }
            if (status == 403) {
                return "error/403";
            }
            if (status == 500) {
                return "error/500";
            }
        }
        return "error/general";
    }
    
    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleErrorJson(HttpServletRequest request) {
        // Get error details
        Integer status = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        String path = (String) request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
        String message = (String) request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        
        if (status == null) {
            status = 500;
        }
        
        ErrorResponse errorResponse = new ErrorResponse(
            status,
            message != null ? message : "No message available",
            LocalDateTime.now(),
            path
        );
        
        return ResponseEntity.status(status).body(errorResponse);
    }
}
```

**Best Practices for Custom Error Pages:**

1. **Keep it user-friendly**: Error pages should be easy to understand for end users.
2. **Provide navigation options**: Include links to navigate back to safe areas of the application.
3. **Be consistent**: Maintain consistent styling with the rest of your application.
4. **Include appropriate information**: Show enough information to be helpful without exposing sensitive details.
5. **Log detailed errors server-side**: While showing simplified messages to users.
6. **Consider internationalization**: Error messages should be translatable.
7. **Test error pages**: Ensure they display correctly in different browsers and devices.

**Example of a Complete Implementation:**

1. Create error page templates:
```
src/main/resources/templates/error/
  ├── 404.html
  ├── 403.html
  ├── 500.html
  └── general.html
```

2. Implement the controller:
```java
@Controller
@RequestMapping("/error")
public class CustomErrorController implements ErrorController {
    
    private static final Logger logger = LoggerFactory.getLogger(CustomErrorController.class);
    
    @RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
    public String handleError(HttpServletRequest request, Model model) {
        // Get error attributes
        Integer statusCode = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        String path = (String) request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
        String message = (String) request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        Exception exception = (Exception) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        
        // Log the error
        if (exception != null) {
            logger.error("Error occurred for path: {}", path, exception);
        } else {
            logger.error("Error {} occurred for path: {}", statusCode, path);
        }
        
        // Add attributes to model
        model.addAttribute("timestamp", LocalDateTime.now());
        model.addAttribute("status", statusCode);
        model.addAttribute("error", message);
        model.addAttribute("path", path);
        
        // Choose view based on status code
        if (statusCode != null) {
            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                return "error/404";
            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                return "error/403";
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                return "error/500";
            }
        }
        
        return "error/general";
    }
}
```

3. Configure application.properties:
```properties
server.error.whitelabel.enabled=false
server.error.include-stacktrace=never
server.error.include-message=always
```

This implementation provides custom error pages for common HTTP status codes while maintaining good logging practices and a consistent user experience.

## Common Spring Boot Errors

### Q4: What are the most common Spring Boot startup errors and how would you troubleshoot them?

**Answer:** Spring Boot applications can encounter various startup errors. Here are the most common ones and how to troubleshoot them:

1. **Port Already in Use**

   **Error Message:**
   ```
   Web server failed to start. Port 8080 was already in use.
   ```
   
   **Causes:**
   - Another application is using the specified port
   - A previous instance of your application is still running
   
   **Troubleshooting:**
   - Check for running processes using the port:
     ```bash
     # On Windows
     net