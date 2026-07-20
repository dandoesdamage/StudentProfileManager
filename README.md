# 🎓 Student Profile Manager

A JavaFX desktop application that allows administrators to efficiently manage student records through a modern and user-friendly interface, with role-based access for both administrators and students.

## 1. Software Overview

Student Profile Manager is a desktop application built with JavaFX and backed by a MySQL database (via XAMPP) through JDBC. It follows an MVC architecture with a repository layer for data access, and supports two roles — Administrator and Student — each with their own dashboard and permissions. Administrators manage the full student record lifecycle, while students can log in to view their own profile information.

## 2. Major Features

- Administrator Login
- Student Login
- Student Registration
- Role-Based Access (Administrator & Student)
- Administrator Dashboard
- Student Dashboard
- View Student Records
- Add Student
- Update Student Information
- Delete Student Records
- Search Students
- Session Persistence via Java Serialization
- Modern JavaFX User Interface
- JDBC Database Connectivity (MySQL/XAMPP)
- Live Dashboard Statistics
- Detailed Student Reports
- 

## 3. Database Technology

The application uses **MySQL (via XAMPP)** as its persistence layer, accessed through **JDBC**. All database access is centralized in `DatabaseConnection`, which opens connections to the `student_profile_manager` database. Repositories (e.g., `StudentRepository`) use this connection to perform CRUD operations, keeping SQL and connection details out of the controllers and services.

## 4. Serialization

The application uses Java Serialization to persist the logged-in user's session across screens, via a dedicated `Session` model and `SessionManager` class.

- **Created after login:** Once `AuthenticationService` verifies a user's credentials, `LoginController` builds a minimal `Session` object (user ID, username, full name, and role) and calls `SessionManager.createSession(...)`, which serializes it to `session.dat`.
- **Maintains the session while navigating:** Protected screens (Student Management, Add Student, Search Students, Update Student) are only ever reached through `SceneNavigator`. Before loading any of them, `SceneNavigator` calls `SessionManager.hasValidSession()` to confirm `session.dat` exists and can be deserialized. If it can't, the user is redirected to the Login screen instead of the requested page.
- **Deleted on logout:** Both `DashboardController` (administrator) and `StudentDashboardController` (student) call `SessionManager.clearSession()` on logout, which deletes `session.dat` before returning the user to the Login screen.

## 5. SOLID Principles

Two SOLID principles are demonstrated in the existing codebase:

**Single Responsibility Principle (SRP)**
- **Classes involved:** `SessionManager`, `DatabaseConnection`, `SceneNavigator`, `AuthenticationService`
- Each class has exactly one reason to change: `SessionManager` only serializes/deserializes/deletes `session.dat`; `DatabaseConnection` only opens JDBC connections; `SceneNavigator` only swaps scenes (delegating session checks to `SessionManager` rather than duplicating that logic); `AuthenticationService` only verifies credentials.
- **Benefits:** Each concern can be modified or tested in isolation, bugs are easier to trace to a single class, and the concerns (persistence, navigation, authentication) stay decoupled from one another.

**Open/Closed Principle (OCP)**
- **Classes involved:** `User` (abstract base class), `Administrator`, `Student`
- `User` defines the shared contract (`userId`, `username`, `password`, `fullName`, `email`, and abstract `login()`/`logout()` methods). `Administrator` and `Student` extend it with role-specific behavior without modifying `User` itself.
- **Benefits:** New roles can be added as new subclasses without risking existing `Administrator`/`Student` behavior, keeping the class hierarchy stable as the application grows.

## 6. Project Structure

````
StudentProfileManager
│
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com.example.studentprofilemanager
│   │   │       ├── controller
│   │   │       │   ├── AddStudentController.java
│   │   │       │   ├── DashboardController.java
│   │   │       │   ├── LoginController.java
│   │   │       │   ├── ReportController.java
│   │   │       │   ├── SearchStudentController.java
│   │   │       │   ├── StudentDashboardController.java
│   │   │       │   ├── StudentManagementController.java
│   │   │       │   ├── StudentRegisterController.java
│   │   │       │   └── UpdateStudentController.java
│   │   │       │
│   │   │       ├── db
│   │   │       │   └── DatabaseConnection.java
│   │   │       │
│   │   │       ├── model
│   │   │       │   ├── Administrator.java
│   │   │       │   ├── Session.java
│   │   │       │   ├── Student.java
│   │   │       │   └── User.java
│   │   │       │
│   │   │       ├── repository
│   │   │       │   └── StudentRepository.java
│   │   │       │
│   │   │       ├── service
│   │   │       │   ├── AuthenticationService.java
│   │   │       │   └── SessionManager.java
│   │   │       │
│   │   │       ├── util
│   │   │       │   ├── AppData.java
│   │   │       │   ├── Components.java
│   │   │       │   ├── Dialogs.java
│   │   │       │   └── SceneNavigator.java
│   │   │       │
│   │   │       ├── Main.java
│   │   │       └── module-info.java
│   │   │
│   │   └── resources
│   │       ├── css
│   │       │   └── style.css
│   │       │
│   │       ├── images
│   │       │   └── logo.png
│   │       │
│   │       └── view
│   │           ├── login.fxml
│   │           ├── register.fxml
│   │           ├── dashboard.fxml
│   │           ├── reports.fxml
│   │           ├── student-dashboard.fxml
│   │           ├── students.fxml
│   │           ├── add-student.fxml
│   │           ├── update-student.fxml
│   │           └── search-student.fxml
│   │
│   └── target
│
├── pom.xml
├── .gitignore
└── README.md
````