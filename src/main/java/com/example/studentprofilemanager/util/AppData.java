package com.example.studentprofilemanager.util;

import java.util.List;

/**
 * Static reference data used to populate the filter and form drop-downs.
 * Centralised here so every screen offers the same choices. These values
 * are placeholders — replace with database look-ups when JDBC is added.
 */
public final class AppData {

    private AppData() {
    }

    public static final List<String> COURSES = List.of(
            "BSIT", "BSCS", "BSIS", "BSCE", "BSBA");

    public static final List<String> YEAR_LEVELS = List.of(
            "1", "2", "3", "4");

    public static final List<String> GENDERS = List.of(
            "Male", "Female", "Other");

    public static final List<String> SECTIONS = List.of(
            "A", "B", "C", "D");
}
