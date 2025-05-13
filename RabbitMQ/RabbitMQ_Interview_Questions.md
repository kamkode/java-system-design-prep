<div align="center">

# 🐇 RabbitMQ Interview Questions and Answers

[![RabbitMQ](https://img.shields.io/badge/RabbitMQ-FF6600?style=for-the-badge&logo=rabbitmq&logoColor=white)](https://www.rabbitmq.com/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](https://opensource.org/licenses/MIT)
[![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg)](https://github.com/yourusername/rabbitmq-interview/pulls)

*A comprehensive guide to mastering RabbitMQ concepts for your next interview* 💼

</div>

<div align="center">
  <p>
    <a href="#core-concepts">Core Concepts</a> •
    <a href="#exchange-types">Exchange Types</a> •
    <a href="#message-patterns">Patterns</a> •
    <a href="#performance">Performance</a> •
    <a href="#real-world">Real World</a>
  </p>
  <hr>
</div>

## 📋 Table of Contents

<details>
<summary>🔍 Click to expand/collapse table of contents</summary>

<div style="margin: 15px 0;">

| Section | Description | Key Topics |
|---------|-------------|-------------|
| [🔍 Core Concepts](#core-concepts) | Fundamentals | Architecture, Components |
| [🔄 Exchange Types](#exchange-types-and-routing) | Routing Logic | Direct, Topic, Fanout, Headers |
| [📨 Message Patterns](#message-patterns) | Communication | Work Queues, Pub/Sub, RPC |
| [⚙️ Configuration](#configuration-and-management) | Setup | HA, Clustering, Persistence |
| [⚡ Performance](#performance-and-scaling) | Optimization | Tuning, Backpressure, Scaling |
| [🔒 Security](#security) | Protection | Authentication, SSL/TLS |
| [📊 Monitoring](#monitoring-and-maintenance) | Observability | Metrics, Alerts |
| [🌐 Real-world Scenarios](#real-world-scenarios) | Practical Use Cases | System Design |
| [🆚 RabbitMQ vs Others](#rabbitmq-vs-other-brokers) | Comparison | Kafka, ActiveMQ |

</div>
</details>

<div style="text-align: center; margin: 25px 0;">
  <hr style="border: 0; height: 1px; background: #e1e4e8;">
  <p style="color: #6a737d; font-size: 0.9em;">Jump to: 
    <a href="#core-concepts">Core</a> • 
    <a href="#exchange-types">Exchanges</a> • 
    <a href="#message-patterns">Patterns</a> • 
    <a href="#performance">Performance</a> • 
    <a href="#real-world">Real World</a>
  </p>
</div>

---
- [Core Concepts](#core-concepts)
- [Exchange Types and Routing](#exchange-types-and-routing)
- [Message Patterns](#message-patterns)
- [Configuration and Management](#configuration-and-management)
- [Performance and Scaling](#performance-and-scaling)
- [Error Handling and Reliability](#error-handling-and-reliability)
- [Security](#security)
- [Monitoring and Maintenance](#monitoring-and-maintenance)
- [Real-world Scenarios](#real-world-scenarios)
- [RabbitMQ vs Other Brokers](#rabbitmq-vs-other-brokers)

## 🔍 Core Concepts

## 🔍 Core Concepts

### Q1: What is RabbitMQ and what are its key features?

<div align="center" style="margin: 20px 0;">
  <img src="https://www.rabbitmq.com/img/tutorials/intro/hello-world-example-routing.png" 
       alt="RabbitMQ Architecture" 
       style="max-width: 80%; border: 1px solid #eee; border-radius: 8px; box-shadow: 0 4px 8px rgba(0,0,0,0.1);">
  <p><em>Figure 1: Basic RabbitMQ Message Flow</em></p>
</div>

<div align="center">
  <img src="https://www.rabbitmq.com/img/tutorials/intro/hello-world-example-routing.png" alt="RabbitMQ Architecture" width="600">
  <p><em>Basic RabbitMQ Message Flow</em></p>
</div>
<details><summary>📝 **Explanation**</summary>  
RabbitMQ is an open-source message broker that implements the Advanced Message Queuing Protocol (AMQP). It helps in building distributed and decoupled applications by enabling asynchronous messaging between services.

**Key Features:**
- Multiple messaging protocols (AMQP 0-9-1, MQTT, STOMP)
- Message queuing and routing
- Reliable delivery with message acknowledgments
- Flexible routing with multiple exchange types
- Clustering and high availability
- Management UI and HTTP API
</details>

### Q2: Explain the basic architecture of RabbitMQ.
<details><summary>📝 **Explanation**</summary>  
RabbitMQ follows a messaging model with these core components:

1. **Producer/Publisher**: Application that sends messages
2. **Consumer**: Application that receives messages
3. **Queue**: Buffer that stores messages
4. **Exchange**: Receives messages from producers and routes them to queues
5. **Binding**: Link between exchange and queue with a routing key
6. **Connection**: TCP connection between application and RabbitMQ broker
7. **Channel**: Lightweight connection inside a connection
8. **Virtual Host**: Logical grouping of resources (like a namespace)
</details>

## 🔄 Exchange Types and Routing

<div class="tip" style="background: #f8f9fa; border-left: 4px solid #42b983; padding: 12px; margin: 20px 0; border-radius: 4px;">
💡 <strong>Pro Tip:</strong> Choose the right exchange type based on your routing needs - Direct for 1:1, Topic for pattern matching, Fanout for broadcasting, and Headers for complex routing logic.
</div>

<div align="center">
  <img src="https://www.rabbitmq.com/img/tutorials/intro/exchanges.png" alt="RabbitMQ Exchanges" width="600">
  <p><em>RabbitMQ Exchange Types</em></p>
</div>

### Q3: What are the different types of exchanges in RabbitMQ?
<details><summary>📝 **Explanation**</summary>  

1. **Direct Exchange**: Routes messages to queues based on exact routing key matching
   ```java
   channel.exchangeDeclare("direct_logs", "direct");
   channel.queueBind(queueName, "direct_logs", "error");
   ```

2. **Fanout Exchange**: Broadcasts messages to all bound queues
   ```java
   channel.exchangeDeclare("logs", "fanout");
   ```

3. **Topic Exchange**: Routes messages based on wildcard matching
   ```java
   channel.exchangeDeclare("topic_logs", "topic");
   channel.queueBind(queueName, "topic_logs", "*.error.#");
   ```

4. **Headers Exchange**: Routes based on message headers
   ```java
   Map<String, Object> headers = new HashMap<>();
   headers.put("x-match", "all");
   headers.put("format", "pdf");
   channel.queueBind(queueName, "headers_exchange", "", headers);
   ```
</details>

## 📨 Message Patterns

<div class="note" style="background: #e7f5ff; border-left: 4px solid #4dabf7; padding: 12px; margin: 20px 0; border-radius: 4px;">
📌 <strong>Note:</strong> Each pattern has its use case - Work Queues for task distribution, Pub/Sub for broadcasting, and RPC for request-response scenarios.
</div>

<div align="center">
  <img src="https://www.rabbitmq.com/img/tutorials/python-two.png" alt="Work Queue Pattern" width="400">
  <p><em>Work Queue Pattern</em></p>
</div>

### Q4: Explain the Work Queue pattern in RabbitMQ.
<details><summary>📝 **Explanation**</summary>  
The Work Queue pattern is used to distribute time-consuming tasks among multiple workers.

**Implementation:**
```java
// Producer
String message = "Task " + i;
channel.basicPublish("", "task_queue", 
    MessageProperties.PERSISTENT_TEXT_PLAIN,
    message.getBytes());

// Consumer (Worker)
channel.basicQos(1); // Fair dispatch
channel.basicConsume("task_queue", false, deliverCallback, consumerTag -> { });
```
</details>

## ⚙️ Configuration and Management

<div class="warning" style="background: #fff3bf; border-left: 4px solid #ffd43b; padding: 12px; margin: 20px 0; border-radius: 4px;">
⚠️ <strong>Important:</strong> Always test configuration changes in a staging environment before applying to production. Backup your configurations and monitor the system after changes.
</div>

<div align="center">
  <img src="https://www.rabbitmq.com/img/tutorials/clustering.png" alt="RabbitMQ Clustering" width="600">
  <p><em>RabbitMQ Clustering</em></p>
</div>

### Q5: How do you configure RabbitMQ for high availability?
<details><summary>📝 **Explanation**</summary>  

**1. Clustering:**
```bash
# On each node
rabbitmqctl stop_app
rabbitmqctl reset
rabbitmqctl join_cluster rabbit@<master-node>
rabbitmqctl start_app
```

**2. Queue Mirroring:**
```java
Map<String, Object> args = new HashMap<>();
args.put("x-ha-policy", "all");
channel.queueDeclare("ha-queue", true, false, false, args);
```
</details>

## ⚡ Performance and Scaling

<div class="performance-tip" style="background: #f3f0ff; border-left: 4px solid #845ef7; padding: 12px; margin: 20px 0; border-radius: 4px;">
🚀 <strong>Performance Tip:</strong> Monitor these key metrics: message rates, queue lengths, memory usage, and disk I/O. Set up alerts for thresholds to prevent issues.
</div>

<div align="center">
  <img src="https://www.rabbitmq.com/img/tutorials/prefetch-count.png" alt="Prefetch Count" width="500">
  <p><em>Consumer Prefetch Count</em></p>
</div>

### Q6: How do you improve RabbitMQ performance?
<details><summary>📝 **Explanation**</summary>  

**1. Connection and Channel Management:**
- Reuse connections
- Use multiple channels per connection

**2. Message Batching:**
```java
channel.confirmSelect();
for (int i = 0; i < BATCH_SIZE; i++) {
    channel.basicPublish("exchange", "routingKey", null, message.getBytes());
}
channel.waitForConfirmsOrDie(5_000);
```

**3. Consumer Prefetch:**
```java
int prefetchCount = 10;
channel.basicQos(prefetchCount);
```
</details>

## 🔄 Error Handling and Reliability

<div class="error-tip" style="background: #ffecf1; border-left: 4px solid #f06595; padding: 12px; margin: 20px 0; border-radius: 4px;">
🔍 <strong>Best Practice:</strong> Always implement proper error handling, use DLX for failed messages, and set up monitoring for dead letter queues.
</div>

<div align="center">
  <img src="https://www.rabbitmq.com/img/tutorials/dlx.png" alt="Dead Letter Exchange" width="600">
  <p><em>Dead Letter Exchange Flow</em></p>
</div>

### Q7: How does RabbitMQ handle message acknowledgments?
<details><summary>📝 **Explanation**</summary>  

**1. Basic Acknowledgment (ACK):**
```java
channel.basicConsume(queueName, false, (consumerTag, delivery) -> {
    try {
        // Process message
        channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
    } catch (Exception e) {
        channel.basicNack(delivery.getEnvelope().getDeliveryTag(), false, true);
    }
}, consumerTag -> { });
```
</details>

## 🔒 Security

<div class="security-note" style="background: #e6fcf5; border-left: 4px solid #20c997; padding: 12px; margin: 20px 0; border-radius: 4px;">
🔐 <strong>Security Note:</strong> Always use SSL/TLS for production traffic, implement proper authentication, and follow the principle of least privilege for user permissions.
</div>

<div align="center">
  <img src="https://www.rabbitmq.com/img/tutorials/access-control.png" alt="Access Control" width="500">
  <p><em>RabbitMQ Access Control</em></p>
</div>

### Q8: How do you secure a RabbitMQ installation?
<details><summary>📝 **Explanation**</summary>  

**1. Authentication and Authorization:**
```bash
rabbitmqctl add_user admin StrongPassword123
rabbitmqctl set_user_tags admin administrator
rabbitmqctl set_permissions -p / admin ".*" ".*" ".*"
```

**2. SSL/TLS Configuration:**
```
listeners.ssl.default = 5671
ssl_options.cacertfile = /path/to/ca_certificate.pem
ssl_options.certfile = /path/to/server_certificate.pem
ssl_options.keyfile = /path/to/server_key.pem
```
</details>

## 📊 Monitoring and Maintenance

<div class="monitoring-tip" style="background: #e3fafc; border-left: 4px solid #3bc9db; padding: 12px; margin: 20px 0; border-radius: 4px;">
📈 <strong>Monitoring Tip:</strong> Set up alerts for queue lengths, consumer counts, and node health. Regularly check logs and performance metrics.
</div>

<div align="center">
  <img src="https://www.rabbitmq.com/img/tutorials/management-screenshot.png" alt="Management UI" width="800">
  <p><em>RabbitMQ Management UI</em></p>
</div>

### Q9: How do you monitor RabbitMQ?
<details><summary>📝 **Explanation**</summary>  

**1. Built-in Management UI:**
- Access at http://server:15672

**2. HTTP API:**
```bash
curl -u guest:guest http://localhost:15672/api/overview
```

**3. Key Metrics to Monitor:**
- Message rates
- Queue lengths
- Memory and disk usage
- Node health
</details>

## 🌐 Real-world Scenarios

<div class="scenario-tip" style="background: #f8f0fc; border-left: 4px solid #b197fc; padding: 12px; margin: 20px 0; border-radius: 4px;">
💡 <strong>Scenario Tip:</strong> When designing systems with RabbitMQ, consider message ordering, delivery guarantees, and error handling from the start.
</div>

<div align="center">
  <img src="https://www.rabbitmq.com/img/tutorials/scenario-notification.png" alt="Notification System" width="600">
  <p><em>Notification System Architecture</em></p>
</div>

### Q10: How would you design a reliable order processing system using RabbitMQ?
<details><summary>📝 **Explanation**</summary>  

**Architecture:**
```
[Order Service] -> [Order Exchange] -> [Order Queue] -> [Order Processor]
                                     -> [DLX] -> [Order DLQ]
```

**Implementation:**
```java
// Publish order
String orderId = UUID.randomUUID().toString();
AMQP.BasicProperties props = new AMQP.BasicProperties.Builder()
    .messageId(orderId)
    .contentType("application/json")
    .deliveryMode(2)
    .build();
channel.basicPublish("orders", "new", props, message.getBytes());
```
</details>

## 🔄 Advanced Messaging Patterns

<div class="advanced-tip" style="background: #f3f0ff; border-left: 4px solid #845ef7; padding: 12px; margin: 20px 0; border-radius: 4px;">
🎓 <strong>Advanced Topic:</strong> These patterns are commonly used in distributed systems. Understand their trade-offs and when to use each one.
</div>

<div align="center">
  <img src="https://www.rabbitmq.com/img/tutorials/rpc-message-flow.png" alt="RPC Pattern" width="600">
  <p><em>RPC Message Flow</em></p>
</div>

### Q11: Explain the Request/Reply (RPC) pattern in RabbitMQ.
<details><summary>📝 **Explanation**</summary>  
The Request/Reply pattern allows clients to send a request and wait for a response, similar to a remote procedure call.

**Implementation:**
```java
// Client (Request)
String replyQueueName = channel.queueDeclare().getQueue();
String corrId = UUID.randomUUID().toString();

AMQP.BasicProperties props = new AMQP.BasicProperties
    .Builder()
    .correlationId(corrId)
    .replyTo(replyQueueName)
    .build();

channel.basicPublish("", "rpc_queue", props, message.getBytes());

// Wait for response
final CompletableFuture<String> response = new CompletableFuture<>();
String ctag = channel.basicConsume(replyQueueName, true, (consumerTag, delivery) -> {
    if (delivery.getProperties().getCorrelationId().equals(corrId)) {
        response.complete(new String(delivery.getBody(), "UTF-8"));
    }
}, consumerTag -> { });

String result = response.get();
channel.basicCancel(ctag);
```

**Server (Responder):**
```java
channel.queueDeclare("rpc_queue", false, false, false, null);
channel.basicQos(1);

DeliverCallback deliverCallback = (consumerTag, delivery) -> {
    AMQP.BasicProperties replyProps = new AMQP.BasicProperties
        .Builder()
        .correlationId(delivery.getProperties().getCorrelationId())
        .build();

    String response = "";
    try {
        String message = new String(delivery.getBody(), "UTF-8");
        // Process message
        response = "Processed: " + message;
    } finally {
        channel.basicPublish("", delivery.getProperties().getReplyTo(), 
            replyProps, response.getBytes("UTF-8"));
        channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
    }
};

channel.basicConsume("rpc_queue", false, deliverCallback, consumerTag -> { });
```
</details>

### Q12: How do you handle message priority in RabbitMQ?
<details><summary>📝 **Explanation**</summary>  
RabbitMQ supports priority queues where messages with higher priority are consumed first.

**Implementation:**
```java
// Enable priority queue (max priority = 10)
Map<String, Object> args = new HashMap<>();
args.put("x-max-priority", 10);
channel.queueDeclare("priority_queue", true, false, false, args);

// Publishing message with priority
AMQP.BasicProperties props = new AMQP.BasicProperties
    .Builder()
    .priority(5)  // Priority from 0 to x-max-priority
    .build();
channel.basicPublish("", "priority_queue", props, message.getBytes());
```

**Important Notes:**
- Consumer must be available when high-priority messages arrive
- Only works with Classic Queues, not Quorum Queues
- Higher numbers indicate higher priority
</details>

### Q13: What are Dead Letter Exchanges (DLX) and when would you use them?
<details><summary>📝 **Explanation**</summary>  
A Dead Letter Exchange is used to handle messages that cannot be processed successfully.

**Common Use Cases:**
- Messages that are rejected or nacked
- Messages that expire (TTL)
- Messages that exceed queue length limits

**Configuration:**
```java
// 1. Declare the DLX and DLQ
channel.exchangeDeclare("dlx", "direct");
channel.queueDeclare("dlq", true, false, false, null);
channel.queueBind("dlq", "dlx", "");

// 2. Configure main queue with DLX
Map<String, Object> args = new HashMap<>();
args.put("x-dead-letter-exchange", "dlx");
args.put("x-message-ttl", 60000);  // Optional: Set TTL

channel.queueDeclare("main_queue", true, false, false, args);

// 3. When rejecting a message
channel.basicReject(deliveryTag, false);  // Set requeue=false to send to DLX
```

**Best Practices:**
- Monitor DLQ size
- Set up alerts for DLQ growth
- Implement a process to inspect and republish or discard dead letters
</details>

## ⚡ Performance and Tuning

<div align="center">
  <img src="https://www.rabbitmq.com/img/tutorials/performance-tuning.png" alt="Performance Tuning" width="600">
  <p><em>Performance Optimization Techniques</em></p>
</div>

### Q14: How do you handle backpressure in RabbitMQ?
<details><summary>📝 **Explanation**</summary>  
Backpressure occurs when a consumer cannot keep up with the message publishing rate.

**Solutions:**

1. **Consumer Prefetch Count:**
```java
// Limit number of unacknowledged messages per consumer
int prefetchCount = 10;
channel.basicQos(prefetchCount);
```

2. **Publisher Confirms:**
```java
// Enable publisher confirms
channel.confirmSelect();

// Add confirm listener
channel.addConfirmListener((sequenceNumber, multiple) -> {
    // Message confirmed
}, (sequenceNumber, multiple) -> {
    // Message nack-ed, handle retry
});
```

3. **Flow Control:**
```java
// Enable publisher confirms with flow control
channel.confirmSelect();
channel.addReturnListener((replyCode, replyText, exchange, routingKey, properties, body) -> {
    // Handle returned messages
});

// Publish with mandatory flag
channel.basicPublish("exchange", "routingKey", true, null, message.getBytes());
```
</details>

### Q15: How do you handle message deduplication in RabbitMQ?
<details><summary>📝 **Explanation**</summary>  

**1. Message Deduplication Techniques:**

**a) Using Message ID:**
```java
// Producer
String messageId = UUID.randomUUID().toString();
AMQP.BasicProperties props = new AMQP.BasicProperties
    .Builder()
    .messageId(messageId)
    .build();
channel.basicPublish("exchange", "routingKey", props, message.getBytes());
```

**b) Using Redis for Deduplication:**
```java
// Consumer
String messageId = properties.getMessageId();
if (redis.setnx("dedup:" + messageId, "1") == 1) {
    // Process message
    redis.expire("dedup:" + messageId, 24 * 60 * 60); // 24h TTL
} else {
    // Duplicate message, skip processing
    channel.basicAck(deliveryTag, false);
}
```

**2. Idempotent Consumers:**
- Design consumers to handle duplicate messages
- Use database unique constraints
- Implement idempotent operations
</details>

## 🆚 RabbitMQ vs Other Brokers

<div class="comparison-tip" style="background: #fff9db; border-left: 4px solid #ffd43b; padding: 12px; margin: 20px 0; border-radius: 4px;">
🤔 <strong>Choosing a Broker:</strong> RabbitMQ excels in complex routing and enterprise messaging, while Kafka is better for high-throughput event streaming.
</div>

<div align="center">
  <img src="https://www.rabbitmq.com/img/tutorials/rabbitmq-vs-kafka.png" alt="RabbitMQ vs Kafka" width="700">
  <p><em>RabbitMQ vs Kafka Comparison</em></p>
</div>

### Q16: Compare RabbitMQ with Apache Kafka.
<details><summary>📝 **Explanation**</summary>  

| Feature | RabbitMQ | Apache Kafka |
|---------|----------|-------------|
| Architecture | Message Broker | Distributed Streaming Platform |
| Protocol | AMQP, MQTT, STOMP | Custom protocol |
| Message Ordering | Per-queue | Per-partition |
| Throughput | Up to 50K msgs/sec | Up to millions msgs/sec |
| Message Retention | Until consumed | Configurable retention period |
| Use Cases | Task queues, RPC, pub/sub | Event streaming, log aggregation |
| Complexity | Lower | Higher |
| Delivery Semantics | At-most-once, At-least-once | At-least-once, Exactly-once |
| Message Ordering | Per-queue | Per-partition |
| Throughput | Up to 50K msgs/sec | Millions of msgs/sec |
| Message Retention | Until consumed | Configurable retention period |

**When to use RabbitMQ:**
- Complex routing needs
- Priority queues and message TTL
- When you need request/reply pattern
- Lower latency requirements
- When you need flexible messaging patterns

**When to use Kafka:**
- High throughput requirements
- Event sourcing
- Stream processing
- When you need to replay messages
- For log aggregation

## 🏗️ Real-world Interview Scenarios

<div class="interview-tip" style="background: #e6fcf5; border-left: 4px solid #20c997; padding: 12px; margin: 20px 0; border-radius: 4px;">
🎯 <strong>Interview Tip:</strong> Be prepared to discuss trade-offs in your design choices, such as delivery guarantees vs performance, and how you would handle failures.
</div>

<div align="center">
  <img src="https://www.rabbitmq.com/img/tutorials/scenario-ecommerce.png" alt="E-commerce Scenario" width="700">
  <p><em>E-commerce Order Processing Flow</em></p>
</div>

### Q17: How would you design a scalable notification system using RabbitMQ?
<details><summary>📝 **Explanation**</summary>  

**Architecture:**
```
[Notification Service] -> [Notification Exchange] -> [Priority Queues] -> [Workers]
                                          |
                                          -> [DLX] -> [Failed Notifications]
```

**Implementation:**

1. **Priority Queues for Different Notification Types:**
```java
// High priority for real-time notifications
Map<String, Object> args = new HashMap<>();
args.put("x-max-priority", 10);
channel.queueDeclare("high_priority", true, false, false, args);

// Low priority for batch/email notifications
channel.queueDeclare("low_priority", true, false, false, args);

// Bind to exchange
channel.queueBind("high_priority", "notifications", "high");
channel.queueBind("low_priority", "notifications", "low");
```

2. **Publishing Notifications:**
```java
public void sendNotification(Notification notification, Priority priority) {
    String routingKey = priority == Priority.HIGH ? "high" : "low";
    
    AMQP.BasicProperties props = new AMQP.BasicProperties
        .Builder()
        .priority(priority == Priority.HIGH ? 5 : 1)
        .deliveryMode(2)  // Persistent
        .build();
        
    channel.basicPublish("notifications", routingKey, props, 
        objectMapper.writeValueAsBytes(notification));
}
```

3. **Handling Failures with DLX:**
```java
// Configure DLX
channel.exchangeDeclare("notifications_dlx", "direct");
channel.queueDeclare("failed_notifications", true, false, false, null);
channel.queueBind("failed_notifications", "notifications_dlx", "");

// Configure main queue with DLX
Map<String, Object> args = new HashMap<>();
args.put("x-dead-letter-exchange", "notifications_dlx");
args.put("x-max-retries", 3);
channel.queueDeclare("high_priority", true, false, false, args);
```

4. **Consumer with Retry Logic:**
```java
channel.basicConsume("high_priority", false, (consumerTag, delivery) -> {
    try {
        Notification notification = objectMapper.readValue(
            delivery.getBody(), Notification.class);
        
        // Process notification
        notificationService.send(notification);
        
        // Acknowledge on success
        channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
    } catch (Exception e) {
        // Get current retry count
        Map<String, Object> headers = delivery.getProperties().getHeaders();
        int retryCount = headers != null && headers.containsKey("x-retry-count") 
            ? (int) headers.get("x-retry-count") : 0;
            
        if (retryCount < MAX_RETRIES) {
            // Requeue with retry count
            Map<String, Object> newHeaders = new HashMap<>();
            newHeaders.put("x-retry-count", retryCount + 1);
            
            AMQP.BasicProperties props = new AMQP.BasicProperties
                .Builder()
                .headers(newHeaders)
                .build();
                
            channel.basicPublish("", delivery.getEnvelope().getRoutingKey(),
                true, props, delivery.getBody());
        } else {
            // Send to DLX
            channel.basicReject(delivery.getEnvelope().getDeliveryTag(), false);
        }
    }
}, consumerTag -> { });
```

**Scaling Considerations:**
- Scale workers based on queue length
- Use separate connections for publishers and consumers
- Monitor queue lengths and consumer counts
- Implement circuit breakers for external services
</details>

### Q18: How would you migrate from RabbitMQ to Kafka (or vice versa)?
<details><summary>📝 **Explanation**</summary>  

**## 🔄 Migration Strategy

<div class="migration-tip" style="background: #fff3bf; border-left: 4px solid #ffd43b; padding: 12px; margin: 20px 0; border-radius: 4px;">
🔄 <strong>Migration Tip:</strong> Always run systems in parallel during migration, validate data consistency, and have a rollback plan in case of issues.
</div>**

<div align="center">
  <img src="https://www.rabbitmq.com/img/tutorials/migration-strategy.png" alt="Migration Strategy" width="700">
  <p><em>Dual-Write Migration Approach</em></p>
</div>

1. **Dual-Write Phase:**
   - Write to both systems simultaneously
   - Keep RabbitMQ as primary initially
   ```java
   public void sendMessage(Message message) {
       // Send to RabbitMQ
       rabbitTemplate.convertAndSend("exchange", "routingKey", message);
       
       // Send to Kafka
       kafkaTemplate.send("topic", message.getKey(), message);
   }
   ```

2. **Data Validation:**
   - Implement verification jobs
   - Compare message counts and contents
   - Monitor for inconsistencies

3. **Consumer Migration:**
   - Migrate consumers one by one
   - Keep both consumers running during transition
   - Compare outputs for consistency

4. **Cutover:**
   - Make Kafka the primary
   - Keep RabbitMQ as fallback
   - Monitor system behavior

**Challenges and Solutions:**
- **Different Delivery Semantics**: Implement idempotent consumers
- **Ordering**: Use single partition in Kafka or implement ordering logic
- **Message TTL**: Implement custom TTL in Kafka if needed
- **Routing**: Use topic-per-queue pattern in Kafka
</details>
</details>
