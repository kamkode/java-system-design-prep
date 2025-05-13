# Spring Framework and Spring Boot Coding Questions

This document contains coding-focused interview questions related to Spring Framework and Spring Boot, suitable for candidates with 3-5 years of experience. These questions require writing or analyzing Spring code to demonstrate practical implementation skills.

## Table of Contents
1. [Spring Core and Dependency Injection](#spring-core-and-dependency-injection)
2. [Spring MVC and REST](#spring-mvc-and-rest)
3. [Spring Data and Database Access](#spring-data-and-database-access)
4. [Spring Boot Configuration](#spring-boot-configuration)
5. [Spring Security Implementation](#spring-security-implementation)
6. [Testing Spring Applications](#testing-spring-applications)
7. [Spring Boot Actuator and Monitoring](#spring-boot-actuator-and-monitoring)
8. [Spring Integration and Messaging](#spring-integration-and-messaging)

## Spring Core and Dependency Injection

### Q1: Write a Spring configuration class that defines three beans: a service, a repository, and a configuration properties bean. Show both constructor injection and setter injection.

**Answer:**

```
@Configuration
@EnableConfigurationProperties(ApplicationProperties.class)
public class AppConfig {

    @Bean
    public UserRepository userRepository() {
        return new UserRepositoryImpl();
    }
    
    @Bean
    public UserService userService(UserRepository userRepository, 
                                  ApplicationProperties properties) {
        // Constructor injection
        UserServiceImpl service = new UserServiceImpl(userRepository);
        // Setter injection
        service.setMaxRetries(properties.getMaxRetries());
        return service;
    }
}

@ConfigurationProperties(prefix = "application")
public class ApplicationProperties {
    private int maxRetries = 3;
    private String apiUrl;
    
    // Getters and setters
    public int getMaxRetries() {
        return maxRetries;
    }
    
    public void setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
    }
    
    public String getApiUrl() {
        return apiUrl;
    }
    
    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }
}

// Service implementation with constructor and setter injection
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private int maxRetries;
    
    // Constructor injection
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    // Setter injection
    public void setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
    }
}
```

### Q2: Implement a custom bean post-processor in Spring that logs the initialization of each bean.

**Answer:**

```
@Component
public class LoggingBeanPostProcessor implements BeanPostProcessor {
    
    private static final Logger logger = LoggerFactory.getLogger(LoggingBeanPostProcessor.class);
    
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) 
            throws BeansException {
        logger.info("Bean '{}' of type {} is about to be initialized", 
                   beanName, bean.getClass().getName());
        return bean;
    }
    
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) 
            throws BeansException {
        logger.info("Bean '{}' of type {} has been initialized", 
                   beanName, bean.getClass().getName());
        return bean;
    }
}
```

To use this bean post-processor, you just need to define it as a bean in your Spring application context, and it will automatically be applied to all beans.

### Q3: Write code to demonstrate three different ways to handle bean dependencies in Spring.

**Answer:**

```
// 1. Constructor Injection (Preferred approach)
@Service
public class OrderServiceImpl implements OrderService {
    
    private final OrderRepository orderRepository;
    private final PaymentService paymentService;
    
    @Autowired // Optional in Spring 4.3+
    public OrderServiceImpl(OrderRepository orderRepository, 
                           PaymentService paymentService) {
        this.orderRepository = orderRepository;
        this.paymentService = paymentService;
    }
    
    // Service methods
}

// 2. Setter Injection
@Service
public class CustomerServiceImpl implements CustomerService {
    
    private CustomerRepository customerRepository;
    private EmailService emailService;
    
    @Autowired
    public void setCustomerRepository(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }
    
    @Autowired
    public void setEmailService(EmailService emailService) {
        this.emailService = emailService;
    }
    
    // Service methods
}

// 3. Field Injection (Not recommended for production code due to testing difficulties)
@Service
public class ProductServiceImpl implements ProductService {
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private PricingService pricingService;
    
    // Service methods
}

// Bonus: Using @Qualifier when multiple beans of the same type exist
@Service
public class NotificationServiceImpl implements NotificationService {
    
    private final MessageSender messageSender;
    
    @Autowired
    public NotificationServiceImpl(@Qualifier("smsMessageSender") MessageSender messageSender) {
        this.messageSender = messageSender;
    }
    
    // Service methods
}
```

### Q4: Implement a custom scope in Spring. Create a "request-per-thread" scope that creates one bean instance per thread.

**Answer:**

```
public class ThreadScope implements Scope {
    
    private final ThreadLocal<Map<String, Object>> threadScope = 
        ThreadLocal.withInitial(HashMap::new);
    
    @Override
    public Object get(String name, ObjectFactory<?> objectFactory) {
        Map<String, Object> scope = threadScope.get();
        if (!scope.containsKey(name)) {
            scope.put(name, objectFactory.getObject());
        }
        return scope.get(name);
    }
    
    @Override
    public Object remove(String name) {
        Map<String, Object> scope = threadScope.get();
        return scope.remove(name);
    }
    
    @Override
    public void registerDestructionCallback(String name, Runnable callback) {
        // Destruction callbacks not supported in this simple implementation
    }
    
    @Override
    public Object resolveContextualObject(String key) {
        return null;
    }
    
    @Override
    public String getConversationId() {
        return Thread.currentThread().getName();
    }
    
    // Method to clear the scope when thread is done
    public void clear() {
        threadScope.remove();
    }
}

// Register the custom scope
@Configuration
public class ScopeConfig implements BeanFactoryPostProcessor {
    
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) 
            throws BeansException {
        beanFactory.registerScope("thread", new ThreadScope());
    }
    
    // Bean to clean up thread scope when request is done
    @Bean
    public ThreadScopeCleanupFilter threadScopeCleanupFilter(BeanFactory beanFactory) {
        return new ThreadScopeCleanupFilter(
            (ThreadScope) ((ConfigurableBeanFactory) beanFactory).getRegisteredScope("thread")
        );
    }
}

// Filter to clean up thread scope
public class ThreadScopeCleanupFilter implements Filter {
    
    private final ThreadScope threadScope;
    
    public ThreadScopeCleanupFilter(ThreadScope threadScope) {
        this.threadScope = threadScope;
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
            throws IOException, ServletException {
        try {
            chain.doFilter(request, response);
        } finally {
            threadScope.clear();
        }
    }
}

// Using the custom scope
@Component
@Scope(value = "thread", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ThreadScopedBean {
    private final String id = UUID.randomUUID().toString();
    
    public String getId() {
        return id;
    }
}
```

### Q5: Write code to implement method-level security using Spring AOP.

**Answer:**

```
// Define a custom annotation for security
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresPermission {
    String value();
}

// Implement the security aspect
@Aspect
@Component
public class SecurityAspect {
    
    private final SecurityService securityService;
    
    @Autowired
    public SecurityAspect(SecurityService securityService) {
        this.securityService = securityService;
    }
    
    @Before("@annotation(requiresPermission)")
    public void checkPermission(JoinPoint joinPoint, RequiresPermission requiresPermission) {
        String requiredPermission = requiresPermission.value();
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new AccessDeniedException("No authentication found");
        }
        
        if (!securityService.hasPermission(authentication, requiredPermission)) {
            throw new AccessDeniedException(
                "User does not have required permission: " + requiredPermission);
        }
    }
}

// Security service interface
public interface SecurityService {
    boolean hasPermission(Authentication authentication, String permission);
}

// Implementation of security service
@Service
public class SecurityServiceImpl implements SecurityService {
    
    @Override
    public boolean hasPermission(Authentication authentication, String permission) {
        return authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .anyMatch(authority -> authority.equals(permission));
    }
}

// Using the custom security annotation
@Service
public class DocumentServiceImpl implements DocumentService {
    
    @RequiresPermission("DOCUMENT_READ")
    @Override
    public Document getDocument(Long id) {
        // Implementation
    }
    
    @RequiresPermission("DOCUMENT_WRITE")
    @Override
    public void saveDocument(Document document) {
        // Implementation
    }
}
```

## Spring MVC and REST

### Q6: Implement a RESTful controller for a User resource with CRUD operations. Include proper response status codes and exception handling.

**Answer:**

```
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.findAllUsers();
        return ResponseEntity.ok(users);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        return userService.findUserById(id)
            .map(ResponseEntity::ok)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }
    
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userDTO) {
        if (userDTO.getId() != null) {
            throw new BadRequestException("A new user cannot already have an ID");
        }
        UserDTO result = userService.createUser(userDTO);
        return ResponseEntity
            .created(URI.create("/api/users/" + result.getId()))
            .body(result);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(
            @PathVariable Long id, 
            @Valid @RequestBody UserDTO userDTO) {
        if (userDTO.getId() == null) {
            throw new BadRequestException("ID must be provided for update");
        }
        if (!Objects.equals(id, userDTO.getId())) {
            throw new BadRequestException("ID in path and body must match");
        }
        
        UserDTO result = userService.updateUser(userDTO);
        return ResponseEntity.ok(result);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
    
    // Exception handlers
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            ex.getMessage(),
            LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(BadRequestException ex) {
        ErrorResponse error = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            ex.getMessage(),
            LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}

// DTO class
public class UserDTO {
    private Long id;
    
    @NotBlank(message = "Username is required")
    private String username;
    
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    private String email;
    
    // Getters and setters
}

// Exception classes
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}

// Error response class
public class ErrorResponse {
    private int status;
    private String message;
    private LocalDateTime timestamp;
    
    public ErrorResponse(int status, String message, LocalDateTime timestamp) {
        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
    }
    
    // Getters
}
```

### Q7: Implement a global exception handler for a Spring Boot application that handles common exceptions and returns appropriate responses.

**Answer:**

```
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
            ResourceNotFoundException ex, WebRequest request) {
        
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            ex.getMessage(),
            LocalDateTime.now(),
            request.getDescription(false)
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(
            BadRequestException ex, WebRequest request) {
        
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            ex.getMessage(),
            LocalDateTime.now(),
            request.getDescription(false)
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex, WebRequest request) {
        
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        ValidationErrorResponse errorResponse = new ValidationErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Validation failed",
            LocalDateTime.now(),
            request.getDescription(false),
            errors
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(
            AccessDeniedException ex, WebRequest request) {
        
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.FORBIDDEN.value(),
            "Access denied: " + ex.getMessage(),
            LocalDateTime.now(),
            request.getDescription(false)
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }
    
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(
            DataIntegrityViolationException ex, WebRequest request) {
        
        String message = "Database error: ";
        if (ex.getCause() instanceof ConstraintViolationException) {
            message += "Constraint violation";
        } else {
            message += ex.getMostSpecificCause().getMessage();
        }
        
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.CONFLICT.value(),
            message,
            LocalDateTime.now(),
            request.getDescription(false)
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }
    
    // Catch-all for any other exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(
            Exception ex, WebRequest request) {
        
        logger.error("Unhandled exception", ex);
        
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "An unexpected error occurred",
            LocalDateTime.now(),
            request.getDescription(false)
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

// Error response classes
public class ErrorResponse {
    private int status;
    private String message;
    private LocalDateTime timestamp;
    private String path;
    
    // Constructor, getters, setters
}

public class ValidationErrorResponse extends ErrorResponse {
    private Map<String, String> fieldErrors;
    
    // Constructor, getters, setters
}
```

### Q8: Write a Spring MVC controller that handles file uploads and downloads.

**Answer:**

```
@Controller
@RequestMapping("/files")
public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);
    
    private final FileStorageService fileStorageService;
    
    @Autowired
    public FileController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }
    
    @GetMapping
    public String showUploadForm(Model model) {
        model.addAttribute("files", fileStorageService.getAllFiles());
        return "file-upload";
    }
    
    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file, 
                            RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return "redirect:/files";
        }
        
        try {
            String fileName = fileStorageService.storeFile(file);
            
            redirectAttributes.addFlashAttribute("message", 
                "File uploaded successfully: " + fileName);
        } catch (Exception e) {
            logger.error("Failed to upload file", e);
            redirectAttributes.addFlashAttribute("message", 
                "Failed to upload file: " + e.getMessage());
        }
        
        return "redirect:/files";
    }
    
    @GetMapping("/download/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, 
                                               HttpServletRequest request) {
        Resource resource = fileStorageService.loadFileAsResource(fileName);
        
        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }
        
        // Fallback to the default content type if type could not be determined
        if (contentType == null) {
            contentType = "application/octet-stream";
        }
        
        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(contentType))
            .header(HttpHeaders.CONTENT_DISPOSITION, 
                   "attachment; filename=\"" + resource.getFilename() + "\"")
            .body(resource);
    }
    
    @DeleteMapping("/{fileName:.+}")
    public ResponseEntity<Void> deleteFile(@PathVariable String fileName) {
        boolean deleted = fileStorageService.deleteFile(fileName);
        
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    // Exception handler for file not found
    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleFileNotFoundException(
            FileNotFoundException ex) {
        
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            ex.getMessage(),
            LocalDateTime.now()
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}

// File storage service interface
public interface FileStorageService {
    String storeFile(MultipartFile file) throws IOException;
    Resource loadFileAsResource(String fileName) throws FileNotFoundException;
    List<String> getAllFiles();
    boolean deleteFile(String fileName);
}

// File storage service implementation
@Service
public class FileStorageServiceImpl implements FileStorageService {
    
    private final Path fileStorageLocation;
    
    @Autowired
    public FileStorageServiceImpl(@Value("${file.upload-dir}") String uploadDir) {
        this.fileStorageLocation = Paths.get(uploadDir)
            .toAbsolutePath().normalize();
        
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }
    
    @Override
    public String storeFile(MultipartFile file) throws IOException {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        
        // Check if the file's name contains invalid characters
        if (fileName.contains("..")) {
            throw new IllegalArgumentException("Filename contains invalid path sequence " + fileName);
        }
        
        // Copy file to the target location (Replacing existing file with the same name)
        Path targetLocation = this.fileStorageLocation.resolve(fileName);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        
        return fileName;
    }
    
    @Override
    public Resource loadFileAsResource(String fileName) throws FileNotFoundException {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            
            if (resource.exists()) {
                return resource;
            } else {
                throw new FileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new FileNotFoundException("File not found " + fileName);
        }
    }
    
    @Override
    public List<String> getAllFiles() {
        try {
            return Files.list(this.fileStorageLocation)
                .map(path -> path.getFileName().toString())
                .collect(Collectors.toList());
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }
    
    @Override
    public boolean deleteFile(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            return Files.deleteIfExists(filePath);
        } catch (IOException e) {
            return false;
        }
    }
}
```

### Q9: Implement a REST controller that uses Spring's WebClient to make calls to another service.

**Answer:**

```
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final WebClient webClient;
    private final String inventoryServiceUrl;
    
    @Autowired
    public ProductController(WebClient.Builder webClientBuilder, 
                           @Value("${inventory.service.url}") String inventoryServiceUrl) {
        this.webClient = webClientBuilder.baseUrl(inventoryServiceUrl).build();
        this.inventoryServiceUrl = inventoryServiceUrl;
    }
    
    @GetMapping("/{id}")
    public Mono<ProductDetailsDTO> getProductWithInventory(@PathVariable Long id) {
        // First get the product details
        Mono<ProductDTO> productMono = webClient.get()
            .uri("/products/{id}", id)
            .retrieve()
            .onStatus(HttpStatus::is4xxClientError, 
                response -> Mono.error(new ResourceNotFoundException("Product not found: " + id)))
            .onStatus(HttpStatus::is5xxServerError, 
                response -> Mono.error(new ServiceUnavailableException("Product service unavailable")))
            .bodyToMono(ProductDTO.class);
        
        // Then get the inventory information
        Mono<InventoryDTO> inventoryMono = productMono
            .flatMap(product -> webClient.get()
                .uri("/inventory/{sku}", product.getSku())
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, 
                    response -> Mono.just(new InventoryDTO(product.getSku(), 0, false)))
                .onStatus(HttpStatus::is5xxServerError, 
                    response -> Mono.error(new ServiceUnavailableException("Inventory service unavailable")))
                .bodyToMono(InventoryDTO.class)
                .onErrorResume(e -> {
                    // Fallback for inventory service
                    return Mono.just(new InventoryDTO(product.getSku(), 0, false));
                })
            );
        
        // Combine the results
        return Mono.zip(productMono, inventoryMono)
            .map(tuple -> {
                ProductDTO product = tuple.getT1();
                InventoryDTO inventory = tuple.getT2();
                
                return new ProductDetailsDTO(
                    product.getId(),
                    product.getName(),
                    product.getDescription(),
                    product.getPrice(),
                    product.getSku(),
                    inventory.getQuantityAvailable(),
                    inventory.isInStock()
                );
            });
    }
    
    @GetMapping
    public Flux<ProductDTO> getAllProducts() {
        return webClient.get()
            .uri("/products")
            .retrieve()
            .onStatus(HttpStatus::is5xxServerError, 
                response -> Mono.error(new ServiceUnavailableException("Product service unavailable")))
            .bodyToFlux(ProductDTO.class);
    }
    
    @PostMapping
    public Mono<ResponseEntity<ProductDTO>> createProduct(@Valid @RequestBody ProductDTO productDTO) {
        return webClient.post()
            .uri("/products")
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(productDTO), ProductDTO.class)
            .retrieve()
            .onStatus(HttpStatus::is4xxClientError, 
                response -> Mono.error(new BadRequestException("Invalid product data")))
            .onStatus(HttpStatus::is5xxServerError, 
                response -> Mono.error(new ServiceUnavailableException("Product service unavailable")))
            .bodyToMono(ProductDTO.class)
            .map(product -> ResponseEntity
                .created(URI.create("/api/products/" + product.getId()))
                .body(product));
    }
    
    // Exception handlers
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            ex.getMessage(),
            LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<ErrorResponse> handleServiceUnavailableException(ServiceUnavailableException ex) {
        ErrorResponse error = new ErrorResponse(
            HttpStatus.SERVICE_UNAVAILABLE.value(),
            ex.getMessage(),
            LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.SERVICE_UNAVAILABLE);
    }
}

// DTOs
public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String sku;
    
    // Constructors, getters, setters
}

public class InventoryDTO {
    private String sku;
    private int quantityAvailable;
    private boolean inStock;
    
    // Constructors, getters, setters
}

public class ProductDetailsDTO {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String sku;
    private int quantityAvailable;
    private boolean inStock;
    
    // Constructors, getters, setters
}

// Exception classes
public class ServiceUnavailableException extends RuntimeException {
    public ServiceUnavailableException(String message) {
        super(message);
    }
}
```

### Q10: Implement a Spring MVC controller that handles form submissions with validation.

**Answer:**

```
@Controller
@RequestMapping("/registration")
public class RegistrationController {

    private final UserService userService;