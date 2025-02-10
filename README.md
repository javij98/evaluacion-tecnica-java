# 📖 Library Management System - Project Structure & Functionality

## 🏛 Project Architecture Overview

The project follows a **layered architecture**, which organizes the codebase into distinct layers to separate concerns and promote maintainability. Here's how the application is structured:

### Project Structure
```
.
├── pom.xml                       # Maven configuration file
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com.example.demo  # Main application package
│   │   │       ├── EvTecnicaApplication.java  # Main Spring Boot application class
│   │   │       ├── book                        # Book module
│   │   │       │   ├── controller              # REST controllers for book-related endpoints
│   │   │       │   ├── dao                     # Data access objects for books
│   │   │       │   ├── dto                     # Data Transfer Objects for books
│   │   │       │   ├── exception               # Custom exceptions for books
│   │   │       │   ├── mapper                  # Mappers for converting between entities and DTOs
│   │   │       │   ├── repository              # JPA repositories for books
│   │   │       │   └── service                 # Service layer handling business logic for books
│   │   │       ├── loan                        # Loan module (similar structure to Book)
│   │   │       └── user                        # User module (similar structure to Book)
│   │   └── resources
│   │       ├── application.properties          # Configuration properties
│   │       ├── static                          # Static files (if any)
│   │       └── templates                       # Templates for views (if applicable)
│   └── test
│       └── java
│           └── com.example.demo                # Test package
│               ├── EvTecnicaApplicationTests.java  # Application tests
│               ├── book
│               │   ├── controller              # Tests for book controllers
│               │   └── service                 # Tests for book services
│               ├── loan                        # Tests for loan module
│               └── user                        # Tests for user module
```

### Key Layers
- **Controller Layer**: Handles incoming HTTP requests and maps them to the corresponding service methods.
- **Service Layer**: Contains the core business logic and validation before interacting with the database.
- **DAO/Repository Layer**: Responsible for data persistence using JPA.
- **DTOs and Mappers**: Facilitate data transformation between the internal models and the external API representations.
- **Exception Handling**: Each module includes custom exceptions to handle specific error cases.

---

## ⚙ Exception Handling
Custom exceptions are implemented for better error management across modules:

- `BookException`
- `UserException`
- `LoanException`

Each exception includes:
- **HTTP Error Codes** (e.g., 400, 404)
- **Descriptive Messages** outlining the issue
- **Logging** for easier debugging

---

## 🔧 Key Validations
- **Book**: Must have a title, author, and ISBN to be saved. Cannot be deleted if it has active loans.
- **User**: Requires name, email, and phone number to be saved. Cannot be deleted if they have active loans.
- **Loan**:
  - Both the book and user must exist before creating a loan.
  - A book cannot have multiple active loans.
  - The return date cannot be earlier than the loan date.

---

## 🔧 Setup & Execution

### 1️⃣ Clone the Repository
```bash
git clone https://github.com/your-username/library-management-system.git
cd library-management-system
```

### 2️⃣ Build the Project with Maven
```bash
mvn clean install
```

### 3️⃣ Run the Application
```bash
mvn spring-boot:run
```

---

## 🛠 Testing
To execute unit tests:
```bash
mvn test
```

---

## 📌 Key Endpoints

### 📖 Books
| Method | Endpoint         | Description              |
|--------|------------------|--------------------------|
| GET    | /books           | Retrieve all books       |
| GET    | /books/{id}      | Retrieve a book by ID    |
| POST   | /books           | Create a new book        |
| PUT    | /books/{id}      | Fully update a book      |
| PATCH  | /books/{id}      | Partially update a book  |
| DELETE | /books/{id}      | Delete a book            |

### 👤 Users
| Method | Endpoint          | Description               |
|--------|-------------------|---------------------------|
| GET    | /users            | Retrieve all users        |
| GET    | /users/{id}       | Retrieve a user by ID     |
| POST   | /users            | Create a new user         |
| PUT    | /users/{id}       | Fully update a user       |
| PATCH  | /users/{id}       | Partially update a user   |
| DELETE | /users/{id}       | Delete a user             |

### 🔄 Loans
| Method | Endpoint          | Description                |
|--------|-------------------|----------------------------|
| GET    | /loans            | Retrieve all loans         |
| GET    | /loans/{id}       | Retrieve a loan by ID      |
| POST   | /loans            | Create a new loan          |
| PUT    | /loans/{id}       | Fully update a loan        |
| PATCH  | /loans/{id}       | Partially update a loan    |
| DELETE | /loans/{id}       | Delete a loan              |

---

## 🔬 Sample Request Bodies for Postman Testing

### 📙 Books
**POST /books**
```json
{
  "title": "The Hobbit",
  "author": "J.R.R. Tolkien",
  "isbn": "978-0345339683",
  "publicationDate": "1937-09-21"
}
```

### 👤 Users
**PUT /users/1**
```json
{
  "name": "Carlos Sanchez",
  "email": "carlos.sanchez@example.com",
  "phone": "123456789",
  "registrationDate": "2022-01-15"
}
```

### ♻ Loans
**PATCH /loans/2**
```json
{
  "loanDate": "2025-02-10",
  "returnDate": "2025-03-10"
}
```

---

## 🔧 Technologies Used
- **Java 17**
- **Spring Boot**
- **Spring Data JPA**
- **H2 Database** (in-memory database for development)
- **Maven** for project management
- **JUnit 5** for testing

---

## 🖇 Contribution
Feel free to fork the repository and submit pull requests. For any issues, please open a ticket in the [Issues](https://github.com/your-username/library-management-system/issues) section.

---

## 📅 License
This project is licensed under the MIT License.

---

**Developed by:** Javier Jiménez Molina  
**GitHub:** [your-username](https://github.com/javij98)

