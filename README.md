# Library Management System

**Version:** 1.1
**Project Type:** Java Desktop Application
**Architecture:** Model-View-Controller (MVC)

## 1. Project Overview

The Library Management System is a Java-based desktop application developed using Java Swing, SQLite, and JDBC.

The application provides a graphical interface for managing books, library members, and book lending and returning operations. It follows the Model-View-Controller (MVC) architectural pattern to separate the user interface, application logic, and database operations.

## 2. Technologies Used

- **Programming Language:** Java
- **GUI Framework:** Java Swing
- **Database:** SQLite
- **Database Connectivity:** JDBC (SQLite JDBC Driver)
- **Architecture:** Model-View-Controller (MVC)
- **JDK Version:** 17 or newer

## 3. Application Features

- Dashboard displaying library statistics.
- Book registration and inventory management.
- Member registration and contact information management.
- Book issuing with an automatically calculated seven-day due date.
- Book return management.
- Automatic overdue fine calculation at ₹5 per day.
- Book search by title or author.
- Input validation for book and member details.
- Database transaction handling for book issuing and returning.
- Automatic database and table creation when required.

## 4. MVC Architecture

The application follows the MVC design pattern.

| Component | Java File | Responsibility |
|---|---|---|
| Model | `LibraryModel.java` | Database connectivity, SQL operations, and business logic. |
| View | `LibraryView.java` | Graphical user interface developed using Java Swing. |
| Controller | `LibraryController.java` | Handles user actions, validation, and communication between Model and View. |
| Application Entry Point | `LibraryManagementSystem.java` | Initializes and connects the application components. |

## 5. Project Files

The ZIP archive contains the following files:

- `LibraryManagementSystem.java`
- `LibraryModel.java`
- `LibraryView.java`
- `LibraryController.java`
- `sqlite-jdbc-3.53.4.0.jar`
- `README.md`

The SQLite database file (`library.db`) is created automatically when the application starts if it does not already exist.

## 6. System Requirements

- Java Development Kit (JDK) 17 or newer.
- Java compiler (`javac`) and Java runtime (`java`).
- SQLite JDBC driver included in the project ZIP.
- Windows, macOS, or Linux operating system.

No separate MySQL installation or SQLite server is required.

## 7. Compilation and Execution

Extract the project ZIP into a folder and open a terminal or command prompt in that folder.

### Step 1: Verify Java Installation

```bash
java -version
javac -version
```

### Step 2: Compile the Java Source Files

**Windows:**

```cmd
javac -cp ".;sqlite-jdbc-3.53.4.0.jar" LibraryManagementSystem.java LibraryModel.java LibraryView.java LibraryController.java
```

**macOS / Linux:**

```bash
javac -cp ".:sqlite-jdbc-3.53.4.0.jar" LibraryManagementSystem.java LibraryModel.java LibraryView.java LibraryController.java
```

### Step 3: Run the Application

**Windows:**

```cmd
java -cp ".;sqlite-jdbc-3.53.4.0.jar" LibraryManagementSystem
```

**macOS / Linux:**

```bash
java -cp ".:sqlite-jdbc-3.53.4.0.jar" LibraryManagementSystem
```

Alternatively, use the provided launcher scripts:

- Windows: Double-click `run.bat`.
- macOS: Double-click `run.command` (or execute it from Terminal).
- Linux: Use the compilation and execution commands above, or execute the launcher if supported by your environment.

## 8. Database Information

The application uses SQLite as its database.

- Database filename: `library.db`
- Database creation: Automatic.
- Table creation: Automatic when the application initializes the database.
- Database connectivity: JDBC using the included SQLite JDBC driver.

The database stores book information, member information, and loan records.

The database file is generated in the application's working directory. Back up the database before manually modifying or deleting it.

## 9. Input Validation and Data Integrity

The application includes validation to help maintain accurate records.

- Book titles and author names are validated.
- Book quantities are restricted to positive values within the configured limit.
- Member names are validated.
- Email addresses are checked for valid formatting.
- Indian mobile numbers are validated as ten-digit numbers.
- Invalid book and member IDs are rejected during lending operations.
- Duplicate book returns are prevented.
- Database transactions help maintain consistency during issuing and returning operations.

## 10. Troubleshooting

**Problem: `javac` is not recognized**

Install JDK 17 or newer and configure the system PATH.

**Problem: SQLite JDBC driver not found**

Ensure `sqlite-jdbc-3.53.4.0.jar` is present in the same folder as the Java source files.

**Problem: Application does not launch**

Verify that compilation completed successfully and that the correct classpath is used when running the application.

**Problem: Database is missing**

The application should automatically create `library.db` during initialization. Ensure the application has permission to write to its working directory.

---

**Project:** Library Management System
**Architecture:** MVC
**GUI:** Java Swing
**Database Integration:** SQLite using JDBC
