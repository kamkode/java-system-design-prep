/**
 * Core Java & OOP - Question 12: Interface vs Abstract class with code
 */
package CodingQuestions.CoreJavaOOP;

public class InterfaceVsAbstractClass {

    public static void main(String[] args) {
        System.out.println("ABSTRACT CLASS EXAMPLES");
        System.out.println("-----------------------");
        
        // Cannot instantiate abstract class
        // Shape shape = new Shape(); // Compilation error
        
        // But can create instances of concrete subclasses
        Circle circle = new Circle(5.0);
        Rectangle rectangle = new Rectangle(4.0, 6.0);
        
        System.out.println("Circle area: " + circle.calculateArea());
        System.out.println("Circle perimeter: " + circle.calculatePerimeter());
        circle.display();
        
        System.out.println("\nRectangle area: " + rectangle.calculateArea());
        System.out.println("Rectangle perimeter: " + rectangle.calculatePerimeter());
        rectangle.display();
        
        System.out.println("\nINTERFACE EXAMPLES");
        System.out.println("------------------");
        
        // Cannot instantiate interface
        // Drawable drawable = new Drawable(); // Compilation error
        
        // But can create instances of implementing classes
        DrawableCircle drawableCircle = new DrawableCircle(3.0);
        DrawableRectangle drawableRectangle = new DrawableRectangle(2.0, 3.0);
        
        drawableCircle.draw();
        drawableCircle.resize(2.0);
        System.out.println("Circle color: " + drawableCircle.getColor());
        
        System.out.println();
        drawableRectangle.draw();
        drawableRectangle.resize(1.5);
        System.out.println("Rectangle color: " + drawableRectangle.getColor());
        
        System.out.println("\nMULTIPLE INHERITANCE WITH INTERFACES");
        System.out.println("-----------------------------------");
        
        // Class implementing multiple interfaces
        SmartPhone phone = new SmartPhone();
        phone.call("123-456-7890");
        phone.sendMessage("Hello, world!");
        phone.browse("https://www.example.com");
        phone.takePhoto();
        
        System.out.println("\nDEFAULT METHODS IN INTERFACES");
        System.out.println("-----------------------------");
        
        // Default method in interface
        ModernVehicle car = new Car();
        car.start();
        car.stop();
        car.autoPark(); // Using default method
        
        System.out.println("\nFUNCTIONAL INTERFACES");
        System.out.println("--------------------");
        
        // Functional interface with lambda
        Processor<Integer> squareProcessor = (input) -> input * input;
        System.out.println("Square of 5: " + squareProcessor.process(5));
        
        Processor<String> reverseProcessor = (input) -> new StringBuilder(input).reverse().toString();
        System.out.println("Reverse of 'Hello': " + reverseProcessor.process("Hello"));
    }
}

/**
 * Problem Statement:
 * Demonstrate the differences between interfaces and abstract classes in Java with code examples.
 * Explain when to use each and how they contribute to different design patterns.
 * 
 * Solution 1: Abstract Class Example
 * 
 * Abstract classes:
 * - Can have both abstract and concrete methods
 * - Can have constructors
 * - Can have instance variables with any access modifier
 * - A class can extend only one abstract class
 */
abstract class Shape {
    protected String name;
    
    // Constructor in abstract class
    public Shape(String name) {
        this.name = name;
    }
    
    // Abstract methods (must be implemented by subclasses)
    public abstract double calculateArea();
    public abstract double calculatePerimeter();
    
    // Concrete method (inherited by subclasses)
    public void display() {
        System.out.println("This is a " + name + " shape");
    }
}

class Circle extends Shape {
    private double radius;
    
    public Circle(double radius) {
        super("Circle");
        this.radius = radius;
    }
    
    @Override
    public double calculateArea() {
        return Math.PI * radius * radius;
    }
    
    @Override
    public double calculatePerimeter() {
        return 2 * Math.PI * radius;
    }
}

class Rectangle extends Shape {
    private double length;
    private double width;
    
    public Rectangle(double length, double width) {
        super("Rectangle");
        this.length = length;
        this.width = width;
    }
    
    @Override
    public double calculateArea() {
        return length * width;
    }
    
    @Override
    public double calculatePerimeter() {
        return 2 * (length + width);
    }
}

/**
 * Solution 2: Interface Example
 * 
 * Interfaces:
 * - Can only have abstract methods (before Java 8)
 * - Can have default and static methods (Java 8+)
 * - Can have constants (public static final)
 * - A class can implement multiple interfaces
 */
interface Drawable {
    // Constants (implicitly public static final)
    String DEFAULT_COLOR = "Black";
    
    // Abstract methods (implicitly public abstract)
    void draw();
    void resize(double factor);
    
    // Default method (Java 8+)
    default String getColor() {
        return DEFAULT_COLOR;
    }
    
    // Static method (Java 8+)
    static void printInfo() {
        System.out.println("This is a drawable object");
    }
}

interface Colorable {
    void setColor(String color);
    String getColor();
}

class DrawableCircle implements Drawable {
    private double radius;
    
    public DrawableCircle(double radius) {
        this.radius = radius;
    }
    
    @Override
    public void draw() {
        System.out.println("Drawing a circle with radius " + radius);
    }
    
    @Override
    public void resize(double factor) {
        this.radius *= factor;
        System.out.println("Resized circle. New radius: " + radius);
    }
}

class DrawableRectangle implements Drawable, Colorable {
    private double length;
    private double width;
    private String color = Drawable.DEFAULT_COLOR;
    
    public DrawableRectangle(double length, double width) {
        this.length = length;
        this.width = width;
    }
    
    @Override
    public void draw() {
        System.out.println("Drawing a rectangle with length " + length + " and width " + width);
    }
    
    @Override
    public void resize(double factor) {
        this.length *= factor;
        this.width *= factor;
        System.out.println("Resized rectangle. New dimensions: " + length + " x " + width);
    }
    
    @Override
    public void setColor(String color) {
        this.color = color;
    }
    
    @Override
    public String getColor() {
        return this.color; // Override the default implementation
    }
}

/**
 * Solution 3: Multiple Inheritance with Interfaces
 */
interface Phone {
    void call(String number);
    void sendMessage(String message);
}

interface WebBrowser {
    void browse(String url);
}

interface Camera {
    void takePhoto();
}

// Class implementing multiple interfaces
class SmartPhone implements Phone, WebBrowser, Camera {
    @Override
    public void call(String number) {
        System.out.println("Calling " + number);
    }
    
    @Override
    public void sendMessage(String message) {
        System.out.println("Sending message: " + message);
    }
    
    @Override
    public void browse(String url) {
        System.out.println("Browsing " + url);
    }
    
    @Override
    public void takePhoto() {
        System.out.println("Taking a photo");
    }
}

/**
 * Solution 4: Default Methods in Interfaces (Java 8+)
 */
interface Vehicle {
    void start();
    void stop();
    
    // Default method
    default void autoPark() {
        System.out.println("Auto-parking the vehicle");
    }
}

interface ModernVehicle extends Vehicle {
    // Overriding default method
    @Override
    default void autoPark() {
        System.out.println("Auto-parking using advanced sensors");
    }
}

class Car implements ModernVehicle {
    @Override
    public void start() {
        System.out.println("Starting the car");
    }
    
    @Override
    public void stop() {
        System.out.println("Stopping the car");
    }
}

/**
 * Solution 5: Functional Interface (Java 8+)
 */
@FunctionalInterface
interface Processor<T> {
    T process(T input);
    
    // Can have default and static methods
    default void printInfo() {
        System.out.println("This is a processor");
    }
}

/**
 * Logic Explanation:
 * 
 * Abstract Classes:
 * 1. Can have both abstract and concrete methods
 * 2. Can have constructors and instance variables
 * 3. Support access modifiers for methods and fields
 * 4. A class can extend only one abstract class (single inheritance)
 * 5. Used when you want to share code among closely related classes
 * 6. Typically represent an "is-a" relationship
 * 
 * Interfaces:
 * 1. Before Java 8, could only have abstract methods
 * 2. Java 8+ can have default and static methods
 * 3. Can only have constants (public static final)
 * 4. All methods are implicitly public
 * 5. A class can implement multiple interfaces (multiple inheritance of type)
 * 6. Used to define a contract that implementing classes must follow
 * 7. Typically represent a "can-do" relationship
 * 
 * Key Differences:
 * 1. Multiple Inheritance: Classes can implement multiple interfaces but extend only one abstract class
 * 2. State: Abstract classes can have state (instance variables), interfaces traditionally cannot
 * 3. Constructors: Abstract classes can have constructors, interfaces cannot
 * 4. Access Modifiers: Abstract classes can use any access modifier, interface methods are implicitly public
 * 5. Default Implementation: Both can provide default implementations (abstract classes via concrete methods, interfaces via default methods)
 * 
 * When to Use Abstract Classes:
 * - When you want to share code among closely related classes
 * - When you need to declare non-public members
 * - When you need to have state (instance variables)
 * - When you're designing for inheritance and have a base implementation
 * 
 * When to Use Interfaces:
 * - When you want to define a contract for unrelated classes
 * - When you need multiple inheritance
 * - When you're designing for API and want to specify behavior without implementation
 * - When you want to use lambda expressions (functional interfaces)
 * 
 * Real-world Use Cases:
 * - Abstract Classes: Template Method pattern, base classes in frameworks
 * - Interfaces: Strategy pattern, Dependency Injection, API design
 */