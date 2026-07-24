package com.example.studentprofilemanager.repository;

import com.example.studentprofilemanager.db.DatabaseConnection;
import com.example.studentprofilemanager.model.Course;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CourseRepository {

    private static CourseRepository instance;

    public static CourseRepository getInstance() {
        if (instance == null) {
            instance = new CourseRepository();
        }
        return instance;
    }

    public List<Course> getAllCourses() {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT id, code, name FROM courses ORDER BY code";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                courses.add(new Course(rs.getInt("id"), rs.getString("code"), rs.getString("name")));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to load courses.", e);
        }

        return courses;
    }

    /** Convenience used by AppData to populate the course dropdowns. */
    public List<String> getAllCourseCodes() {
        List<String> codes = new ArrayList<>();
        for (Course c : getAllCourses()) {
            codes.add(c.getCode());
        }
        return codes;
    }

    public void addCourse(Course course) {
        String sql = "INSERT INTO courses (code, name) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, course.getCode());
            ps.setString(2, course.getName());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to add course.", e);
        }
    }

    public void deleteCourse(Course course) {
        String sql = "DELETE FROM courses WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, course.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete course.", e);
        }
    }

    // Count of all courses. Returns 0 if the table is empty.
    public int countCourses() {
        String sql = "SELECT COUNT(*) AS total FROM courses";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("total");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to count courses.", e);
        }

        return 0;
    }
}