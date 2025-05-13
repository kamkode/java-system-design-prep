# System Design Interview Guide

This directory contains resources and examples for system design interviews, focusing on designing scalable, distributed systems.

## 📚 Core Concepts

### 1. [Fundamentals](fundamentals.md)
- Performance vs Scalability
- Latency vs Throughput
- Availability vs Consistency (CAP Theorem)
- Consistency Patterns
- Availability Patterns

### 2. [Key Components](components.md)
- DNS
- Load Balancers
- CDN
- Caching
- Databases
- Message Queues
- Stream Processing
- Data Processing

### 3. [Design Patterns](patterns.md)
- Microservices
- Event-Driven Architecture
- CQRS (Command Query Responsibility Segregation)
- Event Sourcing
- Sharding
- Rate Limiting
- Circuit Breaker
- Bulkhead

## 🏗️ Design Problems

### 1. [URL Shortener](url-shortener.md)
- Requirements
- Capacity Estimation
- System APIs
- Database Design
- Basic System Design
- Data Partitioning and Replication
- Cache
- Load Balancer
- Purging or DB Cleanup
- Telemetry
- Security and Permissions

### 2. [Twitter](twitter.md)
- Requirements
- System APIs
- High Level Design
- Database Schema
- Data Sharding
- Caching
- Timeline Generation
- Replication and Fault Tolerance
- Load Balancing

### 3. [Netflix](netflix.md)
- Requirements
- System APIs
- High Level Design
- Detailed Component Design
- Video Storage
- Caching
- Content Delivery Network (CDN)
- Fault Tolerance
- Data Partitioning

## 📈 Scaling Techniques

### 1. [Caching](caching.md)
- Client-side caching
- CDN caching
- Web server caching
- Database caching
- Application caching
- Cache Invalidation
- Cache Eviction Policies

### 2. [Load Balancing](load-balancing.md)
- Active-Passive
- Active-Active
- Layer 4 Load Balancing
- Layer 7 Load Balancing
- Horizontal Scaling
- Sticky Sessions
- Handling Failures

### 3. [Database Scaling](database-scaling.md)
- Read Replicas
- Federation
- Sharding
- Denormalization
- SQL Tuning
- NoSQL vs SQL

## 🔍 Real-world Architectures

### 1. [Designing Instagram](instagram.md)
- Requirements
- System APIs
- High Level Design
- Detailed Component Design
- Reliability and Redundancy
- Data Sharding
- Caching
- Timeline Generation
- Replication and Fault Tolerance
- Load Balancing

### 2. [Designing WhatsApp](whatsapp.md)
- Requirements
- System APIs
- High Level Design
- Detailed Component Design
- Handling Online/Offline Status
- End-to-end Encryption
- Push Notifications
- Group Chat
- Media Handling

### 3. [Designing Uber](uber.md)
- Requirements
- System APIs
- High Level Design
- Detailed Component Design
- Trip Matching
- Location Tracking
- Pricing
- Surge Pricing
- Payment Processing

## 🛠️ Tools and Technologies

### 1. [Databases](databases.md)
- Relational (PostgreSQL, MySQL)
- NoSQL (MongoDB, Cassandra)
- Wide-column (Bigtable, HBase)
- Graph (Neo4j)
- Time-series (InfluxDB)
- Search (Elasticsearch)

### 2. [Message Brokers](message-brokers.md)
- Kafka
- RabbitMQ
- ActiveMQ
- AWS SQS
- Google Pub/Sub

### 3. [Caching](caching-tools.md)
- Redis
- Memcached
- Varnish
- Nginx

## 📝 Interview Tips

### 1. [Approach](approach.md)
- Requirements Clarification
- Back-of-the-envelope Estimation
- System Interface Definition
- Defining Data Model
- High-level Design
- Detailed Design
- Identifying and Resolving Bottlenecks
- Follow-up Questions

### 2. [Common Questions](common-questions.md)
- Design a Rate Limiter
- Design Consistent Hashing
- Design a Key-value Store
- Design a Search Autocomplete System
- Design a Web Crawler
- Design Facebook's Newsfeed
- Design a Typeahead Suggestion System
- Design an API Rate Limiter
- Design a Dropbox-like File Storage Service
- Design a Video Streaming Service

## 📚 Resources

### Books
- Designing Data-Intensive Applications by Martin Kleppmann
- System Design Interview by Alex Xu
- Web Scalability for Startup Engineers by Artur Ejsmont

### Online Courses
- Grokking the System Design Interview (Educative)
- System Design Primer (GitHub)
- System Design Course (Gaurav Sen)

### Practice Platforms
- Pramp
- InterviewBit
- LeetCode
- AlgoExpert

## 🤝 Contributing

Contributions are welcome! If you'd like to add more system design problems, improve existing solutions, or add new resources, please feel free to submit a Pull Request.
