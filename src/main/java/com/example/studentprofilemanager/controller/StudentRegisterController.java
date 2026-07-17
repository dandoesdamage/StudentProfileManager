package com.example.studentprofilemanager.controller;

import com.example.studentprofilemanager.Main;
import com.example.studentprofilemanager.model.Student;
import com.example.studentprofilemanager.repository.StudentRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class StudentRegisterController {

    @FXML private TextField studentIdField;
    @FXML private TextField fullNameField;
    @FXML private TextField emailField;
    @FXML private TextField courseField;
    @FXML private TextField yearLevelField;
    @FXML private TextField sectionField;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;

    private final StudentRepository studentRepository = StudentRepository.getInstance();

    @FXML
    private void register(ActionEvent event) {

        String studentId = safe(studentIdField.getText());
        String fullName = safe(fullNameField.getText());
        String email = safe(emailField.getText());
        String course = safe(courseField.getText());
        String section = safe(sectionField.getText());
        String username = safe(usernameField.getText());
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (studentId.isEmpty() || fullName.isEmpty() || username.isEmpty()
                || password == null || password.isEmpty()) {
            showError("Missing Information", "Please fill in Student ID, Full Name, Username and Password.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showError("Password Mismatch", "Password and Confirm Password do not match.");
            return;
        }

        int yearLevel;
        try {
            yearLevel = Integer.parseInt(safe(yearLevelField.getText()));
        } catch (NumberFormatException e) {
            showError("Invalid Year Level", "Year Level must be a number.");
            return;
        }

        if (studentRepository.studentIdExists(studentId)) {
            showError("Registration Failed", "A student with this Student ID already exists.");
            return;
        }

        if (studentRepository.usernameExists(username)) {
            showError("Registration Failed", "This username is already taken.");
            return;
        }

        Student student = new Student();
        student.setUserId(studentRepository.nextUserId());
        student.setUsername(username);
        student.setPassword(password);
        student.setFullName(fullName);
        student.setEmail(email);
        student.setStudentId(studentId);
        student.setCourse(course);
        student.setYearLevel(yearLevel);
        student.setSection(section);
        student.setGpa(0.0);

        studentRepository.addStudent(student);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Registration Successful");
        alert.setContentText("You can now log in with your new account.");
        alert.showAndWait();

        goToLogin(event);
    }

    @FXML
    private void cancel(ActionEvent event) {
        goToLogin(event);
    }

    private void goToLogin(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/view/login.fxml"));
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Student Profile Manager");
            stage.centerOnScreen();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showError(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private String safe(String value) {
        return value == null ? "" : value.trim();
    }
}