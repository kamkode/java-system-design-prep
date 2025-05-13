/**
 * Core Java & OOP - Question 9: Difference between == and equals()
 */
package CodingQuestions.CoreJavaOOP;

public class EqualsVsDoubleEquals {

    public static void main(String[] args) {
        // Primitive types comparison
        System.out.println("PRIMITIVE TYPES COMPARISON");
        comparePrimitives();

        // String comparison
        System.out.println("\nSTRING COMPARISON");
        compareStrings();

        // Object comparison
        System.out.println("\nOBJECT COMPARISON");
        compareObjects();

        // Wrapper classes comparison
        System.out.println("\nWRAPPER CLASSES COMPARISON");
        compareWrapperClasses();

        // Custom class comparison
        System.out.println("\nCUSTOM CLASS COMPARISON");
        compareCustomClasses();
    }

    /**
     * Problem Statement:
     * Explain and demonstrate the difference between the == operator and the equals() method in Java.
     * Show examples with primitives, strings, objects, and custom classes.
     */

    /**
     * Solution 1: Comparing primitive types
     */
    private static void comparePrimitives() {
        int num1 = 10;
        int num2 = 10;
        int num3 = 20;

        // For primitives, == compares the values
        System.out.println("num1 == num2: " + (num1 == num2)); // true
        System.out.println("num1 == num3: " + (num1 == num3)); // false

        // equals() method is not applicable for primitive types
        // We would need to use wrapper classes
    }

    /**
     * Solution 2: Comparing Strings
     */
    private static void compareStrings() {
        // String literals - stored in string pool
        String str1 = "Hello";
        String str2 = "Hello";

        // String objects - stored in heap
        String str3 = new String("Hello");
        String str4 = new String("Hello");

        // Using == operator (compares references)
        System.out.println("str1 == str2: " + (str1 == str2)); // true (same reference in string pool)
        System.out.println("str3 == str4: " + (str3 == str4)); // false (different objects in heap)
        System.out.println("str1 == str3: " + (str1 == str3)); // false (pool vs heap)

        // Using equals() method (compares content)
        System.out.println("str1.equals(str2): " + str1.equals(str2)); // true
        System.out.println("str3.equals(str4): " + str3.equals(str4)); // true
        System.out.println("str1.equals(str3): " + str1.equals(str3)); // true

        // Interning a string
        String str5 = str3.intern(); // Returns the pooled instance
        System.out.println("str1 == str5: " + (str1 == str5)); // true (both reference the same pooled string)
    }

    /**
     * Solution 3: Comparing Objects
     */
    private static void compareObjects() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        Object obj3 = obj1; // Same reference as obj1

        // Using == operator (compares references)
        System.out.println("obj1 == obj2: " + (obj1 == obj2)); // false (different objects)
        System.out.println("obj1 == obj3: " + (obj1 == obj3)); // true (same reference)

        // Using equals() method (for Object class, equals() is same as ==)
        System.out.println("obj1.equals(obj2): " + obj1.equals(obj2)); // false
        System.out.println("obj1.equals(obj3): " + obj1.equals(obj3)); // true
    }

    /**
     * Solution 4: Comparing Wrapper Classes
     */
    private static void compareWrapperClasses() {
        // Auto-boxing creates instances of wrapper classes
        Integer int1 = 127;
        Integer int2 = 127;
        Integer int3 = 128;
        Integer int4 = 128;

        // Integer caches values between -128 and 127 (inclusive)
        System.out.println("int1 == int2: " + (int1 == int2)); // true (cached values)
        System.out.println("int3 == int4: " + (int3 == int4)); // false (outside cache range)

        // Using equals() method
        System.out.println("int1.equals(int2): " + int1.equals(int2)); // true
        System.out.println("int3.equals(int4): " + int3.equals(int4)); // true

        // Explicitly creating new instances
        Integer int5 = Integer.valueOf(127);
        Integer int6 = Integer.valueOf(127);

        System.out.println("int5 == int6: " + (int5 == int6)); // false (different objects)
        System.out.println("int5.equals(int6): " + int5.equals(int6)); // true (same value)
    }

    /**
     * Solution 5: Comparing Custom Classes
     */
    private static void compareCustomClasses() {
        // Class without equals override
        WithoutEqualsOverride obj1 = new WithoutEqualsOverride("Test", 100);
        WithoutEqualsOverride obj2 = new WithoutEqualsOverride("Test", 100);
        WithoutEqualsOverride obj3 = obj1; // Same reference

        System.out.println("Without equals override:");
        System.out.println("obj1 == obj2: " + (obj1 == obj2)); // false (different objects)
        System.out.println("obj1 == obj3: " + (obj1 == obj3)); // true (same reference)
        System.out.println("obj1.equals(obj2): " + obj1.equals(obj2)); // false (uses Object.equals)
        System.out.println("obj1.equals(obj3): " + obj1.equals(obj3)); // true (same reference)

        // Class with equals override
        WithEqualsOverride obj4 = new WithEqualsOverride("Test", 100);
        WithEqualsOverride obj5 = new WithEqualsOverride("Test", 100);
        WithEqualsOverride obj6 = new WithEqualsOverride("Different", 200);

        System.out.println("\nWith equals override:");
        System.out.println("obj4 == obj5: " + (obj4 == obj5)); // false (different objects)
        System.out.println("obj4.equals(obj5): " + obj4.equals(obj5)); // true (same content)
        System.out.println("obj4.equals(obj6): " + obj4.equals(obj6)); // false (different content)
    }
}

/**
 * Class without equals override
 */
class WithoutEqualsOverride {
    private String name;
    private int value;

    public WithoutEqualsOverride(String name, int value) {
        this.name = name;
        this.value = value;
    }

    // No equals override - will use Object.equals (reference comparison)
}

/**
 * Class with equals override
 */
class WithEqualsOverride {
    private String name;
    private int value;

    public WithEqualsOverride(String name, int value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        WithEqualsOverride other = (WithEqualsOverride) obj;
        return value == other.value && 
               (name == null ? other.name == null : name.equals(other.name));
    }

    // Note: In a real application, we should also override hashCode when overriding equals
}

/**
 * Logic Explanation:
 * 
 * 1. == Operator:
 *    - For primitive types: Compares the actual values
 *    - For reference types: Compares the memory addresses (references)
 *    - Returns true if both references point to the same object in memory
 * 
 * 2. equals() Method:
 *    - Defined in the Object class
 *    - Default implementation in Object class behaves like == (compares references)
 *    - Many classes override equals() to compare the content/state of objects
 *    - String, Integer, and other wrapper classes override equals() to compare values
 * 
 * 3. String Comparison:
 *    - String literals are stored in the string pool and share the same reference if content is identical
 *    - String objects created with 'new' are stored in the heap as separate objects
 *    - String.equals() compares the content of strings, not their references
 * 
 * 4. Wrapper Classes:
 *    - Java caches certain values for wrapper classes (e.g., Integer from -128 to 127)
 *    - For cached values, == might return true even for different variables
 *    - Always use equals() for wrapper classes to compare values
 * 
 * 5. Custom Classes:
 *    - By default, equals() behaves like == (compares references)
 *    - Override equals() to compare the content/state of your objects
 *    - When overriding equals(), also override hashCode() to maintain the contract
 * 
 * Best Practices:
 * - Use == for primitive types
 * - Use equals() for objects when you want to compare content
 * - Always override both equals() and hashCode() in custom classes
 * - Be aware of string pool and wrapper class caching behaviors
 * 
 * Real-world Use Cases:
 * - Comparing user input with expected values
 * - Checking if an object exists in a collection
 * - Implementing business logic that depends on object equality
 * - Database record comparison
 * - Caching mechanisms
 */
