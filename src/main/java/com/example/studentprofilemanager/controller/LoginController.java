package com.example.studentprofilemanager.controller;

import com.example.studentprofilemanager.Main;
import com.example.studentprofilemanager.model.Student;
import com.example.studentprofilemanager.service.AuthenticationService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    /* Additive: role selector, defaults to Administrator so existing
       behavior is unchanged unless the user explicitly picks Student. */
    @FXML
    private RadioButton adminRadio;

    @FXML
    private RadioButton studentRadio;

    AuthenticationService authenticationService =
            new AuthenticationService();

    @FXML
    private void login(ActionEvent event) {

        String username = usernameField.getText();
        String password = passwordField.getText();

        boolean loggingInAsStudent = studentRadio != null && studentRadio.isSelected();

        if (loggingInAsStudent) {
            loginAsStudent(event, username, password);
        } else {
            loginAsAdmin(event, username, password);
        }
    }

    /* Original admin logic, unchanged. */
    private void loginAsAdmin(ActionEvent event, String username, String password) {

        if (authenticationService.authenticate(username, password)) {

            try {

                FXMLLoader loader = new FXMLLoader(
                        Main.class.getResource("/view/dashboard.fxml")
                );

                Stage stage = (Stage) usernameField.getScene().getWindow();

                stage.setScene(new Scene(loader.load()));
                stage.centerOnScreen();

            } catch (Exception e) {

                e.printStackTrace();

            }

        } else {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Login Failed");
            alert.setContentText("Invalid Username or Password.");
            alert.showAndWait();

        }
    }

    /* Additive: student login. */
    private void loginAsStudent(ActionEvent event, String username, String password) {

        Student student = authenticationService.authenticateStudent(username, password);

        if (student != null) {

            try {

                FXMLLoader loader = new FXMLLoader(
                        Main.class.getResource("/view/student-dashboard.fxml")
                );

                Scene scene = new Scene(loader.load());

                StudentDashboardController controller = loader.getController();
                controller.setStudent(student);

                Stage stage = (Stage) usernameField.getScene().getWindow();
                stage.setScene(scene);
                stage.centerOnScreen();

            } catch (Exception e) {

                e.printStackTrace();

            }

        } else {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Login Failed");
            alert.setContentText("Invalid Username or Password.");
            alert.showAndWait();

        }
    }

    @FXML
    private void goRegister(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    Main.class.getResource("/view/register.fxml")
            );

            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.centerOnScreen();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void exit() {
        System.exit(0);
    }

}