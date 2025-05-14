package BlitzenxIntervierwQnA;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

/**
 * Question 10: Illustrate proper usage of try..catch block in the solution for one of the above
 * 
 * This program demonstrates proper exception handling techniques in Java,
 * including try-catch-finally, try-with-resources, multi-catch, and exception chaining.
 */
public class ExceptionHandlingDemo {
    
    /**
     * Custom exception for domain-specific errors
     */
    public static class DataProcessingException extends Exception {
        public DataProcessingException(String message) {
            super(message);
        }
        
        public DataProcessingException(String message, Throwable cause) {
            super(message, cause);
        }
    }
    
    /**
     * Demonstrates basic try-catch-finally pattern
     */
    public static void demonstrateBasicTryCatch(String filePath) throws IOException {
        BufferedReader reader = null;
        
        try {
            System.out.println("   Attempting to read file: " + filePath);
            reader = new BufferedReader(new FileReader(filePath));
            String firstLine = reader.readLine();
            System.out.println("   First line: " + firstLine);
        } catch (FileNotFoundException e) {
            System.out.println("   Error: File not found - " + e.getMessage());
            throw e; // Re-throwing the exception
        } catch (IOException e) {
            System.out.println("   Error reading file: " + e.getMessage());
            throw e; // Re-throwing the exception
        } finally {
            System.out.println("   Finally block: Cleaning up resources");
            if (reader != null) {
                try {
                    reader.close();
                    System.out.println("   Reader closed successfully");
                } catch (IOException e) {
                    System.out.println("   Error closing reader: " + e.getMessage());
                }
            }
        }
    }
    
    /**
     * Demonstrates try-with-resources pattern (Java 7+)
     */
    public static void demonstrateTryWithResources(String filePath) throws IOException {
        System.out.println("   Attempting to read file using try-with-resources: " + filePath);
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String firstLine = reader.readLine();
            System.out.println("   First line: " + firstLine);
        } catch (FileNotFoundException e) {
            System.out.println("   Error: File not found - " + e.getMessage());
            throw e;
        } catch (IOException e) {
            System.out.println("   Error reading file: " + e.getMessage());
            throw e;
        }
        // No finally needed - resources are automatically closed
    }
    
    /**
     * Demonstrates multi-catch pattern (Java 7+)
     */
    public static void demonstrateMultiCatch(String filePath) throws IOException {
        System.out.println("   Attempting to read file using multi-catch: " + filePath);
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String firstLine = reader.readLine();
            int number = Integer.parseInt(firstLine); // Potential NumberFormatException
            System.out.println("   First line as number: " + number);
        } catch (FileNotFoundException e) {
            System.out.println("   Error: File not found - " + e.getMessage());
            throw e;
        } catch (NumberFormatException | IOException e) {
            // Multi-catch for different exception types
            System.out.println("   Error: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            throw new IOException("Error processing file", e); // Exception chaining
        }
    }
    
    /**
     * Demonstrates exception chaining and custom exceptions
     */
    public static void demonstrateExceptionChaining(String filePath) throws DataProcessingException {
        System.out.println("   Attempting to process file with custom exceptions: " + filePath);
        
        try {
            // Try to read and process the file
            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String firstLine = reader.readLine();
                if (firstLine == null || firstLine.trim().isEmpty()) {
                    throw new DataProcessingException("File is empty");
                }
                
                try {
                    double value = Double.parseDouble(firstLine);
                    System.out.println("   Processed value: " + value);
                } catch (NumberFormatException e) {
                    throw new DataProcessingException("First line is not a valid number", e);
                }
            }
        } catch (IOException e) {
            // Wrap the original exception in a custom exception
            throw new DataProcessingException("Error accessing file: " + filePath, e);
        }
    }
    
    /**
     * Displays best practices for exception handling
     */
    public static void displayBestPractices() {
        System.out.println("1. Use specific exception types instead of catching generic Exception");
        System.out.println("2. Use try-with-resources for automatic resource management");
        System.out.println("3. Clean up resources properly in finally blocks when needed");
        System.out.println("4. Create custom exceptions for domain-specific error conditions");
        System.out.println("5. Use exception chaining to preserve the original cause");
        System.out.println("6. Avoid empty catch blocks - always handle or log exceptions");
        System.out.println("7. Document exceptions in method signatures with throws clause");
        System.out.println("8. Use multi-catch for concise exception handling");
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Exception Handling Demonstration");
        System.out.println("-------------------------------");
        
        try {
            // Get file path from user
            System.out.print("\nEnter a file path to test exception handling: ");
            String filePath = scanner.nextLine().trim();
            
            System.out.println("\nDemonstrating different exception handling approaches:");
            
            // Demonstrate basic try-catch-finally
            System.out.println("\n1. Basic try-catch-finally pattern:");
            try {
                demonstrateBasicTryCatch(filePath);
            } catch (Exception e) {
                System.out.println("   Caught at main level: " + e.getMessage());
            }
            
            // Demonstrate try-with-resources
            System.out.println("\n2. Try-with-resources pattern:");
            try {
                demonstrateTryWithResources(filePath);
            } catch (Exception e) {
                System.out.println("   Caught at main level: " + e.getMessage());
            }
            
            // Demonstrate multi-catch
            System.out.println("\n3. Multi-catch pattern:");
            try {
                demonstrateMultiCatch(filePath);
            } catch (Exception e) {
                System.out.println("   Caught at main level: " + e.getMessage());
            }
            
            // Demonstrate exception chaining
            System.out.println("\n4. Exception chaining and custom exceptions:");
            try {
                demonstrateExceptionChaining(filePath);
            } catch (Exception e) {
                System.out.println("   Caught at main level: " + e.getMessage());
                if (e.getCause() != null) {
                    System.out.println("   Root cause: " + e.getCause().getMessage());
                }
            }
            
            // Best practices summary
            System.out.println("\n=== Exception Handling Best Practices ===");
            displayBestPractices();
            
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}
