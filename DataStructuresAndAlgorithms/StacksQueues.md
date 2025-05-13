# Stacks and Queues

Stacks and Queues are fundamental linear data structures that follow specific rules for adding and removing elements. This document covers key concepts, operations, and common interview questions related to stacks and queues.

## Table of Contents
1. [Stacks](#stacks)
   - [Introduction](#introduction-to-stacks)
   - [Operations](#stack-operations)
   - [Implementation](#stack-implementation)
   - [Applications](#stack-applications)
   - [Common Interview Questions](#stack-interview-questions)
2. [Queues](#queues)
   - [Introduction](#introduction-to-queues)
   - [Operations](#queue-operations)
   - [Implementation](#queue-implementation)
   - [Applications](#queue-applications)
   - [Common Interview Questions](#queue-interview-questions)
3. [Variations](#variations)
   - [Priority Queue](#priority-queue)
   - [Deque (Double-ended Queue)](#deque-double-ended-queue)
   - [Circular Queue](#circular-queue)
4. [Time and Space Complexity](#time-and-space-complexity)
5. [Tips and Tricks](#tips-and-tricks)

## Stacks

### Introduction to Stacks

A stack is a linear data structure that follows the Last-In-First-Out (LIFO) principle. This means that the last element added to the stack will be the first one to be removed.

Think of a stack as a pile of plates - you can only add or remove plates from the top of the pile.

### Stack Operations

1. **Push**: Add an element to the top of the stack
2. **Pop**: Remove the top element from the stack
3. **Peek/Top**: View the top element without removing it
4. **isEmpty**: Check if the stack is empty
5. **Size**: Get the number of elements in the stack

### Stack Implementation

Stacks can be implemented using arrays or linked lists:

#### Array Implementation

```java
class ArrayStack {
    private int maxSize;
    private int[] stackArray;
    private int top;
    
    public ArrayStack(int size) {
        maxSize = size;
        stackArray = new int[maxSize];
        top = -1; // Stack is initially empty
    }
    
    public void push(int value) {
        if (top < maxSize - 1) {
            stackArray[++top] = value;
        } else {
            System.out.println("Stack Overflow");
        }
    }
    
    public int pop() {
        if (top >= 0) {
            return stackArray[top--];
        } else {
            System.out.println("Stack Underflow");
            return -1;
        }
    }
    
    public int peek() {
        if (top >= 0) {
            return stackArray[top];
        } else {
            System.out.println("Stack is Empty");
            return -1;
        }
    }
    
    public boolean isEmpty() {
        return (top == -1);
    }
    
    public boolean isFull() {
        return (top == maxSize - 1);
    }
    
    public int size() {
        return top + 1;
    }
}
```

#### Linked List Implementation

```java
class LinkedListStack {
    private class Node {
        int data;
        Node next;
        
        Node(int data) {
            this.data = data;
            this.next = null;
        }
    }
    
    private Node top;
    private int size;
    
    public LinkedListStack() {
        top = null;
        size = 0;
    }
    
    public void push(int value) {
        Node newNode = new Node(value);
        newNode.next = top;
        top = newNode;
        size++;
    }
    
    public int pop() {
        if (isEmpty()) {
            System.out.println("Stack Underflow");
            return -1;
        }
        
        int value = top.data;
        top = top.next;
        size--;
        return value;
    }
    
    public int peek() {
        if (isEmpty()) {
            System.out.println("Stack is Empty");
            return -1;
        }
        
        return top.data;
    }
    
    public boolean isEmpty() {
        return top == null;
    }
    
    public int size() {
        return size;
    }
}
```

### Stack Applications

1. **Function Call Management**: Call stack for managing function calls and returns
2. **Expression Evaluation**: Evaluating arithmetic expressions (infix, postfix, prefix)
3. **Syntax Parsing**: Checking for balanced parentheses and syntax validation
4. **Undo Mechanism**: Implementing undo functionality in applications
5. **Backtracking Algorithms**: DFS traversal, maze solving, etc.
6. **Browser History**: Managing back button functionality

### Stack Interview Questions

1. **Valid Parentheses**: Determine if a string of parentheses is valid
2. **Evaluate Reverse Polish Notation**: Evaluate expressions in postfix notation
3. **Min Stack**: Design a stack that supports push, pop, top, and retrieving the minimum element
4. **Implement Queue using Stacks**: Implement a queue using only stack operations
5. **Next Greater Element**: Find the next greater element for each element in an array
6. **Infix to Postfix Conversion**: Convert infix expressions to postfix
7. **Sort a Stack**: Sort a stack using only stack operations
8. **Simplify Path**: Simplify a Unix-style file path
9. **Decode String**: Decode a string encoded with a specific pattern
10. **Asteroid Collision**: Determine the state of asteroids after collisions

## Queues

### Introduction to Queues

A queue is a linear data structure that follows the First-In-First-Out (FIFO) principle. This means that the first element added to the queue will be the first one to be removed.

Think of a queue as a line of people waiting - the first person to join the line is the first one to be served.

### Queue Operations

1. **Enqueue**: Add an element to the back of the queue
2. **Dequeue**: Remove the front element from the queue
3. **Front**: View the front element without removing it
4. **Rear**: View the last element without removing it
5. **isEmpty**: Check if the queue is empty
6. **Size**: Get the number of elements in the queue

### Queue Implementation

Queues can be implemented using arrays or linked lists:

#### Array Implementation

```java
class ArrayQueue {
    private int maxSize;
    private int[] queueArray;
    private int front;
    private int rear;
    private int currentSize;
    
    public ArrayQueue(int size) {
        maxSize = size;
        queueArray = new int[maxSize];
        front = 0;
        rear = -1;
        currentSize = 0;
    }
    
    public void enqueue(int value) {
        if (isFull()) {
            System.out.println("Queue Overflow");
            return;
        }
        
        // Circular queue implementation
        rear = (rear + 1) % maxSize;
        queueArray[rear] = value;
        currentSize++;
    }
    
    public int dequeue() {
        if (isEmpty()) {
            System.out.println("Queue Underflow");
            return -1;
        }
        
        int value = queueArray[front];
        front = (front + 1) % maxSize;
        currentSize--;
        return value;
    }
    
    public int front() {
        if (isEmpty()) {
            System.out.println("Queue is Empty");
            return -1;
        }
        
        return queueArray[front];
    }
    
    public int rear() {
        if (isEmpty()) {
            System.out.println("Queue is Empty");
            return -1;
        }
        
        return queueArray[rear];
    }
    
    public boolean isEmpty() {
        return (currentSize == 0);
    }
    
    public boolean isFull() {
        return (currentSize == maxSize);
    }
    
    public int size() {
        return currentSize;
    }
}
```

#### Linked List Implementation

```java
class LinkedListQueue {
    private class Node {
        int data;
        Node next;
        
        Node(int data) {
            this.data = data;
            this.next = null;
        }
    }
    
    private Node front;
    private Node rear;
    private int size;
    
    public LinkedListQueue() {
        front = null;
        rear = null;
        size = 0;
    }
    
    public void enqueue(int value) {
        Node newNode = new Node(value);
        
        if (isEmpty()) {
            front = newNode;
        } else {
            rear.next = newNode;
        }
        
        rear = newNode;
        size++;
    }
    
    public int dequeue() {
        if (isEmpty()) {
            System.out.println("Queue Underflow");
            return -1;
        }
        
        int value = front.data;
        front = front.next;
        
        // If queue becomes empty
        if (front == null) {
            rear = null;
        }
        
        size--;
        return value;
    }
    
    public int front() {
        if (isEmpty()) {
            System.out.println("Queue is Empty");
            return -1;
        }
        
        return front.data;
    }
    
    public int rear() {
        if (isEmpty()) {
            System.out.println("Queue is Empty");
            return -1;
        }
        
        return rear.data;
    }
    
    public boolean isEmpty() {
        return (front == null);
    }
    
    public int size() {
        return size;
    }
}
```

### Queue Applications

1. **CPU Scheduling**: Managing processes in operating systems
2. **Breadth-First Search**: Traversing graphs level by level
3. **Buffering**: Managing data buffers in I/O operations
4. **Print Queue**: Managing print jobs in a printer
5. **Message Queues**: Handling asynchronous communication between services
6. **Task Scheduling**: Managing tasks in a multithreaded environment

### Queue Interview Questions

1. **Implement Stack using Queues**: Implement a stack using only queue operations
2. **Design Circular Queue**: Implement a circular queue with fixed size
3. **Sliding Window Maximum**: Find the maximum element in each sliding window
4. **First Unique Character**: Find the first non-repeating character in a string
5. **Task Scheduler**: Schedule tasks with cooldown periods
6. **Number of Recent Calls**: Count recent API calls within a time frame
7. **Design Hit Counter**: Design a hit counter that counts requests in the last 5 minutes
8. **Rotting Oranges**: Determine the minimum time to rot all oranges
9. **Walls and Gates**: Find distances to the nearest gate
10. **Design Snake Game**: Implement the classic snake game

## Variations

### Priority Queue

A priority queue is a special type of queue where elements are dequeued based on their priority rather than their arrival order. Elements with higher priority are served before elements with lower priority.

#### Implementation using Heap

```java
import java.util.PriorityQueue;

class PriorityQueueExample {
    public static void main(String[] args) {
        // Min Heap (default)
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        
        // Max Heap
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>((a, b) -> b - a);
        
        // Adding elements
        minHeap.add(5);
        minHeap.add(2);
        minHeap.add(8);
        
        // Removing elements (in order of priority)
        while (!minHeap.isEmpty()) {
            System.out.println(minHeap.poll()); // 2, 5, 8
        }
    }
}
```

### Deque (Double-ended Queue)

A deque (double-ended queue) allows insertion and removal of elements from both ends.

```java
import java.util.ArrayDeque;
import java.util.Deque;

class DequeExample {
    public static void main(String[] args) {
        Deque<Integer> deque = new ArrayDeque<>();
        
        // Adding elements
        deque.addFirst(1);  // [1]
        deque.addLast(2);   // [1, 2]
        deque.addFirst(0);  // [0, 1, 2]
        deque.addLast(3);   // [0, 1, 2, 3]
        
        // Removing elements
        System.out.println(deque.removeFirst()); // 0
        System.out.println(deque.removeLast());  // 3
    }
}
```

### Circular Queue

A circular queue is a linear data structure that follows FIFO principle and wraps around when it reaches the end of the underlying array, making efficient use of space.

## Time and Space Complexity

| Operation | Stack | Queue | Priority Queue |
|-----------|-------|-------|---------------|
| Push/Enqueue | O(1) | O(1) | O(log n) |
| Pop/Dequeue | O(1) | O(1) | O(log n) |
| Peek/Front | O(1) | O(1) | O(1) |
| Search | O(n) | O(n) | O(n) |
| Space Complexity | O(n) | O(n) | O(n) |

## Tips and Tricks

1. **Use Java Collections Framework**: Java provides built-in implementations for stacks and queues:
   ```java
   // Stack
   Stack<Integer> stack = new Stack<>();
   
   // Queue
   Queue<Integer> queue = new LinkedList<>();
   
   // Priority Queue
   PriorityQueue<Integer> pq = new PriorityQueue<>();
   
   // Deque
   Deque<Integer> deque = new ArrayDeque<>();
   ```

2. **Two-Stack Technique**: Use two stacks to implement complex operations:
   - Implement a queue using two stacks
   - Evaluate expressions
   - Sort a stack

3. **Monotonic Stack/Queue**: A stack/queue that maintains elements in increasing or decreasing order:
   ```java
   // Monotonic increasing stack
   Stack<Integer> stack = new Stack<>();
   for (int num : nums) {
       while (!stack.isEmpty() && stack.peek() > num) {
           stack.pop();
       }
       stack.push(num);
   }
   ```

4. **BFS with Queue**: Use a queue for breadth-first search:
   ```java
   Queue<Node> queue = new LinkedList<>();
   queue.add(startNode);
   while (!queue.isEmpty()) {
       Node current = queue.poll();
       // Process current node
       for (Node neighbor : current.neighbors) {
           queue.add(neighbor);
       }
   }
   ```

5. **DFS with Stack**: Use a stack for depth-first search:
   ```java
   Stack<Node> stack = new Stack<>();
   stack.push(startNode);
   while (!stack.isEmpty()) {
       Node current = stack.pop();
       // Process current node
       for (Node neighbor : current.neighbors) {
           stack.push(neighbor);
       }
   }
   ```

## Practice Resources

- [LeetCode Stack](https://leetcode.com/tag/stack/)
- [LeetCode Queue](https://leetcode.com/tag/queue/)
- [HackerRank Stacks](https://www.hackerrank.com/domains/data-structures?filters%5Bsubdomains%5D%5B%5D=stacks)
- [HackerRank Queues](https://www.hackerrank.com/domains/data-structures?filters%5Bsubdomains%5D%5B%5D=queues)