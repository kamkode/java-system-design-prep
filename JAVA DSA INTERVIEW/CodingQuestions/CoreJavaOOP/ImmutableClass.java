/**
 * Core Java & OOP - Question 8: Immutable class in Java
 */
package CodingQuestions.CoreJavaOOP;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImmutableClass {

    public static void main(String[] args) {
        // Create a mutable list and map to pass to our immutable object
        List<String> hobbies = new ArrayList<>();
        hobbies.add("Reading");
        hobbies.add("Hiking");
        
        Map<String, String> contacts = new HashMap<>();
        contacts.put("email", "john.doe@example.com");
        contacts.put("phone", "123-456-7890");
        
        // Create an immutable person
        Date birthDate = new Date();
        ImmutablePerson person = new ImmutablePerson(
                "John", "Doe", birthDate, hobbies, contacts);
        
        System.out.println("Original Person: " + person);
        
        // Try to modify the original collections
        System.out.println("\nTrying to modify original collections...");
        hobbies.add("Swimming");
        contacts.put("address", "123 Main St");
        
        // Modify the date
        System.out.println("\nTrying to modify the birth date...");
        birthDate.setTime(0); // Set to epoch time
        
        // Check if our immutable object was affected
        System.out.println("\nPerson after external modifications: " + person);
        
        // Try to modify the collections returned by getters
        System.out.println("\nTrying to modify collections returned by getters...");
        try {
            person.getHobbies().add("Cooking");
        } catch (UnsupportedOperationException e) {
            System.out.println("Cannot modify hobbies: " + e.getMessage());
        }
        
        try {
            person.getContacts().put("twitter", "@johndoe");
        } catch (UnsupportedOperationException e) {
            System.out.println("Cannot modify contacts: " + e.getMessage());
        }
        
        // Try to modify the date returned by getter
        System.out.println("\nTrying to modify date returned by getter...");
        Date returnedDate = person.getBirthDate();
        returnedDate.setTime(0);
        
        // Check if our immutable object was affected by modifying the returned date
        System.out.println("\nPerson after trying to modify returned date: " + person);
        
        // Create a new person with updated hobbies (the immutable way)
        System.out.println("\nCreating a new person with updated hobbies (the immutable way)...");
        List<String> newHobbies = new ArrayList<>(person.getHobbies());
        newHobbies.add("Cooking");
        
        ImmutablePerson updatedPerson = new ImmutablePerson(
                person.getFirstName(), 
                person.getLastName(), 
                person.getBirthDate(), 
                newHobbies, 
                person.getContacts());
        
        System.out.println("Updated Person: " + updatedPerson);
    }
}

/**
 * Problem Statement:
 * Create an immutable class in Java. An immutable class is one whose state cannot be changed
 * after it is created. Demonstrate how to handle mutable objects within an immutable class.
 * 
 * Solution: Implementing an immutable class
 * 
 * Rules for creating immutable classes:
 * 1. Make the class final so it can't be extended
 * 2. Make all fields private and final
 * 3. Don't provide setter methods
 * 4. If the class has mutable object references, don't allow them to be modified or accessed directly
 *    - Make defensive copies in the constructor
 *    - Make defensive copies in the getters
 * 5. If the class has a mutable object reference that can be changed by another class,
 *    ensure that the caller can't obtain a reference to the mutable object
 */
final class ImmutablePerson {
    private final String firstName;
    private final String lastName;
    private final Date birthDate;  // Mutable
    private final List<String> hobbies;  // Mutable
    private final Map<String, String> contacts;  // Mutable
    
    /**
     * Constructor that makes defensive copies of mutable inputs
     */
    public ImmutablePerson(String firstName, String lastName, Date birthDate, 
                          List<String> hobbies, Map<String, String> contacts) {
        this.firstName = firstName;
        this.lastName = lastName;
        
        // Defensive copy for mutable Date
        this.birthDate = birthDate != null ? new Date(birthDate.getTime()) : null;
        
        // Defensive copy for mutable List
        this.hobbies = new ArrayList<>(hobbies);
        
        // Defensive copy for mutable Map
        this.contacts = new HashMap<>(contacts);
    }
    
    // Getters (no setters for immutability)
    
    public String getFirstName() {
        return firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    /**
     * Return a defensive copy of the mutable birthDate
     */
    public Date getBirthDate() {
        return birthDate != null ? new Date(birthDate.getTime()) : null;
    }
    
    /**
     * Return an unmodifiable view of the hobbies list
     */
    public List<String> getHobbies() {
        return Collections.unmodifiableList(hobbies);
    }
    
    /**
     * Return an unmodifiable view of the contacts map
     */
    public Map<String, String> getContacts() {
        return Collections.unmodifiableMap(contacts);
    }
    
    @Override
    public String toString() {
        return "ImmutablePerson [firstName=" + firstName + 
               ", lastName=" + lastName + 
               ", birthDate=" + birthDate + 
               ", hobbies=" + hobbies + 
               ", contacts=" + contacts + "]";
    }
}

/**
 * Logic Explanation:
 * 
 * 1. Class Design:
 *    - The class is declared as final to prevent inheritance and potential violation of immutability
 *    - All fields are private and final, ensuring they can only be set once during object creation
 *    - No setter methods are provided, preventing direct modification of fields
 * 
 * 2. Handling Mutable Objects:
 *    - For mutable objects like Date, List, and Map, we create defensive copies in the constructor
 *    - This ensures that changes to the original objects passed to the constructor don't affect our immutable object
 * 
 * 3. Protecting Returned Mutable Objects:
 *    - For Date: We return a new Date object with the same time value
 *    - For List and Map: We use Collections.unmodifiableList/Map to return read-only views
 *    - This prevents callers from modifying the internal state through getters
 * 
 * 4. Immutable Update Pattern:
 *    - To "modify" an immutable object, we create a new instance with the updated values
 *    - The original object remains unchanged
 * 
 * Benefits of Immutability:
 * - Thread safety without synchronization
 * - Simplicity and reliability (state doesn't change)
 * - Safe for use as keys in maps or elements in sets
 * - Facilitates defensive programming
 * 
 * Real-world Use Cases:
 * - String class in Java
 * - Wrapper classes (Integer, Boolean, etc.)
 * - BigInteger and BigDecimal for precise arithmetic
 * - LocalDate, LocalTime in Java 8 date/time API
 * - Configuration objects
 * - Value objects in domain-driven design
 * - Objects shared between threads
 * - Cache keys
 */