package com.example.studentprofilemanager.controller;

import com.example.studentprofilemanager.service.SessionManager;
import com.example.studentprofilemanager.Main;
import com.example.studentprofilemanager.model.Student;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class StudentDashboardController {

    @FXML private Label welcomeLabel;
    @FXML private Label avatarLabel;
    @FXML private Label fullNameLabel;
    @FXML private Label emailLabel;

    @FXML private Label studentIdValue;
    @FXML private Label courseValue;
    @FXML private Label sectionValue;
    @FXML private Label gpaValue;

    @FXML private Label fullNameDetailLabel;
    @FXML private Label usernameLabel;

    public void setStudent(Student student) {
        if (student == null) {
            return;
        }

        String fullName = student.getFullName() != null ? student.getFullName() : "Student";

        welcomeLabel.setText("Welcome back, " + fullName);
        fullNameLabel.setText(fullName);
        emailLabel.setText(student.getEmail() != null ? student.getEmail() : "");
        avatarLabel.setText(initial(fullName));

        studentIdValue.setText(student.getStudentId() != null ? student.getStudentId() : "-");
        courseValue.setText(student.getCourse() != null ? student.getCourse() : "-");
        sectionValue.setText(student.getYearLevel() + " - " +
                (student.getSection() != null ? student.getSection() : "-"));
        gpaValue.setText(String.format("%.2f", student.getGpa()));

        fullNameDetailLabel.setText(fullName);
        usernameLabel.setText(student.getUsername() != null ? student.getUsername() : "-");
    }

    private String initial(String fullName) {
        if (fullName == null || fullName.isBlank()) {
            return "S";
        }
        return fullName.trim().substring(0, 1).toUpperCase();
    }

    @FXML
    private void logout(ActionEvent event) {
        try {
            SessionManager.clearSession();
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/view/login.fxml"));
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Student Profile Manager");
            stage.centerOnScreen();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}