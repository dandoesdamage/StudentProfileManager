package com.example.studentprofilemanager.util;

// add to imports
import com.example.studentprofilemanager.service.SessionManager;
import com.example.studentprofilemanager.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Small helper that swaps the scene on the current {@link Stage} while keeping
 * the same window. Used by the sidebar navigation buttons on every screen so
 * the app behaves as one continuous desktop application.
 */
public final class SceneNavigator {

    private SceneNavigator() {
    }

    /** Loads {@code fxmlPath} into the window that fired {@code event}. */
    public static void navigate(ActionEvent event, String fxmlPath, String title) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        navigate(stage, fxmlPath, title);
    }

    /** Loads {@code fxmlPath} into the given {@code stage}. */
    public static void navigate(Stage stage, String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource(fxmlPath));
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.setTitle(title);
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
            Dialogs.error("Navigation Error",
                    "Unable to open the requested screen.");
        }
    }
    /**
     * Guards protected screens: if there is no valid session (missing/
     * corrupted session.dat), redirects to the login screen instead of
     * loading the requested FXML. Returns true if it's safe to proceed.
     */
    private static boolean requireSession(Stage stage) {
        if (SessionManager.hasValidSession()) {
            return true;
        }
        try {
            FXMLLoader loginLoader =
                    new FXMLLoader(Main.class.getResource("/view/login.fxml"));
            Scene loginScene = new Scene(loginLoader.load());
            stage.setScene(loginScene);
            stage.setTitle("Student Profile Manager");
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Loads {@code fxmlPath} and returns the loaded controller so the caller
     * can pass data into it before the scene is shown (used to pre-fill the
     * Update Student screen with the selected record).
     */
    public static <T> T navigateWithController(ActionEvent event,
                                               String fxmlPath,
                                               String title) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource(fxmlPath));
            Scene scene = new Scene(loader.load());

            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle(title);
            stage.centerOnScreen();

            return loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
            Dialogs.error("Navigation Error",
                    "Unable to open the requested screen.");
            return null;
        }
    }

}
