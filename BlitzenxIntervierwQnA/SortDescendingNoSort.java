package BlitzenxIntervierwQnA;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/*
 * PROBLEM STATEMENT:
 * Write a Java program to sort a list of integers in descending order (largest to smallest)
 * without using any built-in sort() methods (e.g., Collections.sort() or List.sort()).
 * Requirements:
 * - Use a List (e.g., ArrayList) to store the integers.
 * - Use Scanner to take input from the user (list size and elements).
 * - Handle edge cases:
 *   - Empty list.
 *   - Single element.
 *   - Lists with duplicates or negative numbers.
 * - Implement a custom sorting algorithm (not sort()).
 * - Include an alternative sorting algorithm as commented-out code for future use.
 * - Provide an efficient solution for typical input sizes.
 * - Output the sorted list in descending order.
 * - Include all details (problem statement, explanation, solution, alternative logic) in a single .java file.
 *
 * EXPLANATION:
 * Class: SortDescendingNoSort
 * Purpose: Sorts a list of integers in descending order using a custom algorithm and stores in an ArrayList.
 *
 * CONCEPTS INVOLVED:
 * 1. ArrayList: A dynamic, resizable array implementing the List interface.
 *    - Stores Integer objects.
 *    - Example: List<Integer> numbers = new ArrayList<>();
 * 2. Scanner: Reads user input (list size and elements) from console with validation.
 * 3. Sorting: Custom algorithm to arrange elements in descending order (largest first).
 * 4. Edge Cases:
 *    - Empty list (n = 0): Throw exception.
 *    - Single element: Already sorted, return as-is.
 *    - Duplicates/Negative numbers: Handled by comparison logic.
 *    - Invalid input: Non-integer or negative list size.
 * 5. Efficiency:
 *    - Primary Method (Bubble Sort): O(n²) time, O(1) extra space.
 *    - Alternative Method (Selection Sort): O(n²) time, O(1) extra space.
 * 6. OOP: Encapsulate logic in a class with public methods.
 * 7. Exception Handling: Use a custom exception for invalid input.
 *
 * PRIMARY ALGORITHM (BUBBLE SORT - MOST EFFICIENT FOR SIMPLICITY):
 * - Read integer n (list size) and n integers using Scanner with validation.
 * - Validate input:
 *   - If n < 0 or non-integer, throw InvalidInputException.
 *   - If n = 0, throw InvalidInputException for empty list.
 * - Store integers in an ArrayList<Integer>.
 * - Bubble Sort for descending order:
 *   - For each pass i from 0 to n-1:
 *     - Compare adjacent elements (j and j+1).
 *     - If numbers[j] < numbers[j+1], swap them to move larger element left.
 *     - Track if any swaps occurred; if none, list is sorted, break early.
 * - Return the sorted ArrayList.
 * - In main, print the input list and sorted list.
 * - Why Chosen:
 *   - Simple to implement and understand.
 *   - Stable (maintains relative order of equal elements).
 *   - Early termination optimizes for partially sorted lists.
 *   - Suitable for small lists (common in user input).
 *
 * ALTERNATIVE CODING LOGIC (SELECTION SORT - COMMENTED OUT):
 * - Use Selection Sort for descending order.
 * - Steps:
 *   - For each index i from 0 to n-1:
 *     - Find the maximum element in the unsorted portion (i to n-1).
 *     - Swap the maximum with the element at index i.
 *   - Continue until the list is sorted.
 * - Pros:
 *   - Fewer swaps than Bubble Sort (O(n) swaps vs O(n²) for Bubble Sort).
 *   - Simple logic based on finding maximum.
 * - Cons:
 *   - Always performs O(n²) comparisons, no early termination.
 *   - Slightly more complex to track maximum index.
 * - Use case: When minimizing swaps is important (e.g., costly swap operations).
 * - Implementation: Included as commented-out code in the class for future use.
 *
 * IMPLEMENTATION DETAILS:
 * - Use ArrayList<Integer> for the input list.
 * - Create a custom exception (InvalidInputException) for invalid input (empty, null, non-integer).
 * - Method sortDescending (uncommented) uses Bubble Sort for efficiency and simplicity.
 * - Alternative method (commented) uses Selection Sort for descending order.
 * - Main method uses Scanner to:
 *   - Read an integer n (list size, n >= 0) with validation.
 *   - Read n integers to populate the ArrayList with validation.
 *   - Call sortDescending and print input and sorted list.
 * - Input format:
 *   - First line: Integer n (list size, n >= 0).
 *   - Second line: n space-separated integers.
 * - Output format:
 *   - Line 1: "Input: [list]" (e.g., Input: [3, 5, 2, 8, 1]).
 *   - Line 2: "Sorted in descending order: [list]" (e.g., [8, 5, 3, 2, 1]).
 * - Edge cases:
 *   - n <= 0 or non-integer: Throw exception with message "Invalid input: List size must be positive."
 *   - Empty list (n = 0): Throw exception.
 *   - Single element: Return as-is (no sorting needed).
 *   - Duplicates/Negative numbers: Handled naturally by comparison.
 */

class InvalidInputException extends Exception {
    public InvalidInputException(String message) {
        super(message);
    }
}

public class SortDescendingNoSort {
    public List<Integer> sortDescending(List<Integer> numbers) throws InvalidInputException {
        validateInput(numbers);
        
        if (numbers.size() == 1) {
            return new ArrayList<>(numbers);
        }
        
        List<Integer> result = new ArrayList<>(numbers);
        bubbleSortDescending(result);
        return result;
    }
    
    // Bubble Sort implementation optimized for descending order
    private void bubbleSortDescending(List<Integer> list) {
        int n = list.size();
        boolean swapped;
        
        for (int i = 0; i < n - 1; i++) {
            swapped = false;
            for (int j = 0; j < n - 1 - i; j++) {
                if (list.get(j) < list.get(j + 1)) {
                    // Swap elements
                    swap(list, j, j + 1);
                    swapped = true;
                }
            }
            
            // If no swapping occurred in this pass, array is sorted
            if (!swapped) {
                break;
            }
        }
    }
    
    // Selection Sort implementation for descending order
    public List<Integer> sortDescendingSelection(List<Integer> numbers) throws InvalidInputException {
        validateInput(numbers);
        
        if (numbers.size() == 1) {
            return new ArrayList<>(numbers);
        }
        
        List<Integer> result = new ArrayList<>(numbers);
        selectionSortDescending(result);
        return result;
    }
    
    private void selectionSortDescending(List<Integer> list) {
        int n = list.size();
        
        for (int i = 0; i < n - 1; i++) {
            int maxIndex = findMaxIndex(list, i, n);
            
            if (maxIndex != i) {
                swap(list, i, maxIndex);
            }
        }
    }
    
    private int findMaxIndex(List<Integer> list, int startIndex, int endIndex) {
        int maxIndex = startIndex;
        
        for (int j = startIndex + 1; j < endIndex; j++) {
            if (list.get(j) > list.get(maxIndex)) {
                maxIndex = j;
            }
        }
        
        return maxIndex;
    }
    
    private void swap(List<Integer> list, int i, int j) {
        int temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
    }
    
    private void validateInput(List<Integer> numbers) throws InvalidInputException {
        if (numbers == null) {
            throw new InvalidInputException("Invalid input: List cannot be null.");
        }
        if (numbers.isEmpty()) {
            throw new InvalidInputException("Invalid input: List cannot be empty.");
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.print("Enter the size of the list: ");
            if (!scanner.hasNextInt()) {
                System.out.println("Error: Invalid input: List size must be an integer.");
                return;
            }
            
            int n = scanner.nextInt();
            if (n < 0) {
                System.out.println("Error: Invalid input: List size must be non-negative.");
                return;
            }
            
            List<Integer> numbers = new ArrayList<>();
            System.out.println("Enter " + n + " integers (one per line):");
            
            for (int i = 0; i < n; i++) {
                if (!scanner.hasNextInt()) {
                    scanner.next(); // consume invalid input
                    System.out.println("Warning: Non-integer input at position " + (i+1) + ". Using 0 instead.");
                    numbers.add(0);
                } else {
                    numbers.add(scanner.nextInt());
                }
            }
            
            System.out.println("\nInput: " + numbers);
            
            if (!numbers.isEmpty()) {
                SortDescendingNoSort sorter = new SortDescendingNoSort();
                
                System.out.println("Choose sorting algorithm:\n1. Bubble Sort\n2. Selection Sort");
                int choice = 1; // Default to bubble sort
                if (scanner.hasNextInt()) {
                    choice = scanner.nextInt();
                }
                
                List<Integer> result;
                if (choice == 2) {
                    result = sorter.sortDescendingSelection(numbers);
                    System.out.println("Sorted in descending order (Selection Sort): " + result);
                } else {
                    result = sorter.sortDescending(numbers);
                    System.out.println("Sorted in descending order (Bubble Sort): " + result);
                }
            } else {
                System.out.println("No elements to sort.");
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