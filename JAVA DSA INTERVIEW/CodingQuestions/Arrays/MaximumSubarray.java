package CodingQuestions.Arrays;

/**
 * Maximum Subarray Problem (Kadane's Algorithm)
 * 
 * Problem Statement:
 * Given an integer array nums, find the contiguous subarray (containing at least one number)
 * which has the largest sum and return its sum.
 * 
 * Example:
 * Input: nums = [-2, 1, -3, 4, -1, 2, 1, -5, 4]
 * Output: 6
 * Explanation: The contiguous subarray [4, -1, 2, 1] has the largest sum = 6.
 */
public class MaximumSubarray {
    
    /**
     * Brute Force Approach
     * Time Complexity: O(n^2)
     * Space Complexity: O(1)
     */
    public static int maxSubArrayBruteForce(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        
        int maxSum = Integer.MIN_VALUE;
        
        for (int i = 0; i < nums.length; i++) {
            int currentSum = 0;
            for (int j = i; j < nums.length; j++) {
                currentSum += nums[j];
                maxSum = Math.max(maxSum, currentSum);
            }
        }
        
        return maxSum;
    }
    
    /**
     * Kadane's Algorithm (Dynamic Programming)
     * Time Complexity: O(n)
     * Space Complexity: O(1)
     */
    public static int maxSubArrayKadane(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        
        int maxSoFar = nums[0];
        int maxEndingHere = nums[0];
        
        for (int i = 1; i < nums.length; i++) {
            // Either extend the previous subarray or start a new one
            maxEndingHere = Math.max(nums[i], maxEndingHere + nums[i]);
            
            // Update the maximum sum found so far
            maxSoFar = Math.max(maxSoFar, maxEndingHere);
        }
        
        return maxSoFar;
    }
    
    /**
     * Divide and Conquer Approach
     * Time Complexity: O(n log n)
     * Space Complexity: O(log n) due to recursion stack
     */
    public static int maxSubArrayDivideAndConquer(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        
        return maxSubArrayHelper(nums, 0, nums.length - 1);
    }
    
    private static int maxSubArrayHelper(int[] nums, int left, int right) {
        // Base case: only one element
        if (left == right) {
            return nums[left];
        }
        
        // Find the middle point
        int mid = left + (right - left) / 2;
        
        // Find maximum subarray sum in the left half
        int leftMax = maxSubArrayHelper(nums, left, mid);
        
        // Find maximum subarray sum in the right half
        int rightMax = maxSubArrayHelper(nums, mid + 1, right);
        
        // Find maximum subarray sum that crosses the middle
        int crossMax = maxCrossingSum(nums, left, mid, right);
        
        // Return the maximum of the three
        return Math.max(Math.max(leftMax, rightMax), crossMax);
    }
    
    private static int maxCrossingSum(int[] nums, int left, int mid, int right) {
        // Find maximum sum starting from mid point and going left
        int sum = 0;
        int leftSum = Integer.MIN_VALUE;
        
        for (int i = mid; i >= left; i--) {
            sum += nums[i];
            leftSum = Math.max(leftSum, sum);
        }
        
        // Find maximum sum starting from mid + 1 and going right
        sum = 0;
        int rightSum = Integer.MIN_VALUE;
        
        for (int i = mid + 1; i <= right; i++) {
            sum += nums[i];
            rightSum = Math.max(rightSum, sum);
        }
        
        // Return the sum of the two halves
        return leftSum + rightSum;
    }
    
    /**
     * Main method to test the solutions
     */
    public static void main(String[] args) {
        // Test case 1
        int[] nums1 = {-2, 1, -3, 4, -1, 2, 1, -5, 4};
        
        System.out.println("Test Case 1:");
        System.out.println("Input: nums = [-2, 1, -3, 4, -1, 2, 1, -5, 4]");
        
        int result1BruteForce = maxSubArrayBruteForce(nums1);
        System.out.println("Output (Brute Force): " + result1BruteForce);
        
        int result1Kadane = maxSubArrayKadane(nums1);
        System.out.println("Output (Kadane's Algorithm): " + result1Kadane);
        
        int result1DivideAndConquer = maxSubArrayDivideAndConquer(nums1);
        System.out.println("Output (Divide and Conquer): " + result1DivideAndConquer);
        
        // Test case 2
        int[] nums2 = {1};
        
        System.out.println("\nTest Case 2:");
        System.out.println("Input: nums = [1]");
        
        int result2BruteForce = maxSubArrayBruteForce(nums2);
        System.out.println("Output (Brute Force): " + result2BruteForce);
        
        int result2Kadane = maxSubArrayKadane(nums2);
        System.out.println("Output (Kadane's Algorithm): " + result2Kadane);
        
        int result2DivideAndConquer = maxSubArrayDivideAndConquer(nums2);
        System.out.println("Output (Divide and Conquer): " + result2DivideAndConquer);
        
        // Test case 3
        int[] nums3 = {5, 4, -1, 7, 8};
        
        System.out.println("\nTest Case 3:");
        System.out.println("Input: nums = [5, 4, -1, 7, 8]");
        
        int result3BruteForce = maxSubArrayBruteForce(nums3);
        System.out.println("Output (Brute Force): " + result3BruteForce);
        
        int result3Kadane = maxSubArrayKadane(nums3);
        System.out.println("Output (Kadane's Algorithm): " + result3Kadane);
        
        int result3DivideAndConquer = maxSubArrayDivideAndConquer(nums3);
        System.out.println("Output (Divide and Conquer): " + result3DivideAndConquer);
    }
}