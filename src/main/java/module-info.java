module com.example.protrack {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.protrack to javafx.fxml;
    exports com.example.protrack;
}