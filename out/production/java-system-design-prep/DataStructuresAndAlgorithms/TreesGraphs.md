# Trees and Graphs

Trees and graphs are fundamental non-linear data structures used to represent hierarchical relationships and networks. This document covers key concepts, operations, and common interview questions related to trees and graphs.

## Table of Contents
1. [Trees](#trees)
   - [Basic Concepts](#basic-tree-concepts)
   - [Types of Trees](#types-of-trees)
   - [Tree Traversals](#tree-traversals)
   - [Common Tree Operations](#common-tree-operations)
   - [Tree Interview Questions](#tree-interview-questions)
2. [Graphs](#graphs)
   - [Basic Concepts](#basic-graph-concepts)
   - [Types of Graphs](#types-of-graphs)
   - [Graph Representations](#graph-representations)
   - [Graph Traversals](#graph-traversals)
   - [Graph Algorithms](#graph-algorithms)
   - [Graph Interview Questions](#graph-interview-questions)
3. [Time and Space Complexity](#time-and-space-complexity)
4. [Implementation in Java](#implementation-in-java)
5. [Tips and Tricks](#tips-and-tricks)

## Trees

### Basic Tree Concepts

A tree is a hierarchical data structure consisting of nodes connected by edges. Each tree has a root node, and every node (except the root) has exactly one parent node. A node can have zero or more child nodes.

**Key Terms:**
- **Root**: The topmost node of a tree
- **Parent**: A node that has one or more child nodes
- **Child**: A node that has a parent node
- **Leaf**: A node that has no children
- **Sibling**: Nodes that share the same parent
- **Ancestor**: A node reachable by repeated proceeding from child to parent
- **Descendant**: A node reachable by repeated proceeding from parent to child
- **Height**: The length of the longest path from the root to a leaf
- **Depth**: The length of the path from the root to the node
- **Level**: The depth of a node plus 1
- **Subtree**: A tree consisting of a node and all its descendants

### Types of Trees

#### Binary Tree
A tree in which each node has at most two children, referred to as the left child and the right child.

#### Binary Search Tree (BST)
A binary tree where for each node:
- All nodes in the left subtree have values less than the node's value
- All nodes in the right subtree have values greater than the node's value

This property makes searching, insertion, and deletion operations efficient.

#### Balanced Trees
Trees designed to maintain balance to ensure O(log n) operations:
- **AVL Tree**: Self-balancing BST where the heights of the two child subtrees differ by at most one
- **Red-Black Tree**: Self-balancing BST with additional properties to ensure balance
- **B-Tree**: Self-balancing tree with multiple keys per node, used in databases and file systems

#### Heap
A complete binary tree where each node satisfies the heap property:
- **Max Heap**: Parent nodes have values greater than or equal to their children
- **Min Heap**: Parent nodes have values less than or equal to their children

#### Trie (Prefix Tree)
A tree-like data structure used to store a dynamic set of strings, where keys are usually strings.

### Tree Traversals

#### Depth-First Traversals
1. **Preorder (Root-Left-Right)**
   ```java
   void preorder(TreeNode node) {
       if (node == null) return;
       
       // Process the current node
       System.out.print(node.val + " ");
       
       // Traverse left subtree
       preorder(node.left);
       
       // Traverse right subtree
       preorder(node.right);
   }
   ```

2. **Inorder (Left-Root-Right)**
   ```java
   void inorder(TreeNode node) {
       if (node == null) return;
       
       // Traverse left subtree
       inorder(node.left);
       
       // Process the current node
       System.out.print(node.val + " ");
       
       // Traverse right subtree
       inorder(node.right);
   }
   ```

3. **Postorder (Left-Right-Root)**
   ```java
   void postorder(TreeNode node) {
       if (node == null) return;
       
       // Traverse left subtree
       postorder(node.left);
       
       // Traverse right subtree
       postorder(node.right);
       
       // Process the current node
       System.out.print(node.val + " ");
   }
   ```

#### Breadth-First Traversal (Level Order)
```java
void levelOrder(TreeNode root) {
    if (root == null) return;
    
    Queue<TreeNode> queue = new LinkedList<>();
    queue.add(root);
    
    while (!queue.isEmpty()) {
        TreeNode node = queue.poll();
        
        // Process the current node
        System.out.print(node.val + " ");
        
        // Add children to the queue
        if (node.left != null) queue.add(node.left);
        if (node.right != null) queue.add(node.right);
    }
}
```

### Common Tree Operations

#### Insertion in a BST
```java
TreeNode insert(TreeNode root, int key) {
    // If the tree is empty, return a new node
    if (root == null) return new TreeNode(key);
    
    // Otherwise, recur down the tree
    if (key < root.val)
        root.left = insert(root.left, key);
    else if (key > root.val)
        root.right = insert(root.right, key);
    
    // Return the unchanged node pointer
    return root;
}
```

#### Deletion in a BST
```java
TreeNode delete(TreeNode root, int key) {
    // Base case
    if (root == null) return root;
    
    // Recursive calls for ancestors of node to be deleted
    if (key < root.val)
        root.left = delete(root.left, key);
    else if (key > root.val)
        root.right = delete(root.right, key);
    else {
        // Node with only one child or no child
        if (root.left == null)
            return root.right;
        else if (root.right == null)
            return root.left;
        
        // Node with two children: Get the inorder successor
        root.val = minValue(root.right);
        
        // Delete the inorder successor
        root.right = delete(root.right, root.val);
    }
    
    return root;
}

int minValue(TreeNode root) {
    int minv = root.val;
    while (root.left != null) {
        minv = root.left.val;
        root = root.left;
    }
    return minv;
}
```

#### Searching in a BST
```java
TreeNode search(TreeNode root, int key) {
    // Base Cases: root is null or key is present at root
    if (root == null || root.val == key)
        return root;
    
    // Key is greater than root's key
    if (root.val < key)
        return search(root.right, key);
    
    // Key is smaller than root's key
    return search(root.left, key);
}
```

### Tree Interview Questions

1. **Maximum Depth of Binary Tree**: Find the maximum depth (height) of a binary tree
2. **Validate Binary Search Tree**: Determine if a binary tree is a valid BST
3. **Symmetric Tree**: Check if a binary tree is a mirror of itself
4. **Binary Tree Level Order Traversal**: Return the level order traversal of a binary tree
5. **Lowest Common Ancestor**: Find the lowest common ancestor of two nodes in a binary tree
6. **Serialize and Deserialize Binary Tree**: Design an algorithm to serialize and deserialize a binary tree
7. **Path Sum**: Determine if the tree has a root-to-leaf path that sums to a given value
8. **Construct Binary Tree**: Construct a binary tree from preorder and inorder traversal
9. **Binary Tree Maximum Path Sum**: Find the maximum path sum in a binary tree
10. **Kth Smallest Element in a BST**: Find the kth smallest element in a BST

## Graphs

### Basic Graph Concepts

A graph is a non-linear data structure consisting of nodes (vertices) and edges that connect these nodes. Graphs are used to represent networks, relationships, and many real-world problems.

**Key Terms:**
- **Vertex (Node)**: A fundamental unit in a graph
- **Edge**: A connection between two vertices
- **Adjacent Vertices**: Vertices connected by an edge
- **Path**: A sequence of vertices where each adjacent pair is connected by an edge
- **Cycle**: A path that starts and ends at the same vertex
- **Connected Graph**: A graph where there is a path between every pair of vertices
- **Degree**: The number of edges connected to a vertex
- **Weight**: A value assigned to an edge

### Types of Graphs

#### Directed vs. Undirected
- **Directed Graph (Digraph)**: Edges have a direction (one-way)
- **Undirected Graph**: Edges have no direction (two-way)

#### Weighted vs. Unweighted
- **Weighted Graph**: Edges have weights or costs
- **Unweighted Graph**: Edges have no weights

#### Cyclic vs. Acyclic
- **Cyclic Graph**: Contains at least one cycle
- **Acyclic Graph**: Contains no cycles

#### Special Types
- **Tree**: A connected acyclic undirected graph
- **Directed Acyclic Graph (DAG)**: A directed graph with no cycles
- **Complete Graph**: Every vertex is connected to every other vertex
- **Bipartite Graph**: Vertices can be divided into two disjoint sets such that no two vertices within the same set are adjacent

### Graph Representations

#### Adjacency Matrix
A 2D array where matrix[i][j] represents an edge from vertex i to vertex j.

```java
class Graph {
    private int V; // Number of vertices
    private boolean[][] adjMatrix;
    
    public Graph(int v) {
        V = v;
        adjMatrix = new boolean[v][v];
    }
    
    public void addEdge(int i, int j) {
        adjMatrix[i][j] = true;
        adjMatrix[j][i] = true; // For undirected graph
    }
    
    public void removeEdge(int i, int j) {
        adjMatrix[i][j] = false;
        adjMatrix[j][i] = false; // For undirected graph
    }
    
    public boolean isEdge(int i, int j) {
        return adjMatrix[i][j];
    }
}
```

#### Adjacency List
An array of lists where each list contains the neighbors of a vertex.

```java
class Graph {
    private int V; // Number of vertices
    private List<List<Integer>> adjList;
    
    public Graph(int v) {
        V = v;
        adjList = new ArrayList<>(v);
        for (int i = 0; i < v; i++) {
            adjList.add(new ArrayList<>());
        }
    }
    
    public void addEdge(int u, int v) {
        adjList.get(u).add(v);
        adjList.get(v).add(u); // For undirected graph
    }
    
    public List<Integer> getNeighbors(int v) {
        return adjList.get(v);
    }
}
```

### Graph Traversals

#### Depth-First Search (DFS)
```java
void dfs(int start, boolean[] visited) {
    // Mark the current node as visited
    visited[start] = true;
    System.out.print(start + " ");
    
    // Recur for all adjacent vertices
    for (int neighbor : adjList.get(start)) {
        if (!visited[neighbor]) {
            dfs(neighbor, visited);
        }
    }
}
```

#### Breadth-First Search (BFS)
```java
void bfs(int start) {
    // Mark all vertices as not visited
    boolean[] visited = new boolean[V];
    
    // Create a queue for BFS
    Queue<Integer> queue = new LinkedList<>();
    
    // Mark the current node as visited and enqueue it
    visited[start] = true;
    queue.add(start);
    
    while (!queue.isEmpty()) {
        // Dequeue a vertex from queue and print it
        int vertex = queue.poll();
        System.out.print(vertex + " ");
        
        // Get all adjacent vertices of the dequeued vertex
        // If an adjacent has not been visited, mark it visited and enqueue it
        for (int neighbor : adjList.get(vertex)) {
            if (!visited[neighbor]) {
                visited[neighbor] = true;
                queue.add(neighbor);
            }
        }
    }
}
```

### Graph Algorithms

#### Shortest Path Algorithms

1. **Dijkstra's Algorithm**: Finds the shortest path from a source vertex to all other vertices in a weighted graph with non-negative weights.

2. **Bellman-Ford Algorithm**: Finds the shortest path from a source vertex to all other vertices in a weighted graph, even with negative weights (but no negative cycles).

3. **Floyd-Warshall Algorithm**: Finds the shortest paths between all pairs of vertices in a weighted graph.

#### Minimum Spanning Tree Algorithms

1. **Prim's Algorithm**: Finds a minimum spanning tree for a weighted undirected graph.

2. **Kruskal's Algorithm**: Finds a minimum spanning tree for a weighted undirected graph.

#### Topological Sorting
An ordering of vertices in a directed acyclic graph (DAG) such that for every directed edge (u, v), vertex u comes before v in the ordering.

#### Strongly Connected Components
A strongly connected component (SCC) is a subgraph where every vertex is reachable from every other vertex.

### Graph Interview Questions

1. **Number of Islands**: Count the number of islands in a 2D grid
2. **Course Schedule**: Determine if it's possible to finish all courses given prerequisites
3. **Clone Graph**: Deep copy a graph
4. **Word Ladder**: Find the shortest transformation sequence from start word to end word
5. **Network Delay Time**: Find the time it takes for all nodes to receive a signal
6. **Alien Dictionary**: Determine the order of characters in an alien language
7. **Reconstruct Itinerary**: Reconstruct the itinerary from a list of airline tickets
8. **Evaluate Division**: Evaluate equations and queries of division
9. **Redundant Connection**: Find the redundant connection in a graph
10. **Critical Connections**: Find all critical connections in a network

## Time and Space Complexity

### Tree Operations

| Operation | Binary Tree | Binary Search Tree (Average) | Binary Search Tree (Worst) | Balanced BST |
|-----------|------------|------------------------------|----------------------------|--------------|
| Search    | O(n)       | O(log n)                     | O(n)                       | O(log n)     |
| Insertion | O(1)*      | O(log n)                     | O(n)                       | O(log n)     |
| Deletion  | O(n)       | O(log n)                     | O(n)                       | O(log n)     |
| Traversal | O(n)       | O(n)                         | O(n)                       | O(n)         |

*Assuming you have a reference to the insertion point

### Graph Operations

| Operation | Adjacency Matrix | Adjacency List |
|-----------|------------------|---------------|
| Add Vertex | O(V²)           | O(1)          |
| Add Edge   | O(1)            | O(1)          |
| Remove Vertex | O(V²)        | O(V + E)      |
| Remove Edge | O(1)           | O(E)          |
| Query      | O(1)            | O(V)          |
| Storage    | O(V²)           | O(V + E)      |
| DFS        | O(V²)           | O(V + E)      |
| BFS        | O(V²)           | O(V + E)      |

## Implementation in Java

### Binary Tree Node
```java
class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;
    
    TreeNode() {}
    
    TreeNode(int val) {
        this.val = val;
    }
    
    TreeNode(int val, TreeNode left, TreeNode right) {
        this.val = val;
        this.left = left;
        this.right = right;
    }
}
```

### Graph Node
```java
class Node {
    public int val;
    public List<Node> neighbors;
    
    public Node() {
        val = 0;
        neighbors = new ArrayList<>();
    }
    
    public Node(int _val) {
        val = _val;
        neighbors = new ArrayList<>();
    }
    
    public Node(int _val, ArrayList<Node> _neighbors) {
        val = _val;
        neighbors = _neighbors;
    }
}
```

## Tips and Tricks

### Tree Tips

1. **Use Recursion**: Many tree problems can be solved elegantly using recursion.
   ```java
   // Example: Check if a binary tree is balanced
   boolean isBalanced(TreeNode root) {
       if (root == null) return true;
       
       int leftHeight = height(root.left);
       int rightHeight = height(root.right);
       
       return Math.abs(leftHeight - rightHeight) <= 1 && 
              isBalanced(root.left) && 
              isBalanced(root.right);
   }
   
   int height(TreeNode node) {
       if (node == null) return 0;
       return 1 + Math.max(height(node.left), height(node.right));
   }
   ```

2. **Iterative Traversals**: Use a stack for DFS and a queue for BFS.
   ```java
   // Iterative inorder traversal
   void inorderIterative(TreeNode root) {
       Stack<TreeNode> stack = new Stack<>();
       TreeNode current = root;
       
       while (current != null || !stack.isEmpty()) {
           // Reach the leftmost node
           while (current != null) {
               stack.push(current);
               current = current.left;
           }
           
           // Current is now null, pop from stack
           current = stack.pop();
           
           // Process the node
           System.out.print(current.val + " ");
           
           // Move to the right subtree
           current = current.right;
       }
   }
   ```

3. **Level Order Traversal with Level Information**:
   ```java
   List<List<Integer>> levelOrder(TreeNode root) {
       List<List<Integer>> result = new ArrayList<>();
       if (root == null) return result;
       
       Queue<TreeNode> queue = new LinkedList<>();
       queue.add(root);
       
       while (!queue.isEmpty()) {
           int levelSize = queue.size();
           List<Integer> currentLevel = new ArrayList<>();
           
           for (int i = 0; i < levelSize; i++) {
               TreeNode node = queue.poll();
               currentLevel.add(node.val);
               
               if (node.left != null) queue.add(node.left);
               if (node.right != null) queue.add(node.right);
           }
           
           result.add(currentLevel);
       }
       
       return result;
   }
   ```

### Graph Tips

1. **Visited Set**: Always use a visited set/array to avoid cycles.
   ```java
   void dfs(int vertex, boolean[] visited) {
       visited[vertex] = true;
       
       for (int neighbor : adjList.get(vertex)) {
           if (!visited[neighbor]) {
               dfs(neighbor, visited);
           }
       }
   }
   ```

2. **Topological Sort**:
   ```java
   void topologicalSort() {
       Stack<Integer> stack = new Stack<>();
       boolean[] visited = new boolean[V];
       
       // Call the recursive helper function for all vertices
       for (int i = 0; i < V; i++) {
           if (!visited[i]) {
               topologicalSortUtil(i, visited, stack);
           }
       }
       
       // Print contents of stack
       while (!stack.isEmpty()) {
           System.out.print(stack.pop() + " ");
       }
   }
   
   void topologicalSortUtil(int v, boolean[] visited, Stack<Integer> stack) {
       visited[v] = true;
       
       // Recur for all adjacent vertices
       for (int neighbor : adjList.get(v)) {
           if (!visited[neighbor]) {
               topologicalSortUtil(neighbor, visited, stack);
           }
       }
       
       // Push current vertex to stack after all its adjacent vertices are processed
       stack.push(v);
   }
   ```

3. **Dijkstra's Algorithm**:
   ```java
   void dijkstra(int src) {
       int[] dist = new int[V]; // Distance array
       Arrays.fill(dist, Integer.MAX_VALUE);
       dist[src] = 0;
       
       PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[1] - b[1]);
       pq.add(new int[]{src, 0});
       
       while (!pq.isEmpty()) {
           int[] current = pq.poll();
           int u = current[0];
           int distance = current[1];
           
           // If the distance is greater than the current known distance, skip
           if (distance > dist[u]) continue;
           
           for (int[] edge : adjList.get(u)) {
               int v = edge[0];
               int weight = edge[1];
               
               if (dist[u] + weight < dist[v]) {
                   dist[v] = dist[u] + weight;
                   pq.add(new int[]{v, dist[v]});
               }
           }
       }
       
       // Print the distances
       for (int i = 0; i < V; i++) {
           System.out.println("Distance from " + src + " to " + i + " is " + dist[i]);
       }
   }
   ```

## Practice Resources

- [LeetCode Tree](https://leetcode.com/tag/tree/)
- [LeetCode Graph](https://leetcode.com/tag/graph/)
- [HackerRank Trees](https://www.hackerrank.com/domains/data-structures?filters%5Bsubdomains%5D%5B%5D=trees)
- [HackerRank Graphs](https://www.hackerrank.com/domains/algorithms?filters%5Bsubdomains%5D%5B%5D=graph-theory)