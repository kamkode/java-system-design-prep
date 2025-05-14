package BlitzenxIntervierwQnA;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/*
 * PROBLEM STATEMENT:
 * Write a Java program to find the second-highest number in a list of integers.
 * The second-highest number is the number that is strictly less than the maximum
 * number in the list but greater than or equal to all other numbers.
 * Requirements:
 * - Use a List (e.g., ArrayList) to store the integers.
 * - Handle edge cases:
 *   - Empty list.
 *   - List with only one element.
 *   - List with all identical elements.
 *   - Lists with duplicates or negative numbers.
 * - Use Scanner to take input from the user (size of list and elements).
 * - Provide a clear solution with efficient time complexity.
 * - Output the second-highest number or an error message for edge cases.
 *
 * EXPLANATION:
 * Class: SecondHighestFinder
 * Purpose: Finds the second-highest number in a list of integers using an ArrayList.
 *
 * CONCEPTS INVOLVED:
 * 1. ArrayList: A dynamic, resizable array implementing the List interface.
 *    - Stores Integer objects (wrapper for int).
 *    - Example: List<Integer> numbers = new ArrayList<>();
 * 2. Scanner: Reads user input (list size and elements) from console.
 * 3. Iteration: Use a for-each loop to process each number.
 * 4. Comparison: Track the maximum and second-maximum numbers.
 * 5. Edge Cases:
 *    - Empty list: Throw exception.
 *    - Single element: No second-highest exists.
 *    - All identical: No distinct second-highest.
 *    - Duplicates: Second-highest may appear multiple times.
 * 6. Efficiency:
 *    - Time Complexity: O(n) with a single pass.
 *    - Space Complexity: O(1) extra space (excluding input list).
 * 7. OOP: Encapsulate logic in a class with private fields and public methods.
 * 8. Exception Handling: Use a custom exception for edge cases.
 *
 * ALGORITHM:
 * - Initialize two variables:
 *   - max: Tracks the highest number (initially Integer.MIN_VALUE).
 *   - secondMax: Tracks the second-highest (initially Integer.MIN_VALUE).
 * - Check edge cases:
 *   - Empty list: Throw NoSecondHighestException.
 *   - Single element: Throw NoSecondHighestException.
 * - Iterate through the list:
 *   - If current number > max:
 *     - secondMax = max
 *     - max = currentNumber
 *   - If max > current number > secondMax:
 *     - secondMax = currentNumber
 * - After iteration:
 *   - If secondMax is Integer.MIN_VALUE, all elements are identical.
 *   - Throw NoSecondHighestException if no second-highest exists.
 *   - Otherwise, return secondMax.
 *
 * WHY THIS APPROACH:
 * - Efficient: O(n) time complexity avoids sorting (O(n log n)).
 * - Robust: Handles all edge cases explicitly.
 * - Modular: Class-based design is reusable and maintainable.
 * - User-Friendly: Scanner allows dynamic input at runtime.
 *
 * IMPLEMENTATION DETAILS:
 * - Use ArrayList<Integer> for the input list.
 * - Create a custom exception (NoSecondHighestException) for errors.
 * - Defensive copy in constructor to prevent external modifications.
 * - Main method uses Scanner to:
 *   - Read an integer n (list size).
 *   - Read n integers to populate the ArrayList.
 *   - Create SecondHighestFinder instance and call findSecondHighest.
 *   - Print input list and result or error message.
 * - Input format:
 *   - First line: Integer n (list size, n >= 0).
 *   - Second line: n space-separated integers.
 * - Output format:
 *   - Print "Input: [list]" (e.g., Input: [3, 5, 2, 8, 1]).
 *   - Print "Second-highest number: X" or "Error: message".
 */

class NoSecondHighestException extends Exception {
    public NoSecondHighestException(String message) {
        super(message);
    }
}

public class SecondHighestFinder {
    private List<Integer> numbers;

    public SecondHighestFinder(List<Integer> numbers) {
        this.numbers = numbers != null ? new ArrayList<>(numbers) : new ArrayList<>();
    }

    public int findSecondHighest() throws NoSecondHighestException {
        validateInput();
        
        // Handle edge cases involving minimum values in the list
        boolean containsMinValue = numbers.contains(Integer.MIN_VALUE);
        
        // Initialize trackers for first and second highest numbers
        int max = containsMinValue ? Integer.MIN_VALUE : numbers.get(0);
        int secondMax = Integer.MIN_VALUE;
        boolean foundSecond = false;
        
        // First pass: find maximum value
        for (int num : numbers) {
            if (num > max) {
                max = num;
            }
        }
        
        // Second pass: find second maximum (strictly less than max)
        for (int num : numbers) {
            if (num < max && (num > secondMax || !foundSecond)) {
                secondMax = num;
                foundSecond = true;
            }
        }
        
        if (!foundSecond) {
            throw new NoSecondHighestException("No second-highest number exists (all elements are identical).");
        }
        
        return secondMax;
    }
    
    private void validateInput() throws NoSecondHighestException {
        if (numbers == null) {
            throw new NoSecondHighestException("The list is null.");
        }
        if (numbers.isEmpty()) {
            throw new NoSecondHighestException("The list is empty.");
        }
        if (numbers.size() == 1) {
            throw new NoSecondHighestException("The list has only one element.");
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.print("Enter the size of the list: ");
            if (!scanner.hasNextInt()) {
                System.out.println("Error: Please enter a valid integer for list size.");
                return;
            }
            
            int n = scanner.nextInt();
            if (n < 0) {
                System.out.println("Error: List size cannot be negative.");
                return;
            }
            
            List<Integer> numbers = new ArrayList<>();
            System.out.println("Enter " + n + " integers (one per line):");
            
            for (int i = 0; i < n; i++) {
                if (!scanner.hasNextInt()) {
                    scanner.next(); // consume invalid input
                    System.out.println("Warning: Non-integer input detected at position " + (i+1) + ". Using 0 instead.");
                    numbers.add(0);
                } else {
                    numbers.add(scanner.nextInt());
                }
            }
            
            System.out.println("Input: " + numbers);
            SecondHighestFinder finder = new SecondHighestFinder(numbers);
            
            try {
                int result = finder.findSecondHighest();
                System.out.println("Second-highest number: " + result);
            } catch (NoSecondHighestException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}