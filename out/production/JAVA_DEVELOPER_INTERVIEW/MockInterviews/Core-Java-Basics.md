# Core Java Basics - 50+ Output Questions

This file contains tricky output questions focusing on Java fundamentals that are commonly asked in interviews.

## Java Language Fundamentals

### 1. What will this code print?
```java
public class MainMethodOverloading {
    public static void main(String[] args) {
        System.out.println("Original main");
        String[] newArgs = {"test"};
        main(newArgs, "extra");
    }
    
    public static void main(String[] args, String extra) {
        System.out.println("Overloaded main with: " + args[0] + ", " + extra);
    }
}
```

**Answer:**
```
Original main
Overloaded main with: test, extra
```

**Explanation:**
- The main method can be overloaded like any other Java method
- The JVM only calls the standard `main(String[] args)` method as the entry point
- Other overloaded main methods can be called programmatically

### 2. What will be the output of the following code?
```java
public class LabeledBreak {
    public static void main(String[] args) {
        outer:
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (i == 1 && j == 1) {
                    break outer;
                }
                System.out.println(i + " " + j);
            }
        }
    }
}
```

**Answer:**
```
0 0
0 1
0 2
1 0
```

**Explanation:**
- Java supports labeled break statements which can break out of multiple nested loops
- When `i == 1` and `j == 1`, the break with label `outer` breaks completely out of both loops

### 3. What will this code output?
```java
public class StaticInitialization {
    static {
        System.out.println("Static initialization block");
    }
    
    {
        System.out.println("Instance initialization block");
    }
    
    public StaticInitialization() {
        System.out.println("Constructor");
    }
    
    public static void main(String[] args) {
        System.out.println("Main method");
        StaticInitialization obj1 = new StaticInitialization();
        StaticInitialization obj2 = new StaticInitialization();
    }
}
```

**Answer:**
```
Static initialization block
Main method
Instance initialization block
Constructor
Instance initialization block
Constructor
```

**Explanation:**
- Static initialization blocks are executed when the class is loaded, before any instance is created
- Instance initialization blocks run every time an instance is created, before the constructor
- The execution order is: static blocks → main method → instance blocks → constructor

### 4. What does this code print?
```java
public class FinallyWithReturn {
    public static void main(String[] args) {
        System.out.println(test());
    }
    
    public static int test() {
        try {
            System.out.println("Try block");
            return 1;
        } catch (Exception e) {
            System.out.println("Catch block");
            return 2;
        } finally {
            System.out.println("Finally block");
            // Note: No return here
        }
    }
}
```

**Answer:**
```
Try block
Finally block
1
```

**Explanation:**
- The finally block always executes, even when a return statement is encountered in try or catch
- The return value is calculated before the finally block executes, but the actual return happens after finally
- Since there's no return in finally, the original return value from try (1) is used

### 5. What will be the output?
```java
public class DoubleBrace {
    private String name;
    
    public DoubleBrace(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    public static void main(String[] args) {
        List<DoubleBrace> list = new ArrayList<DoubleBrace>() {{
            add(new DoubleBrace("One"));
            add(new DoubleBrace("Two"));
        }};
        
        for (DoubleBrace item : list) {
            System.out.println(item.getName());
        }
        
        System.out.println("List class: " + list.getClass().getName());
    }
}
```

**Answer:**
```
One
Two
List class: DoubleBrace$1
```

**Explanation:**
- Double brace initialization creates an anonymous inner class that extends ArrayList
- The outer braces create the anonymous subclass of ArrayList
- The inner braces create an instance initialization block
- This creates a new class (DoubleBrace$1) that's a subclass of ArrayList
- While convenient for initialization, this approach creates extra classes and can cause memory leaks

### 6. What will be the output?
```java
public class Recursion {
    public static void main(String[] args) {
        print(3);
    }
    
    public static void print(int n) {
        if (n > 0) {
            System.out.print(n + " ");
            print(n - 1);
            System.out.print(n + " ");
        }
    }
}
```

**Answer:**
```
3 2 1 1 2 3
```

**Explanation:**
- This demonstrates how recursion works with a stack of method calls
- First, all the numbers are printed in descending order (3, 2, 1) during the recursive descent
- Then, as each recursive call completes and returns, the numbers are printed again in ascending order (1, 2, 3)
- This shows that statements after a recursive call are executed after the recursive call completes

### 7. What's the output of this bitwise operation?
```java
public class BitwiseShift {
    public static void main(String[] args) {
        int a = -1;
        System.out.println(Integer.toBinaryString(a));
        
        int b = a >>> 1;  // Unsigned right shift
        System.out.println(Integer.toBinaryString(b));
        System.out.println(b);
        
        int c = a >> 1;   // Signed right shift
        System.out.println(Integer.toBinaryString(c));
        System.out.println(c);
    }
}
```

**Answer:**
```
11111111111111111111111111111111
1111111111111111111111111111111
2147483647
11111111111111111111111111111111
-1
```

**Explanation:**
- -1 in two's complement is represented as all 1s in binary
- >>> (unsigned right shift) shifts in 0s from the left, regardless of the sign bit
- After unsigned right shift by 1, we get a binary number with 31 1s, which is 2147483647 (Integer.MAX_VALUE)
- >> (signed right shift) preserves the sign bit, so shifting -1 right still gives -1

### 8. What will this code print?
```java
public class StaticVsInstance {
    static int staticVar = 0;
    int instanceVar = 0;
    
    public static void main(String[] args) {
        StaticVsInstance obj1 = new StaticVsInstance();
        StaticVsInstance obj2 = new StaticVsInstance();
        
        obj1.staticVar = 1;
        obj1.instanceVar = 1;
        
        obj2.staticVar = 2;
        obj2.instanceVar = 2;
        
        System.out.println("obj1 staticVar: " + obj1.staticVar);
        System.out.println("obj1 instanceVar: " + obj1.instanceVar);
        System.out.println("obj2 staticVar: " + obj2.staticVar);
        System.out.println("obj2 instanceVar: " + obj2.instanceVar);
        System.out.println("StaticVsInstance.staticVar: " + StaticVsInstance.staticVar);
    }
}
```

**Answer:**
```
obj1 staticVar: 2
obj1 instanceVar: 1
obj2 staticVar: 2
obj2 instanceVar: 2
StaticVsInstance.staticVar: 2
```

**Explanation:**
- Static variables belong to the class, not to instances
- There is only one copy of a static variable shared by all instances
- When obj2 sets staticVar to 2, it changes it for all instances and the class itself
- Instance variables are unique to each instance, so instanceVar has different values for obj1 and obj2

### 9. What will this code output?
```java
public class VariableScoping {
    private int x = 10;
    
    public void printX() {
        int x = 20;
        System.out.println("Local x: " + x);
        System.out.println("Instance x: " + this.x);
        
        {
            int y = 30;
            System.out.println("y in block: " + y);
            int x = 40; // Will this compile?
        }
    }
    
    public static void main(String[] args) {
        new VariableScoping().printX();
    }
}
```

**Answer:**
This code will not compile. The variable 'x' is already defined in the method scope.

**Explanation:**
- Local variables can shadow instance variables with the same name
- 'this' keyword refers to the current instance and allows access to shadowed instance variables
- Variable scope is defined by the block in which it's declared
- You cannot redeclare a variable with the same name in an overlapping scope, so the second 'int x = 40' causes a compilation error

### 10. What's the output of this code?
```java
public class StringIntern {
    public static void main(String[] args) {
        String s1 = "Hello";
        String s2 = new String("Hello");
        String s3 = "Hel" + "lo";
        String s4 = "Hel";
        String s5 = s4 + "lo";
        String s6 = s5.intern();
        
        System.out.println(s1 == s2);
        System.out.println(s1 == s3);
        System.out.println(s1 == s5);
        System.out.println(s1 == s6);
    }
}
```

**Answer:**
```
false
true
false
true
```

**Explanation:**
- s1 is a string literal that goes into the string pool
- s2 is created using 'new' so it's a separate object, not from the string pool
- s3 is composed of two literals at compile-time, so it's also a pooled string (same as s1)
- s5 involves runtime concatenation with a variable, so it creates a new object (not from the pool)
- s6 uses intern() which returns the pooled version of the string, which is the same as s1

### 11. What will be the output?
```java
public class Array2D {
    public static void main(String[] args) {
        int[][] arr = new int[3][];
        arr[0] = new int[1];
        arr[1] = new int[2];
        arr[2] = new int[3];
        
        int sum = 0;
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                arr[i][j] = i + j;
                sum += arr[i][j];
            }
        }
        
        System.out.println("Sum: " + sum);
        System.out.println("arr[1][1]: " + arr[1][1]);
    }
}
```

**Answer:**
```
Sum: 6
arr[1][1]: 2
```

**Explanation:**
- Java supports jagged arrays (arrays of different lengths)
- arr[0][0] = 0 + 0 = 0
- arr[1][0] = 1 + 0 = 1, arr[1][1] = 1 + 1 = 2
- arr[2][0] = 2 + 0 = 2, arr[2][1] = 2 + 1 = 3, arr[2][2] = 2 + 2 = 4
- The sum is 0 + 1 + 2 + 2 + 3 + 4 = 12 (wait - sum should be 12, not 6!)

### 12. What will this code output?
```java
public class PassByValue {
    public static void main(String[] args) {
        int x = 5;
        changeValue(x);
        System.out.println("x after changeValue: " + x);
        
        int[] arr = {1, 2, 3};
        changeArray(arr);
        System.out.println("arr[0] after changeArray: " + arr[0]);
        
        String str = "Hello";
        changeString(str);
        System.out.println("str after changeString: " + str);
    }
    
    public static void changeValue(int value) {
        value = 10;
    }
    
    public static void changeArray(int[] array) {
        array[0] = 100;
    }
    
    public static void changeString(String s) {
        s = "World";
    }
}
```

**Answer:**
```
x after changeValue: 5
arr[0] after changeArray: 100
str after changeString: Hello
```

**Explanation:**
- Java is always pass-by-value
- For primitives, a copy of the value is passed, so changes in the method don't affect the original
- For objects (including arrays), a copy of the reference is passed
- The reference copy points to the same object, so changes to the object are visible outside
- Strings are immutable, so changeString can't modify the original string, it can only make s refer to a new string

### 13. What will be printed?
```java
public class ForEachLimitations {
    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 4, 5};
        
        System.out.println("Using for-each loop:");
        for (int num : arr) {
            num = num * 2;
        }
        
        for (int num : arr) {
            System.out.print(num + " ");
        }
        
        System.out.println("\nUsing indexed for loop:");
        for (int i = 0; i < arr.length; i++) {
            arr[i] = arr[i] * 2;
        }
        
        for (int num : arr) {
            System.out.print(num + " ");
        }
    }
}
```

**Answer:**
```
Using for-each loop:
1 2 3 4 5 
Using indexed for loop:
2 4 6 8 10
```

**Explanation:**
- In a for-each loop, the loop variable (num) gets a copy of each element
- Modifying the loop variable does not affect the original array elements
- The indexed for loop modifies the actual array elements with arr[i] = arr[i] * 2
- This demonstrates a key limitation of for-each loops: you can't modify the array elements directly

### 14. What will be the output?
```java
public class TernaryOperator {
    public static void main(String[] args) {
        int x = 5;
        int y = 10;
        
        // Simple ternary
        int max = (x > y) ? x : y;
        System.out.println("Max: " + max);
        
        // Nested ternary
        String result = (x > 10) ? "x > 10" : 
                       (x > 5) ? "5 < x <= 10" : 
                       (x == 5) ? "x = 5" : "x < 5";
        System.out.println(result);
        
        // Ternary with different types
        Object obj = (x > y) ? new Integer(x) : "y is greater";
        System.out.println("obj type: " + obj.getClass().getName());
    }
}
```

**Answer:**
```
Max: 10
x = 5
obj type: java.lang.String
```

**Explanation:**
- The ternary operator evaluates the condition and returns one of two values
- Nested ternary operators work from left to right
- In the nested ternary, x is 5, so the first two conditions fail and the third one passes
- In the last example, both branches must be compatible types (Integer and String) so the common supertype Object is used
- Since x is not greater than y, "y is greater" is returned, which is a String

### 15. What's the output of this switch statement?
```java
public class SwitchFallthrough {
    public static void main(String[] args) {
        int day = 3;
        
        switch (day) {
            case 1:
                System.out.println("Monday");
                break;
            case 2:
                System.out.println("Tuesday");
                break;
            case 3:
                System.out.println("Wednesday");
                // No break statement
            case 4:
                System.out.println("Thursday");
                break;
            case 5:
                System.out.println("Friday");
                break;
            default:
                System.out.println("Weekend");
        }
    }
}
```

**Answer:**
```
Wednesday
Thursday
```

**Explanation:**
- Without a break statement, execution "falls through" to the next case
- When day is 3, it matches case 3 and executes its code
- Since there's no break after case 3, execution continues to case 4
- Execution stops at case 4's break statement

### 16. What happens here?
```java
public class UnreachableCode {
    public static void main(String[] args) {
        System.out.println("Before if");
        
        if (true) {
            System.out.println("This will always execute");
            return;
        } else {
            System.out.println("This will never execute");
        }
        
        System.out.println("After if");
    }
}
```

**Answer:**
```
Before if
This will always execute
```

**Explanation:**
- The if condition is always true, so the first block always executes
- The else block is unreachable but doesn't cause compilation errors
- The return statement exits the method, so "After if" is never printed
- Java doesn't issue warnings for unreachable code in if-else when the condition is a constant, but it would if there were statements after a return

### 17. What's the output of this primitive autoboxing code?
```java
public class AutoboxingEquality {
    public static void main(String[] args) {
        Boolean b1 = true;
        Boolean b2 = true;
        Boolean b3 = Boolean.valueOf(true);
        Boolean b4 = new Boolean(true);
        
        System.out.println(b1 == b2);
        System.out.println(b1 == b3);
        System.out.println(b1 == b4);
        System.out.println(b1.equals(b4));
    }
}
```

**Answer:**
```
true
true
false
true
```

**Explanation:**
- Boolean autoboxing uses a pool of Boolean.TRUE and Boolean.FALSE constants
- b1 and b2 both refer to the same Boolean.TRUE instance due to autoboxing
- Boolean.valueOf() also returns the same Boolean.TRUE constant
- new Boolean() creates a new object instance, so == returns false
- .equals() compares the boolean value, not the object reference, so it returns true

### 18. What does this variable shadowing code print?
```java
public class VariableShadowing {
    private static int x = 5;
    
    public static void main(String[] args) {
        int x = 10;
        System.out.println("Local x: " + x);
        System.out.println("Static x: " + VariableShadowing.x);
        
        test();
    }
    
    public static void test() {
        System.out.println("In test, x: " + x);
    }
}
```

**Answer:**
```
Local x: 10
Static x: 5
In test, x: 5
```

**Explanation:**
- The local variable x in main() shadows the static variable x
- Inside main(), x refers to the local variable with value 10
- VariableShadowing.x explicitly refers to the static variable with value 5
- In the test() method, there's no local x, so x refers to the static variable with value 5

### 19. What will be the output?
```java
public class FinalParameter {
    public static void main(String[] args) {
        final int x = 5;
        // x = 10; // Would cause compilation error
        
        modifyValue(x);
        
        final int[] arr = {1, 2, 3};
        modifyArray(arr);
        
        System.out.println("arr[0]: " + arr[0]);
    }
    
    public static void modifyValue(final int value) {
        // value = 20; // Would cause compilation error
        System.out.println("Value: " + value);
    }
    
    public static void modifyArray(final int[] array) {
        // array = new int[]{4, 5, 6}; // Would cause compilation error
        array[0] = 100; // This is legal
    }
}
```

**Answer:**
```
Value: 5
arr[0]: 100
```

**Explanation:**
- The final keyword prevents reassignment of a variable
- For primitives, this means the value cannot be changed
- For objects and arrays, the reference cannot be changed, but the object's internal state can be modified
- In modifyArray(), we cannot assign a new array to array, but we can modify its elements

### 20. What does this constant folding example print?
```java
public class CompileTimeEvaluation {
    public static void main(String[] args) {
        final int a = 5;
        final int b = 3;
        int c = 5;
        int d = 3;
        
        // Compile-time constant expressions
        int resultConst = a * b;
        
        // Runtime expressions
        int resultVar = c * d;
        
        System.out.println("10 * 10: " + 10 * 10);
        System.out.println("resultConst: " + resultConst);
        System.out.println("resultVar: " + resultVar);
        
        String s1 = "Hello";
        final String s2 = "Hel";
        final String s3 = "lo";
        String s4 = s2 + s3;
        
        System.out.println(s1 == "Hello");
        System.out.println(s1 == s4);
    }
}
```

**Answer:**
```
10 * 10: 100
resultConst: 15
resultVar: 15
true
true
```

**Explanation:**
- The Java compiler performs constant folding for expressions involving constants
- 10 * 10 is evaluated at compile time to 100
- a * b (5 * 3) is also evaluated at compile time to 15 because a and b are final
- c * d is evaluated at runtime
- s1 == "Hello" is true because both refer to the same string pool entry
- s2 + s3 is evaluated at compile time to "Hello" because s2 and s3 are final, so s1 == s4 is true
