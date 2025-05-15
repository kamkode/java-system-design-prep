# Arrays and Strings

Arrays and strings are fundamental data structures that form the basis of many interview questions. This document covers key concepts, common operations, and frequently asked interview questions related to arrays and strings.

## Table of Contents
1. [Arrays](#arrays)
   - [Key Concepts](#key-concepts)
   - [Common Operations](#common-operations)
   - [Time Complexity](#time-complexity)
   - [Common Interview Questions](#common-interview-questions)
   - [Tips and Tricks](#tips-and-tricks)
2. [Strings](#strings)
   - [Key Concepts](#key-concepts-1)
   - [Common Operations](#common-operations-1)
   - [Time Complexity](#time-complexity-1)
   - [Common Interview Questions](#common-interview-questions-1)
   - [Tips and Tricks](#tips-and-tricks-1)

## Arrays

### Key Concepts
- An array is a collection of elements stored at contiguous memory locations
- Elements can be accessed using indices (0-based indexing in Java)
- Arrays have a fixed size once initialized
- Java arrays are objects and inherit methods from the Object class
- Multi-dimensional arrays are arrays of arrays

### Common Operations
- **Accessing elements**: `arr[i]` - O(1)
- **Searching**: Linear search - O(n), Binary search (if sorted) - O(log n)
- **Insertion**: At the end (if space available) - O(1), At a specific position - O(n)
- **Deletion**: From a specific position - O(n)
- **Traversal**: O(n)

### Time Complexity
| Operation | Time Complexity |
|-----------|----------------|
| Access    | O(1)           |
| Search    | O(n)           |
| Search (sorted) | O(log n) |
| Insertion | O(n)           |
| Deletion  | O(n)           |

### Common Interview Questions
1. **Two Sum**: Find two numbers in an array that add up to a target value
2. **Three Sum**: Find all unique triplets in the array that give the sum of zero
3. **Maximum Subarray**: Find the contiguous subarray with the largest sum
4. **Container With Most Water**: Find two lines that together with the x-axis form a container that holds the most water
5. **Rotate Array**: Rotate an array to the right by k steps
6. **Remove Duplicates from Sorted Array**: Remove duplicates in-place
7. **Merge Sorted Arrays**: Merge two sorted arrays into one sorted array
8. **Product of Array Except Self**: Calculate the product of all elements except the current one
9. **Next Permutation**: Rearrange numbers to the lexicographically next greater permutation
10. **Trapping Rain Water**: Calculate how much water can be trapped after raining

### Tips and Tricks
- Use two pointers technique for many array problems
- Consider sorting the array if the order doesn't matter
- Use hash maps to reduce time complexity for lookup operations
- For sliding window problems, maintain a window and update it as you traverse
- Use binary search on sorted arrays to reduce time complexity
- Consider edge cases: empty array, single element, duplicate elements

## Strings

### Key Concepts
- Strings are sequences of characters
- In Java, strings are immutable objects
- String operations often involve character manipulation and substring operations
- String comparison is case-sensitive by default
- String pool and string interning concepts are important for memory optimization

### Common Operations
- **Character Access**: `str.charAt(i)` - O(1)
- **Substring**: `str.substring(i, j)` - O(n)
- **Concatenation**: `str1 + str2` or `str1.concat(str2)` - O(n)
- **Comparison**: `str1.equals(str2)` - O(n)
- **Length**: `str.length()` - O(1)
- **Search**: `str.indexOf(substr)` - O(n*m)

### Time Complexity
| Operation | Time Complexity |
|-----------|----------------|
| Access    | O(1)           |
| Search    | O(n)           |
| Substring | O(n)           |
| Concatenation | O(n)       |
| Comparison | O(n)          |

### Common Interview Questions
1. **Valid Anagram**: Determine if two strings are anagrams
2. **Longest Substring Without Repeating Characters**: Find the length of the longest substring without repeating characters
3. **Longest Palindromic Substring**: Find the longest palindromic substring
4. **String to Integer (atoi)**: Convert a string to an integer
5. **Valid Palindrome**: Check if a string is a palindrome
6. **Implement strStr()**: Return the index of the first occurrence of a substring
7. **Group Anagrams**: Group strings that are anagrams of each other
8. **Longest Common Prefix**: Find the longest common prefix string amongst an array of strings
9. **Valid Parentheses**: Determine if a string with parentheses is valid
10. **Edit Distance**: Find the minimum number of operations to convert one string to another

### Tips and Tricks
- Use StringBuilder for mutable string operations to improve performance
- Consider using character arrays for in-place string manipulation
- Hash maps are useful for character frequency counting
- Two pointers technique works well for many string problems
- For pattern matching, consider KMP or Rabin-Karp algorithms
- Be careful with string immutability in Java - operations create new strings
- Use regular expressions for complex pattern matching

## Common Patterns for Array and String Problems

1. **Two Pointers**: Useful for problems involving searching, reversing, or finding pairs
2. **Sliding Window**: Effective for substring or subarray problems with constraints
3. **Hash Map/Set**: Helps with frequency counting, finding duplicates, or tracking elements
4. **Divide and Conquer**: Useful for problems that can be broken down into smaller subproblems
5. **Dynamic Programming**: For optimization problems with overlapping subproblems

## Practice Resources

- [LeetCode Arrays 101](https://leetcode.com/explore/learn/card/fun-with-arrays/)
- [LeetCode Top Interview Questions - Easy Collection](https://leetcode.com/explore/interview/card/top-interview-questions-easy/)
- [HackerRank Arrays and Strings](https://www.hackerrank.com/domains/data-structures?filters%5Bsubdomains%5D%5B%5D=arrays)