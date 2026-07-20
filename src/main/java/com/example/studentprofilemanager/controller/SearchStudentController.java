package com.example.studentprofilemanager.controller;

import com.example.studentprofilemanager.model.Student;
import com.example.studentprofilemanager.repository.StudentRepository;
import com.example.studentprofilemanager.util.Components;
import com.example.studentprofilemanager.util.SceneNavigator;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.List;

/**
 * Controller for the Search Students screen (search-student.fxml).
 * Runs a keyword search against the shared {@link StudentRepository} and shows
 * the matches in a table, with a friendly placeholder when nothing is found.
 */
public class SearchStudentController {

    @FXML private TextField searchField;
    @FXML private TableView<Student> resultTable;
    @FXML private TableColumn<Student, String> colId;
    @FXML private TableColumn<Student, String> colName;
    @FXML private TableColumn<Student, String> colCourse;
    @FXML private TableColumn<Student, Integer> colYear;
    @FXML private TableColumn<Student, String> colSection;
    @FXML private TableColumn<Student, String> colEmail;
    @FXML private TableColumn<Student, String> colContact;
    @FXML private Label statusLabel;

    private final StudentRepository repository = StudentRepository.getInstance();
    private final ObservableList<Student> results = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
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

        resultTable.setItems(results);
        resultTable.setPlaceholder(Components.emptyPlaceholder(
                "No results yet",
                "Type a student ID, name, course or email and press Search."));

        // Allow pressing Enter in the field to trigger a search.
        searchField.setOnAction(this::search);
    }

    @FXML
    private void search(ActionEvent event) {
        String keyword = searchField.getText() == null ? "" : searchField.getText().trim();

        if (keyword.isEmpty()) {
            results.clear();
            resultTable.setPlaceholder(Components.emptyPlaceholder(
                    "No results yet",
                    "Type a student ID, name, course or email and press Search."));
            setStatus("Enter a keyword to begin", "status-info");
            return;
        }

        List<Student> matches = repository.searchStudents(keyword);
        results.setAll(matches);

        if (matches.isEmpty()) {
            resultTable.setPlaceholder(Components.emptyPlaceholder(
                    "No students found",
                    "No records match \"" + keyword + "\". Try a different keyword."));
            setStatus("0 result(s) found", "status-error");
        } else {
            setStatus(matches.size() + " result(s) found", "status-success");
        }
    }

    @FXML
    private void reset(ActionEvent event) {
        searchField.clear();
        results.clear();
        resultTable.setPlaceholder(Components.emptyPlaceholder(
                "No results yet",
                "Type a student ID, name, course or email and press Search."));
        setStatus("Enter a keyword to begin", "status-info");
    }

    private void setStatus(String text, String styleClass) {
        statusLabel.setText(text);
        statusLabel.getStyleClass().removeAll("status-success", "status-error", "status-info");
        statusLabel.getStyleClass().add(styleClass);
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
