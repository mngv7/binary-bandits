module com.example.protrack {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.protrack to javafx.fxml;
    exports com.example.protrack;
    exports com.example.protrack.applicationpages;
    opens com.example.protrack.applicationpages to javafx.fxml;
}