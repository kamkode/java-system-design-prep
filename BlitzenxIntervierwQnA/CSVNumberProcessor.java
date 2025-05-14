package BlitzenxIntervierwQnA;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Question 8: Read a CSV file containing numbers, parse it and transfer the data to a list and calculate their sum
 * 
 * This program reads numbers from a CSV file, parses them into a list, and calculates their sum.
 */
public class CSVNumberProcessor {
    
    /**
     * Reads numbers from a CSV file and calculates their sum
     * 
     * @param filePath Path to the CSV file
     * @return ProcessingResult containing the list of numbers and any parsing issues
     * @throws IOException If there's an error reading the file
     */
    public static ProcessingResult readNumbersFromCSV(String filePath) throws IOException {
        List<Double> numbers = new ArrayList<>();
        List<String> parsingIssues = new ArrayList<>();
        double sum = 0;
        int lineCount = 0;
        
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
        }
        
        return new ProcessingResult(numbers, parsingIssues, sum);
    }
    
    /**
     * Creates a sample CSV file with numbers for testing
     * 
     * @param filePath Path where the sample file should be created
     * @throws IOException If there's an error creating the file
     */
    public static void createSampleCSVFile(String filePath) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
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
     * Result class to hold the processing results and any issues
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
        
        System.out.println("CSV Number Processor");
        System.out.println("-------------------");
        
        try {
            // Get file path or create sample file
            System.out.println("\nOptions:");
            System.out.println("1. Enter path to an existing CSV file");
            System.out.println("2. Create a sample CSV file for testing");
            System.out.print("Enter your choice (1 or 2): ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            
            String filePath;
            if (choice == 2) {
                // Create a sample file
                filePath = System.getProperty("user.dir") + File.separator + "numbers.csv";
                System.out.println("\nCreating a sample CSV file at: " + filePath);
                createSampleCSVFile(filePath);
                System.out.println("Sample file created successfully.");
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
                        createSampleCSVFile(filePath);
                        System.out.println("Sample file created successfully.");
                    } else {
                        System.out.println("Operation cancelled.");
                        return;
                    }
                }
            }
            
            // Process the file
            System.out.println("\nProcessing file: " + filePath);
            ProcessingResult result = readNumbersFromCSV(filePath);
            
            // Display results
            System.out.println("\n=== Results ===");
            System.out.println("Numbers found: " + result.getNumberCount());
            System.out.println("Numbers list: " + result.getNumbers());
            System.out.println("Sum of all numbers: " + result.getSum());
            
            // Display any parsing issues
            if (result.hasParsingIssues()) {
                System.out.println("\n=== Parsing Issues ===");
                for (String issue : result.getParsingIssues()) {
                    System.out.println("- " + issue);
                }
            }
            
            // Explain the algorithm
            System.out.println("\nAlgorithm explanation:");
            System.out.println("1. Open the CSV file using BufferedReader for efficient reading");
            System.out.println("2. Read the file line by line");
            System.out.println("3. Split each line by commas to get individual values");
            System.out.println("4. Parse each value to a double and add to the list");
            System.out.println("5. Calculate the sum while adding to the list");
            System.out.println("6. Handle parsing errors by tracking issues and skipping invalid entries");
            System.out.println("7. Time Complexity: O(n) where n is the total number of values in the file");
            System.out.println("8. Space Complexity: O(n) for storing all numbers and parsing issues");
            
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}
