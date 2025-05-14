package BlitzenxIntervierwQnA;

import java.util.Scanner;

/*
 * PROBLEM STATEMENT:
 * Write a Java program to reverse a given integer number without converting it to a String.
 * For example, if the input is 45367, the output should be 76354.
 * 
 * Requirements:
 * - Do NOT use String conversion (e.g., Integer.toString() or String.valueOf()).
 * - Use mathematical operations only (division, modulo, multiplication).
 * - Use Scanner to take input from the user.
 * - Handle edge cases:
 *   - Negative numbers (preserve the sign, reverse only the digits).
 *   - Single digit numbers (should return the same number).
 *   - Numbers ending with zeros (e.g., 1200 should reverse to 21, not 0021).
 *   - Potential overflow for large numbers.
 * - Output the reversed number.
 * - Include all details (problem statement, explanation, solution) in a single .java file.
 *
 * EXPLANATION:
 * Class: NumberReverser
 * Purpose: Reverses an integer number using mathematical operations without String conversion.
 *
 * CONCEPTS INVOLVED:
 * 1. Mathematical Operations:
 *    - Modulo (%) to extract the last digit of a number.
 *    - Division (/) to remove the last digit from a number.
 *    - Multiplication and Addition to build the reversed number.
 * 2. Scanner: Reads user input (number to reverse) from console.
 * 3. Integer Manipulation:
 *    - Working with digits of a number directly.
 *    - Building a new number digit by digit.
 * 4. Edge Cases:
 *    - Negative numbers: Preserve the sign, reverse only the absolute value.
 *    - Zero handling: Avoid leading zeros in the reversed number.
 *    - Overflow: Handle potential overflow for large reversed numbers.
 * 5. Time and Space Complexity:
 *    - Time Complexity: O(log n) where n is the input number (number of digits).
 *    - Space Complexity: O(1) as we use a constant amount of variables.
 * 6. OOP: Encapsulate logic in a class with public methods.
 * 7. Error Handling: Handle potential exceptions and invalid inputs.
 *
 * ALGORITHM:
 * 1. Store the sign of the number (positive or negative).
 * 2. Take the absolute value of the input number.
 * 3. Initialize the reversed number to 0.
 * 4. While the input number is greater than 0:
 *    a. Extract the last digit using modulo (number % 10).
 *    b. Add the digit to the reversed number (reversed = reversed * 10 + digit).
 *    c. Remove the last digit from the input number (number = number / 10).
 * 5. Apply the original sign to the reversed number.
 * 6. Check for potential overflow and handle appropriately.
 * 7. Return the reversed number.
 *
 * WHY THIS APPROACH:
 * - Using mathematical operations is more efficient than String conversion for this task.
 * - Division and modulo are the natural operators for extracting digits from right to left.
 * - Building the result as we go avoids the need for temporary storage structures.
 * - This approach automatically handles trailing zeros (e.g., 1200 becomes 21).
 * - It works for all integers within the range of the primitive data type.
 *
 * IMPLEMENTATION DETAILS:
 * - Main method uses Scanner to read an integer input.
 * - Includes input validation to handle non-integer inputs.
 * - Implements both a standard method and an alternative method:
 *   - Standard method: Works with int, potential overflow for large numbers.
 *   - Alternative method: Uses long to handle larger numbers safely.
 * - Prints the original number and the reversed result.
 * - Includes thorough error handling for all edge cases.
 */

class NumberOverflowException extends Exception {
    public NumberOverflowException(String message) {
        super(message);
    }
}

public class NumberReverser {
    
    /**
     * Reverses an integer number using mathematical operations.
     * 
     * @param number The number to reverse
     * @return The reversed number
     * @throws NumberOverflowException If the reversed number would overflow int
     */
    public int reverseNumber(int number) throws NumberOverflowException {
        // Handle the sign separately
        boolean isNegative = number < 0;
        
        // Work with absolute value
        int absNumber = Math.abs(number);
        
        // Handle single digit case
        if (absNumber < 10) {
            return isNegative ? -absNumber : absNumber;
        }
        
        int reversed = 0;
        
        while (absNumber > 0) {
            // Extract the last digit
            int lastDigit = absNumber % 10;
            
            // Check for potential overflow before multiplication
            if (reversed > Integer.MAX_VALUE / 10) {
                throw new NumberOverflowException("Reversed number would overflow integer limits.");
            }
            
            // Build the reversed number
            reversed = reversed * 10 + lastDigit;
            
            // Remove the last digit
            absNumber /= 10;
        }
        
        // Apply the original sign
        return isNegative ? -reversed : reversed;
    }
    
    /**
     * Alternative method using long to handle larger numbers safely.
     * 
     * @param number The number to reverse
     * @return The reversed number, or Integer.MAX_VALUE/MIN_VALUE if overflow occurs
     */
    public int reverseNumberSafe(int number) {
        // Handle the sign separately
        boolean isNegative = number < 0;
        
        // Work with absolute value as a long to avoid overflow during calculation
        long absNumber = Math.abs((long) number);
        long reversed = 0;
        
        while (absNumber > 0) {
            // Extract the last digit
            long lastDigit = absNumber % 10;
            
            // Build the reversed number
            reversed = reversed * 10 + lastDigit;
            
            // Check for overflow
            if (reversed > Integer.MAX_VALUE) {
                return isNegative ? Integer.MIN_VALUE : Integer.MAX_VALUE;
            }
            
            // Remove the last digit
            absNumber /= 10;
        }
        
        // Apply the original sign and convert back to int
        return isNegative ? -(int)reversed : (int)reversed;
    }
    
    /**
     * Formats the output message for a reversed number.
     * 
     * @param original The original number
     * @param reversed The reversed number
     * @return A formatted output message
     */
    private String formatOutput(int original, int reversed) {
        return "Original number: " + original + "\n" +
               "Reversed number: " + reversed;
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.print("Enter a number to reverse: ");
            if (!scanner.hasNextInt()) {
                System.out.println("Error: Please enter a valid integer.");
                return;
            }
            
            int number = scanner.nextInt();
            NumberReverser reverser = new NumberReverser();
            
            System.out.println("\nChoose method for reversing number:");
            System.out.println("1. Standard method (may throw exception for large numbers)");
            System.out.println("2. Safe method (handles overflow by capping at MAX/MIN_VALUE)");
            int choice = 1; // Default to standard method
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
            }
            
            int reversed;
            try {
                if (choice == 2) {
                    reversed = reverser.reverseNumberSafe(number);
                    System.out.println(reverser.formatOutput(number, reversed));
                    if (reversed == Integer.MAX_VALUE || reversed == Integer.MIN_VALUE) {
                        System.out.println("Warning: Result was capped due to integer overflow.");
                    }
                } else {
                    reversed = reverser.reverseNumber(number);
                    System.out.println(reverser.formatOutput(number, reversed));
                }
                
                // Show mathematical proof
                System.out.println("\nStep-by-step reversal process for " + number + ":");
                reverser.demonstrateReversal(number);
                
            } catch (NumberOverflowException e) {
                System.out.println("Error: " + e.getMessage());
                System.out.println("Try using the safe method (option 2) for large numbers.");
            }
            
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
    
    /**
     * Demonstrates the step-by-step process of reversing a number.
     * This method is purely educational to show how the algorithm works.
     * 
     * @param number The number to reverse
     */
    private void demonstrateReversal(int number) {
        int absNumber = Math.abs(number);
        int reversed = 0;
        int step = 1;
        
        System.out.println("Start with reversed = 0");
        
        while (absNumber > 0) {
            int lastDigit = absNumber % 10;
            int oldReversed = reversed;
            reversed = reversed * 10 + lastDigit;
            
            System.out.println("Step " + step + ": Extract last digit: " + lastDigit);
            System.out.println("        reversed = " + oldReversed + " * 10 + " + lastDigit + " = " + reversed);
            
            absNumber /= 10;
            System.out.println("        Remove last digit from original, remaining: " + absNumber);
            step++;
        }
        
        // Apply sign if needed
        if (number < 0) {
            System.out.println("Apply negative sign: -" + reversed + " = " + (-reversed));
            reversed = -reversed;
        }
        
        System.out.println("Final result: " + reversed);
    }
}
