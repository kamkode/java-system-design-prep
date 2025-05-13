/**
 * Core Java & OOP - Question 11: Overloading vs Overriding with code
 */
package CodingQuestions.CoreJavaOOP;

public class OverloadingVsOverriding {

    public static void main(String[] args) {
        System.out.println("METHOD OVERLOADING EXAMPLES");
        System.out.println("---------------------------");
        
        // Method overloading examples
        Calculator calculator = new Calculator();
        System.out.println("Sum of 5 and 10: " + calculator.add(5, 10));
        System.out.println("Sum of 5.5 and 10.3: " + calculator.add(5.5, 10.3));
        System.out.println("Sum of 5, 10, and 15: " + calculator.add(5, 10, 15));
        
        System.out.println("\nMETHOD OVERRIDING EXAMPLES");
        System.out.println("---------------------------");
        
        // Method overriding examples
        Animal animal = new Animal();
        Dog dog = new Dog();
        Cat cat = new Cat();
        
        System.out.println("Animal sound:");
        animal.makeSound();
        
        System.out.println("\nDog sound:");
        dog.makeSound();
        
        System.out.println("\nCat sound:");
        cat.makeSound();
        
        System.out.println("\nDynamic Method Dispatch (Runtime Polymorphism):");
        Animal dogAsAnimal = new Dog();
        Animal catAsAnimal = new Cat();
        
        System.out.println("\nDog as Animal sound:");
        dogAsAnimal.makeSound();  // Calls Dog's makeSound() method
        
        System.out.println("\nCat as Animal sound:");
        catAsAnimal.makeSound();  // Calls Cat's makeSound() method
        
        System.out.println("\nCOMPILE-TIME VS RUNTIME BINDING");
        System.out.println("-------------------------------");
        
        // Compile-time binding (Method Overloading)
        System.out.println("Compile-time binding example:");
        BindingExample example = new BindingExample();
        example.display(10);      // Calls display(int)
        example.display("Hello"); // Calls display(String)
        
        // Runtime binding (Method Overriding)
        System.out.println("\nRuntime binding example:");
        Parent parent = new Parent();
        Child child = new Child();
        Parent childAsParent = new Child();
        
        parent.show();       // Calls Parent's show()
        child.show();        // Calls Child's show()
        childAsParent.show(); // Calls Child's show() due to runtime binding
    }
}

/**
 * Problem Statement:
 * Demonstrate the difference between method overloading and method overriding in Java
 * with practical code examples. Explain when to use each and how they contribute to
 * polymorphism in Java.
 * 
 * Solution 1: Method Overloading Example
 * Method overloading allows a class to have multiple methods with the same name
 * but different parameters (different number, types, or order of parameters).
 */
class Calculator {
    // Method with two int parameters
    public int add(int a, int b) {
        System.out.println("Calling add(int, int)");
        return a + b;
    }
    
    // Method with two double parameters - overloaded based on parameter type
    public double add(double a, double b) {
        System.out.println("Calling add(double, double)");
        return a + b;
    }
    
    // Method with three int parameters - overloaded based on number of parameters
    public int add(int a, int b, int c) {
        System.out.println("Calling add(int, int, int)");
        return a + b + c;
    }
    
    // Method with parameters in different order - overloaded based on parameter order
    public String add(String a, int b) {
        return a + b;
    }
    
    public String add(int a, String b) {
        return a + b;
    }
}

/**
 * Solution 2: Method Overriding Example
 * Method overriding allows a subclass to provide a specific implementation
 * of a method that is already defined in its superclass.
 */
class Animal {
    public void makeSound() {
        System.out.println("Animal makes a generic sound");
    }
    
    // Final method cannot be overridden
    public final void breathe() {
        System.out.println("Animal breathes");
    }
    
    // Static method cannot be overridden (but can be hidden)
    public static void sleep() {
        System.out.println("Animal sleeps");
    }
}

class Dog extends Animal {
    // Overriding the makeSound method
    @Override
    public void makeSound() {
        System.out.println("Dog barks: Woof! Woof!");
    }
    
    // This is method hiding, not overriding
    public static void sleep() {
        System.out.println("Dog sleeps");
    }
}

class Cat extends Animal {
    // Overriding the makeSound method
    @Override
    public void makeSound() {
        System.out.println("Cat meows: Meow! Meow!");
    }
}

/**
 * Solution 3: Compile-time vs Runtime Binding Example
 */
class BindingExample {
    // Method overloading - resolved at compile time
    public void display(int num) {
        System.out.println("Displaying integer: " + num);
    }
    
    public void display(String text) {
        System.out.println("Displaying string: " + text);
    }
}

class Parent {
    public void show() {
        System.out.println("Parent's show() method");
    }
}

class Child extends Parent {
    // Method overriding - resolved at runtime
    @Override
    public void show() {
        System.out.println("Child's show() method");
    }
}

/**
 * Logic Explanation:
 * 
 * Method Overloading:
 * 1. Occurs when a class has multiple methods with the same name but different parameters
 * 2. Parameters can differ in:
 *    - Number (add(int, int) vs add(int, int, int))
 *    - Type (add(int, int) vs add(double, double))
 *    - Order (add(String, int) vs add(int, String))
 * 3. Return type alone is not sufficient for overloading
 * 4. Resolved at compile time (static binding or early binding)
 * 5. Example of compile-time polymorphism
 * 
 * Method Overriding:
 * 1. Occurs when a subclass provides a specific implementation of a method already defined in its superclass
 * 2. Method signature must be the same (name, parameters, return type)
 * 3. Access modifier can be the same or less restrictive
 * 4. Cannot override final or static methods
 * 5. Resolved at runtime (dynamic binding or late binding)
 * 6. Example of runtime polymorphism
 * 
 * Key Differences:
 * 1. Overloading: Same method name, different parameters, in the same class
 * 2. Overriding: Same method name, same parameters, in parent-child classes
 * 3. Overloading is resolved at compile time, overriding at runtime
 * 4. Overloading is about providing different ways to call the same method
 * 5. Overriding is about providing a specific implementation of a method in a subclass
 * 
 * When to Use:
 * - Use overloading when you want to perform similar operations with different inputs
 * - Use overriding when you want to provide a specific implementation in a subclass
 * 
 * Real-world Use Cases:
 * - Overloading: Constructor overloading for flexible object creation, utility methods that handle different input types
 * - Overriding: Framework customization, implementing abstract methods, specializing behavior in subclasses
 */