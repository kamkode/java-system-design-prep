package BlitzenxIntervierwQnA;

import java.util.Scanner;

/**
 * Question 7: Compute sum of 2 numbers irrespective of type using runtime polymorphism
 * 
 * This program demonstrates runtime polymorphism by creating a hierarchy of number types
 * that can be added together regardless of their specific types.
 */
public class PolymorphicAdder {
    
    /**
     * Abstract base class for different number types
     */
    public static abstract class Number {
        // Abstract method to add another number
        public abstract Number add(Number other);
        
        // Abstract method to get string representation
        public abstract String toString();
        
        // Abstract method to get numeric value
        public abstract double getValue();
    }
    
    /**
     * Integer number implementation
     */
    public static class IntegerNumber extends Number {
        private int value;
        
        public IntegerNumber(int value) {
            this.value = value;
        }
        
        @Override
        public Number add(Number other) {
            // If other is IntegerNumber and result fits in int, return IntegerNumber
            if (other instanceof IntegerNumber) {
                long result = (long) value + (long) ((IntegerNumber) other).value;
                if (result >= Integer.MIN_VALUE && result <= Integer.MAX_VALUE) {
                    return new IntegerNumber((int) result);
                }
            }
            
            // Otherwise, return DoubleNumber
            return new DoubleNumber(this.getValue() + other.getValue());
        }
        
        @Override
        public String toString() {
            return Integer.toString(value);
        }
        
        @Override
        public double getValue() {
            return value;
        }
    }
    
    /**
     * Double number implementation
     */
    public static class DoubleNumber extends Number {
        private double value;
        
        public DoubleNumber(double value) {
            this.value = value;
        }
        
        @Override
        public Number add(Number other) {
            return new DoubleNumber(this.value + other.getValue());
        }
        
        @Override
        public String toString() {
            return Double.toString(value);
        }
        
        @Override
        public double getValue() {
            return value;
        }
    }
    
    /**
     * String number implementation (parses string to number)
     */
    public static class StringNumber extends Number {
        private String strValue;
        private double numericValue;
        
        public StringNumber(String value) {
            this.strValue = value;
            try {
                // Try to parse as integer first
                this.numericValue = Integer.parseInt(value);
            } catch (NumberFormatException e) {
                try {
                    // Try to parse as double
                    this.numericValue = Double.parseDouble(value);
                } catch (NumberFormatException ex) {
                    throw new IllegalArgumentException("Cannot parse '" + value + "' as a number");
                }
            }
        }
        
        @Override
        public Number add(Number other) {
            double result = this.numericValue + other.getValue();
            
            // Check if result is an integer
            if (result == (int) result) {
                if (result >= Integer.MIN_VALUE && result <= Integer.MAX_VALUE) {
                    return new IntegerNumber((int) result);
                }
            }
            
            return new DoubleNumber(result);
        }
        
        @Override
        public String toString() {
            return strValue + " (numeric value: " + numericValue + ")";
        }
        
        @Override
        public double getValue() {
            return numericValue;
        }
    }
    
    /**
     * Factory to create appropriate Number objects based on input
     */
    public static class NumberFactory {
        public static Number createNumber(String input) {
            try {
                // Try to parse as integer first
                int intValue = Integer.parseInt(input);
                return new IntegerNumber(intValue);
            } catch (NumberFormatException e) {
                try {
                    // Try to parse as double
                    double doubleValue = Double.parseDouble(input);
                    return new DoubleNumber(doubleValue);
                } catch (NumberFormatException ex) {
                    // Treat as string number
                    return new StringNumber(input);
                }
            }
        }
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Polymorphic Number Adder");
        System.out.println("------------------------");
        
        try {
            // Get first number
            System.out.print("\nEnter first number: ");
            String input1 = scanner.nextLine();
            
            // Get second number
            System.out.print("Enter second number: ");
            String input2 = scanner.nextLine();
            
            // Create Number objects using factory
            Number num1 = NumberFactory.createNumber(input1);
            Number num2 = NumberFactory.createNumber(input2);
            
            // Add numbers
            Number result = num1.add(num2);
            
            // Display results
            System.out.println("\nFirst number: " + num1 + " (type: " + num1.getClass().getSimpleName() + ")");
            System.out.println("Second number: " + num2 + " (type: " + num2.getClass().getSimpleName() + ")");
            System.out.println("Sum: " + result + " (type: " + result.getClass().getSimpleName() + ")");
            
            // Explain polymorphism
            System.out.println("\nPolymorphism Explanation:");
            System.out.println("1. We created an abstract Number class with an add() method");
            System.out.println("2. Different number types (Integer, Double, String) extend this class");
            System.out.println("3. Each subclass implements add() differently (runtime polymorphism)");
            System.out.println("4. The correct implementation is chosen at runtime based on object type");
            System.out.println("5. This allows adding numbers of different types without explicit type checking");
            System.out.println("\nBenefits of this approach:");
            System.out.println("- Extensible: New number types can be added without changing existing code");
            System.out.println("- Type-safe: Each number type handles its own conversion logic");
            System.out.println("- Maintainable: Single responsibility principle is followed");
            
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}
