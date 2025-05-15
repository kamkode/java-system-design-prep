# Linked Lists

Linked Lists are fundamental data structures that consist of nodes where each node contains data and a reference to the next node in the sequence. This document covers key concepts, operations, and common interview questions related to linked lists.

## Table of Contents
1. [Introduction to Linked Lists](#introduction-to-linked-lists)
2. [Types of Linked Lists](#types-of-linked-lists)
3. [Basic Operations](#basic-operations)
4. [Time and Space Complexity](#time-and-space-complexity)
5. [Common Interview Questions](#common-interview-questions)
6. [Implementation in Java](#implementation-in-java)
7. [Tips and Tricks](#tips-and-tricks)

## Introduction to Linked Lists

A linked list is a linear data structure where elements are not stored in contiguous memory locations. Instead, each element (node) contains a pointer to the next node in the sequence.

### Advantages of Linked Lists
- Dynamic size (can grow or shrink during execution)
- Ease of insertion/deletion (no need to shift elements)
- Efficient memory utilization (allocates memory as needed)

### Disadvantages of Linked Lists
- Random access is not allowed (must traverse from beginning)
- Extra memory for pointers
- Not cache-friendly due to non-contiguous memory allocation

## Types of Linked Lists

### Singly Linked List
- Each node contains data and a pointer to the next node
- Last node points to null
- Traversal is only possible in one direction

### Doubly Linked List
- Each node contains data, a pointer to the next node, and a pointer to the previous node
- First node's previous pointer points to null
- Last node's next pointer points to null
- Traversal is possible in both directions

### Circular Linked List
- Similar to singly linked list, but the last node points back to the first node
- No null references
- Useful for applications that require circular behavior

## Basic Operations

### Traversal
- Start from the head node
- Access each node sequentially until reaching the end (null)
- Time Complexity: O(n)

### Insertion
1. **At the beginning (head)**
   - Create a new node
   - Set its next pointer to the current head
   - Update head to point to the new node
   - Time Complexity: O(1)

2. **At the end (tail)**
   - Create a new node
   - Traverse to the last node
   - Update the last node's next pointer to the new node
   - Time Complexity: O(n) for singly linked list, O(1) if tail pointer is maintained

3. **At a specific position**
   - Traverse to the node just before the desired position
   - Insert the new node between this node and its next node
   - Time Complexity: O(n)

### Deletion
1. **From the beginning (head)**
   - Update head to point to the second node
   - Time Complexity: O(1)

2. **From the end (tail)**
   - Traverse to the second-last node
   - Update its next pointer to null
   - Time Complexity: O(n) for singly linked list, O(1) for doubly linked list

3. **From a specific position**
   - Traverse to the node just before the node to be deleted
   - Update its next pointer to skip the node to be deleted
   - Time Complexity: O(n)

### Search
- Start from the head node
- Compare each node's data with the target value
- Return the node or its position if found, otherwise indicate not found
- Time Complexity: O(n)

## Time and Space Complexity

| Operation | Singly Linked List | Doubly Linked List |
|-----------|-------------------|-------------------|
| Access    | O(n)              | O(n)              |
| Search    | O(n)              | O(n)              |
| Insertion at beginning | O(1) | O(1)              |
| Insertion at end | O(n) or O(1)* | O(1)           |
| Insertion at position | O(n) | O(n)               |
| Deletion at beginning | O(1) | O(1)               |
| Deletion at end | O(n) or O(1)* | O(1)            |
| Deletion at position | O(n) | O(n)                |

*O(1) if tail pointer is maintained

**Space Complexity**: O(n) for all linked list types

## Common Interview Questions

1. **Reverse a Linked List**: Reverse the direction of a linked list
2. **Detect Cycle**: Determine if a linked list has a cycle
3. **Find Middle Node**: Find the middle node of a linked list
4. **Remove Nth Node From End**: Remove the nth node from the end of the list
5. **Merge Two Sorted Lists**: Merge two sorted linked lists into one sorted list
6. **Palindrome Linked List**: Check if a linked list is a palindrome
7. **Intersection of Two Linked Lists**: Find the node where two linked lists intersect
8. **Remove Duplicates**: Remove duplicate nodes from a linked list
9. **Partition List**: Partition a linked list around a value x
10. **Add Two Numbers**: Add two numbers represented by linked lists

## Implementation in Java

### Basic Node Class
```java
class ListNode {
    int val;
    ListNode next;
    
    ListNode() {}
    
    ListNode(int val) {
        this.val = val;
    }
    
    ListNode(int val, ListNode next) {
        this.val = val;
        this.next = next;
    }
}
```

### Singly Linked List Implementation
```java
class SinglyLinkedList {
    ListNode head;
    
    // Add a node at the front
    public void addAtHead(int val) {
        ListNode newNode = new ListNode(val);
        newNode.next = head;
        head = newNode;
    }
    
    // Add a node at the end
    public void addAtTail(int val) {
        ListNode newNode = new ListNode(val);
        
        // If the list is empty, make the new node the head
        if (head == null) {
            head = newNode;
            return;
        }
        
        // Traverse to the last node
        ListNode current = head;
        while (current.next != null) {
            current = current.next;
        }
        
        // Add the new node at the end
        current.next = newNode;
    }
    
    // Delete a node with given value
    public void deleteNode(int val) {
        // If the list is empty
        if (head == null) return;
        
        // If head node itself holds the value to be deleted
        if (head.val == val) {
            head = head.next;
            return;
        }
        
        // Search for the node to be deleted
        ListNode current = head;
        ListNode prev = null;
        
        while (current != null && current.val != val) {
            prev = current;
            current = current.next;
        }
        
        // If value not found
        if (current == null) return;
        
        // Unlink the node from the list
        prev.next = current.next;
    }
    
    // Print the linked list
    public void printList() {
        ListNode current = head;
        while (current != null) {
            System.out.print(current.val + " -> ");
            current = current.next;
        }
        System.out.println("NULL");
    }
}
```

## Tips and Tricks

1. **Use Fast and Slow Pointers**: Useful for finding the middle node, detecting cycles, and finding the nth node from the end.
   ```java
   // Finding the middle node
   ListNode slow = head, fast = head;
   while (fast != null && fast.next != null) {
       slow = slow.next;
       fast = fast.next.next;
   }
   // slow is now at the middle node
   ```

2. **Dummy Node Technique**: Use a dummy node to simplify edge cases, especially when the head might change.
   ```java
   ListNode dummy = new ListNode(0);
   dummy.next = head;
   // Perform operations
   return dummy.next; // Return the new head
   ```

3. **Two-Pointer Technique**: Useful for many linked list problems.
   ```java
   // Finding the nth node from the end
   ListNode first = head, second = head;
   // Move first pointer n steps ahead
   for (int i = 0; i < n; i++) {
       first = first.next;
   }
   // Move both pointers until first reaches the end
   while (first != null) {
       first = first.next;
       second = second.next;
   }
   // second is now at the nth node from the end
   ```

4. **Recursion**: Many linked list problems can be solved elegantly using recursion.
   ```java
   // Reverse a linked list using recursion
   public ListNode reverseList(ListNode head) {
       if (head == null || head.next == null) return head;
       ListNode newHead = reverseList(head.next);
       head.next.next = head;
       head.next = null;
       return newHead;
   }
   ```

5. **Edge Cases**: Always consider these edge cases:
   - Empty list (head == null)
   - Single node list (head.next == null)
   - Operations on the head or tail
   - Cycles in the list

## Practice Resources

- [LeetCode Linked List](https://leetcode.com/explore/learn/card/linked-list/)
- [HackerRank Linked Lists](https://www.hackerrank.com/domains/data-structures?filters%5Bsubdomains%5D%5B%5D=linked-lists)
- [GeeksforGeeks Linked List Problems](https://www.geeksforgeeks.org/data-structures/linked-list/)