# Java DSA Interview Repository Reorganization Plan

## Current Issues
1. Inconsistent naming conventions (camelCase, snake_case, kebab-case, PascalCase)
2. Redundant nested directory (JAVA DSA INTERVIEW inside JAVA DSA INTERVIEW)
3. Overlapping content (CodingQuestions vs DataStructuresAndAlgorithms)
4. Some directories have README.md files while others don't
5. Some topics have their own directories while others are just files in the root
6. The "out" directory contains compiled files that shouldn't be in version control

## Proposed New Structure

```
java-interview-prep/
├── README.md
├── core-java/
│   ├── README.md
│   ├── basics.md
│   ├── oop-concepts.md
│   ├── exception-handling.md
│   ├── collections.md
│   ├── multithreading.md
│   ├── io.md
│   └── advanced-multithreading.md
├── advanced-java/
│   ├── README.md
│   ├── reflection.md
│   ├── annotations.md
│   ├── generics.md
│   ├── lambda-streams.md
│   └── concurrency.md
├── data-structures/
│   ├── README.md
│   ├── arrays/
│   │   ├── README.md
│   │   ├── two-sum.java
│   │   └── maximum-subarray.java
│   ├── linked-lists/
│   │   ├── README.md
│   │   ├── reverse-linked-list.java
│   │   └── detect-cycle.java
│   ├── strings/
│   │   ├── README.md
│   │   └── longest-substring-without-repeating-chars.java
│   ├── trees-graphs/
│   │   └── README.md
│   ├── stacks-queues/
│   │   └── README.md
│   └── hash-tables/
│       └── README.md
├── algorithms/
│   ├── README.md
│   ├── sorting/
│   │   └── README.md
│   ├── searching/
│   │   └── README.md
│   ├── dynamic-programming/
│   │   └── README.md
│   ├── greedy/
│   │   └── README.md
│   └── problem-solving-patterns.md
├── coding-practice/
│   ├── README.md
│   ├── core-java-examples/
│   │   ├── encapsulation-access-modifiers.java
│   │   ├── equals-hashcode.java
│   │   ├── immutable-class.java
│   │   ├── interface-vs-abstract-class.java
│   │   ├── java-cloning.java
│   │   ├── overloading-vs-overriding.java
│   │   ├── singleton-class.java
│   │   └── this-and-super.java
│   ├── string-manipulation/
│   │   ├── count-characters.java
│   │   ├── palindrome-check.java
│   │   ├── remove-duplicate-chars.java
│   │   └── reverse-string.java
│   └── practical-problems.md
├── frameworks/
│   ├── README.md
│   ├── spring/
│   │   ├── README.md
│   │   ├── theory.md
│   │   ├── coding-questions.md
│   │   ├── best-practices.md
│   │   ├── security.md
│   │   ├── testing.md
│   │   ├── performance.md
│   │   ├── debugging.md
│   │   ├── scenario-questions.md
│   │   └── misc.md
│   ├── microservices/
│   │   ├── README.md
│   │   └── interview-questions.md
│   └── message-brokers/
│       ├── README.md
│       └── rabbitmq-interview-questions.md
├── databases/
│   ├── README.md
│   └── mysql.md
├── system-design/
│   ├── README.md
│   └── interview-questions.md
├── mock-interviews/
│   ├── README.md
│   ├── behavioral/
│   │   └── README.md
│   ├── technical/
│   │   └── README.md
│   └── company-specific/
│       └── README.md
└── resources/
    ├── README.md
    ├── interview-questions-index.md
    └── tricky-interview-questions.md
```

## Detailed Changes

### 1. Root Directory
- Rename from "JAVA DSA INTERVIEW" to "java-interview-prep"
- Remove redundant nested directory
- Move loose files to appropriate subdirectories
- Create README.md files for all directories

### 2. Naming Conventions
- Use kebab-case for all directory and file names (e.g., "core-java" instead of "CoreJava")
- Use lowercase for all file and directory names
- Use descriptive, consistent names
- Add README.md files to all directories

### 3. Content Organization
- Merge "CodingQuestions" and "DataStructuresAndAlgorithms" into separate "data-structures", "algorithms", and "coding-practice" directories
- Create a "frameworks" directory for Spring, Microservices, and RabbitMQ
- Create a "databases" directory for MySQL and other database content
- Move loose files in the root to appropriate subdirectories

### 4. File Cleanup
- Remove the "out" directory with compiled files
- Remove any duplicate or redundant files

## Implementation Steps
1. Create the new directory structure
2. Move files to their new locations
3. Rename files and directories according to the new naming convention
4. Create README.md files for directories that don't have them
5. Update internal links in markdown files to reflect the new structure
6. Remove redundant or unnecessary files