module com.example.protrack {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;
    requires jbcrypt;
    requires javafx.base;
    requires java.desktop;

    opens com.example.protrack to javafx.fxml;
    opens com.example.protrack.products to javafx.base;
    opens com.example.protrack.parts to javafx.base;
    opens com.example.protrack.database to javafx.base;

    exports com.example.protrack;
    exports com.example.protrack.products;
    exports com.example.protrack.parts;
    exports com.example.protrack.database;
    exports com.example.protrack.users;
    exports com.example.protrack.applicationpages;
    opens com.example.protrack.applicationpages to javafx.fxml;
    exports com.example.protrack.utility;
    opens com.example.protrack.utility to javafx.fxml;
    
    exports com.example.protrack.profile;
    opens com.example.protrack.profile to javafx.fxml;
    exports com.example.protrack.workorder;
    opens com.example.protrack.workorder to javafx.fxml;
    exports com.example.protrack.customer;
    opens com.example.protrack.customer to javafx.fxml;

    exports com.example.protrack.warehouseutil;
    opens com.example.protrack.warehouseutil to javafx.fxml;
}
