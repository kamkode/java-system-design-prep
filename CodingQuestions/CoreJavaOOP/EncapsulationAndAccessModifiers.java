/**
 * Core Java & OOP - Question 15: Encapsulation and access modifiers
 */
package CodingQuestions.CoreJavaOOP;

public class EncapsulationAndAccessModifiers {

    public static void main(String[] args) {
        System.out.println("ENCAPSULATION EXAMPLES");
        System.out.println("---------------------");

        // Example of encapsulation
        BankAccount account = new BankAccount("123456789", "John Doe", 1000.0);

        // Accessing data through public methods
        System.out.println("Account Information:");
        System.out.println("Account Number: " + account.getAccountNumber());
        System.out.println("Account Holder: " + account.getAccountHolder());
        System.out.println("Balance: $" + account.getBalance());

        // Modifying data through public methods
        System.out.println("\nDepositing $500...");
        account.deposit(500.0);
        System.out.println("New Balance: $" + account.getBalance());

        System.out.println("\nWithdrawing $200...");
        boolean withdrawalSuccess = account.withdraw(200.0);
        System.out.println("Withdrawal successful: " + withdrawalSuccess);
        System.out.println("New Balance: $" + account.getBalance());

        System.out.println("\nAttempting to withdraw $2000...");
        withdrawalSuccess = account.withdraw(2000.0);
        System.out.println("Withdrawal successful: " + withdrawalSuccess);
        System.out.println("Balance remains: $" + account.getBalance());

        // Trying to access private fields directly would cause compilation error
        // System.out.println(account.balance); // This would not compile

        System.out.println("\nACCESS MODIFIERS EXAMPLES");
        System.out.println("-------------------------");

        // Creating instances of classes with different access modifiers
        AccessModifiersDemo demo = new AccessModifiersDemo();

        // Accessing public members
        System.out.println("\nAccessing public members:");
        System.out.println("Public variable: " + demo.publicVar);
        demo.publicMethod();

        // Accessing protected members
        System.out.println("\nAccessing protected members (from subclass):");
        AccessModifiersSubclass subclass = new AccessModifiersSubclass();
        subclass.accessProtectedMembers();

        // Accessing default members
        System.out.println("\nAccessing default members (from same package):");
        System.out.println("Default variable: " + demo.defaultVar);
        demo.defaultMethod();

        // Accessing private members (only through public methods)
        System.out.println("\nAccessing private members (through public methods):");
        demo.accessPrivateMembers();

        System.out.println("\nPRACTICAL ENCAPSULATION EXAMPLE");
        System.out.println("-------------------------------");

        // Example of a well-encapsulated class with validation
        CompanyEmployee employee = new CompanyEmployee();

        // Setting valid values
        employee.setEmployeeId(1001);
        employee.setName("Alice Smith");
        employee.setAge(30);
        employee.setSalary(50000.0);

        System.out.println("\nEmployee after setting valid values:");
        System.out.println(employee);

        // Setting invalid values
        System.out.println("\nAttempting to set invalid values:");

        System.out.print("Setting negative employee ID: ");
        employee.setEmployeeId(-100);

        System.out.print("Setting null name: ");
        employee.setName(null);

        System.out.print("Setting invalid age: ");
        employee.setAge(150);

        System.out.print("Setting negative salary: ");
        employee.setSalary(-5000.0);

        System.out.println("\nEmployee after attempting to set invalid values:");
        System.out.println(employee);
    }
}

/**
 * Problem Statement:
 * Demonstrate encapsulation and the use of access modifiers in Java.
 * Show how encapsulation helps in data hiding, validation, and maintaining class invariants.
 * Explain the different access modifiers and their scope.
 * 
 * Solution 1: A well-encapsulated BankAccount class
 */
class BankAccount {
    // Private fields - data hiding
    private String accountNumber;
    private String accountHolder;
    private double balance;

    // Constructor
    public BankAccount(String accountNumber, String accountHolder, double initialBalance) {
        this.accountNumber = accountNumber;
        this.accountHolder = accountHolder;

        // Validation in constructor
        if (initialBalance < 0) {
            this.balance = 0;
        } else {
            this.balance = initialBalance;
        }
    }

    // Public getter methods
    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAccountHolder() {
        return accountHolder;
    }

    public double getBalance() {
        return balance;
    }

    // Public methods for operations
    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
        }
    }

    public boolean withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            return true;
        }
        return false;
    }

    // No setter for accountNumber - immutable after creation

    // Setter with validation for accountHolder
    public void setAccountHolder(String accountHolder) {
        if (accountHolder != null && !accountHolder.trim().isEmpty()) {
            this.accountHolder = accountHolder;
        }
    }
}

/**
 * Solution 2: Demonstrating different access modifiers
 */
class AccessModifiersDemo {
    // Public - accessible from anywhere
    public int publicVar = 10;

    // Protected - accessible within the same package and subclasses
    protected int protectedVar = 20;

    // Default (no modifier) - accessible within the same package
    int defaultVar = 30;

    // Private - accessible only within this class
    private int privateVar = 40;

    // Public method
    public void publicMethod() {
        System.out.println("This is a public method");
    }

    // Protected method
    protected void protectedMethod() {
        System.out.println("This is a protected method");
    }

    // Default method
    void defaultMethod() {
        System.out.println("This is a default method");
    }

    // Private method
    private void privateMethod() {
        System.out.println("This is a private method");
    }

    // Method to access private members
    public void accessPrivateMembers() {
        System.out.println("Private variable: " + privateVar);
        privateMethod();
    }
}

/**
 * Solution 3: Subclass to demonstrate protected access
 */
class AccessModifiersSubclass extends AccessModifiersDemo {
    public void accessProtectedMembers() {
        System.out.println("Protected variable from subclass: " + protectedVar);
        protectedMethod();
    }
}

/**
 * Solution 4: A well-encapsulated class with validation
 */
class CompanyEmployee {
    private int employeeId;
    private String name;
    private int age;
    private double salary;

    // Default constructor
    public CompanyEmployee() {
        this.employeeId = 0;
        this.name = "Unknown";
        this.age = 18;
        this.salary = 0.0;
    }

    // Getters and setters with validation
    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        if (employeeId > 0) {
            this.employeeId = employeeId;
        } else {
            System.out.println("Invalid employee ID. Must be positive.");
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name != null && !name.trim().isEmpty()) {
            this.name = name;
        } else {
            System.out.println("Invalid name. Cannot be null or empty.");
        }
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        if (age >= 18 && age <= 100) {
            this.age = age;
        } else {
            System.out.println("Invalid age. Must be between 18 and 100.");
        }
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        if (salary >= 0) {
            this.salary = salary;
        } else {
            System.out.println("Invalid salary. Cannot be negative.");
        }
    }

    @Override
    public String toString() {
        return "CompanyEmployee [employeeId=" + employeeId + 
               ", name=" + name + 
               ", age=" + age + 
               ", salary=" + salary + "]";
    }
}

/**
 * Logic Explanation:
 * 
 * Encapsulation:
 * 1. Definition: Bundling data (attributes) and methods (behavior) that operate on the data into a single unit (class)
 *    and restricting direct access to some of the object's components.
 * 
 * 2. Benefits:
 *    - Data Hiding: Private fields cannot be accessed directly from outside the class
 *    - Validation: Setters can validate data before changing field values
 *    - Flexibility: Implementation can change without affecting client code
 *    - Maintainability: Easier to maintain class invariants
 * 
 * 3. Implementation:
 *    - Declare fields as private
 *    - Provide public getter and setter methods
 *    - Add validation in setters and constructors
 * 
 * Access Modifiers:
 * 
 * 1. Public:
 *    - Accessible from any class
 *    - Use for methods and classes that need to be accessed from anywhere
 * 
 * 2. Protected:
 *    - Accessible within the same package and from subclasses
 *    - Use for methods and fields that should be accessible to subclasses but not to unrelated classes
 * 
 * 3. Default (no modifier):
 *    - Accessible only within the same package
 *    - Use for methods and classes that should be accessible only to related classes in the same package
 * 
 * 4. Private:
 *    - Accessible only within the same class
 *    - Use for implementation details that should be hidden from other classes
 * 
 * Best Practices:
 * - Make fields private unless there's a good reason not to
 * - Provide getters and setters only when necessary
 * - Add validation in setters to maintain object state integrity
 * - Use the most restrictive access modifier possible
 * 
 * Real-world Use Cases:
 * - Data validation in form processing
 * - Business rule enforcement in domain objects
 * - API design to hide implementation details
 * - Security to prevent unauthorized data access
 * - Framework development for extensibility
 */
