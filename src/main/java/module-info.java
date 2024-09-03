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
    exports com.example.protrack.stakeholders;
    opens com.example.protrack.stakeholders to javafx.fxml;
    exports com.example.protrack.inventory;
    opens com.example.protrack.inventory to javafx.fxml;
    exports com.example.protrack.orders;
    opens com.example.protrack.orders to javafx.fxml;
    exports com.example.protrack.work;
    opens com.example.protrack.work to javafx.fxml;
    exports com.example.protrack.reports;
    opens com.example.protrack.reports to javafx.fxml;
}