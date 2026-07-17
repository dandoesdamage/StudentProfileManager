package com.example.studentprofilemanager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader(
                Main.class.getResource("/view/login.fxml")
        );

        Scene scene = new Scene(loader.load());

        stage.setTitle("Student Profile Manager");
        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();

    }
    @SuppressWarnings("unused")
    public static void main(String[] args) {
        launch(args);
    }
}