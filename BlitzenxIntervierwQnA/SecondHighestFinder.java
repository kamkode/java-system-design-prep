package BlitzenxIntervierwQnA;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Question 1: Find the 2nd highest number from a list of integers using List
 * 
 * This program finds the second highest number in a list of integers.
 * It uses a single pass through the list, tracking both the highest and
 * second highest values encountered.
 */
public class SecondHighestFinder {
    
    /**
     * Finds the second highest number in a list of integers
     * 
     * @param numbers List of integers
     * @return The second highest number
     * @throws IllegalArgumentException if list has fewer than 2 elements or all elements are the same
     */
    public static int findSecondHighest(List<Integer> numbers) {
        if (numbers == null || numbers.size() < 2) {
            throw new IllegalArgumentException("List must contain at least 2 elements");
        }
        
        int highest = Integer.MIN_VALUE;
        int secondHighest = Integer.MIN_VALUE;
        
        for (int num : numbers) {
            if (num > highest) {
                // Current highest becomes second highest
                secondHighest = highest;
                // Update highest
                highest = num;
            } else if (num > secondHighest && num < highest) {
                // Update second highest if current number is between highest and second highest
                secondHighest = num;
            }
        }
        
        // Check if we found a valid second highest
        if (secondHighest == Integer.MIN_VALUE) {
            // This happens when all elements are the same
            throw new IllegalArgumentException("No second highest element found (all elements are the same)");
        }
        
        return secondHighest;
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Second Highest Number Finder");
        System.out.println("---------------------------");
        
        try {
            // Get list size from user
            System.out.print("\nEnter the size of the list: ");
            int n = scanner.nextInt();
            
            if (n < 2) {
                System.out.println("Error: List must contain at least 2 elements.");
                return;
            }
            
            // Get list elements from user
            List<Integer> numbers = new ArrayList<>();
            System.out.println("\nEnter " + n + " integers (one per line):");
            
            for (int i = 0; i < n; i++) {
                System.out.print("Element " + (i + 1) + ": ");
                if (scanner.hasNextInt()) {
                    numbers.add(scanner.nextInt());
                } else {
                    scanner.next(); // consume invalid input
                    System.out.println("Warning: Non-integer input. Using 0 instead.");
                    numbers.add(0);
                }
            }
            
            // Display input list
            System.out.println("\nInput list: " + numbers);
            
            // Find and display second highest
            int secondHighest = findSecondHighest(numbers);
            System.out.println("Second highest number: " + secondHighest);
            
            // Explain the algorithm
            System.out.println("\nAlgorithm explanation:");
            System.out.println("1. Initialize highest and secondHighest to minimum possible value");
            System.out.println("2. Iterate through the list once");
            System.out.println("3. Update highest and secondHighest as needed");
            System.out.println("4. Time Complexity: O(n) - single pass through the list");
            System.out.println("5. Space Complexity: O(1) - constant extra space");
            
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: Invalid input.");
        } finally {
            scanner.close();
        }
    }
}
