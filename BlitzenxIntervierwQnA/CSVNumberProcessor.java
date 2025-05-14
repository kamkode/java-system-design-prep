package BlitzenxIntervierwQnA;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/*
 * PROBLEM STATEMENT:
 * Write a Java program to read a CSV file containing numbers, parse the data into 
 * a list, and calculate the sum of all numbers in the file.
 * 
 * Requirements:
 * - Read data from a CSV file where each line may contain one or more comma-separated numbers.
 * - Parse the string data into numeric types (doubles to handle both integers and decimal numbers).
 * - Store the parsed numbers in a list data structure.
 * - Calculate the sum of all numbers in the list.
 * - Handle edge cases:
 *   - Empty file.
 *   - File with non-numeric entries.
 *   - File not found or permission issues.
 * - Allow the user to provide the file path or create a sample file if needed.
 * - Output the list of numbers read and their sum.
 * - Include all details (problem statement, explanation, solution) in a single .java file.
 *
 * EXPLANATION:
 * Class: CSVNumberProcessor
 * Purpose: Reads numbers from a CSV file, parses them into a list, and calculates their sum.
 *
 * CONCEPTS INVOLVED:
 * 1. File I/O:
 *    - Reading from a CSV file using BufferedReader or Files class.
 *    - Writing to a file (to create a sample CSV file).
 *    - Path handling for file operations.
 * 2. String Manipulation:
 *    - Splitting comma-separated values using String.split().
 *    - Trimming whitespace from each value.
 * 3. Parsing:
 *    - Converting string representations of numbers to numeric types.
 *    - Handling potential format exceptions.
 * 4. Collections:
 *    - Using List (ArrayList) to store the parsed numbers.
 *    - Iterating through the list to calculate the sum.
 * 5. Error Handling:
 *    - Handling IOException for file operations.
 *    - Handling NumberFormatException for parsing errors.
 *    - Providing clear error messages to the user.
 * 6. User Interaction:
 *    - Taking file path input from the user.
 *    - Allowing the creation of a sample file for testing.
 * 7. Efficiency:
 *    - Using BufferedReader for efficient file reading.
 *    - Single-pass summation while adding to the list.
 *
 * ALGORITHM:
 * 1. File Reading:
 *    - Create a File object from the provided path.
 *    - Check if the file exists; if not, prompt the user to create a sample file.
 *    - Open a BufferedReader to read the file line by line.
 *
 * 2. Parsing and Processing:
 *    - For each line in the file:
 *      - Split the line by commas to get individual values.
 *      - For each value:
 *        - Trim whitespace.
 *        - Parse the string to a double.
 *        - Add the parsed number to the list.
 *    - Handle any parsing errors by skipping invalid entries or notifying the user.
 *
 * 3. Calculation:
 *    - Iterate through the list of numbers.
 *    - Calculate the sum by adding each number to a running total.
 *
 * 4. Output:
 *    - Display the list of numbers read from the file.
 *    - Display the calculated sum.
 *    - Provide information about any entries that couldn't be parsed.
 *
 * WHY THIS APPROACH:
 * - Comprehensive: Handles all required file operations, parsing, and error cases.
 * - Robust: Includes proper exception handling and validation.
 * - User-Friendly: Provides clear prompts and explanations, with an option to create a sample file.
 * - Efficient: Uses BufferedReader for memory-efficient file reading.
 * - Modular: Separates concerns into distinct methods for better code organization.
 *
 * IMPLEMENTATION DETAILS:
 * - Creates a List<Double> to store parsed numbers from the CSV file.
 * - Uses BufferedReader wrapped around FileReader for efficient file reading.
 * - Implements proper resource management with try-with-resources.
 * - Provides helper methods for creating a sample CSV file if needed.
 * - Includes detailed error handling and user feedback.
 */

class FileProcessingException extends Exception {
    public FileProcessingException(String message) {
        super(message);
    }
    
    public FileProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}

public class CSVNumberProcessor {

    /**
     * Reads a CSV file containing numbers and parses them into a list.
     * 
     * @param filePath Path to the CSV file
     * @return ProcessingResult containing the list of numbers and any parsing issues
     * @throws FileProcessingException If there's an error reading or processing the file
     */
    public ProcessingResult readNumbersFromCSV(String filePath) throws FileProcessingException {
        List<Double> numbers = new ArrayList<>();
        List<String> parsingIssues = new ArrayList<>();
        int lineCount = 0;
        double sum = 0;
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lineCount++;
                if (line.trim().isEmpty()) {
                    parsingIssues.add("Line " + lineCount + ": Empty line (skipped)");
                    continue;
                }
                
                String[] values = line.split(",");
                for (String value : values) {
                    String trimmedValue = value.trim();
                    try {
                        double number = Double.parseDouble(trimmedValue);
                        numbers.add(number);
                        sum += number;
                    } catch (NumberFormatException e) {
                        parsingIssues.add("Line " + lineCount + ": '" + trimmedValue + 
                                         "' is not a valid number (skipped)");
                    }
                }
            }
            
            if (numbers.isEmpty() && !parsingIssues.isEmpty()) {
                throw new FileProcessingException("No valid numbers found in the file. Check parsing issues.");
            }
            
        } catch (IOException e) {
            throw new FileProcessingException("Error reading the file: " + e.getMessage(), e);
        }
        
        return new ProcessingResult(numbers, parsingIssues, sum);
    }
    
    /**
     * Creates a sample CSV file with numbers for testing purposes.
     * 
     * @param filePath Path where the sample file should be created
     * @throws IOException If there's an error creating the file
     */
    public void createSampleCSVFile(String filePath) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            // Write a variety of numbers to test parsing
            writer.write("10, 20, 30\n");
            writer.write("15.5, 25.75, 35.25\n");
            writer.write("-5, -10.5\n");
            writer.write("1000\n");
            writer.write("abc, 42, xyz, 99.9\n");  // Include some invalid entries
            writer.write("\n");  // Empty line
            writer.write("3.14159, 2.71828");
        }
    }
    
    /**
     * Suggests a default file path in the current directory.
     * 
     * @return A suggested file path for the CSV file
     */
    public String suggestDefaultFilePath() {
        return System.getProperty("user.dir") + File.separator + "numbers.csv";
    }
    
    /**
     * Formats the list of numbers for display, limiting to a specified number of items.
     * 
     * @param numbers The list of numbers to format
     * @param maxItemsToShow Maximum number of items to include in the output
     * @return A formatted string representation of the list
     */
    public String formatNumberList(List<Double> numbers, int maxItemsToShow) {
        if (numbers.isEmpty()) {
            return "[]";
        }
        
        StringBuilder sb = new StringBuilder("[");
        int itemsToShow = Math.min(numbers.size(), maxItemsToShow);
        
        for (int i = 0; i < itemsToShow; i++) {
            sb.append(formatNumber(numbers.get(i)));
            if (i < itemsToShow - 1) {
                sb.append(", ");
            }
        }
        
        if (numbers.size() > maxItemsToShow) {
            sb.append(", ... (").append(numbers.size() - maxItemsToShow).append(" more)");
        }
        
        sb.append("]");
        return sb.toString();
    }
    
    /**
     * Formats a number to remove unnecessary decimal places.
     * 
     * @param number The number to format
     * @return A formatted string representation of the number
     */
    private String formatNumber(double number) {
        // If it's an integer value, display without decimal point
        if (number == (long) number) {
            return String.format("%d", (long) number);
        } else {
            return String.format("%.2f", number);
        }
    }
    
    /**
     * Result class to hold the processing results and any issues.
     */
    public static class ProcessingResult {
        private final List<Double> numbers;
        private final List<String> parsingIssues;
        private final double sum;
        
        public ProcessingResult(List<Double> numbers, List<String> parsingIssues, double sum) {
            this.numbers = numbers;
            this.parsingIssues = parsingIssues;
            this.sum = sum;
        }
        
        public List<Double> getNumbers() {
            return numbers;
        }
        
        public List<String> getParsingIssues() {
            return parsingIssues;
        }
        
        public double getSum() {
            return sum;
        }
        
        public int getNumberCount() {
            return numbers.size();
        }
        
        public boolean hasParsingIssues() {
            return !parsingIssues.isEmpty();
        }
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CSVNumberProcessor processor = new CSVNumberProcessor();
        
        try {
            System.out.println("=== CSV Number Processor ===");
            System.out.println("This program reads numbers from a CSV file, parses them into a list, and calculates their sum.");
            
            // Get file path from user or create a sample file
            String filePath;
            System.out.println("\nOptions:");
            System.out.println("1. Enter path to an existing CSV file");
            System.out.println("2. Create a sample CSV file for testing");
            System.out.print("Enter your choice (1 or 2): ");
            
            int choice = 1;
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
            } else {
                scanner.nextLine(); // Consume invalid input
            }
            
            if (choice == 2) {
                // Create a sample file
                filePath = processor.suggestDefaultFilePath();
                System.out.println("\nCreating a sample CSV file at: " + filePath);
                
                try {
                    processor.createSampleCSVFile(filePath);
                    System.out.println("Sample file created successfully.");
                } catch (IOException e) {
                    System.out.println("Error creating sample file: " + e.getMessage());
                    System.out.print("\nPlease enter an alternative file path: ");
                    filePath = scanner.nextLine().trim();
                    processor.createSampleCSVFile(filePath);
                    System.out.println("Sample file created successfully at the alternative location.");
                }
            } else {
                // Get path from user
                System.out.print("\nEnter the path to your CSV file: ");
                filePath = scanner.nextLine().trim();
                
                // Check if file exists
                if (!Files.exists(Paths.get(filePath))) {
                    System.out.println("\nFile not found at: " + filePath);
                    System.out.print("Would you like to create a sample file at this location? (y/n): ");
                    String createSample = scanner.nextLine().trim().toLowerCase();
                    
                    if (createSample.startsWith("y")) {
                        processor.createSampleCSVFile(filePath);
                        System.out.println("Sample file created successfully.");
                    } else {
                        System.out.println("Operation cancelled. Please run the program again with a valid file path.");
                        return;
                    }
                }
            }
            
            // Process the file
            System.out.println("\nProcessing file: " + filePath);
            ProcessingResult result = processor.readNumbersFromCSV(filePath);
            
            // Display results
            System.out.println("\n=== Results ===");
            System.out.println("Numbers found: " + result.getNumberCount());
            
            // Show the list with a reasonable limit to avoid overwhelming output
            System.out.println("Numbers list: " + processor.formatNumberList(result.getNumbers(), 20));
            System.out.println("Sum of all numbers: " + processor.formatNumber(result.getSum()));
            
            // Display any parsing issues
            if (result.hasParsingIssues()) {
                System.out.println("\n=== Parsing Issues ===");
                System.out.println("The following issues were encountered:");
                for (String issue : result.getParsingIssues()) {
                    System.out.println("- " + issue);
                }
            }
            
            // Show file details
            System.out.println("\n=== File Details ===");
            Path path = Paths.get(filePath);
            System.out.println("File size: " + Files.size(path) + " bytes");
            System.out.println("Last modified: " + Files.getLastModifiedTime(path));
            
        } catch (FileProcessingException e) {
            System.out.println("Error processing the file: " + e.getMessage());
            if (e.getCause() != null) {
                System.out.println("Cause: " + e.getCause().getMessage());
            }
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}
