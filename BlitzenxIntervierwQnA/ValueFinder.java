package BlitzenxIntervierwQnA;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/*
 * PROBLEM STATEMENT:
 * Write a Java program to find a specific value within a list of integers using appropriate search algorithms.
 * The program should implement both linear search and binary search algorithms and compare their efficiency.
 * 
 * Requirements:
 * - Use a List (e.g., ArrayList) to store the integers.
 * - Implement both Linear Search and Binary Search algorithms.
 * - Use Scanner to take input from the user (list size, elements, and search value).
 * - Handle edge cases:
 *   - Empty list.
 *   - List with only one element.
 *   - Value not present in the list.
 *   - Multiple occurrences of the search value (return first occurrence).
 * - Output the index of the value if found, or an appropriate message if not found.
 * - Compare and display the efficiency of both search algorithms by counting comparisons.
 * - Include all details (problem statement, explanation, solution) in a single .java file.
 *
 * EXPLANATION:
 * Class: ValueFinder
 * Purpose: Finds a specific value in a list of integers using different search algorithms.
 *
 * CONCEPTS INVOLVED:
 * 1. Search Algorithms:
 *    - Linear Search: O(n) - Checks each element sequentially until a match is found.
 *    - Binary Search: O(log n) - Requires a sorted list, repeatedly divides the search interval in half.
 * 2. ArrayList: A dynamic, resizable array implementing the List interface.
 *    - Stores Integer objects for the list.
 *    - Example: List<Integer> numbers = new ArrayList<>();
 * 3. Scanner: Reads user input (list size, elements, search value) from console.
 * 4. Time Complexity Analysis:
 *    - Counting comparisons to demonstrate algorithm efficiency.
 * 5. Edge Cases:
 *    - Empty list: Throw exception.
 *    - Search value not in list: Return -1 with appropriate message.
 *    - Unsorted list for binary search: Sort first (with warning).
 * 6. Efficiency:
 *    - Linear Search: O(n) time complexity - simple but inefficient for large lists.
 *    - Binary Search: O(log n) time complexity - efficient but requires sorted list.
 * 7. OOP: Encapsulate logic in a class with private fields and public methods.
 * 8. Exception Handling: Use custom exceptions for invalid inputs.
 *
 * ALGORITHMS:
 * 1. Linear Search:
 *    - Initialize a counter to track comparisons.
 *    - Iterate through each element in the list:
 *      - Increment the comparison counter.
 *      - If current element equals the search value, return its index.
 *    - If no match is found, return -1.
 *    - Return the number of comparisons for efficiency analysis.
 *
 * 2. Binary Search:
 *    - Ensure the list is sorted in ascending order.
 *    - Initialize a counter to track comparisons.
 *    - Initialize left = 0, right = size-1.
 *    - While left <= right:
 *      - Calculate mid = (left + right) / 2.
 *      - Increment the comparison counter.
 *      - If list[mid] == search value, return mid.
 *      - If list[mid] < search value, set left = mid + 1.
 *      - If list[mid] > search value, set right = mid - 1.
 *    - If no match is found, return -1.
 *    - Return the number of comparisons for efficiency analysis.
 *
 * WHY THESE APPROACHES:
 * - Linear Search: Simple, works on unsorted lists, but inefficient for large lists.
 * - Binary Search: Very efficient (O(log n)), but requires a sorted list.
 * - Providing both allows demonstrating algorithm selection based on list characteristics:
 *   - For small lists: Linear search may be faster due to low overhead.
 *   - For large lists: Binary search is significantly more efficient if the list is sorted.
 *   - For unsorted lists: Linear search or sort-then-binary-search depending on repeated searches.
 *
 * IMPLEMENTATION DETAILS:
 * - Use ArrayList<Integer> for the input list.
 * - Create methods for both search algorithms that return SearchResult objects.
 * - SearchResult includes index and comparison count.
 * - Custom exception (InvalidInputException) for error handling.
 * - Main method uses Scanner to:
 *   - Read an integer n (list size).
 *   - Read n integers to populate the ArrayList.
 *   - Read a search value.
 *   - Execute both search algorithms.
 *   - Print results and comparison of efficiency.
 * - Input validation prevents invalid inputs.
 * - Output format:
 *   - Print "Input list: [list]"
 *   - Print "Searching for: value"
 *   - Print search results with comparison counts.
 *   - Compare efficiency of algorithms.
 */

class InvalidInputException extends Exception {
    public InvalidInputException(String message) {
        super(message);
    }
}

class SearchResult {
    private final int index;
    private final int comparisons;
    
    public SearchResult(int index, int comparisons) {
        this.index = index;
        this.comparisons = comparisons;
    }
    
    public int getIndex() {
        return index;
    }
    
    public int getComparisons() {
        return comparisons;
    }
}

public class ValueFinder {
    
    /**
     * Performs a linear search on the list to find the target value.
     * Time Complexity: O(n) where n is the size of the list.
     * 
     * @param numbers The list to search in
     * @param target The value to search for
     * @return A SearchResult containing the index of the found item and number of comparisons
     */
    public SearchResult linearSearch(List<Integer> numbers, int target) throws InvalidInputException {
        validateInput(numbers);
        
        int comparisons = 0;
        for (int i = 0; i < numbers.size(); i++) {
            comparisons++;
            if (numbers.get(i) == target) {
                return new SearchResult(i, comparisons);
            }
        }
        
        return new SearchResult(-1, comparisons);
    }
    
    /**
     * Performs a binary search on the list to find the target value.
     * The list must be sorted in ascending order for this to work correctly.
     * Time Complexity: O(log n) where n is the size of the list.
     * 
     * @param numbers The sorted list to search in
     * @param target The value to search for
     * @return A SearchResult containing the index of the found item and number of comparisons
     */
    public SearchResult binarySearch(List<Integer> numbers, int target) throws InvalidInputException {
        validateInput(numbers);
        
        // For binary search, we need a sorted list
        boolean isSorted = true;
        for (int i = 1; i < numbers.size(); i++) {
            if (numbers.get(i - 1) > numbers.get(i)) {
                isSorted = false;
                break;
            }
        }
        
        if (!isSorted) {
            System.out.println("Warning: List is not sorted. Binary search requires a sorted list.");
            System.out.println("Sorting the list in ascending order...");
            numbers = new ArrayList<>(numbers);  // Create a copy to avoid modifying original
            bubbleSortAscending(numbers);
            System.out.println("Sorted list: " + numbers);
        }
        
        int left = 0;
        int right = numbers.size() - 1;
        int comparisons = 0;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;  // Avoid potential overflow
            comparisons++;
            
            if (numbers.get(mid) == target) {
                return new SearchResult(mid, comparisons);
            }
            
            if (numbers.get(mid) < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        return new SearchResult(-1, comparisons);
    }
    
    /**
     * Simple bubble sort to sort a list in ascending order.
     * Used to prepare lists for binary search if they aren't sorted.
     * 
     * @param list The list to sort
     */
    private void bubbleSortAscending(List<Integer> list) {
        int n = list.size();
        boolean swapped;
        
        for (int i = 0; i < n - 1; i++) {
            swapped = false;
            for (int j = 0; j < n - 1 - i; j++) {
                if (list.get(j) > list.get(j + 1)) {
                    // Swap elements
                    int temp = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, temp);
                    swapped = true;
                }
            }
            
            // If no swapping occurred in this pass, array is sorted
            if (!swapped) {
                break;
            }
        }
    }
    
    /**
     * Validates the input list to ensure it's not null or empty.
     * 
     * @param numbers The list to validate
     * @throws InvalidInputException If the list is null or empty
     */
    private void validateInput(List<Integer> numbers) throws InvalidInputException {
        if (numbers == null) {
            throw new InvalidInputException("Invalid input: List cannot be null.");
        }
        if (numbers.isEmpty()) {
            throw new InvalidInputException("Invalid input: List cannot be empty.");
        }
    }
    
    /**
     * Formats the search result message based on the index and comparisons.
     * 
     * @param algorithm The name of the algorithm used
     * @param result The SearchResult object containing the index and comparisons
     * @param target The target value that was searched for
     * @return A formatted result message
     */
    private String formatSearchResult(String algorithm, SearchResult result, int target) {
        if (result.getIndex() == -1) {
            return algorithm + ": Value " + target + " not found. Made " + result.getComparisons() + " comparisons.";
        } else {
            return algorithm + ": Value " + target + " found at index " + result.getIndex() + 
                   ". Made " + result.getComparisons() + " comparisons.";
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
                    System.out.println("Warning: Non-integer input at position " + (i+1) + ". Using 0 instead.");
                    numbers.add(0);
                } else {
                    numbers.add(scanner.nextInt());
                }
            }
            
            System.out.println("\nInput list: " + numbers);
            
            if (!numbers.isEmpty()) {
                System.out.print("Enter the value to search for: ");
                if (!scanner.hasNextInt()) {
                    System.out.println("Error: Please enter a valid integer to search for.");
                    return;
                }
                
                int target = scanner.nextInt();
                System.out.println("Searching for: " + target);
                
                ValueFinder finder = new ValueFinder();
                
                // Perform linear search
                SearchResult linearResult = finder.linearSearch(numbers, target);
                System.out.println(finder.formatSearchResult("Linear Search", linearResult, target));
                
                // Perform binary search
                SearchResult binaryResult = finder.binarySearch(new ArrayList<>(numbers), target);
                System.out.println(finder.formatSearchResult("Binary Search", binaryResult, target));
                
                // Compare efficiency
                System.out.println("\nEfficiency Comparison:");
                if (linearResult.getComparisons() < binaryResult.getComparisons()) {
                    System.out.println("Linear Search was more efficient for this search.");
                } else if (linearResult.getComparisons() > binaryResult.getComparisons()) {
                    System.out.println("Binary Search was more efficient for this search.");
                } else {
                    System.out.println("Both algorithms had the same efficiency for this search.");
                }
                
                System.out.println("\nAlgorithm Selection Tips:");
                System.out.println("- Linear Search is better for unsorted lists or small datasets.");
                System.out.println("- Binary Search is significantly faster for large, sorted datasets.");
                System.out.println("- For repeated searches in the same dataset, it's often worth sorting first, then using Binary Search.");
            } else {
                System.out.println("No elements to search in.");
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
