/**
 * Core Java & OOP - Question 14: Use of `this` and `super` keywords
 */
package CodingQuestions.CoreJavaOOP;

public class ThisAndSuper {

    public static void main(String[] args) {
        System.out.println("EXAMPLES OF 'this' KEYWORD");
        System.out.println("--------------------------");

        // Example 1: Using 'this' to refer to current instance variables
        Person person = new Person("John", "Doe", 30);
        person.displayInfo();

        // Example 2: Using 'this' to invoke current class method
        System.out.println("\nUsing 'this' to invoke current class method:");
        Calculator calculator = new Calculator();
        calculator.add(5, 10);

        // Example 3: Using 'this' in constructor chaining
        System.out.println("\nUsing 'this' in constructor chaining:");
        Employee employee1 = new Employee("Alice", "Smith");
        Employee employee2 = new Employee("Bob", "Johnson", "Developer");
        Employee employee3 = new Employee("Charlie", "Brown", "Manager", 75000);

        employee1.displayInfo();
        employee2.displayInfo();
        employee3.displayInfo();

        System.out.println("\nEXAMPLES OF 'super' KEYWORD");
        System.out.println("---------------------------");

        // Example 1: Using 'super' to call parent class method
        System.out.println("\nUsing 'super' to call parent class method:");
        Dog dog = new Dog();
        dog.makeSound();

        // Example 2: Using 'super' to access parent class variable
        System.out.println("\nUsing 'super' to access parent class variable:");
        RectangleShape rectangle = new RectangleShape(5, 10);
        rectangle.displayDimensions();

        // Example 3: Using 'super' in constructor
        System.out.println("\nUsing 'super' in constructor:");
        Manager manager = new Manager("David", "Wilson", "Senior Manager", 100000, "IT Department");
        manager.displayInfo();

        // Example 4: Method overriding and using both 'this' and 'super'
        System.out.println("\nMethod overriding with 'this' and 'super':");
        CircleExample circle = new CircleExample(7);
        circle.displayArea();
    }
}

/**
 * Problem Statement:
 * Demonstrate the use of 'this' and 'super' keywords in Java with practical examples.
 * Show different scenarios where these keywords are useful and explain their purpose.
 * 
 * Solution 1: Using 'this' to refer to current instance variables
 */
class Person {
    private String firstName;
    private String lastName;
    private int age;

    public Person(String firstName, String lastName, int age) {
        // Using 'this' to refer to instance variables
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }

    public void displayInfo() {
        System.out.println("Person: " + this.firstName + " " + this.lastName + ", Age: " + this.age);
    }
}

/**
 * Solution 2: Using 'this' to invoke current class method
 */
class MathCalculator {
    public void add(int a, int b) {
        System.out.println("Sum of " + a + " and " + b + " is: " + (a + b));
        // Using 'this' to call another method in the same class
        this.multiply(a, b);
    }

    public void multiply(int a, int b) {
        System.out.println("Product of " + a + " and " + b + " is: " + (a * b));
    }
}

/**
 * Solution 3: Using 'this' in constructor chaining
 */
class Employee {
    private String firstName;
    private String lastName;
    private String position;
    private double salary;

    // Constructor 1: Basic information
    public Employee(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.position = "Unassigned";
        this.salary = 0.0;
    }

    // Constructor 2: With position, calls Constructor 1
    public Employee(String firstName, String lastName, String position) {
        this(firstName, lastName); // Using 'this' to call another constructor
        this.position = position;
    }

    // Constructor 3: With position and salary, calls Constructor 2
    public Employee(String firstName, String lastName, String position, double salary) {
        this(firstName, lastName, position); // Using 'this' to call another constructor
        this.salary = salary;
    }

    public void displayInfo() {
        System.out.println("Employee: " + this.firstName + " " + this.lastName + 
                          ", Position: " + this.position + ", Salary: $" + this.salary);
    }
}

/**
 * Solution 4: Using 'super' to call parent class method
 */
class BaseAnimal {
    public void makeSound() {
        System.out.println("Animal makes a sound");
    }
}

class DogPet extends BaseAnimal {
    @Override
    public void makeSound() {
        // Using 'super' to call the parent class method
        super.makeSound();
        System.out.println("Dog barks: Woof! Woof!");
    }
}

/**
 * Solution 5: Using 'super' to access parent class variable
 */
class GeometricShape {
    protected String name;

    public GeometricShape() {
        this.name = "Shape";
    }
}

class RectangleShape extends GeometricShape {
    private double length;
    private double width;

    public RectangleShape(double length, double width) {
        this.name = "Rectangle"; // Overriding parent's variable
        this.length = length;
        this.width = width;
    }

    public void displayDimensions() {
        // Using 'super' to access parent class variable
        System.out.println("Parent class name: " + super.name);
        System.out.println("Current class name: " + this.name);
        System.out.println("Rectangle dimensions: " + this.length + " x " + this.width);
    }
}

/**
 * Solution 6: Using 'super' in constructor
 */
class Manager extends Employee {
    private String department;

    public Manager(String firstName, String lastName, String position, double salary, String department) {
        // Using 'super' to call parent class constructor
        super(firstName, lastName, position, salary);
        this.department = department;
    }

    @Override
    public void displayInfo() {
        // Using 'super' to call parent class method
        super.displayInfo();
        System.out.println("Department: " + this.department);
    }
}

/**
 * Solution 7: Method overriding and using both 'this' and 'super'
 */
class RoundShape {
    protected double radius;

    public RoundShape(double radius) {
        this.radius = radius;
    }

    public double calculateArea() {
        return Math.PI * radius * radius;
    }
}

class CircleExample extends RoundShape {
    public CircleExample(double radius) {
        super(radius);
    }

    @Override
    public double calculateArea() {
        // Using 'super' to call parent method
        return super.calculateArea();
    }

    public void displayArea() {
        // Using 'this' to call current class method
        double area = this.calculateArea();
        System.out.println("Circle with radius " + this.radius + " has area: " + area);
    }
}

/**
 * Logic Explanation:
 * 
 * The 'this' Keyword:
 * 
 * 1. Referring to Current Instance Variables:
 *    - Used to distinguish between instance variables and parameters with the same name
 *    - Example: this.firstName = firstName;
 * 
 * 2. Invoking Current Class Methods:
 *    - Used to call another method in the same class
 *    - Example: this.multiply(a, b);
 * 
 * 3. Constructor Chaining:
 *    - Used to call another constructor in the same class
 *    - Must be the first statement in a constructor
 *    - Example: this(firstName, lastName);
 * 
 * 4. Returning Current Class Instance:
 *    - Used in method chaining to return the current object
 *    - Example: return this;
 * 
 * 5. Passing as an Argument:
 *    - Used to pass the current object as an argument to another method
 *    - Example: someMethod(this);
 * 
 * The 'super' Keyword:
 * 
 * 1. Calling Parent Class Methods:
 *    - Used to call a method in the parent class that has been overridden
 *    - Example: super.makeSound();
 * 
 * 2. Accessing Parent Class Variables:
 *    - Used to access a variable in the parent class that has been hidden
 *    - Example: super.name;
 * 
 * 3. Calling Parent Class Constructor:
 *    - Used to call a constructor in the parent class
 *    - Must be the first statement in a constructor
 *    - Example: super(firstName, lastName);
 * 
 * Key Differences:
 * 1. 'this' refers to the current object instance, while 'super' refers to the parent class
 * 2. 'this' is used to access members of the current class, while 'super' is used to access members of the parent class
 * 3. 'this' can be used to call constructors in the same class, while 'super' can be used to call constructors in the parent class
 * 
 * Real-world Use Cases:
 * - Constructor chaining for flexible object creation
 * - Method overriding while still using parent functionality
 * - Avoiding variable name conflicts
 * - Building fluent interfaces with method chaining
 * - Implementing the Template Method design pattern
 */
