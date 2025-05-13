# Problem Solving Patterns

This document outlines common problem-solving patterns that can help you tackle a wide variety of coding interview questions. Understanding these patterns will enable you to recognize problem types and apply appropriate strategies.

## Table of Contents

1. [Two Pointers](#two-pointers)
2. [Sliding Window](#sliding-window)
3. [Fast and Slow Pointers](#fast-and-slow-pointers)
4. [Merge Intervals](#merge-intervals)
5. [Cyclic Sort](#cyclic-sort)
6. [In-place Reversal of a Linked List](#in-place-reversal-of-a-linked-list)
7. [Tree Breadth-First Search](#tree-breadth-first-search)
8. [Tree Depth-First Search](#tree-depth-first-search)
9. [Two Heaps](#two-heaps)
10. [Subsets](#subsets)
11. [Modified Binary Search](#modified-binary-search)
12. [Top K Elements](#top-k-elements)
13. [K-way Merge](#k-way-merge)
14. [Topological Sort](#topological-sort)
15. [Dynamic Programming](#dynamic-programming)

## Two Pointers

The Two Pointers pattern uses two pointers to iterate through the data structure in tandem until one or both pointers hit a certain condition.

### When to use:
- Searching for pairs in a sorted array
- Removing duplicates
- Squaring a sorted array
- Finding triplets that sum to zero

### Example Problems:
- Two Sum (sorted array)
- Remove Duplicates
- Squaring a Sorted Array
- Triplet Sum to Zero
- Dutch National Flag Problem (Sort Colors)

### Implementation Example:

```java
// Two Sum (sorted array)
public int[] twoSum(int[] numbers, int target) {
    int left = 0;
    int right = numbers.length - 1;
    
    while (left < right) {
        int sum = numbers[left] + numbers[right];
        
        if (sum == target) {
            return new int[] {left + 1, right + 1}; // 1-indexed result
        } else if (sum < target) {
            left++;
        } else {
            right--;
        }
    }
    
    return new int[] {-1, -1}; // No solution found
}
```

## Sliding Window

The Sliding Window pattern is used to perform operations on a specific window size of an array or linked list.

### When to use:
- Finding subarrays that meet certain constraints
- String problems like anagrams or substring with constraints
- Maximum/minimum subarray of a given size

### Example Problems:
- Maximum Sum Subarray of Size K
- Longest Substring with K Distinct Characters
- Fruits into Baskets
- Longest Substring Without Repeating Characters

### Implementation Example:

```java
// Maximum Sum Subarray of Size K
public int maxSubarraySum(int[] arr, int k) {
    int maxSum = 0;
    int windowSum = 0;
    int windowStart = 0;
    
    for (int windowEnd = 0; windowEnd < arr.length; windowEnd++) {
        windowSum += arr[windowEnd]; // Add the next element
        
        // Slide the window, we don't need to slide if we haven't hit the window size of k
        if (windowEnd >= k - 1) {
            maxSum = Math.max(maxSum, windowSum);
            windowSum -= arr[windowStart]; // Remove the element going out
            windowStart++; // Slide the window ahead
        }
    }
    
    return maxSum;
}
```

## Fast and Slow Pointers

This pattern uses two pointers that move at different speeds through an array or linked list.

### When to use:
- Cycle detection in a linked list
- Finding the middle of a linked list
- Palindrome linked list
- Cycle start point in a linked list

### Example Problems:
- Linked List Cycle
- Middle of the Linked List
- Palindrome Linked List
- Cycle in a Circular Array

### Implementation Example:

```java
// Detect Cycle in a Linked List
public boolean hasCycle(ListNode head) {
    if (head == null || head.next == null) {
        return false;
    }
    
    ListNode slow = head;
    ListNode fast = head;
    
    while (fast != null && fast.next != null) {
        slow = slow.next;          // Move one step
        fast = fast.next.next;     // Move two steps
        
        if (slow == fast) {        // Found a cycle
            return true;
        }
    }
    
    return false;                  // No cycle
}
```

## Merge Intervals

This pattern deals with overlapping intervals.

### When to use:
- Merging intervals
- Inserting intervals
- Meeting rooms problem
- Maximum CPU load

### Example Problems:
- Merge Intervals
- Insert Interval
- Intervals Intersection
- Conflicting Appointments

### Implementation Example:

```java
// Merge Intervals
public int[][] merge(int[][] intervals) {
    if (intervals.length <= 1) {
        return intervals;
    }
    
    // Sort by start time
    Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));
    
    List<int[]> result = new ArrayList<>();
    int[] currentInterval = intervals[0];
    result.add(currentInterval);
    
    for (int[] interval : intervals) {
        int currentEnd = currentInterval[1];
        int nextStart = interval[0];
        int nextEnd = interval[1];
        
        // If overlapping, merge
        if (currentEnd >= nextStart) {
            currentInterval[1] = Math.max(currentEnd, nextEnd);
        } else {
            // Not overlapping, add to result
            currentInterval = interval;
            result.add(currentInterval);
        }
    }
    
    return result.toArray(new int[result.size()][]);
}
```

## Dynamic Programming

Dynamic Programming is a method for solving complex problems by breaking them down into simpler subproblems.

### When to use:
- Optimization problems
- Counting problems
- Problems with overlapping subproblems

### Example Problems:
- Fibonacci Number
- Climbing Stairs
- Coin Change
- Longest Common Subsequence
- Knapsack Problem

### Implementation Example:

```java
// Fibonacci Number (Bottom-Up DP)
public int fib(int n) {
    if (n <= 1) {
        return n;
    }
    
    int[] dp = new int[n + 1];
    dp[0] = 0;
    dp[1] = 1;
    
    for (int i = 2; i <= n; i++) {
        dp[i] = dp[i - 1] + dp[i - 2];
    }
    
    return dp[n];
}
```

## Conclusion

These patterns form the foundation of many algorithmic solutions. By recognizing these patterns in problems, you can quickly identify the approach needed to solve them efficiently. Practice applying these patterns to different problems to strengthen your problem-solving skills.

Remember:
1. Identify the pattern that fits the problem
2. Apply the appropriate technique
3. Optimize your solution if needed
4. Test with different inputs