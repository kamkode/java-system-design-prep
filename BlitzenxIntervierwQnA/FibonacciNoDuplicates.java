package BlitzenxIntervierwQnA;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * Question 2: Find the Fibonacci numbers and add them to a collection without duplicates
 * 
 * This program generates Fibonacci numbers and adds them to a HashSet,
 * which automatically ensures there are no duplicates.
 */
public class FibonacciNoDuplicates {
    
    /**
     * Generates Fibonacci numbers and stores them in a Set to avoid duplicates
     * 
     * @param n Number of Fibonacci numbers to generate
     * @return Set containing Fibonacci numbers without duplicates
     */
    public static Set<Long> generateFibonacci(int n) {
        Set<Long> fibSet = new HashSet<>();
        
        if (n <= 0) {
            return fibSet; // Return empty set for invalid input
        }
        
        // First Fibonacci number
        long a = 0;
        fibSet.add(a);
        
        if (n == 1) {
            return fibSet; // Return only first number if n=1
        }
        
        // Second Fibonacci number
        long b = 1;
        fibSet.add(b);
        
        // Generate remaining Fibonacci numbers
        for (int i = 2; i < n; i++) {
            // Check for potential overflow
            if (Long.MAX_VALUE - b < a) {
                System.out.println("Warning: Reached maximum Long value limit. Stopping generation.");
                break;
            }
            
            // Calculate next Fibonacci number
            long c = a + b;
            fibSet.add(c); // Add to set (duplicates are automatically handled)
            
            // Update values for next iteration
            a = b;
            b = c;
        }
        
        return fibSet;
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Fibonacci Generator - Without Duplicates");
        System.out.println("---------------------------------------");
        
        try {
            // Get input from user
            System.out.print("\nEnter the number of Fibonacci numbers to generate: ");
            int n = scanner.nextInt();
            
            if (n <= 0) {
                System.out.println("Please enter a positive number.");
                return;
            }
            
            // Generate Fibonacci numbers
            Set<Long> fibonacciNumbers = generateFibonacci(n);
            
            // Display results
            System.out.println("\nInput n: " + n);
            System.out.println("Fibonacci numbers without duplicates: " + fibonacciNumbers);
            System.out.println("Number of unique Fibonacci numbers: " + fibonacciNumbers.size());
            
            // Explain why there might be fewer numbers than requested
            if (fibonacciNumbers.size() < n) {
                System.out.println("\nNote: The set contains fewer than " + n + " numbers because:");
                System.out.println("1. Fibonacci sequence has no duplicates in the first " + n + " numbers, or");
                System.out.println("2. Generation stopped due to potential numeric overflow.");
            }
            
        } catch (Exception e) {
            System.out.println("Error: Please enter a valid integer.");
        } finally {
            scanner.close();
        }
    }
}
