# Database Setup

This folder contains the SQL database for the **Student Profile Manager** application.

## Requirements

- XAMPP (Apache and MySQL)
- phpMyAdmin (included with XAMPP)

## Installation

1. Open the **XAMPP Control Panel**.
2. Start the **MySQL** service.
3. Open your web browser and go to:
   ```
   http://localhost/phpmyadmin
   ```
4. Click **New** and create a database named:
   ```
   student_profile_manager
   ```
5. Select the newly created database.
6. Go to the **Import** tab.
7. Click **Choose File** and select:
   ```
   student_profile_manager.sql
   ```
8. Click **Import** and wait for the process to complete.

## Database Connection

Ensure your application uses the correct database credentials.

Example:

```properties
URL=jdbc:mysql://localhost:3306/student_profile_manager
USERNAME=root
PASSWORD=
```

> **Note:** By default, XAMPP's MySQL user is `root` with an empty password.

## Notes

- Make sure the **MySQL** service is running before starting the application.
- If you change the database name, update the JDBC connection string in the project.
- The SQL file contains the database schema and any sample data required by the application.

## Folder Structure

```
database/
├── student_profile_manager.sql
└── README.md
```
