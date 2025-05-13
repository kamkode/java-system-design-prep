# Spring Framework and Spring Boot Security Questions

This document contains interview questions related to Spring Security, suitable for candidates with 3-5 years of experience. These questions focus on authentication, authorization, JWT, CSRF protection, OAuth2, filter chains, and other security concepts.

## Table of Contents
1. [Security Fundamentals](#security-fundamentals)
2. [Authentication Mechanisms](#authentication-mechanisms)
3. [Authorization and Access Control](#authorization-and-access-control)
4. [JWT Implementation](#jwt-implementation)
5. [OAuth2 and OpenID Connect](#oauth2-and-openid-connect)
6. [CSRF and XSS Protection](#csrf-and-xss-protection)
7. [Security Filter Chain](#security-filter-chain)
8. [Testing Security](#testing-security)

## Security Fundamentals

### Q1: Explain the core components of Spring Security architecture and how they work together.

**Answer:** Spring Security's architecture consists of several key components that work together to provide a comprehensive security framework:

1. **SecurityContextHolder**:
   - The heart of Spring Security's authentication model
   - Stores details of the currently authenticated user (the "Security Context")
   - Uses a ThreadLocal to store the context, making it accessible throughout the application
   - Can be configured to use different strategies (ThreadLocal, InheritableThreadLocal, or Global)

   ```java
   // Accessing the current user
   SecurityContext context = SecurityContextHolder.getContext();
   Authentication authentication = context.getAuthentication();
   UserDetails userDetails = (UserDetails) authentication.getPrincipal();
   ```

2. **Authentication**:
   - Core interface representing an authentication request or an authenticated principal
   - Contains:
     - Principal: identifies the user (often a UserDetails object)
     - Credentials: often a password (cleared after authentication for security)
     - Authorities: permissions granted to the user
     - Details: additional information about the authentication
     - Authenticated flag: indicates if the user is authenticated

3. **AuthenticationManager**:
   - Central interface for authentication
   - Primary implementation is ProviderManager
   - Delegates to a chain of AuthenticationProvider instances

   ```java
   public interface AuthenticationManager {
       Authentication authenticate(Authentication authentication) 
           throws AuthenticationException;
   }
   ```

4. **AuthenticationProvider**:
   - Performs specific types of authentication
   - Common implementations:
     - DaoAuthenticationProvider: username/password authentication using a UserDetailsService
     - JwtAuthenticationProvider: JWT token authentication
     - OAuth2LoginAuthenticationProvider: OAuth 2.0 login

5. **UserDetailsService**:
   - Loads user-specific data
   - Core interface for user data retrieval
   - Custom implementations typically load user information from databases or other sources

   ```java
   public interface UserDetailsService {
       UserDetails loadUserByUsername(String username) 
           throws UsernameNotFoundException;
   }
   ```

6. **PasswordEncoder**:
   - Handles password hashing and verification
   - Modern implementations use adaptive one-way functions (bcrypt, PBKDF2, etc.)

   ```java
   public interface PasswordEncoder {
       String encode(CharSequence rawPassword);
       boolean matches(CharSequence rawPassword, String encodedPassword);
   }
   ```

7. **SecurityFilterChain**:
   - Chain of servlet filters that process the HTTP request
   - Each filter performs a specific security function
   - Filters are executed in a specific order
   - In Spring Boot, configured through HttpSecurity in a SecurityFilterChain bean

8. **AccessDecisionManager**:
   - Makes authorization decisions
   - Uses voters to determine if access should be granted
   - Common implementations:
     - AffirmativeBased: grants access if any voter returns affirmative
     - ConsensusBased: grants access based on voter majority
     - UnanimousBased: grants access only if all voters agree

9. **FilterSecurityInterceptor**:
   - Secures HTTP resources
   - Uses AccessDecisionManager to make authorization decisions
   - Typically the last filter in the chain

10. **Method Security**:
    - Secures method invocations
    - Uses annotations like @PreAuthorize, @PostAuthorize, @Secured
    - Implemented through AOP proxies

**How These Components Work Together**:

1. **Authentication Flow**:
   - A request comes in and is processed by the security filters
   - The appropriate authentication filter extracts credentials from the request
   - The filter creates an Authentication object and passes it to the AuthenticationManager
   - The AuthenticationManager delegates to appropriate AuthenticationProviders
   - If authentication succeeds, the Authentication is stored in the SecurityContextHolder
   - If authentication fails, an AuthenticationException is thrown

2. **Authorization Flow**:
   - After authentication, the request proceeds through the filter chain
   - When it reaches FilterSecurityInterceptor, authorization checks are performed
   - The AccessDecisionManager consults its voters to decide if access should be granted
   - If access is denied, an AccessDeniedException is thrown

3. **Configuration in Spring Boot 3.x**:
   ```java
   @Configuration
   @EnableWebSecurity
   public class SecurityConfig {

       @Bean
       public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
           http
               .authorizeHttpRequests(authorize -> authorize
                   .requestMatchers("/public/**").permitAll()
                   .requestMatchers("/admin/**").hasRole("ADMIN")
                   .anyRequest().authenticated()
               )
               .formLogin(form -> form
                   .loginPage("/login")
                   .permitAll()
               )
               .logout(logout -> logout
                   .logoutSuccessUrl("/login?logout")
               );

           return http.build();
       }

       @Bean
       public UserDetailsService userDetailsService() {
           UserDetails user = User.builder()
               .username("user")
               .password(passwordEncoder().encode("password"))
               .roles("USER")
               .build();

           UserDetails admin = User.builder()
               .username("admin")
               .password(passwordEncoder().encode("admin"))
               .roles("ADMIN", "USER")
               .build();

           return new InMemoryUserDetailsManager(user, admin);
       }

       @Bean
       public PasswordEncoder passwordEncoder() {
           return new BCryptPasswordEncoder();
       }
   }
   ```

This architecture provides a flexible and powerful security framework that can be customized to meet various security requirements while maintaining a consistent approach to authentication and authorization.

### Q2: What are the differences between authentication and authorization in Spring Security? How are they implemented?

**Answer:** Authentication and authorization are two fundamental but distinct security concepts in Spring Security:

### Authentication vs. Authorization

**Authentication**:
- **Definition**: The process of verifying the identity of a user, system, or entity
- **Question it answers**: "Who are you?"
- **Focus**: Validating credentials and establishing identity
- **Timing**: Occurs before authorization
- **Result**: Authenticated principal with granted authorities

**Authorization**:
- **Definition**: The process of determining if an authenticated entity has permission to access a resource or perform an action
- **Question it answers**: "What are you allowed to do?"
- **Focus**: Checking permissions and enforcing access control
- **Timing**: Occurs after successful authentication
- **Result**: Access granted or denied to specific resources

### Authentication Implementation in Spring Security

1. **Authentication Entry Points**:
   - Form-based login
   - HTTP Basic Authentication
   - OAuth2/OpenID Connect
   - JWT tokens
   - Custom authentication mechanisms

2. **Core Components**:

   a) **Authentication Interface**:
   ```java
   public interface Authentication extends Principal, Serializable {
       Collection<? extends GrantedAuthority> getAuthorities();
       Object getCredentials();
       Object getDetails();
       Object getPrincipal();
       boolean isAuthenticated();
       void setAuthenticated(boolean isAuthenticated);
   }
   ```

   b) **AuthenticationManager**:
   ```java
   public interface AuthenticationManager {
       Authentication authenticate(Authentication authentication) 
           throws AuthenticationException;
   }
   ```

   c) **AuthenticationProvider**:
   ```java
   public interface AuthenticationProvider {
       Authentication authenticate(Authentication authentication) 
           throws AuthenticationException;
       boolean supports(Class<?> authentication);
   }
   ```

3. **Common Authentication Implementations**:

   a) **Username/Password Authentication**:
   ```java
   @Configuration
   @EnableWebSecurity
   public class SecurityConfig {

       @Bean
       public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
           http
               .authorizeHttpRequests(authorize -> authorize
                   .anyRequest().authenticated()
               )
               .formLogin(withDefaults());

           return http.build();
       }

       @Bean
       public UserDetailsService userDetailsService() {
           // Define users here
           return new InMemoryUserDetailsManager(users);
       }

       @Bean
       public PasswordEncoder passwordEncoder() {
           return new BCryptPasswordEncoder();
       }
   }
   ```

   b) **JWT Authentication**:
   ```java
   @Component
   public class JwtAuthenticationFilter extends OncePerRequestFilter {

       private final JwtTokenProvider tokenProvider;

       @Override
       protected void doFilterInternal(HttpServletRequest request, 
                                      HttpServletResponse response, 
                                      FilterChain filterChain) 
               throws ServletException, IOException {

           try {
               String jwt = getJwtFromRequest(request);

               if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                   Long userId = tokenProvider.getUserIdFromJWT(jwt);

                   UserDetails userDetails = customUserDetailsService.loadUserById(userId);
                   UsernamePasswordAuthenticationToken authentication = 
                       new UsernamePasswordAuthenticationToken(
                           userDetails, null, userDetails.getAuthorities());

                   authentication.setDetails(new WebAuthenticationDetailsSource()
                       .buildDetails(request));

                   SecurityContextHolder.getContext().setAuthentication(authentication);
               }
           } catch (Exception ex) {
               logger.error("Could not set user authentication in security context", ex);
           }

           filterChain.doFilter(request, response);
       }

       private String getJwtFromRequest(HttpServletRequest request) {
           String bearerToken = request.getHeader("Authorization");
           if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
               return bearerToken.substring(7);
           }
           return null;
       }
   }
   ```

4. **Authentication Storage**:
   - SecurityContextHolder stores the Authentication object
   - Default strategy uses ThreadLocal storage
   - Can be configured for different strategies:
     ```
     SecurityContextHolder.setStrategyName(
         SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
     ```

### Authorization Implementation in Spring Security

1. **Core Components**:

   a) **AccessDecisionManager**:
   ```java
   public interface AccessDecisionManager {
       void decide(Authentication authentication, Object object, 
                  Collection<ConfigAttribute> configAttributes) 
           throws AccessDeniedException, InsufficientAuthenticationException;
       boolean supports(ConfigAttribute attribute);
       boolean supports(Class<?> clazz);
   }
   ```

   b) **AccessDecisionVoter**:
   ```java
   public interface AccessDecisionVoter<S> {
       int ACCESS_GRANTED = 1;
       int ACCESS_ABSTAIN = 0;
       int ACCESS_DENIED = -1;

       int vote(Authentication authentication, S object, 
               Collection<ConfigAttribute> attributes);
       boolean supports(ConfigAttribute attribute);
       boolean supports(Class<?> clazz);
   }
   ```

2. **URL-Based Authorization**:
   ```java
   @Bean
   public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
       http
           .authorizeHttpRequests(authorize -> authorize
               .requestMatchers("/public/**").permitAll()
               .requestMatchers("/user/**").hasRole("USER")
               .requestMatchers("/admin/**").hasRole("ADMIN")
               .requestMatchers("/api/**").hasAuthority("API_ACCESS")
               .anyRequest().authenticated()
           );

       return http.build();
   }
   ```

3. **Method-Level Security**:
   ```java
   @Configuration
   @EnableMethodSecurity
   public class MethodSecurityConfig {
       // Configuration if needed
   }

   @Service
   public class UserService {

       @PreAuthorize("hasRole('ADMIN')")
       public List<User> getAllUsers() {
           // Only accessible to admins
       }

       @PreAuthorize("hasRole('USER') and #username == authentication.principal.username")
       public User getUserByUsername(String username) {
           // Users can only access their own data
       }

       @PostAuthorize("returnObject.username == authentication.principal.username")
       public User getUser(Long id) {
           // Verify the returned user matches the current user
       }

       @Secured({"ROLE_ADMIN", "ROLE_SUPER_ADMIN"})
       public void deleteUser(Long id) {
           // Only admins can delete users
       }
   }
   ```

4. **Expression-Based Access Control**:
   ```java
   @Bean
   public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
       http
           .authorizeHttpRequests(authorize -> authorize
               .requestMatchers("/user/{id}/**").access(
                   new WebExpressionAuthorizationManager(
                       "authentication.principal.id == #id or hasRole('ADMIN')")
               )
           );

       return http.build();
   }
   ```

5. **Custom Authorization Rules**:
   ```java
   @Component
   public class CustomPermissionEvaluator implements PermissionEvaluator {

       @Override
       public boolean hasPermission(Authentication auth, Object targetDomainObject, 
                                   Object permission) {
           if (auth == null || targetDomainObject == null || !(permission instanceof String)) {
               return false;
           }

           String permString = (String) permission;

           if (targetDomainObject instanceof Document) {
               return hasDocumentPermission(auth, (Document) targetDomainObject, permString);
           }

           return false;
       }

       @Override
       public boolean hasPermission(Authentication auth, Serializable targetId, 
                                   String targetType, Object permission) {
           // Implementation
       }

       private boolean hasDocumentPermission(Authentication auth, Document document, 
                                           String permission) {
           // Custom logic to determine if the user has the required permission
       }
   }

   // Usage in @PreAuthorize
   @PreAuthorize("hasPermission(#document, 'EDIT')")
   public void editDocument(Document document) {
       // Method implementation
   }
   ```

### Integration of Authentication and Authorization

1. **Security Filter Chain**:
   - Authentication filters process the request first
   - If authentication succeeds, the request proceeds to authorization filters
   - FilterSecurityInterceptor typically performs URL-based authorization

2. **Method Security**:
   - Uses AOP proxies to intercept method calls
   - Checks authorization rules before method execution
   - Can also perform checks after method execution (@PostAuthorize)

3. **Complete Configuration Example**:
   ```java
   @Configuration
   @EnableWebSecurity
   @EnableMethodSecurity
   public class SecurityConfig {

       @Bean
       public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
           http
               // Authentication configuration
               .formLogin(form -> form
                   .loginPage("/login")
                   .defaultSuccessUrl("/dashboard")
                   .failureUrl("/login?error=true")
                   .permitAll()
               )
               .rememberMe(remember -> remember
                   .key("uniqueAndSecretKey")
                   .tokenValiditySeconds(86400)
               )
               .logout(logout -> logout
                   .logoutUrl("/logout")
                   .logoutSuccessUrl("/login?logout=true")
               )

               // Authorization configuration
               .authorizeHttpRequests(authorize -> authorize
                   .requestMatchers("/public/**", "/resources/**").permitAll()
                   .requestMatchers("/admin/**").hasRole("ADMIN")
                   .requestMatchers("/api/**").hasAuthority("API_ACCESS")
                   .anyRequest().authenticated()
               );

           return http.build();
       }

       // Other beans for UserDetailsService, PasswordEncoder, etc.
   }
   ```

By understanding the distinction between authentication and authorization and how they're implemented in Spring Security, developers can create secure applications with fine-grained access control that properly validates user identities before granting access to protected resources.

### Q3: How would you implement custom authentication in Spring Security?

**Answer:** Implementing custom authentication in Spring Security involves several approaches depending on your specific requirements. Here's a comprehensive guide to different custom authentication strategies:

## 1. Custom UserDetailsService Implementation

This is the simplest approach when you need to authenticate against a custom user store but still want to use Spring Security's standard authentication mechanisms.

```java
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return new org.springframework.security.core.userdetails.User(
            user.getUsername(),
            user.getPassword(),
            user.isEnabled(),
            true, // accountNonExpired
            true, // credentialsNonExpired
            !user.isLocked(), // accountNonLocked
            getAuthorities(user.getRoles())
        );
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Set<Role> roles) {
        return roles.stream()
            .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
            .collect(Collectors.toList());
    }
}

// Configuration
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                .anyRequest().authenticated()
            )
            .formLogin(withDefaults());

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

## 2. Custom Authentication Provider

When you need more control over the authentication process, implement a custom AuthenticationProvider.

```java
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CustomAuthenticationProvider(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        User user = userService.findByUsername(username)
            .orElseThrow(() -> new BadCredentialsException("Invalid username or password"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }

        if (!user.isEnabled()) {
            throw new DisabledException("User account is disabled");
        }

        if (user.isLocked()) {
            throw new LockedException("User account is locked");
        }

        List<GrantedAuthority> authorities = user.getRoles().stream()
            .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
            .collect(Collectors.toList());

        return new UsernamePasswordAuthenticationToken(username, password, authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}

// Configuration
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomAuthenticationProvider authProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                .anyRequest().authenticated()
            )
            .formLogin(withDefaults());

        return http.build();
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authManagerBuilder = 
            http.getSharedObject(AuthenticationManagerBuilder.class);
        authManagerBuilder.authenticationProvider(authProvider);
        return authManagerBuilder.build();
    }
}
```

## 3. Custom Authentication Filter

For completely custom authentication mechanisms (like API keys, custom tokens, etc.), implement a custom authentication filter.

```java
public class ApiKeyAuthenticationFilter extends OncePerRequestFilter {

    private final String headerName;
    private final AuthenticationManager authenticationManager;

    public ApiKeyAuthenticationFilter(String headerName, AuthenticationManager authenticationManager) {
        this.headerName = headerName;
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                   HttpServletResponse response, 
                                   FilterChain filterChain) 
            throws ServletException, IOException {

        String apiKey = request.getHeader(headerName);

        if (apiKey == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            ApiKeyAuthenticationToken authRequest = new ApiKeyAuthenticationToken(apiKey);
            Authentication authentication = authenticationManager.authenticate(authRequest);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (AuthenticationException e) {
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}

// Custom Authentication Token
public class ApiKeyAuthenticationToken extends AbstractAuthenticationToken {

    private final String apiKey;

    // Constructor for unauthenticated token
    public ApiKeyAuthenticationToken(String apiKey) {
        super(null);
        this.apiKey = apiKey;
        setAuthenticated(false);
    }

    // Constructor for authenticated token
    public ApiKeyAuthenticationToken(String apiKey, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.apiKey = apiKey;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return apiKey;
    }

    @Override
    public Object getPrincipal() {
        return apiKey;
    }
}

// Custom Authentication Provider for API Keys
@Component
public class ApiKeyAuthenticationProvider implements AuthenticationProvider {

    private final ApiKeyService apiKeyService;

    @Autowired
    public ApiKeyAuthenticationProvider(ApiKeyService apiKeyService) {
        this.apiKeyService = apiKeyService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String apiKey = (String) authentication.getCredentials();

        ApiKeyUser user = apiKeyService.findByApiKey(apiKey)
            .orElseThrow(() -> new BadCredentialsException("Invalid API key"));

        List<GrantedAuthority> authorities = user.getRoles().stream()
            .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
            .collect(Collectors.toList());

        return new ApiKeyAuthenticationToken(apiKey, authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return ApiKeyAuthenticationToken.class.isAssignableFrom(authentication);
    }
}

// Configuration
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private ApiKeyAuthenticationProvider apiKeyAuthProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                .anyRequest().authenticated()
            )
            .addFilterBefore(
                apiKeyAuthenticationFilter(http.getSharedObject(AuthenticationManager.class)),
                UsernamePasswordAuthenticationFilter.class
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            );

        return http.build();
    }

    @Bean
    public ApiKeyAuthenticationFilter apiKeyAuthenticationFilter(AuthenticationManager authManager) {
        return new ApiKeyAuthenticationFilter("X-API-KEY", authManager);
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authManagerBuilder = 
            http.getSharedObject(AuthenticationManagerBuilder.class);
        authManagerBuilder.authenticationProvider(apiKeyAuthProvider);
        return authManagerBuilder.build();
    }
}
```

## 4. JWT Authentication Implementation

JWT (JSON Web Token) is a common authentication mechanism for RESTful APIs.

```java
// JWT Authentication Filter
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtTokenProvider tokenProvider, 
                                  CustomUserDetailsService userDetailsService) {
        this.tokenProvider = tokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                   HttpServletResponse response, 
                                   FilterChain filterChain) 
            throws ServletException, IOException {

        try {
            String jwt = getJwtFromRequest(request);

            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                String username = tokenProvider.getUsernameFromToken(jwt);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authentication = 
                    new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource()
                    .buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ex) {
            logger.error("Could not set user authentication in security context", ex);
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}

// JWT Token Provider
@Component
public class JwtTokenProvider {

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationInMs}")
    private int jwtExpirationInMs;

    public String generateToken(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        return Jwts.builder()
            .setSubject(userDetails.getUsername())
            .setIssuedAt(new Date())
            .setExpiration(expiryDate)
            .signWith(SignatureAlgorithm.HS512, jwtSecret)
            .compact();
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
            .setSigningKey(jwtSecret)
            .parseClaimsJws(token)
            .getBody();

        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (SignatureException ex) {
            logger.error("Invalid JWT signature");
        } catch
