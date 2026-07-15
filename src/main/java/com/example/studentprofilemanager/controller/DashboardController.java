package com.example.studentprofilemanager.controller;

import com.example.studentprofilemanager.Main;
import com.example.studentprofilemanager.util.SceneNavigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardController {

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
    private void logout(ActionEvent event) throws IOException {

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