package com.example.studentprofilemanager.util;

import com.example.studentprofilemanager.Main;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;

import java.util.Optional;

/**
 * Reusable, theme-styled message dialogs (confirmation / success / error).
 * Every dialog reuses the application stylesheet so it matches the blue/white
 * design language of the rest of the app.
 */
public final class Dialogs {

    private Dialogs() {
    }

    private static void applyTheme(Alert alert) {
        DialogPane pane = alert.getDialogPane();
        pane.getStylesheets().add(
                Main.class.getResource("/css/style.css").toExternalForm());
        pane.getStyleClass().add("app-dialog");
    }

    /** Yes/No style confirmation. Returns {@code true} when the user accepts. */
    public static boolean confirm(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Please Confirm");
        alert.setHeaderText(header);
        alert.setContentText(content);
        applyTheme(alert);
        alert.getDialogPane().getStyleClass().add("dialog-confirm");

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    /** Success / information message. */
    public static void success(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(header);
        alert.setContentText(content);
        applyTheme(alert);
        alert.getDialogPane().getStyleClass().add("dialog-success");
        alert.showAndWait();
    }

    /** Error message. */
    public static void error(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.setContentText(content);
        applyTheme(alert);
        alert.getDialogPane().getStyleClass().add("dialog-error");
        alert.showAndWait();
    }
}
