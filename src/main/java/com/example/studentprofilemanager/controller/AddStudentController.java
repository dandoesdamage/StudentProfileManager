package com.example.studentprofilemanager.controller;

import com.example.studentprofilemanager.model.Student;
import com.example.studentprofilemanager.repository.StudentRepository;
import com.example.studentprofilemanager.util.AppData;
import com.example.studentprofilemanager.util.Dialogs;
import com.example.studentprofilemanager.util.SceneNavigator;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

   // Collects a new student profile plus login credentials, validates it and

public class AddStudentController {

    @FXML private TextField studentIdField;
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField middleNameField;
    @FXML private ComboBox<String> genderCombo;
    @FXML private ComboBox<String> courseCombo;
    @FXML private ComboBox<String> yearLevelCombo;
    @FXML private ComboBox<String> sectionCombo;
    @FXML private TextField emailField;
    @FXML private TextField contactField;
    @FXML private TextField gpaField;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label statusLabel;

    private final StudentRepository repository = StudentRepository.getInstance();

    @FXML
    private void initialize() {
        genderCombo.setItems(FXCollections.observableArrayList(AppData.GENDERS));
        courseCombo.setItems(FXCollections.observableArrayList(AppData.COURSES));
        yearLevelCombo.setItems(FXCollections.observableArrayList(AppData.YEAR_LEVELS));
        sectionCombo.setItems(FXCollections.observableArrayList(AppData.SECTIONS));
    }

    @FXML
    private void save(ActionEvent event) {

        String error = validate();
        if (error != null) {
            showError(error);
            return;
        }

        String id = studentIdField.getText().trim();
        if (repository.findStudentById(id) != null) {
            showError("A student with ID " + id + " already exists.");
            return;
        }

        String username = usernameField.getText().trim();
        if (repository.usernameExists(username)) {
            showError("Username \"" + username + "\" is already taken.");
            return;
        }

        Student student = new Student();
        student.setStudentId(id);
        student.setFirstName(firstNameField.getText().trim());
        student.setLastName(lastNameField.getText().trim());
        student.setMiddleName(middleNameField.getText().trim());
        student.setGender(genderCombo.getValue());
        student.setCourse(courseCombo.getValue());
        student.setYearLevel(Integer.parseInt(yearLevelCombo.getValue()));
        student.setSection(sectionCombo.getValue());
        student.setEmail(emailField.getText().trim());
        student.setContactNumber(contactField.getText().trim());
        student.setGpa(parseGpa());
        student.setFullName(firstNameField.getText().trim()
                + " " + lastNameField.getText().trim());
        student.setUsername(username);
        student.setPassword(passwordField.getText());

        repository.addStudent(student);

        Dialogs.success("Student Added",
                student.getDisplayName() + " has been added successfully.");

        SceneNavigator.navigate(event, "/view/students.fxml", "Student Management");
    }

    @FXML
    private void clear(ActionEvent event) {
        studentIdField.clear();
        firstNameField.clear();
        lastNameField.clear();
        middleNameField.clear();
        emailField.clear();
        contactField.clear();
        gpaField.clear();
        usernameField.clear();
        passwordField.clear();
        genderCombo.getSelectionModel().clearSelection();
        courseCombo.getSelectionModel().clearSelection();
        yearLevelCombo.getSelectionModel().clearSelection();
        sectionCombo.getSelectionModel().clearSelection();
        statusLabel.setText("");
    }

    @FXML
    private void cancel(ActionEvent event) {
        SceneNavigator.navigate(event, "/view/students.fxml", "Student Management");
    }

    // Validation helpers

    private String validate() {
        if (isBlank(studentIdField.getText())) return "Student ID is required.";
        if (isBlank(firstNameField.getText())) return "First name is required.";
        if (isBlank(lastNameField.getText())) return "Last name is required.";
        if (genderCombo.getValue() == null) return "Please select a gender.";
        if (courseCombo.getValue() == null) return "Please select a course.";
        if (yearLevelCombo.getValue() == null) return "Please select a year level.";
        if (sectionCombo.getValue() == null) return "Please select a section.";
        if (isBlank(emailField.getText())) return "Email is required.";
        if (!emailField.getText().trim().matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            return "Please enter a valid email address.";
        }
        if (isBlank(contactField.getText())) return "Contact number is required.";
        String gpaError = validateGpa();
        if (gpaError != null) return gpaError;
        if (isBlank(usernameField.getText())) return "Username is required.";
        if (passwordField.getText() == null || passwordField.getText().isEmpty()) {
            return "Password is required.";
        }
        return null;
    }

    private String validateGpa() {
        if (isBlank(gpaField.getText())) return "GPA is required.";
        try {
            double gpa = Double.parseDouble(gpaField.getText().trim());
            if (gpa < 0.0 || gpa > 4.0) {
                return "GPA must be between 0.0 and 4.0.";
            }
        } catch (NumberFormatException ex) {
            return "GPA must be a valid number.";
        }
        return null;
    }

    private double parseGpa() {
        return Double.parseDouble(gpaField.getText().trim());
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private void showError(String message) {
        statusLabel.setText(message);
        statusLabel.getStyleClass().removeAll("status-success", "status-error", "status-info");
        statusLabel.getStyleClass().add("status-error");
        Dialogs.error("Invalid Input", message);
    }

    // Sidebar navigation

    @FXML
    private void goDashboard(ActionEvent event) {
        SceneNavigator.navigate(event, "/view/dashboard.fxml", "Dashboard");
    }

    @FXML
    private void goStudents(ActionEvent event) {
        SceneNavigator.navigate(event, "/view/students.fxml", "Student Management");
    }

    @FXML
    private void goAddStudent(ActionEvent event) {
        SceneNavigator.navigate(event, "/view/add-student.fxml", "Add Student");
    }

    @FXML
    private void goSearch(ActionEvent event) {
        SceneNavigator.navigate(event, "/view/search-student.fxml", "Search Students");
    }

    @FXML
    private void goReports(ActionEvent event) {
        SceneNavigator.navigate(event, "/view/reports.fxml", "Reports");
    }

    @FXML
    private void logout(ActionEvent event) {
        SceneNavigator.navigate(event, "/view/login.fxml", "Student Profile Manager");
    }
}