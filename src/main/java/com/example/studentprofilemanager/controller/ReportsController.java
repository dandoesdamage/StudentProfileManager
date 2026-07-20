package com.example.studentprofilemanager.controller;

import com.example.studentprofilemanager.model.Student;
import com.example.studentprofilemanager.repository.StudentRepository;
import com.example.studentprofilemanager.util.AppData;
import com.example.studentprofilemanager.util.Components;
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
 * Controller for the Reports screen (reports.fxml).
 * Read-only detailed student report with search + course/year filtering.
 * Deliberately does NOT duplicate the Dashboard's summary statistics
 * (Total Students / Active Courses / Total Users / Average GPA).
 * All data comes from {@link StudentRepository}; no SQL lives here.
 */
public class ReportsController {

    @FXML private TextField searchField;
    @FXML private ComboBox<String> courseFilter;
    @FXML private ComboBox<String> yearFilter;
    @FXML private Label statusLabel;

    @FXML private TableView<Student> reportTable;
    @FXML private TableColumn<Student, String> colId;
    @FXML private TableColumn<Student, String> colName;
    @FXML private TableColumn<Student, String> colCourse;
    @FXML private TableColumn<Student, Integer> colYear;
    @FXML private TableColumn<Student, String> colEmail;
    @FXML private TableColumn<Student, String> colGpa;

    private final StudentRepository repository = StudentRepository.getInstance();
    private final ObservableList<Student> tableData = FXCollections.observableArrayList();

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
        colEmail.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getEmail()));
        colGpa.setCellValueFactory(c ->
                new SimpleStringProperty(String.format("%.2f", c.getValue().getGpa())));

        reportTable.setItems(tableData);
        reportTable.setPlaceholder(Components.emptyPlaceholder(
                "No records to display",
                "Adjust your search or filters."));

        courseFilter.getItems().add("All");
        courseFilter.getItems().addAll(AppData.COURSES);
        courseFilter.getSelectionModel().selectFirst();

        yearFilter.getItems().add("All");
        yearFilter.getItems().addAll(AppData.YEAR_LEVELS);
        yearFilter.getSelectionModel().selectFirst();

        searchField.textProperty().addListener((obs, oldV, newV) -> applyFilters());
        courseFilter.valueProperty().addListener((obs, oldV, newV) -> applyFilters());
        yearFilter.valueProperty().addListener((obs, oldV, newV) -> applyFilters());

        applyFilters();
    }

    /** Rebuilds the table from the repository using the active search/filters. */
    private void applyFilters() {
        String keyword = searchField.getText() == null ? "" : searchField.getText().trim();
        String course = courseFilter.getValue();
        String year = yearFilter.getValue();

        List<Student> matches = repository.filterStudents(keyword, course, year);

        tableData.setAll(matches);
        statusLabel.setText(tableData.size() + " record(s) shown");
        statusLabel.getStyleClass().removeAll("status-success", "status-error", "status-info");
        statusLabel.getStyleClass().add("status-info");
    }

    @FXML
    private void refresh(ActionEvent event) {
        searchField.clear();
        courseFilter.getSelectionModel().selectFirst();
        yearFilter.getSelectionModel().selectFirst();
        applyFilters();
        statusLabel.setText("Report refreshed");
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
    private void goReports(ActionEvent event) {
        SceneNavigator.navigate(event, "/view/reports.fxml", "Reports");
    }

    @FXML
    private void logout(ActionEvent event) {
        SceneNavigator.navigate(event, "/view/login.fxml", "Student Profile Manager");
    }
}