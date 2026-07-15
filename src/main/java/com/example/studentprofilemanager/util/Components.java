package com.example.studentprofilemanager.util;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Factory for small reusable UI pieces that keep a consistent look across
 * screens. Currently provides the styled empty-table placeholder.
 */
public final class Components {

    private Components() {
    }

    /**
     * Placeholder shown inside a {@code TableView} when it has no rows.
     * Use via {@code table.setPlaceholder(Components.emptyPlaceholder(...))}.
     */
    public static VBox emptyPlaceholder(String title, String subtitle) {
        Label icon = new Label("∅"); // empty-set glyph
        icon.getStyleClass().add("placeholder-icon");

        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("placeholder-title");

        Label subtitleLabel = new Label(subtitle);
        subtitleLabel.getStyleClass().add("placeholder-text");
        subtitleLabel.setWrapText(true);

        VBox box = new VBox(6, icon, titleLabel, subtitleLabel);
        box.setAlignment(Pos.CENTER);
        box.getStyleClass().add("placeholder-box");
        return box;
    }
}
