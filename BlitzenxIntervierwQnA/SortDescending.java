package BlitzenxIntervierwQnA;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Question 3: Sort a list of integers in descending order without using sort()
 * 
 * This program sorts a list of integers in descending order (largest to smallest)
 * using bubble sort algorithm without using any built-in sort methods.
 */
public class SortDescending {
    
    /**
     * Sorts a list of integers in descending order using bubble sort
     * 
     * @param numbers List of integers to sort
     * @return New list with integers sorted in descending order
     */
    public static List<Integer> sortDescending(List<Integer> numbers) {
        if (numbers == null || numbers.isEmpty()) {
            return new ArrayList<>();
        }
        
        if (numbers.size() == 1) {
            return new ArrayList<>(numbers);
        }
        
        // Create a copy of the input list to avoid modifying the original
        List<Integer> result = new ArrayList<>(numbers);
        
        // Bubble sort implementation for descending order
        int n = result.size();
        for (int i = 0; i < n - 1; i++) {
            boolean swapped = false;
            
            for (int j = 0; j < n - 1 - i; j++) {
                // Compare adjacent elements and swap if needed for descending order
                if (result.get(j) < result.get(j + 1)) {
                    // Swap elements
                    int temp = result.get(j);
                    result.set(j, result.get(j + 1));
                    result.set(j + 1, temp);
                    swapped = true;
                }
            }
            
            // If no swapping occurred in this pass, array is sorted
            if (!swapped) {
                break;
            }
        }
        
        return result;
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Descending Order Sorter - Without using sort()");
        System.out.println("---------------------------------------------");
        
        try {
            // Get list size from user
            System.out.print("\nEnter the size of the list: ");
            int n = scanner.nextInt();
            
            if (n < 0) {
                System.out.println("Error: List size cannot be negative.");
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
            System.out.println("\nInput: " + numbers);
            
            // Sort and display result
            List<Integer> sortedList = sortDescending(numbers);
            System.out.println("Sorted in descending order: " + sortedList);
            
            // Explain the sorting algorithm
            System.out.println("\nAlgorithm used: Bubble Sort");
            System.out.println("Time Complexity: O(n²) in worst case, O(n) in best case");
            System.out.println("Space Complexity: O(n) for the result list");
            System.out.println("\nHow it works:");
            System.out.println("1. Compare adjacent elements and swap if they are in the wrong order");
            System.out.println("2. For descending order, swap if left element < right element");
            System.out.println("3. After each pass, the smallest element 'bubbles' to the end");
            System.out.println("4. Optimization: If no swaps occur in a pass, the list is already sorted");
            
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}
