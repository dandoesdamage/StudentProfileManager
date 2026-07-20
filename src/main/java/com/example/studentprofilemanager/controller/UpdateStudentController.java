package com.example.studentprofilemanager.controller;

import com.example.studentprofilemanager.model.Student;
import com.example.studentprofilemanager.repository.StudentRepository;
import com.example.studentprofilemanager.util.AppData;
import com.example.studentprofilemanager.util.Dialogs;
import com.example.studentprofilemanager.util.SceneNavigator;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * Controller for the Update Student screen (update-student.fxml).
 * Shares the same layout as Add Student but is pre-filled with the selected
 * record via {@link #setStudent(Student)} and saves changes back to the
 * shared {@link StudentRepository}.
 */
public class UpdateStudentController {

    @FXML private TextField studentIdField;
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField middleNameField;
    @FXML private ComboBox<String> genderCombo;
    @FXML private ComboBox<String> courseCombo;
    @FXML private ComboBox<String> yearLevelCombo;
    @FXML private ComboBox<String> sectionCombo;
    @FXML private TextField emailField;
    @FXML private TextField contactField;
    @FXML private TextField gpaField;
    @FXML private Label statusLabel;

    private final StudentRepository repository = StudentRepository.getInstance();

    /** The record being edited. */
    private Student editing;

    @FXML
    private void initialize() {
        genderCombo.setItems(FXCollections.observableArrayList(AppData.GENDERS));
        courseCombo.setItems(FXCollections.observableArrayList(AppData.COURSES));
        yearLevelCombo.setItems(FXCollections.observableArrayList(AppData.YEAR_LEVELS));
        sectionCombo.setItems(FXCollections.observableArrayList(AppData.SECTIONS));
    }

    /** Pre-fills the form with the selected student. Called after navigation. */
    public void setStudent(Student student) {
        this.editing = student;
        if (student == null) {
            return;
        }
        studentIdField.setText(student.getStudentId());
        firstNameField.setText(student.getFirstName());
        lastNameField.setText(student.getLastName());
        middleNameField.setText(student.getMiddleName());
        genderCombo.setValue(student.getGender());
        courseCombo.setValue(student.getCourse());
        yearLevelCombo.setValue(String.valueOf(student.getYearLevel()));
        sectionCombo.setValue(student.getSection());
        emailField.setText(student.getEmail());
        contactField.setText(student.getContactNumber());
        gpaField.setText(String.format("%.2f", student.getGpa()));
    }

    @FXML
    private void update(ActionEvent event) {

        if (editing == null) {
            Dialogs.error("Nothing to Update", "No student record is loaded.");
            return;
        }

        String error = validate();
        if (error != null) {
            showError(error);
            return;
        }

        // Apply the edits back onto the same object (preserves userId/gpa etc.).
        editing.setStudentId(studentIdField.getText().trim());
        editing.setFirstName(firstNameField.getText().trim());
        editing.setLastName(lastNameField.getText().trim());
        editing.setMiddleName(middleNameField.getText().trim());
        editing.setGender(genderCombo.getValue());
        editing.setCourse(courseCombo.getValue());
        editing.setYearLevel(Integer.parseInt(yearLevelCombo.getValue()));
        editing.setSection(sectionCombo.getValue());
        editing.setEmail(emailField.getText().trim());
        editing.setContactNumber(contactField.getText().trim());
        editing.setGpa(parseGpa());
        editing.setFullName(firstNameField.getText().trim()
                + " " + lastNameField.getText().trim());

        repository.updateStudent(editing);

        Dialogs.success("Student Updated",
                editing.getDisplayName() + " has been updated successfully.");

        SceneNavigator.navigate(event, "/view/students.fxml", "Student Management");
    }

    @FXML
    private void cancel(ActionEvent event) {
        SceneNavigator.navigate(event, "/view/students.fxml", "Student Management");
    }

    /* ------------------------------------------------------------------ */
    /* Validation helpers                                                  */
    /* ------------------------------------------------------------------ */

    private String validate() {
        if (isBlank(studentIdField.getText())) return "Student ID is required.";
        if (isBlank(firstNameField.getText())) return "First name is required.";
        if (isBlank(lastNameField.getText())) return "Last name is required.";
        if (genderCombo.getValue() == null) return "Please select a gender.";
        if (courseCombo.getValue() == null) return "Please select a course.";
        if (yearLevelCombo.getValue() == null) return "Please select a year level.";
        if (sectionCombo.getValue() == null) return "Please select a section.";
        if (isBlank(emailField.getText())) return "Email is required.";
        if (!emailField.getText().trim().matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            return "Please enter a valid email address.";
        }
        if (isBlank(contactField.getText())) return "Contact number is required.";
        String gpaError = validateGpa();
        if (gpaError != null) return gpaError;
        return null;
    }

    private String validateGpa() {
        if (isBlank(gpaField.getText())) return "GPA is required.";
        try {
            double gpa = Double.parseDouble(gpaField.getText().trim());
            if (gpa < 0.0 || gpa > 4.0) {
                return "GPA must be between 0.0 and 4.0.";
            }
        } catch (NumberFormatException ex) {
            return "GPA must be a valid number.";
        }
        return null;
    }

    private double parseGpa() {
        return Double.parseDouble(gpaField.getText().trim());
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private void showError(String message) {
        statusLabel.setText(message);
        statusLabel.getStyleClass().removeAll("status-success", "status-error", "status-info");
        statusLabel.getStyleClass().add("status-error");
        Dialogs.error("Invalid Input", message);
    }

    /* ------------------------------------------------------------------ */
    /* Sidebar navigation                                                  */
    /* ------------------------------------------------------------------ */

    @FXML
    private void goDashboard(ActionEvent event) {
        SceneNavigator.navigate(event, "/view/dashboard.fxml", "Dashboard");
    }

    @FXML
    private void goStudents(ActionEvent event) {
        SceneNavigator.navigate(event, "/view/students.fxml", "Student Management");
    }

    @FXML
    private void goAddStudent(ActionEvent event) {
        SceneNavigator.navigate(event, "/view/add-student.fxml", "Add Student");
    }

    @FXML
    private void goSearch(ActionEvent event) {
        SceneNavigator.navigate(event, "/view/search-student.fxml", "Search Students");
    }

    @FXML
    private void goReports(ActionEvent event) {
        SceneNavigator.navigate(event, "/view/reports.fxml", "Reports");
    }

    @FXML
    private void logout(ActionEvent event) {
        SceneNavigator.navigate(event, "/view/login.fxml", "Student Profile Manager");
    }
}
