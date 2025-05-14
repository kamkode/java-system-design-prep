package BlitzenxIntervierwQnA;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Question 9: Parse an XML file, transfer the data in each tag to suitable variables and print them
 * 
 * This program parses an XML file containing student information and transfers
 * the data to appropriate variables.
 */
public class XMLParser {
    
    /**
     * Creates a sample XML file for testing
     * 
     * @param filePath Path where the sample file should be created
     * @throws IOException If there's an error creating the file
     */
    public static void createSampleXMLFile(String filePath) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            writer.write("<students>\n");
            writer.write("    <student id=\"1\">\n");
            writer.write("        <name>John Doe</name>\n");
            writer.write("        <age>21</age>\n");
            writer.write("        <gpa>3.8</gpa>\n");
            writer.write("        <courses>\n");
            writer.write("            <course>Java Programming</course>\n");
            writer.write("            <course>Database Systems</course>\n");
            writer.write("            <course>Web Development</course>\n");
            writer.write("        </courses>\n");
            writer.write("    </student>\n");
            writer.write("    <student id=\"2\">\n");
            writer.write("        <name>Jane Smith</name>\n");
            writer.write("        <age>22</age>\n");
            writer.write("        <gpa>4.0</gpa>\n");
            writer.write("        <courses>\n");
            writer.write("            <course>Data Structures</course>\n");
            writer.write("            <course>Algorithms</course>\n");
            writer.write("            <course>Machine Learning</course>\n");
            writer.write("        </courses>\n");
            writer.write("    </student>\n");
            writer.write("</students>");
        }
    }
    
    /**
     * Student class to store parsed XML data
     */
    public static class Student {
        private int id;
        private String name;
        private int age;
        private double gpa;
        private String[] courses;
        
        public Student(int id, String name, int age, double gpa, String[] courses) {
            this.id = id;
            this.name = name;
            this.age = age;
            this.gpa = gpa;
            this.courses = courses;
        }
        
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Student ID: ").append(id).append("\n");
            sb.append("Name: ").append(name).append("\n");
            sb.append("Age: ").append(age).append("\n");
            sb.append("GPA: ").append(gpa).append("\n");
            sb.append("Courses: ");
            
            if (courses != null && courses.length > 0) {
                for (int i = 0; i < courses.length; i++) {
                    sb.append("\n  - ").append(courses[i]);
                }
            } else {
                sb.append("None");
            }
            
            return sb.toString();
        }
    }
    
    /**
     * Parses an XML file and extracts student information
     * 
     * @param filePath Path to the XML file
     * @return Array of Student objects
     * @throws Exception If there's an error parsing the file
     */
    public static Student[] parseXMLFile(String filePath) throws Exception {
        // Create a DocumentBuilder
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        
        // Parse the XML file
        Document document = builder.parse(new File(filePath));
        document.getDocumentElement().normalize();
        
        // Get all student elements
        NodeList studentNodes = document.getElementsByTagName("student");
        Student[] students = new Student[studentNodes.getLength()];
        
        // Process each student
        for (int i = 0; i < studentNodes.getLength(); i++) {
            Node studentNode = studentNodes.item(i);
            
            if (studentNode.getNodeType() == Node.ELEMENT_NODE) {
                Element studentElement = (Element) studentNode;
                
                // Get student ID
                int id = Integer.parseInt(studentElement.getAttribute("id"));
                
                // Get student name
                String name = getElementValue(studentElement, "name");
                
                // Get student age
                int age = Integer.parseInt(getElementValue(studentElement, "age"));
                
                // Get student GPA
                double gpa = Double.parseDouble(getElementValue(studentElement, "gpa"));
                
                // Get student courses
                NodeList courseNodes = studentElement.getElementsByTagName("course");
                String[] courses = new String[courseNodes.getLength()];
                
                for (int j = 0; j < courseNodes.getLength(); j++) {
                    courses[j] = courseNodes.item(j).getTextContent();
                }
                
                // Create Student object
                students[i] = new Student(id, name, age, gpa, courses);
            }
        }
        
        return students;
    }
    
    /**
     * Gets the text content of an element
     * 
     * @param element Parent element
     * @param tagName Tag name to find
     * @return Text content of the element
     */
    private static String getElementValue(Element element, String tagName) {
        NodeList nodeList = element.getElementsByTagName(tagName);
        if (nodeList.getLength() > 0) {
            return nodeList.item(0).getTextContent();
        }
        return "";
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("XML Parser Demonstration");
        System.out.println("-----------------------");
        
        try {
            // Get file path or create sample file
            System.out.println("\nOptions:");
            System.out.println("1. Enter path to an existing XML file");
            System.out.println("2. Create a sample XML file for testing");
            System.out.print("Enter your choice (1 or 2): ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            
            String filePath;
            if (choice == 2) {
                // Create a sample file
                filePath = System.getProperty("user.dir") + File.separator + "students.xml";
                System.out.println("\nCreating a sample XML file at: " + filePath);
                createSampleXMLFile(filePath);
                System.out.println("Sample file created successfully.");
            } else {
                // Get path from user
                System.out.print("\nEnter the path to your XML file: ");
                filePath = scanner.nextLine().trim();
                
                // Check if file exists
                if (!new File(filePath).exists()) {
                    System.out.println("\nFile not found at: " + filePath);
                    System.out.print("Would you like to create a sample file at this location? (y/n): ");
                    String createSample = scanner.nextLine().trim().toLowerCase();
                    
                    if (createSample.startsWith("y")) {
                        createSampleXMLFile(filePath);
                        System.out.println("Sample file created successfully.");
                    } else {
                        System.out.println("Operation cancelled.");
                        return;
                    }
                }
            }
            
            // Parse the XML file
            System.out.println("\nParsing XML file: " + filePath);
            Student[] students = parseXMLFile(filePath);
            
            // Display results
            System.out.println("\n=== Parsed Student Data ===");
            System.out.println("Number of students found: " + students.length);
            
            for (int i = 0; i < students.length; i++) {
                System.out.println("\nStudent " + (i + 1) + ":");
                System.out.println(students[i]);
            }
            
            // Explain the parsing process
            System.out.println("\nXML Parsing Process Explanation:");
            System.out.println("1. DocumentBuilder parses the XML file into a Document object");
            System.out.println("2. We navigate the Document to find elements by tag name");
            System.out.println("3. For each element, we extract attributes and text content");
            System.out.println("4. We convert the extracted data to appropriate types (int, double, etc.)");
            System.out.println("5. We store the data in custom objects (Student class)");
            System.out.println("\nTime Complexity: O(n) where n is the number of elements in the XML file");
            System.out.println("Space Complexity: O(m) where m is the amount of data extracted");
            
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }
}
