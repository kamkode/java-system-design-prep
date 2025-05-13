/**
 * Core Java & OOP - Question 1: Reverse a String without using library methods
 */
package CodingQuestions.CoreJavaOOP;

public class ReverseString {

    public static void main(String[] args) {
        String input = "Hello, World!";
        
        System.out.println("Original string: " + input);
        System.out.println("Reversed string (using iteration): " + reverseStringIterative(input));
        System.out.println("Reversed string (using recursion): " + reverseStringRecursive(input));
        System.out.println("Reversed string (using char array): " + reverseStringUsingCharArray(input));
    }

    /**
     * Problem Statement:
     * Write a Java program to reverse a string without using any built-in string reverse methods.
     * For example, if the input is "Hello", the output should be "olleH".
     * 
     * Solution 1: Iterative approach using StringBuilder
     * 
     * Time Complexity: O(n) where n is the length of the string
     * Space Complexity: O(n) for the new StringBuilder
     */
    public static String reverseStringIterative(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        
        StringBuilder reversed = new StringBuilder();
        for (int i = input.length() - 1; i >= 0; i--) {
            reversed.append(input.charAt(i));
        }
        
        return reversed.toString();
    }
    
    /**
     * Solution 2: Recursive approach
     * 
     * Time Complexity: O(n) where n is the length of the string
     * Space Complexity: O(n) for the call stack
     */
    public static String reverseStringRecursive(String input) {
        if (input == null || input.isEmpty() || input.length() == 1) {
            return input;
        }
        
        return input.charAt(input.length() - 1) + 
               reverseStringRecursive(input.substring(0, input.length() - 1));
    }
    
    /**
     * Solution 3: Using character array
     * 
     * Time Complexity: O(n) where n is the length of the string
     * Space Complexity: O(n) for the character array
     */
    public static String reverseStringUsingCharArray(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        
        char[] charArray = input.toCharArray();
        int left = 0;
        int right = charArray.length - 1;
        
        while (left < right) {
            // Swap characters
            char temp = charArray[left];
            charArray[left] = charArray[right];
            charArray[right] = temp;
            
            // Move towards the middle
            left++;
            right--;
        }
        
        return new String(charArray);
    }
    
    /**
     * Logic Explanation:
     * 1. Iterative Approach: We create a new StringBuilder and append characters from the original string
     *    in reverse order (from the end to the beginning).
     * 
     * 2. Recursive Approach: We use recursion to break down the problem. We take the last character of the string
     *    and concatenate it with the reversed version of the rest of the string.
     * 
     * 3. Character Array Approach: We convert the string to a character array, then swap characters from
     *    the beginning and end, moving towards the middle until we've processed the entire string.
     * 
     * Real-world Use Case:
     * String reversal is commonly used in:
     * - Palindrome checking
     * - Text processing applications
     * - Cryptography algorithms
     * - DNA sequence analysis (reverse complement)
     */
}