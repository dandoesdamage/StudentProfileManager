package com.example.studentprofilemanager.controller;

import com.example.studentprofilemanager.Main;
import com.example.studentprofilemanager.repository.CourseRepository;
import com.example.studentprofilemanager.repository.StudentRepository;
import com.example.studentprofilemanager.repository.UserRepository;
import com.example.studentprofilemanager.service.SessionManager;
import com.example.studentprofilemanager.util.SceneNavigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardController {

    /* ------------------------------------------------------------------ */
    /* Statistics cards (additive — populated from the database)           */
    /* ------------------------------------------------------------------ */

    @FXML
    private Label totalStudentsValue;

    @FXML
    private Label activeCoursesValue;

    @FXML
    private Label totalUsersValue;

    @FXML
    private Label averageGpaValue;

    /**
     * Called automatically by the FXMLLoader after fields are injected.
     * Retrieves live counts/averages from the repositories and displays
     * them — no SQL here, only retrieval + formatting.
     */
    @FXML
    private void initialize() {
        totalStudentsValue.setText(
                String.valueOf(StudentRepository.getInstance().countStudents())
        );

        activeCoursesValue.setText(
                String.valueOf(CourseRepository.getInstance().countCourses())
        );

        totalUsersValue.setText(
                String.valueOf(UserRepository.getInstance().countUsers())
        );

        averageGpaValue.setText(
                String.format("%.2f", StudentRepository.getInstance().getAverageGpa())
        );
    }

    /* ------------------------------------------------------------------ */
    /* Sidebar navigation (additive — routes to the management screens)    */
    /* ------------------------------------------------------------------ */

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
    private void logout(ActionEvent event) throws IOException {

        SessionManager.clearSession();

        FXMLLoader loader = new FXMLLoader(
                Main.class.getResource("/view/login.fxml")
        );

        Scene scene = new Scene(loader.load());

        Stage stage = (Stage) ((javafx.scene.Node) event.getSource())
                .getScene()
                .getWindow();

        stage.setScene(scene);
        stage.setTitle("Student Profile Manager");
        stage.centerOnScreen();
    }
}