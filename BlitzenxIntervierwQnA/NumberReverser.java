package BlitzenxIntervierwQnA;

import java.util.Scanner;

/**
 * Question 5: Reverse this number 45367 without using String
 * 
 * This program reverses an integer number using mathematical operations
 * without converting it to a String.
 */
public class NumberReverser {
    
    /**
     * Reverses an integer number using mathematical operations.
     * 
     * @param number The number to reverse
     * @return The reversed number
     */
    public static int reverseNumber(int number) {
        // Handle the sign separately
        boolean isNegative = number < 0;
        
        // Work with absolute value
        int absNumber = Math.abs(number);
        int reversed = 0;
        
        while (absNumber > 0) {
            // Extract the last digit
            int lastDigit = absNumber % 10;
            
            // Build the reversed number
            reversed = reversed * 10 + lastDigit;
            
            // Remove the last digit
            absNumber /= 10;
        }
        
        // Apply the original sign
        return isNegative ? -reversed : reversed;
    }
    
    /**
     * Demonstrates the step-by-step process of reversing a number.
     */
    private static void demonstrateReversal(int number) {
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
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Number Reverser - Reverse a number without using String conversion");
        System.out.println("----------------------------------------------------------------");
        
        try {
            // Default example from the question
            int defaultNumber = 45367;
            System.out.println("Default example: " + defaultNumber);
            int defaultReversed = reverseNumber(defaultNumber);
            System.out.println("Reversed: " + defaultReversed);
            
            // Get input from user
            System.out.print("\nEnter your own number to reverse (or press Enter to skip): ");
            String input = scanner.nextLine().trim();
            
            if (!input.isEmpty()) {
                int number = Integer.parseInt(input);
                
                // Reverse the number
                int reversed = reverseNumber(number);
                
                // Display result
                System.out.println("\nOriginal number: " + number);
                System.out.println("Reversed number: " + reversed);
                
                // Show step-by-step process
                System.out.println("\nStep-by-step reversal process:");
                demonstrateReversal(number);
            }
            
            // Explain the algorithm
            System.out.println("\nAlgorithm explanation:");
            System.out.println("1. Handle the sign separately (preserve negative sign)");
            System.out.println("2. Use modulo (%) to extract the last digit: number % 10");
            System.out.println("3. Build the reversed number: reversed = reversed * 10 + lastDigit");
            System.out.println("4. Remove the last digit: number = number / 10");
            System.out.println("5. Repeat until all digits are processed");
            System.out.println("6. Apply the original sign to the result");
            System.out.println("\nTime Complexity: O(log n) where n is the number (number of digits)");
            System.out.println("Space Complexity: O(1) - constant extra space");
            
        } catch (NumberFormatException e) {
            System.out.println("Error: Please enter a valid integer.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}
