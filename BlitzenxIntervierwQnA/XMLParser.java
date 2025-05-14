package BlitzenxIntervierwQnA;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/*
 * PROBLEM STATEMENT:
 * Write a Java program to parse an XML file, extract data from each tag into suitable variables,
 * and print the extracted information. The program should demonstrate both DOM and SAX parsing approaches.
 * 
 * Requirements:
 * - Parse an XML file using both DOM (Document Object Model) and SAX (Simple API for XML) approaches.
 * - Extract data from XML tags into appropriate Java variables.
 * - Handle both attributes and element content.
 * - Demonstrate traversing the XML hierarchy.
 * - Handle edge cases:
 *   - Missing or malformed XML files.
 *   - Empty elements or attributes.
 *   - Invalid XML structure.
 * - Allow the user to choose between DOM and SAX parsing.
 * - Include the ability to create a sample XML file for testing purposes.
 * - Output the extracted data in a well-formatted manner.
 * - Include all details (problem statement, explanation, solution) in a single .java file.
 *
 * EXPLANATION:
 * Class: XMLParser
 * Purpose: Demonstrates parsing XML files using both DOM and SAX approaches, extracting data into variables.
 *
 * CONCEPTS INVOLVED:
 * 1. XML Parsing:
 *    - DOM: Loads the entire XML document into memory as a tree structure.
 *    - SAX: Event-based parser that processes XML sequentially without loading the entire document.
 * 2. Java XML APIs:
 *    - javax.xml.parsers: Factory classes for creating parsers.
 *    - org.w3c.dom: Classes for DOM parsing.
 *    - org.xml.sax: Classes for SAX parsing.
 * 3. File I/O:
 *    - Reading XML files.
 *    - Creating sample XML files for testing.
 * 4. Object Modeling:
 *    - Mapping XML elements to Java objects.
 *    - Creating appropriate data structures to hold parsed data.
 * 5. Error Handling:
 *    - Handling XML parsing exceptions.
 *    - Validating XML structure.
 * 6. Traversal Algorithms:
 *    - Recursive traversal of DOM tree.
 *    - Event-based traversal with SAX.
 * 7. User Interaction:
 *    - Taking file path input from the user.
 *    - Providing options for parsing methods.
 *
 * COMPARISON OF APPROACHES:
 * 1. DOM Parsing:
 *    - Advantages:
 *      - Loads the entire document, allowing random access to elements.
 *      - Enables navigation in any direction within the document.
 *      - Supports modification of the document.
 *    - Disadvantages:
 *      - Memory intensive for large XML files.
 *      - Slower initial parsing as the entire document must be loaded.
 *    - Use Cases:
 *      - Small to medium-sized XML files.
 *      - When navigation or modification of the document is required.
 *
 * 2. SAX Parsing:
 *    - Advantages:
 *      - Memory efficient as it doesn't load the entire document.
 *      - Faster for large XML files.
 *      - Good for streaming or sequential processing.
 *    - Disadvantages:
 *      - One-way, forward-only parsing.
 *      - No random access to elements.
 *      - Cannot modify the document.
 *    - Use Cases:
 *      - Large XML files.
 *      - When only specific elements need to be extracted.
 *      - Memory-constrained environments.
 *
 * IMPLEMENTATION DETAILS:
 * - For DOM Parsing:
 *   - Use DocumentBuilderFactory and DocumentBuilder to parse the XML.
 *   - Navigate through nodes using getChildNodes() and related methods.
 *   - Extract data from elements and attributes.
 *
 * - For SAX Parsing:
 *   - Use SAXParserFactory and SAXParser to create a parser.
 *   - Implement a custom DefaultHandler to handle parsing events.
 *   - Use startElement(), endElement(), and characters() methods to extract data.
 *
 * - Sample XML Creation:
 *   - Create a function to generate a sample XML file for testing.
 *   - Include various element types and attributes for comprehensive testing.
 *
 * - Data Model:
 *   - Create classes to represent the entities in the XML.
 *   - Include appropriate fields to store element content and attributes.
 */

// Exception class for XML parsing errors
class XMLParsingException extends Exception {
    public XMLParsingException(String message) {
        super(message);
    }
    
    public XMLParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}

// Class to represent a book entity from the XML
class Book {
    private String id;
    private String title;
    private String author;
    private int year;
    private double price;
    private String category;
    
    public Book(String id, String title, String author, int year, double price, String category) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.year = year;
        this.price = price;
        this.category = category;
    }
    
    // Empty constructor for SAX parsing
    public Book() {
        this.id = "";
        this.title = "";
        this.author = "";
        this.year = 0;
        this.price = 0.0;
        this.category = "";
    }
    
    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    
    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }
    
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    @Override
    public String toString() {
        return String.format("Book[id=%s, title=%s, author=%s, year=%d, price=%.2f, category=%s]",
                            id, title, author, year, price, category);
    }
    
    // Formats the book info for display
    public String formattedInfo() {
        return String.format("ID: %s%nTitle: %s%nAuthor: %s%nYear: %d%nPrice: $%.2f%nCategory: %s",
                            id, title, author, year, price, category);
    }
}

// SAX handler for book XML parsing
class BookHandler extends DefaultHandler {
    private List<Book> books;
    private Book currentBook;
    private StringBuilder currentValue;
    private boolean inBook = false;
    
    public BookHandler() {
        books = new ArrayList<>();
        currentValue = new StringBuilder();
    }
    
    public List<Book> getBooks() {
        return books;
    }
    
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        currentValue.setLength(0);
        
        if (qName.equalsIgnoreCase("book")) {
            currentBook = new Book();
            inBook = true;
            
            // Get book ID from attribute
            String id = attributes.getValue("id");
            if (id != null) {
                currentBook.setId(id);
            }
        }
    }
    
    @Override
    public void endElement(String uri, String localName, String qName) {
        if (inBook) {
            if (qName.equalsIgnoreCase("title")) {
                currentBook.setTitle(currentValue.toString());
            } else if (qName.equalsIgnoreCase("author")) {
                currentBook.setAuthor(currentValue.toString());
            } else if (qName.equalsIgnoreCase("year")) {
                try {
                    currentBook.setYear(Integer.parseInt(currentValue.toString()));
                } catch (NumberFormatException e) {
                    System.out.println("Warning: Invalid year format: " + currentValue);
                }
            } else if (qName.equalsIgnoreCase("price")) {
                try {
                    currentBook.setPrice(Double.parseDouble(currentValue.toString()));
                } catch (NumberFormatException e) {
                    System.out.println("Warning: Invalid price format: " + currentValue);
                }
            } else if (qName.equalsIgnoreCase("category")) {
                currentBook.setCategory(currentValue.toString());
            } else if (qName.equalsIgnoreCase("book")) {
                books.add(currentBook);
                inBook = false;
            }
        }
    }
    
    @Override
    public void characters(char[] ch, int start, int length) {
        currentValue.append(ch, start, length);
    }
}

public class XMLParser {
    
    /**
     * Parses an XML file using the DOM approach.
     * 
     * @param filePath Path to the XML file
     * @return List of Book objects parsed from the XML
     * @throws XMLParsingException If there's an error parsing the XML
     */
    public List<Book> parseXmlWithDOM(String filePath) throws XMLParsingException {
        List<Book> books = new ArrayList<>();
        
        try {
            File xmlFile = new File(filePath);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);
            
            // Normalize the XML structure
            doc.getDocumentElement().normalize();
            
            // Get all book elements
            NodeList bookNodes = doc.getElementsByTagName("book");
            
            // Process each book node
            for (int i = 0; i < bookNodes.getLength(); i++) {
                Node bookNode = bookNodes.item(i);
                
                if (bookNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element bookElement = (Element) bookNode;
                    
                    // Extract book information
                    String id = bookElement.getAttribute("id");
                    String title = getElementText(bookElement, "title");
                    String author = getElementText(bookElement, "author");
                    int year = parseIntSafely(getElementText(bookElement, "year"), 0);
                    double price = parseDoubleSafely(getElementText(bookElement, "price"), 0.0);
                    String category = getElementText(bookElement, "category");
                    
                    // Create Book object and add to list
                    Book book = new Book(id, title, author, year, price, category);
                    books.add(book);
                }
            }
            
        } catch (ParserConfigurationException e) {
            throw new XMLParsingException("Parser configuration error: " + e.getMessage(), e);
        } catch (SAXException e) {
            throw new XMLParsingException("XML parsing error: " + e.getMessage(), e);
        } catch (IOException e) {
            throw new XMLParsingException("I/O error while parsing XML: " + e.getMessage(), e);
        }
        
        return books;
    }
    
    /**
     * Parses an XML file using the SAX approach.
     * 
     * @param filePath Path to the XML file
     * @return List of Book objects parsed from the XML
     * @throws XMLParsingException If there's an error parsing the XML
     */
    public List<Book> parseXmlWithSAX(String filePath) throws XMLParsingException {
        try {
            // Create a SAX parser factory
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            
            // Create a handler for the SAX parser
            BookHandler handler = new BookHandler();
            
            // Parse the XML file using the handler
            saxParser.parse(new File(filePath), handler);
            
            // Return the list of books from the handler
            return handler.getBooks();
            
        } catch (ParserConfigurationException e) {
            throw new XMLParsingException("SAX parser configuration error: " + e.getMessage(), e);
        } catch (SAXException e) {
            throw new XMLParsingException("SAX parsing error: " + e.getMessage(), e);
        } catch (IOException e) {
            throw new XMLParsingException("I/O error while parsing XML with SAX: " + e.getMessage(), e);
        }
    }
    
    /**
     * Creates a sample XML file for testing purposes.
     * 
     * @param filePath Path where the sample file should be created
     * @throws IOException If there's an error creating the file
     */
    public void createSampleXmlFile(String filePath) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            writer.write("<bookstore>\n");
            
            // Book 1
            writer.write("  <book id=\"B001\">\n");
            writer.write("    <title>Java Programming Masterclass</title>\n");
            writer.write("    <author>John Smith</author>\n");
            writer.write("    <year>2021</year>\n");
            writer.write("    <price>49.99</price>\n");
            writer.write("    <category>Programming</category>\n");
            writer.write("  </book>\n");
            
            // Book 2
            writer.write("  <book id=\"B002\">\n");
            writer.write("    <title>Data Structures and Algorithms</title>\n");
            writer.write("    <author>Jane Doe</author>\n");
            writer.write("    <year>2019</year>\n");
            writer.write("    <price>39.95</price>\n");
            writer.write("    <category>Computer Science</category>\n");
            writer.write("  </book>\n");
            
            // Book 3
            writer.write("  <book id=\"B003\">\n");
            writer.write("    <title>Design Patterns</title>\n");
            writer.write("    <author>Robert Martin</author>\n");
            writer.write("    <year>2018</year>\n");
            writer.write("    <price>45.50</price>\n");
            writer.write("    <category>Software Engineering</category>\n");
            writer.write("  </book>\n");
            
            // Book 4 (with empty elements)
            writer.write("  <book id=\"B004\">\n");
            writer.write("    <title>XML Processing with Java</title>\n");
            writer.write("    <author></author>\n");
            writer.write("    <year>invalid-year</year>\n");
            writer.write("    <price>29.99</price>\n");
            writer.write("    <category></category>\n");
            writer.write("  </book>\n");
            
            writer.write("</bookstore>");
        }
    }
    
    /**
     * Extracts text content from an element.
     * 
     * @param element The parent element
     * @param tagName The name of the child tag
     * @return The text content of the tag, or empty string if not found
     */
    private String getElementText(Element element, String tagName) {
        NodeList nodeList = element.getElementsByTagName(tagName);
        if (nodeList.getLength() > 0) {
            Node node = nodeList.item(0);
            if (node.hasChildNodes() && node.getFirstChild().getNodeType() == Node.TEXT_NODE) {
                return node.getFirstChild().getNodeValue().trim();
            }
        }
        return "";
    }
    
    /**
     * Safely parses a string to an integer, returning a default value if parsing fails.
     * 
     * @param value The string to parse
     * @param defaultValue The default value to return if parsing fails
     * @return The parsed integer or the default value
     */
    private int parseIntSafely(String value, int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
    
    /**
     * Safely parses a string to a double, returning a default value if parsing fails.
     * 
     * @param value The string to parse
     * @param defaultValue The default value to return if parsing fails
     * @return The parsed double or the default value
     */
    private double parseDoubleSafely(String value, double defaultValue) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
    
    /**
     * Formats and displays a list of books.
     * 
     * @param books The list of books to display
     * @param title A title for the display
     */
    public void displayBooks(List<Book> books, String title) {
        System.out.println("\n=== " + title + " ===");
        System.out.println("Found " + books.size() + " book(s)");
        
        for (int i = 0; i < books.size(); i++) {
            Book book = books.get(i);
            System.out.println("\nBook #" + (i + 1) + ":");
            System.out.println(book.formattedInfo());
        }
    }
    
    /**
     * Suggests a default file path in the current directory.
     * 
     * @return A suggested file path for the XML file
     */
    public String suggestDefaultFilePath() {
        return System.getProperty("user.dir") + File.separator + "books.xml";
    }
    
    /**
     * Demonstrates the differences between DOM and SAX parsing.
     */
    public void explainParsingDifferences() {
        System.out.println("\n=== XML Parsing Approaches ===");
        System.out.println("DOM (Document Object Model):");
        System.out.println("- Loads the entire XML document into memory as a tree structure");
        System.out.println("- Allows random access and navigation in any direction");
        System.out.println("- More memory intensive but easier to work with");
        System.out.println("- Best for smaller XML files or when document modification is needed");
        
        System.out.println("\nSAX (Simple API for XML):");
        System.out.println("- Event-based, sequential parsing without loading the entire document");
        System.out.println("- More memory efficient for large XML files");
        System.out.println("- One-way, forward-only parsing");
        System.out.println("- Best for large XML files or when only specific data is needed");
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        XMLParser parser = new XMLParser();
        
        try {
            System.out.println("=== XML Parser ===");
            System.out.println("This program demonstrates parsing XML files to extract data into variables.");
            
            // Get file path from user or create a sample file
            String filePath;
            System.out.println("\nOptions:");
            System.out.println("1. Enter path to an existing XML file");
            System.out.println("2. Create a sample XML file for testing");
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
                filePath = parser.suggestDefaultFilePath();
                System.out.println("\nCreating a sample XML file at: " + filePath);
                
                try {
                    parser.createSampleXmlFile(filePath);
                    System.out.println("Sample file created successfully.");
                } catch (IOException e) {
                    System.out.println("Error creating sample file: " + e.getMessage());
                    System.out.print("\nPlease enter an alternative file path: ");
                    filePath = scanner.nextLine().trim();
                    parser.createSampleXmlFile(filePath);
                    System.out.println("Sample file created successfully at the alternative location.");
                }
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
                        parser.createSampleXmlFile(filePath);
                        System.out.println("Sample file created successfully.");
                    } else {
                        System.out.println("Operation cancelled. Please run the program again with a valid file path.");
                        return;
                    }
                }
            }
            
            // Choose parsing method
            parser.explainParsingDifferences();
            System.out.println("\nChoose a parsing method:");
            System.out.println("1. DOM Parsing (loads entire document)");
            System.out.println("2. SAX Parsing (event-based)");
            System.out.println("3. Both (compare the approaches)");
            System.out.print("Enter your choice (1-3): ");
            
            int parsingChoice = 3; // Default to both
            if (scanner.hasNextInt()) {
                parsingChoice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
            } else {
                scanner.nextLine(); // Consume invalid input
            }
            
            // Parse the XML file based on the chosen method
            if (parsingChoice == 1 || parsingChoice == 3) {
                try {
                    List<Book> domBooks = parser.parseXmlWithDOM(filePath);
                    parser.displayBooks(domBooks, "Books Parsed with DOM");
                } catch (XMLParsingException e) {
                    System.out.println("Error parsing with DOM: " + e.getMessage());
                }
            }
            
            if (parsingChoice == 2 || parsingChoice == 3) {
                try {
                    List<Book> saxBooks = parser.parseXmlWithSAX(filePath);
                    parser.displayBooks(saxBooks, "Books Parsed with SAX");
                } catch (XMLParsingException e) {
                    System.out.println("Error parsing with SAX: " + e.getMessage());
                }
            }
            
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}
