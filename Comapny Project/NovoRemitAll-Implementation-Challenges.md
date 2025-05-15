# NovoRemitAll: Implementation Challenges & Lessons Learned

## 🌊 Navigating Challenges: Technical Implementation Stories

"When interviewing, I've found that discussing not just what we built but the journey to get there really resonates with technical leaders. Here are some of the most significant implementation challenges we faced with NovoRemitAll and how we overcame them."

### The Midnight Database Crisis

"Three weeks after our launch in five countries, our monitoring alerts started firing at 2 AM when database connections suddenly spiked to near-maximum capacity. Transaction success rate was plummeting, and thousands of customers couldn't complete their transfers.

After examining the logs, I discovered that a routine cleanup job was competing with user transactions for database connections. The job had been triggered at midnight local time in our largest market, coinciding with peak usage as workers were sending remittances after receiving their paychecks.

I led the emergency response by implementing an immediate temporary fix by adjusting the database connection pool parameters, then developing a more elegant solution with connection prioritization:

```java
@Configuration
public class DataSourceConfiguration {

    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource.hikari.transaction")
    public DataSource transactionDataSource() {
        return DataSourceBuilder.create()
            .type(HikariDataSource.class)
            .build();
    }
    
    @Bean(name = "maintenanceDataSource")
    @ConfigurationProperties("spring.datasource.hikari.maintenance")
    public DataSource maintenanceDataSource() {
        return DataSourceBuilder.create()
            .type(HikariDataSource.class)
            .build();
    }
    
    // Connection pool configuration with prioritization
    @PostConstruct
    public void configureConnectionPools() {
        HikariDataSource transDs = (HikariDataSource) transactionDataSource();
        transDs.setMaximumPoolSize(100); // Higher priority pool
        transDs.setMinimumIdle(20);
        
        HikariDataSource maintDs = (HikariDataSource) maintenanceDataSource();
        maintDs.setMaximumPoolSize(20);  // Lower priority pool
        maintDs.setMinimumIdle(5);
    }
}
```

The long-term solution was developing a 'load-aware scheduler' that dynamically postponed non-critical maintenance tasks during peak usage hours. This incident taught me the importance of considering regional usage patterns when designing global systems."

### The Performance Bottleneck

"As our user base grew to over 100,000 active daily users, we started noticing increased latency in our transaction processing API. Our P95 response time doubled from 300ms to over 600ms in just two weeks.

After extensive profiling, we discovered the issue was in our sanction screening service, which was making synchronous HTTP calls to external compliance providers. As transaction volume increased, the external APIs couldn't scale with our demand.

I proposed and implemented a two-part solution:

1. First, we implemented a local caching layer for frequent checks using a probabilistic data structure:

```java
@Service
public class CachingScreeningService implements ScreeningService {
    private final ExternalScreeningService externalService;
    private final ScreeningResultRepository resultRepository;
    private final BloomFilter<String> screeningCache;
    
    public CachingScreeningService(ExternalScreeningService externalService) {
        this.externalService = externalService;
        // Create a Bloom filter with 1% false positive rate
        this.screeningCache = BloomFilter.create(
            Funnels.stringFunnel(StandardCharsets.UTF_8),
            1_000_000,
            0.01);
            
        // Pre-load known sanctioned identifiers
        List<String> sanctionedIds = resultRepository.findAllSanctionedIdentifiers();
        for (String id : sanctionedIds) {
            screeningCache.put(id);
        }
    }
    
    @Override
    public ScreeningResult screenTransaction(ScreeningRequest request) {
        // Check if definitely not in sanctions list (Bloom filter never has false negatives)
        if (!screeningCache.mightContain(request.getIdentifier())) {
            return ScreeningResult.passed();
        }
        
        // Potential match requires thorough check
        return externalService.screenTransaction(request);
    }
}
```

2. Next, we developed an asynchronous processing pipeline for non-urgent transactions:

```java
@Service
public class AsyncScreeningService {
    private final ScreeningService screeningService;
    private final TransactionRepository transactionRepository;
    private final NotificationService notificationService;
    
    @Async("screeningExecutor")
    public CompletableFuture<ScreeningResult> screenTransactionAsync(
            Transaction transaction, ScreeningRequest request) {
        
        // Update transaction status
        transaction.setStatus(TransactionStatus.SCREENING_IN_PROGRESS);
        transactionRepository.save(transaction);
        
        // Perform screening in background thread
        ScreeningResult result = screeningService.screenTransaction(request);
        
        // Update transaction based on result
        if (result.isPassed()) {
            transaction.setStatus(TransactionStatus.SCREENING_PASSED);
        } else {
            transaction.setStatus(TransactionStatus.SCREENING_FAILED);
            notificationService.notifyComplianceTeam(transaction, result);
        }
        
        transactionRepository.save(transaction);
        return CompletableFuture.completedFuture(result);
    }
}
```

These implementations brought our P95 response time back down to under 200ms even as we continued to scale. The most rewarding feedback came from users in regions with poor connectivity who noticed the improved responsiveness immediately."

### The Compliance Conundrum

"One of our most daunting challenges emerged when we expanded to our 10th country. Each country had different regulatory requirements for transaction verification, documentation, and reporting. Initially, we had hardcoded these rules into our compliance service, but this approach became unsustainable.

I proposed and implemented a dynamic rules engine that allowed us to externalize compliance rules as configurable entities:

```java
@Service
public class DynamicComplianceService {
    private final RuleRepository ruleRepository;
    private final RuleEngine ruleEngine;
    
    public ComplianceResult evaluateCompliance(Transaction transaction) {
        // Fetch country-specific rules
        List<ComplianceRule> countryRules = ruleRepository.findByCountryCode(
            transaction.getDestinationCountry());
            
        // Evaluate all applicable rules
        RuleEvaluationContext context = RuleEvaluationContext.builder()
            .transaction(transaction)
            .timestamp(LocalDateTime.now())
            .build();
            
        return ruleEngine.evaluate(countryRules, context);
    }
}

// The Rule interface that all compliance rules implement
public interface ComplianceRule {
    String getRuleId();
    String getDescription();
    RuleResult evaluate(RuleEvaluationContext context);
    RuleSeverity getSeverity();
}
```

This solution reduced new country onboarding time from 3-4 weeks to just 2-3 days, dramatically accelerating our global expansion."

### The Handling of Cross-Border Failures

"Perhaps the most challenging technical problem we faced was handling transaction failures that occurred mid-process across different banking systems. Money might leave the sender's account but fail to reach the recipient due to issues with correspondent banks or local banking infrastructure.

We implemented what we called 'Transaction Reconciliation & Recovery' - a subsystem dedicated to detecting, diagnosing and resolving failed or stuck transactions:

```java
@Service
public class TransactionReconciliationService {
    private final TransactionRepository transactionRepository;
    private final PaymentGatewayAdapter paymentGateway;
    private final NotificationService notificationService;
    
    @Scheduled(fixedRate = 300000) // Run every 5 minutes
    public void reconcileTransactions() {
        // Find transactions that have been in an intermediate state for too long
        List<Transaction> suspectedStuckTransactions = transactionRepository
            .findByStatusAndLastUpdatedBefore(
                TransactionStatus.PROCESSING_PAYMENT,
                LocalDateTime.now().minusHours(1)
            );
            
        for (Transaction transaction : suspectedStuckTransactions) {
            // Check with payment gateway for actual status
            PaymentStatus gatewayStatus = paymentGateway
                .checkStatus(transaction.getExternalId());
                
            if (gatewayStatus.isCompleted() && 
                    transaction.getStatus() != TransactionStatus.COMPLETED) {
                // Gateway shows completed but our system doesn't - fix the discrepancy
                transaction.setStatus(TransactionStatus.COMPLETED);
                transactionRepository.save(transaction);
                notificationService.sendCompletionNotification(transaction);
                
            } else if (gatewayStatus.isFailed()) {
                // Gateway confirms failure - initiate recovery process
                transaction.setStatus(TransactionStatus.FAILED);
                transactionRepository.save(transaction);
                initiateRecoveryProcess(transaction);
            } else if (gatewayStatus.isProcessing()) {
                // Still processing - update timeout threshold based on destination country
                updateTimeoutThreshold(transaction);
            }
        }
    }
    
    private void initiateRecoveryProcess(Transaction transaction) {
        // Create recovery task
        RecoveryTask task = RecoveryTask.builder()
            .transactionId(transaction.getId())
            .recoveryType(determineRecoveryType(transaction))
            .status(RecoveryStatus.INITIATED)
            .createdAt(LocalDateTime.now())
            .build();
            
        recoveryTaskRepository.save(task);
        
        // Notify appropriate teams based on recovery type
        notificationService.notifyRecoveryTeam(task);
        
        // Notify customer about the issue and recovery process
        notificationService.sendRecoveryNotification(transaction);
    }
}
```

This system achieved two critical outcomes:
1. Automatic resolution of 78% of stuck transactions without human intervention
2. Clear, prompt communication to users when issues occurred, increasing trust even when problems happened

What I learned from this challenge was that in financial systems, gracefully handling failure scenarios is often more important than preventing every possible failure."

## 🧠 Key Lessons for System Design Interviews

"From the NovoRemitAll journey, I extracted several key principles that I now apply to all system designs:

### 1. Embrace Eventual Consistency

"In distributed financial systems, pursuing strict consistency across all components can severely limit scalability. Instead, we identified which parts of the system truly needed immediate consistency (e.g., account balances) and where eventual consistency was acceptable (e.g., reporting data).

This approach allowed us to use different data storage strategies optimized for specific access patterns rather than forcing a one-size-fits-all solution."

### 2. Design for Observability From Day One

"We invested heavily in observability from the beginning, implementing:
- Distributed tracing across all microservices
- Structured logging with consistent correlation IDs
- Detailed metrics for business and technical processes
- Custom health checks that verified business functionality

This investment paid off tremendously when debugging complex issues spanning multiple services. Being able to trace a single transaction across all systems made the difference between hours and days of debugging time."

### 3. Build Safety Valves Into Critical Flows

"For every critical system, we implemented 'safety valves' - mechanisms to handle overload conditions gracefully:
- Transaction rate limiting to prevent overwhelming downstream systems
- Circuit breakers to fail fast when external dependencies had issues
- Fallback mechanisms for critical features when primary implementation was unavailable
- Automatic alerts with specific actionable information

These patterns created a self-healing system that could operate even during partial outages."

### 4. Real Users Over Theoretical Best Practices

"Perhaps the most important lesson was prioritizing real user needs over theoretical architectural purity. For example, we chose to maintain a complete transaction history in an easily queryable form, even though purists might argue this violated microservice data isolation principles.

This decision made customer support vastly more effective and allowed us to provide users with detailed transaction history - features that mattered more to our business success than adherence to any specific architectural pattern."

## 💫 Legacy Beyond Code: The Human Impact

"The most fulfilling aspect of building NovoRemitAll wasn't the technical achievements but the human impact. We received messages from:

- A Filipino nurse in Dubai who used NovoRemitAll to send emergency funds to her daughter for surgery within minutes
- A construction worker in Qatar who saved enough on remittance fees over six months to finally visit his family
- A student in London who could pay her mother's medical bills in Kenya instantly after an accident

These stories reminded us that behind every transaction, API call, and database record was a human need. This understanding fundamentally changed how we prioritized features and fixed bugs - not just based on technical severity, but on human impact.

In system design interviews now, I bring this perspective: technology exists to serve human needs, and the best architectures are those that create reliable, accessible systems for real people."
