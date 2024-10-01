package com.example.protrack;

import com.example.protrack.customer.Customer;
import com.example.protrack.customer.CustomerDAO;
import com.example.protrack.parts.Parts;
import com.example.protrack.parts.PartsDAO;
import com.example.protrack.productbuild.ProductBuild;
import com.example.protrack.productbuild.ProductBuildDAO;
import com.example.protrack.products.*;
import com.example.protrack.requests.Requests;
import com.example.protrack.requests.RequestsDAO;
import com.example.protrack.timesheets.Timesheets;
import com.example.protrack.timesheets.TimesheetsDAO;
import com.example.protrack.users.ManagerialUser;
import com.example.protrack.users.ProductionUser;
import com.example.protrack.users.UsersDAO;
import com.example.protrack.users.WarehouseUser;
import com.example.protrack.warehouseutil.*;
import com.example.protrack.workorder.WorkOrder;
import com.example.protrack.workorder.WorkOrdersDAOImplementation;
import com.example.protrack.workorderproducts.WorkOrderProductsDAOImplementation;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import javax.xml.stream.Location;
import java.sql.Date;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Main extends Application {

    private static final String TITLE = "ProTrack";
    private static final int WIDTH = 1920;
    private static final int HEIGHT = 1080;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), WIDTH, HEIGHT);
        String stylesheet = Objects.requireNonNull(Main.class.getResource("stylesheet.css")).toExternalForm();
        scene.getStylesheets().add(stylesheet);
        stage.setTitle(TITLE);
        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("Images/application_logo.png")));
        stage.getIcons().add(icon);
        stage.setScene(scene);
        stage.setMaximized(false);
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
        if (productDAO.isTableEmpty()) {
            long millis = System.currentTimeMillis();
            java.sql.Date date = new java.sql.Date(millis);
            productDAO.newProduct(new Product(36014, "Mining Collision Detector", date));
            productDAO.newProduct(new Product(45021, "Displacement Monitoring Device", date));
            productDAO.newProduct(new Product(67890, "Power Supply", date));
        }

        PartsDAO partsDAO = new PartsDAO();
        partsDAO.createTable();
        if (partsDAO.isTableEmpty()) {
            partsDAO.newPart(new Parts(50, "TestPart", "Testing", 50, 12.50));
            partsDAO.newPart(new Parts(51, "TestPart2", "Testing2", 50, 6.69));
            partsDAO.newPart(new Parts(1, "AA batteries", "Batteries from Japan", 50, 5.50));
            partsDAO.newPart(new Parts(2, "Wooden Panel", "Panels from Tom's workshop", 52, 3.75));
            partsDAO.newPart(new Parts(3, "Stainless Steel", "Stainless Steel from Bob Industry", 51, 2.10));
            partsDAO.newPart(new Parts(4, "Australium Cables", "Cables from Mann.co", 53, 52.1));
            partsDAO.newPart(new Parts(5, "Glass Panel", "Panels from Bob Industry", 51, 20.50));
        }

        BillOfMaterialsDAO billOfMaterial = new BillOfMaterialsDAO();
        billOfMaterial.createTable();
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

        TestRecordDAO testRecordDAO = new TestRecordDAO();
        testRecordDAO.createTable();
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

        TimesheetsDAO timesheetsDAO = new TimesheetsDAO();
        timesheetsDAO.createTable();
        if (timesheetsDAO.isTableEmpty()) {
            timesheetsDAO.newTimesheet(new Timesheets(LocalDateTime.of(2024, 10, 1, 10, 00, 00, 00), LocalDateTime.of(2024, 10, 1, 14, 30, 00, 00), 100, 1));
            timesheetsDAO.newTimesheet(new Timesheets(LocalDateTime.of(2024, 9, 1, 10,00, 00, 00), LocalDateTime.of(2024, 9, 1, 14, 30, 0, 0), 100, 2));
        }

        UsersDAO usersDAO = new UsersDAO();
        usersDAO.createTable();
        if (usersDAO.isTableEmpty()) {
            usersDAO.newUser(new ManagerialUser(100, "John", "Doe", Date.valueOf("1985-01-01"), "john.doe@example.com", "0400125123", "Male", "password"));
            usersDAO.newUser(new ManagerialUser(101, "Alice", "Smith", Date.valueOf("1990-05-15"), "alice.smith@example.com", "0400123113", "Female", "alicepass"));
            usersDAO.newUser(new ManagerialUser(102, "Bob", "Johnson", Date.valueOf("1982-09-23"), "bob.johnson@example.com", "0400123723", "Male", "bobpass"));
            usersDAO.newUser(new WarehouseUser(103, "Charlie", "Brown", Date.valueOf("1978-11-30"), "charlie.brown@example.com", "0400129723", "Male", "charliepass"));
            usersDAO.newUser(new WarehouseUser(104, "Diana", "White", Date.valueOf("1987-03-12"), "diana.white@example.com", "0400123093", "Female", "dianapass"));
            usersDAO.newUser(new ProductionUser(105, "Eve", "Davis", Date.valueOf("1993-07-22"), "eve.davis@example.com", "0400473123", "Female", "evepass"));
            usersDAO.newUser(new ProductionUser(106, "Frank", "Miller", Date.valueOf("1989-12-09"), "frank.miller@example.com", "0400192123", "Male", "frankpass"));
            usersDAO.newUser(new WarehouseUser(107, "Grace", "Harris", Date.valueOf("1992-12-11"), "grace.harris@example.com", "0400987654", "Female", "gracepass"));
            usersDAO.newUser(new ProductionUser(108, "Henry", "Martinez", Date.valueOf("1985-08-05"), "henry.martinez@example.com", "0400543210", "Male", "henrypass"));
            usersDAO.newUser(new WarehouseUser(109, "Isabella", "Garcia", Date.valueOf("1991-06-19"), "isabella.garcia@example.com", "0400789345", "Female", "isabellapass"));
            usersDAO.newUser(new ManagerialUser(110, "Jack", "Wilson", Date.valueOf("1975-02-28"), "jack.wilson@example.com", "0400129876", "Male", "jackpass"));
        }

        List<ProductionUser> productionUsers = usersDAO.getProductionUsers();

        CustomerDAO customerDAO = new CustomerDAO();
        customerDAO.createTable();
        if (customerDAO.isTableEmpty()) {
            customerDAO.addCustomer(new Customer(1, "Jane", "Doe", "jane.doe@example.com", "0400187362", "billingAddress", "shippingAddress", "Active"));
        }
        List<Customer> customers = customerDAO.getAllCustomers();

        WorkOrdersDAOImplementation wdao =  new WorkOrdersDAOImplementation(productionUsers, customers);
        wdao.createTable();
        if (wdao.isTableEmpty()) {
            wdao.createWorkOrder(new WorkOrder(100, productionUsers.getFirst(), customers.getFirst(), LocalDateTime.now(), null, "shipAdd", "Pending", 40.87));
        }

        LocationsAndContentsDAO locationsAndContentsDAO = new LocationsAndContentsDAO();
        locationsAndContentsDAO.createTables();

        if (locationsAndContentsDAO.isLocationsTableEmpty()) {
            locationsAndContentsDAO.newWarehouse(new RealWarehouse(0, "Lotus", 5000));
            locationsAndContentsDAO.newWorkstation(new RealWorkstation(1, "Workstation 1", 300));
            locationsAndContentsDAO.newWorkstation(new RealWorkstation(2, "Workstation 2", 40));
            locationsAndContentsDAO.newWorkstation(new RealWorkstation(3, "Workstation 3", 54));
            locationsAndContentsDAO.newWorkstation(new RealWorkstation(4, "Workstation 4", 255));
        }

        partIdWithQuantity testPart1 = new partIdWithQuantity();
        partIdWithQuantity testPart2 = new partIdWithQuantity();
        partIdWithQuantity testPart3 = new partIdWithQuantity();
        partIdWithQuantity testPart4 = new partIdWithQuantity();
        partIdWithQuantity testPart5 = new partIdWithQuantity();

        testPart1.partsId = 1;
        testPart1.quantity = 7;
        testPart2.partsId = 3;
        testPart2.quantity = 10;
        testPart3.partsId = 2;
        testPart3.quantity = 12;
        testPart4.partsId = 4;
        testPart4.quantity = 4;
        testPart5.partsId = 5;
        testPart5.quantity = 8;

        if (locationsAndContentsDAO.isLocationContentsTableEmpty()) {
            locationsAndContentsDAO.insertPartsIdWithQuantityIntoLocation(0, testPart1);
            locationsAndContentsDAO.insertPartsIdWithQuantityIntoLocation(0, testPart2);
            locationsAndContentsDAO.insertPartsIdWithQuantityIntoLocation(0, testPart3);
            locationsAndContentsDAO.insertPartsIdWithQuantityIntoLocation(0, testPart4);
            locationsAndContentsDAO.insertPartsIdWithQuantityIntoLocation(0, testPart5);

            locationsAndContentsDAO.insertPartsIdWithQuantityIntoLocation(1, testPart1);
            locationsAndContentsDAO.insertPartsIdWithQuantityIntoLocation(1, testPart3);
            locationsAndContentsDAO.insertPartsIdWithQuantityIntoLocation(1, testPart5);

            locationsAndContentsDAO.insertPartsIdWithQuantityIntoLocation(2, testPart2);
            locationsAndContentsDAO.insertPartsIdWithQuantityIntoLocation(2, testPart4);

            //
            //System.out.println("Warning: location Contents table created empty intentionally.");
        }

        RequestsDAO requestsDAO = new RequestsDAO();
        requestsDAO.createTable();

        if (requestsDAO.isTableEmpty()) {
            requestsDAO.newRequest(new Requests(1, 1, 1, 1));
            requestsDAO.newRequest(new Requests(1, 2, 2, 7));
            requestsDAO.newRequest(new Requests(1, 5, 3, 3));
            requestsDAO.newRequest(new Requests(2, 1, 4, 2));
            requestsDAO.newRequest(new Requests(2, 3, 5, 5));
            requestsDAO.newRequest(new Requests(2, 1, 6, 3));
            requestsDAO.newRequest(new Requests(2, 5, 7, 6));
            requestsDAO.newRequest(new Requests(1, 2, 8, 10));
            requestsDAO.newRequest(new Requests(1, 4, 9, 2));
            requestsDAO.newRequest(new Requests(3, 1, 10, 3));
        }

        WorkOrderProductsDAOImplementation workOrderProductsDAOImplementation = new WorkOrderProductsDAOImplementation();
        workOrderProductsDAOImplementation.createTable();

        ProductBuildDAO productBuildDAO = new ProductBuildDAO();
        productBuildDAO.createTable();

        if (productBuildDAO.isTableEmpty()) {
            productBuildDAO.newProductBuild(new ProductBuild(500, 1, 0.00F, 36014));
            productBuildDAO.newProductBuild(new ProductBuild(501, 1, 0.00F, 45021));
            productBuildDAO.newProductBuild(new ProductBuild(502, 1, 0.00F, 67890));
            productBuildDAO.newProductBuild(new ProductBuild(503, 2, 0.00F, 36014));
            productBuildDAO.newProductBuild(new ProductBuild(504, 2, 0.00F, 45021));
            productBuildDAO.newProductBuild(new ProductBuild(505, 3, 0.00F, 67890));

        }

        launch();
    }
}