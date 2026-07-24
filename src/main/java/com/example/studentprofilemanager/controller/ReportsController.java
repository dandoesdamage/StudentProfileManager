package com.example.studentprofilemanager.controller;

import com.example.studentprofilemanager.model.Student;
import com.example.studentprofilemanager.service.ReportService;
import com.example.studentprofilemanager.util.AppData;
import com.example.studentprofilemanager.util.Components;
import com.example.studentprofilemanager.util.SceneNavigator;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.List;

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

    @FXML private Button exportCsvButton;
    @FXML private Button exportPdfButton;
    @FXML private ProgressIndicator progressIndicator;

    // All report logic (DB query, CSV/PDF generation) now lives in
    // ReportService — the controller only starts Tasks and updates the UI.
    private final ReportService reportService = new ReportService();

    private final ObservableList<Student> tableData = FXCollections.observableArrayList();

    // Tracks the currently running load Task so a fast typist doesn't end up
    // with several overlapping background queries racing to update the table.
    private Task<List<Student>> currentLoadTask;

    // Simple counter so the progress indicator only hides once every
    // currently running background Task has finished (e.g. a load Task and
    // an export Task could in theory overlap).
    private int activeBackgroundTasks = 0;

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

        searchField.textProperty().addListener((obs, oldV, newV) -> applyFiltersAsync());
        courseFilter.valueProperty().addListener((obs, oldV, newV) -> applyFiltersAsync());
        yearFilter.valueProperty().addListener((obs, oldV, newV) -> applyFiltersAsync());

        progressIndicator.setVisible(false);

        // Load the report asynchronously as soon as the screen opens,
        // instead of blocking the FX Application Thread on the initial query.
        applyFiltersAsync();
    }

    private void applyFiltersAsync() {

        // Cancel a still-running previous load so its (now stale) results
        // can't overwrite results from a more recent search/filter change.
        if (currentLoadTask != null && currentLoadTask.isRunning()) {
            currentLoadTask.cancel();
        }

        String keyword = searchField.getText() == null ? "" : searchField.getText().trim();
        String course = courseFilter.getValue();
        String year = yearFilter.getValue();

        Task<List<Student>> loadTask = new Task<>() {
            @Override
            protected List<Student> call() {
                // Runs on a background thread — the (potentially slow) JDBC
                // query happens here, off the FX Application Thread, so the
                // UI never freezes while it runs.
                return reportService.loadReportData(keyword, course, year);
            }
        };

        loadTask.setOnRunning(e -> taskStarted());

        loadTask.setOnSucceeded(e -> {
            // Guaranteed by JavaFX to run on the FX Application Thread —
            // safe to update UI controls directly here.
            tableData.setAll(loadTask.getValue());
            statusLabel.setText(tableData.size() + " record(s) shown");
            setStatusStyle("status-info");
            taskFinished();
        });

        loadTask.setOnFailed(e -> {
            setStatusStyle("status-error");
            statusLabel.setText("Failed to load report data");
            taskFinished();
            if (loadTask.getException() != null) {
                loadTask.getException().printStackTrace();
            }
        });

        currentLoadTask = loadTask;

        // Background thread, marked daemon so it never blocks JVM shutdown.
        Thread thread = new Thread(loadTask, "report-load-thread");
        thread.setDaemon(true);
        thread.start();
    }

    @FXML
    private void refresh(ActionEvent event) {
        searchField.clear();
        courseFilter.getSelectionModel().selectFirst();
        yearFilter.getSelectionModel().selectFirst();
        applyFiltersAsync();
        statusLabel.setText("Refreshing report…");
        setStatusStyle("status-info");
    }

    /* ------------------------------------------------------------------ */
    /* Export                                                              */
    /* ------------------------------------------------------------------ */

    @FXML
    private void exportCsv(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Export Report to CSV");
        chooser.setInitialFileName("student-report.csv");
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

        // FileChooser MUST be shown on the FX Application Thread — this
        // happens here, before the background Task is created below.
        File destination = chooser.showSaveDialog(exportCsvButton.getScene().getWindow());
        if (destination == null) {
            return; // user cancelled
        }

        // Snapshot the currently displayed rows now, on the FX thread, so the
        // background Task never touches the live ObservableList itself.
        List<Student> snapshot = List.copyOf(tableData);

        runExportTask(
                snapshot,
                destination,
                reportService::exportToCsv
        );
    }

    @FXML
    private void runExportTask(List<Student> snapshot,
                               File destination,
                               Exporter exporter) {

        exportCsvButton.setDisable(true);
        exportPdfButton.setDisable(true);
        statusLabel.setText("Exporting…");
        setStatusStyle("status-info");

        Task<Void> exportTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                // Runs entirely on a background thread — file writing never
                // touches the FX Application Thread, so the window stays
                // responsive during export.
                exporter.export(snapshot, destination);
                return null;
            }
        };

        exportTask.setOnRunning(e -> taskStarted());

        exportTask.setOnSucceeded(e -> {
            exportCsvButton.setDisable(false);
            exportPdfButton.setDisable(false);
            taskFinished();
            statusLabel.setText("CSV report exported successfully.");
            setStatusStyle("status-success");
            new Alert(AlertType.INFORMATION,
                    "CSV report exported successfully." + "\n\nSaved to:\n" + destination.getAbsolutePath())
                    .showAndWait();
        });

        exportTask.setOnFailed(e -> {
            exportCsvButton.setDisable(false);
            exportPdfButton.setDisable(false);
            taskFinished();
            statusLabel.setText("Failed to export CSV report");
            setStatusStyle("status-error");
            String detail = exportTask.getException() != null
                    ? exportTask.getException().getMessage()
                    : "Unknown error";
            new Alert(AlertType.ERROR, "Failed to export CSV report" + ":\n" + detail).showAndWait();
        });

        Thread thread = new Thread(exportTask, "report-export-thread");
        thread.setDaemon(true);
        thread.start();
    }

    /** Functional interface so exportCsv/exportPdf can share runExportTask. */
    @FunctionalInterface
    private interface Exporter {
        void export(List<Student> students, File destination) throws Exception;
    }

    /* ------------------------------------------------------------------ */
    /* Small UI helpers                                                    */
    /* ------------------------------------------------------------------ */

    /** Increments the active-task count and shows the progress indicator. */
    private void taskStarted() {
        activeBackgroundTasks++;
        progressIndicator.setVisible(true);
    }

    /** Decrements the active-task count and hides the indicator once idle. */
    private void taskFinished() {
        activeBackgroundTasks = Math.max(0, activeBackgroundTasks - 1);
        if (activeBackgroundTasks == 0) {
            progressIndicator.setVisible(false);
        }
    }

    private void setStatusStyle(String styleClass) {
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