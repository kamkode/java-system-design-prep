package BlitzenxIntervierwQnA;

import java.util.Scanner;

/*
 * PROBLEM STATEMENT:
 * Write a Java program to compute the sum of two numbers irrespective of their type (integer, double, etc.)
 * using runtime polymorphism (method overriding). The program should demonstrate proper implementation
 * of inheritance and polymorphic behavior.
 * 
 * Requirements:
 * - Create a class hierarchy with a base class and derived classes for different number types.
 * - Use runtime polymorphism to calculate the sum based on the actual object type.
 * - Handle different number types (int, double, String representations of numbers).
 * - Use Scanner to take input from the user (numbers and their types).
 * - Implement proper error handling for invalid inputs.
 * - Output the sum with appropriate type information.
 * - Include all details (problem statement, explanation, solution) in a single .java file.
 *
 * EXPLANATION:
 * Class: PolymorphicAdder
 * Purpose: Demonstrates runtime polymorphism to calculate the sum of two numbers regardless of their type.
 *
 * CONCEPTS INVOLVED:
 * 1. Object-Oriented Programming:
 *    - Inheritance: Creating a class hierarchy for different number types.
 *    - Polymorphism: Using method overriding to provide type-specific implementations.
 *    - Abstract Classes: Base class definition with abstract methods.
 * 2. Runtime Type Determination:
 *    - Objects behave according to their actual type at runtime.
 *    - Method resolution happens dynamically (late binding).
 * 3. Type Handling:
 *    - Different numeric types (int, double) and their conversions.
 *    - Parsing string representations of numbers.
 * 4. Design Patterns:
 *    - Factory Method: Creating appropriate number type objects based on input.
 * 5. Exception Handling:
 *    - Handling invalid inputs and type conversion errors.
 * 6. Scanner: Reading user input for numbers and type selection.
 * 7. Efficiency:
 *    - Method binding occurs at runtime, not compile time.
 *    - Appropriate type conversions maintain precision.
 *
 * CLASS HIERARCHY:
 * 1. Number (Abstract Base Class):
 *    - Contains an abstract method add() that takes another Number object.
 *    - Defines getValue() to access the underlying value.
 *    - Implements a displayType() method to show the runtime type.
 *
 * 2. IntegerNumber (Derived Class):
 *    - Stores an integer value.
 *    - Overrides add() to handle addition with different number types.
 *    - Returns a new Number object of the appropriate type after addition.
 *
 * 3. DoubleNumber (Derived Class):
 *    - Stores a double value.
 *    - Overrides add() to handle addition with different number types.
 *    - Returns a new Number object of the appropriate type after addition.
 *
 * 4. StringNumber (Derived Class):
 *    - Stores a numeric string and its parsed value.
 *    - Overrides add() to handle addition with different number types.
 *    - Returns a new Number object of the appropriate type after addition.
 *
 * 5. NumberFactory:
 *    - Creates appropriate Number objects based on type and value inputs.
 *    - Handles error checking and validation.
 *
 * ALGORITHM:
 * 1. Define the abstract base class Number with abstract method add().
 * 2. Implement derived classes for different number types with their own add() implementations.
 * 3. Create a NumberFactory to instantiate the appropriate Number objects.
 * 4. In main():
 *    - Ask user to input two numbers and their types.
 *    - Create Number objects using the factory.
 *    - Call add() on the first number with the second number as parameter.
 *    - Display the result and the dynamic type information.
 *
 * WHY THIS APPROACH:
 * - Demonstrates true runtime polymorphism through method overriding.
 * - Enables addition of different number types without extensive if-else statements.
 * - Follows OOP principles of inheritance and polymorphism.
 * - Provides extensibility: New number types can be added by creating new derived classes.
 * - Educational value: Clearly shows how runtime type determination works in Java.
 */

// Abstract base class for all number types
abstract class Number {
    // Abstract method to add another number, returns a new Number object
    public abstract Number add(Number other);
    
    // Abstract method to get the numeric value
    public abstract Object getValue();
    
    // Display the runtime type of this number
    public String displayType() {
        return this.getClass().getSimpleName();
    }
    
    // Override toString() for better display
    @Override
    public abstract String toString();
}

// Class for integer numbers
class IntegerNumber extends Number {
    private int value;
    
    public IntegerNumber(int value) {
        this.value = value;
    }
    
    @Override
    public Number add(Number other) {
        if (other instanceof IntegerNumber) {
            // If other is also an integer, return an integer sum
            return new IntegerNumber(this.value + ((IntegerNumber) other).value);
        } else if (other instanceof DoubleNumber) {
            // If other is a double, convert to double and return a double sum
            return new DoubleNumber(this.value + (double)((DoubleNumber) other).getValue());
        } else if (other instanceof StringNumber) {
            // If other is a string number, use its numeric value for addition
            Object otherValue = other.getValue();
            if (otherValue instanceof Integer) {
                return new IntegerNumber(this.value + (Integer)otherValue);
            } else if (otherValue instanceof Double) {
                return new DoubleNumber(this.value + (Double)otherValue);
            }
        }
        // Default fallback if something unexpected happens
        return this;
    }
    
    @Override
    public Integer getValue() {
        return value;
    }
    
    @Override
    public String toString() {
        return Integer.toString(value);
    }
}

// Class for double numbers
class DoubleNumber extends Number {
    private double value;
    
    public DoubleNumber(double value) {
        this.value = value;
    }
    
    @Override
    public Number add(Number other) {
        if (other instanceof IntegerNumber) {
            // If other is an integer, convert it to double and add
            return new DoubleNumber(this.value + (int)((IntegerNumber) other).getValue());
        } else if (other instanceof DoubleNumber) {
            // If other is also a double, add directly
            return new DoubleNumber(this.value + ((DoubleNumber) other).value);
        } else if (other instanceof StringNumber) {
            // If other is a string number, use its numeric value for addition
            Object otherValue = other.getValue();
            if (otherValue instanceof Integer) {
                return new DoubleNumber(this.value + (Integer)otherValue);
            } else if (otherValue instanceof Double) {
                return new DoubleNumber(this.value + (Double)otherValue);
            }
        }
        // Default fallback
        return this;
    }
    
    @Override
    public Double getValue() {
        return value;
    }
    
    @Override
    public String toString() {
        return Double.toString(value);
    }
}

// Class for string-based numbers (can be parsed to int or double)
class StringNumber extends Number {
    private String strValue;
    private Object numericValue;  // Can be Integer or Double
    
    public StringNumber(String value) throws NumberFormatException {
        this.strValue = value;
        
        // Try to parse as int first, then as double if that fails
        try {
            this.numericValue = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            // Not an integer, try as double
            try {
                this.numericValue = Double.parseDouble(value);
            } catch (NumberFormatException e2) {
                // Not a valid number
                throw new NumberFormatException("Invalid number format: " + value);
            }
        }
    }
    
    @Override
    public Number add(Number other) {
        if (numericValue instanceof Integer) {
            // If this string is an integer, create IntegerNumber and delegate
            IntegerNumber intNum = new IntegerNumber((Integer)numericValue);
            return intNum.add(other);
        } else if (numericValue instanceof Double) {
            // If this string is a double, create DoubleNumber and delegate
            DoubleNumber doubleNum = new DoubleNumber((Double)numericValue);
            return doubleNum.add(other);
        }
        // Should never reach here if constructor validation works properly
        return this;
    }
    
    @Override
    public Object getValue() {
        return numericValue;
    }
    
    @Override
    public String toString() {
        return strValue + " (parsed as " + numericValue + ")";
    }
}

// Factory class to create the appropriate Number objects
class NumberFactory {
    // Type constants
    public static final int INTEGER_TYPE = 1;
    public static final int DOUBLE_TYPE = 2;
    public static final int STRING_TYPE = 3;
    
    // Create a Number object based on type and string input
    public static Number createNumber(int type, String input) throws NumberFormatException, IllegalArgumentException {
        switch (type) {
            case INTEGER_TYPE:
                try {
                    int value = Integer.parseInt(input);
                    return new IntegerNumber(value);
                } catch (NumberFormatException e) {
                    throw new NumberFormatException("Cannot parse '" + input + "' as an integer.");
                }
                
            case DOUBLE_TYPE:
                try {
                    double value = Double.parseDouble(input);
                    return new DoubleNumber(value);
                } catch (NumberFormatException e) {
                    throw new NumberFormatException("Cannot parse '" + input + "' as a double.");
                }
                
            case STRING_TYPE:
                return new StringNumber(input);
                
            default:
                throw new IllegalArgumentException("Invalid number type: " + type);
        }
    }
    
    // Get a user-friendly type name
    public static String getTypeName(int type) {
        switch (type) {
            case INTEGER_TYPE: return "Integer";
            case DOUBLE_TYPE: return "Double";
            case STRING_TYPE: return "String";
            default: return "Unknown";
        }
    }
}

// Main class
public class PolymorphicAdder {
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        try {
            System.out.println("=== Polymorphic Number Adder ===");
            System.out.println("This program demonstrates runtime polymorphism by adding numbers of different types.");
            
            // Get first number
            System.out.println("\n--- First Number ---");
            Number num1 = getNumberFromUser(scanner);
            
            // Get second number
            System.out.println("\n--- Second Number ---");
            Number num2 = getNumberFromUser(scanner);
            
            // Perform addition using polymorphism
            System.out.println("\n--- Result using Polymorphism ---");
            System.out.println("First number: " + num1 + " (Type: " + num1.displayType() + ")");
            System.out.println("Second number: " + num2 + " (Type: " + num2.displayType() + ")");
            
            Number result = num1.add(num2);
            System.out.println("Sum: " + result + " (Result Type: " + result.displayType() + ")");
            
            // Explain the runtime polymorphism that occurred
            explainPolymorphism(num1, num2, result);
            
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
    
    // Helper method to get a number from the user
    private static Number getNumberFromUser(Scanner scanner) {
        while (true) {
            try {
                System.out.println("Select the number type:");
                System.out.println("1. Integer");
                System.out.println("2. Double");
                System.out.println("3. String (will be parsed to a number)");
                System.out.print("Enter your choice (1-3): ");
                
                if (!scanner.hasNextInt()) {
                    scanner.next(); // consume invalid input
                    System.out.println("Invalid input. Please enter a number between 1 and 3.");
                    continue;
                }
                
                int typeChoice = scanner.nextInt();
                scanner.nextLine(); // consume newline
                
                if (typeChoice < 1 || typeChoice > 3) {
                    System.out.println("Invalid choice. Please enter a number between 1 and 3.");
                    continue;
                }
                
                System.out.print("Enter the " + NumberFactory.getTypeName(typeChoice) + " value: ");
                String input = scanner.nextLine().trim();
                
                return NumberFactory.createNumber(typeChoice, input);
                
            } catch (NumberFormatException e) {
                System.out.println("Error: " + e.getMessage() + " Please try again.");
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage() + " Please try again.");
            }
        }
    }
    
    // Explains the polymorphism that occurred during addition
    private static void explainPolymorphism(Number num1, Number num2, Number result) {
        System.out.println("\n--- How Runtime Polymorphism Worked ---");
        System.out.println("1. We created objects of different types that all extend the abstract 'Number' class.");
        System.out.println("2. When we called num1.add(num2), the JVM determined at runtime that num1 is a " + 
                         num1.displayType() + " object.");
        System.out.println("3. Since each subclass overrides the 'add' method, the " + num1.displayType() + 
                         " version of 'add' was called.");
        System.out.println("4. Inside that method, it checked the type of num2 (" + num2.displayType() + 
                         ") to determine how to add the values.");
        System.out.println("5. The method then returned a new Number object of type " + result.displayType() + 
                         " containing the sum.");
        System.out.println("6. This demonstrates polymorphism because the same method call (add) behaved differently");
        System.out.println("   based on the actual types of the objects involved, determined at runtime.");
        
        System.out.println("\nKey Points About Runtime Polymorphism:");
        System.out.println("- Method binding happens at runtime, not compile time");
        System.out.println("- The behavior depends on the actual object type, not the reference type");
        System.out.println("- It enables writing flexible and extensible code");
        System.out.println("- It's one of the fundamental principles of object-oriented programming");
    }
}
