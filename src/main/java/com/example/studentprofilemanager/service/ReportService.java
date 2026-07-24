package com.example.studentprofilemanager.service;

import com.example.studentprofilemanager.model.Student;
import com.example.studentprofilemanager.repository.StudentRepository;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

 // Service layer for the Reports feature.
public class ReportService {
    private final StudentRepository repository = StudentRepository.getInstance();

    public List<Student> loadReportData(String keyword, String course, String year) {
        return repository.filterStudents(keyword, course, year);
    }

    public void exportToCsv(List<Student> students, File destination) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(destination))) {
            writer.println("Student ID,Full Name,Course,Year Level,Email,GPA");
            for (Student s : students) {
                writer.printf(
                        "%s,%s,%s,%d,%s,%.2f%n",
                        csvEscape(s.getStudentId()),
                        csvEscape(s.getDisplayName()),
                        csvEscape(s.getCourse()),
                        s.getYearLevel(),
                        csvEscape(s.getEmail()),
                        s.getGpa());
            }
        }
    }

    // Escapes a value for safe placement in a CSV cell.
    private String csvEscape(String value) {
        if (value == null) {
            return "";
        }
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}