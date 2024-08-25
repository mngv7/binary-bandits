module com.example.protrack {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.protrack to javafx.fxml;
    exports com.example.protrack;
}