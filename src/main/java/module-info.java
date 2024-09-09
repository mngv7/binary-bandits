module com.example.protrack {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;
    requires jbcrypt;
    requires javafx.base;


    opens com.example.protrack to javafx.fxml;
    exports com.example.protrack;
    exports com.example.protrack.applicationpages;
    opens com.example.protrack.applicationpages to javafx.fxml;
    exports com.example.protrack.databaseutil;
    opens com.example.protrack.databaseutil to javafx.fxml;
    opens org.example to javafx.fxml;
    exports com.example.protrack.profile;
    opens com.example.protrack.profile to javafx.fxml;
    exports com.example.protrack.workorder;
    opens com.example.protrack.workorder to javafx.fxml;
    exports com.example.protrack.customer;
    opens com.example.protrack.customer to javafx.fxml;
}