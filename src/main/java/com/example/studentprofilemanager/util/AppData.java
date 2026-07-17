package com.example.studentprofilemanager.util;

import com.example.studentprofilemanager.repository.CourseRepository;

import java.util.List;

/**
 * Static reference data used to populate the filter and form drop-downs.
 * Centralised here so every screen offers the same choices.
 *
 * COURSES is now loaded from the `courses` table via CourseRepository.
 * If the database is unreachable or the table is empty, it falls back to
 * the original hardcoded list so existing screens never break.
 */
public final class AppData {

    private AppData() {
    }

    private static final List<String> DEFAULT_COURSES = List.of(
            "BSIT", "BSCS", "BSIS", "BSCE", "BSBA");

    public static final List<String> COURSES = loadCourses();

    public static final List<String> YEAR_LEVELS = List.of(
            "1", "2", "3", "4");

    public static final List<String> GENDERS = List.of(
            "Male", "Female", "Other");

    public static final List<String> SECTIONS = List.of(
            "A", "B", "C", "D");

    private static List<String> loadCourses() {
        try {
            List<String> codes = CourseRepository.getInstance().getAllCourseCodes();
            if (codes != null && !codes.isEmpty()) {
                return codes;
            }
        } catch (Exception e) {
            // Database unavailable, or courses table empty — fall back.
        }
        return DEFAULT_COURSES;
    }
}