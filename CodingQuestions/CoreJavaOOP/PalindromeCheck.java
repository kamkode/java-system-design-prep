/**
 * Core Java & OOP - Question 2: Check if a string is a palindrome
 */
package CodingQuestions.CoreJavaOOP;

public class PalindromeCheck {

    public static void main(String[] args) {
        String[] testCases = {
            "racecar",
            "A man, a plan, a canal: Panama",
            "hello",
            "Madam, I'm Adam",
            "12321",
            ""
        };
        
        for (String test : testCases) {
            System.out.println("\"" + test + "\" is a palindrome: " + 
                isPalindromeSimple(test) + " (simple check)");
            System.out.println("\"" + test + "\" is a palindrome: " + 
                isPalindromeIgnoreNonAlphanumeric(test) + " (ignoring non-alphanumeric)");
            System.out.println();
        }
    }

    /**
     * Problem Statement:
     * Write a Java program to check if a given string is a palindrome.
     * A palindrome is a word, phrase, number, or other sequence of characters
     * that reads the same forward and backward (ignoring spaces, punctuation, and case).
     * 
     * Solution 1: Simple palindrome check (case-sensitive, includes all characters)
     * 
     * Time Complexity: O(n) where n is the length of the string
     * Space Complexity: O(1) - constant space
     */
    public static boolean isPalindromeSimple(String input) {
        if (input == null) {
            return false;
        }
        
        int left = 0;
        int right = input.length() - 1;
        
        while (left < right) {
            if (input.charAt(left) != input.charAt(right)) {
                return false;
            }
            left++;
            right--;
        }
        
        return true;
    }
    
    /**
     * Solution 2: Palindrome check ignoring non-alphanumeric characters and case
     * 
     * Time Complexity: O(n) where n is the length of the string
     * Space Complexity: O(1) - constant space
     */
    public static boolean isPalindromeIgnoreNonAlphanumeric(String input) {
        if (input == null) {
            return false;
        }
        
        int left = 0;
        int right = input.length() - 1;
        
        while (left < right) {
            // Skip non-alphanumeric characters from the left
            while (left < right && !Character.isLetterOrDigit(input.charAt(left))) {
                left++;
            }
            
            // Skip non-alphanumeric characters from the right
            while (left < right && !Character.isLetterOrDigit(input.charAt(right))) {
                right--;
            }
            
            // Compare characters (ignoring case)
            if (Character.toLowerCase(input.charAt(left)) != 
                Character.toLowerCase(input.charAt(right))) {
                return false;
            }
            
            left++;
            right--;
        }
        
        return true;
    }
    
    /**
     * Solution 3: Using StringBuilder (for demonstration, not the most efficient)
     * 
     * Time Complexity: O(n) where n is the length of the string
     * Space Complexity: O(n) for the new string
     */
    public static boolean isPalindromeUsingStringBuilder(String input) {
        if (input == null) {
            return false;
        }
        
        // Remove non-alphanumeric characters and convert to lowercase
        StringBuilder cleanString = new StringBuilder();
        for (char c : input.toCharArray()) {
            if (Character.isLetterOrDigit(c)) {
                cleanString.append(Character.toLowerCase(c));
            }
        }
        
        String cleaned = cleanString.toString();
        String reversed = cleanString.reverse().toString();
        
        return cleaned.equals(reversed);
    }
    
    /**
     * Logic Explanation:
     * 1. Simple Approach: We use two pointers, one starting from the beginning and one from the end.
     *    We compare characters at these positions and move the pointers towards the middle.
     *    If at any point the characters don't match, the string is not a palindrome.
     * 
     * 2. Ignoring Non-Alphanumeric: Similar to the first approach, but we skip non-alphanumeric
     *    characters and ignore case when comparing.
     * 
     * 3. StringBuilder Approach: We first clean the string by removing non-alphanumeric characters
     *    and converting to lowercase. Then we create a reversed version and compare the two.
     * 
     * Real-world Use Cases:
     * - Validating certain types of identifiers or codes that should be palindromic
     * - Text processing in natural language processing
     * - DNA sequence analysis (palindromic sequences)
     * - Puzzle games and word games
     * - Detecting certain types of data structures or patterns
     */
}