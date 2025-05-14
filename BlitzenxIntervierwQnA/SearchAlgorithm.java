package BlitzenxIntervierwQnA;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Question 4: Find a value from a list of values using suitable search algorithm
 * 
 * This program demonstrates two search algorithms:
 * 1. Linear Search - works on any list
 * 2. Binary Search - works on sorted lists only
 */
public class SearchAlgorithm {
    
    /**
     * Linear search algorithm - works on any list
     * 
     * @param list List of integers to search
     * @param target Value to find
     * @return Index of the target if found, -1 otherwise
     */
    public static int linearSearch(List<Integer> list, int target) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) == target) {
                return i;
            }
        }
        return -1; // Not found
    }
    
    /**
     * Binary search algorithm - works on sorted lists only
     * 
     * @param list Sorted list of integers to search
     * @param target Value to find
     * @return Index of the target if found, -1 otherwise
     */
    public static int binarySearch(List<Integer> list, int target) {
        int left = 0;
        int right = list.size() - 1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            
            // Check if target is at mid
            if (list.get(mid) == target) {
                return mid;
            }
            
            // If target is greater, ignore left half
            if (list.get(mid) < target) {
                left = mid + 1;
            } 
            // If target is smaller, ignore right half
            else {
                right = mid - 1;
            }
        }
        
        return -1; // Not found
    }
    
    /**
     * Checks if a list is sorted in ascending order
     */
    public static boolean isSorted(List<Integer> list) {
        for (int i = 0; i < list.size() - 1; i++) {
            if (list.get(i) > list.get(i + 1)) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Sorts a list in ascending order using bubble sort
     */
    public static List<Integer> sortAscending(List<Integer> list) {
        List<Integer> result = new ArrayList<>(list);
        int n = result.size();
        
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (result.get(j) > result.get(j + 1)) {
                    // Swap
                    int temp = result.get(j);
                    result.set(j, result.get(j + 1));
                    result.set(j + 1, temp);
                }
            }
        }
        
        return result;
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Search Algorithm Demonstration");
        System.out.println("-----------------------------");
        
        try {
            // Get list size from user
            System.out.print("\nEnter the size of the list: ");
            int n = scanner.nextInt();
            
            if (n <= 0) {
                System.out.println("Error: List size must be positive.");
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
            
            // Get target value to search
            System.out.print("\nEnter the value to search for: ");
            int target = scanner.nextInt();
            
            // Display input list
            System.out.println("\nInput list: " + numbers);
            System.out.println("Searching for: " + target);
            
            // Perform linear search
            System.out.println("\n1. Linear Search:");
            long startTime = System.nanoTime();
            int linearResult = linearSearch(numbers, target);
            long endTime = System.nanoTime();
            
            if (linearResult != -1) {
                System.out.println("   Found at index: " + linearResult);
            } else {
                System.out.println("   Not found in the list");
            }
            System.out.println("   Time taken: " + (endTime - startTime) + " nanoseconds");
            
            // Check if list is sorted for binary search
            boolean isSorted = isSorted(numbers);
            List<Integer> sortedList = numbers;
            
            if (!isSorted) {
                System.out.println("\nNote: List is not sorted. Sorting for binary search...");
                sortedList = sortAscending(numbers);
                System.out.println("Sorted list: " + sortedList);
            }
            
            // Perform binary search
            System.out.println("\n2. Binary Search (on sorted list):");
            startTime = System.nanoTime();
            int binaryResult = binarySearch(sortedList, target);
            endTime = System.nanoTime();
            
            if (binaryResult != -1) {
                System.out.println("   Found at index: " + binaryResult);
            } else {
                System.out.println("   Not found in the list");
            }
            System.out.println("   Time taken: " + (endTime - startTime) + " nanoseconds");
            
            // Compare algorithms
            System.out.println("\nAlgorithm Comparison:");
            System.out.println("1. Linear Search:");
            System.out.println("   - Works on any list (sorted or unsorted)");
            System.out.println("   - Time Complexity: O(n) - may need to check every element");
            System.out.println("   - Space Complexity: O(1) - constant extra space");
            
            System.out.println("\n2. Binary Search:");
            System.out.println("   - Works only on sorted lists");
            System.out.println("   - Time Complexity: O(log n) - eliminates half the elements each time");
            System.out.println("   - Space Complexity: O(1) - constant extra space");
            System.out.println("   - Much faster for large sorted lists");
            
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}
