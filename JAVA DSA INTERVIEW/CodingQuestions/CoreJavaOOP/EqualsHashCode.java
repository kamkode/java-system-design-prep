/**
 * Core Java & OOP - Question 7: Override equals and hashCode
 */
package CodingQuestions.CoreJavaOOP;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class EqualsHashCode {

    public static void main(String[] args) {
        // Test with a class that doesn't override equals and hashCode
        System.out.println("Testing PersonWithoutEqualsHashCode:");
        testPersonWithoutEqualsHashCode();
        
        // Test with a class that correctly overrides equals but not hashCode
        System.out.println("\nTesting PersonWithEqualsOnly:");
        testPersonWithEqualsOnly();
        
        // Test with a class that correctly overrides both equals and hashCode
        System.out.println("\nTesting PersonWithEqualsAndHashCode:");
        testPersonWithEqualsAndHashCode();
        
        // Test with a class that uses Objects.equals and Objects.hash
        System.out.println("\nTesting PersonWithObjectsUtil:");
        testPersonWithObjectsUtil();
    }
    
    /**
     * Test a class that doesn't override equals and hashCode
     */
    private static void testPersonWithoutEqualsHashCode() {
        PersonWithoutEqualsHashCode person1 = new PersonWithoutEqualsHashCode("John", "Doe", 30);
        PersonWithoutEqualsHashCode person2 = new PersonWithoutEqualsHashCode("John", "Doe", 30);
        
        // Test equality
        System.out.println("person1.equals(person2): " + person1.equals(person2));
        System.out.println("person1 == person2: " + (person1 == person2));
        
        // Test in HashSet
        Set<PersonWithoutEqualsHashCode> personSet = new HashSet<>();
        personSet.add(person1);
        System.out.println("HashSet contains person2: " + personSet.contains(person2));
        personSet.add(person2);
        System.out.println("HashSet size after adding both: " + personSet.size());
        
        // Test in HashMap
        Map<PersonWithoutEqualsHashCode, String> personMap = new HashMap<>();
        personMap.put(person1, "Person 1");
        System.out.println("HashMap contains person2 as key: " + personMap.containsKey(person2));
        System.out.println("Value for person2: " + personMap.get(person2));
    }
    
    /**
     * Test a class that overrides equals but not hashCode
     */
    private static void testPersonWithEqualsOnly() {
        PersonWithEqualsOnly person1 = new PersonWithEqualsOnly("John", "Doe", 30);
        PersonWithEqualsOnly person2 = new PersonWithEqualsOnly("John", "Doe", 30);
        
        // Test equality
        System.out.println("person1.equals(person2): " + person1.equals(person2));
        
        // Test in HashSet
        Set<PersonWithEqualsOnly> personSet = new HashSet<>();
        personSet.add(person1);
        System.out.println("HashSet contains person2: " + personSet.contains(person2));
        personSet.add(person2);
        System.out.println("HashSet size after adding both: " + personSet.size());
        
        // Test in HashMap
        Map<PersonWithEqualsOnly, String> personMap = new HashMap<>();
        personMap.put(person1, "Person 1");
        System.out.println("HashMap contains person2 as key: " + personMap.containsKey(person2));
        System.out.println("Value for person2: " + personMap.get(person2));
    }
    
    /**
     * Test a class that correctly overrides both equals and hashCode
     */
    private static void testPersonWithEqualsAndHashCode() {
        PersonWithEqualsAndHashCode person1 = new PersonWithEqualsAndHashCode("John", "Doe", 30);
        PersonWithEqualsAndHashCode person2 = new PersonWithEqualsAndHashCode("John", "Doe", 30);
        
        // Test equality
        System.out.println("person1.equals(person2): " + person1.equals(person2));
        
        // Test in HashSet
        Set<PersonWithEqualsAndHashCode> personSet = new HashSet<>();
        personSet.add(person1);
        System.out.println("HashSet contains person2: " + personSet.contains(person2));
        personSet.add(person2);
        System.out.println("HashSet size after adding both: " + personSet.size());
        
        // Test in HashMap
        Map<PersonWithEqualsAndHashCode, String> personMap = new HashMap<>();
        personMap.put(person1, "Person 1");
        System.out.println("HashMap contains person2 as key: " + personMap.containsKey(person2));
        System.out.println("Value for person2: " + personMap.get(person2));
    }
    
    /**
     * Test a class that uses Objects.equals and Objects.hash
     */
    private static void testPersonWithObjectsUtil() {
        PersonWithObjectsUtil person1 = new PersonWithObjectsUtil("John", "Doe", 30);
        PersonWithObjectsUtil person2 = new PersonWithObjectsUtil("John", "Doe", 30);
        
        // Test equality
        System.out.println("person1.equals(person2): " + person1.equals(person2));
        
        // Test in HashSet
        Set<PersonWithObjectsUtil> personSet = new HashSet<>();
        personSet.add(person1);
        System.out.println("HashSet contains person2: " + personSet.contains(person2));
        personSet.add(person2);
        System.out.println("HashSet size after adding both: " + personSet.size());
        
        // Test in HashMap
        Map<PersonWithObjectsUtil, String> personMap = new HashMap<>();
        personMap.put(person1, "Person 1");
        System.out.println("HashMap contains person2 as key: " + personMap.containsKey(person2));
        System.out.println("Value for person2: " + personMap.get(person2));
    }
}

/**
 * Problem Statement:
 * Implement a class with properly overridden equals() and hashCode() methods.
 * Demonstrate why both methods need to be overridden together and the consequences
 * of not doing so, especially when using hash-based collections like HashSet and HashMap.
 * 
 * Solution 1: Class without overriding equals and hashCode
 */
class PersonWithoutEqualsHashCode {
    private String firstName;
    private String lastName;
    private int age;
    
    public PersonWithoutEqualsHashCode(String firstName, String lastName, int age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }
    
    // No equals or hashCode override
    
    @Override
    public String toString() {
        return "Person [firstName=" + firstName + ", lastName=" + lastName + ", age=" + age + "]";
    }
}

/**
 * Solution 2: Class with equals but no hashCode
 */
class PersonWithEqualsOnly {
    private String firstName;
    private String lastName;
    private int age;
    
    public PersonWithEqualsOnly(String firstName, String lastName, int age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        PersonWithEqualsOnly other = (PersonWithEqualsOnly) obj;
        return age == other.age && 
               (firstName == null ? other.firstName == null : firstName.equals(other.firstName)) &&
               (lastName == null ? other.lastName == null : lastName.equals(other.lastName));
    }
    
    // No hashCode override
    
    @Override
    public String toString() {
        return "Person [firstName=" + firstName + ", lastName=" + lastName + ", age=" + age + "]";
    }
}

/**
 * Solution 3: Class with both equals and hashCode
 */
class PersonWithEqualsAndHashCode {
    private String firstName;
    private String lastName;
    private int age;
    
    public PersonWithEqualsAndHashCode(String firstName, String lastName, int age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        PersonWithEqualsAndHashCode other = (PersonWithEqualsAndHashCode) obj;
        return age == other.age && 
               (firstName == null ? other.firstName == null : firstName.equals(other.firstName)) &&
               (lastName == null ? other.lastName == null : lastName.equals(other.lastName));
    }
    
    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + age;
        return result;
    }
    
    @Override
    public String toString() {
        return "Person [firstName=" + firstName + ", lastName=" + lastName + ", age=" + age + "]";
    }
}

/**
 * Solution 4: Using Java 7+ Objects utility methods
 */
class PersonWithObjectsUtil {
    private String firstName;
    private String lastName;
    private int age;
    
    public PersonWithObjectsUtil(String firstName, String lastName, int age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        PersonWithObjectsUtil other = (PersonWithObjectsUtil) obj;
        return age == other.age && 
               Objects.equals(firstName, other.firstName) &&
               Objects.equals(lastName, other.lastName);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, age);
    }
    
    @Override
    public String toString() {
        return "Person [firstName=" + firstName + ", lastName=" + lastName + ", age=" + age + "]";
    }
}

/**
 * Logic Explanation:
 * 
 * 1. Class without equals and hashCode:
 *    - Uses default Object.equals() which compares object references (memory addresses)
 *    - Uses default Object.hashCode() which is typically based on memory address
 *    - Two objects with the same content are considered different
 *    - In hash-based collections, lookups will fail for equivalent objects
 * 
 * 2. Class with equals but no hashCode:
 *    - Correctly identifies when two objects have the same content
 *    - But in hash-based collections, objects will be placed in different buckets
 *    - This violates the contract that equal objects must have equal hash codes
 *    - Results in unexpected behavior with HashSet and HashMap
 * 
 * 3. Class with both equals and hashCode:
 *    - Correctly identifies when two objects have the same content
 *    - Ensures equal objects have equal hash codes
 *    - Works correctly with hash-based collections
 *    - Follows the contract between equals and hashCode
 * 
 * 4. Class using Objects utility methods:
 *    - A more modern approach using Java 7+ utilities
 *    - Objects.equals handles null checks automatically
 *    - Objects.hash generates a hash code from multiple fields
 *    - Cleaner code with less boilerplate
 * 
 * The equals-hashCode Contract:
 * 1. If two objects are equal according to equals(), they MUST have the same hashCode()
 * 2. If two objects have the same hashCode(), they are NOT necessarily equal
 * 3. If equals() is overridden, hashCode() must also be overridden
 * 
 * Real-world Use Cases:
 * - Any domain object that needs to be stored in hash-based collections
 * - Entity classes in ORM frameworks like Hibernate
 * - Data transfer objects (DTOs) in web services
 * - Cache keys in memory caches
 * - Business objects where logical equality matters more than reference equality
 */