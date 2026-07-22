package com.example.studentprofilemanager.controller;

import com.example.studentprofilemanager.Main;
import com.example.studentprofilemanager.service.DashboardFacade;
import com.example.studentprofilemanager.service.DashboardStats;
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

    @FXML
    private Label totalStudentsValue;

    @FXML
    private Label activeCoursesValue;

    @FXML
    private Label totalUsersValue;

    @FXML
    private Label averageGpaValue;

    private final DashboardFacade dashboardFacade = new DashboardFacade();

    @FXML
    private void initialize() {
        DashboardStats stats = dashboardFacade.getDashboardStats();

        totalStudentsValue.setText(String.valueOf(stats.getTotalStudents()));
        activeCoursesValue.setText(String.valueOf(stats.getActiveCourses()));
        totalUsersValue.setText(String.valueOf(stats.getTotalUsers()));
        averageGpaValue.setText(String.format("%.2f", stats.getAverageGpa()));
    }

    /* ------------------------------------------------------------------ */
    /* Sidebar navigation (unchanged)                                      */
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