/**
 * Core Java & OOP - Question 4: Count characters in a string
 */
package CodingQuestions.CoreJavaOOP;

import java.util.HashMap;
import java.util.Map;

public class CountCharacters {

    public static void main(String[] args) {
        String text = "Hello, World! This is a test string for counting characters.";
        
        System.out.println("Original text: " + text);
        System.out.println("\n--- Basic Character Count ---");
        countCharacters(text);
        
        System.out.println("\n--- Character Frequency ---");
        Map<Character, Integer> frequencyMap = getCharacterFrequency(text);
        printCharacterFrequency(frequencyMap);
        
        System.out.println("\n--- Character Type Count ---");
        countCharacterTypes(text);
        
        // Additional test case with different character types
        String mixedText = "Java123!@# is AWESOME456";
        System.out.println("\n\nOriginal text: " + mixedText);
        System.out.println("\n--- Character Type Count ---");
        countCharacterTypes(mixedText);
    }

    /**
     * Problem Statement:
     * Write a Java program to count the number of characters in a string.
     * The program should count total characters, and optionally provide a breakdown
     * of character types (letters, digits, spaces, special characters) or frequency of each character.
     * 
     * Solution 1: Basic character count
     * 
     * Time Complexity: O(n) where n is the length of the string
     * Space Complexity: O(1) - constant space
     */
    public static void countCharacters(String text) {
        if (text == null) {
            System.out.println("The string is null");
            return;
        }
        
        int totalChars = text.length();
        int charsWithoutSpaces = text.replace(" ", "").length();
        
        System.out.println("Total characters (including spaces): " + totalChars);
        System.out.println("Total characters (excluding spaces): " + charsWithoutSpaces);
        System.out.println("Total spaces: " + (totalChars - charsWithoutSpaces));
    }
    
    /**
     * Solution 2: Count frequency of each character using HashMap
     * 
     * Time Complexity: O(n) where n is the length of the string
     * Space Complexity: O(k) where k is the number of unique characters
     */
    public static Map<Character, Integer> getCharacterFrequency(String text) {
        if (text == null) {
            return new HashMap<>();
        }
        
        Map<Character, Integer> frequencyMap = new HashMap<>();
        
        for (char c : text.toCharArray()) {
            frequencyMap.put(c, frequencyMap.getOrDefault(c, 0) + 1);
        }
        
        return frequencyMap;
    }
    
    public static void printCharacterFrequency(Map<Character, Integer> frequencyMap) {
        for (Map.Entry<Character, Integer> entry : frequencyMap.entrySet()) {
            char c = entry.getKey();
            int count = entry.getValue();
            
            // Print the character (with special handling for space)
            if (c == ' ') {
                System.out.println("Space: " + count);
            } else {
                System.out.println("'" + c + "': " + count);
            }
        }
    }
    
    /**
     * Solution 3: Count different types of characters
     * 
     * Time Complexity: O(n) where n is the length of the string
     * Space Complexity: O(1) - constant space
     */
    public static void countCharacterTypes(String text) {
        if (text == null) {
            System.out.println("The string is null");
            return;
        }
        
        int letters = 0;
        int digits = 0;
        int spaces = 0;
        int uppercase = 0;
        int lowercase = 0;
        int specialChars = 0;
        
        for (char c : text.toCharArray()) {
            if (Character.isLetter(c)) {
                letters++;
                if (Character.isUpperCase(c)) {
                    uppercase++;
                } else {
                    lowercase++;
                }
            } else if (Character.isDigit(c)) {
                digits++;
            } else if (Character.isWhitespace(c)) {
                spaces++;
            } else {
                specialChars++;
            }
        }
        
        System.out.println("Letters: " + letters + " (Uppercase: " + uppercase + ", Lowercase: " + lowercase + ")");
        System.out.println("Digits: " + digits);
        System.out.println("Spaces: " + spaces);
        System.out.println("Special characters: " + specialChars);
        System.out.println("Total characters: " + text.length());
    }
    
    /**
     * Logic Explanation:
     * 
     * 1. Basic Character Count:
     *    - We simply count the total number of characters in the string using the length() method.
     *    - To count characters excluding spaces, we remove all spaces and then count.
     * 
     * 2. Character Frequency Count:
     *    - We use a HashMap to store each character as a key and its frequency as the value.
     *    - For each character in the string, we increment its count in the map.
     *    - Finally, we print each character and its frequency.
     * 
     * 3. Character Type Count:
     *    - We iterate through each character in the string and check its type using Character class methods.
     *    - We maintain separate counters for letters, digits, spaces, uppercase, lowercase, and special characters.
     *    - Finally, we print the count of each type.
     * 
     * Real-world Use Cases:
     * - Text analysis in natural language processing
     * - Password strength validation (checking for mix of character types)
     * - Data cleaning and preprocessing in text mining
     * - Compression algorithms that use character frequency
     * - Cryptography and code-breaking (frequency analysis)
     * - Word processors for character/word count features
     */
}