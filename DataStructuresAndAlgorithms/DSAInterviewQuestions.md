# Data Structures and Algorithms Interview Guide

This comprehensive guide covers essential data structures and algorithms commonly tested in technical interviews, with a focus on Java implementations and practical applications.

## Table of Contents

### 1. [Core Data Structures](core-data-structures.md)
- Arrays and Strings
- Linked Lists
- Stacks and Queues
- Hash Tables
- Trees and Binary Trees
- Heaps
- Graphs

### 2. [Algorithms](algorithms.md)
- Sorting Algorithms
- Searching Algorithms
- Graph Algorithms
- Dynamic Programming
- Greedy Algorithms
- Divide and Conquer
- Backtracking

### 3. [Problem Solving Patterns](problem-solving-patterns.md)
- Two Pointers
- Sliding Window
- Fast & Slow Pointers
- Merge Intervals
- Cyclic Sort
- In-place Reversal of Linked List
- BFS & DFS
- Topological Sort

### 4. [System Design Basics](system-design-basics.md)
- Scalability
- Load Balancing
- Caching
- Database Design
- API Design
- Message Queues
- Microservices

## Core Data Structures

### 1. Arrays vs. Linked Lists

**Arrays:**
- Contiguous memory allocation
- Fixed size (in most languages)
- O(1) random access
- O(n) insertion/deletion (worst case)
- Cache-friendly
- Less memory overhead

**Linked Lists:**
- Non-contiguous memory allocation
- Dynamic size
- O(n) access (sequential)
- O(1) insertion/deletion at head
- Extra memory for pointers/references
- Better for frequent insertions/deletions

```java
// Array declaration and initialization
int[] numbers = new int[5];
int[] numbers = {1, 2, 3, 4, 5};

// Linked List in Java
LinkedList<Integer> list = new LinkedList<>();
list.add(1);
list.add(2);
list.add(3);
```

### 2. Stacks and Queues

**Stack (LIFO):**
- Operations: push, pop, peek
- Implementation: Array or Linked List
- Time Complexity: O(1) for all operations
- Use cases: Function calls, Undo/Redo, Back/Forward

**Queue (FIFO):**
- Operations: enqueue, dequeue, peek
- Implementation: Array or Linked List
- Time Complexity: O(1) for all operations
- Use cases: Task scheduling, Print spooling, BFS

```java
// Stack implementation
Stack<Integer> stack = new Stack<>();
stack.push(1);
int top = stack.pop();

// Queue implementation
Queue<Integer> queue = new LinkedList<>();
queue.offer(1);
int front = queue.poll();
```

## Time and Space Complexity Cheat Sheet

| Data Structure | Access | Search | Insertion | Deletion | Space |
|----------------|--------|--------|-----------|----------|-------|
| Array          | O(1)   | O(n)   | O(n)      | O(n)     | O(n)  |
| Stack          | O(n)   | O(n)   | O(1)      | O(1)     | O(n)  |
| Queue          | O(n)   | O(n)   | O(1)      | O(1)     | O(n)  |
| Linked List    | O(n)   | O(n)   | O(1)      | O(1)     | O(n)  |
| Hash Table     | N/A    | O(1)   | O(1)      | O(1)     | O(n)  |
| BST            | O(log n)| O(log n)| O(log n) | O(log n) | O(n)  |
| AVL Tree       | O(log n)| O(log n)| O(log n) | O(log n) | O(n)  |

### Q2: What is the time complexity of common operations in different data structures?

Arrays:
- Access: O(1)
- Search: O(n) for unsorted, O(log n) for sorted (binary search)
- Insertion: O(n) (worst case)
- Deletion: O(n) (worst case)

LinkedList:
- Access: O(n)
- Search: O(n)
- Insertion: O(1) (if position is known)
- Deletion: O(1) (if position is known)

HashMap/HashTable:
- Access: N/A
- Search: O(1) average, O(n) worst case
- Insertion: O(1) average, O(n) worst case
- Deletion: O(1) average, O(n) worst case

Binary Search Tree:
- Access: N/A
- Search: O(log n) average, O(n) worst case
- Insertion: O(log n) average, O(n) worst case
- Deletion: O(log n) average, O(n) worst case

Heap:
- Access: N/A
- Search: O(n)
- Insertion: O(log n)
- Deletion (extract min/max): O(log n)

### Q3: What is a Stack and a Queue? How are they different?

Stack:
- LIFO (Last In First Out) data structure
- Operations: push (add to top), pop (remove from top), peek (view top)
- Applications: function call management, expression evaluation, backtracking algorithms
- Implementation: can be implemented using arrays or linked lists

Queue:
- FIFO (First In First Out) data structure
- Operations: enqueue (add to rear), dequeue (remove from front), peek (view front)
- Applications: CPU scheduling, disk scheduling, IO buffers, breadth-first search
- Implementation: can be implemented using arrays or linked lists

Key Difference:
- Stack: Elements are removed in the reverse order of their addition
- Queue: Elements are removed in the same order of their addition

### Q4: What is a Binary Tree and a Binary Search Tree?

Binary Tree:
- A tree data structure where each node has at most two children (left and right)
- No specific ordering of nodes is required

Binary Search Tree (BST):
- A binary tree with the following properties:
  - The left subtree of a node contains only nodes with keys less than the node's key
  - The right subtree of a node contains only nodes with keys greater than the node's key
  - Both the left and right subtrees are also binary search trees
- Provides efficient insertion, deletion, and lookup operations (O(log n) on average)
- Inorder traversal of BST gives sorted output
- Worst-case time complexity can degrade to O(n) if the tree becomes skewed

### Q5: What is a Hash Table and how does it work?

A Hash Table is a data structure that implements an associative array abstract data type, a structure that can map keys to values.

How it works:
1. Hash Function: Converts the key into an index in the array
2. Hash Code: The result of the hash function
3. Collision Resolution: Handling when two keys hash to the same index
   - Chaining: Each array position contains a linked list of entries
   - Open Addressing: Finding another position in the array (linear probing, quadratic probing, double hashing)

Time Complexity:
- Average case: O(1) for search, insert, delete
- Worst case: O(n) if many collisions occur

Load Factor: The ratio of the number of stored entries to the size of the hash table
- Higher load factor means more collisions
- Typically, hash tables resize when the load factor exceeds a threshold

### Q6: What is a Graph and what are the different ways to represent it?

A Graph is a non-linear data structure consisting of vertices (nodes) and edges that connect these vertices.

Types of Graphs:
- Directed vs Undirected
- Weighted vs Unweighted
- Cyclic vs Acyclic
- Connected vs Disconnected

Representations:
1. Adjacency Matrix:
   - 2D array where matrix[i][j] = 1 if there's an edge from vertex i to j
   - Space complexity: O(V²)
   - Time complexity: O(1) for checking edge, O(V) for finding all adjacent vertices

2. Adjacency List:
   - Array of lists where each list contains the adjacent vertices of the corresponding vertex
   - Space complexity: O(V+E)
   - Time complexity: O(degree(v)) for finding all adjacent vertices

3. Edge List:
   - List of all edges in the graph
   - Space complexity: O(E)
   - Time complexity: O(E) for checking edge

### Q7: What are common graph traversal algorithms?

1. Breadth-First Search (BFS):
   - Uses a queue to explore all neighbors at the current depth before moving to nodes at the next depth
   - Time Complexity: O(V+E)
   - Space Complexity: O(V)
   - Applications: Shortest path in unweighted graphs, connected components, network broadcasting

2. Depth-First Search (DFS):
   - Uses a stack (or recursion) to explore as far as possible along each branch before backtracking
   - Time Complexity: O(V+E)
   - Space Complexity: O(V)
   - Applications: Topological sorting, cycle detection, path finding, strongly connected components

3. Dijkstra's Algorithm:
   - Finds the shortest path from a source vertex to all other vertices in a weighted graph
   - Time Complexity: O(V²) with array, O((V+E)log V) with binary heap
   - Cannot handle negative weights

4. Bellman-Ford Algorithm:
   - Finds the shortest path from a source vertex to all other vertices
   - Can handle negative weights (but not negative cycles)
   - Time Complexity: O(V*E)

### Q8: What are common sorting algorithms and their time complexities?

1. Bubble Sort:
   - Repeatedly steps through the list, compares adjacent elements and swaps them if they are in wrong order
   - Time Complexity: O(n²) average and worst case, O(n) best case
   - Space Complexity: O(1)
   - Stable: Yes

2. Selection Sort:
   - Repeatedly finds the minimum element from the unsorted part and puts it at the beginning
   - Time Complexity: O(n²) for all cases
   - Space Complexity: O(1)
   - Stable: No

3. Insertion Sort:
   - Builds the sorted array one item at a time by inserting each new item in its proper position
   - Time Complexity: O(n²) average and worst case, O(n) best case
   - Space Complexity: O(1)
   - Stable: Yes

4. Merge Sort:
   - Divide and conquer algorithm that divides the input array into two halves, recursively sorts them, and merges
   - Time Complexity: O(n log n) for all cases
   - Space Complexity: O(n)
   - Stable: Yes

5. Quick Sort:
   - Divide and conquer algorithm that picks a pivot and partitions the array around it
   - Time Complexity: O(n log n) average, O(n²) worst case
   - Space Complexity: O(log n)
   - Stable: No

6. Heap Sort:
   - Uses a binary heap data structure to sort elements
   - Time Complexity: O(n log n) for all cases
   - Space Complexity: O(1)
   - Stable: No

### Q9: What is Dynamic Programming and how is it different from recursion?

Dynamic Programming (DP) is a method for solving complex problems by breaking them down into simpler subproblems. It is applicable when:
1. The problem has overlapping subproblems (same subproblems are solved multiple times)
2. The problem has optimal substructure (optimal solution can be constructed from optimal solutions of its subproblems)

Approaches:
- Top-down (Memoization): Recursive approach with caching of results
- Bottom-up (Tabulation): Iterative approach building solutions from smallest subproblems

Difference from Recursion:
- Recursion solves problems by breaking them into smaller instances of the same problem
- DP uses recursion but adds memoization or tabulation to avoid redundant calculations
- Recursion without DP can lead to exponential time complexity for problems with overlapping subproblems

Common DP Problems:
- Fibonacci sequence
- Knapsack problem
- Longest Common Subsequence
- Matrix Chain Multiplication
- Shortest Path algorithms

### Q10: What is Big O notation and why is it important?

Big O notation is a mathematical notation that describes the limiting behavior of a function when the argument tends towards a particular value or infinity. In computer science, it's used to classify algorithms according to how their run time or space requirements grow as the input size grows.

Importance:
- Provides a standardized way to compare algorithm efficiency
- Helps predict performance for large inputs
- Guides algorithm selection and optimization
- Helps identify bottlenecks in code

Common Big O complexities (from fastest to slowest):
- O(1): Constant time (e.g., array access by index)
- O(log n): Logarithmic time (e.g., binary search)
- O(n): Linear time (e.g., linear search)
- O(n log n): Linearithmic time (e.g., efficient sorting algorithms like merge sort)
- O(n²): Quadratic time (e.g., bubble sort)
- O(2^n): Exponential time (e.g., recursive calculation of Fibonacci numbers)
- O(n!): Factorial time (e.g., brute force solution to traveling salesman problem)

When analyzing algorithms, we focus on the worst-case scenario and the dominant term.

### Real-world Applications of DSA:

1. Arrays and Strings:
   - Text editors, spreadsheets, image processing

2. Linked Lists:
   - Implementation of other data structures, music playlists, browser history

3. Stacks:
   - Expression evaluation, syntax parsing, browser back button, undo functionality

4. Queues:
   - CPU scheduling, print spooling, handling of requests in web servers

5. Trees:
   - File systems, organization hierarchies, decision trees, database indexing

6. Graphs:
   - Social networks, maps/navigation, network routing, recommendation systems

7. Hash Tables:
   - Database indexing, caching, compiler symbol tables, spell checkers

8. Heaps:
   - Priority queues, scheduling algorithms, heap sort

9. Sorting and Searching:
   - Database operations, information retrieval, data analysis

10. Dynamic Programming:
    - Resource allocation, optimization problems, sequence alignment in bioinformatics