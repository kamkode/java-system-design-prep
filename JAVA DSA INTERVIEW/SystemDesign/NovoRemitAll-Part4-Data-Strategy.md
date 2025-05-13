# NovoRemitAll: Data Strategy & Management

## 📊 Data Architecture Overview

At NovoRemitAll, we handle millions of transactions daily, generating terabytes of data. Our data strategy ensures data integrity, availability, and regulatory compliance while supporting both real-time processing and analytical workloads.

```
┌─────────────────────────────────────────────────────────┐
│                  Data Access Layer                      │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐  │
│  │   Primary   │  │    Read     │  │   Cache     │  │
│  │  Database   │◄─┤  Replicas   │◄─┤   Layer     │  │
│  │ (PostgreSQL)│  │             │  │   (Redis)   │  │
│  └──────┬──────┘  └─────────────┘  └──────┬──────┘  │
│         │                   ▲                  │       │
└─────────┼───────────────────┼──────────────────┼───────┘
          │                   │                  │
          ▼                   ▼                  ▼
┌─────────────────────────────────────────────────────────┐
│                Data Processing Layer                   │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐  │
│  │ Transaction │  │  Analytics  │  │  Reporting  │  │
│  │  Service    │  │  Pipeline   │  │   Service   │  │
│  └─────────────┘  └─────────────┘  └─────────────┘  │
└─────────────────────────────────────────────────────────┘
```

## 🗃️ Data Partitioning Strategy

### 1. Horizontal Partitioning (Sharding)
```java
public class TransactionShardingStrategy {
    
    // Shard transactions by date and region
    public String determineShard(Transaction transaction) {
        String region = transaction.getSenderCountry().toLowerCase();
        LocalDate date = transaction.getCreatedAt().toLocalDate();
        return String.format("shard_%s_%d_%02d", 
            region, 
            date.getYear(), 
            (date.getMonthValue() / 4) + 1); // Quarterly shards
    }
}
```

### 2. Vertical Partitioning
- **Hot Data**: Recent transactions (last 6 months) in primary database
- **Warm Data**: 6-24 months old in read-optimized storage
- **Cold Data**: Archived to S3/Glacier after 2 years

## 🔄 Data Replication & Consistency

### Multi-Region Replication
```yaml
# PostgreSQL replication configuration
wal_level = replica
max_wal_senders = 10
hot_standby = on
synchronous_commit = remote_apply
synchronous_standby_names = '*'  # All standbys must confirm
```

### Eventual Consistency with Compensation
```java
@Transactional
public void processTransaction(Transaction tx) {
    // 1. Deduct from sender
    accountService.debit(tx.getSenderId(), tx.getAmount());
    
    // 2. Publish event asynchronously
    eventPublisher.publishEvent(new TransactionInitiatedEvent(
        tx.getId(),
        tx.getSenderId(),
        tx.getRecipientId(),
        tx.getAmount()
    ));
    
    // 3. Compensating transaction in case of failure
    transactionTemplate.execute(status -> {
        // Mark as pending in transaction log
        transactionLogRepository.save(
            new TransactionLog(tx.getId(), "PENDING"));
        return null;
    });
}

@KafkaListener(topics = "transaction-events")
public void handleTransactionEvent(TransactionEvent event) {
    try {
        // Process the transaction
        transactionService.process(event);
        
        // Update transaction log
        transactionLogRepository.updateStatus(
            event.getTransactionId(), 
            "COMPLETED");
            
    } catch (Exception e) {
        // Trigger compensation
        compensationService.compensate(event);
        transactionLogRepository.updateStatus(
            event.getTransactionId(), 
            "FAILED");
    }
}
```

## 🔒 Data Retention & Compliance

### 1. Data Retention Policy
| Data Type       | Retention Period | Storage Class      | Access Pattern |
|-----------------|-----------------|-------------------|----------------|
| Transaction     | 7 years         | Hot/Warm Storage  | Frequent       |
| KYC Documents   | 10 years        | Encrypted Storage | Rare           |
| Audit Logs      | 5 years         | Cold Storage      | Occasional     |
| System Metrics  | 1 year          | Time-series DB    | Frequent       |


### 2. GDPR & CCPA Compliance
- Right to be forgotten implementation
- Data portability endpoints
- Automated data subject access request processing

## 📈 Analytics Pipeline

```
┌─────────────┐   ┌─────────────┐   ┌─────────────┐   ┌─────────────┐
│  Data       │   │  Stream     │   │  Real-time  │   │  Data       │
│  Sources    │──▶│  Processing │──▶│  Analytics  │──▶│  Warehouse  │
│  (Kafka)    │   │  (Flink)    │   │  (Druid)    │   │  (BigQuery) │
└─────────────┘   └─────────────┘   └─────────────┘   └─────────────┘
                                                                  │
                                                                  ▼
                                                         ┌─────────────────┐
                                                         │  BI & Reporting │
                                                         │  (Tableau, etc.) │
                                                         └─────────────────┘
```

## 🛠️ Data Migration Strategy

### 1. Zero-Downtime Migration
```java
public class DataMigrationService {
    
    @Value("${migration.enabled:false}")
    private boolean migrationEnabled;
    
    @Transactional
    public void process(Transaction tx) {
        // Write to both old and new schemas
        legacyRepository.save(convertToLegacy(tx));
        
        if (migrationEnabled) {
            newRepository.save(convertToNewSchema(tx));
        }
    }
    
    // Background job to verify and sync data
    @Scheduled(fixedRate = 3600000) // Every hour
    public void verifyAndSync() {
        List<LegacyTx> legacyTxs = legacyRepository.findUnsynced();
        legacyTxs.forEach(tx -> {
            if (!newRepository.existsById(tx.getId())) {
                newRepository.save(convertToNewSchema(tx));
            }
        });
    }
}
```

## 🔍 Common Interview Questions & Answers

### Q: How do you ensure data consistency across services?
**A:** "We use the Saga pattern with compensating transactions. Each service participating in a transaction emits events, and we have a coordinator service that ensures either all operations complete successfully or appropriate compensating actions are taken. For example, if the recipient's bank is unavailable after debiting the sender, we have automated processes to reverse the transaction and notify the user."

### Q: How do you handle schema migrations?
**A:** "We follow an expand-contract pattern for schema changes. First, we make the schema backward compatible, deploy the application changes, then clean up the old schema. For example, when we needed to add a new field for transaction metadata, we:
1. Added the new column as nullable
2. Deployed code that could handle both old and new schemas
3. Backfilled existing records
4. Made the column non-nullable in a subsequent release"

### Q: How do you ensure data privacy?
**A:** "We implement data minimization, encryption at rest and in transit, and strict access controls. For PII, we use field-level encryption and tokenization. All data access is logged and monitored for suspicious activities. We also have automated data masking in non-production environments."

### Q: How do you handle data recovery?
**A:** "We maintain point-in-time recovery with 15-minute RPO (Recovery Point Objective) and 1-hour RTO (Recovery Time Objective). Our backup strategy includes:
- Continuous WAL archiving
- Daily full backups
- Weekly verification of backup integrity
- Cross-region replication of backups
- Regular disaster recovery drills"

## 🚀 Performance Optimization

### 1. Read/Write Splitting
```java
@Configuration
@EnableJpaRepositories(
    basePackages = "com.novoremitall.repository.read",
    entityManagerFactoryRef = "readEntityManager",
    transactionManagerRef = "readTransactionManager"
)
public class ReadOnlyDataSourceConfig {
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.read")
    public DataSource readDataSource() {
        return DataSourceBuilder.create().build();
    }
    
    // Additional read-only configuration...
}
```

### 2. Caching Strategy
```java
@Service
@CacheConfig(cacheNames = "exchangeRates")
public class ExchangeRateService {
    
    @Cacheable(key = "{#fromCurrency + '-' + #toCurrency}", 
               unless = "#result == null",
               cacheManager = "shortLivedCache")
    public BigDecimal getExchangeRate(String fromCurrency, String toCurrency) {
        // Fetch from external service
    }
    
    @Scheduled(fixedRate = 300000) // 5 minutes
    @CacheEvict(allEntries = true)
    public void evictAllCacheValues() {}
}
```

## 📊 Monitoring & Alerting

### Key Metrics Tracked
- **Database**: Query performance, connection pool usage, replication lag
- **Application**: Request latency, error rates, JVM metrics
- **Business**: Transaction volume, success/failure rates, value by region
- **Infrastructure**: CPU, memory, disk I/O, network

### Alerting Strategy
- **P0 (Critical)**: Immediate page (e.g., transaction failures > 1%)
- **P1 (High)**: Page during business hours (e.g., latency > 2s p95)
- **P2 (Medium)**: Ticket creation (e.g., cache hit rate < 80%)
- **P3 (Low)**: Log and review (e.g., backup job warnings)

## 🧠 Advanced Data Patterns

### 1. Time-Series Data Handling
```java
@TimeWindow(size = "1h")
public class TransactionMetrics {
    @Id
    private String id;
    private String currencyPair;
    private BigDecimal totalAmount;
    private long transactionCount;
    private Instant windowStart;
    private Instant windowEnd;
    
    @TimeWindow.Interval
    public Instant getWindowStart() {
        return windowStart;
    }
}

// Aggregation query example
public interface TransactionMetricsRepository 
    extends JpaRepository<TransactionMetrics, String> {
    
    @Query("SELECT new com.novoremitall.dto.VolumeDTO(" +
           "t.currencyPair, SUM(t.totalAmount) as volume, t.windowStart) " +
           "FROM TransactionMetrics t " +
           "WHERE t.windowStart >= :startTime " +
           "GROUP BY t.currencyPair, t.windowStart")
    List<VolumeDTO> findHourlyVolumes(@Param("startTime") Instant startTime);
}
```

### 2. Data Versioning Strategy
```java
@Entity
@Audited  // Hibernate Envers for audit logging
public class Transaction {
    @Id
    private String id;
    
    @Version
    private Long version;
    
    @Column(updatable = false)
    private Instant createdAt;
    
    @LastModifiedDate
    private Instant lastModifiedAt;
    
    // Business fields
    private BigDecimal amount;
    private String status;
    
    // JSON column for flexible schema evolution
    @Column(columnDefinition = "jsonb")
    private String metadata;
}

// Querying historical versions
AuditReader reader = AuditReaderFactory.get(entityManager);
List<Number> revisions = reader.getRevisions(Transaction.class, transactionId);
Transaction oldVersion = reader.find(Transaction.class, transactionId, revisions.get(0));
```

## 🔄 Data Synchronization Patterns

### 1. Change Data Capture (CDC)
```java
@Configuration
public class CdcConfiguration {
    
    @Bean
    public DebeziumEngine<ChangeEvent<String, String>> debeziumEngine(
            Configuration connectorConfiguration) {
        
        return DebeziumEngine.create(Json.class)
            .using(connectorConfiguration.asProperties())
            .notifying(record -> {
                // Process change event
                handleChangeEvent(record.value());
            }).build();
    }
    
    @Bean
    public io.debezium.config.Configuration customerConnector() {
        return io.debezium.config.Configuration.create()
            .with("name", "transaction-connector")
            .with("connector.class", "io.debezium.connector.postgresql.PostgresConnector")
            .with("offset.storage", "org.apache.kafka.connect.storage.FileOffsetBackingStore")
            .with("offset.storage.file.filename", "/tmp/offsets.dat")
            .with("offset.flush.interval.ms", 60000)
            .with("database.hostname", databaseHostname)
            .with("database.port", databasePort)
            .with("database.user", databaseUser)
            .with("database.password", databasePassword)
            .with("database.dbname", databaseName)
            .with("database.server.name", "dbserver1")
            .with("table.whitelist", "public.transactions")
            .build();
    }
}
```

### 2. Data Mesh Implementation
```java
// Domain-Oriented Data Product
public interface TransactionDataProduct {
    
    @Query("SELECT t FROM Transaction t WHERE t.status = 'COMPLETED'")
    List<Transaction> findCompletedTransactions();
    
    @Query(value = """
        SELECT new com.novoremitall.dto.TransactionStatsDTO(
            t.currencyPair,
            COUNT(t),
            SUM(t.amount),
            AVG(t.amount)
        ) 
        FROM Transaction t 
        WHERE t.timestamp >= :startDate 
        GROUP BY t.currencyPair""")
    List<TransactionStatsDTO> getTransactionStats(
        @Param("startDate") Instant startDate);
    
    // Data quality metrics
    @Query("SELECT COUNT(*) FROM Transaction WHERE amount IS NULL")
    long countTransactionsWithNullAmount();
}

// Data Product Configuration
@Configuration
@EnableJpaRepositories(
    basePackages = "com.novoremitall.dataproducts.transactions",
    entityManagerFactoryRef = "transactionEntityManager",
    transactionManagerRef = "transactionTransactionManager"
)
public class TransactionDataProductConfig {
    // Configuration for the transaction data product
}
```

## 🧪 Data Quality & Testing

### 1. Contract Testing
```java
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMessageVerifier
public class TransactionContractVerifierBase {

    @Test
    void validate_transactionCreatedEvent() throws IOException {
        // Given
        TransactionEvent event = new TransactionEvent(
            "tx123", 
            "user456", 
            "USD", 
            new BigDecimal("100.00"), 
            Instant.now()
        );

        // When
        Message<TransactionEvent> message = MessageBuilder
            .withPayload(event)
            .setHeader("event-type", "transaction.created")
            .build();

        // Then
        ContractVerifierMessage response = 
            contractVerifierMessaging.receive("transaction-events");
        response.getHeader("event-type").isEqualTo("transaction.created");
        
        TransactionEvent receivedEvent = (TransactionEvent) 
            response.getPayload();
        assertThat(receivedEvent.getId()).isEqualTo("tx123");
    }
}
```

### 2. Data Quality Rules
```java
public class TransactionDataQualityRules {
    
    // Rule: Transaction amount must be positive
    public static final Specification<Transaction> AMOUNT_POSITIVE = 
        (root, query, cb) -> cb.greaterThan(root.get("amount"), BigDecimal.ZERO);
    
    // Rule: Currency must be valid ISO code
    public static final Specification<Transaction> VALID_CURRENCY = 
        (root, query, cb) -> root.get("currency").in(Currency.getAvailableCurrencies()
            .stream()
            .map(Currency::getCurrencyCode)
            .collect(Collectors.toSet()));
    
    // Rule: Timestamp must be in the past
    public static final Specification<Transaction> VALID_TIMESTAMP = 
        (root, query, cb) -> cb.lessThan(root.get("timestamp"), cb.currentTimestamp());
    
    // Composite rule combining all checks
    public static Specification<Transaction> ALL_RULES = 
        AMOUNT_POSITIVE.and(VALID_CURRENCY).and(VALID_TIMESTAMP);
}

// Usage in service
public class TransactionValidationService {
    
    @Autowired
    private TransactionRepository transactionRepository;
    
    public List<DataQualityIssue> validateTransaction(Transaction transaction) {
        List<DataQualityIssue> issues = new ArrayList<>();
        
        if (!TransactionDataQualityRules.AMOUNT_POSITIVE
                .toPredicate(transaction, null, null)) {
            issues.add(new DataQualityIssue("INVALID_AMOUNT", 
                "Transaction amount must be positive"));
        }
        
        // Add other validations...
        
        return issues;
    }
}
```

## 🚀 Performance Optimization Techniques

### 1. Read-Model Projection
```java
public interface TransactionProjection {
    String getId();
    BigDecimal getAmount();
    String getCurrency();
    String getStatus();
    
    @Value("#{target.sender.name + ' ' + target.sender.lastName}")
    String getSenderName();
    
    @Value("#{target.recipient.name + ' ' + target.recipient.lastName}")
    String getRecipientName();
    
    @Value("#{@currencyConverter.convert(target.amount, target.currency, 'USD')}")
    BigDecimal getAmountInUsd();
}

// Usage in repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {
    
    @Query("""
        SELECT 
            t.id as id,
            t.amount as amount,
            t.currency as currency,
            t.status as status,
            s as sender,
            r as recipient
        FROM Transaction t
        JOIN t.sender s
        JOIN t.recipient r
        WHERE t.id = :id""")
    Optional<TransactionProjection> findProjectionById(@Param("id") String id);
}
```

### 2. Batch Processing
```java
@Repository
public class TransactionBatchRepository {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @Transactional
    public int batchInsert(List<Transaction> transactions) {
        int batchSize = 50;
        int count = 0;
        
        for (Transaction tx : transactions) {
            entityManager.persist(tx);
            
            if (++count % batchSize == 0) {
                entityManager.flush();
                entityManager.clear();
            }
        }
        
        if (count % batchSize != 0) {
            entityManager.flush();
            entityManager.clear();
        }
        
        return count;
    }
}
```

## 🔍 Interview Preparation: Data Strategy Questions

### Q: How do you handle schema evolution in production?
**A:** "We follow these principles and patterns to ensure smooth schema evolution:

#### 1. Backward Compatibility
- New fields are always nullable with sensible defaults
- Use of `@JsonIgnoreProperties(ignoreUnknown = true)` to handle unknown properties
- Example of safe field addition:

```java
@Entity
public class Transaction {
    // Existing fields...
    
    // New field added with default value
    @Column(nullable = true)
    private String referenceId = "N/A";
    
    // Getters and setters...
}
```

#### 2. Versioned APIs
- API versioning through URL path or headers
- Deprecation policy with sunset headers
- Example of versioned controller:

```java
@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionControllerV1 { /* ... */ }

@RestController
@RequestMapping("/api/v2/transactions")
public class TransactionControllerV2 { /* ... */ }
```

#### 3. Database Migrations with Flyway
- Versioned migrations with rollback capabilities
- Pre-deployment and post-deployment scripts
- Example migration file (`V2__add_reference_id.sql`):

```sql
-- Add nullable column first
ALTER TABLE transactions ADD COLUMN reference_id VARCHAR(50);

-- Backfill with default value
UPDATE transactions SET reference_id = 'N/A' WHERE reference_id IS NULL;

-- Optionally make it non-nullable after backfill
-- ALTER TABLE transactions ALTER COLUMN reference_id SET NOT NULL;
```

#### 4. Dual-Write Pattern for Major Changes
- Temporary write to both old and new schemas
- Background migration for existing data
- Example implementation:

```java
@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final NewTransactionRepository newTransactionRepository;
    
    @Transactional
    public Transaction createTransaction(TransactionRequest request) {
        // 1. Create in old schema
        Transaction tx = transactionRepository.save(mapToEntity(request));
        
        try {
            // 2. Create in new schema (async)
            newTransactionRepository.save(mapToNewEntity(tx));
        } catch (Exception e) {
            // Log and handle error, but don't fail the main operation
            log.error("Failed to write to new schema", e);
        }
        
        return tx;
    }
    
    @Scheduled(fixedDelay = 3600000) // Run hourly
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void reconcileSchemas() {
        // Find records in old schema not yet in new schema
        List<Transaction> unreconciled = transactionRepository
            .findByLastModifiedAfterAndNotInNewSchema(lastRun);
            
        // Migrate them in batches
        unreconciled.forEach(tx -> {
            try {
                newTransactionRepository.save(mapToNewEntity(tx));
            } catch (Exception e) {
                log.error("Failed to migrate transaction: " + tx.getId(), e);
            }
        });
    }
}
```

#### 5. Feature Flags for Gradual Rollout
- Control feature visibility and behavior
- Easy rollback if issues arise
- Example using Togglz:

```java
public enum TransactionFeatures implements Feature {
    @Label("New Reference ID Format")
    NEW_REFERENCE_ID_FORMAT,
    
    @Label("Enhanced Validation")
    ENHANCED_VALIDATION;
    
    public boolean isActive() {
        return FeatureContext.getFeatureManager()
            .isActive(this);
    }
}

// Usage in service
if (TransactionFeatures.ENHANCED_VALIDATION.isActive()) {
    validateEnhanced(request);
} else {
    validateLegacy(request);
}
```

#### 6. Monitoring and Metrics
- Track adoption rate of new schema
- Monitor error rates during transition
- Example metrics:

```java
@Aspect
@Component
@RequiredArgsConstructor
public class SchemaMigrationMetricsAspect {
    private final MeterRegistry meterRegistry;
    
    @AfterReturning(
        "@annotation(org.springframework.transaction.annotation.Transactional) && " +
        "execution(* com.novoremitall..*Repository.save(..))"
    )
    public void trackSchemaUsage(JoinPoint jp) {
        Object entity = jp.getArgs()[0];
        String entityName = entity.getClass().getSimpleName();
        
        // Track usage of new vs old schema
        boolean isNewSchema = entity.getClass().getPackage().getName().contains(".v2");
        meterRegistry.counter("schema.usage", 
            "entity", entityName,
            "schema", isNewSchema ? "new" : "old"
        ).increment();
    }
}
```

This comprehensive approach ensures smooth schema evolution with minimal disruption to the production system."

### Q: How would you design a data warehouse for transaction analysis?
**A:** "I'd implement a star schema with:
- **Fact Tables**: Transaction facts (amount, timestamp, status)
- **Dimension Tables**: Time, Customer, Currency, Country
- **Aggregations**: Pre-computed for common queries
- **ETL Pipeline**: Using tools like Apache Airflow"

### Q: How do you ensure data consistency in a distributed system?
**A:** "We use:
1. **Eventual Consistency**: For non-critical paths
2. **Saga Pattern**: For distributed transactions
3. **Idempotent Operations**: For safe retries
4. **Compensating Transactions**: For rollback"

## 🎯 Key Takeaways

1. **Data Modeling**: Design for both operational and analytical needs
2. **Performance**: Use appropriate patterns for read/write optimization
3. **Quality**: Implement robust validation and testing
4. **Evolution**: Plan for schema changes and data migrations
5. **Governance**: Ensure compliance and data privacy
