package com.example.protrack;

import com.example.protrack.products.*;
import com.example.protrack.users.ManagerialUser;
import com.example.protrack.users.ProductionUser;
import com.example.protrack.users.UsersDAO;
import com.example.protrack.users.WarehouseUser;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {

    private static final String TITLE = "ProTrack";
    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), WIDTH, HEIGHT);
        String stylesheet = Objects.requireNonNull(Main.class.getResource("stylesheet.css")).toExternalForm();
        scene.getStylesheets().add(stylesheet);
        stage.setTitle(TITLE);
        stage.setScene(scene);
        stage.show();
    }

    public static int getWidth() {
        return WIDTH;
    }

    public static int getHeight() {
        return HEIGHT;
    }

    public static void main(String[] args) {

        ProductDAO productDAO = new ProductDAO();
        RequiredPartsDAO requiredPartsDAO = new RequiredPartsDAO();
        TestRecordStepsDAO testRecordDAO = new TestRecordStepsDAO();
        UsersDAO usersDAO = new UsersDAO();

        productDAO.createTable();
        usersDAO.createTable();
        testRecordDAO.createTable();
        requiredPartsDAO.createTable();

        if (usersDAO.isTableEmpty()) {
            usersDAO.newUser(new ManagerialUser(100, "John", "Doe", "password"));
            usersDAO.newUser(new ManagerialUser(101, "Alice", "Smith", "alicepass"));
            usersDAO.newUser(new ManagerialUser(102, "Bob", "Johnson", "bobpass"));
            usersDAO.newUser(new WarehouseUser(103, "Charlie", "Brown", "charliepass"));
            usersDAO.newUser(new WarehouseUser(104, "Diana", "White", "dianapass"));
            usersDAO.newUser(new ProductionUser(105, "Eve", "Davis", "evepass"));
            usersDAO.newUser(new ProductionUser(106, "Frank", "Miller", "frankpass"));

        }

        if (testRecordDAO.isTableEmpty()) {
            // Product 36014: Mining Collision Detector
            testRecordDAO.newTestRecordStep(new TestRecordSteps(1, 36014, 1, "Torque the T10 screw to 5Nm.", "CheckBox", "NULL"));
            testRecordDAO.newTestRecordStep(new TestRecordSteps(2, 36014, 2, "Measure the thickness of the metal plate (mm).", "TextEntry", ">5"));
            testRecordDAO.newTestRecordStep(new TestRecordSteps(3, 36014, 3, "Power on the device and check the operating voltage (V).", "TextEntry", ">10"));
            testRecordDAO.newTestRecordStep(new TestRecordSteps(4, 36014, 4, "Power off the device and put into sleep mode for shipping.", "CheckBox", "NULL"));

            // Product 45021: Displacement Monitoring Device
            testRecordDAO.newTestRecordStep(new TestRecordSteps(1, 45021, 1, "Inspect the exterior for any visible damage or scratches.", "CheckBox", "NULL"));
            testRecordDAO.newTestRecordStep(new TestRecordSteps(2, 45021, 2, "Check that the battery is fully charged.", "CheckBox", "NULL"));
            testRecordDAO.newTestRecordStep(new TestRecordSteps(3, 45021, 3, "Test the wireless connection strength (dBm).", "TextEntry", ">60"));
            testRecordDAO.newTestRecordStep(new TestRecordSteps(4, 45021, 4, "Verify that the display is working without any dead pixels.", "CheckBox", "NULL"));
            testRecordDAO.newTestRecordStep(new TestRecordSteps(5, 45021, 5, "Measure the device temperature after 10 minutes of operation (°C).", "TextEntry", "<40"));
            testRecordDAO.newTestRecordStep(new TestRecordSteps(6, 45021, 6, "Ensure all screws are torqued to the specified value (Nm).", "CheckBox", "NULL"));
            testRecordDAO.newTestRecordStep(new TestRecordSteps(7, 45021, 7, "Check that the firmware version is up to date.", "TextEntry", "Latest"));
            testRecordDAO.newTestRecordStep(new TestRecordSteps(8, 45021, 8, "Power off the device and package it in the protective casing.", "CheckBox", "NULL"));

            // Product 67890: Power Supply
            testRecordDAO.newTestRecordStep(new TestRecordSteps(1, 67890, 1, "Verify the serial number matches the product ID label.", "CheckBox", "NULL"));
            testRecordDAO.newTestRecordStep(new TestRecordSteps(2, 67890, 2, "Check the power cable for any signs of wear.", "CheckBox", "NULL"));
            testRecordDAO.newTestRecordStep(new TestRecordSteps(3, 67890, 3, "Test the main function of the device to ensure it operates correctly.", "CheckBox", "NULL"));
            testRecordDAO.newTestRecordStep(new TestRecordSteps(4, 67890, 4, "Ensure all accessory components are included in the packaging.", "CheckBox", "NULL"));
        }

        if (productDAO.isTableEmpty()) {
            long millis = System.currentTimeMillis();
            java.sql.Date date = new java.sql.Date(millis);
            productDAO.newProduct(new Product(36014, "Mining Collision Detector", date));
            productDAO.newProduct(new Product(45021, "Displacement Monitoring Device", date));
            productDAO.newProduct(new Product(67890, "Power Supply", date));
        }

        if (requiredPartsDAO.isTableEmpty()) {
            if (requiredPartsDAO.isTableEmpty()) {
                // Product 36014: Mining Collision Detector
                requiredPartsDAO.newRequiredParts(new RequiredParts(1, 36014, 10)); // 10 units of part ID 1
                requiredPartsDAO.newRequiredParts(new RequiredParts(2, 36014, 5));  // 5 units of part ID 2

                // Product 45021: Displacement Monitoring Device
                requiredPartsDAO.newRequiredParts(new RequiredParts(1, 45021, 8));  // 8 units of part ID 1
                requiredPartsDAO.newRequiredParts(new RequiredParts(3, 45021, 12)); // 12 units of part ID 3

                // Product 67890: Power Supply
                requiredPartsDAO.newRequiredParts(new RequiredParts(2, 67890, 6));  // 6 units of part ID 2
                requiredPartsDAO.newRequiredParts(new RequiredParts(3, 67890, 4));  // 4 units of part ID 3
            }

        }

        launch();
    }
}