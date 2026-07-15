🎓 Student Profile Manager

A JavaFX desktop application that allows administrators to efficiently manage student records through a modern and user-friendly interface.

✨ Features

- Administrator Login
- Dashboard
- View Student Records
- Add Student
- Update Student Information
- Delete Student Records
- Search Students
- Modern JavaFX User Interface

🛠️ Built With

- Java
- JavaFX
- FXML
- CSS
- Maven
- Git & GitHub

📁 Project Structure

```
src
└── main
    ├── java
    │   └── com.example.studentprofilemanager
    │       ├── controller      * Application controllers
    │       ├── model           * Student model/classes
    │       ├── repository      * Data management
    │       ├── service         * Business logic
    │       ├── util            * Helper and utility classes
    │       ├── database        * Database connection (future)
    │       └── Main.java       * Application entry point
    │
    └── resources
        ├── css                 * Stylesheets
        └── view                * FXML user interfaces
        
```

 📌 Notes
The current version stores data temporarily in memory. Future versions will integrate a persistent database using MySQL/MariaDB through XAMPP.