# Tricky Interview Questions and Answers

This document contains solutions to tricky Java interview questions that often catch candidates off-guard.

## Core Java Tricky Questions (Output-Based)

### 1. What will be the output of the following code?
```java
public class TrickyQuestion1 {
    public static void main(String[] args) {
        Integer a = 127;
        Integer b = 127;
        
        System.out.println(a == b);
        
        Integer c = 128;
        Integer d = 128;
        
        System.out.println(c == d);
    }
}
```

**Answer:**
```
true
false
```

**Explanation:**
- Java caches Integer objects in the range -128 to 127 (Integer.IntegerCache)
- When you create Integer objects within this range using autoboxing, Java reuses the same objects
- For 127, both 'a' and 'b' reference the same cached Integer object, so a == b is true
- For 128, which is outside the cache range, Java creates new objects for 'c' and 'd', so c == d is false
- This is a classic trick question showing that == compares object references, not values

**Follow-up:** How would you correctly compare the values of two Integer objects regardless of their value range?
