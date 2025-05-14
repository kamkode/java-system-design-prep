package BlitzenxIntervierwQnA;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Question 6: Find the words with the maximum number of vowels from a sentence
 * 
 * This program finds all words in a sentence that have the maximum number of vowels.
 */
public class MaxVowelsFinder {
    
    /**
     * Counts the number of vowels in a word
     * 
     * @param word The word to check
     * @return Number of vowels in the word
     */
    public static int countVowels(String word) {
        if (word == null || word.isEmpty()) {
            return 0;
        }
        
        int count = 0;
        String lowerWord = word.toLowerCase();
        
        for (int i = 0; i < lowerWord.length(); i++) {
            char ch = lowerWord.charAt(i);
            if (ch == 'a' || ch == 'e' || ch == 'i' || ch == 'o' || ch == 'u') {
                count++;
            }
        }
        
        return count;
    }
    
    /**
     * Finds words with the maximum number of vowels in a sentence
     * 
     * @param sentence The input sentence
     * @return List of words with the maximum number of vowels
     */
    public static List<String> findWordsWithMaxVowels(String sentence) {
        if (sentence == null || sentence.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        // Split the sentence into words
        String[] words = sentence.split("\\s+");
        List<String> maxVowelWords = new ArrayList<>();
        int maxVowelCount = 0;
        
        // Find the maximum vowel count
        for (String word : words) {
            // Remove any punctuation
            String cleanWord = word.replaceAll("[^a-zA-Z]", "");
            if (cleanWord.isEmpty()) {
                continue;
            }
            
            int vowelCount = countVowels(cleanWord);
            
            if (vowelCount > maxVowelCount) {
                // Found a new maximum
                maxVowelCount = vowelCount;
                maxVowelWords.clear();
                maxVowelWords.add(cleanWord);
            } else if (vowelCount == maxVowelCount) {
                // Add to the list of words with max vowels
                maxVowelWords.add(cleanWord);
            }
        }
        
        return maxVowelWords;
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Maximum Vowels Finder");
        System.out.println("---------------------");
        
        try {
            // Get input sentence from user
            System.out.println("\nEnter a sentence:");
            String sentence = scanner.nextLine();
            
            if (sentence.trim().isEmpty()) {
                System.out.println("Error: Please enter a non-empty sentence.");
                return;
            }
            
            // Find words with maximum vowels
            List<String> maxVowelWords = findWordsWithMaxVowels(sentence);
            
            // Display results
            System.out.println("\nInput sentence: \"" + sentence + "\"");
            
            if (maxVowelWords.isEmpty()) {
                System.out.println("No words with vowels found in the sentence.");
            } else {
                int maxVowelCount = countVowels(maxVowelWords.get(0));
                System.out.println("Maximum number of vowels: " + maxVowelCount);
                System.out.println("Words with maximum vowels: " + maxVowelWords);
                
                // Show vowel count for each word
                System.out.println("\nVowel count for each word:");
                String[] words = sentence.split("\\s+");
                for (String word : words) {
                    String cleanWord = word.replaceAll("[^a-zA-Z]", "");
                    if (!cleanWord.isEmpty()) {
                        System.out.println("\"" + cleanWord + "\": " + countVowels(cleanWord) + " vowels");
                    }
                }
            }
            
            // Explain the algorithm
            System.out.println("\nAlgorithm explanation:");
            System.out.println("1. Split the sentence into words");
            System.out.println("2. For each word, count the number of vowels (a, e, i, o, u)");
            System.out.println("3. Track the maximum vowel count and all words with that count");
            System.out.println("4. Time Complexity: O(n) where n is the total number of characters");
            System.out.println("5. Space Complexity: O(m) where m is the number of words");
            
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}
