# Exception Handling Interview Questions and Answers

This document contains common interview questions related to exception handling in Java, including try-catch blocks, exception types, custom exceptions, and best practices.

## Table of Contents
1. [Exception Basics](#q1-what-is-an-exception-in-java)
2. [Exception Hierarchy](#q2-explain-the-exception-hierarchy-in-java)
3. [Checked vs Unchecked Exceptions](#q3-what-is-the-difference-between-checked-and-unchecked-exceptions)
4. [Try-Catch-Finally](#q4-explain-the-try-catch-finally-block-in-java)
5. [Multiple Catch Blocks](#q5-how-do-you-handle-multiple-exceptions-in-java)
6. [Try-with-Resources](#q6-what-is-try-with-resources-in-java)
7. [Throw and Throws](#q7-what-is-the-difference-between-throw-and-throws-in-java)
8. [Custom Exceptions](#q8-how-do-you-create-custom-exceptions-in-java)
9. [Exception Chaining](#q9-what-is-exception-chaining-in-java)
10. [Best Practices](#q10-what-are-the-best-practices-for-exception-handling-in-java)

### Q1: What is an exception in Java?

An exception in Java is an event that disrupts the normal flow of program execution. It represents an error condition that occurs during the execution of a program.

**Key characteristics of exceptions**:

1. **Object-Based**: Exceptions in Java are objects that are instances of classes derived from the `Throwable` class.
2. **Runtime Errors**: They represent runtime errors, not compilation errors.
3. **Propagation**: If not handled, exceptions propagate up the call stack until they are caught or reach the main method.
4. **Recovery Mechanism**: They provide a way to transfer control from one part of a program to another.

**Common scenarios that cause exceptions**:

1. Invalid user input
2. Device failure or loss of connection
3. Opening an unavailable file
4. Memory exhaustion
5. Programming errors (like array index out of bounds)
6. Arithmetic errors (like division by zero)

Example of an exception:
```java
public class ExceptionExample {
    public static void main(String[] args) {
        try {
            // This will throw an ArithmeticException
            int result = 10 / 0;
            System.out.println("Result: " + result);  // This line won't execute
        } catch (ArithmeticException e) {
            System.out.println("Exception caught: " + e.getMessage());
        }
        
        System.out.println("Program continues execution...");
    }
}
```

Output:
```
Exception caught: / by zero
Program continues execution...
```

### Q2: Explain the exception hierarchy in Java.

In Java, all exceptions are derived from the `Throwable` class, which is the root class of the exception hierarchy. The exception hierarchy is organized as follows:

**1. Throwable**:
   - The root class of all exceptions and errors
   - Contains methods like `getMessage()`, `printStackTrace()`, and `getStackTrace()`

**2. Error**:
   - Subclass of `Throwable`
   - Represents serious problems that a reasonable application should not try to catch
   - Examples: `OutOfMemoryError`, `StackOverflowError`, `VirtualMachineError`

**3. Exception**:
   - Subclass of `Throwable`
   - Represents conditions that a reasonable application might want to catch
   - Further divided into two categories:

     a. **Checked Exceptions**:
     - Direct subclasses of `Exception` (excluding `RuntimeException` and its subclasses)
     - Must be either caught or declared in the method signature using the `throws` clause
     - Examples: `IOException`, `SQLException`, `ClassNotFoundException`

     b. **Unchecked Exceptions (Runtime Exceptions)**:
     - Subclasses of `RuntimeException`
     - Do not need to be explicitly caught or declared
     - Examples: `NullPointerException`, `ArrayIndexOutOfBoundsException`, `ArithmeticException`

Visual representation of the hierarchy:
```
Throwable
├── Error
│   ├── OutOfMemoryError
│   ├── StackOverflowError
│   └── ...
└── Exception
    ├── IOException
    ├── SQLException
    ├── ClassNotFoundException
    ├── ... (other checked exceptions)
    └── RuntimeException
        ├── NullPointerException
        ├── ArithmeticException
        ├── IllegalArgumentException
        ├── IndexOutOfBoundsException
        │   ├── ArrayIndexOutOfBoundsException
        │   └── StringIndexOutOfBoundsException
        └── ... (other unchecked exceptions)
```

Example demonstrating different types of exceptions:
```java
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class ExceptionHierarchyExample {
    public static void main(String[] args) {
        // Unchecked exception (RuntimeException)
        try {
            int[] arr = new int[5];
            arr[10] = 50;  // ArrayIndexOutOfBoundsException
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Unchecked exception: " + e.getMessage());
        }
        
        // Checked exception
        try {
            File file = new File("nonexistent.txt");
            FileInputStream fis = new FileInputStream(file);  // FileNotFoundException
        } catch (FileNotFoundException e) {
            System.out.println("Checked exception: " + e.getMessage());
        }
        
        // Error (not typically caught)
        try {
            // This would cause an OutOfMemoryError if uncommented
            // int[] bigArray = new int[Integer.MAX_VALUE];
            
            // Instead, let's simulate an Error for demonstration
            throw new StackOverflowError("Simulated error");
        } catch (Error e) {
            System.out.println("Error: " + e.getMessage());
            // In real applications, you typically shouldn't catch Errors
        }
    }
}
```

### Q3: What is the difference between checked and unchecked exceptions?

In Java, exceptions are categorized into two main types: checked exceptions and unchecked exceptions. They differ in how they are handled and when they are checked by the compiler.

**Checked Exceptions**:

1. **Compile-time Checking**: The compiler checks at compile-time that these exceptions are either caught or declared in the method signature.
2. **Declaration Requirement**: Methods that might throw checked exceptions must declare them using the `throws` clause.
3. **Handling Requirement**: Code that calls methods throwing checked exceptions must either catch them or propagate them.
4. **Class Hierarchy**: All exceptions that are subclasses of `Exception` but not subclasses of `RuntimeException`.
5. **Purpose**: Represent conditions that a reasonable application might want to catch, often external to the program (like I/O errors).
6. **Examples**: `IOException`, `SQLException`, `ClassNotFoundException`, `InterruptedException`.

**Unchecked Exceptions (Runtime Exceptions)**:

1. **Runtime Checking**: Not checked at compile-time; only detected at runtime.
2. **Declaration Optional**: Methods are not required to declare unchecked exceptions they might throw.
3. **Handling Optional**: Callers are not required to catch or declare unchecked exceptions.
4. **Class Hierarchy**: All exceptions that are subclasses of `RuntimeException`.
5. **Purpose**: Often represent programming errors or unexpected conditions that are internal to the application.
6. **Examples**: `NullPointerException`, `ArrayIndexOutOfBoundsException`, `ArithmeticException`, `IllegalArgumentException`.

**Comparison Table**:

| Feature | Checked Exceptions | Unchecked Exceptions |
|---------|-------------------|----------------------|
| Compiler Check | Yes | No |
| Must Declare | Yes | No |
| Must Handle | Yes | No |
| Recovery Possible | Often | Rarely |
| Base Class | `Exception` (excluding `RuntimeException`) | `RuntimeException` |
| Common Causes | External factors (file not found, network issues) | Programming errors (null reference, invalid index) |

Example demonstrating both types:
```java
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class CheckedVsUncheckedExample {
    
    // Method declaring a checked exception
    public static void readFile(String filename) throws FileNotFoundException, IOException {
        FileInputStream fis = new FileInputStream(filename);
        fis.read();
        fis.close();
    }
    
    // Method throwing an unchecked exception (no declaration needed)
    public static int divide(int a, int b) {
        return a / b;  // Throws ArithmeticException if b is 0
    }
    
    public static void main(String[] args) {
        // Handling checked exception
        try {
            readFile("nonexistent.txt");
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO error: " + e.getMessage());
        }
        
        // Handling unchecked exception (optional but good practice)
        try {
            int result = divide(10, 0);
            System.out.println("Result: " + result);
        } catch (ArithmeticException e) {
            System.out.println("Arithmetic error: " + e.getMessage());
        }
    }
}
```

### Q4: Explain the try-catch-finally block in Java.

The try-catch-finally block is a fundamental construct in Java for exception handling. It allows you to write code that might throw exceptions and provide handlers for those exceptions, as well as cleanup code that executes regardless of whether an exception occurs.

**Structure and Purpose**:

1. **try block**:
   - Contains the code that might throw an exception
   - Must be followed by either a catch block or a finally block (or both)

2. **catch block**:
   - Catches and handles exceptions thrown in the try block
   - Multiple catch blocks can be used to handle different types of exceptions
   - Each catch block specifies the type of exception it can handle
   - Executed only if an exception of the specified type occurs

3. **finally block**:
   - Contains code that always executes, whether an exception occurs or not
   - Typically used for cleanup operations (closing resources, etc.)
   - Executes even if the try or catch block contains a return statement
   - The only scenarios where finally doesn't execute are:
     - If the JVM exits (e.g., System.exit() is called)
     - If a thread is killed
     - If an exception occurs in the finally block itself

**Basic Syntax**:
```java
try {
    // Code that might throw an exception
} catch (ExceptionType1 e1) {
    // Handler for ExceptionType1
} catch (ExceptionType2 e2) {
    // Handler for ExceptionType2
} finally {
    // Code that always executes (cleanup code)
}
```

**Example**:
```java
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class TryCatchFinallyExample {
    public static void main(String[] args) {
        FileInputStream fis = null;
        
        try {
            // Code that might throw an exception
            fis = new FileInputStream("example.txt");
            int data = fis.read();
            while (data != -1) {
                System.out.print((char) data);
                data = fis.read();
            }
        } catch (FileNotFoundException e) {
            // Handler for FileNotFoundException
            System.out.println("File not found: " + e.getMessage());
        } catch (IOException e) {
            // Handler for IOException
            System.out.println("Error reading file: " + e.getMessage());
        } finally {
            // Cleanup code that always executes
            try {
                if (fis != null) {
                    fis.close();
                    System.out.println("\nFile closed successfully");
                }
            } catch (IOException e) {
                System.out.println("Error closing file: " + e.getMessage());
            }
        }
        
        System.out.println("Program continues execution...");
    }
}
```

**Control Flow in try-catch-finally**:

1. **Normal execution (no exception)**:
   - Execute try block completely
   - Skip all catch blocks
   - Execute finally block
   - Continue with the code after the try-catch-finally

2. **Exception occurs and is caught**:
   - Execute try block until exception occurs
   - Jump to the appropriate catch block
   - Execute the catch block
   - Execute finally block
   - Continue with the code after the try-catch-finally

3. **Exception occurs but is not caught**:
   - Execute try block until exception occurs
   - Execute finally block
   - Propagate the exception to the caller

4. **Return statement in try or catch**:
   - Execute finally block before actually returning
   - The return value is determined before finally executes

Example demonstrating return in try-catch-finally:
```java
public class ReturnInTryCatchFinally {
    public static int test() {
        try {
            System.out.println("In try block");
            return 1;
        } catch (Exception e) {
            System.out.println("In catch block");
            return 2;
        } finally {
            System.out.println("In finally block");
            // This doesn't change the return value from try
        }
    }
    
    public static void main(String[] args) {
        int result = test();
        System.out.println("Result: " + result);
    }
}
```

Output:
```
In try block
In finally block
Result: 1
```

### Q5: How do you handle multiple exceptions in Java?

In Java, there are several ways to handle multiple exceptions that might occur in a try block:

**1. Multiple catch blocks**:
- The most traditional approach
- Each catch block handles a specific type of exception
- Catch blocks are evaluated in order, so more specific exceptions should come before more general ones

```java
try {
    // Code that might throw different exceptions
    int[] array = new int[5];
    array[10] = 30 / 0;  // Could throw ArithmeticException or ArrayIndexOutOfBoundsException
} catch (ArithmeticException e) {
    System.out.println("Arithmetic Exception: " + e.getMessage());
} catch (ArrayIndexOutOfBoundsException e) {
    System.out.println("Array Index Out Of Bounds: " + e.getMessage());
} catch (Exception e) {
    System.out.println("Generic Exception: " + e.getMessage());
}
```

**2. Multi-catch block (Java 7+)**:
- Allows catching multiple exception types in a single catch block
- Uses the pipe symbol (|) to separate exception types
- Cannot use hierarchically related exception types (e.g., IOException | Exception is invalid)
- The exception parameter is effectively final

```java
try {
    // Code that might throw different exceptions
    int[] array = new int[5];
    array[10] = 30 / 0;
} catch (ArithmeticException | ArrayIndexOutOfBoundsException e) {
    System.out.println("Either arithmetic or array index exception: " + e.getMessage());
} catch (Exception e) {
    System.out.println("Generic Exception: " + e.getMessage());
}
```

**3. Catching the base Exception class**:
- Catches all exceptions that are subclasses of Exception
- Less specific, so you lose information about the exact exception type
- Generally not recommended unless you plan to handle all exceptions the same way

```java
try {
    // Code that might throw different exceptions
    int[] array = new int[5];
    array[10] = 30 / 0;
} catch (Exception e) {
    System.out.println("An exception occurred: " + e.getMessage());
    e.printStackTrace();
}
```

**4. Exception hierarchy and polymorphism**:
- You can use the exception hierarchy to your advantage
- Catch specific exceptions first, then more general ones
- This allows for different handling based on the exception type

```java
try {
    // Code that might throw different exceptions
    FileInputStream file = new FileInputStream("nonexistent.txt");
    int x = 10 / 0;
} catch (FileNotFoundException e) {
    // Specific handling for file not found
    System.out.println("File not found: " + e.getMessage());
} catch (IOException e) {
    // Handling for any other IO exception
    System.out.println("IO error: " + e.getMessage());
} catch (ArithmeticException e) {
    // Handling for arithmetic exceptions
    System.out.println("Arithmetic error: " + e.getMessage());
} catch (Exception e) {
    // Fallback for any other exception
    System.out.println("Other exception: " + e.getMessage());
}
```

**5. Rethrowing exceptions**:
- You can catch an exception, perform some action, and then rethrow it
- Useful when you want to log the exception but let the caller handle it
- Can also rethrow a different exception (exception chaining)

```java
try {
    // Code that might throw different exceptions
    processFile("data.txt");
} catch (IOException e) {
    System.out.println("Logging IO exception: " + e.getMessage());
    throw e;  // Rethrow the same exception
} catch (Exception e) {
    System.out.println("Logging general exception: " + e.getMessage());
    throw new RuntimeException("A problem occurred", e);  // Wrap and rethrow
}
```

**Complete example demonstrating multiple exception handling techniques**:

```java
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MultipleExceptionHandlingExample {
    
    public static void main(String[] args) {
        // Example 1: Multiple catch blocks
        System.out.println("Example 1: Multiple catch blocks");
        try {
            int result = divide(10, 0);
            System.out.println("Result: " + result);
        } catch (ArithmeticException e) {
            System.out.println("Arithmetic error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Generic error: " + e.getMessage());
        }
        
        // Example 2: Multi-catch block
        System.out.println("\nExample 2: Multi-catch block");
        try {
            int[] array = new int[5];
            if (Math.random() > 0.5) {
                array[10] = 100;  // ArrayIndexOutOfBoundsException
            } else {
                int result = 10 / 0;  // ArithmeticException
            }
        } catch (ArithmeticException | ArrayIndexOutOfBoundsException e) {
            System.out.println("Either arithmetic or array index error: " + e.getMessage());
        }
        
        // Example 3: Exception hierarchy
        System.out.println("\nExample 3: Exception hierarchy");
        try {
            readFile("nonexistent.txt");
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO error: " + e.getMessage());
        }
    }
    
    public static int divide(int a, int b) {
        return a / b;
    }
    
    public static void readFile(String filename) throws IOException {
        FileInputStream fis = new FileInputStream(filename);
        int data = fis.read();
        fis.close();
    }
}
```

### Q6: What is try-with-resources in Java?

Try-with-resources is a feature introduced in Java 7 that simplifies the handling of resources that need to be closed after use. It automatically closes resources that implement the `AutoCloseable` or `Closeable` interface, eliminating the need for explicit cleanup in a finally block.

**Key features of try-with-resources**:

1. **Automatic Resource Management**:
   - Resources are automatically closed at the end of the try block
   - No need for explicit finally blocks to close resources
   - Resources are closed in the reverse order of their creation

2. **Cleaner Code**:
   - Reduces boilerplate code
   - Makes code more readable and maintainable
   - Reduces the risk of resource leaks

3. **Improved Exception Handling**:
   - If both the try block and the automatic close method throw exceptions, the exception from the try block is thrown and the exception from close is suppressed
   - Suppressed exceptions can be retrieved using the `getSuppressed()` method

**Basic Syntax**:
```java
try (Resource resource = new Resource()) {
    // Use the resource
} catch (Exception e) {
    // Handle exceptions
}
```

**Multiple resources**:
```java
try (Resource1 resource1 = new Resource1();
     Resource2 resource2 = new Resource2()) {
    // Use the resources
}
```

**Example with FileInputStream and FileOutputStream**:
```java
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class TryWithResourcesExample {
    public static void main(String[] args) {
        // Copy file using try-with-resources
        try (FileInputStream fis = new FileInputStream("source.txt");
             FileOutputStream fos = new FileOutputStream("destination.txt")) {
            
            byte[] buffer = new byte[1024];
            int length;
            
            // Read from source and write to destination
            while ((length = fis.read(buffer)) > 0) {
                fos.write(buffer, 0, length);
            }
            
            System.out.println("File copied successfully");
            
        } catch (IOException e) {
            System.out.println("Error copying file: " + e.getMessage());
        }
        // No finally block needed - resources are automatically closed
    }
}
```

**Comparison with traditional try-catch-finally**:

Traditional approach:
```java
FileInputStream fis = null;
FileOutputStream fos = null;
try {
    fis = new FileInputStream("source.txt");
    fos = new FileOutputStream("destination.txt");
    
    byte[] buffer = new byte[1024];
    int length;
    
    while ((length = fis.read(buffer)) > 0) {
        fos.write(buffer, 0, length);
    }
    
    System.out.println("File copied successfully");
    
} catch (IOException e) {
    System.out.println("Error copying file: " + e.getMessage());
} finally {
    try {
        if (fis != null) {
            fis.close();
        }
    } catch (IOException e) {
        System.out.println("Error closing input stream: " + e.getMessage());
    }
    
    try {
        if (fos != null) {
            fos.close();
        }
    } catch (IOException e) {
        System.out.println("Error closing output stream: " + e.getMessage());
    }
}
```

**Creating custom AutoCloseable resources**:
```java
class DatabaseConnection implements AutoCloseable {
    public DatabaseConnection() {
        System.out.println("Database connection opened");
    }
    
    public void executeQuery(String query) {
        System.out.println("Executing query: " + query);
    }
    
    @Override
    public void close() throws Exception {
        System.out.println("Database connection closed");
    }
}

public class CustomResourceExample {
    public static void main(String[] args) {
        try (DatabaseConnection connection = new DatabaseConnection()) {
            connection.executeQuery("SELECT * FROM users");
        } catch (Exception e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }
}
```

**Handling suppressed exceptions**:
```java
class Resource implements AutoCloseable {
    private final String name;
    
    public Resource(String name) {
        this.name = name;
        System.out.println(name + " opened");
    }
    
    public void use() throws Exception {
        System.out.println(name + " used");
        if (name.equals("Resource2")) {
            throw new Exception(name + " failed during use");
        }
    }
    
    @Override
    public void close() throws Exception {
        System.out.println(name + " closing");
        throw new Exception(name + " failed during close");
    }
}

public class SuppressedExceptionExample {
    public static void main(String[] args) {
        try (Resource r1 = new Resource("Resource1");
             Resource r2 = new Resource("Resource2")) {
            
            r1.use();
            r2.use();
            
        } catch (Exception e) {
            System.out.println("Main exception: " + e.getMessage());
            
            // Get suppressed exceptions
            Throwable[] suppressed = e.getSuppressed();
            for (Throwable t : suppressed) {
                System.out.println("Suppressed: " + t.getMessage());
            }
        }
    }
}
```

### Q7: What is the difference between throw and throws in Java?

In Java, both `throw` and `throws` are related to exception handling but serve different purposes:

**1. throw**:
- Used to explicitly throw an exception within a method or block
- Followed by an instance of an exception class
- Can throw both checked and unchecked exceptions
- Used inside the method body

**2. throws**:
- Used in method declaration to indicate that the method might throw certain exceptions
- Followed by one or more exception class names (separated by commas)
- Primarily used for checked exceptions (though can list unchecked exceptions too)
- Used in the method signature

**Key Differences**:

| Feature | throw | throws |
|---------|-------|--------|
| Purpose | Explicitly throws an exception | Declares exceptions that a method might throw |
| Usage | Inside method body | In method signature |
| Syntax | `throw exceptionObject;` | `returnType methodName() throws ExceptionType1, ExceptionType2 {...}` |
| Number of exceptions | Can throw only one exception at a time | Can declare multiple exceptions |
| Instance vs. Class | Requires an instance of an exception | Uses exception class names |
| Handling | Creates an exception to be handled | Delegates exception handling to the caller |

**Examples**:

Using `throw`:
```java
public void validateAge(int age) {
    if (age < 0) {
        throw new IllegalArgumentException("Age cannot be negative");
    }
    if (age > 120) {
        throw new IllegalArgumentException("Age seems too high");
    }
    System.out.println("Age is valid: " + age);
}
```

Using `throws`:
```java
public void readFile(String filename) throws FileNotFoundException, IOException {
    FileInputStream file = new FileInputStream(filename);  // Might throw FileNotFoundException
    int data = file.read();  // Might throw IOException
    file.close();
}
```

Using both together:
```java
public void processFile(String filename) throws IOException {
    if (filename == null || filename.isEmpty()) {
        throw new IllegalArgumentException("Filename cannot be null or empty");
    }
    
    FileInputStream file = null;
    try {
        file = new FileInputStream(filename);
        // Process file...
    } finally {
        if (file != null) {
            file.close();
        }
    }
}
```

**Complete example demonstrating both**:
```java
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ThrowVsThrowsExample {
    
    // Method that declares exceptions using 'throws'
    public static void readFile(String filename) throws FileNotFoundException, IOException {
        if (filename == null) {
            // Using 'throw' to explicitly throw an exception
            throw new IllegalArgumentException("Filename cannot be null");
        }
        
        FileInputStream file = new FileInputStream(filename);
        try {
            int data = file.read();
            while (data != -1) {
                System.out.print((char) data);
                data = file.read();
            }
        } finally {
            file.close();
        }
    }
    
    public static void main(String[] args) {
        try {
            // This will throw IllegalArgumentException
            readFile(null);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid argument: " + e.getMessage());
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO error: " + e.getMessage());
        }
        
        try {
            // This will throw FileNotFoundException
            readFile("nonexistent.txt");
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid argument: " + e.getMessage());
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO error: " + e.getMessage());
        }
    }
}
```

### Q8: How do you create custom exceptions in Java?

Creating custom exceptions in Java allows you to define application-specific error conditions and provide meaningful error messages and handling. Custom exceptions can make your code more readable and maintainable by clearly communicating what went wrong.

**Steps to create a custom exception**:

1. **Decide the exception type**:
   - Extend `Exception` for checked exceptions
   - Extend `RuntimeException` for unchecked exceptions

2. **Create the exception class**:
   - Provide constructors that match the parent class
   - Add any additional functionality needed

3. **Use the custom exception in your code**:
   - Throw the exception when appropriate
   - Catch and handle it as needed

**Basic custom exception example**:
```java
// Custom checked exception
public class InsufficientFundsException extends Exception {
    // Default constructor
    public InsufficientFundsException() {
        super();
    }
    
    // Constructor with message
    public InsufficientFundsException(String message) {
        super(message);
    }
    
    // Constructor with message and cause
    public InsufficientFundsException(String message, Throwable cause) {
        super(message, cause);
    }
}
```

**Using the custom exception**:
```java
public class BankAccount {
    private String accountNumber;
    private double balance;
    
    public BankAccount(String accountNumber, double initialBalance) {
        this.accountNumber = accountNumber;
        this.balance = initialBalance;
    }
    
    public void withdraw(double amount) throws InsufficientFundsException {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }
        
        if (amount > balance) {
            throw new InsufficientFundsException("Insufficient funds: current balance is " + balance + ", but attempted to withdraw " + amount);
        }
        
        balance -= amount;
        System.out.println("Withdrawal successful. New balance: " + balance);
    }
    
    public String getAccountNumber() {
        return accountNumber;
    }
    
    public double getBalance() {
        return balance;
    }
}

public class CustomExceptionExample {
    public static void main(String[] args) {
        BankAccount account = new BankAccount("123456", 1000.0);
        
        try {
            account.withdraw(500.0);
            System.out.println("First withdrawal successful");
            
            account.withdraw(700.0);
            System.out.println("Second withdrawal successful");
        } catch (InsufficientFundsException e) {
            System.out.println("Transaction failed: " + e.getMessage());
        }
    }
}
```

**Custom unchecked exception example**:
```java
// Custom unchecked exception
public class InvalidProductException extends RuntimeException {
    private String productId;
    
    public InvalidProductException(String message, String productId) {
        super(message);
        this.productId = productId;
    }
    
    public String getProductId() {
        return productId;
    }
}

public class ProductManager {
    public void processProduct(String productId) {
        if (productId == null || productId.isEmpty()) {
            throw new InvalidProductException("Product ID cannot be null or empty", productId);
        }
        
        if (!productId.startsWith("PRD")) {
            throw new InvalidProductException("Invalid product ID format", productId);
        }
        
        // Process the product...
        System.out.println("Processing product: " + productId);
    }
}
```

**Benefits of custom exceptions**:

1. **Meaningful error messages**: Custom exceptions can provide more context-specific information about what went wrong
2. **Better exception handling**: Callers can catch specific exception types for different error conditions
3. **Additional information**: Custom exceptions can carry additional data related to the error
4. **Self-documenting code**: The exception name itself can explain the error condition
5. **Cleaner API design**: Custom exceptions can make the API more intuitive for users

**Best practices for custom exceptions**:

1. **Follow naming conventions**: Exception class names should end with "Exception"
2. **Choose the right base class**: Extend Exception for checked exceptions, RuntimeException for unchecked exceptions
3. **Provide constructors**: At minimum, provide constructors that match the parent class
4. **Keep exceptions serializable**: Ensure proper serialVersionUID if you plan to serialize exceptions
5. **Document exceptions**: Include thorough Javadoc explaining when and why the exception is thrown

### Q9: What is exception chaining in Java?

Exception chaining (also known as exception wrapping) is a technique in Java where an exception is caught and a new exception is thrown, with the original exception attached as the cause. This helps preserve the stack trace and context of the original problem while allowing higher-level components to handle exceptions in a more abstracted way.

**Key features of exception chaining**:

1. **Preserves original exception information**: The original exception is not lost, but wrapped inside a new exception
2. **Adds additional context**: The new exception can add higher-level context to the error
3. **Abstraction**: Lower-level exceptions can be wrapped in higher-level, more meaningful exceptions
4. **Enables root cause analysis**: The entire chain of exceptions can be examined to determine the root cause

**How to implement exception chaining**:

1. **Using constructor with cause parameter**:
```java
try {
    // Code that might throw an exception
} catch (SQLException e) {
    throw new ServiceException("Database operation failed", e);
}
```

2. **Using the initCause() method**:
```java
try {
    // Code that might throw an exception
} catch (SQLException e) {
    ServiceException serviceEx = new ServiceException("Database operation failed");
    serviceEx.initCause(e);
    throw serviceEx;
}
```

3. **Accessing the cause**:
```java
try {
    // Code that might throw a chained exception
} catch (Exception e) {
    System.out.println("Exception: " + e.getMessage());
    Throwable cause = e.getCause();
    if (cause != null) {
        System.out.println("Caused by: " + cause.getMessage());
    }
}
```

**Complete example of exception chaining**:
```java
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

// Custom exceptions
class DatabaseException extends Exception {
    public DatabaseException(String message) {
        super(message);
    }
    
    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}

class ServiceException extends Exception {
    public ServiceException(String message) {
        super(message);
    }
    
    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}

public class ExceptionChainingExample {
    
    // Method that simulates a database operation
    public static void updateDatabase(String data) throws DatabaseException {
        try {
            // Simulate a database operation that fails
            if (data == null) {
                throw new SQLException("NULL data cannot be inserted into the database");
            }
            // Process data...
        } catch (SQLException e) {
            // Wrap the SQLException in a DatabaseException
            throw new DatabaseException("Database update failed", e);
        }
    }
    
    // Service method that uses the database
    public static void updateUserProfile(String userId, String data) throws ServiceException {
        try {
            // Additional validation
            if (userId == null || userId.isEmpty()) {
                throw new IllegalArgumentException("User ID cannot be empty");
            }
            
            // Try to update the database
            updateDatabase(data);
            
        } catch (DatabaseException e) {
            // Wrap the DatabaseException in a ServiceException
            throw new ServiceException("Failed to update user profile for user: " + userId, e);
        } catch (IllegalArgumentException e) {
            // Wrap the IllegalArgumentException in a ServiceException
            throw new ServiceException("Invalid user profile data", e);
        }
    }
    
    public static void main(String[] args) {
        try {
            // This will cause a chain of exceptions
            updateUserProfile("user123", null);
            
        } catch (ServiceException e) {
            System.out.println("Service Exception: " + e.getMessage());
            
            // Get the cause (first level)
            Throwable cause = e.getCause();
            if (cause != null) {
                System.out.println("Caused by: " + cause.getClass().getName() + ": " + cause.getMessage());
                
                // Get the root cause (second level)
                Throwable rootCause = cause.getCause();
                if (rootCause != null) {
                    System.out.println("Root cause: " + rootCause.getClass().getName() + ": " + rootCause.getMessage());
                }
            }
            
            // Print the complete stack trace
            System.out.println("\nComplete Stack Trace:");
            e.printStackTrace();
        }
    }
}
```

**Output**:
```
Service Exception: Failed to update user profile for user: user123
Caused by: DatabaseException: Database update failed
Root cause: java.sql.SQLException: NULL data cannot be inserted into the database

Complete Stack Trace:
ServiceException: Failed to update user profile for user: user123
    at ExceptionChainingExample.updateUserProfile(ExceptionChainingExample.java:46)
    at ExceptionChainingExample.main(ExceptionChainingExample.java:59)
Caused by: DatabaseException: Database update failed
    at ExceptionChainingExample.updateDatabase(ExceptionChainingExample.java:33)
    at ExceptionChainingExample.updateUserProfile(ExceptionChainingExample.java:42)
    ... 1 more
Caused by: java.sql.SQLException: NULL data cannot be inserted into the database
    at ExceptionChainingExample.updateDatabase(ExceptionChainingExample.java:29)
    ... 2 more
```

**Benefits of exception chaining**:

1. **Proper abstraction**: Higher-level components don't need to know about lower-level exceptions
2. **Detailed error information**: The complete chain of exceptions provides a detailed history of what went wrong
3. **Cleaner APIs**: Methods can declare a single exception type rather than multiple specific exceptions
4. **Easier debugging**: Helps track down the root cause of problems

### Q10: What are the best practices for exception handling in Java?

Following good exception handling practices is crucial for writing robust, maintainable, and reliable Java applications. Here are the best practices for exception handling in Java:

**1. Use specific exceptions**:
- Catch specific exceptions rather than generic ones (e.g., catch `FileNotFoundException` instead of just `Exception`)
- This allows for more precise error handling and recovery strategies

**2. Don't swallow exceptions**:
- Avoid empty catch blocks that hide exceptions
- Always provide some form of handling, even if it's just logging
```java
// Bad practice
try {
    // Code that might throw an exception
} catch (Exception e) {
    // Empty catch block - exception is swallowed
}

// Good practice
try {
    // Code that might throw an exception
} catch (Exception e) {
    logger.error("Operation failed", e);
    // Take appropriate action
}
```

**3. Clean up resources properly**:
- Use try-with-resources for automatic resource management
- If not using try-with-resources, always clean up in a finally block

**4. Throw exceptions with meaningful messages**:
- Include relevant information in exception messages
- Make error messages clear and actionable
```java
// Bad practice
throw new IllegalArgumentException();

// Good practice
throw new IllegalArgumentException("User ID cannot be negative: " + userId);
```

**5. Document exceptions in Javadoc**:
- Use `@throws` to document exceptions that a method might throw
- Explain when and why each exception is thrown
```java
/**
 * Processes the user order.
 *
 * @param order The order to process
 * @throws InvalidOrderException If the order contains invalid items
 * @throws InsufficientInventoryException If there is not enough inventory to fulfill the order
 */
public void processOrder(Order order) throws InvalidOrderException, InsufficientInventoryException {
    // Method implementation
}
```

**6. Don't use exceptions for normal flow control**:
- Exceptions are for exceptional conditions, not regular control flow
- Using exceptions for normal flow control degrades performance and readability
```java
// Bad practice
try {
    int value = list.get(index);
    // Process value
} catch (IndexOutOfBoundsException e) {
    // Handle index not found
}

// Good practice
if (index < list.size()) {
    int value = list.get(index);
    // Process value
} else {
    // Handle index not found
}
```

**7. Wrap and rethrow exceptions when appropriate**:
- Use exception chaining to preserve the original cause
- Add context information when rethrowing exceptions
```java
try {
    // Low-level operation
} catch (SQLException e) {
    throw new ServiceException("Failed to fetch user data", e);
}
```

**8. Prefer unchecked exceptions for programming errors**:
- Use checked exceptions for recoverable conditions
- Use unchecked (runtime) exceptions for programming errors that should not be caught

**9. Fail fast**:
- Validate parameters at the beginning of methods
- Throw exceptions early rather than allowing invalid states to propagate
```java
public void processUser(User user) {
    // Fail fast with validation
    if (user == null) {
        throw new IllegalArgumentException("User cannot be null");
    }
    
    // Continue processing with valid user
}
```

**10. Handle exceptions at the appropriate level**:
- Don't catch exceptions you don't know how to handle
- Let exceptions propagate to a level where they can be meaningfully handled

**11. Log exceptions properly**:
- Include the stack trace when logging exceptions
- Log at the appropriate level (ERROR for exceptions that affect functionality)
- Include contextual information in log messages
```java
try {
    // Operation that might fail
} catch (Exception e) {
    logger.error("Failed to process transaction {}: {}", transactionId, e.getMessage(), e);
}
```

**12. Create custom exceptions when appropriate**:
- Create custom exceptions for domain-specific errors
- Extend the appropriate exception base class

**13. Don't expose sensitive information in exceptions**:
- Be careful not to include passwords, tokens, or other sensitive data in exception messages
- Sanitize exception data that might be displayed to end users

**14. Use finally blocks or try-with-resources for cleanup**:
- Ensure resources are closed even when exceptions occur
- Remember that finally blocks execute even when there's a return statement in the try block

**15. Test exception handling**:
- Write unit tests specifically for exception handling logic
- Test both normal and exceptional paths

**Example of combining best practices**:
```java
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Logger;

public class ExceptionBestPracticesExample {
    private static final Logger logger = Logger.getLogger(ExceptionBestPracticesExample.class.getName());
    
    /**
     * Reads data from the specified file.
     *
     * @param filePath the path to the file
     * @return the file contents as a string
     * @throws IllegalArgumentException if filePath is null or empty
     * @throws FileProcessingException if an error occurs while reading the file
     */
    public static String readFile(String filePath) throws FileProcessingException {
        // Validate parameters (fail fast)
        if (filePath == null || filePath.isEmpty()) {
            throw new IllegalArgumentException("File path cannot be null or empty");
        }
        
        // Use try-with-resources for proper resource management
        try (FileInputStream fis = new FileInputStream(filePath)) {
            byte[] data = new byte[fis.available()];
            fis.read(data);
            return new String(data);
        } catch (IOException e) {
            // Wrap and rethrow with additional context
            logger.severe("Failed to read file: " + filePath + ". Error: " + e.getMessage());
            throw new FileProcessingException("Could not read file: " + filePath, e);
        }
    }
    
    // Custom domain-specific exception
    public static class FileProcessingException extends Exception {
        public FileProcessingException(String message) {
            super(message);
        }
        
        public FileProcessingException(String message, Throwable cause) {
            super(message, cause);
        }
    }
    
    public static void main(String[] args) {
        try {
            String content = readFile("example.txt");
            System.out.println("File content: " + content);
        } catch (FileProcessingException e) {
            System.err.println("Error processing file: " + e.getMessage());
            // Log the complete exception with stack trace
            logger.severe("File processing failed");
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid argument: " + e.getMessage());
        }
    }
}
```

## Advanced Exception Handling Questions for 3+ Years Experience

### Q11: How would you handle exceptions in a multi-threaded environment?

This question tests your understanding of exception handling in concurrent applications.

**Key considerations**:

1. **Thread isolation**: Exceptions in one thread don't affect other threads
2. **Uncaught exception handlers**: Can be used to handle exceptions that would otherwise terminate a thread
3. **ExecutorService handling**: When using thread pools, exceptions need special handling
4. **Future.get()**: Throws ExecutionException when the task throws an exception

**Example of handling exceptions in a thread pool**:

```java
import java.util.concurrent.*;
import java.util.logging.Logger;

public class ThreadExceptionHandlingExample {
    private static final Logger logger = Logger.getLogger(ThreadExceptionHandlingExample.class.getName());
    
    public static void main(String[] args) {
        // Set up a global uncaught exception handler
        Thread.setDefaultUncaughtExceptionHandler((thread, exception) -> {
            logger.severe("Uncaught exception in thread " + thread.getName() + ": " + exception.getMessage());
        });
        
        // Example 1: Direct thread with exception
        Thread thread = new Thread(() -> {
            throw new RuntimeException("Exception in direct thread");
        }, "CustomThread");
        thread.start();
        
        // Example 2: Thread pool with exception handling
        ExecutorService executor = Executors.newFixedThreadPool(2);
        
        // Submit tasks that might throw exceptions
        Future<String> future1 = executor.submit(() -> {
            if (Math.random() < 0.5) {
                throw new IllegalStateException("Random failure in task");
            }
            return "Task completed successfully";
        });
        
        Future<String> future2 = executor.submit(() -> {
            throw new RuntimeException("Deliberate failure");
        });
        
        // Handle exceptions from Future.get()
        try {
            String result1 = future1.get(); // This might throw ExecutionException
            System.out.println("Result 1: " + result1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warning("Task was interrupted");
        } catch (ExecutionException e) {
            logger.severe("Task threw an exception: " + e.getCause().getMessage());
        }
        
        try {
            String result2 = future2.get(); // This will throw ExecutionException
            System.out.println("Result 2: " + result2);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warning("Task was interrupted");
        } catch (ExecutionException e) {
            logger.severe("Task threw an exception: " + e.getCause().getMessage());
        }
        
        executor.shutdown();
    }
}
```

**Best practices for handling exceptions in multi-threaded code**:

1. **Always handle InterruptedException properly**:
   ```java
   try {
       // Some blocking operation
   } catch (InterruptedException e) {
       // Restore the interrupted status
       Thread.currentThread().interrupt();
       // Handle appropriately
   }
   ```

2. **Use uncaught exception handlers**:
   ```java
   Thread.UncaughtExceptionHandler handler = (thread, throwable) -> {
       logger.severe("Uncaught exception in thread " + thread.getName() + ": " + throwable.getMessage());
   };
   
   Thread thread = new Thread(runnable);
   thread.setUncaughtExceptionHandler(handler);
   thread.start();
   ```

3. **Handle exceptions when using CompletableFuture**:
   ```java
   CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
       if (Math.random() < 0.5) {
           throw new RuntimeException("Random failure");
       }
       return "Success";
   });
   
   future.exceptionally(ex -> {
       logger.severe("Operation failed: " + ex.getMessage());
       return "Default value after failure";
   });
   ```

### Q12: How does exception handling impact application performance, and how would you optimize it?

This question tests your understanding of the performance implications of exception handling.

**Performance considerations**:

1. **Creation cost**: Creating exception objects with stack traces is expensive
2. **Propagation cost**: Unwinding the stack during exception propagation adds overhead
3. **Normal vs. exceptional paths**: The "happy path" should be optimized
4. **Exception use cases**: Exceptions should be truly exceptional

**Performance impacts**:

1. **Stack trace collection**: The most expensive part of creating an exception
2. **JVM optimizations**: The JVM can't optimize code paths with exceptions as effectively
3. **Exception types**: Checked exceptions are generally more expensive than unchecked ones

**Optimization techniques**:

1. **Don't use exceptions for control flow**:
   ```java
   // Bad (expensive) approach
   try {
       return map.get(key);
   } catch (NullPointerException e) {
       return defaultValue;
   }
   
   // Better approach
   return map.containsKey(key) ? map.get(key) : defaultValue;
   
   // Best approach (Java 8+)
   return map.getOrDefault(key, defaultValue);
   ```

2. **Reuse exception objects** (when thread-safe):
   ```java
   public class ReusableException extends RuntimeException {
       private static final ReusableException INSTANCE = new ReusableException("Error occurred");
       
       private ReusableException(String message) {
           super(message, null, true, false); // Suppress stack trace for better performance
       }
       
       public static ReusableException getInstance() {
           return INSTANCE;
       }
   }
   ```

3. **Use exception constructors that suppress stack traces**:
   ```java
   // Full stack trace (expensive)
   throw new IllegalArgumentException("Invalid value");
   
   // Suppressed stack trace (more efficient)
   throw new IllegalArgumentException("Invalid value", null, false, false);
   ```

4. **Handle exceptions at the appropriate level**:
   ```java
   // Catch and log at a high level to avoid multiple handling costs
   try {
       // Multiple operations that might fail
   } catch (Exception e) {
       logger.error("Operation failed", e);
   }
   ```

5. **Use try-with-resources for resource cleanup**:
   ```java
   // More efficient and cleaner than try-finally
   try (Connection conn = dataSource.getConnection()) {
       // Use connection
   }
   ```

**Measuring exception handling performance**:

```java
public class ExceptionPerformanceTest {
    private static final int ITERATIONS = 1_000_000;
    
    public static void main(String[] args) {
        // Test exception-based approach
        long startTime = System.nanoTime();
        for (int i = 0; i < ITERATIONS; i++) {
            try {
                methodThatMightThrow(i);
            } catch (Exception e) {
                // Do nothing
            }
        }
        long endTime = System.nanoTime();
        System.out.println("Exception approach: " + (endTime - startTime) / 1_000_000 + " ms");
        
        // Test conditional approach
        startTime = System.nanoTime();
        for (int i = 0; i < ITERATIONS; i++) {
            methodWithConditional(i);
        }
        endTime = System.nanoTime();
        System.out.println("Conditional approach: " + (endTime - startTime) / 1_000_000 + " ms");
    }
    
    private static void methodThatMightThrow(int value) throws Exception {
        if (value % 10 == 0) {
            throw new Exception("Value is divisible by 10");
        }
    }
    
    private static void methodWithConditional(int value) {
        if (value % 10 == 0) {
            // Handle the special case without throwing
        }
    }
}
```

### Q13: How do you handle exceptions in a layered architecture (e.g., Controller-Service-Repository pattern)?

This question tests your understanding of exception handling in enterprise application architectures.

**Key considerations for layered exception handling**:

1. **Layer-specific exceptions**: Each layer should have appropriate exception types
2. **Exception translation**: Lower-level exceptions should be translated to higher-level ones
3. **Information preservation**: Original exception details should be preserved
4. **Consistent API**: Exception handling should provide a consistent experience

**Common layers and exception strategies**:

1. **Data/Repository layer**:
   - Handle database-specific exceptions
   - Translate to domain-specific exceptions
   ```java
   public User findById(long id) throws EntityNotFoundException {
       try {
           Optional<User> result = userRepository.findById(id);
           return result.orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
       } catch (DataAccessException e) {
           throw new RepositoryException("Database error while finding user", e);
       }
   }
   ```

2. **Service layer**:
   - Handle business logic exceptions
   - Consolidate and translate repository exceptions
   ```java
   public UserDto getUserDetails(long id) throws ServiceException {
       try {
           User user = userRepository.findById(id);
           return userMapper.toDto(user);
       } catch (EntityNotFoundException e) {
           throw new ResourceNotFoundException("User not found", e);
       } catch (RepositoryException e) {
           throw new ServiceException("Error fetching user details", e);
       }
   }
   ```

3. **Controller/API layer**:
   - Translate service exceptions to HTTP responses
   - Handle API-specific concerns
   ```java
   @GetMapping("/users/{id}")
   public ResponseEntity<UserDto> getUser(@PathVariable long id) {
       try {
           UserDto user = userService.getUserDetails(id);
           return ResponseEntity.ok(user);
       } catch (ResourceNotFoundException e) {
           log.info("User not found: {}", id);
           return ResponseEntity.notFound().build();
       } catch (ServiceException e) {
           log.error("Error fetching user", e);
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
       }
   }
   ```

**Global exception handling (Spring example)**:
```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
        ErrorResponse error = new ErrorResponse("NOT_FOUND", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidation(ValidationException ex) {
        ErrorResponse error = new ErrorResponse("VALIDATION_FAILED", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ErrorResponse> handleService(ServiceException ex) {
        ErrorResponse error = new ErrorResponse("SERVICE_ERROR", "An internal service error occurred");
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception ex) {
        ErrorResponse error = new ErrorResponse("INTERNAL_ERROR", "An unexpected error occurred");
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
```

**Exception hierarchy for layered applications**:
```java
// Base application exception
public class ApplicationException extends Exception {
    public ApplicationException(String message) {
        super(message);
    }
    
    public ApplicationException(String message, Throwable cause) {
        super(message, cause);
    }
}

// Repository layer exceptions
public class RepositoryException extends ApplicationException {
    public RepositoryException(String message) {
        super(message);
    }
    
    public RepositoryException(String message, Throwable cause) {
        super(message, cause);
    }
}

public class EntityNotFoundException extends RepositoryException {
    public EntityNotFoundException(String message) {
        super(message);
    }
}

// Service layer exceptions
public class ServiceException extends ApplicationException {
    public ServiceException(String message) {
        super(message);
    }
    
    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}

public class ValidationException extends ServiceException {
    public ValidationException(String message) {
        super(message);
    }
}

// API layer exceptions
public class ApiException extends ApplicationException {
    private final HttpStatus status;
    
    public ApiException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
    
    public HttpStatus getStatus() {
        return status;
    }
}

public class ResourceNotFoundException extends ApiException {
    public ResourceNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
    
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, HttpStatus.NOT_FOUND);
        initCause(cause);
    }
}
```

### Q14: How would you implement retry logic with exponential backoff for handling transient exceptions?

This question tests your ability to create robust error handling for distributed systems.

**Key components of a retry mechanism**:

1. **Retry policy**: Defines which exceptions to retry and how many times
2. **Backoff strategy**: Determines the delay between retry attempts
3. **Circuit breaker**: Prevents retries when a service is consistently failing

**Simple implementation of retry with exponential backoff**:

```java
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class RetryUtil {
    
    /**
     * Executes an operation with retry logic and exponential backoff
     * 
     * @param operation The operation to execute
     * @param maxRetries Maximum number of retry attempts
     * @param retryableExceptions Exception types that should trigger a retry
     * @return The result of the operation
     * @throws Exception if the operation fails after all retries
     */
    public static <T> T executeWithRetry(
            Supplier<T> operation,
            int maxRetries,
            Class<? extends Exception>... retryableExceptions
    ) throws Exception {
        int attempts = 0;
        Exception lastException = null;
        
        while (attempts <= maxRetries) {
            try {
                return operation.get();
            } catch (Exception e) {
                lastException = e;
                
                // Check if exception is retryable
                boolean shouldRetry = false;
                for (Class<? extends Exception> retryable : retryableExceptions) {
                    if (retryable.isInstance(e)) {
                        shouldRetry = true;
                        break;
                    }
                }
                
                if (!shouldRetry || attempts >= maxRetries) {
                    throw e; // Rethrow if not retryable or max retries reached
                }
                
                // Calculate backoff time with exponential increase and jitter
                long backoffMillis = (long) (Math.pow(2, attempts) * 100) + (long) (Math.random() * 100);
                
                // Log retry attempt
                System.out.printf("Attempt %d failed with %s. Retrying in %d ms...%n", 
                        attempts + 1, e.getMessage(), backoffMillis);
                
                // Wait before next attempt
                try {
                    TimeUnit.MILLISECONDS.sleep(backoffMillis);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw ie;
                }
                
                attempts++;
            }
        }
        
        // This should never happen with the current logic, but added as a safeguard
        throw new RuntimeException("All retry attempts failed", lastException);
    }
}
```

**Usage example**:

```java
import java.io.IOException;
import java.net.SocketTimeoutException;

public class RetryExample {
    public static void main(String[] args) {
        try {
            String result = RetryUtil.executeWithRetry(
                () -> callExternalService("api/data"),
                3,  // Max 3 retries
                SocketTimeoutException.class,
                IOException.class
            );
            System.out.println("Success: " + result);
        } catch (Exception e) {
            System.err.println("Operation failed after retries: " + e.getMessage());
        }
    }
    
    private static String callExternalService(String endpoint) throws IOException {
        // Simulate a flaky service that sometimes succeeds
        double random = Math.random();
        if (random < 0.7) { // 70% chance of failure
            if (random < 0.4) {
                throw new SocketTimeoutException("Connection timed out");
            } else {
                throw new IOException("Service temporarily unavailable");
            }
        }
        return "Data from " + endpoint;
    }
}
```

**More advanced retry handling with a library (Resilience4j example)**:

```java
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;

import java.io.IOException;
import java.time.Duration;
import java.util.function.Supplier;

public class Resilience4jExample {
    public static void main(String[] args) {
        // Configure retry behavior
        RetryConfig config = RetryConfig.custom()
                .maxAttempts(3)
                .waitDuration(Duration.ofMillis(100))
                .retryExceptions(IOException.class, SocketTimeoutException.class)
                .exponentialBackoff(Duration.ofMillis(100), Duration.ofSeconds(1), 2.0)
                .build();
        
        // Create a retry registry
        RetryRegistry registry = RetryRegistry.of(config);
        
        // Get a retry instance
        Retry retry = registry.retry("externalServiceRetry");
        
        // Create a retry-decorated function
        Supplier<String> retryableSupplier = Retry.decorateSupplier(
                retry, () -> {
                    try {
                        return callExternalService("api/data");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
        
        // Execute with retry
        try {
            String result = retryableSupplier.get();
            System.out.println("Success: " + result);
        } catch (Exception e) {
            System.err.println("Operation failed after retries: " + e.getMessage());
        }
    }
    
    private static String callExternalService(String endpoint) throws IOException {
        // Same implementation as before
    }
}
```

### Q15: How would you handle exceptions in a RESTful API to provide meaningful error responses to clients?

This question tests your understanding of API design and exception handling from a client-facing perspective.

**Key considerations for API error handling**:

1. **Consistent error format**: All errors should follow the same structure
2. **Appropriate HTTP status codes**: Use standard HTTP status codes correctly
3. **Detailed error messages**: Include useful information for debugging
4. **Error codes**: Use specific error codes for programmatic handling
5. **Security**: Don't expose sensitive information in error messages

**Example error response structure**:
```json
{
  "status": 400,
  "code": "VALIDATION_ERROR",
  "message": "Invalid input parameters",
  "details": [
    {
      "field": "email",
      "message": "Must be a valid email address"
    },
    {
      "field": "password",
      "message": "Must be at least 8 characters long"
    }
  ],
  "timestamp": "2023-05-10T14:32:24.456Z",
  "path": "/api/users",
  "traceId": "abc123def456"
}
```

**Implementation using Spring Boot**:

1. **Create error response classes**:
```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiError {
    private int status;
    private String code;
    private String message;
    private List<FieldError> details;
    private String timestamp;
    private String path;
    private String traceId;
    
    // Constructors, getters, setters
}

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FieldError {
    private String field;
    private String message;
}
```

2. **Create a global exception handler**:
```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFound(ResourceNotFoundException ex, WebRequest request) {
        ApiError error = new ApiError();
        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setCode("RESOURCE_NOT_FOUND");
        error.setMessage(ex.getMessage());
        error.setTimestamp(ZonedDateTime.now().toString());
        error.setPath(((ServletWebRequest) request).getRequest().getRequestURI());
        error.setTraceId(generateTraceId());
        
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        List<FieldError> validationErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new FieldError(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());
        
        ApiError error = new ApiError();
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setCode("VALIDATION_ERROR");
        error.setMessage("Validation failed");
        error.setDetails(validationErrors);
        error.setTimestamp(ZonedDateTime.now().toString());
        error.setPath(((ServletWebRequest) request).getRequest().getRequestURI());
        error.setTraceId(generateTraceId());
        
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGenericExceptions(Exception ex, WebRequest request) {
        ApiError error = new ApiError();
        error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        error.setCode("INTERNAL_ERROR");
        error.setMessage("An unexpected error occurred");
        error.setTimestamp(ZonedDateTime.now().toString());
        error.setPath(((ServletWebRequest) request).getRequest().getRequestURI());
        error.setTraceId(generateTraceId());
        
        // Log the full exception for internal use
        log.error("Unhandled exception: " + ex.getMessage(), ex);
        
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    private String generateTraceId() {
        return UUID.randomUUID().toString();
    }
}
```

3. **Using the exception handling in controllers**:
```java
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        UserDto user = userService.findById(id);
        if (user == null) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        return ResponseEntity.ok(user);
    }
    
    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserCreateRequest request) {
        // @Valid will trigger MethodArgumentNotValidException if validation fails
        try {
            UserDto createdUser = userService.createUser(request);
            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        } catch (DuplicateEmailException e) {
            // Custom business exception
            throw new BusinessException("EMAIL_ALREADY_EXISTS", "A user with this email already exists");
        }
    }
}
```

**HTTP Status Codes for Common Exceptions**:

| Exception Type | HTTP Status | Description |
|----------------|-------------|-------------|
| ValidationException | 400 (Bad Request) | Invalid input data |
| AuthenticationException | 401 (Unauthorized) | Authentication failed |
| AccessDeniedException | 403 (Forbidden) | User not authorized |
| ResourceNotFoundException | 404 (Not Found) | Resource doesn't exist |
| MethodNotAllowedException | 405 (Method Not Allowed) | HTTP method not supported |
| ConflictException | 409 (Conflict) | Request conflicts with server state |
| UnsupportedMediaTypeException | 415 (Unsupported Media Type) | Content type not supported |
| RateLimitExceededException | 429 (Too Many Requests) | Client sent too many requests |
| InternalServerException | 500 (Internal Server Error) | Unexpected server error |
| ServiceUnavailableException | 503 (Service Unavailable) | Service temporarily unavailable |

**Best practices for RESTful API exception handling**:

1. **Use proper HTTP status codes**: Match status codes to the type of error
2. **Be consistent**: Use a consistent error response format across the API
3. **Include error codes**: Use specific error codes that clients can programmatically handle
4. **Provide useful messages**: Error messages should help diagnose the problem
5. **Add correlation IDs**: Include a unique ID for tracking errors across systems
6. **Log detailed errors**: Log detailed information for debugging, but don't expose it to clients
7. **Validate input early**: Catch validation errors before processing business logic
8. **Use problem details format**: Consider using RFC 7807 (Problem Details for HTTP APIs)

**Example using Spring Boot and RFC 7807 Problem Details**:
```java
@RestControllerAdvice
public class ProblemDetailsExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    ProblemDetail handleResourceNotFoundException(ResourceNotFoundException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND, e.getMessage());
        problemDetail.setTitle("Resource Not Found");
        problemDetail.setType(URI.create("https://api.example.com/errors/not-found"));
        problemDetail.setProperty("errorCategory", "NOT_FOUND");
        problemDetail.setProperty("timestamp", ZonedDateTime.now());
        return problemDetail;
    }
    
    // Other exception handlers
}
```

### Q16: What are the differences between assertions and exceptions in Java, and when would you use each?

This question tests your understanding of different error handling mechanisms in Java.

**Assertions**:

Assertions in Java are a debugging feature introduced in Java 1.4 that allow developers to verify assumptions about their code. They are enabled or disabled at runtime and are not meant for error handling in production code.

**Key characteristics of assertions**:

1. **Syntax**: `assert condition;` or `assert condition : errorMessage;`
2. **Enabled/Disabled**: Disabled by default, enabled with `-ea` or `-enableassertions` JVM flag
3. **Exception Type**: Throws `AssertionError` when the assertion fails
4. **Purpose**: Verify program correctness during development and testing
5. **Recovery**: Not designed for recovery - program typically terminates

**Exceptions**:

Exceptions in Java are objects that represent exceptional conditions that occur during program execution. They are used for error handling and recovery in both development and production code.

**Key characteristics of exceptions**:

1. **Types**: Checked (must be handled or declared) and unchecked (runtime) exceptions
2. **Handling**: Can be caught and handled using try-catch blocks
3. **Purpose**: Signal and handle exceptional conditions during runtime
4. **Recovery**: Designed to allow programs to recover from errors
5. **Information**: Can carry detailed information about the error

**Comparison**:

| Feature | Assertions | Exceptions |
|---------|------------|------------|
| Purpose | Verify program correctness | Handle runtime errors |
| When used | Development and testing | Development and production |
| Can be disabled | Yes, at runtime | No |
| Recovery | Not designed for recovery | Designed for recovery |
| Performance impact | Only when enabled | Always present |
| Handling required | No | Yes (for checked exceptions) |

**When to use assertions**:

1. **Internal invariants**: Verify that internal state is as expected
2. **Preconditions**: Verify that method inputs meet requirements (when not part of the public API)
3. **Postconditions**: Verify that method outputs are as expected
4. **Control flow**: Verify that "impossible" conditions never occur

```java
public int divide(int dividend, int divisor) {
    // Use an assertion to verify an internal assumption
    assert divisor != 0 : "Divisor should not be zero";
    
    return dividend / divisor;
}
```

**When to use exceptions**:

1. **Public API validation**: Validate inputs to public methods
2. **Runtime errors**: Handle errors that occur during program execution
3. **Resource management**: Handle errors with external resources (files, network, etc.)
4. **Business logic violations**: Signal business rule violations

```java
public int divide(int dividend, int divisor) {
    // Use an exception for input validation in public APIs
    if (divisor == 0) {
        throw new IllegalArgumentException("Divisor cannot be zero");
    }
    
    return dividend / divisor;
}
```

**Best practices**:

1. **Use assertions for internal invariants**:
   ```java
   private void processInternalState() {
       assert internalList != null : "List should be initialized";
       // Process internal state
   }
   ```

2. **Use exceptions for public API validation**:
   ```java
   public void processUserInput(String input) {
       if (input == null || input.isEmpty()) {
           throw new IllegalArgumentException("Input cannot be null or empty");
       }
       // Process input
   }
   ```

3. **Don't use assertions for input validation in public methods**:
   ```java
   // Bad practice
   public void processData(Data data) {
       assert data != null; // Don't do this for public APIs
       // Process data
   }
   
   // Good practice
   public void processData(Data data) {
       if (data == null) {
           throw new IllegalArgumentException("Data cannot be null");
       }
       // Process data
   }
   ```

4. **Use assertions to document and verify assumptions**:
   ```java
   private void sortInternal(List<String> list, boolean ascending) {
       // Verify our assumption that the list is not null and not empty
       assert list != null && !list.isEmpty() : "List must be non-null and non-empty";
       
       // Implementation details...
   }
   ```

5. **Don't use assertions for runtime control flow**:
   ```java
   // Bad practice
   public void processFile(String filename) {
       File file = new File(filename);
       assert file.exists(); // Don't do this
       // Process file
   }
   
   // Good practice
   public void processFile(String filename) {
       File file = new File(filename);
       if (!file.exists()) {
           throw new FileNotFoundException("File not found: " + filename);
       }
       // Process file
   }
   ```

### Q17: How do Java exceptions work under the hood, and what is their performance impact?

This question tests your deep understanding of Java exception implementation details and performance considerations.

**Exception mechanism under the hood**:

1. **Exception objects**: Java exceptions are normal objects that extend the `Throwable` class
2. **Stack traces**: When an exception is thrown, the JVM captures the current execution stack
3. **Stack unwinding**: The JVM searches up the call stack for a matching catch block
4. **Call stack navigation**: The JVM uses stack frames to track method invocations

**Internal implementation details**:

1. **Stack trace creation**:
   - When an exception is thrown, the JVM creates a snapshot of the current thread's stack
   - Each element in the stack trace represents a stack frame (method call)
   - For each frame, the JVM records class name, method name, filename, and line number

2. **Exception table**:
   - Each method with a try-catch block has an exception table in its bytecode
   - The table maps exception types to handlers (catch blocks)
   - When an exception occurs, the JVM checks the exception table to find a matching handler

3. **Stack unwinding process**:
   - If a handler is found, control is transferred to the catch block
   - If no handler is found in the current method, the JVM pops the current stack frame
   - The JVM then checks the exception table of the caller method
   - This process continues until a handler is found or the stack is fully unwound

**Performance impacts**:

1. **Creation cost**:
   - Creating an exception object is relatively expensive
   - Capturing the stack trace is the most expensive part (requires walking the stack)
   - Constructing the string representations of stack frames adds overhead

2. **Throwing cost**:
   - Throwing an exception is more expensive than normal control flow
   - The JVM must search for catch handlers up the call stack
   - Stack unwinding involves significant processing

3. **Benchmarking exception overhead**:

```java
public class ExceptionPerformanceTest {
    private static final int ITERATIONS = 10_000_000;
    
    public static void main(String[] args) {
        // Warm up the JVM
        for (int i = 0; i < 1_000_000; i++) {
            normalFlow();
            try {
                exceptionFlow();
            } catch (Exception e) {
                // Do nothing
            }
        }
        
        // Measure normal flow
        long startTime = System.nanoTime();
        for (int i = 0; i < ITERATIONS; i++) {
            normalFlow();
        }
        long endTime = System.nanoTime();
        System.out.println("Normal flow: " + (endTime - startTime) / 1_000_000 + " ms");
        
        // Measure exception flow
        startTime = System.nanoTime();
        for (int i = 0; i < ITERATIONS; i++) {
            try {
                exceptionFlow();
            } catch (Exception e) {
                // Do nothing
            }
        }
        endTime = System.nanoTime();
        System.out.println("Exception flow: " + (endTime - startTime) / 1_000_000 + " ms");
    }
    
    private static boolean normalFlow() {
        return false;
    }
    
    private static boolean exceptionFlow() throws Exception {
        throw new Exception();
    }
}
```

**Optimizing exception handling performance**:

1. **Don't use exceptions for normal flow control**:
   ```java
   // Bad - using exceptions for control flow
   try {
       return map.get(key);
   } catch (NullPointerException e) {
       return defaultValue; 
   }
   
   // Good - using conditional logic
   return map != null && map.containsKey(key) ? map.get(key) : defaultValue;
   ```

2. **Reuse exception objects** (when appropriate):
   ```java
   public class CachedExceptionExample {
       private static final RuntimeException CACHED_EXCEPTION = 
           new RuntimeException("Cached exception");
       
       public void methodThatMightThrow(boolean condition) {
           if (condition) {
               throw CACHED_EXCEPTION;
           }
       }
   }
   ```

3. **Suppress stack traces when not needed**:
   ```java
   // In Java 7+, you can suppress stack traces
   throw new RuntimeException("No stack trace needed", null, true, false);
   ```

4. **Handle exceptions at the right level**:
   ```java
   // Inefficient - catching and rethrowing at multiple levels
   void level3() {
       try {
           // Some operation
       } catch (Exception e) {
           throw new RuntimeException(e);
       }
   }
   
   void level2() {
       try {
           level3();
       } catch (RuntimeException e) {
           throw new ServiceException(e);
       }
   }
   
   void level1() {
       try {
           level2();
       } catch (ServiceException e) {
           // Finally handle it here
       }
   }
   
   // More efficient - handle at the appropriate level
   void level3() throws Exception {
       // Some operation
   }
   
   void level2() throws Exception {
       level3();
   }
   
   void level1() {
       try {
           level2();
       } catch (Exception e) {
           // Handle it once
       }
   }
   ```

5. **Use try-with-resources** instead of manual cleanup:
   ```java
   // More efficient and safer
   try (FileInputStream fis = new FileInputStream(file)) {
       // Use the resource
   }
   ```

**Real-world performance data**:

- Creating and throwing an exception can be 100-1000 times slower than normal control flow
- The performance impact varies based on JVM implementation and the depth of the stack
- The cost is primarily in creating the exception and capturing the stack trace
- Exceptions that are never thrown have minimal impact (JIT optimization)

**JVM Exception Handling Implementation Details**:

1. **Exception tables in bytecode**:
   ```
   Exception table:
   from    to  target type
     10    18    21   Class java/io/IOException
     10    18    31   Class java/lang/Exception
   ```

2. **Exception handler lookup in the JVM**:
   - The JVM uses a fast lookup mechanism for finding exception handlers
   - Exception types are matched based on class hierarchy
   - More specific exceptions are checked before more general ones

3. **JIT compiler optimizations**:
   - Modern JVMs optimize exception handling when possible
   - Exceptions in hot code paths may be optimized differently
   - JITs can sometimes eliminate exception handling for unreachable exceptions

### Q18: How would you design and implement a custom error handling framework for a large enterprise application?

This question tests your ability to design robust error handling systems for complex applications.

**Key requirements for an enterprise error handling framework**:

1. **Consistency**: Provide a uniform way to handle exceptions across the application
2. **Centralization**: Centralize error handling logic to avoid duplication
3. **Abstraction**: Abstract underlying technologies and implementation details
4. **Configurability**: Allow for configuration of error handling behavior
5. **Monitoring**: Support error logging, metrics, and monitoring
6. **User Experience**: Present user-friendly error messages when appropriate

**Design components of an error handling framework**:

1. **Exception hierarchy**:
   - Base application exception classes
   - Layer-specific exception types
   - Categorization by severity and error type

```java
// Base application exception
public abstract class ApplicationException extends Exception {
    private final ErrorCode errorCode;
    private final ErrorSeverity severity;
    
    public ApplicationException(String message, ErrorCode errorCode, ErrorSeverity severity) {
        super(message);
        this.errorCode = errorCode;
        this.severity = severity;
    }
    
    public ApplicationException(String message, ErrorCode errorCode, ErrorSeverity severity, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.severity = severity;
    }
    
    public ErrorCode getErrorCode() {
        return errorCode;
    }
    
    public ErrorSeverity getSeverity() {
        return severity;
    }
}

// Error codes enum
public enum ErrorCode {
    // System errors
    SYSTEM_ERROR(1000, ErrorCategory.SYSTEM),
    DATABASE_ERROR(1001, ErrorCategory.SYSTEM),
    NETWORK_ERROR(1002, ErrorCategory.SYSTEM),
    
    // Business errors
    VALIDATION_ERROR(2000, ErrorCategory.BUSINESS),
    INSUFFICIENT_FUNDS(2001, ErrorCategory.BUSINESS),
    RESOURCE_NOT_FOUND(2002, ErrorCategory.BUSINESS),
    
    // Security errors
    AUTHENTICATION_ERROR(3000, ErrorCategory.SECURITY),
    AUTHORIZATION_ERROR(3001, ErrorCategory.SECURITY);
    
    private final int code;
    private final ErrorCategory category;
    
    ErrorCode(int code, ErrorCategory category) {
        this.code = code;
        this.category = category;
    }
    
    public int getCode() {
        return code;
    }
    
    public ErrorCategory getCategory() {
        return category;
    }
}

// Error categories
public enum ErrorCategory {
    SYSTEM,
    BUSINESS,
    SECURITY
}

// Error severity
public enum ErrorSeverity {
    FATAL,    // Application cannot continue
    ERROR,    // Operation failed but application can continue
    WARNING,  // Operation succeeded but with issues
    INFO      // Informational only
}
```

2. **Layer-specific exceptions**:
```java
// Data layer exception
public class DataException extends ApplicationException {
    public DataException(String message, ErrorCode errorCode) {
        super(message, errorCode, ErrorSeverity.ERROR);
    }
    
    public DataException(String message, ErrorCode errorCode, Throwable cause) {
        super(message, errorCode, ErrorSeverity.ERROR, cause);
    }
}

// Service layer exception
public class ServiceException extends ApplicationException {
    public ServiceException(String message, ErrorCode errorCode) {
        super(message, errorCode, ErrorSeverity.ERROR);
    }
    
    public ServiceException(String message, ErrorCode errorCode, Throwable cause) {
        super(message, errorCode, ErrorSeverity.ERROR, cause);
    }
}
```

3. **Exception handling service**:
```java
public interface ErrorHandlingService {
    void handleException(ApplicationException exception, ErrorContext context);
    ErrorResponse createErrorResponse(ApplicationException exception, ErrorContext context);
}

public class DefaultErrorHandlingService implements ErrorHandlingService {
    private final ErrorLogger logger;
    private final ErrorNotifier notifier;
    private final ErrorResponseFactory responseFactory;
    
    @Override
    public void handleException(ApplicationException exception, ErrorContext context) {
        // Log the exception
        logger.logException(exception, context);
        
        // Notify if necessary based on severity
        if (needsNotification(exception)) {
            notifier.sendNotification(exception, context);
        }
        
        // Track metrics
        trackMetrics(exception);
    }
    
    @Override
    public ErrorResponse createErrorResponse(ApplicationException exception, ErrorContext context) {
        return responseFactory.createResponse(exception, context);
    }
    
    private boolean needsNotification(ApplicationException exception) {
        return exception.getSeverity() == ErrorSeverity.FATAL || 
               exception.getSeverity() == ErrorSeverity.ERROR;
    }
    
    private void trackMetrics(ApplicationException exception) {
        // Increment error counters by type and severity
    }
}
```

4. **Error context**:
```java
public class ErrorContext {
    private final String requestId;
    private final String userId;
    private final String sessionId;
    private final Map<String, Object> contextData;
    
    // Constructor, getters, and methods to add context data
}
```

5. **Web layer integration** (Spring MVC example):
```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    private final ErrorHandlingService errorHandlingService;
    
    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponse> handleApplicationException(ApplicationException ex, 
                                                                  HttpServletRequest request) {
        // Create error context
        ErrorContext context = createErrorContext(request);
        
        // Handle the exception
        errorHandlingService.handleException(ex, context);
        
        // Create and return the response
        ErrorResponse response = errorHandlingService.createErrorResponse(ex, context);
        HttpStatus status = mapToHttpStatus(ex);
        
        return new ResponseEntity<>(response, status);
    }
    
    private ErrorContext createErrorContext(HttpServletRequest request) {
        // Extract data from request and create context
    }
    
    private HttpStatus mapToHttpStatus(ApplicationException ex) {
        // Map exception to appropriate HTTP status code based on error code/category
    }
}
```

6. **Error logging and monitoring**:
```java
public class ErrorLogger {
    private final Logger logger = LoggerFactory.getLogger(ErrorLogger.class);
    
    public void logException(ApplicationException exception, ErrorContext context) {
        MDC.put("requestId", context.getRequestId());
        MDC.put("userId", context.getUserId());
        MDC.put("errorCode", String.valueOf(exception.getErrorCode().getCode()));
        MDC.put("errorCategory", exception.getErrorCode().getCategory().name());
        
        if (exception.getSeverity() == ErrorSeverity.FATAL) {
            logger.error("FATAL: {}", exception.getMessage(), exception);
        } else if (exception.getSeverity() == ErrorSeverity.ERROR) {
            logger.error("{}", exception.getMessage(), exception);
        } else if (exception.getSeverity() == ErrorSeverity.WARNING) {
            logger.warn("{}", exception.getMessage(), exception);
        } else {
            logger.info("{}", exception.getMessage(), exception);
        }
        
        MDC.clear();
    }
}
```

7. **Configuration**:
```java
@Configuration
public class ErrorHandlingConfig {
    
    @Value("${error.notification.enabled:true}")
    private boolean notificationEnabled;
    
    @Value("${error.stacktrace.included:false}")
    private boolean includeStackTrace;
    
    @Bean
    public ErrorHandlingService errorHandlingService() {
        return new DefaultErrorHandlingService(errorLogger(), errorNotifier(), errorResponseFactory());
    }
    
    @Bean
    public ErrorLogger errorLogger() {
        return new ErrorLogger();
    }
    
    @Bean
    public ErrorNotifier errorNotifier() {
        return new ErrorNotifier(notificationEnabled);
    }
    
    @Bean
    public ErrorResponseFactory errorResponseFactory() {
        return new ErrorResponseFactory(includeStackTrace);
    }
}
```

**Implementation example for service layer**:

```java
@Service
public class UserService {
    
    private final UserRepository userRepository;
    
    public UserDTO getUserById(String userId) throws ServiceException {
        try {
            User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataException(
                    "User not found with ID: " + userId,
                    ErrorCode.RESOURCE_NOT_FOUND));
            
            return mapToDTO(user);
        } catch (DataException e) {
            // Translate data exception to service exception
            throw new ServiceException("Error retrieving user: " + userId, 
                    ErrorCode.RESOURCE_NOT_FOUND, e);
        } catch (Exception e) {
            // Unexpected exception - wrap it
            throw new ServiceException("Unexpected error retrieving user: " + userId, 
                    ErrorCode.SYSTEM_ERROR, e);
        }
    }
    
    private UserDTO mapToDTO(User user) {
        // Mapping logic here
    }
}
```

**Benefits of a custom error handling framework**:

1. **Consistency**: All errors are handled in a consistent manner
2. **Traceability**: Errors can be traced across system boundaries
3. **Separation of concerns**: Error handling logic is separated from business logic
4. **Improved debugging**: More context is available for debugging
5. **Better user experience**: Users receive appropriate error messages

**Challenges and considerations**:

1. **Performance overhead**: Complex error handling can add performance overhead
2. **Framework integration**: Integration with existing frameworks requires careful design
3. **Backward compatibility**: Changes to the error handling framework must maintain compatibility
4. **Developer adoption**: Ensuring developers use the framework consistently

### Q19: How do you handle errors in asynchronous and reactive programming?

This question tests your understanding of error handling in modern asynchronous programming paradigms.

**Challenges with asynchronous error handling**:

1. **Execution context**: Errors may occur in a different thread from the caller
2. **Lost stack traces**: Stack traces may lose context across asynchronous boundaries
3. **Callback hell**: Nested error handling can become complex
4. **Resource cleanup**: Ensuring resources are properly released

**Handling errors with CompletableFuture**:

```java
CompletableFuture<User> future = userService.getUserAsync(userId)
    .thenApply(user -> {
        // Transform user
        return enrichUser(user);
    })
    .exceptionally(ex -> {
        // Handle exception
        logger.error("Error getting user: {}", ex.getMessage());
        return fallbackUser();
    });

// Alternative with handle() for both success and error cases
CompletableFuture<User> future = userService.getUserAsync(userId)
    .handle((user, ex) -> {
        if (ex != null) {
            logger.error("Error getting user: {}", ex.getMessage());
            return fallbackUser();
        }
        return enrichUser(user);
    });
```

**Handling errors with Reactive Streams (e.g., Project Reactor)**:

```java
Flux<User> userFlux = userRepository.findAllUsers()
    .map(user -> enrichUser(user))
    .onErrorReturn(UserNotFoundException.class, getFallbackUser())
    .onErrorResume(DatabaseException.class, ex -> 
        getBackupUserSource())
    .doOnError(ex -> logger.error("Error in user stream: {}", ex.getMessage()))
    .timeout(Duration.ofSeconds(5))
    .onErrorMap(TimeoutException.class, ex -> 
        new ServiceException("Service timed out", ex));
```

**Best practices for asynchronous error handling**:

1. **Always handle errors**: Never leave asynchronous errors unhandled

```java
// Bad - unhandled errors
CompletableFuture<User> future = userService.getUserAsync(userId)
    .thenApply(user -> enrichUser(user));

// Good - with error handling
CompletableFuture<User> future = userService.getUserAsync(userId)
    .thenApply(user -> enrichUser(user))
    .exceptionally(ex -> {
        logger.error("Error enriching user: {}", ex.getMessage());
        return fallbackUser();
    });
```

2. **Preserve context**: Capture and propagate context information

```java
public CompletableFuture<Result> processAsync(String requestId, Input input) {
    return CompletableFuture.supplyAsync(() -> {
        MDC.put("requestId", requestId);  // Set context for logging
        try {
            return processInput(input);
        } catch (Exception e) {
            throw new CompletionException(
                new ProcessingException(
                    "Failed to process request: " + requestId, e));
        } finally {
            MDC.remove("requestId");
        }
    });
}
```

3. **Error recovery strategies**:

```java
// Retry with backoff
Flux<Data> dataFlux = webClient.get()
    .uri("/api/data")
    .retrieve()
    .bodyToFlux(Data.class)
    .retryWhen(Retry.backoff(3, Duration.ofSeconds(1))
        .filter(e -> e instanceof NetworkException)
        .onRetryExhaustedThrow((retrySpec, retrySignal) -> 
            new ServiceException("Service unavailable after retries", 
                retrySignal.failure())));

// Fallback values
Mono<UserProfile> profile = userService.getProfile(userId)
    .onErrorResume(ex -> {
        if (ex instanceof ResourceNotFoundException) {
            return Mono.just(new UserProfile(userId, "Anonymous"));
        }
        return Mono.error(ex);
    });
```

4. **Global error handlers**:

```java
// Reactor global error handler
Hooks.onOperatorError((error, obj) -> {
    log.error("Global reactor error: {}", error.getMessage());
    return error;
});

// ExecutorService uncaught exception handler
executor.setUncaughtExceptionHandler((thread, throwable) -> {
    log.error("Uncaught async error in thread {}: {}", 
              thread.getName(), throwable.getMessage());
});
```

5. **Timeouts and circuit breakers**:

```java
// Timeout example
Mono<Response> response = service.callExternalApi()
    .timeout(Duration.ofSeconds(3))
    .onErrorResume(TimeoutException.class, ex -> 
        Mono.just(Response.fallback("Timed out")));

// Circuit breaker with Resilience4j
CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults("apiService");

Mono<Response> response = Mono.fromCallable(() -> {
    return circuitBreaker.executeCallable(() -> {
        return apiClient.callApi();
    });
}).onErrorResume(CircuitBreakerOpenException.class, ex -> 
    Mono.just(Response.fallback("Circuit open")));
```

**Testing asynchronous error handling**:

```java
@Test
public void testAsyncErrorHandling() {
    // Given
    when(apiClient.fetchDataAsync()).thenReturn(
        CompletableFuture.failedFuture(
            new ApiException("API unavailable")));
    
    // When
    CompletableFuture<Result> result = service.processDataAsync();
    
    // Then
    assertThatThrownBy(() -> result.join())
        .isInstanceOf(CompletionException.class)
        .hasCauseInstanceOf(ServiceException.class);
}

@Test
public void testReactiveErrorHandling() {
    // Given
    when(repository.findById("not-found"))
        .thenReturn(Mono.error(new ResourceNotFoundException("Not found")));
    
    // When
    Mono<Response> response = service.getResource("not-found");
    
    // Then
    StepVerifier.create(response)
        .expectErrorMatches(e -> e instanceof ResourceNotFoundException)
        .verify();
}
```

### Q20: What is the role of exception handling in ensuring fault tolerance and high availability in distributed systems?

This question tests your understanding of how exception handling contributes to building resilient distributed systems.

**Key challenges in distributed systems**:

1. **Partial failures**: Components may fail independently
2. **Network issues**: Latency, partitions, and connectivity problems
3. **Cascading failures**: Failures can propagate through the system
4. **Consistency issues**: Data may become inconsistent due to failures
5. **Resource exhaustion**: Services may run out of resources

**Exception handling strategies for distributed systems**:

1. **Timeout handling**:
   - Prevent hanging operations that never return
   - Fail fast rather than waiting indefinitely

```java
public Response callService(String serviceUrl) {
    try {
        return httpClient
            .target(serviceUrl)
            .request()
            .property(ClientProperties.CONNECT_TIMEOUT, 1000)
            .property(ClientProperties.READ_TIMEOUT, 3000)
            .get(Response.class);
    } catch (ProcessingException e) {
        if (e.getCause() instanceof SocketTimeoutException) {
            // Handle timeout specifically
            throw new ServiceTimeoutException("Service call timed out", e);
        }
        throw new ServiceException("Error calling service", e);
    }
}
```

2. **Circuit breaker pattern**:
   - Prevent cascading failures by failing fast
   - Allow systems time to recover

```java
@CircuitBreaker(name = "userService", fallbackMethod = "getUserFallback")
public User getUser(String userId) {
    return userServiceClient.getUser(userId);
}

public User getUserFallback(String userId, Exception e) {
    log.warn("Circuit open for user service, using fallback for user {}", userId);
    return getCachedUser(userId);
}
```

3. **Bulkhead pattern**:
   - Isolate components to contain failures
   - Prevent a single failing component from consuming all resources

```java
@Bulkhead(name = "notificationService", fallbackMethod = "sendNotificationFallback")
public void sendNotification(Notification notification) {
    notificationClient.send(notification);
}

public void sendNotificationFallback(Notification notification, Exception e) {
    log.warn("Bulkhead triggered for notification service, queueing notification");
    notificationQueue.enqueue(notification);
}
```

4. **Retry with backoff**:
   - Automatically retry transient failures
   - Use exponential backoff to prevent overwhelming resources

```java
@Retry(name = "databaseService", fallbackMethod = "saveDataFallback")
public void saveData(Data data) {
    databaseClient.save(data);
}

public void saveDataFallback(Data data, Exception e) {
    log.error("Failed to save data after retries", e);
    emergencyDataStore.save(data);
    alertService.sendAlert("Data save failed: " + e.getMessage());
}
```

5. **Fallback mechanisms**:
   - Provide alternative implementations when services fail
   - Return cached or default data

```java
public ProductDetails getProductDetails(String productId) {
    try {
        return productService.getDetails(productId);
    } catch (Exception e) {
        log.warn("Error fetching product details, using cached version", e);
        return cacheService.getProductDetails(productId)
            .orElse(new ProductDetails(productId, "Unknown", "No description available"));
    }
}
```

6. **Health checking and self-healing**:
   - Monitor component health through exception patterns
   - Take corrective actions automatically

```java
public class ServiceHealthMonitor {
    private final Map<String, HealthStatus> serviceStatuses = new ConcurrentHashMap<>();
    
    public void recordException(String serviceName, Exception e) {
        HealthStatus status = serviceStatuses.getOrDefault(serviceName, new HealthStatus());
        status.recordFailure();
        if (status.isUnhealthy()) {
            attemptRecovery(serviceName);
        }
        serviceStatuses.put(serviceName, status);
    }
    
    private void attemptRecovery(String serviceName) {
        log.warn("Service {} is unhealthy, attempting recovery", serviceName);
        try {
            serviceManager.restartService(serviceName);
        } catch (Exception e) {
            log.error("Failed to restart service {}", serviceName, e);
            alertService.sendAlert("Service restart failed: " + serviceName);
        }
    }
}
```

7. **Compensating transactions**:
   - Handle failures in distributed transactions
   - Maintain eventual consistency

```java
public void processOrder(Order order) {
    TransactionContext context = new TransactionContext(order.getId());
    try {
        // Step 1: Reserve inventory
        inventoryService.reserveItems(order.getItems(), context);
        
        // Step 2: Process payment
        paymentService.processPayment(order.getPayment(), context);
        
        // Step 3: Create shipment
        shipmentService.createShipment(order, context);
        
        // Commit the transaction
        transactionManager.commit(context);
    } catch (InventoryException e) {
        // No compensation needed - failure at first step
        transactionManager.abort(context);
        throw new OrderProcessingException("Insufficient inventory", e);
    } catch (PaymentException e) {
        // Compensate for inventory reservation
        inventoryService.releaseItems(order.getItems(), context);
        transactionManager.abort(context);
        throw new OrderProcessingException("Payment failed", e);
    } catch (ShipmentException e) {
        // Compensate for inventory and payment
        inventoryService.releaseItems(order.getItems(), context);
        paymentService.refundPayment(order.getPayment(), context);
        transactionManager.abort(context);
        throw new OrderProcessingException("Shipment creation failed", e);
    } catch (Exception e) {
        // Unexpected exception - compensate for all steps
        transactionManager.rollback(context);
        throw new OrderProcessingException("Order processing failed", e);
    }
}
```

**Centralized exception handling for microservices**:

```java
@Component
public class GlobalExceptionHandler {
    
    private final ErrorReportingService errorReporter;
    private final MetricsCollector metricsCollector;
    
    @EventListener
    public void handleServiceException(ServiceExceptionEvent event) {
        Exception exception = event.getException();
        String serviceName = event.getServiceName();
        String requestId = event.getRequestId();
        
        // Log the exception with context
        log.error("Exception in service {}: {}", serviceName, exception.getMessage(), exception);
        
        // Record metrics
        metricsCollector.incrementErrorCount(serviceName, exception.getClass().getSimpleName());
        
        // Report error to centralized system
        errorReporter.reportError(serviceName, requestId, exception);
        
        // Trigger recovery if needed
        if (isRecoverable(exception)) {
            recoveryManager.triggerRecovery(serviceName, requestId);
        }
    }
    
    private boolean isRecoverable(Exception exception) {
        return exception instanceof NetworkException ||
               exception instanceof TimeoutException ||
               exception instanceof ConcurrencyException;
    }
}
```

**Key principles for fault-tolerant exception handling**:

1. **Design for failure**: Assume components will fail and design accordingly
2. **Fail fast**: Detect failures quickly and respond immediately
3. **Isolate failures**: Contain failures to prevent them from spreading
4. **Degrade gracefully**: Provide reduced functionality rather than complete failure
5. **Self-healing**: Implement automatic recovery mechanisms
6. **Redundancy**: Provide multiple paths to achieve goals
7. **Monitoring and alerting**: Track exceptions to identify patterns and issues