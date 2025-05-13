package CodingQuestions.LinkedList;

/**
 * Reverse Linked List
 * 
 * Problem Statement:
 * Given the head of a singly linked list, reverse the list, and return the reversed list.
 * 
 * Example:
 * Input: head = [1,2,3,4,5]
 * Output: [5,4,3,2,1]
 */
public class ReverseLinkedList {
    
    /**
     * Definition for singly-linked list node.
     */
    public static class ListNode {
        int val;
        ListNode next;
        
        ListNode() {}
        
        ListNode(int val) {
            this.val = val;
        }
        
        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }
    
    /**
     * Approach 1: Iterative
     * Time Complexity: O(n)
     * Space Complexity: O(1)
     */
    public static ListNode reverseListIterative(ListNode head) {
        ListNode prev = null;
        ListNode current = head;
        
        while (current != null) {
            // Save the next node
            ListNode nextTemp = current.next;
            
            // Reverse the link
            current.next = prev;
            
            // Move pointers one position ahead
            prev = current;
            current = nextTemp;
        }
        
        // prev is the new head of the reversed list
        return prev;
    }
    
    /**
     * Approach 2: Recursive
     * Time Complexity: O(n)
     * Space Complexity: O(n) due to the recursion stack
     */
    public static ListNode reverseListRecursive(ListNode head) {
        // Base case: empty list or list with only one node
        if (head == null || head.next == null) {
            return head;
        }
        
        // Recursively reverse the rest of the list after head
        ListNode reversedListHead = reverseListRecursive(head.next);
        
        // Reverse the link between head and head.next
        head.next.next = head;
        head.next = null;
        
        // Return the head of the reversed list
        return reversedListHead;
    }
    
    /**
     * Approach 3: Using Stack
     * Time Complexity: O(n)
     * Space Complexity: O(n)
     */
    public static ListNode reverseListUsingStack(ListNode head) {
        // If the list is empty or has only one node, no need to reverse
        if (head == null || head.next == null) {
            return head;
        }
        
        // Create a stack to store the nodes
        java.util.Stack<ListNode> stack = new java.util.Stack<>();
        
        // Push all nodes onto the stack
        ListNode current = head;
        while (current != null) {
            stack.push(current);
            current = current.next;
        }
        
        // Pop the top node (which was the last node in the original list)
        // This will be the new head of the reversed list
        ListNode newHead = stack.pop();
        current = newHead;
        
        // Pop remaining nodes and link them
        while (!stack.isEmpty()) {
            current.next = stack.pop();
            current = current.next;
        }
        
        // Set the next of the last node (which was the first node in the original list) to null
        current.next = null;
        
        return newHead;
    }
    
    /**
     * Helper method to create a linked list from an array of values
     */
    private static ListNode createLinkedList(int[] values) {
        if (values == null || values.length == 0) {
            return null;
        }
        
        ListNode head = new ListNode(values[0]);
        ListNode current = head;
        
        for (int i = 1; i < values.length; i++) {
            current.next = new ListNode(values[i]);
            current = current.next;
        }
        
        return head;
    }
    
    /**
     * Helper method to convert a linked list to a string representation
     */
    private static String linkedListToString(ListNode head) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        
        ListNode current = head;
        while (current != null) {
            sb.append(current.val);
            if (current.next != null) {
                sb.append(", ");
            }
            current = current.next;
        }
        
        sb.append("]");
        return sb.toString();
    }
    
    /**
     * Main method to test the solutions
     */
    public static void main(String[] args) {
        // Test case 1
        int[] values1 = {1, 2, 3, 4, 5};
        
        System.out.println("Test Case 1:");
        System.out.println("Input: head = " + linkedListToString(createLinkedList(values1)));
        
        ListNode result1Iterative = reverseListIterative(createLinkedList(values1));
        System.out.println("Output (Iterative): " + linkedListToString(result1Iterative));
        
        ListNode result1Recursive = reverseListRecursive(createLinkedList(values1));
        System.out.println("Output (Recursive): " + linkedListToString(result1Recursive));
        
        ListNode result1Stack = reverseListUsingStack(createLinkedList(values1));
        System.out.println("Output (Stack): " + linkedListToString(result1Stack));
        
        // Test case 2
        int[] values2 = {1, 2};
        
        System.out.println("\nTest Case 2:");
        System.out.println("Input: head = " + linkedListToString(createLinkedList(values2)));
        
        ListNode result2Iterative = reverseListIterative(createLinkedList(values2));
        System.out.println("Output (Iterative): " + linkedListToString(result2Iterative));
        
        ListNode result2Recursive = reverseListRecursive(createLinkedList(values2));
        System.out.println("Output (Recursive): " + linkedListToString(result2Recursive));
        
        ListNode result2Stack = reverseListUsingStack(createLinkedList(values2));
        System.out.println("Output (Stack): " + linkedListToString(result2Stack));
        
        // Test case 3
        int[] values3 = {};
        
        System.out.println("\nTest Case 3:");
        System.out.println("Input: head = " + linkedListToString(createLinkedList(values3)));
        
        ListNode result3Iterative = reverseListIterative(createLinkedList(values3));
        System.out.println("Output (Iterative): " + linkedListToString(result3Iterative));
        
        ListNode result3Recursive = reverseListRecursive(createLinkedList(values3));
        System.out.println("Output (Recursive): " + linkedListToString(result3Recursive));
        
        ListNode result3Stack = reverseListUsingStack(createLinkedList(values3));
        System.out.println("Output (Stack): " + linkedListToString(result3Stack));
    }
}