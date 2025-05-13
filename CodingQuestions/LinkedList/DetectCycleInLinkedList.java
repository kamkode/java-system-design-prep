package CodingQuestions.LinkedList;

/**
 * Detect Cycle in Linked List
 * 
 * Problem Statement:
 * Given a linked list, determine if the linked list has a cycle in it.
 * A cycle occurs when a node in the linked list can be reached again by continuously following the next pointer.
 * 
 * Example:
 * Input: head = [3,2,0,-4], where -4 points back to 2
 * Output: true
 * Explanation: There is a cycle in the linked list, where the tail connects to the 1st node (0-indexed).
 */
public class DetectCycleInLinkedList {
    
    /**
     * Definition for singly-linked list node.
     */
    public static class ListNode {
        int val;
        ListNode next;
        
        ListNode(int x) {
            val = x;
            next = null;
        }
    }
    
    /**
     * Approach 1: Using HashSet
     * Time Complexity: O(n)
     * Space Complexity: O(n)
     */
    public static boolean hasCycleUsingHashSet(ListNode head) {
        java.util.HashSet<ListNode> visited = new java.util.HashSet<>();
        
        ListNode current = head;
        while (current != null) {
            // If we've seen this node before, there's a cycle
            if (visited.contains(current)) {
                return true;
            }
            
            // Add the current node to the set of visited nodes
            visited.add(current);
            
            // Move to the next node
            current = current.next;
        }
        
        // If we reach the end of the list, there's no cycle
        return false;
    }
    
    /**
     * Approach 2: Floyd's Cycle-Finding Algorithm (Tortoise and Hare)
     * Time Complexity: O(n)
     * Space Complexity: O(1)
     */
    public static boolean hasCycleUsingFloyd(ListNode head) {
        if (head == null || head.next == null) {
            return false;
        }
        
        ListNode slow = head;      // Tortoise - moves one step at a time
        ListNode fast = head;      // Hare - moves two steps at a time
        
        while (fast != null && fast.next != null) {
            slow = slow.next;          // Move slow pointer one step
            fast = fast.next.next;     // Move fast pointer two steps
            
            // If the pointers meet, there's a cycle
            if (slow == fast) {
                return true;
            }
        }
        
        // If fast reaches the end of the list, there's no cycle
        return false;
    }
    
    /**
     * Approach 3: Find the start of the cycle
     * Time Complexity: O(n)
     * Space Complexity: O(1)
     * 
     * This method not only detects if there's a cycle but also returns the node where the cycle begins.
     * If there's no cycle, it returns null.
     */
    public static ListNode detectCycleStart(ListNode head) {
        if (head == null || head.next == null) {
            return null;
        }
        
        ListNode slow = head;
        ListNode fast = head;
        boolean hasCycle = false;
        
        // First, find if there's a cycle using Floyd's algorithm
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            
            if (slow == fast) {
                hasCycle = true;
                break;
            }
        }
        
        // If there's no cycle, return null
        if (!hasCycle) {
            return null;
        }
        
        // Reset one pointer to the head and keep the other at the meeting point
        // Both pointers now move at the same speed
        slow = head;
        while (slow != fast) {
            slow = slow.next;
            fast = fast.next;
        }
        
        // When they meet again, that's the start of the cycle
        return slow;
    }
    
    /**
     * Helper method to create a linked list with a cycle for testing
     */
    private static ListNode createLinkedListWithCycle(int[] values, int cyclePos) {
        if (values == null || values.length == 0) {
            return null;
        }
        
        ListNode head = new ListNode(values[0]);
        ListNode current = head;
        ListNode cycleNode = cyclePos == 0 ? head : null;
        
        for (int i = 1; i < values.length; i++) {
            current.next = new ListNode(values[i]);
            current = current.next;
            
            if (i == cyclePos) {
                cycleNode = current;
            }
        }
        
        // Create cycle if cyclePos is valid
        if (cyclePos >= 0 && cyclePos < values.length) {
            current.next = cycleNode;
        }
        
        return head;
    }
    
    /**
     * Helper method to print a linked list (up to a limit to avoid infinite loops)
     */
    private static void printLinkedList(ListNode head, int limit) {
        ListNode current = head;
        int count = 0;
        
        System.out.print("[");
        while (current != null && count < limit) {
            System.out.print(current.val);
            if (current.next != null && count < limit - 1) {
                System.out.print(", ");
            }
            current = current.next;
            count++;
        }
        
        if (count == limit) {
            System.out.print(", ...");
        }
        
        System.out.println("]");
    }
    
    /**
     * Main method to test the solutions
     */
    public static void main(String[] args) {
        // Test case 1: Linked list with a cycle
        int[] values1 = {3, 2, 0, -4};
        ListNode head1 = createLinkedListWithCycle(values1, 1); // Cycle at position 1 (points to node with value 2)
        
        System.out.println("Test Case 1:");
        System.out.println("Input: head = [3, 2, 0, -4], where -4 points back to 2");
        
        boolean result1HashSet = hasCycleUsingHashSet(head1);
        System.out.println("Output (HashSet): " + result1HashSet);
        
        boolean result1Floyd = hasCycleUsingFloyd(head1);
        System.out.println("Output (Floyd's Algorithm): " + result1Floyd);
        
        ListNode cycleStart1 = detectCycleStart(head1);
        System.out.println("Cycle starts at node with value: " + (cycleStart1 != null ? cycleStart1.val : "N/A"));
        
        // Test case 2: Linked list with a cycle at the head
        int[] values2 = {1, 2};
        ListNode head2 = createLinkedListWithCycle(values2, 0); // Cycle at position 0 (points to head)
        
        System.out.println("\nTest Case 2:");
        System.out.println("Input: head = [1, 2], where 2 points back to 1");
        
        boolean result2HashSet = hasCycleUsingHashSet(head2);
        System.out.println("Output (HashSet): " + result2HashSet);
        
        boolean result2Floyd = hasCycleUsingFloyd(head2);
        System.out.println("Output (Floyd's Algorithm): " + result2Floyd);
        
        ListNode cycleStart2 = detectCycleStart(head2);
        System.out.println("Cycle starts at node with value: " + (cycleStart2 != null ? cycleStart2.val : "N/A"));
        
        // Test case 3: Linked list without a cycle
        int[] values3 = {1, 2, 3, 4};
        ListNode head3 = createLinkedListWithCycle(values3, -1); // No cycle
        
        System.out.println("\nTest Case 3:");
        System.out.println("Input: head = [1, 2, 3, 4]");
        printLinkedList(head3, 10);
        
        boolean result3HashSet = hasCycleUsingHashSet(head3);
        System.out.println("Output (HashSet): " + result3HashSet);
        
        boolean result3Floyd = hasCycleUsingFloyd(head3);
        System.out.println("Output (Floyd's Algorithm): " + result3Floyd);
        
        ListNode cycleStart3 = detectCycleStart(head3);
        System.out.println("Cycle starts at node with value: " + (cycleStart3 != null ? cycleStart3.val : "N/A"));
    }
}