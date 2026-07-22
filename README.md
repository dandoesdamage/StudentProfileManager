# 🎓 Student Profile Manager

A JavaFX desktop application that allows administrators to efficiently manage student records through a modern and user-friendly interface, with role-based access for both administrators and students.

---

# 1. Software Overview

Student Profile Manager is a desktop application built with JavaFX and backed by a MySQL database (via XAMPP) through JDBC. It follows an MVC architecture with a repository layer for data access and supports two roles — Administrator and Student — each with their own dashboard and permissions. Administrators manage the full student record lifecycle, monitor live dashboard statistics, and generate detailed student reports, while students can log in to view their own profile information.

---

# 2. Major Features

- Administrator Login
- Student Login
- Student Registration
- Role-Based Access (Administrator & Student)
- Administrator Dashboard
- Student Dashboard
- Live Dashboard Statistics
- Detailed Student Reports
- View Student Records
- Add Student
- Update Student Information
- Delete Student Records
- Search Students
- Session Persistence via Java Serialization
- Modern JavaFX User Interface with Custom Branding
- JDBC Database Connectivity (MySQL/XAMPP)

---

# 3. Database Technology

The application uses MySQL (via XAMPP) as its persistence layer, accessed through JDBC. All database access is centralized in `DatabaseConnection`, which opens connections to the `student_profile_manager` database. Repositories (e.g., `StudentRepository`) use this connection to perform CRUD operations, retrieve live dashboard statistics, and generate report data while keeping SQL and connection details out of the controllers and services.

---

# 4. Serialization

The application uses Java Serialization to persist the logged-in user's session across screens through a dedicated `Session` model and `SessionManager` class.

- **Created after login:** After a user's credentials are successfully verified by `AuthenticationService`, `LoginController` creates a `Session` object containing the user's essential information (user ID, username, full name, and role). `SessionManager` then serializes this object into a `session.dat` file.

- **Maintains the session while navigating:** Protected pages such as the Administrator Dashboard, Student Dashboard, Student Management, Add Student, Update Student, Search Students, and Reports validate the existence of `session.dat` through `SessionManager` before allowing access. If no valid session exists, the user is redirected to the Login screen.

- **Deleted on logout:** When either an Administrator or Student logs out, `SessionManager.clearSession()` automatically deletes the `session.dat` file before returning the user to the Login screen, ensuring the session is properly terminated.

---

# 5. SOLID Principles

Two SOLID principles are demonstrated in the project's implementation.

## Single Responsibility Principle (SRP)

**Classes involved:** `SessionManager`, `DatabaseConnection`, `SceneNavigator`, `AuthenticationService`

Each of these classes has a single, well-defined responsibility. `SessionManager` manages the creation, validation, loading, and deletion of serialized user sessions. `DatabaseConnection` is responsible only for establishing JDBC database connections. `SceneNavigator` manages screen navigation while delegating session validation to `SessionManager`. `AuthenticationService` handles only user authentication during login.

**Benefits:** Applying SRP keeps each class focused on one responsibility, making the code easier to understand, maintain, test, and debug. Changes to session management, database connectivity, navigation, or authentication can be made independently without affecting other components of the application.

---

## Open/Closed Principle (OCP)

**Classes involved:** `User`, `Administrator`, `Student`

The application follows the Open/Closed Principle through its user hierarchy. `User` serves as the abstract base class that defines the common attributes and behaviors shared by all system users. `Administrator` and `Student` extend this base class by implementing role-specific functionality without modifying the existing `User` class.

**Benefits:** This design allows the application to be extended by introducing new user roles without changing existing classes. It improves scalability, reduces the risk of introducing bugs into existing functionality, and promotes a flexible and maintainable architecture.

---

# 6. Project Structure

```
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
│   │   │       │   ├── Course.java
│   │   │       │   ├── Session.java
│   │   │       │   ├── Student.java
│   │   │       │   └── User.java
│   │   │       │
│   │   │       ├── repository
│   │   │       │   ├── CourseRepository.java
│   │   │       │   ├── StudentRepository.java
│   │   │       │   └── UserRepository.java
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
```