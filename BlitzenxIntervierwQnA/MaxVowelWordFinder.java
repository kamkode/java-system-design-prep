package BlitzenxIntervierwQnA;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/*
 * PROBLEM STATEMENT:
 * Write a Java program to find the word(s) with the maximum number of vowels in a given sentence.
 * If multiple words have the same maximum vowel count, return all of them.
 * 
 * Requirements:
 * - Consider 'a', 'e', 'i', 'o', 'u' (both lowercase and uppercase) as vowels.
 * - Use Scanner to take input from the user (the sentence).
 * - Handle edge cases:
 *   - Empty sentence or sentence with no vowels.
 *   - Sentence with multiple words having the same maximum vowel count.
 *   - Punctuation marks and special characters (ignore them in vowel counting).
 * - Output the word(s) with the maximum vowel count and the count itself.
 * - Include all details (problem statement, explanation, solution) in a single .java file.
 *
 * EXPLANATION:
 * Class: MaxVowelWordFinder
 * Purpose: Finds and returns the word(s) in a sentence that contain the maximum number of vowels.
 *
 * CONCEPTS INVOLVED:
 * 1. String Manipulation:
 *    - Splitting a sentence into words.
 *    - Counting vowels in a string.
 *    - Case-insensitive character comparison.
 * 2. Collections:
 *    - Using List to store words with the same vowel count.
 *    - Using Set to efficiently check for vowels.
 * 3. Scanner: Reads user input (the sentence) from console.
 * 4. Edge Cases:
 *    - Empty input: Return appropriate message.
 *    - No vowels in any word: Return message indicating zero vowels.
 *    - Multiple words with the same maximum: Return all of them.
 * 5. Efficiency:
 *    - Time Complexity: O(n) where n is the total number of characters in the sentence.
 *    - Space Complexity: O(m) where m is the number of words with the maximum vowel count.
 * 6. OOP: Encapsulate logic in a class with private helper methods and public interface.
 * 7. Error Handling: Validate input and handle exceptions appropriately.
 *
 * ALGORITHM:
 * 1. Split the input sentence into words using whitespace as a delimiter.
 * 2. Initialize variables to track the maximum vowel count and words with that count.
 * 3. For each word:
 *    a. Count the number of vowels in the word (case-insensitive).
 *    b. If the vowel count > current maximum:
 *       - Update the maximum vowel count.
 *       - Clear the list of words with maximum vowels.
 *       - Add the current word to the list.
 *    c. If the vowel count equals the current maximum:
 *       - Add the current word to the list.
 * 4. Return the list of words with the maximum vowel count and the count itself.
 *
 * WHY THIS APPROACH:
 * - Simple and efficient single-pass algorithm.
 * - Handles all required edge cases.
 * - Returns multiple words if they share the maximum count.
 * - Clearly separates concerns (vowel counting, word processing, result formatting).
 * - Avoids unnecessary sorting or multiple iterations through the sentence.
 *
 * IMPLEMENTATION DETAILS:
 * - Use String.split() to separate words.
 * - Create a helper method isVowel() to check if a character is a vowel.
 * - Create a method countVowels() to count vowels in a word.
 * - Create the main method findWordsWithMaxVowels() to find words with maximum vowels.
 * - Include input validation and proper error handling.
 * - Format the output clearly to show the words and their vowel counts.
 */

class InvalidInputException extends Exception {
    public InvalidInputException(String message) {
        super(message);
    }
}

public class MaxVowelWordFinder {
    
    // Set of vowels for efficient lookups
    private static final Set<Character> VOWELS = new HashSet<>();
    
    // Static initializer to populate the vowel set
    static {
        char[] vowelChars = {'a', 'e', 'i', 'o', 'u', 'A', 'E', 'I', 'O', 'U'};
        for (char c : vowelChars) {
            VOWELS.add(c);
        }
    }
    
    /**
     * Result class to hold the words with max vowels and the count
     */
    public static class VowelResult {
        private final List<String> words;
        private final int vowelCount;
        
        public VowelResult(List<String> words, int vowelCount) {
            this.words = words;
            this.vowelCount = vowelCount;
        }
        
        public List<String> getWords() {
            return words;
        }
        
        public int getVowelCount() {
            return vowelCount;
        }
    }
    
    /**
     * Finds words with the maximum number of vowels in a sentence.
     * 
     * @param sentence The input sentence to analyze
     * @return A VowelResult containing the words with max vowels and the count
     * @throws InvalidInputException If the input is null or empty
     */
    public VowelResult findWordsWithMaxVowels(String sentence) throws InvalidInputException {
        validateInput(sentence);
        
        // Clean the sentence and split into words
        String cleanedSentence = sentence.replaceAll("[^a-zA-Z\\s]", "").trim();
        if (cleanedSentence.isEmpty()) {
            throw new InvalidInputException("Sentence contains no valid words after cleaning.");
        }
        
        String[] words = cleanedSentence.split("\\s+");
        
        // Find words with max vowels
        int maxVowelCount = -1;
        List<String> maxVowelWords = new ArrayList<>();
        
        for (String word : words) {
            int vowelCount = countVowels(word);
            
            if (vowelCount > maxVowelCount) {
                maxVowelCount = vowelCount;
                maxVowelWords.clear();
                maxVowelWords.add(word);
            } else if (vowelCount == maxVowelCount) {
                maxVowelWords.add(word);
            }
        }
        
        return new VowelResult(maxVowelWords, maxVowelCount);
    }
    
    /**
     * Counts the number of vowels in a word.
     * 
     * @param word The word to count vowels in
     * @return The number of vowels in the word
     */
    private int countVowels(String word) {
        int count = 0;
        
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (isVowel(c)) {
                count++;
            }
        }
        
        return count;
    }
    
    /**
     * Checks if a character is a vowel (a, e, i, o, u, case-insensitive).
     * 
     * @param c The character to check
     * @return true if the character is a vowel, false otherwise
     */
    private boolean isVowel(char c) {
        return VOWELS.contains(c);
    }
    
    /**
     * Validates the input sentence.
     * 
     * @param sentence The sentence to validate
     * @throws InvalidInputException If the input is null or empty
     */
    private void validateInput(String sentence) throws InvalidInputException {
        if (sentence == null) {
            throw new InvalidInputException("Input sentence cannot be null.");
        }
        if (sentence.trim().isEmpty()) {
            throw new InvalidInputException("Input sentence cannot be empty.");
        }
    }
    
    /**
     * Alternative implementation that uses regular expressions to count vowels.
     * This method is provided as an example of an alternative approach.
     * 
     * @param word The word to count vowels in
     * @return The number of vowels in the word
     */
    private int countVowelsRegex(String word) {
        // Remove all non-vowels and count the remaining characters
        return word.toLowerCase().replaceAll("[^aeiou]", "").length();
    }
    
    /**
     * Formats the result message based on the finding.
     * 
     * @param result The VowelResult with words and vowel count
     * @return A formatted result message
     */
    private String formatResult(VowelResult result) {
        if (result.getVowelCount() == 0) {
            return "No vowels found in any word of the sentence.";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("Word(s) with maximum vowels (").append(result.getVowelCount()).append("): ");
        
        List<String> words = result.getWords();
        for (int i = 0; i < words.size(); i++) {
            sb.append(words.get(i));
            if (i < words.size() - 1) {
                sb.append(", ");
            }
        }
        
        return sb.toString();
    }
    
    /**
     * Demonstrates the vowel counting for each word.
     * 
     * @param sentence The input sentence
     */
    private void demonstrateVowelCounting(String sentence) {
        String cleanedSentence = sentence.replaceAll("[^a-zA-Z\\s]", "").trim();
        String[] words = cleanedSentence.split("\\s+");
        
        System.out.println("\nVowel count for each word:");
        System.out.println("-------------------------");
        
        for (String word : words) {
            int vowelCount = countVowels(word);
            System.out.println(word + ": " + vowelCount + " vowel(s)");
            
            // Show which characters are vowels
            StringBuilder vowelPositions = new StringBuilder("   Vowels: ");
            for (int i = 0; i < word.length(); i++) {
                char c = word.charAt(i);
                if (isVowel(c)) {
                    vowelPositions.append(c).append(" at position ").append(i + 1).append(", ");
                }
            }
            
            // Remove trailing comma and space
            if (vowelCount > 0) {
                vowelPositions.setLength(vowelPositions.length() - 2);
                System.out.println(vowelPositions);
            } else {
                System.out.println("   No vowels in this word");
            }
            
            System.out.println();
        }
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.println("Enter a sentence to find words with maximum vowels:");
            String sentence = scanner.nextLine();
            
            MaxVowelWordFinder finder = new MaxVowelWordFinder();
            
            try {
                VowelResult result = finder.findWordsWithMaxVowels(sentence);
                System.out.println(finder.formatResult(result));
                
                // Demonstrate how vowels are counted in each word
                System.out.println("\nWould you like to see detailed vowel analysis for each word? (y/n)");
                String showDetails = scanner.nextLine().trim().toLowerCase();
                
                if (showDetails.startsWith("y")) {
                    finder.demonstrateVowelCounting(sentence);
                }
                
            } catch (InvalidInputException e) {
                System.out.println("Error: " + e.getMessage());
            }
            
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}
