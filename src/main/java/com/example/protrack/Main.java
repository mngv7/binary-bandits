package com.example.protrack;

import com.example.protrack.customer.Customer;
import com.example.protrack.customer.CustomerDAO;
import com.example.protrack.parts.Parts;
import com.example.protrack.parts.PartsDAO;
import com.example.protrack.products.*;
import com.example.protrack.users.ManagerialUser;
import com.example.protrack.users.ProductionUser;
import com.example.protrack.users.UsersDAO;
import com.example.protrack.users.WarehouseUser;
import com.example.protrack.workorder.WorkOrdersDAOImplementation;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.HashMap;
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
        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("application_logo.png")));
        stage.getIcons().add(icon);
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    public static int getWidth() {
        return WIDTH;
    }

    public static int getHeight() {
        return HEIGHT;
    }

    public static void main(String[] args) throws SQLException {

        ProductDAO productDAO = new ProductDAO();
        productDAO.createTable();

        PartsDAO partsDAO = new PartsDAO();
        partsDAO.createTable();

        BillOfMaterialsDAO billOfMaterial = new BillOfMaterialsDAO();
        billOfMaterial.createTable();

        TestRecordDAO testRecordDAO = new TestRecordDAO();
        testRecordDAO.createTable();

        UsersDAO usersDAO = new UsersDAO();
        usersDAO.createTable();

        CustomerDAO customerDAO = new CustomerDAO();
        customerDAO.createTable();

        if (usersDAO.isTableEmpty()) {
            usersDAO.newUser(new ManagerialUser(100, "John", "Doe", Date.valueOf("1985-01-01"), "john.doe@example.com", "0400125123", "Male", "password"));
            usersDAO.newUser(new ManagerialUser(101, "Alice", "Smith", Date.valueOf("1990-05-15"), "alice.smith@example.com", "0400123113", "Female", "alicepass"));
            usersDAO.newUser(new ManagerialUser(102, "Bob", "Johnson", Date.valueOf("1982-09-23"), "bob.johnson@example.com", "0400123723", "Male", "bobpass"));
            usersDAO.newUser(new WarehouseUser(103, "Charlie", "Brown", Date.valueOf("1978-11-30"), "charlie.brown@example.com", "0400129723", "Male", "charliepass"));
            usersDAO.newUser(new WarehouseUser(104, "Diana", "White", Date.valueOf("1987-03-12"), "diana.white@example.com", "0400123093", "Female", "dianapass"));
            usersDAO.newUser(new ProductionUser(105, "Eve", "Davis", Date.valueOf("1993-07-22"), "eve.davis@example.com", "0400473123", "Female", "evepass"));
            usersDAO.newUser(new ProductionUser(106, "Frank", "Miller", Date.valueOf("1989-12-09"), "frank.miller@example.com", "0400192123", "Male", "frankpass"));
        }

        if (testRecordDAO.isTableEmpty()) {
            // Product 36014: Mining Collision Detector
            testRecordDAO.newTestRecordStep(new TestRecord(1, 36014, 1, "Torque the T10 screw to 5Nm.", "CheckBox", "NULL"));
            testRecordDAO.newTestRecordStep(new TestRecord(2, 36014, 2, "Measure the thickness of the metal plate (mm).", "TextEntry", ">5"));
            testRecordDAO.newTestRecordStep(new TestRecord(3, 36014, 3, "Power on the device and check the operating voltage (V).", "TextEntry", ">10"));
            testRecordDAO.newTestRecordStep(new TestRecord(4, 36014, 4, "Power off the device and put into sleep mode for shipping.", "CheckBox", "NULL"));

            // Product 45021: Displacement Monitoring Device
            testRecordDAO.newTestRecordStep(new TestRecord(1, 45021, 1, "Inspect the exterior for any visible damage or scratches.", "CheckBox", "NULL"));
            testRecordDAO.newTestRecordStep(new TestRecord(2, 45021, 2, "Check that the battery is fully charged.", "CheckBox", "NULL"));
            testRecordDAO.newTestRecordStep(new TestRecord(3, 45021, 3, "Test the wireless connection strength (dBm).", "TextEntry", ">60"));
            testRecordDAO.newTestRecordStep(new TestRecord(4, 45021, 4, "Verify that the display is working without any dead pixels.", "CheckBox", "NULL"));
            testRecordDAO.newTestRecordStep(new TestRecord(5, 45021, 5, "Measure the device temperature after 10 minutes of operation (°C).", "TextEntry", "<40"));
            testRecordDAO.newTestRecordStep(new TestRecord(6, 45021, 6, "Ensure all screws are torqued to the specified value (Nm).", "CheckBox", "NULL"));
            testRecordDAO.newTestRecordStep(new TestRecord(7, 45021, 7, "Check that the firmware version is up to date.", "TextEntry", "Latest"));
            testRecordDAO.newTestRecordStep(new TestRecord(8, 45021, 8, "Power off the device and package it in the protective casing.", "CheckBox", "NULL"));

            // Product 67890: Power Supply
            testRecordDAO.newTestRecordStep(new TestRecord(1, 67890, 1, "Verify the serial number matches the product ID label.", "CheckBox", "NULL"));
            testRecordDAO.newTestRecordStep(new TestRecord(2, 67890, 2, "Check the power cable for any signs of wear.", "CheckBox", "NULL"));
            testRecordDAO.newTestRecordStep(new TestRecord(3, 67890, 3, "Test the main function of the device to ensure it operates correctly.", "CheckBox", "NULL"));
            testRecordDAO.newTestRecordStep(new TestRecord(4, 67890, 4, "Ensure all accessory components are included in the packaging.", "CheckBox", "NULL"));
        }

        if (productDAO.isTableEmpty()) {
            long millis = System.currentTimeMillis();
            java.sql.Date date = new java.sql.Date(millis);
            productDAO.newProduct(new Product(36014, "Mining Collision Detector", date));
            productDAO.newProduct(new Product(45021, "Displacement Monitoring Device", date));
            productDAO.newProduct(new Product(67890, "Power Supply", date));
        }

        if (billOfMaterial.isTableEmpty()) {
            if (billOfMaterial.isTableEmpty()) {
                // Product 36014: Mining Collision Detector
                billOfMaterial.newRequiredParts(new BillOfMaterials(1, 36014, 10)); // 10 units of part ID 1
                billOfMaterial.newRequiredParts(new BillOfMaterials(2, 36014, 5));  // 5 units of part ID 2

                // Product 45021: Displacement Monitoring Device
                billOfMaterial.newRequiredParts(new BillOfMaterials(1, 45021, 8));  // 8 units of part ID 1
                billOfMaterial.newRequiredParts(new BillOfMaterials(3, 45021, 12)); // 12 units of part ID 3

                // Product 67890: Power Supply
                billOfMaterial.newRequiredParts(new BillOfMaterials(2, 67890, 6));  // 6 units of part ID 2
                billOfMaterial.newRequiredParts(new BillOfMaterials(3, 67890, 4));  // 4 units of part ID 3
            }
        }

        if (partsDAO.isTableEmpty()) {
            partsDAO.newPart(new Parts(50, "TestPart", "Testing", 50, 12.50));
            partsDAO.newPart(new Parts(51, "TestPart2", "Testing2", 50, 6.69));
            partsDAO.newPart(new Parts(1, "AA batteries", "Batteries from Japan", 50, 5.50));
            partsDAO.newPart(new Parts(3, "Stainless Steel", "Stainless Steel from Bob Industry", 51, 2.10));
        }

        HashMap<Integer, ProductionUser> productionUsers = usersDAO.getProductionUsers();
        HashMap<Integer, Customer> customers = customerDAO.getAllCustomers();
        System.out.println(productionUsers.keySet());
        System.out.println(customers.keySet());
        WorkOrdersDAOImplementation wdao =  new WorkOrdersDAOImplementation(productionUsers, customers);
        wdao.createTable();
        ///wdao.createWorkOrder(new WorkOrder(100, productionUsers.get(105), customers.get(1), LocalDateTime.now(), null, "shipAdd", 1, "pending", 40.87));
        System.out.print(wdao.getAllWorkOrders());

        launch();
    }
}