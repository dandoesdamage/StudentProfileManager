package com.example.studentprofilemanager.repository;

import com.example.studentprofilemanager.model.Student;

import java.util.ArrayList;
import java.util.List;

/**
 * Temporary in-memory storage for {@link Student} records.
 *
 * <p>This class deliberately hides the persistence mechanism behind a small
 * API (add / getAll / delete / update / find / search) so that the current
 * {@link ArrayList} backing store can later be swapped for a JDBC-based
 * implementation without touching any controller code.</p>
 *
 * <p>A process-wide {@link #getInstance() singleton} is provided so that every
 * screen shares the same list while the app is running. The original instance
 * API is preserved for backward compatibility.</p>
 */
public class StudentRepository {

    private final List<Student> studentList = new ArrayList<>();

    /* ---------------------------------------------------------------------
       Shared instance so all screens read/write the same temporary store.
       (Swap the body of getInstance() for a JDBC-backed repository later.)
       --------------------------------------------------------------------- */
    private static StudentRepository instance;

    public static StudentRepository getInstance() {
        if (instance == null) {
            instance = new StudentRepository();
            instance.seedSampleData();
        }
        return instance;
    }

    /* ---------------------------------------------------------------------
       Original API (unchanged)
       --------------------------------------------------------------------- */

    public void addStudent(Student student) {
        studentList.add(student);
    }

    public List<Student> getAllStudents() {
        return studentList;
    }

    public void deleteStudent(Student student) {
        studentList.remove(student);
    }

    public void updateStudent(int index, Student student) {
        studentList.set(index, student);
    }

    public Student findStudentById(String studentId) {

        for (Student student : studentList) {

            if (student.getStudentId().equals(studentId)) {

                return student;

            }

        }

        return null;

    }

    /* ---------------------------------------------------------------------
       Additive helpers used by the new screens
       --------------------------------------------------------------------- */

    /**
     * Replaces the stored record that matches the given student's ID.
     * Falls back to adding it when no existing record is found.
     */
    public void updateStudent(Student updated) {
        if (updated == null) {
            return;
        }
        for (int i = 0; i < studentList.size(); i++) {
            if (studentList.get(i).getStudentId() != null
                    && studentList.get(i).getStudentId().equals(updated.getStudentId())) {
                studentList.set(i, updated);
                return;
            }
        }
        studentList.add(updated);
    }

    /**
     * Case-insensitive search across ID, name, course and section.
     * An empty/blank keyword returns every student.
     */
    public List<Student> searchStudents(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return new ArrayList<>(studentList);
        }

        String needle = keyword.trim().toLowerCase();
        List<Student> matches = new ArrayList<>();

        for (Student student : studentList) {
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

    /**
     * Loads a handful of placeholder records so the tables are populated for
     * demonstrations. Safe to remove once real (JDBC) data is available.
     */
    private void seedSampleData() {
        studentList.add(build("2023-0001", "Dela Cruz", "Juan", "Santos",
                "Male", "BSIT", 3, "A", "juan.delacruz@spm.edu", "0917-123-4567", 3.75));
        studentList.add(build("2023-0002", "Reyes", "Maria", "Lopez",
                "Female", "BSCS", 2, "B", "maria.reyes@spm.edu", "0918-234-5678", 3.50));
        studentList.add(build("2023-0003", "Santos", "Pedro", "Garcia",
                "Male", "BSIS", 4, "A", "pedro.santos@spm.edu", "0919-345-6789", 2.85));
        studentList.add(build("2023-0004", "Villanueva", "Ana", "Cruz",
                "Female", "BSIT", 1, "C", "ana.villanueva@spm.edu", "0920-456-7890", 3.25));
        studentList.add(build("2023-0005", "Mendoza", "Jose", "Ramos",
                "Male", "BSBA", 3, "B", "jose.mendoza@spm.edu", "0921-567-8901", 3.90));
    }

    private Student build(String studentId, String lastName, String firstName,
                          String middleName, String gender, String course,
                          int yearLevel, String section, String email,
                          String contactNumber, double gpa) {

        Student s = new Student();
        s.setStudentId(studentId);
        s.setLastName(lastName);
        s.setFirstName(firstName);
        s.setMiddleName(middleName);
        s.setGender(gender);
        s.setCourse(course);
        s.setYearLevel(yearLevel);
        s.setSection(section);
        s.setEmail(email);
        s.setContactNumber(contactNumber);
        s.setGpa(gpa);
        s.setFullName(firstName + " " + lastName);
        return s;
    }
}
