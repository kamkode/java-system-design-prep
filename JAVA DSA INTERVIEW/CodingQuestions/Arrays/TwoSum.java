package CodingQuestions.Arrays;

/**
 * Two Sum Problem
 * 
 * Problem Statement:
 * Given an array of integers nums and an integer target, return indices of the two numbers
 * such that they add up to target. You may assume that each input would have exactly one solution,
 * and you may not use the same element twice.
 * 
 * Example:
 * Input: nums = [2, 7, 11, 15], target = 9
 * Output: [0, 1]
 * Explanation: Because nums[0] + nums[1] == 9, we return [0, 1].
 */
public class TwoSum {

    /**
     * Brute Force Approach
     * Time Complexity: O(n^2)
     * Space Complexity: O(1)
     */
    public static int[] twoSumBruteForce(int[] nums, int target) {
        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                if (nums[i] + nums[j] == target) {
                    return new int[] {i, j};
                }
            }
        }
        // No solution found
        return new int[] {-1, -1};
    }

    /**
     * Optimized Approach using HashMap
     * Time Complexity: O(n)
     * Space Complexity: O(n)
     */
    public static int[] twoSumOptimized(int[] nums, int target) {
        // Create a HashMap to store the value and its index
        java.util.HashMap<Integer, Integer> map = new java.util.HashMap<>();

        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];

            // Check if the complement exists in the map
            if (map.containsKey(complement)) {
                return new int[] {map.get(complement), i};
            }

            // Add the current number and its index to the map
            map.put(nums[i], i);
        }

        // No solution found
        return new int[] {-1, -1};
    }

    /**
     * Main method to test the solutions
     */
    public static void main(String[] args) {
        // Test case 1
        int[] nums1 = {2, 7, 11, 15};
        int target1 = 9;

        System.out.println("Test Case 1:");
        System.out.println("Input: nums = [2, 7, 11, 15], target = 9");

        int[] result1BruteForce = twoSumBruteForce(nums1, target1);
        System.out.print("Output (Brute Force): [");
        System.out.print(result1BruteForce[0] + ", " + result1BruteForce[1]);
        System.out.println("]");

        int[] result1Optimized = twoSumOptimized(nums1, target1);
        System.out.print("Output (Optimized): [");
        System.out.print(result1Optimized[0] + ", " + result1Optimized[1]);
        System.out.println("]");

        // Test case 2
        int[] nums2 = {3, 2, 4};
        int target2 = 6;

        System.out.println("\nTest Case 2:");
        System.out.println("Input: nums = [3, 2, 4], target = 6");

        int[] result2BruteForce = twoSumBruteForce(nums2, target2);
        System.out.print("Output (Brute Force): [");
        System.out.print(result2BruteForce[0] + ", " + result2BruteForce[1]);
        System.out.println("]");

        int[] result2Optimized = twoSumOptimized(nums2, target2);
        System.out.print("Output (Optimized): [");
        System.out.print(result2Optimized[0] + ", " + result2Optimized[1]);
        System.out.println("]");

        // Test case 3
        int[] nums3 = {3, 3};
        int target3 = 6;

        System.out.println("\nTest Case 3:");
        System.out.println("Input: nums = [3, 3], target = 6");

        int[] result3BruteForce = twoSumBruteForce(nums3, target3);
        System.out.print("Output (Brute Force): [");
        System.out.print(result3BruteForce[0] + ", " + result3BruteForce[1]);
        System.out.println("]");

        int[] result3Optimized = twoSumOptimized(nums3, target3);
        System.out.print("Output (Optimized): [");
        System.out.print(result3Optimized[0] + ", " + result3Optimized[1]);
        System.out.println("]");
    }
}
