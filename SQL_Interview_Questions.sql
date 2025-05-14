/*
SQL INTERVIEW QUESTIONS AND ANSWERS
==================================

This file contains SQL interview questions and their solutions with explanations.
*/

/*
TABLE CREATION FOR QUESTION 1:
------------------------------
Below are the CREATE TABLE statements for the Account and Policy tables used in Question 1.
These can be executed to set up the database schema for testing.
*/

CREATE TABLE Account (
                         ID INT PRIMARY KEY,
                         AccountNumber VARCHAR(50) NOT NULL
);

CREATE TABLE Policy (
                        ID INT PRIMARY KEY,
                        AccountID INT,
                        PolicyNumber VARCHAR(50) NOT NULL,
                        FOREIGN KEY (AccountID) REFERENCES Account(ID)
);

/*
QUESTION 1:
-----------
List policy numbers of a specific account "1234" given the table structure below:

Account
- ID (int, PK)
- AccountNumber (varchar)

Policy
- ID (int, PK)
- AccountID (int, FK)
- PolicyNumber (varchar)

TABLE STRUCTURE DIAGRAM (TEXTUAL REPRESENTATION):
------------------------------------------------
Below is a textual diagram of the Account and Policy tables, showing columns, data types, and constraints.

Account Table:
+---------------+------------------+--------------------+
| Column        | Data Type        | Constraints        |
+---------------+------------------+--------------------+
| ID            | INT              | PRIMARY KEY        |
| AccountNumber | VARCHAR(50)      | NOT NULL           |
+---------------+------------------+--------------------+

Policy Table:
+---------------+------------------+--------------------+
| Column        | Data Type        | Constraints        |
+---------------+------------------+--------------------+
| ID            | INT              | PRIMARY KEY        |
| AccountID     | INT              | FOREIGN KEY (Account.ID) |
| PolicyNumber  | VARCHAR(50)      | NOT NULL           |
+---------------+------------------+--------------------+

Relationship:
- Policy.AccountID references Account.ID (Foreign Key relationship)
*/

-- ANSWER 1 (Primary Solution - INNER JOIN):
-- This query retrieves all policy numbers associated with account number "1234"
-- It joins the Account and Policy tables on AccountID to find the matching policies

SELECT p.PolicyNumber
FROM Policy p
         JOIN Account a ON p.AccountID = a.ID
WHERE a.AccountNumber = '1234';

-- Alternative Solution 1 (Subquery):
SELECT PolicyNumber
FROM Policy
WHERE AccountID = (SELECT ID FROM Account WHERE AccountNumber = '1234');

-- Alternative Solution 2 (LEFT JOIN - Includes accounts with no policies):
SELECT a.AccountNumber, p.PolicyNumber
FROM Account a
         LEFT JOIN Policy p ON p.AccountID = a.ID
WHERE a.AccountNumber = '1234';

-- Alternative Solution 3 (EXISTS):
SELECT PolicyNumber
FROM Policy p
WHERE EXISTS (
    SELECT 1
    FROM Account a
    WHERE a.ID = p.AccountID
      AND a.AccountNumber = '1234'
);

/*
EXPLANATION:
- Primary Solution (INNER JOIN):
  - Uses an INNER JOIN to connect the Account and Policy tables.
  - Joins on the AccountID foreign key in Policy and the ID primary key in Account.
  - The WHERE clause filters for the specific account number "1234".
  - Returns only the PolicyNumber column from matching records.
  - Note: Will not return the account if it has no policies (due to INNER JOIN).

- Alternative 1 (Subquery):
  - Uses a subquery to first find the Account ID for account "1234".
  - Then filters the Policy table using that ID.
  - Works well for single-value lookups but may be less efficient for large datasets.

- Alternative 2 (LEFT JOIN):
  - Uses a LEFT JOIN to include the account even if it has no policies.
  - Returns the AccountNumber and PolicyNumber (PolicyNumber will be NULL if no policies exist).
  - Useful when you need to confirm the account exists even if it has no policies.

- Alternative 3 (EXISTS):
  - Uses EXISTS to check for a matching account without retrieving the Account data.
  - Can be more efficient than a subquery in some databases (e.g., when the subquery returns multiple rows).
  - Similar to the INNER JOIN in terms of results (excludes accounts with no policies).

- Performance Notes:
  - INNER JOIN vs. Subquery: JOIN is often more efficient for larger datasets as it can use indexes effectively.
  - EXISTS vs. Subquery: EXISTS can stop as soon as a match is found, potentially faster for large tables.
  - LEFT JOIN: Useful for completeness but may be slower due to including non-matching rows.

- Indexing Recommendations:
  - Index on Account(AccountNumber) to speed up the WHERE clause.
  - Index on Policy(AccountID) to improve JOIN performance.
  - Primary keys (Account.ID, Policy.ID) are typically indexed by default.
*/


/*
TABLE CREATION FOR QUESTION 2:
------------------------------
Below are the CREATE TABLE statements for the Contact and Address tables used in Question 2.
These can be executed to set up the database schema for testing.
*/

CREATE TABLE Contact (
                         ID INT PRIMARY KEY,
                         Name VARCHAR(100) NOT NULL,
                         Mobile VARCHAR(20),
                         Email VARCHAR(100)
);

CREATE TABLE Address (
                         ID INT PRIMARY KEY,
                         ContactID INT,
                         AddressLine VARCHAR(255) NOT NULL,
                         City VARCHAR(100) NOT NULL,
                         PIN VARCHAR(10) NOT NULL,
                         FOREIGN KEY (ContactID) REFERENCES Contact(ID)
);

/*
QUESTION 2:
-----------
List addresses of a specific contact "Will Smith" given the table structure below:

Contact
- ID (int, PK)
- Name (varchar)
- Mobile (varchar)
- Email (varchar)

Address
- ID (int, PK)
- ContactID (int, FK)
- AddressLine (varchar)
- City (varchar)
- PIN (varchar)

TABLE STRUCTURE DIAGRAM (TEXTUAL REPRESENTATION):
------------------------------------------------
Below is a textual diagram of the Contact and Address tables, showing columns, data types, and constraints.

Contact Table:
+---------------+------------------+--------------------+
| Column        | Data Type        | Constraints        |
+---------------+------------------+--------------------+
| ID            | INT              | PRIMARY KEY        |
| Name          | VARCHAR(100)     | NOT NULL           |
| Mobile        | VARCHAR(20)      |                    |
| Email         | VARCHAR(100)     |                    |
+---------------+------------------+--------------------+

Address Table:
+---------------+------------------+--------------------+
| Column        | Data Type        | Constraints        |
+---------------+------------------+--------------------+
| ID            | INT              | PRIMARY KEY        |
| ContactID     | INT              | FOREIGN KEY (Contact.ID) |
| AddressLine   | VARCHAR(255)     | NOT NULL           |
| City          | VARCHAR(100)     | NOT NULL           |
| PIN           | VARCHAR(10)      | NOT NULL           |
+---------------+------------------+--------------------+

Relationship:
- Address.ContactID references Contact.ID (Foreign Key relationship)
*/

-- ANSWER 2 (Primary Solution - INNER JOIN):
-- This query retrieves all addresses associated with the contact "Will Smith"
-- It joins the Contact and Address tables to find all addresses for this specific person

SELECT a.AddressLine, a.City, a.PIN
FROM Address a
         JOIN Contact c ON a.ContactID = c.ID
WHERE c.Name = 'Will Smith';

-- Alternative Solution 1 (Case-Insensitive Search):
SELECT a.AddressLine, a.City, a.PIN
FROM Address a
         JOIN Contact c ON a.ContactID = c.ID
WHERE UPPER(c.Name) = UPPER('Will Smith');

-- Alternative Solution 2 (LEFT JOIN - Includes contacts with no addresses):
SELECT c.Name, a.AddressLine, a.City, a.PIN
FROM Contact c
         LEFT JOIN Address a ON a.ContactID = c.ID
WHERE c.Name = 'Will Smith';

-- Alternative Solution 3 (Concatenated Address Output):
SELECT CONCAT(a.AddressLine, ', ', a.City, ', ', a.PIN) AS FullAddress
FROM Address a
         JOIN Contact c ON a.ContactID = c.ID
WHERE c.Name = 'Will Smith';

/*
EXPLANATION:
- Primary Solution (INNER JOIN):
  - Uses an INNER JOIN to connect the Contact and Address tables.
  - Joins on the ContactID foreign key in Address and the ID primary key in Contact.
  - The WHERE clause filters for contacts with the name "Will Smith".
  - Returns address details (AddressLine, City, PIN) for all matching records.
  - Note: Will not return the contact if they have no addresses (due to INNER JOIN).

- Alternative 1 (Case-Insensitive Search):
  - Modifies the WHERE clause to use UPPER() for case-insensitive matching.
  - Ensures "Will Smith", "will smith", or "WILL SMITH" all match.
  - Useful when the database collation is case-sensitive.

- Alternative 2 (LEFT JOIN):
  - Uses a LEFT JOIN to include the contact even if they have no addresses.
  - Returns the contact’s Name and address fields (AddressLine, City, PIN will be NULL if no addresses exist).
  - Useful to confirm the contact exists even if they have no addresses.

- Alternative 3 (Concatenated Address):
  - Uses CONCAT to combine AddressLine, City, and PIN into a single column (e.g., "123 Main St, Los Angeles, 90001").
  - Same INNER JOIN logic as the primary solution.
  - Useful for simplified output or reporting purposes.

- Performance Notes:
  - INNER JOIN vs. LEFT JOIN: INNER JOIN is generally faster as it excludes non-matching rows.
  - UPPER() Function: Adds slight overhead; consider a case-insensitive collation if this is a common need.
  - CONCAT: Minimal overhead, but formatting in SQL may be better handled in the application layer for flexibility.

- Indexing Recommendations:
  - Index on Contact(Name) to speed up the WHERE clause.
  - Index on Address(ContactID) to improve JOIN performance.
  - Primary keys (Contact.ID, Address.ID) are typically indexed by default.
*/