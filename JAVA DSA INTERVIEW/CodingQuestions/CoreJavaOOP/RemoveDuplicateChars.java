/**
 * Core Java & OOP - Question 5: Remove duplicate characters from a string
 */
package CodingQuestions.CoreJavaOOP;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class RemoveDuplicateChars {

    public static void main(String[] args) {
        String[] testCases = {
            "programming",
            "hello world",
            "java java java",
            "aaaaaa",
            "abcdefg",
            ""
        };
        
        for (String test : testCases) {
            System.out.println("Original string: \"" + test + "\"");
            System.out.println("Using LinkedHashSet: \"" + removeDuplicatesUsingLinkedHashSet(test) + "\"");
            System.out.println("Using StringBuilder: \"" + removeDuplicatesUsingStringBuilder(test) + "\"");
            System.out.println("Using Stream API: \"" + removeDuplicatesUsingStreamAPI(test) + "\"");
            System.out.println("Case-insensitive removal: \"" + removeDuplicatesCaseInsensitive(test) + "\"");
            System.out.println();
        }
    }

    /**
     * Problem Statement:
     * Write a Java program to remove duplicate characters from a string.
     * For example, if the input is "programming", the output should be "progamin".
     * 
     * Solution 1: Using LinkedHashSet to maintain order
     * 
     * Time Complexity: O(n) where n is the length of the string
     * Space Complexity: O(k) where k is the number of unique characters
     */
    public static String removeDuplicatesUsingLinkedHashSet(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        
        // LinkedHashSet maintains insertion order
        Set<Character> uniqueChars = new LinkedHashSet<>();
        
        // Add each character to the set
        for (char c : input.toCharArray()) {
            uniqueChars.add(c);
        }
        
        // Build the result string
        StringBuilder result = new StringBuilder();
        for (Character c : uniqueChars) {
            result.append(c);
        }
        
        return result.toString();
    }
    
    /**
     * Solution 2: Using StringBuilder and indexOf
     * 
     * Time Complexity: O(n²) where n is the length of the string
     * Space Complexity: O(k) where k is the number of unique characters
     */
    public static String removeDuplicatesUsingStringBuilder(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        
        StringBuilder result = new StringBuilder();
        
        // For each character in the input string
        for (int i = 0; i < input.length(); i++) {
            char currentChar = input.charAt(i);
            
            // If the character is not already in the result, add it
            if (result.indexOf(String.valueOf(currentChar)) == -1) {
                result.append(currentChar);
            }
        }
        
        return result.toString();
    }
    
    /**
     * Solution 3: Using Java 8 Stream API
     * 
     * Time Complexity: O(n) where n is the length of the string
     * Space Complexity: O(k) where k is the number of unique characters
     */
    public static String removeDuplicatesUsingStreamAPI(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        
        // Convert string to char array, then to stream, then collect unique chars
        return input.chars()                    // IntStream
                   .distinct()                  // Remove duplicates
                   .mapToObj(c -> (char) c)     // Convert int to Character
                   .collect(StringBuilder::new, // Collect into StringBuilder
                           StringBuilder::append,
                           StringBuilder::append)
                   .toString();
    }
    
    /**
     * Solution 4: Case-insensitive duplicate removal
     * 
     * Time Complexity: O(n) where n is the length of the string
     * Space Complexity: O(k) where k is the number of unique characters (case-insensitive)
     */
    public static String removeDuplicatesCaseInsensitive(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        
        Set<Character> seenChars = new HashSet<>();
        StringBuilder result = new StringBuilder();
        
        for (char c : input.toCharArray()) {
            // Convert to lowercase for case-insensitive comparison
            char lowerC = Character.toLowerCase(c);
            
            // If we haven't seen this character (case-insensitive), add it to the result
            if (!seenChars.contains(lowerC)) {
                result.append(c);  // Add the original character (preserving case)
                seenChars.add(lowerC);
            }
        }
        
        return result.toString();
    }
    
    /**
     * Logic Explanation:
     * 
     * 1. LinkedHashSet Approach:
     *    - We use a LinkedHashSet which automatically removes duplicates while maintaining insertion order.
     *    - We add each character to the set, which will only keep the first occurrence of each character.
     *    - Then we build a new string from the unique characters in the set.
     * 
     * 2. StringBuilder Approach:
     *    - We use a StringBuilder to build our result string.
     *    - For each character in the input, we check if it's already in our result.
     *    - If not, we add it to the result.
     *    - This approach is less efficient due to the indexOf operation which is O(n).
     * 
     * 3. Stream API Approach:
     *    - We convert the string to a stream of characters.
     *    - We use the distinct() method to remove duplicates.
     *    - Then we collect the unique characters back into a string.
     *    - This is a more modern and concise approach using Java 8 features.
     * 
     * 4. Case-insensitive Approach:
     *    - Similar to the first approach, but we convert characters to lowercase for comparison.
     *    - This allows us to treat 'A' and 'a' as the same character for deduplication purposes.
     *    - We still add the original character to the result to preserve the original case.
     * 
     * Real-world Use Cases:
     * - Data cleaning in text processing
     * - Removing duplicate entries in user input
     * - Creating unique identifiers from strings
     * - Simplifying search terms in search engines
     * - Generating compact representations of text for display purposes
     * - Creating unique tags or keywords from user-generated content
     */
}