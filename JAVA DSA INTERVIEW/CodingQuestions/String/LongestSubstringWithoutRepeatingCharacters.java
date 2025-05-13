package CodingQuestions.String;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Longest Substring Without Repeating Characters
 * 
 * Problem Statement:
 * Given a string s, find the length of the longest substring without repeating characters.
 * 
 * Example:
 * Input: s = "abcabcbb"
 * Output: 3
 * Explanation: The answer is "abc", with the length of 3.
 */
public class LongestSubstringWithoutRepeatingCharacters {
    
    /**
     * Approach 1: Brute Force
     * Time Complexity: O(n^3)
     * Space Complexity: O(min(m, n)) where m is the size of the character set
     */
    public static int lengthOfLongestSubstringBruteForce(String s) {
        int n = s.length();
        int maxLength = 0;
        
        // Check all possible substrings
        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {
                // Check if the substring s[i...j] has all unique characters
                if (allUnique(s, i, j)) {
                    maxLength = Math.max(maxLength, j - i + 1);
                }
            }
        }
        
        return maxLength;
    }
    
    /**
     * Helper method to check if a substring has all unique characters
     */
    private static boolean allUnique(String s, int start, int end) {
        HashSet<Character> set = new HashSet<>();
        
        for (int i = start; i <= end; i++) {
            char ch = s.charAt(i);
            if (set.contains(ch)) {
                return false;
            }
            set.add(ch);
        }
        
        return true;
    }
    
    /**
     * Approach 2: Sliding Window with HashSet
     * Time Complexity: O(2n) = O(n) in the worst case each character will be visited twice
     * Space Complexity: O(min(m, n)) where m is the size of the character set
     */
    public static int lengthOfLongestSubstringSliding(String s) {
        int n = s.length();
        HashSet<Character> set = new HashSet<>();
        int maxLength = 0;
        int left = 0;
        int right = 0;
        
        while (left < n && right < n) {
            // Try to extend the window [left, right]
            if (!set.contains(s.charAt(right))) {
                set.add(s.charAt(right));
                right++;
                maxLength = Math.max(maxLength, right - left);
            } else {
                // Found a duplicate, remove the leftmost character and move left pointer
                set.remove(s.charAt(left));
                left++;
            }
        }
        
        return maxLength;
    }
    
    /**
     * Approach 3: Sliding Window with HashMap (Optimized)
     * Time Complexity: O(n)
     * Space Complexity: O(min(m, n)) where m is the size of the character set
     */
    public static int lengthOfLongestSubstringOptimized(String s) {
        int n = s.length();
        HashMap<Character, Integer> map = new HashMap<>(); // current index of character
        int maxLength = 0;
        
        // Try to extend the range [i, j]
        for (int j = 0, i = 0; j < n; j++) {
            char currentChar = s.charAt(j);
            
            // If the current character is already in the map, update i
            if (map.containsKey(currentChar)) {
                // Move i to the position after the last occurrence of the current character
                // Math.max is used to ensure i only moves forward
                i = Math.max(map.get(currentChar) + 1, i);
            }
            
            // Update the maximum length
            maxLength = Math.max(maxLength, j - i + 1);
            
            // Update the index of the current character
            map.put(currentChar, j);
        }
        
        return maxLength;
    }
    
    /**
     * Approach 4: Using ASCII Array (Optimized for ASCII characters)
     * Time Complexity: O(n)
     * Space Complexity: O(m) where m is the size of the character set (128 for ASCII)
     */
    public static int lengthOfLongestSubstringASCII(String s) {
        int n = s.length();
        int maxLength = 0;
        
        // ASCII table has 128 characters
        int[] index = new int[128];
        
        // Initialize all values in the index array to -1
        for (int i = 0; i < 128; i++) {
            index[i] = -1;
        }
        
        // Try to extend the range [i, j]
        for (int j = 0, i = 0; j < n; j++) {
            char currentChar = s.charAt(j);
            
            // If the current character is already in the window, update i
            if (index[currentChar] != -1) {
                // Move i to the position after the last occurrence of the current character
                // Math.max is used to ensure i only moves forward
                i = Math.max(index[currentChar] + 1, i);
            }
            
            // Update the maximum length
            maxLength = Math.max(maxLength, j - i + 1);
            
            // Update the index of the current character
            index[currentChar] = j;
        }
        
        return maxLength;
    }
    
    /**
     * Main method to test the solutions
     */
    public static void main(String[] args) {
        // Test case 1
        String s1 = "abcabcbb";
        
        System.out.println("Test Case 1:");
        System.out.println("Input: s = \"" + s1 + "\"");
        
        int result1BruteForce = lengthOfLongestSubstringBruteForce(s1);
        System.out.println("Output (Brute Force): " + result1BruteForce);
        
        int result1Sliding = lengthOfLongestSubstringSliding(s1);
        System.out.println("Output (Sliding Window): " + result1Sliding);
        
        int result1Optimized = lengthOfLongestSubstringOptimized(s1);
        System.out.println("Output (Optimized): " + result1Optimized);
        
        int result1ASCII = lengthOfLongestSubstringASCII(s1);
        System.out.println("Output (ASCII): " + result1ASCII);
        
        // Test case 2
        String s2 = "bbbbb";
        
        System.out.println("\nTest Case 2:");
        System.out.println("Input: s = \"" + s2 + "\"");
        
        int result2BruteForce = lengthOfLongestSubstringBruteForce(s2);
        System.out.println("Output (Brute Force): " + result2BruteForce);
        
        int result2Sliding = lengthOfLongestSubstringSliding(s2);
        System.out.println("Output (Sliding Window): " + result2Sliding);
        
        int result2Optimized = lengthOfLongestSubstringOptimized(s2);
        System.out.println("Output (Optimized): " + result2Optimized);
        
        int result2ASCII = lengthOfLongestSubstringASCII(s2);
        System.out.println("Output (ASCII): " + result2ASCII);
        
        // Test case 3
        String s3 = "pwwkew";
        
        System.out.println("\nTest Case 3:");
        System.out.println("Input: s = \"" + s3 + "\"");
        
        int result3BruteForce = lengthOfLongestSubstringBruteForce(s3);
        System.out.println("Output (Brute Force): " + result3BruteForce);
        
        int result3Sliding = lengthOfLongestSubstringSliding(s3);
        System.out.println("Output (Sliding Window): " + result3Sliding);
        
        int result3Optimized = lengthOfLongestSubstringOptimized(s3);
        System.out.println("Output (Optimized): " + result3Optimized);
        
        int result3ASCII = lengthOfLongestSubstringASCII(s3);
        System.out.println("Output (ASCII): " + result3ASCII);
        
        // Test case 4
        String s4 = "";
        
        System.out.println("\nTest Case 4:");
        System.out.println("Input: s = \"" + s4 + "\"");
        
        int result4BruteForce = lengthOfLongestSubstringBruteForce(s4);
        System.out.println("Output (Brute Force): " + result4BruteForce);
        
        int result4Sliding = lengthOfLongestSubstringSliding(s4);
        System.out.println("Output (Sliding Window): " + result4Sliding);
        
        int result4Optimized = lengthOfLongestSubstringOptimized(s4);
        System.out.println("Output (Optimized): " + result4Optimized);
        
        int result4ASCII = lengthOfLongestSubstringASCII(s4);
        System.out.println("Output (ASCII): " + result4ASCII);
    }
}