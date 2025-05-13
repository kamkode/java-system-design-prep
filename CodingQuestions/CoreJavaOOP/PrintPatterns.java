/**
 * Core Java & OOP - Question 10: Print patterns using loops
 */
package CodingQuestions.CoreJavaOOP;

import java.util.Scanner;

public class PrintPatterns {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Pattern Printing Examples");
        System.out.println("------------------------");
        
        // Print a few patterns with fixed sizes
        System.out.println("\n1. Right Triangle (height 5):");
        printRightTriangle(5);
        
        System.out.println("\n2. Pyramid (height 5):");
        printPyramid(5);
        
        System.out.println("\n3. Diamond (height 5):");
        printDiamond(5);
        
        System.out.println("\n4. Hollow Square (size 5):");
        printHollowSquare(5);
        
        System.out.println("\n5. Number Pattern (size 5):");
        printNumberPattern(5);
        
        // Interactive mode
        System.out.println("\nInteractive Mode");
        System.out.println("---------------");
        System.out.println("Choose a pattern to print:");
        System.out.println("1. Right Triangle");
        System.out.println("2. Pyramid");
        System.out.println("3. Diamond");
        System.out.println("4. Hollow Square");
        System.out.println("5. Number Pattern");
        System.out.print("Enter your choice (1-5): ");
        
        try {
            int choice = scanner.nextInt();
            System.out.print("Enter size/height: ");
            int size = scanner.nextInt();
            
            switch (choice) {
                case 1:
                    printRightTriangle(size);
                    break;
                case 2:
                    printPyramid(size);
                    break;
                case 3:
                    printDiamond(size);
                    break;
                case 4:
                    printHollowSquare(size);
                    break;
                case 5:
                    printNumberPattern(size);
                    break;
                default:
                    System.out.println("Invalid choice!");
            }
        } catch (Exception e) {
            System.out.println("Invalid input!");
        } finally {
            scanner.close();
        }
    }

    /**
     * Problem Statement:
     * Write Java programs to print various patterns using loops.
     * Patterns are a common way to test understanding of loops and control structures.
     * 
     * Solution 1: Print a right triangle pattern
     * 
     * Example output for size 5:
     * *
     * **
     * ***
     * ****
     * *****
     * 
     * Time Complexity: O(n²) where n is the height
     * Space Complexity: O(1)
     */
    public static void printRightTriangle(int height) {
        if (height <= 0) {
            System.out.println("Height must be positive!");
            return;
        }
        
        for (int i = 1; i <= height; i++) {
            for (int j = 1; j <= i; j++) {
                System.out.print("*");
            }
            System.out.println();
        }
    }
    
    /**
     * Solution 2: Print a pyramid pattern
     * 
     * Example output for size 5:
     *     *
     *    ***
     *   *****
     *  *******
     * *********
     * 
     * Time Complexity: O(n²) where n is the height
     * Space Complexity: O(1)
     */
    public static void printPyramid(int height) {
        if (height <= 0) {
            System.out.println("Height must be positive!");
            return;
        }
        
        for (int i = 0; i < height; i++) {
            // Print spaces
            for (int j = 0; j < height - i - 1; j++) {
                System.out.print(" ");
            }
            
            // Print stars
            for (int k = 0; k < 2 * i + 1; k++) {
                System.out.print("*");
            }
            
            System.out.println();
        }
    }
    
    /**
     * Solution 3: Print a diamond pattern
     * 
     * Example output for size 5:
     *     *
     *    ***
     *   *****
     *  *******
     * *********
     *  *******
     *   *****
     *    ***
     *     *
     * 
     * Time Complexity: O(n²) where n is the height
     * Space Complexity: O(1)
     */
    public static void printDiamond(int height) {
        if (height <= 0) {
            System.out.println("Height must be positive!");
            return;
        }
        
        // Upper half of the diamond
        for (int i = 0; i < height; i++) {
            // Print spaces
            for (int j = 0; j < height - i - 1; j++) {
                System.out.print(" ");
            }
            
            // Print stars
            for (int k = 0; k < 2 * i + 1; k++) {
                System.out.print("*");
            }
            
            System.out.println();
        }
        
        // Lower half of the diamond
        for (int i = height - 2; i >= 0; i--) {
            // Print spaces
            for (int j = 0; j < height - i - 1; j++) {
                System.out.print(" ");
            }
            
            // Print stars
            for (int k = 0; k < 2 * i + 1; k++) {
                System.out.print("*");
            }
            
            System.out.println();
        }
    }
    
    /**
     * Solution 4: Print a hollow square pattern
     * 
     * Example output for size 5:
     * *****
     * *   *
     * *   *
     * *   *
     * *****
     * 
     * Time Complexity: O(n²) where n is the size
     * Space Complexity: O(1)
     */
    public static void printHollowSquare(int size) {
        if (size <= 0) {
            System.out.println("Size must be positive!");
            return;
        }
        
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                // Print stars only for the border
                if (i == 0 || i == size - 1 || j == 0 || j == size - 1) {
                    System.out.print("*");
                } else {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
    }
    
    /**
     * Solution 5: Print a number pattern
     * 
     * Example output for size 5:
     * 1
     * 12
     * 123
     * 1234
     * 12345
     * 
     * Time Complexity: O(n²) where n is the size
     * Space Complexity: O(1)
     */
    public static void printNumberPattern(int size) {
        if (size <= 0) {
            System.out.println("Size must be positive!");
            return;
        }
        
        for (int i = 1; i <= size; i++) {
            for (int j = 1; j <= i; j++) {
                System.out.print(j);
            }
            System.out.println();
        }
    }
    
    /**
     * Logic Explanation:
     * 
     * 1. Right Triangle Pattern:
     *    - Uses nested loops where the outer loop controls the rows
     *    - The inner loop prints stars equal to the current row number
     * 
     * 2. Pyramid Pattern:
     *    - Uses nested loops with three components:
     *      a. Outer loop for rows
     *      b. First inner loop for printing spaces (decreasing with each row)
     *      c. Second inner loop for printing stars (increasing with each row)
     * 
     * 3. Diamond Pattern:
     *    - Combines two pyramid patterns:
     *      a. First half is a regular pyramid
     *      b. Second half is an inverted pyramid
     * 
     * 4. Hollow Square Pattern:
     *    - Uses nested loops where we print stars only for border cells
     *    - A cell is on the border if it's in the first/last row or first/last column
     * 
     * 5. Number Pattern:
     *    - Similar to the right triangle but prints numbers instead of stars
     *    - The printed number corresponds to the column number
     * 
     * Real-world Use Cases:
     * - Teaching programming fundamentals
     * - Testing understanding of loops and control structures
     * - Creating visual displays or ASCII art
     * - Generating formatted reports
     * - Creating data visualization patterns
     */
}