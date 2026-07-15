module com.example.studentprofilemanager {

    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.example.studentprofilemanager to javafx.fxml;
    opens com.example.studentprofilemanager.controller to javafx.fxml;
    opens com.example.studentprofilemanager.model to javafx.base;

    exports com.example.studentprofilemanager;
}