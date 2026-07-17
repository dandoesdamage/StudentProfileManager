🎓 Student Profile Manager

A JavaFX desktop application that allows administrators to efficiently manage student records through a modern and user-friendly interface.

✨ Features

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
- Modern JavaFX User Interface
- JDBC Database Connectivity (MySQL/XAMPP)

🛠️ Built With

- Java
- JavaFX
- FXML
- CSS
- Maven
- Git & GitHub

📁 Project Structure

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
│   │   │       │   ├── Student.java
│   │   │       │   └── User.java
│   │   │       │
│   │   │       ├── repository
│   │   │       │   └── StudentRepository.java
│   │   │       │
│   │   │       ├── service
│   │   │       │   └── AuthenticationService.java
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
│   │       └── view
│   │           ├── login.fxml
│   │           ├── register.fxml
│   │           ├── dashboard.fxml
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

**Note:** This project uses **MySQL/MariaDB (XAMPP)** with **JDBC** for persistent data storage and database connectivity.