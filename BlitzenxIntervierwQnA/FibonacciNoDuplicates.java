package BlitzenxIntervierwQnA;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.math.BigInteger;

/*
 * PROBLEM STATEMENT:
 * Write a Java program to generate Fibonacci numbers and add them to a collection
 * without duplicates. The Fibonacci sequence starts with 0 and 1, where each
 * subsequent number is the sum of the two preceding ones (0, 1, 1, 2, 3, 5, 8, ...).
 * Requirements:
 * - Use a Set (e.g., HashSet) to store Fibonacci numbers, ensuring no duplicates.
 * - Use Scanner to take input from the user (number of Fibonacci numbers to generate).
 * - Handle edge cases:
 *   - Invalid input (e.g., non-positive number of terms).
 *   - Potential overflow for large Fibonacci numbers.
 * - Provide the most efficient solution without comments in the implementation.
 * - Include alternative coding logic as commented-out code for future use.
 * - Output the collection of unique Fibonacci numbers.
 * - Include all details (problem statement, explanation, solution, alternative logic) in a single .java file.
 *
 * EXPLANATION:
 * Class: FibonacciNoDuplicates
 * Purpose: Generates Fibonacci numbers and stores them in a HashSet to ensure no duplicates.
 *
 * CONCEPTS INVOLVED:
 * 1. Fibonacci Sequence: Starts with 0, 1; each number is the sum of the two preceding ones.
 * 2. HashSet: A collection that stores unique elements, automatically eliminating duplicates.
 *    - Example: Set<Long> fibSet = new HashSet<>();
 * 3. Scanner: Reads user input (number of terms) from console.
 * 4. Iteration: Generate Fibonacci numbers iteratively to avoid recursion overhead.
 * 5. Edge Cases:
 *    - Non-positive input (n <= 0): Throw exception.
 *    - Overflow: Stop generating if numbers exceed Long.MAX_VALUE (primary method).
 * 6. Efficiency:
 *    - Primary Method: O(n) time, O(n) space for HashSet.
 *    - Alternative Method: O(n * M) time (M is BigInteger operation cost), O(n) space.
 * 7. OOP: Encapsulate logic in a class with public methods.
 * 8. Exception Handling: Use a custom exception for invalid input.
 *
 * PRIMARY ALGORITHM (MOST EFFICIENT):
 * - Read integer n (number of Fibonacci numbers to generate) using Scanner.
 * - Validate input:
 *   - If n <= 0, throw InvalidInputException.
 * - Initialize a HashSet<Long> to store Fibonacci numbers.
 * - Generate Fibonacci numbers:
 *   - Start with a = 0, b = 1.
 *   - Add a to the HashSet.
 *   - Compute next number: c = a + b.
 *   - Check for overflow (c < 0 or c > Long.MAX_VALUE).
 *   - Update a = b, b = c.
 *   - Repeat n times or until overflow.
 * - Return the HashSet.
 * - In main, print the input n and the HashSet.
 * - Why Efficient: O(n) time using native long arithmetic, minimal overhead.
 *
 * ALTERNATIVE CODING LOGIC (COMMENTED OUT):
 * - Use BigInteger to generate Fibonacci numbers without overflow.
 * - Steps:
 *   - Import java.math.BigInteger.
 *   - Initialize BigInteger a = BigInteger.ZERO, b = BigInteger.ONE.
 *   - Create a HashSet<BigInteger> to store numbers.
 *   - For i from 0 to n-1:
 *     - Add a to the HashSet.
 *     - Compute c = a.add(b).
 *     - Update a = b, b = c.
 *   - Return the HashSet.
 * - Pros:
 *   - Handles arbitrarily large Fibonacci numbers (no overflow).
 *   - Suitable for very large n (e.g., n > 93, where Long overflows).
 * - Cons:
 *   - Slower due to BigInteger arithmetic (O(n * M), where M is operation cost).
 *   - Increased memory usage for large numbers.
 * - Use case: When n is large or precise large numbers are required.
 * - Implementation: Included as commented-out code in the class for future use.
 *
 * IMPLEMENTATION DETAILS:
 * - Use HashSet<Long> for the primary method to store unique Fibonacci numbers.
 * - Create a custom exception (InvalidInputException) for invalid input.
 * - Method generateFibonacci (uncommented) uses Long for efficiency.
 * - Alternative method (commented) uses BigInteger for large numbers.
 * - Main method uses Scanner to:
 *   - Read an integer n (number of terms).
 *   - Call generateFibonacci and store result in HashSet.
 *   - Print input n and the HashSet.
 * - Input format:
 *   - Single line: Integer n (number of Fibonacci numbers, n >= 0).
 * - Output format:
 *   - Line 1: "Input n: n"
 *   - Line 2: "Fibonacci numbers without duplicates: [set]" (e.g., [0, 1, 2, 3, 5]).
 * - Edge cases:
 *   - n <= 0: Throw exception with message "Invalid input: Number of terms must be positive."
 *   - Overflow: Primary method stops if sum exceeds Long.MAX_VALUE or becomes negative.
 */

class InvalidInputException extends Exception {
    public InvalidInputException(String message) {
        super(message);
    }
}

public class FibonacciNoDuplicates {
    public Set<Long> generateFibonacci(int n) throws InvalidInputException {
        validateInput(n);
        Set<Long> fibSet = new HashSet<>();
        long a = 0, b = 1;
        fibSet.add(a);
        if (n == 1) {
            return fibSet;
        }
        fibSet.add(b);
        
        try {
            for (int i = 2; i < n; i++) {
                if (Long.MAX_VALUE - b < a) {
                    System.out.println("Warning: Reached maximum Long value limit. Returning current set.");
                    break;
                }
                long c = a + b;
                fibSet.add(c);
                a = b;
                b = c;
            }
        } catch (ArithmeticException e) {
            System.out.println("Warning: Arithmetic overflow. Returning current set.");
        }
        
        return fibSet;
    }
    
    public Set<BigInteger> generateFibonacciBigInteger(int n) throws InvalidInputException {
        validateInput(n);
        Set<BigInteger> fibSet = new HashSet<>();
        BigInteger a = BigInteger.ZERO;
        BigInteger b = BigInteger.ONE;
        fibSet.add(a);
        if (n == 1) {
            return fibSet;
        }
        fibSet.add(b);
        
        for (int i = 2; i < n; i++) {
            BigInteger c = a.add(b);
            fibSet.add(c);
            a = b;
            b = c;
        }
        
        return fibSet;
    }
    
    private void validateInput(int n) throws InvalidInputException {
        if (n <= 0) {
            throw new InvalidInputException("Invalid input: Number of terms must be positive.");
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.print("Enter the number of Fibonacci terms to generate: ");
            if (!scanner.hasNextInt()) {
                System.out.println("Error: Please enter a valid integer.");
                return;
            }
            
            int n = scanner.nextInt();
            System.out.println("Input n: " + n);
            
            FibonacciNoDuplicates fib = new FibonacciNoDuplicates();
            
            System.out.println("Choose calculation method:\n1. Using Long (faster but limited size)\n2. Using BigInteger (slower but unlimited size)");
            int choice = 1;
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
            }
            
            if (choice == 2) {
                Set<BigInteger> bigResult = fib.generateFibonacciBigInteger(n);
                System.out.println("Fibonacci numbers without duplicates (BigInteger): " + bigResult);
            } else {
                Set<Long> result = fib.generateFibonacci(n);
                System.out.println("Fibonacci numbers without duplicates: " + result);
            }
        } catch (InvalidInputException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}