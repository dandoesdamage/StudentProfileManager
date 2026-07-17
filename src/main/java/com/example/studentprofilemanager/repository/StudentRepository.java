package com.example.studentprofilemanager.repository;

import com.example.studentprofilemanager.db.DatabaseConnection;
import com.example.studentprofilemanager.model.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC-backed storage for {@link Student} records, persisted across the
 * `users` and `students` tables in the student_profile_manager MySQL
 * database (see schema.sql).
 *
 * <p>Public method signatures are unchanged from the original in-memory
 * implementation so no controller code needed to change.</p>
 */
public class StudentRepository {

    private static StudentRepository instance;

    public static StudentRepository getInstance() {
        if (instance == null) {
            instance = new StudentRepository();
        }
        return instance;
    }

    private static final String BASE_SELECT =
            "SELECT u.id AS user_id, u.username, u.password, u.full_name, u.email, "
                    + "s.student_id, s.first_name, s.last_name, s.middle_name, s.gender, "
                    + "s.course, s.year_level, s.section, s.contact_number, s.gpa "
                    + "FROM students s JOIN users u ON s.user_id = u.id";

    /* ---------------------------------------------------------------------
       Original API (signatures unchanged)
       --------------------------------------------------------------------- */

    public void addStudent(Student student) {

        String insertUser =
                "INSERT INTO users (username, password, full_name, email, role) "
                        + "VALUES (?, ?, ?, ?, 'STUDENT')";
        String insertStudent =
                "INSERT INTO students (user_id, student_id, first_name, last_name, "
                        + "middle_name, gender, course, year_level, section, contact_number, gpa) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                int userId;

                try (PreparedStatement ps = conn.prepareStatement(insertUser, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, student.getUsername());
                    ps.setString(2, student.getPassword());
                    ps.setString(3, student.getFullName());
                    ps.setString(4, student.getEmail());
                    ps.executeUpdate();

                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        if (keys.next()) {
                            userId = keys.getInt(1);
                        } else {
                            throw new SQLException("Failed to obtain generated user id.");
                        }
                    }
                }

                try (PreparedStatement ps = conn.prepareStatement(insertStudent)) {
                    ps.setInt(1, userId);
                    ps.setString(2, student.getStudentId());
                    ps.setString(3, student.getFirstName());
                    ps.setString(4, student.getLastName());
                    ps.setString(5, student.getMiddleName());
                    ps.setString(6, student.getGender());
                    ps.setString(7, student.getCourse());
                    ps.setInt(8, student.getYearLevel());
                    ps.setString(9, student.getSection());
                    ps.setString(10, student.getContactNumber());
                    ps.setDouble(11, student.getGpa());
                    ps.executeUpdate();
                }

                conn.commit();
                student.setUserId(userId);

            } catch (SQLException e) {
                conn.rollback();
                throw new RuntimeException("Failed to add student.", e);
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database connection error while adding student.", e);
        }
    }

    public List<Student> getAllStudents() {
        List<Student> results = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(BASE_SELECT);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                results.add(mapRow(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to load students.", e);
        }

        return results;
    }

    public void deleteStudent(Student student) {
        if (student == null) {
            return;
        }

        String sql = "DELETE FROM users WHERE id = ?"; // cascades to students row

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, student.getUserId());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete student.", e);
        }
    }

    /**
     * No known callers exist for this overload (confirmed by inspection of
     * AddStudentController, SearchStudentController, StudentManagementController,
     * UpdateStudentController). Implemented as a thin wrapper for safety:
     * resolves the record currently at that position and delegates to the
     * by-object update below.
     */
    public void updateStudent(int index, Student student) {
        List<Student> all = getAllStudents();
        if (index >= 0 && index < all.size()) {
            student.setUserId(all.get(index).getUserId());
        }
        updateStudent(student);
    }

    /**
     * Matches by studentId, same as the original in-memory version.
     * Falls back to addStudent when no existing record is found.
     */
    public void updateStudent(Student updated) {
        if (updated == null) {
            return;
        }

        Student existing = findStudentById(updated.getStudentId());
        if (existing == null) {
            addStudent(updated);
            return;
        }
        updated.setUserId(existing.getUserId());

        String updateUser =
                "UPDATE users SET username = ?, password = ?, full_name = ?, email = ? WHERE id = ?";
        String updateStudentSql =
                "UPDATE students SET student_id = ?, first_name = ?, last_name = ?, middle_name = ?, "
                        + "gender = ?, course = ?, year_level = ?, section = ?, contact_number = ?, gpa = ? "
                        + "WHERE user_id = ?";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                try (PreparedStatement ps = conn.prepareStatement(updateUser)) {
                    ps.setString(1, updated.getUsername());
                    ps.setString(2, updated.getPassword());
                    ps.setString(3, updated.getFullName());
                    ps.setString(4, updated.getEmail());
                    ps.setInt(5, updated.getUserId());
                    ps.executeUpdate();
                }

                try (PreparedStatement ps = conn.prepareStatement(updateStudentSql)) {
                    ps.setString(1, updated.getStudentId());
                    ps.setString(2, updated.getFirstName());
                    ps.setString(3, updated.getLastName());
                    ps.setString(4, updated.getMiddleName());
                    ps.setString(5, updated.getGender());
                    ps.setString(6, updated.getCourse());
                    ps.setInt(7, updated.getYearLevel());
                    ps.setString(8, updated.getSection());
                    ps.setString(9, updated.getContactNumber());
                    ps.setDouble(10, updated.getGpa());
                    ps.setInt(11, updated.getUserId());
                    ps.executeUpdate();
                }

                conn.commit();

            } catch (SQLException e) {
                conn.rollback();
                throw new RuntimeException("Failed to update student.", e);
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database connection error while updating student.", e);
        }
    }

    public Student findStudentById(String studentId) {
        if (studentId == null) {
            return null;
        }

        String sql = BASE_SELECT + " WHERE s.student_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to find student by id.", e);
        }

        return null;
    }

    /* ---------------------------------------------------------------------
       Additive helpers (unchanged signatures)
       --------------------------------------------------------------------- */

    /**
     * Same behavior as before: fetches all rows and filters in Java, so the
     * search still covers the computed getDisplayName() exactly as it did
     * against the in-memory list.
     */
    public List<Student> searchStudents(String keyword) {
        List<Student> all = getAllStudents();

        if (keyword == null || keyword.isBlank()) {
            return all;
        }

        String needle = keyword.trim().toLowerCase();
        List<Student> matches = new ArrayList<>();

        for (Student student : all) {
            if (contains(student.getStudentId(), needle)
                    || contains(student.getDisplayName(), needle)
                    || contains(student.getFullName(), needle)
                    || contains(student.getCourse(), needle)
                    || contains(student.getSection(), needle)
                    || contains(student.getEmail(), needle)) {
                matches.add(student);
            }
        }

        return matches;
    }

    private boolean contains(String value, String needle) {
        return value != null && value.toLowerCase().contains(needle);
    }

    public Student findStudentByUsername(String username) {
        if (username == null) {
            return null;
        }

        String sql = BASE_SELECT + " WHERE u.username = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to find student by username.", e);
        }

        return null;
    }

    public boolean usernameExists(String username) {
        return findStudentByUsername(username) != null;
    }

    public boolean studentIdExists(String studentId) {
        return findStudentById(studentId) != null;
    }

    /**
     * Kept for backward compatibility with StudentRegisterController, which
     * pre-assigns a userId before calling addStudent(). Note that addStudent
     * now ignores the pre-assigned value and overwrites it with the real
     * AUTO_INCREMENT id from the database, so this value is never actually
     * relied upon for uniqueness anymore — it just avoids a null/zero id in
     * memory before the insert happens.
     */
    public int nextUserId() {
        String sql = "SELECT MAX(id) AS max_id FROM users";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("max_id") + 1;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to compute next user id.", e);
        }

        return 1;
    }

    /* ---------------------------------------------------------------------
       Row mapping
       --------------------------------------------------------------------- */

    private Student mapRow(ResultSet rs) throws SQLException {
        Student s = new Student();
        s.setUserId(rs.getInt("user_id"));
        s.setUsername(rs.getString("username"));
        s.setPassword(rs.getString("password"));
        s.setFullName(rs.getString("full_name"));
        s.setEmail(rs.getString("email"));
        s.setStudentId(rs.getString("student_id"));
        s.setFirstName(rs.getString("first_name"));
        s.setLastName(rs.getString("last_name"));
        s.setMiddleName(rs.getString("middle_name"));
        s.setGender(rs.getString("gender"));
        s.setCourse(rs.getString("course"));
        s.setYearLevel(rs.getInt("year_level"));
        s.setSection(rs.getString("section"));
        s.setContactNumber(rs.getString("contact_number"));
        s.setGpa(rs.getDouble("gpa"));
        return s;
    }
}