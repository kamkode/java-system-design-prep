package BlitzenxIntervierwQnA;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

/*
 * PROBLEM STATEMENT:
 * Write a Java program to demonstrate proper usage of try-catch blocks for exception handling.
 * The program should showcase various exception handling techniques and best practices.
 * 
 * Requirements:
 * - Demonstrate proper try-catch-finally structure
 * - Show try-with-resources for automatic resource management
 * - Illustrate exception propagation and re-throwing
 * - Use custom exceptions for domain-specific error conditions
 * - Demonstrate multi-catch blocks for handling different exception types
 * - Implement proper exception logging and messaging
 * - Show how to clean up resources properly in finally blocks
 * - Include examples of both checked and unchecked exceptions
 * - Demonstrate nested try-catch blocks and their proper usage
 * - Illustrate proper exception handling strategy (prevent, detect, handle)
 *
 * EXPLANATION:
 * Class: ExceptionHandlingDemo
 * Purpose: Demonstrates proper exception handling techniques in Java using file operations.
 *
 * CONCEPTS INVOLVED:
 * 1. Exception Handling:
 *    - try-catch-finally blocks
 *    - try-with-resources statement
 *    - Checked vs. unchecked exceptions
 *    - Exception hierarchy and type handling
 * 2. Resource Management:
 *    - Proper resource cleanup in finally blocks
 *    - Automatic resource management with try-with-resources
 * 3. Exception Design:
 *    - Custom exception classes
 *    - Exception wrapping and re-throwing
 *    - Informative error messages
 * 4. File Operations:
 *    - File reading with proper exception handling
 *    - Handling file not found scenarios
 * 5. Best Practices:
 *    - Handling exceptions at the appropriate level
 *    - Avoiding empty catch blocks
 *    - Providing meaningful error messages
 *    - Proper cleanup in all scenarios
 *
 * IMPLEMENTATION DETAILS:
 * - The program demonstrates file reading with proper exception handling
 * - Multiple approaches are shown for comparison
 * - Detailed explanations of each technique
 * - Comprehensive error handling for all operations
 */

// Custom exception for specific business logic validation
class InvalidFileFormatException extends Exception {
    public InvalidFileFormatException(String message) {
        super(message);
    }
    
    public InvalidFileFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}

public class ExceptionHandlingDemo {
    
    /**
     * Main method to run the demonstration
     */
    public static void main(String[] args) {
        ExceptionHandlingDemo demo = new ExceptionHandlingDemo();
        Scanner scanner = new Scanner(System.in);
        
        try {
            System.out.println("=== Exception Handling Demonstration ===");
            System.out.println("This program shows proper try-catch usage patterns.");
            
            // Get a file path from the user
            System.out.print("\nEnter a file path to read (or press Enter for a non-existent file): ");
            String filePath = scanner.nextLine().trim();
            
            if (filePath.isEmpty()) {
                filePath = "non_existent_file.txt";
                System.out.println("Using a non-existent file for demonstration: " + filePath);
            }
            
            System.out.println("\nDemonstrating different exception handling approaches:");
            
            // Demonstrate basic try-catch-finally
            System.out.println("\n1. Basic try-catch-finally pattern:");
            try {
                demo.demonstrateBasicTryCatch(filePath);
            } catch (Exception e) {
                System.out.println("   Caught at main level: " + e.getMessage());
            }
            
            // Demonstrate try-with-resources
            System.out.println("\n2. Try-with-resources pattern:");
            try {
                demo.demonstrateTryWithResources(filePath);
            } catch (Exception e) {
                System.out.println("   Caught at main level: " + e.getMessage());
            }
            
            // Demonstrate multi-catch
            System.out.println("\n3. Multi-catch pattern:");
            try {
                demo.demonstrateMultiCatch(filePath);
            } catch (Exception e) {
                System.out.println("   Caught at main level: " + e.getMessage());
            }
            
            // Demonstrate exception chaining
            System.out.println("\n4. Exception chaining and custom exceptions:");
            try {
                demo.demonstrateExceptionChaining(filePath);
            } catch (Exception e) {
                System.out.println("   Caught at main level: " + e.getMessage());
                if (e.getCause() != null) {
                    System.out.println("   Root cause: " + e.getCause().getMessage());
                }
            }
            
            // Demonstrate nested try-catch
            System.out.println("\n5. Nested try-catch pattern:");
            demo.demonstrateNestedTryCatch(filePath);
            
            // Best practices summary
            System.out.println("\n=== Exception Handling Best Practices ===");
            demo.displayBestPractices();
            
        } catch (Exception e) {
            System.out.println("Unexpected error in main method: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close();
            System.out.println("\nProgram completed.");
        }
    }
    
    /**
     * Demonstrates basic try-catch-finally structure
     * 
     * @param filePath Path to file to read
     * @throws IOException If file operations fail
     */
    public void demonstrateBasicTryCatch(String filePath) throws IOException {
        System.out.println("   Reading file: " + filePath);
        
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        
        try {
            // Open resources - may throw FileNotFoundException (a subclass of IOException)
            fileReader = new FileReader(filePath);
            bufferedReader = new BufferedReader(fileReader);
            
            // Read the first line - may throw IOException
            String firstLine = bufferedReader.readLine();
            System.out.println("   First line: " + (firstLine != null ? firstLine : "No content in file"));
            
            // Simulate potential NullPointerException
            if (firstLine == null) {
                System.out.println("   Warning: File is empty");
            } else if (firstLine.length() > 10) {
                // Deliberate attempt to process only characters that might not exist
                System.out.println("   First 10 chars: " + firstLine.substring(0, 10));
            }
            
        } catch (FileNotFoundException e) {
            // Specific exception handling for file not found
            System.out.println("   Error: File not found: " + e.getMessage());
            // Re-throw to demonstrate propagation
            throw e;
            
        } catch (IOException e) {
            // Handle other I/O exceptions
            System.out.println("   Error reading file: " + e.getMessage());
            throw e;
            
        } catch (RuntimeException e) {
            // Handle unexpected runtime exceptions
            System.out.println("   Unexpected runtime error: " + e.getMessage());
            throw e;
            
        } finally {
            // Clean up resources in the finally block - always executes
            System.out.println("   In finally block: Cleaning up resources");
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (fileReader != null) {
                    fileReader.close();
                }
            } catch (IOException e) {
                System.out.println("   Error while closing resources: " + e.getMessage());
            }
        }
    }
    
    /**
     * Demonstrates try-with-resources for automatic resource management
     * 
     * @param filePath Path to file to read
     * @throws IOException If file operations fail
     */
    public void demonstrateTryWithResources(String filePath) throws IOException {
        System.out.println("   Reading file: " + filePath);
        
        // Resources declared in the try statement are automatically closed when the try block exits
        try (FileReader fileReader = new FileReader(filePath);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            
            // Read the first line
            String firstLine = bufferedReader.readLine();
            System.out.println("   First line: " + (firstLine != null ? firstLine : "No content in file"));
            
            // Resources will be closed automatically, even if an exception occurs
            
        } catch (FileNotFoundException e) {
            System.out.println("   Error: File not found: " + e.getMessage());
            throw e;
        } catch (IOException e) {
            System.out.println("   Error reading file: " + e.getMessage());
            throw e;
        }
        
        // No finally block needed for resource cleanup - it's handled automatically
        System.out.println("   Resources automatically closed by try-with-resources");
    }
    
    /**
     * Demonstrates multi-catch blocks for handling different exceptions
     * 
     * @param filePath Path to file to read
     * @throws Exception If any operation fails
     */
    public void demonstrateMultiCatch(String filePath) throws Exception {
        System.out.println("   Reading file: " + filePath);
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            // Read first line and process it
            String line = reader.readLine();
            
            // Intentional potential exceptions for demonstration
            if (line == null) {
                throw new NullPointerException("Demonstrating null pointer exception - file was empty");
            }
            
            // Try to parse line as an integer
            int number = Integer.parseInt(line.trim());
            System.out.println("   Read number: " + number);
            
        } catch (FileNotFoundException | NullPointerException e) {
            // Multi-catch - handles multiple exception types with the same code
            System.out.println("   Error accessing file contents: " + e.getMessage());
            throw e;
            
        } catch (NumberFormatException e) {
            // Specific handler for parsing errors
            System.out.println("   Error: File does not contain a valid number: " + e.getMessage());
            throw e;
            
        } catch (IOException e) {
            // Handle other I/O exceptions
            System.out.println("   I/O Error: " + e.getMessage());
            throw e;
            
        } catch (Exception e) {
            // Catch-all for any other exceptions
            System.out.println("   Unexpected error: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Demonstrates exception chaining and custom exceptions
     * 
     * @param filePath Path to file to read
     * @throws InvalidFileFormatException If file format is invalid
     */
    public void demonstrateExceptionChaining(String filePath) throws InvalidFileFormatException {
        System.out.println("   Validating file: " + filePath);
        
        try {
            File file = new File(filePath);
            
            // Check if file exists
            if (!file.exists()) {
                throw new FileNotFoundException("File does not exist: " + filePath);
            }
            
            // Check file extension
            if (!filePath.toLowerCase().endsWith(".txt")) {
                throw new InvalidFileFormatException("File must be a .txt file");
            }
            
            // Check file size
            if (file.length() == 0) {
                throw new InvalidFileFormatException("File is empty");
            }
            
            System.out.println("   File validation successful");
            
        } catch (FileNotFoundException e) {
            // Wrap the original exception in a custom exception
            System.out.println("   Validation error (will be wrapped): " + e.getMessage());
            throw new InvalidFileFormatException("Cannot process the file: " + e.getMessage(), e);
            
        } catch (SecurityException e) {
            // Wrap security exceptions
            System.out.println("   Security error (will be wrapped): " + e.getMessage());
            throw new InvalidFileFormatException("Security error while accessing the file", e);
        }
    }
    
    /**
     * Demonstrates nested try-catch blocks
     * 
     * @param filePath Path to file to read
     */
    public void demonstrateNestedTryCatch(String filePath) {
        System.out.println("   Processing file with nested error handling: " + filePath);
        
        try {
            // Outer try block for file existence
            File file = new File(filePath);
            if (!file.exists()) {
                System.out.println("   File does not exist: " + filePath);
                return;
            }
            
            try {
                // Inner try block for file reading
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line = reader.readLine();
                    
                    try {
                        // Innermost try block for data processing
                        if (line == null) {
                            System.out.println("   File is empty");
                        } else {
                            System.out.println("   Read line: " + line);
                            
                            // Try to perform some operation on the data
                            if (line.length() > 5) {
                                System.out.println("   Line has more than 5 characters");
                            } else {
                                System.out.println("   Line has 5 or fewer characters");
                            }
                        }
                    } catch (NullPointerException e) {
                        System.out.println("   Error processing line data: " + e.getMessage());
                    }
                }
            } catch (IOException e) {
                System.out.println("   Error reading file: " + e.getMessage());
            }
            
        } catch (SecurityException e) {
            System.out.println("   Security error accessing file: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("   Unexpected error: " + e.getMessage());
        }
        
        System.out.println("   Nested try-catch demonstration completed");
    }
    
    /**
     * Displays best practices for exception handling
     */
    private void displayBestPractices() {
        System.out.println("1. Be specific with exception types - catch specific exceptions before general ones");
        System.out.println("2. Use try-with-resources for automatic resource management");
        System.out.println("3. Always clean up resources in finally blocks when not using try-with-resources");
        System.out.println("4. Provide meaningful error messages");
        System.out.println("5. Only catch exceptions you can handle properly");
        System.out.println("6. Use custom exceptions for domain-specific error conditions");
        System.out.println("7. Preserve the original exception when re-throwing (exception chaining)");
        System.out.println("8. Log exceptions appropriately - include context information");
        System.out.println("9. Don't use exceptions for normal flow control");
        System.out.println("10. Validate input parameters to prevent exceptions where possible");
    }
}
