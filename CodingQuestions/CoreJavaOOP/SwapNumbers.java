/**
 * Core Java & OOP - Question 3: Swap two numbers without using a temp variable
 */
package CodingQuestions.CoreJavaOOP;

public class SwapNumbers {

    public static void main(String[] args) {
        // Test cases
        int a = 10, b = 20;
        System.out.println("Original values: a = " + a + ", b = " + b);
        
        swapUsingAdditionSubtraction(a, b);
        swapUsingMultiplicationDivision(5, 10);
        swapUsingXOR(15, 25);
        
        // Test with negative numbers
        swapUsingAdditionSubtraction(-5, 10);
        
        // Test with large numbers
        swapUsingXOR(Integer.MAX_VALUE - 10, Integer.MAX_VALUE - 5);
    }

    /**
     * Problem Statement:
     * Write a Java program to swap two numbers without using a temporary variable.
     * For example, if a = 10 and b = 20, after swapping a should be 20 and b should be 10.
     * 
     * Solution 1: Using addition and subtraction
     * 
     * Time Complexity: O(1) - constant time
     * Space Complexity: O(1) - constant space
     */
    public static void swapUsingAdditionSubtraction(int a, int b) {
        System.out.println("\nUsing Addition and Subtraction:");
        System.out.println("Before swap: a = " + a + ", b = " + b);
        
        a = a + b; // a now contains sum of both numbers
        b = a - b; // b now contains original value of a
        a = a - b; // a now contains original value of b
        
        System.out.println("After swap: a = " + a + ", b = " + b);
    }
    
    /**
     * Solution 2: Using multiplication and division
     * Note: This method has limitations - it doesn't work if one of the numbers is 0
     * and may cause overflow with large numbers.
     * 
     * Time Complexity: O(1) - constant time
     * Space Complexity: O(1) - constant space
     */
    public static void swapUsingMultiplicationDivision(int a, int b) {
        System.out.println("\nUsing Multiplication and Division:");
        System.out.println("Before swap: a = " + a + ", b = " + b);
        
        // Check if either number is 0 to avoid division by zero
        if (a == 0 || b == 0) {
            System.out.println("Cannot use multiplication/division method when one number is 0");
            return;
        }
        
        a = a * b; // a now contains product of both numbers
        b = a / b; // b now contains original value of a
        a = a / b; // a now contains original value of b
        
        System.out.println("After swap: a = " + a + ", b = " + b);
    }
    
    /**
     * Solution 3: Using XOR (bitwise exclusive OR)
     * This is the most reliable method as it works for all integers without overflow issues.
     * 
     * Time Complexity: O(1) - constant time
     * Space Complexity: O(1) - constant space
     */
    public static void swapUsingXOR(int a, int b) {
        System.out.println("\nUsing XOR (Bitwise Exclusive OR):");
        System.out.println("Before swap: a = " + a + ", b = " + b);
        
        a = a ^ b; // a now contains XOR of both numbers
        b = a ^ b; // b now contains original value of a
        a = a ^ b; // a now contains original value of b
        
        System.out.println("After swap: a = " + a + ", b = " + b);
    }
    
    /**
     * Logic Explanation:
     * 
     * 1. Addition and Subtraction Method:
     *    - First, we add both numbers and store in a: a = a + b
     *    - Then, we subtract the new value of a from b to get original a: b = a - b
     *    - Finally, we subtract the new value of b from a to get original b: a = a - b
     *    - Potential issue: May cause integer overflow with large numbers
     * 
     * 2. Multiplication and Division Method:
     *    - First, we multiply both numbers and store in a: a = a * b
     *    - Then, we divide a by b to get original a: b = a / b
     *    - Finally, we divide a by b to get original b: a = a / b
     *    - Limitations: Doesn't work if either number is 0, may cause overflow
     * 
     * 3. XOR Method:
     *    - XOR has the property that a ^ a = 0 and a ^ 0 = a
     *    - First XOR: a = a ^ b
     *    - Second XOR: b = a ^ b = (a ^ b) ^ b = a ^ (b ^ b) = a ^ 0 = a
     *    - Third XOR: a = a ^ b = (a ^ b) ^ a = (a ^ a) ^ b = 0 ^ b = b
     *    - This method works for all integers without overflow issues
     * 
     * Real-world Use Cases:
     * - Memory optimization in embedded systems
     * - Cryptography algorithms
     * - Low-level programming where memory usage is critical
     * - Competitive programming and coding interviews
     * - Implementing certain algorithms where temporary variables need to be avoided
     */
}