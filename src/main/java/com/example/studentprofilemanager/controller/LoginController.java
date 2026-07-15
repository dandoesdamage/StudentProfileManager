package com.example.studentprofilemanager.controller;

import com.example.studentprofilemanager.Main;
import com.example.studentprofilemanager.service.AuthenticationService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    AuthenticationService authenticationService =
            new AuthenticationService();

    @FXML
    private void login(ActionEvent event) {

        String username = usernameField.getText();
        String password = passwordField.getText();

        if(authenticationService.authenticate(username,password)){

            try{

                FXMLLoader loader = new FXMLLoader(
                        Main.class.getResource("/view/dashboard.fxml")
                );

                Stage stage = (Stage) usernameField.getScene().getWindow();

                stage.setScene(new Scene(loader.load()));
                stage.centerOnScreen();

            }

            catch(Exception e){

                e.printStackTrace();

            }

        }

        else{

            Alert alert = new Alert(Alert.AlertType.ERROR);

            alert.setHeaderText("Login Failed");

            alert.setContentText("Invalid Username or Password.");

            alert.showAndWait();

        }

    }

    @FXML
    private void exit(){

        System.exit(0);

    }

}