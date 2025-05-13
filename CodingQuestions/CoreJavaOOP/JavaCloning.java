/**
 * Core Java & OOP - Question 13: Cloning in Java (shallow vs deep copy)
 */
package CodingQuestions.CoreJavaOOP;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JavaCloning {

    public static void main(String[] args) {
        System.out.println("SHALLOW COPY EXAMPLES");
        System.out.println("---------------------");
        
        // Shallow copy using clone() method
        try {
            Address address = new Address("123 Main St", "New York", "10001");
            PersonWithShallowCopy original = new PersonWithShallowCopy("John", 30, address);
            
            // Create a shallow copy
            PersonWithShallowCopy shallowCopy = original.clone();
            
            System.out.println("Original: " + original);
            System.out.println("Shallow Copy: " + shallowCopy);
            
            // Modify primitive field in the copy
            shallowCopy.setAge(35);
            System.out.println("\nAfter modifying age in the copy:");
            System.out.println("Original: " + original);
            System.out.println("Shallow Copy: " + shallowCopy);
            
            // Modify reference field in the copy
            shallowCopy.getAddress().setCity("Boston");
            System.out.println("\nAfter modifying address in the copy:");
            System.out.println("Original: " + original);
            System.out.println("Shallow Copy: " + shallowCopy);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        
        System.out.println("\nDEEP COPY EXAMPLES");
        System.out.println("------------------");
        
        // Deep copy using clone() method
        try {
            Address address = new Address("456 Oak St", "Chicago", "60601");
            PersonWithDeepCopy original = new PersonWithDeepCopy("Alice", 25, address);
            
            // Create a deep copy
            PersonWithDeepCopy deepCopy = original.clone();
            
            System.out.println("Original: " + original);
            System.out.println("Deep Copy: " + deepCopy);
            
            // Modify primitive field in the copy
            deepCopy.setAge(28);
            System.out.println("\nAfter modifying age in the copy:");
            System.out.println("Original: " + original);
            System.out.println("Deep Copy: " + deepCopy);
            
            // Modify reference field in the copy
            deepCopy.getAddress().setCity("San Francisco");
            System.out.println("\nAfter modifying address in the copy:");
            System.out.println("Original: " + original);
            System.out.println("Deep Copy: " + deepCopy);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        
        System.out.println("\nCOPY CONSTRUCTOR EXAMPLE");
        System.out.println("-----------------------");
        
        // Deep copy using copy constructor
        Address address = new Address("789 Pine St", "Seattle", "98101");
        PersonWithCopyConstructor original = new PersonWithCopyConstructor("Bob", 40, address);
        
        // Create a copy using copy constructor
        PersonWithCopyConstructor copy = new PersonWithCopyConstructor(original);
        
        System.out.println("Original: " + original);
        System.out.println("Copy: " + copy);
        
        // Modify reference field in the copy
        copy.getAddress().setCity("Portland");
        System.out.println("\nAfter modifying address in the copy:");
        System.out.println("Original: " + original);
        System.out.println("Copy: " + copy);
        
        System.out.println("\nCLONING COLLECTIONS EXAMPLE");
        System.out.println("---------------------------");
        
        // Cloning collections
        List<String> originalList = new ArrayList<>(Arrays.asList("one", "two", "three"));
        List<String> shallowCopyList = new ArrayList<>(originalList); // Creates a shallow copy
        
        System.out.println("Original List: " + originalList);
        System.out.println("Shallow Copy List: " + shallowCopyList);
        
        // Modify the copy
        shallowCopyList.add("four");
        System.out.println("\nAfter adding an element to the copy:");
        System.out.println("Original List: " + originalList);
        System.out.println("Shallow Copy List: " + shallowCopyList);
        
        // Demonstrate shallow copy with mutable objects in collections
        List<Address> originalAddresses = new ArrayList<>();
        originalAddresses.add(new Address("123 Main St", "New York", "10001"));
        originalAddresses.add(new Address("456 Oak St", "Chicago", "60601"));
        
        // Shallow copy of the list
        List<Address> shallowCopyAddresses = new ArrayList<>(originalAddresses);
        
        System.out.println("\nOriginal Addresses: " + originalAddresses);
        System.out.println("Shallow Copy Addresses: " + shallowCopyAddresses);
        
        // Modify an object in the copy
        shallowCopyAddresses.get(0).setCity("Boston");
        System.out.println("\nAfter modifying an address in the copy:");
        System.out.println("Original Addresses: " + originalAddresses);
        System.out.println("Shallow Copy Addresses: " + shallowCopyAddresses);
        
        // Deep copy of the list
        List<Address> deepCopyAddresses = new ArrayList<>();
        for (Address addr : originalAddresses) {
            deepCopyAddresses.add(new Address(addr)); // Using copy constructor
        }
        
        System.out.println("\nOriginal Addresses: " + originalAddresses);
        System.out.println("Deep Copy Addresses: " + deepCopyAddresses);
        
        // Modify an object in the deep copy
        deepCopyAddresses.get(0).setCity("San Francisco");
        System.out.println("\nAfter modifying an address in the deep copy:");
        System.out.println("Original Addresses: " + originalAddresses);
        System.out.println("Deep Copy Addresses: " + deepCopyAddresses);
    }
}

/**
 * Problem Statement:
 * Demonstrate the difference between shallow copy and deep copy in Java.
 * Implement both approaches using the clone() method and copy constructors.
 * Show how each approach affects primitive fields and reference fields.
 * 
 * Solution 1: Address class (used by other classes)
 */
class Address implements Cloneable {
    private String street;
    private String city;
    private String zipCode;
    
    public Address(String street, String city, String zipCode) {
        this.street = street;
        this.city = city;
        this.zipCode = zipCode;
    }
    
    // Copy constructor for deep copy
    public Address(Address other) {
        this.street = other.street;
        this.city = other.city;
        this.zipCode = other.zipCode;
    }
    
    // Getters and setters
    public String getStreet() {
        return street;
    }
    
    public void setStreet(String street) {
        this.street = street;
    }
    
    public String getCity() {
        return city;
    }
    
    public void setCity(String city) {
        this.city = city;
    }
    
    public String getZipCode() {
        return zipCode;
    }
    
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
    
    @Override
    public String toString() {
        return "Address [street=" + street + ", city=" + city + ", zipCode=" + zipCode + "]";
    }
    
    @Override
    protected Address clone() throws CloneNotSupportedException {
        return (Address) super.clone();
    }
}

/**
 * Solution 2: Person class with shallow copy
 */
class PersonWithShallowCopy implements Cloneable {
    private String name;
    private int age;
    private Address address; // Reference type
    
    public PersonWithShallowCopy(String name, int age, Address address) {
        this.name = name;
        this.age = age;
        this.address = address;
    }
    
    // Getters and setters
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public int getAge() {
        return age;
    }
    
    public void setAge(int age) {
        this.age = age;
    }
    
    public Address getAddress() {
        return address;
    }
    
    public void setAddress(Address address) {
        this.address = address;
    }
    
    @Override
    public String toString() {
        return "Person [name=" + name + ", age=" + age + ", address=" + address + "]";
    }
    
    // Shallow copy implementation
    @Override
    public PersonWithShallowCopy clone() throws CloneNotSupportedException {
        return (PersonWithShallowCopy) super.clone();
    }
}

/**
 * Solution 3: Person class with deep copy
 */
class PersonWithDeepCopy implements Cloneable {
    private String name;
    private int age;
    private Address address; // Reference type
    
    public PersonWithDeepCopy(String name, int age, Address address) {
        this.name = name;
        this.age = age;
        this.address = address;
    }
    
    // Getters and setters
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public int getAge() {
        return age;
    }
    
    public void setAge(int age) {
        this.age = age;
    }
    
    public Address getAddress() {
        return address;
    }
    
    public void setAddress(Address address) {
        this.address = address;
    }
    
    @Override
    public String toString() {
        return "Person [name=" + name + ", age=" + age + ", address=" + address + "]";
    }
    
    // Deep copy implementation
    @Override
    public PersonWithDeepCopy clone() throws CloneNotSupportedException {
        PersonWithDeepCopy cloned = (PersonWithDeepCopy) super.clone();
        cloned.address = this.address.clone(); // Clone the reference object
        return cloned;
    }
}

/**
 * Solution 4: Person class with copy constructor
 */
class PersonWithCopyConstructor {
    private String name;
    private int age;
    private Address address; // Reference type
    
    public PersonWithCopyConstructor(String name, int age, Address address) {
        this.name = name;
        this.age = age;
        this.address = address;
    }
    
    // Copy constructor for deep copy
    public PersonWithCopyConstructor(PersonWithCopyConstructor other) {
        this.name = other.name;
        this.age = other.age;
        this.address = new Address(other.address); // Create a new Address using its copy constructor
    }
    
    // Getters and setters
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public int getAge() {
        return age;
    }
    
    public void setAge(int age) {
        this.age = age;
    }
    
    public Address getAddress() {
        return address;
    }
    
    public void setAddress(Address address) {
        this.address = address;
    }
    
    @Override
    public String toString() {
        return "Person [name=" + name + ", age=" + age + ", address=" + address + "]";
    }
}

/**
 * Logic Explanation:
 * 
 * Shallow Copy:
 * 1. Creates a new object and copies all fields from the original object
 * 2. If a field is a primitive type, its value is copied
 * 3. If a field is a reference type, the reference is copied (not the object itself)
 * 4. Both original and copy objects point to the same referenced objects
 * 5. Changes to referenced objects in either original or copy affect both
 * 
 * Deep Copy:
 * 1. Creates a new object and copies all fields from the original object
 * 2. If a field is a primitive type, its value is copied
 * 3. If a field is a reference type, a new copy of the referenced object is created
 * 4. Original and copy objects point to different referenced objects
 * 5. Changes to referenced objects in either original or copy do not affect the other
 * 
 * Implementation Approaches:
 * 
 * 1. Using clone() method:
 *    - Implement the Cloneable interface
 *    - Override the clone() method
 *    - For shallow copy: use super.clone()
 *    - For deep copy: use super.clone() and then clone each reference field
 * 
 * 2. Using copy constructor:
 *    - Create a constructor that takes an object of the same class as a parameter
 *    - Copy all fields from the parameter object to the new object
 *    - For reference fields, create new instances using their copy constructors
 * 
 * 3. For collections:
 *    - Shallow copy: use the collection's constructor or addAll() method
 *    - Deep copy: iterate through the collection and create new copies of each element
 * 
 * When to Use:
 * - Use shallow copy when referenced objects are immutable or when sharing is acceptable
 * - Use deep copy when referenced objects are mutable and independent copies are needed
 * 
 * Real-world Use Cases:
 * - Caching: Creating copies of objects to store in cache
 * - Defensive programming: Returning copies to prevent modification of internal state
 * - Undo/Redo functionality: Saving object state for later restoration
 * - Parallel processing: Creating copies for concurrent processing
 * - Object pooling: Creating copies of template objects
 */