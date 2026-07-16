package com.example.studentprofilemanager.controller;

import com.example.studentprofilemanager.model.Student;
import com.example.studentprofilemanager.repository.StudentRepository;
import com.example.studentprofilemanager.util.AppData;
import com.example.studentprofilemanager.util.Components;
import com.example.studentprofilemanager.util.Dialogs;
import com.example.studentprofilemanager.util.SceneNavigator;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.List;

/**
 * Controller for the Student Management screen (students.fxml).
 * Displays all students in a table with search, filtering and CRUD actions.
 * All data comes from the shared in-memory {@link StudentRepository}.
 */
public class StudentManagementController {

    @FXML private TableView<Student> studentTable;
    @FXML private TableColumn<Student, String> colId;
    @FXML private TableColumn<Student, String> colName;
    @FXML private TableColumn<Student, String> colCourse;
    @FXML private TableColumn<Student, Integer> colYear;
    @FXML private TableColumn<Student, String> colSection;
    @FXML private TableColumn<Student, String> colEmail;
    @FXML private TableColumn<Student, String> colContact;
    @FXML private TableColumn<Student, String> colGpa;

    @FXML private TextField searchField;
    @FXML private ComboBox<String> courseFilter;
    @FXML private ComboBox<String> yearFilter;
    @FXML private Label statusLabel;

    private final StudentRepository repository = StudentRepository.getInstance();
    private final ObservableList<Student> tableData = FXCollections.observableArrayList();

    @FXML
    private void initialize() {

        // Column bindings (reflection-free — safe for module system).
        colId.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getStudentId()));
        colName.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getDisplayName()));
        colCourse.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getCourse()));
        colYear.setCellValueFactory(c ->
                new SimpleIntegerProperty(c.getValue().getYearLevel()).asObject());
        colSection.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getSection()));
        colEmail.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getEmail()));
        colContact.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getContactNumber()));
        colGpa.setCellValueFactory(c ->
                new SimpleStringProperty(String.format("%.2f", c.getValue().getGpa())));

        studentTable.setItems(tableData);
        studentTable.setPlaceholder(Components.emptyPlaceholder(
                "No students to display",
                "Add a student or adjust your search and filters."));

        // Filter drop-downs (with an "All" option).
        courseFilter.getItems().add("All");
        courseFilter.getItems().addAll(AppData.COURSES);
        courseFilter.getSelectionModel().selectFirst();

        yearFilter.getItems().add("All");
        yearFilter.getItems().addAll(AppData.YEAR_LEVELS);
        yearFilter.getSelectionModel().selectFirst();

        // Live search + filter reactions.
        searchField.textProperty().addListener((obs, oldV, newV) -> applyFilters());
        courseFilter.valueProperty().addListener((obs, oldV, newV) -> applyFilters());
        yearFilter.valueProperty().addListener((obs, oldV, newV) -> applyFilters());

        applyFilters();
    }

    /** Rebuilds the table contents from the repository using the active filters. */
    private void applyFilters() {

        String keyword = searchField.getText() == null ? "" : searchField.getText().trim();
        String course = courseFilter.getValue();
        String year = yearFilter.getValue();

        List<Student> base = repository.searchStudents(keyword);

        tableData.clear();
        for (Student s : base) {
            boolean courseOk = course == null || course.equals("All")
                    || course.equals(s.getCourse());
            boolean yearOk = year == null || year.equals("All")
                    || year.equals(String.valueOf(s.getYearLevel()));
            if (courseOk && yearOk) {
                tableData.add(s);
            }
        }

        statusLabel.setText(tableData.size() + " student(s) shown");
        statusLabel.getStyleClass().removeAll("status-success", "status-error", "status-info");
        statusLabel.getStyleClass().add("status-info");
    }

    /* ------------------------------------------------------------------ */
    /* Toolbar actions                                                     */
    /* ------------------------------------------------------------------ */

    @FXML
    private void addStudent(ActionEvent event) {
        SceneNavigator.navigate(event, "/view/add-student.fxml", "Add Student");
    }

    @FXML
    private void editStudent(ActionEvent event) {
        Student selected = studentTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Dialogs.error("No Student Selected",
                    "Please select a student from the table to edit.");
            return;
        }
        UpdateStudentController controller = SceneNavigator.navigateWithController(
                event, "/view/update-student.fxml", "Update Student");
        if (controller != null) {
            controller.setStudent(selected);
        }
    }

    @FXML
    private void deleteStudent(ActionEvent event) {
        Student selected = studentTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Dialogs.error("No Student Selected",
                    "Please select a student from the table to delete.");
            return;
        }

        boolean confirmed = Dialogs.confirm(
                "Delete Student",
                "Are you sure you want to delete "
                        + selected.getDisplayName() + " (" + selected.getStudentId() + ")?");

        if (confirmed) {
            repository.deleteStudent(selected);
            applyFilters();
            statusLabel.setText("Student deleted successfully");
            statusLabel.getStyleClass().removeAll("status-success", "status-error", "status-info");
            statusLabel.getStyleClass().add("status-success");
            Dialogs.success("Deleted", "The student record has been removed.");
        }
    }

    @FXML
    private void refresh(ActionEvent event) {
        searchField.clear();
        courseFilter.getSelectionModel().selectFirst();
        yearFilter.getSelectionModel().selectFirst();
        applyFilters();
        statusLabel.setText("List refreshed");
        statusLabel.getStyleClass().removeAll("status-success", "status-error", "status-info");
        statusLabel.getStyleClass().add("status-info");
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
    private void logout(ActionEvent event) {
        SceneNavigator.navigate(event, "/view/login.fxml", "Student Profile Manager");
    }
}
